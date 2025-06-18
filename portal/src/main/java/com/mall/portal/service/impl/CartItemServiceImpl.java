package com.mall.portal.service.impl;

import com.mall.common.exception.Assert;
import com.mall.mbg.mapper.OmsCartItemMapper;
import com.mall.mbg.model.*;
import com.mall.portal.dao.OrdersDao;
import com.mall.portal.domain.model.CartProduct;
import com.mall.portal.domain.model.PromotionCartItem;
import com.mall.portal.service.CartItemService;
import com.mall.portal.service.ConsumerService;
import com.mall.portal.service.CouponService;
import com.mall.portal.service.PortalProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CartItemServiceImpl implements CartItemService {
    @Autowired private ConsumerService consumerService;
    @Autowired private OmsCartItemMapper cartItemMapper;
    @Autowired private OrdersDao ordersDao;
    @Autowired private PortalProductService productService;
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
    public List<OmsCartItem> list(int offset,int limit) {
        UmsMember member = consumerService.getCurrentMember();
        OmsCartItemExample example = new OmsCartItemExample();
        example.createCriteria().andMemberIdEqualTo(member.getId());
        example.setOrderByClause("id asc limit "+offset+" ,"+limit);
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
        //获取用户所有优惠券
        List<CouponService.UserCoupons> userCouponsList = this.couponService.getUserCoupons();
        for (Long id : cartIds){
            OmsCartItem cartItem = cartItemMapper.selectByPrimaryKey(id);
            if (cartItem!=null){
                cartItemList.add(cartItem);
            }
        }
        for (OmsCartItem cartItem : cartItemList){
            PromotionCartItem promotionCartItem = new PromotionCartItem();
            promotionCartItem.setCartId(cartItem.getId());
            //获取库存
            PmsSkuStock stock = this.productService.getSkuStock(cartItem.getProductSkuId(),cartItem.getProductId());
            if (stock!=null){
                promotionCartItem.setRealStock(stock.getStock());
            }
            //获取商品
            PmsProduct product = this.productService.getProduct(cartItem.getProductId());
            if (product==null){
                continue;
            }
            //统计价格
            BigDecimal cartItemPrice = new BigDecimal(cartItem.getQuantity()).multiply(product.getPrice());
            BigDecimal reductionPrice = new BigDecimal("0.00");
            promotionCartItem.setGrowth(product.getGiftGrowth());
            promotionCartItem.setIntegration(product.getGiftPoint());
            //获取商品的减满
            List<PmsProductFullReduction> productFullReductionList = this.productService.getProductFullReductions(product.getId());
            BigDecimal fullReductionPrice = this.computeFullReduction(cartItemPrice,productFullReductionList);
            //获取商品价格阶梯
            List<PmsProductLadder> productLadderList = this.productService.getProductLadders(product.getId());
            BigDecimal ladderPrice = this.computeLadderList(cartItemPrice,cartItem.getQuantity(),productLadderList);
            //计的减免金额
            reductionPrice =reductionPrice.add(cartItemPrice.subtract(fullReductionPrice)).add(cartItemPrice.subtract(ladderPrice));
            promotionCartItem.setReduceAmount(reductionPrice);
            //获得促销信息


            promotionCartItemList.add(promotionCartItem);
        }
        return promotionCartItemList;
    }

    /**
     *获得减满条件计算减慢后价格
     */
    private BigDecimal computeFullReduction(BigDecimal lastPrice,List<PmsProductFullReduction> fullReductionList){
        if (fullReductionList==null || fullReductionList.isEmpty()){
            return lastPrice;
        }
        return fullReductionList.stream()
                .filter(full->lastPrice.compareTo(full.getFullPrice())>=0)
                .map(full->lastPrice.subtract(full.getReducePrice()))
                .max(BigDecimal::compareTo)
                .orElse(lastPrice);
    }

    /**
     * 获得打折条件后计算打折后价格
     */
    private BigDecimal computeLadderList(BigDecimal lastPrice,Integer count,List<PmsProductLadder> ladderList){
        if(ladderList==null || ladderList.isEmpty() || count<=0){
            return lastPrice;
        }
        return ladderList.stream()
                .filter(ladder->ladder.getCount()<=count)
                .map(ladder->lastPrice.multiply(ladder.getDiscount()))
                .max(BigDecimal::compareTo)
                .orElse(lastPrice);
    }


    @Override
    public void clear() {
        UmsMember member = consumerService.getCurrentMember();
        OmsCartItemExample example = new OmsCartItemExample();
        example.createCriteria().andMemberIdEqualTo(member.getId());
        cartItemMapper.deleteByExample(example);
    }
}