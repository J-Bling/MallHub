package com.mall.portal.service.impl;

import com.mall.common.constant.enums.CouponUseTypeEnum;
import com.mall.common.constant.enums.UseCouponStatusEnum;
import com.mall.common.exception.ApiException;
import com.mall.common.exception.Assert;
import com.mall.mbg.mapper.SmsCouponHistoryMapper;
import com.mall.mbg.mapper.SmsCouponMapper ;
import com.mall.mbg.mapper.SmsCouponProductCategoryRelationMapper;
import com.mall.mbg.mapper.SmsCouponProductRelationMapper;
import com.mall.mbg.mapper.PmsProductMapper;
import com.mall.mbg.model.*;
import com.mall.portal.dao.CouponDao;
import com.mall.common.constant.enums.CouponRedemptionMethodEnum;
import com.mall.portal.domain.model.PromotionCartItem;
import com.mall.portal.domain.model.CouponHistoryDetail;
import com.mall.portal.service.CouponService;
import com.mall.portal.service.ConsumerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CouponServiceImpl implements CouponService {

    @Autowired private ConsumerService consumerService;
    @Autowired private SmsCouponMapper couponMapper;
    @Autowired private SmsCouponHistoryMapper couponHistoryMapper;
    @Autowired private SmsCouponProductRelationMapper productRelationMapper;
    @Autowired private SmsCouponProductCategoryRelationMapper productCategoryRelationMapper;
    @Autowired private PmsProductMapper productMapper;
    @Autowired private CouponDao couponDao;

    private final Logger logger = LoggerFactory.getLogger(CouponServiceImpl.class);

    @Override
    @Transactional
    public void add(Long couponId) {
        UmsMember member = consumerService.getCurrentMember();
        SmsCoupon coupon = couponMapper.selectByPrimaryKey(couponId);
        if (coupon==null){
            Assert.fail("优惠券不存在");
        }
        if (coupon.getCount() < 1){
            Assert.fail("优惠券发放完毕");
        }
        Date date = new Date();
        if (coupon.getEnableTime()==null || date.before(coupon.getEnableTime())){
            Assert.fail("优惠券还没有到达领取时间");
        }
        if (coupon.getEndTime()==null || date.after(coupon.getEndTime())){
            Assert.fail("优惠券已经过了领取时间");
        }
        SmsCouponHistoryExample historyExample = new SmsCouponHistoryExample();
        historyExample.createCriteria().andMemberIdEqualTo(member.getId()).andCouponIdEqualTo(couponId);
        long count = couponHistoryMapper.countByExample(historyExample);
        if (count >= coupon.getPerLimit()){
            Assert.fail("你领取的优惠券已经到达限额");
        }
        SmsCouponHistory couponHistory = new CouponHistoryDetail();
        couponHistory.setCouponId(couponId);
        couponHistory.setMemberId(member.getId());
        couponHistory.setCouponCode(generateCouponCode(member.getId()));
        couponHistory.setCreateTime(date);
        couponHistory.setMemberNickname(member.getNickname());
        couponHistory.setGetType(CouponRedemptionMethodEnum.ONLINE_CLAIM.getCode());
        couponHistory.setUseStatus(UseCouponStatusEnum.UNUSED.getCode());
        try {
            int insertCount = couponHistoryMapper.insert(couponHistory);
            if (insertCount < 1) {
                logger.error("插入 couponHistory 失败 memberId:{}", member.getId());
                Assert.fail("领取优惠券失败");
            }
            couponDao.updateCouponCountReceiveCount(couponId,-1,1);
        }catch (ApiException apiException){
            throw apiException;
        }catch (Exception e){
            logger.error("插入couponHistory发送异常 : {}",e.getMessage());
            Assert.fail("领取优惠券失败");
        }
    }


    /**
     * 16位优惠码生成：时间戳后8位+4位随机数+用户id后4位
     */
    private String generateCouponCode(Long memberId) {
        StringBuilder sb = new StringBuilder();
        long currentTimeMillis = System.currentTimeMillis();
        String timeMillisStr = Long.toString(currentTimeMillis);
        sb.append(timeMillisStr.substring(timeMillisStr.length() - 8));
        for (int i = 0; i < 4; i++) {
            sb.append(new Random().nextInt(10));
        }
        String memberIdStr = memberId.toString();
        if (memberIdStr.length() <= 4) {
            sb.append(String.format("%04d", memberId));
        } else {
            sb.append(memberIdStr.substring(memberIdStr.length()-4));
        }
        return sb.toString();
    }

    @Override
    public List<SmsCouponHistory> listHistory(Integer useStatus) {
        UmsMember member = consumerService.getCurrentMember();
        SmsCouponHistoryExample historyExample = new SmsCouponHistoryExample();
        historyExample.createCriteria().andMemberIdEqualTo(member.getId());
        return couponHistoryMapper.selectByExample(historyExample);
    }

    @Override
    public List<CouponHistoryDetail> listCart(List<PromotionCartItem> cartItemList, Integer type) {
        UmsMember currentMember = consumerService.getCurrentMember();
        Date now = new Date();
        //获取该用户所有优惠券
        List<CouponHistoryDetail> allList = couponDao.getDetailList(currentMember.getId());
        //根据优惠券使用类型来判断优惠券是否可用
        List<CouponHistoryDetail> enableList = new ArrayList<>();
        List<CouponHistoryDetail> disableList = new ArrayList<>();
        BigDecimal totalAmountAll = calcTotalAmount(cartItemList);
        for (CouponHistoryDetail couponHistoryDetail : allList) {
            Integer useType = couponHistoryDetail.getCoupon().getUseType();
            BigDecimal minPoint = couponHistoryDetail.getCoupon().getMinPoint();
            Date endTime = couponHistoryDetail.getCoupon().getEndTime();
            if(useType.equals(CouponUseTypeEnum.ALL.getCode())){
                //0->全场通用
                //判断是否满足优惠起点
                //计算购物车商品的总价
                if(now.before(endTime)&&totalAmountAll.subtract(minPoint).floatValue()>=0){
                    enableList.add(couponHistoryDetail);
                }else{
                    disableList.add(couponHistoryDetail);
                }
            }else if(useType.equals(CouponUseTypeEnum.CATEGORY.getCode())){
                //1->指定分类
                //计算指定分类商品的总价
                List<Long> productCategoryIds = new ArrayList<>();
                for (SmsCouponProductCategoryRelation categoryRelation : couponHistoryDetail.getCategoryRelationList()) {
                    productCategoryIds.add(categoryRelation.getProductCategoryId());
                }
                BigDecimal totalAmount = calcTotalAmountByproductCategoryId(cartItemList,productCategoryIds);
                if(now.before(endTime)&&totalAmount.floatValue()>0&&totalAmount.subtract(minPoint).floatValue()>=0){
                    enableList.add(couponHistoryDetail);
                }else{
                    disableList.add(couponHistoryDetail);
                }
            }else if(useType.equals(CouponUseTypeEnum.PRODUCT.getCode())){
                //2->指定商品
                //计算指定商品的总价
                List<Long> productIds = new ArrayList<>();
                for (SmsCouponProductRelation productRelation : couponHistoryDetail.getProductRelationList()) {
                    productIds.add(productRelation.getProductId());
                }
                BigDecimal totalAmount = calcTotalAmountByProductId(cartItemList,productIds);
                if(now.before(endTime)&&totalAmount.floatValue()>0&&totalAmount.subtract(minPoint).floatValue()>=0){
                    enableList.add(couponHistoryDetail);
                }else{
                    disableList.add(couponHistoryDetail);
                }
            }
        }
        if(type.equals(1)){
            return enableList;
        }else{
            return disableList;
        }
    }

    @Override
    public List<SmsCoupon> listByProduct(Long productId) {
        PmsProduct product = productMapper.selectByPrimaryKey(productId);
        if (product==null){
            return null;
        }
        List<Long> coupons = new ArrayList<>();
        //获取指定商品优惠券
        SmsCouponProductRelationExample relationExample = new SmsCouponProductRelationExample();
        relationExample.createCriteria().andProductIdEqualTo(productId);
        List<SmsCouponProductRelation> productRelationList = productRelationMapper.selectByExample(relationExample);
        if (productRelationList!=null && !productRelationList.isEmpty()){
            List<Long> coupons1= productRelationList.stream().map(SmsCouponProductRelation::getCouponId).collect(Collectors.toList());
            coupons.addAll(coupons1);
        }
        //获取指定分类优惠券
        Long categoryId = product.getProductCategoryId();
        SmsCouponProductCategoryRelationExample productCategoryRelationExample = new SmsCouponProductCategoryRelationExample();
        productCategoryRelationExample.createCriteria().andProductCategoryIdEqualTo(categoryId);
        List<SmsCouponProductCategoryRelation> productCategoryRelationList = productCategoryRelationMapper.selectByExample(productCategoryRelationExample);
        if (productCategoryRelationList!=null && !productCategoryRelationList.isEmpty()){
            List<Long> coupons2 = productCategoryRelationList.stream().map(SmsCouponProductCategoryRelation::getCouponId).collect(Collectors.toList());
            coupons.addAll(coupons2);
        }

        SmsCouponExample example = new SmsCouponExample();
        Date date = new Date();
        example.createCriteria().andEndTimeGreaterThan(date)
                .andStartTimeLessThan(date)
                .andUseTypeEqualTo(CouponUseTypeEnum.ALL.getCode());
        if (!coupons.isEmpty()){
            example.or(example.createCriteria()
                    .andStartTimeLessThan(date)
                    .andEndTimeGreaterThan(date)
                    .andUseTypeNotEqualTo(CouponUseTypeEnum.ALL.getCode())
                    .andIdIn(coupons)
            );
        }
        return couponMapper.selectByExample(example);
    }

    @Override
    public List<SmsCoupon> list(Integer useStatus) {
        UseCouponStatusEnum useCouponStatusEnum = UseCouponStatusEnum.formCode(useStatus);
        UmsMember member = consumerService.getCurrentMember();
        return couponDao.getListByUseStatus(member.getId(),useCouponStatusEnum.getCode());
    }


    private BigDecimal calcTotalAmount(List<PromotionCartItem> cartItemList) {
        BigDecimal total = new BigDecimal("0");
        for (PromotionCartItem item : cartItemList) {
            BigDecimal realPrice = item.getPrice().subtract(item.getReduceAmount());
            total=total.add(realPrice.multiply(new BigDecimal(item.getQuantity())));
        }
        return total;
    }

    private BigDecimal calcTotalAmountByproductCategoryId(List<PromotionCartItem> cartItemList, List<Long> productCategoryIds) {
        BigDecimal total = new BigDecimal("0");
        for (PromotionCartItem item : cartItemList) {
            if(productCategoryIds.contains(item.getProductCategoryId())){
                BigDecimal realPrice = item.getPrice().subtract(item.getReduceAmount());
                total=total.add(realPrice.multiply(new BigDecimal(item.getQuantity())));
            }
        }
        return total;
    }

    private BigDecimal calcTotalAmountByProductId(List<PromotionCartItem> cartItemList, List<Long> productIds) {
        BigDecimal total = new BigDecimal("0");
        for (PromotionCartItem item : cartItemList) {
            if(productIds.contains(item.getProductId())){
                BigDecimal realPrice = item.getPrice().subtract(item.getReduceAmount());
                total=total.add(realPrice.multiply(new BigDecimal(item.getQuantity())));
            }
        }
        return total;
    }
}
