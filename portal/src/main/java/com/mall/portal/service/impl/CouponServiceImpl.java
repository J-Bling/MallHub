package com.mall.portal.service.impl;

import com.mall.common.constant.enums.UseCouponStatusEnum;
import com.mall.common.exception.ApiException;
import com.mall.common.exception.Assert;
import com.mall.mbg.mapper.SmsCouponHistoryMapper;
import com.mall.mbg.mapper.PmsProductMapper;
import com.mall.mbg.mapper.SmsCouponProductCategoryRelationMapper;
import com.mall.mbg.mapper.SmsCouponProductRelationMapper;
import com.mall.mbg.mapper.SmsCouponMapper;
import com.mall.mbg.model.*;
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
    @Autowired private SmsCouponMapper couponMapper;
    @Autowired private SmsCouponProductRelationMapper productRelationMapper;
    @Autowired private SmsCouponProductCategoryRelationMapper productCategoryRelationMapper;

    private final Logger logger = LoggerFactory.getLogger(CouponServiceImpl.class);

    @Override
    @Transactional
    public void add(Long couponId) {
        UmsMember member = consumerService.getCurrentMember();
        SmsCoupon coupon = couponMapper.selectByPrimaryKey(couponId);
        if (coupon == null) {
            Assert.fail("优惠券不存在");
        }
        if (coupon.getCount() < 1) {
            Assert.fail("优惠券发放完毕");
        }
        Date date = new Date();
        if (coupon.getEnableTime() != null && date.before(coupon.getEnableTime())) {
            Assert.fail("优惠券还没有到达领取时间");
        }
        if (coupon.getEndTime() != null && date.after(coupon.getEndTime())) {
            Assert.fail("优惠券已经过了领取时间");
        }

        // 检查用户是否达到领取上限
        int receivedCount = couponDao.countMemberCouponHistory(member.getId(), couponId);
        if (receivedCount >= coupon.getPerLimit()) {
            Assert.fail("您领取的优惠券已经到达限额");
        }

        // 创建优惠券领取记录
        SmsCouponHistory couponHistory = new CouponHistoryDetail();
        couponHistory.setCouponId(couponId);
        couponHistory.setMemberId(member.getId());
        couponHistory.setCouponCode(generateCouponCode(member.getId()));
        couponHistory.setCreateTime(date);
        couponHistory.setMemberNickname(member.getNickname());
        couponHistory.setGetType(CouponRedemptionMethodEnum.ONLINE_CLAIM.getCode());
        couponHistory.setUseStatus(UseCouponStatusEnum.UNUSED.getCode());

        try {
            // 插入领取记录
            int insertCount = couponHistoryMapper.insert(couponHistory);
            if (insertCount < 1) {
                logger.error("插入 couponHistory 失败 memberId:{}", member.getId());
                Assert.fail("领取优惠券失败");
            }

            // 更新优惠券库存
            couponDao.updateCouponCountReceiveCount(couponId, -1, 1);
        } catch (ApiException apiException) {
            throw apiException;
        } catch (Exception e) {
            logger.error("领取优惠券异常: {}", e.getMessage());
            Assert.fail("领取优惠券失败");
        }
    }

    // 生成优惠券码
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
        return couponDao.selectCouponHistoryByMember(member.getId(), useStatus);
    }

    @Override
    public List<CouponHistoryDetail> listCart(List<PmsProduct> productList, Integer type) {
        if (productList == null || productList.isEmpty()) {
            return Collections.emptyList();
        }

        UmsMember member = consumerService.getCurrentMember();
        List<SmsCouponHistory> couponHistoryList = couponDao.selectCouponHistoryByMember(member.getId(), null);
        if (couponHistoryList == null || couponHistoryList.isEmpty()) {
            return Collections.emptyList();
        }

        List<CouponHistoryDetail> canUse = new ArrayList<>();
        List<CouponHistoryDetail> noUse = new ArrayList<>();

        for (SmsCouponHistory history : couponHistoryList) {
            Long couponId = history.getCouponId();
            SmsCoupon coupon = couponMapper.selectByPrimaryKey(couponId);
            if (coupon == null) continue;

            CouponHistoryDetail detail = new CouponHistoryDetail();
            BeanUtils.copyProperties(history, detail);
            detail.setCoupon(coupon);

            switch (coupon.getUseType()) {
                case 0: // 全场通用
                    if (type == 1) canUse.add(detail);
                    break;
                case 1: // 指定分类
                    List<SmsCouponProductCategoryRelation> categoryRelations =
                            couponDao.selectCategoryRelationsByCoupon(couponId);
                    detail.setCategoryRelationList(categoryRelations);
                    if (containCategory(productList, categoryRelations)) {
                        canUse.add(detail);
                    } else {
                        noUse.add(detail);
                    }
                    break;
                case 2: // 指定商品
                    List<SmsCouponProductRelation> productRelations =
                            couponDao.selectProductRelationsByCoupon(couponId);
                    detail.setProductRelationList(productRelations);
                    if (containProduct(productList, productRelations)) {
                        canUse.add(detail);
                    } else {
                        noUse.add(detail);
                    }
                    break;
            }
        }

        return type == 1 ? canUse : noUse;
    }

    private boolean containProduct(List<PmsProduct> productList, List<SmsCouponProductRelation> relations) {
        if (relations == null || relations.isEmpty()) return false;
        Set<Long> productIds = productList.stream().map(PmsProduct::getId).collect(Collectors.toSet());
        return relations.stream().anyMatch(rel -> productIds.contains(rel.getProductId()));
    }

    private boolean containCategory(List<PmsProduct> productList, List<SmsCouponProductCategoryRelation> relations) {
        if (relations == null || relations.isEmpty()) return false;
        Set<Long> categoryIds = productList.stream().map(PmsProduct::getProductCategoryId).collect(Collectors.toSet());
        return relations.stream().anyMatch(rel -> categoryIds.contains(rel.getProductCategoryId()));
    }

    @Override
    public List<SmsCoupon> listByProduct(Long productId) {
        PmsProduct product = productMapper.selectByPrimaryKey(productId);
        if (product == null) return Collections.emptyList();
        return couponDao.selectCouponsForProduct(productId, product.getProductCategoryId());
    }

    @Override
    public List<SmsCoupon> list(Integer useStatus) {
        UmsMember member = consumerService.getCurrentMember();
        return couponDao.selectCouponsByUseStatus(member.getId(), useStatus);
    }

    @Override
    public List<UserCoupons> getUserCoupons() {
        UmsMember member = consumerService.getCurrentMember();
        List<SmsCouponHistory> histories = couponDao.selectCouponHistoryByMember(member.getId(), null);
        if (histories == null || histories.isEmpty()) return Collections.emptyList();

        return histories.stream().map(history -> {
            UserCoupons userCoupon = new UserCoupons();
            userCoupon.setCouponHistory(history);
            userCoupon.setCoupon(couponMapper.selectByPrimaryKey(history.getCouponId()));
            return userCoupon;
        }).collect(Collectors.toList());
    }
}