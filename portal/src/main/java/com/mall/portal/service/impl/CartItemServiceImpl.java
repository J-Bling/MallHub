package com.mall.portal.service.impl;

import com.mall.common.constant.enums.PromotionTypeEnum;
import com.mall.common.exception.Assert;
import com.mall.mbg.mapper.OmsCartItemMapper;
import com.mall.mbg.model.*;
import com.mall.portal.dao.OrdersDao;
import com.mall.portal.domain.model.product.CartProduct;
import com.mall.portal.domain.model.product.PromotionCartItem;
import com.mall.portal.service.CartItemService;
import com.mall.portal.service.ConsumerService;
import com.mall.portal.service.CouponService;
import com.mall.portal.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
public class CartItemServiceImpl implements CartItemService {
    @Autowired private ConsumerService consumerService;
    @Autowired private OmsCartItemMapper cartItemMapper;
    @Autowired private OrdersDao ordersDao;
    @Autowired private ProductService productService;
    @Autowired private CouponService couponService;

    @Override
    @Transactional
    public void add(OmsCartItem cartItem) {
        if (cartItem.getProductId()==null || cartItem.getProductSkuId()==null){
            Assert.fail("参数错误");
        }
        UmsMember member = consumerService.getCurrentMember();
        OmsCartItemExample example = new OmsCartItemExample();
        example.createCriteria().andMemberIdEqualTo(member.getId())
                .andProductIdEqualTo(cartItem.getProductId())
                .andProductSkuIdEqualTo(cartItem.getProductSkuId());
        List<OmsCartItem> cartItemList = cartItemMapper.selectByExample(example);
        if (cartItemList!=null && !cartItemList.isEmpty()){
            cartItem = cartItemList.get(0);
            ordersDao.incrementCartQuantityAndModify(cartItem.getId(),1,new Date());
        }else {
            cartItem.setMemberId(member.getId());
            cartItem.setMemberNickname(member.getNickname());
            cartItem.setDeleteStatus(0);
            cartItem.setCreateDate(new Date());
            cartItemMapper.insert(cartItem);
        }
    }

    @Override
    public List<OmsCartItem> list() {
        UmsMember member = consumerService.getCurrentMember();
        OmsCartItemExample example = new OmsCartItemExample();
        example.createCriteria().andMemberIdEqualTo(member.getId());
        return cartItemMapper.selectByExample(example);
    }

    @Override
    public void updateQuantity(Long cartId, Integer quantity) {
        UmsMember member = consumerService.getCurrentMember();
        OmsCartItem cartItem = new OmsCartItem();
        cartItem.setQuantity(quantity);
        OmsCartItemExample example = new OmsCartItemExample();
        example.createCriteria().andMemberIdEqualTo(member.getId())
                .andIdEqualTo(cartId)
                .andDeleteStatusEqualTo(0);
        cartItemMapper.updateByExampleSelective(cartItem,example);
    }

    @Override
    public void delete(List<Long> cartIds) {
        OmsCartItemExample example = new OmsCartItemExample();
        example.createCriteria().andIdIn(cartIds);
        cartItemMapper.deleteByExample(example);
    }

    @Override
    public CartProduct getCartProduct(Long productId) {
        PmsProduct product = this.productService.getProduct(productId);
        if (product==null) {
            return null;
        }
        CartProduct cartProduct = new CartProduct();
        BeanUtils.copyProperties(product,cartProduct);
        cartProduct.setProductAttributeList(productService.getProductAttribute(productId));
        cartProduct.setPmsSkuStockList(productService.getSkuStock(productId));
        return cartProduct;
    }

    @Override
    public void updateAttribute(OmsCartItem cartItem) {
        if (cartItem.getMemberId()==null){
            Assert.fail("参数错误");
        }
        OmsCartItemExample example = new OmsCartItemExample();
        example.createCriteria().andIdEqualTo(cartItem.getId())
                .andMemberIdEqualTo(cartItem.getMemberId());
        cartItem.setModifyDate(new Date());
        cartItemMapper.updateByExampleSelective(cartItem,example);
    }

