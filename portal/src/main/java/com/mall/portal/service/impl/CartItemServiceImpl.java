package com.mall.portal.service.impl;

import com.mall.common.exception.Assert;
import com.mall.mbg.mapper.OmsCartItemMapper;
import com.mall.mbg.model.OmsCartItem;
import com.mall.mbg.model.OmsCartItemExample;
import com.mall.mbg.model.PmsProduct;
import com.mall.mbg.model.UmsMember;
import com.mall.portal.dao.OrdersDao;
import com.mall.portal.domain.model.CartProduct;
import com.mall.portal.domain.model.PromotionCartItem;
import com.mall.portal.service.CartItemService;
import com.mall.portal.service.ConsumerService;
import com.mall.portal.service.PortalProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class CartItemServiceImpl implements CartItemService {
    @Autowired private ConsumerService consumerService;
    @Autowired private OmsCartItemMapper cartItemMapper;
    @Autowired private OrdersDao ordersDao;
    @Autowired private PortalProductService productService;

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
        return null;
    }

    @Override
    public void clear() {
        UmsMember member = consumerService.getCurrentMember();
        OmsCartItemExample example = new OmsCartItemExample();
        example.createCriteria().andMemberIdEqualTo(member.getId());
        cartItemMapper.deleteByExample(example);
    }
}