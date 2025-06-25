package com.mall.portal.controller;

import com.mall.common.api.ResponseResult;
import com.mall.mbg.model.PmsProduct;
import com.mall.mbg.model.SmsCoupon;
import com.mall.mbg.model.SmsCouponHistory;
import com.mall.portal.domain.model.flash.CouponHistoryDetail;
import com.mall.portal.service.CouponService;
import com.mall.portal.service.ProductService;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/portal/api/member/coupon")
@Tag(name = "CouponController", description = "会员优惠券管理")
@Validated
@Api(tags = "会员优惠券管理")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @Autowired
    private ProductService productService;

    @PostMapping("/add/{couponId}")
    @Operation(summary = "领取指定优惠券")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "couponId", value = "优惠券ID", required = true, paramType = "path", dataType = "long")
    })
    public ResponseResult<String> addCoupon(
            @PathVariable @Min(value = 1, message = "优惠券ID必须大于0") Long couponId) {
        couponService.add(couponId);
        return ResponseResult.success("领取成功");
    }

    @GetMapping("/history")
    @Operation(summary = "获取优惠券历史记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "useStatus", value = "使用状态:0-未使用,1-已使用,2-已过期",
                    paramType = "query", dataType = "int", allowableValues = "0,1,2")
    })
    public ResponseResult<List<SmsCouponHistory>> getCouponHistory(
            @RequestParam(required = false) Integer useStatus) {
        return ResponseResult.success(couponService.listHistory(useStatus));
    }

    @PostMapping("/cart/available")
    @Operation(summary = "获取购物车可用优惠券")
    public ResponseResult<List<CouponHistoryDetail>> getAvailableCartCoupons(
            @RequestBody @Valid @NotNull(message = "商品列表不能为空") List<@Min(1) Long> productIds,
            @RequestParam(defaultValue = "1") @Range(min = 0, max = 1, message = "类型只能是0或1") Integer type) {

        List<PmsProduct> products = productIds.stream()
                .map(productService::getProduct)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return ResponseResult.success(couponService.listCart(products, type));
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "获取商品关联优惠券")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productId", value = "商品ID", required = true, paramType = "path", dataType = "long")
    })
    public ResponseResult<List<SmsCoupon>> getProductCoupons(
            @PathVariable @Min(value = 1, message = "商品ID必须大于0") Long productId) {
        return ResponseResult.success(couponService.listByProduct(productId));
    }

    @GetMapping("/list")
    @Operation(summary = "按状态获取优惠券列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "useStatus", value = "使用状态:0-未使用,1-已使用,2-已过期",
                    required = true, paramType = "query", dataType = "int", allowableValues = "0,1,2")
    })
    public ResponseResult<List<SmsCoupon>> getCouponsByStatus(
            @RequestParam @NotNull Integer useStatus) {
        return ResponseResult.success(couponService.list(useStatus));
    }

    @GetMapping("/user/all")
    @Operation(summary = "获取用户所有优惠券及详情")
    public ResponseResult<List<CouponService.UserCoupons>> getUserAllCoupons() {
        return ResponseResult.success(couponService.getUserCoupons());
    }
}