    @Override
    public List<PromotionCartItem> promotionList(List<Long> cartIds) {
        if (cartIds==null || cartIds.isEmpty()){
            return null;
        }
        List<PromotionCartItem> promotionCartItemList = new ArrayList<>();
        List<OmsCartItem> cartItemList = new ArrayList<>();
        for (Long id : cartIds){
            OmsCartItem cartItem = cartItemMapper.selectByPrimaryKey(id);
            if (cartItem!=null){
                cartItemList.add(cartItem);
            }
        }
        for (OmsCartItem cartItem : cartItemList){
            PromotionCartItem promotionCartItem = new PromotionCartItem();
            promotionCartItem.setCartId(cartItem.getId());
            //获取商品
            PmsProduct product = this.productService.getProduct(cartItem.getProductId());
            if (product==null){
                continue;
            }
            // 购买数量
            Integer quantity = cartItem.getQuantity();
            //获得成长值
            promotionCartItem.setGrowth(product.getGiftGrowth()*quantity);
            //获得积分
            int integration = product.getGiftPoint()*quantity;
            promotionCartItem.setIntegration(integration > product.getUsePointLimit() ? product.getUsePointLimit() : integration);
            //获取 促销信息
            Integer promotionType = product.getPromotionType();
            //原价购买
            BigDecimal cartItemPrice = new BigDecimal(quantity).multiply(product.getPrice());
            //促销减少的金额
            BigDecimal reductionPrice = new BigDecimal("0.00");
            //促销信息
            String promotionMessage = "";
            if (PromotionTypeEnum.PROMOTION_PRICE.getCode().equals(promotionType)){
                //使用促销价促销
                //获取库存
                PmsSkuStock skuStock = this.productService.getSkuStock(cartItem.getProductSkuId(),cartItem.getProductId());
                if (skuStock!=null){
                    promotionCartItem.setRealStock(skuStock.getStock());
                    reductionPrice = skuStock.getPrice().subtract(skuStock.getPromotionPrice()).multiply(new BigDecimal(quantity));
                    promotionMessage = "商品促销减价";
                }
            }else if (PromotionTypeEnum.TIERED_PRICE.getCode().equals(promotionType)){
                //数量促销
                List<PmsProductLadder> productLadderList = this.productService.getProductLadders(product.getId());
                PmsProductLadder ladder = this.computeLadderList(quantity,productLadderList);
                if (ladder!=null){
                    reductionPrice = cartItemPrice.subtract(cartItemPrice.multiply(ladder.getDiscount()));
                    promotionMessage = "满"+quantity+"件可享打"+ladder.getDiscount().multiply(new BigDecimal("10"))+"折优惠";
                }

            }else if (PromotionTypeEnum.FULL_REDUCTION_PRICE.getCode().equals(promotionType)){
                //获取商品的减满
                List<PmsProductFullReduction> productFullReductionList = this.productService.getProductFullReductions(product.getId());
                PmsProductFullReduction fullReduction = this.computeFullReduction(cartItemPrice,productFullReductionList);
                reductionPrice = fullReduction.getReducePrice();
                promotionMessage = "减满优惠 :"+"满"+fullReduction.getFullPrice()+" 元"+" 减免"+fullReduction.getReducePrice()+"元";

            }else if (PromotionTypeEnum.ORIGINAL_PRICE.getCode().equals(promotionType)){
                //原价购买
                promotionMessage = "原价购买";
            }

            promotionCartItem.setReduceAmount(reductionPrice);
            promotionCartItem.setPromotionMessage(promotionMessage);
            promotionCartItemList.add(promotionCartItem);
        }
        return promotionCartItemList;
    }

    /**
     *获得减满条件计算减满后价格
     */
    private PmsProductFullReduction computeFullReduction(BigDecimal lastPrice,List<PmsProductFullReduction> fullReductionList){
        if (fullReductionList==null || fullReductionList.isEmpty()){
            return null;
        }
        return fullReductionList.stream()
                .filter(full->lastPrice.compareTo(full.getFullPrice())>=0)
                .max(new Comparator<PmsProductFullReduction>() {
                    @Override
                    public int compare(PmsProductFullReduction o1, PmsProductFullReduction o2) {
                        return o1.getReducePrice().compareTo(o2.getReducePrice());
                    }
                }).orElse(null);
    }

    /**
     * 获得打折条件后计算打折后价格
     */
    private PmsProductLadder computeLadderList(Integer count,List<PmsProductLadder> ladderList){
        if(ladderList==null || ladderList.isEmpty() || count<=0){
            return null;
        }
        return ladderList.stream()
                .filter(ladder->ladder.getCount()<=count)
                .max(new Comparator<PmsProductLadder>() {
                    @Override
                    public int compare(PmsProductLadder o1, PmsProductLadder o2) {
                        return o1.getCount() - o2.getCount();
                    }
                }).orElse(null);
    }


    @Override
    public void clear() {
        UmsMember member = consumerService.getCurrentMember();
        OmsCartItemExample example = new OmsCartItemExample();
        example.createCriteria().andMemberIdEqualTo(member.getId());
        cartItemMapper.deleteByExample(example);
    }
}