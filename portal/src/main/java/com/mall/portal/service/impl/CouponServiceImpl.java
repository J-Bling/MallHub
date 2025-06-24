package com.mall.portal.service.impl;

import com.mall.common.constant.enums.CouponUseTypeEnum;
import com.mall.common.constant.enums.UseCouponStatusEnum;
import com.mall.common.exception.ApiException;
import com.mall.common.exception.Assert;
import com.mall.mbg.mapper.SmsCouponHistoryMapper;
import com.mall.mbg.mapper.PmsProductMapper;
import com.mall.mbg.mapper.SmsCouponProductCategoryRelationMapper;
import com.mall.mbg.mapper.SmsCouponProductRelationMapper;
import com.mall.mbg.model.*;
import com.mall.portal.cache.CouponCacheService;
import com.mall.portal.dao.CouponDao;
import com.mall.common.constant.enums.CouponRedemptionMethodEnum;
import com.mall.portal.domain.model.flash.CouponHistoryDetail;
import com.mall.portal.service.CouponService;
import com.mall.portal.service.ConsumerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CouponServiceImpl implements CouponService {

    @Autowired private ConsumerService consumerService;
    @Autowired private SmsCouponHistoryMapper couponHistoryMapper;
    @Autowired private PmsProductMapper productMapper;
    @Autowired private CouponDao couponDao;
    @Autowired private CouponCacheService couponCacheService;
    @Autowired private SmsCouponProductRelationMapper productRelationMapper;
    @Autowired private SmsCouponProductCategoryRelationMapper productCategoryRelationMapper;

    private final Logger logger = LoggerFactory.getLogger(CouponServiceImpl.class);

    @Override
    @Transactional
    public void add(Long couponId) {
        UmsMember member = consumerService.getCurrentMember();
        SmsCoupon coupon = couponCacheService.get(couponId);
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
            couponCacheService.incrementCount(couponId,-1);
            couponCacheService.incrementReceiveCount(couponId,1);
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
    public List<CouponHistoryDetail> listCart(List<PmsProduct> productList, Integer type) {
        if (productList==null||productList.isEmpty()){
            return null;
        }
        UmsMember member = consumerService.getCurrentMember();
        SmsCouponHistoryExample historyExample = new SmsCouponHistoryExample();
        historyExample.createCriteria().andMemberIdEqualTo(member.getId());
        //获取所有历史优惠券
        List<SmsCouponHistory> couponHistoryList = couponHistoryMapper.selectByExample(historyExample);
        if (couponHistoryList==null || couponHistoryList.isEmpty()){
            return null;
        }

        List<CouponHistoryDetail> canUse = new ArrayList<>();
        List<CouponHistoryDetail> noUse = new ArrayList<>();
        Map<Long,SmsCoupon> couponMap = new HashMap<>();
        for (SmsCouponHistory couponHistory : couponHistoryList){
            Long couponId = couponHistory.getCouponId();
            SmsCoupon coupon = couponMap.get(couponId);
            if (coupon==null){
                coupon = this.couponCacheService.get(couponId);
                if (coupon==null){
                    continue;
                }
                couponMap.put(couponId,coupon);
            }
            CouponHistoryDetail couponHistoryDetail = new CouponHistoryDetail();
            BeanUtils.copyProperties(couponHistory,couponHistoryDetail);
            List<SmsCouponProductRelation> couponProductRelationList = null;
            List<SmsCouponProductCategoryRelation> couponProductCategoryRelationList = null;
            Integer useType = coupon.getUseType();
            if (CouponUseTypeEnum.ALL.getCode().equals(useType)){
                //全场通用优惠券
                if (type==1){
                    canUse.add(couponHistoryDetail);
                    continue;
                }
            }else if (CouponUseTypeEnum.CATEGORY.getCode().equals(useType)){
                //指定分类使用
                couponProductCategoryRelationList = this.getSmsCouponProductCategoryRelationList(couponId);
                couponHistoryDetail.setCategoryRelationList(couponProductCategoryRelationList);
                if (this.containCategory(productList,couponProductCategoryRelationList)){
                    canUse.add(couponHistoryDetail);
                }else{
                    noUse.add(couponHistoryDetail);
                }

            }else if (CouponUseTypeEnum.PRODUCT.getCode().equals(useType)){
                //指定商品使用
                couponProductRelationList = this.getCouponProductRelationList(couponId);
                couponHistoryDetail.setProductRelationList(couponProductRelationList);
                if (this.containProduct(productList,couponProductRelationList)){
                    canUse.add(couponHistoryDetail);
                }else {
                    noUse.add(couponHistoryDetail);
                }
            }
        }

        return type == 1 ? canUse : noUse;
    }

    private List<SmsCouponProductRelation> getCouponProductRelationList(long couponId){
        SmsCouponProductRelationExample relationExample = new SmsCouponProductRelationExample();
        relationExample.createCriteria().andCouponIdEqualTo(couponId);
        return productRelationMapper.selectByExample(relationExample);
    }

    private List<SmsCouponProductCategoryRelation> getSmsCouponProductCategoryRelationList(long couponId){
        SmsCouponProductCategoryRelationExample example =new SmsCouponProductCategoryRelationExample();
        example.createCriteria().andCouponIdEqualTo(couponId);
        return productCategoryRelationMapper.selectByExample(example);
    }

    private boolean containProduct(List<PmsProduct> productList,List<SmsCouponProductRelation> productRelationList){
        Set<Long> ids = productList.stream().map(PmsProduct::getId).collect(Collectors.toSet());
        return productRelationList.stream().anyMatch(relation->ids.contains(relation.getProductId()));
    }

    protected boolean containCategory(List<PmsProduct> productList,List<SmsCouponProductCategoryRelation> categoryRelationList){
        Set<Long> ids = productList.stream().map(PmsProduct::getProductCategoryId).collect(Collectors.toSet());
        return categoryRelationList.stream().anyMatch(category->ids.contains(category.getProductCategoryId()));
    }

    @Override
    public List<SmsCoupon> listByProduct(Long productId) {
        PmsProduct product = productMapper.selectByPrimaryKey(productId);
        if (product==null){
            return null;
        }
        Long categoryId = product.getProductCategoryId();
        return this.getCoupons(productId,categoryId);
    }

    private List<SmsCoupon> getCoupons(long productId,long categoryId){
        List<Long> coupons = new ArrayList<>();
        //获取指定商品优惠券
        List<SmsCouponProductRelation> productRelationList = couponCacheService.getCouponProductRelationList(productId);

        if (productRelationList!=null && !productRelationList.isEmpty()){
            List<Long> coupons1= productRelationList.stream().map(SmsCouponProductRelation::getCouponId).collect(Collectors.toList());
            coupons.addAll(coupons1);
        }
        //获取指定分类优惠券
        List<SmsCouponProductCategoryRelation> productCategoryRelationList = couponCacheService.getCouponProductCategoryRelationList(categoryId);
        if (productCategoryRelationList!=null && !productCategoryRelationList.isEmpty()){
            List<Long> coupons2 = productCategoryRelationList.stream().map(SmsCouponProductCategoryRelation::getCouponId).collect(Collectors.toList());
            coupons.addAll(coupons2);
        }

        List<Long> allUseTypeIds = couponCacheService.getAllUseTypeCouponIds();
        if (allUseTypeIds !=null && !allUseTypeIds.isEmpty()){
            coupons.addAll(allUseTypeIds);
        }

        if (coupons.isEmpty()){
            return null;
        }
        return couponCacheService.get(coupons);
    }

    @Override
    public List<SmsCoupon> list(Integer useStatus) {
        UseCouponStatusEnum useCouponStatusEnum = UseCouponStatusEnum.formCode(useStatus);
        UmsMember member = consumerService.getCurrentMember();
        return couponDao.getListByUseStatus(member.getId(),useCouponStatusEnum.getCode());
    }

    @Override
    public List<UserCoupons> getUserCoupons() {
        UmsMember member = consumerService.getCurrentMember();
        SmsCouponHistoryExample historyExample = new SmsCouponHistoryExample();
        historyExample.createCriteria().andMemberIdEqualTo(member.getId());
        List<SmsCouponHistory> couponHistoryList = couponHistoryMapper.selectByExample(historyExample);
        if (couponHistoryList==null || couponHistoryList.isEmpty()){
            return null;
        }
        List<UserCoupons> userCouponsList = new ArrayList<>();
        for (SmsCouponHistory history : couponHistoryList){
            UserCoupons userCoupons = new UserCoupons();
            userCoupons.setCouponHistory(history);
            userCoupons.setCoupon(this.couponCacheService.get(history.getCouponId()));
            userCouponsList.add(userCoupons);
        }
        return userCouponsList;
    }
}