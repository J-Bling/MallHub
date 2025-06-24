package com.mall.portal.controller;


import com.mall.common.api.ResponseResult;
import com.mall.mbg.model.PmsProduct;
import com.mall.mbg.model.SmsCoupon;
import com.mall.mbg.model.SmsCouponHistory;
import com.mall.portal.domain.model.flash.CouponHistoryDetail;
import com.mall.portal.service.CouponService;
import com.mall.portal.service.ProductService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/portal/api/member/coupon")
@Tag(name = "优惠券管理")
public class CouponController {
    @Autowired private CouponService couponService;
    @Autowired private ProductService productService;

    @PostMapping("/add/{couponId}")
    @ApiOperation("领取优惠券")
    public ResponseResult<String> add(@PathVariable long couponId){
        couponService.add(couponId);
        return ResponseResult.success("领取成功");
    }


    @GetMapping("/listHistory/{useStatus}")
    @ApiOperation("获取优惠券历史列表")
    @ApiImplicitParam(name = "useStatus" ,value = "优惠券筛选类型:0->未使用；1->已使用；2->已过期",dataType = "integer")
    public ResponseResult<List<SmsCouponHistory>> getListHistory(@PathVariable int useStatus){
        return ResponseResult.success(couponService.listHistory(useStatus));
    }


    @ApiOperation("获取会员优惠券列表")
    @ApiImplicitParam(name = "useStatus", value = "优惠券筛选类型:0->未使用；1->已使用；2->已过期",
            allowableValues = "0,1,2", paramType = "query", dataType = "integer")
    @GetMapping(value = "/list/{useStatus}")
    public ResponseResult<List<SmsCoupon>> list(@RequestParam int useStatus) {
        return ResponseResult.success(couponService.list(useStatus));
    }


    @ApiOperation("获取购物车里的相关优惠券")
    @ApiImplicitParam(name = "type", value = "使用可用:0->不可用；1->可用",
            defaultValue = "1", allowableValues = "0,1", paramType = "path", dataType = "integer")
    @PostMapping("/list/cart/{type}")
    public ResponseResult<List<CouponHistoryDetail>> listCart(@PathVariable int type , @RequestBody List<Long> productIds) {
        List<PmsProduct> productList = new ArrayList<>();
        for (Long id : productIds){
            productList.add(productService.getProduct(id));
        }
        List<CouponHistoryDetail> couponHistoryList = couponService.listCart(productList,type);
        return ResponseResult.success(couponHistoryList);
    }


    @ApiOperation("获取当前商品相关优惠券")
    @GetMapping(value = "/listByProduct/{productId}")
    public ResponseResult<List<SmsCoupon>> listByProduct(@PathVariable long productId) {
        List<SmsCoupon> couponHistoryList = couponService.listByProduct(productId);
        return ResponseResult.success(couponHistoryList);
    }
}
