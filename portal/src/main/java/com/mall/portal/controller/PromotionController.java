package com.mall.portal.controller;

import com.mall.common.api.ResponseResult;
import com.mall.mbg.model.SmsFlashBehavior;
import com.mall.portal.domain.model.flash.*;
import com.mall.portal.service.FlashPromotionService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/portal/api/promotion")
public class PromotionController {
    @Autowired private FlashPromotionService flashPromotionService;


    @GetMapping("/product-promotion-detail/{productId}")
    @ApiOperation("根据 productId 获取商品的秒杀信息")
    public ResponseResult<FlashProductRelation> getProductPromotionDetail(@PathVariable("productId") long productId){
        return ResponseResult.success(flashPromotionService.getFlashProductRelation(productId));
    }


    @GetMapping("/flash-product-list")
    @ApiOperation("分页按排行榜热度获取当前场次的秒杀商品")
    public ResponseResult<List<FlashProduct>> getFlashProductList(
            @RequestParam("sessionId") long sessionId,
            @RequestParam("offset") int offset,
            @RequestParam("limit") int limit
    ){
        return ResponseResult.success(flashPromotionService.getFlashProductList(sessionId,offset,limit));
    }

    @GetMapping("/start-promotion-list")
    @ApiOperation("获取已经开始的活动 type =1 平台活动 2 商家活动")
    public ResponseResult<List<FlashPromotion>> getStartingPromotionList(@RequestParam("type") int type){
        return ResponseResult.success(flashPromotionService.getStartFlashPromotion((byte)type));
    }


    @GetMapping("/all-promotion-list")
    @ApiOperation("获取7天以内要开始的所有秒杀活动")
    public ResponseResult<List<FlashPromotion>> getAllPromotionList(){
        return ResponseResult.success(flashPromotionService.getAllFlashPromotion());
    }

    @GetMapping("/unstart-promotion-list")
    @ApiOperation("获取7天以内未开始的所有秒杀活动")
    public ResponseResult<List<FlashPromotion>> getUnStartPromotionList(){
        return ResponseResult.success(flashPromotionService.getPreparationFlashPromotion());
    }

    @GetMapping("/subscribe-session")
    @ApiOperation("订阅 一个秒杀场次 只会在该场次开始通知")
    public ResponseResult<String> subscribeSession(@RequestParam("sessionId") long sessionId){
        flashPromotionService.subscribeFlashSession(sessionId);
        return ResponseResult.success("订阅成功");
    }

    @GetMapping("/subscribe-product")
    @ApiOperation("订阅抢购商品")
    public ResponseResult<String> subscribeProduct(@RequestParam("productId") long productId,
                                                   @RequestParam("skuId") long skuId){
        flashPromotionService.subscribeFlashProduct(productId,skuId);
        return ResponseResult.success("订阅成功");
    }

    @GetMapping("/cancel-subscribe-session")
    @ApiOperation("取消订阅抢购场次")
    public ResponseResult<String> cancelSubscribeSession(@RequestParam long sessionId){
        flashPromotionService.unSubscribeFlashSession(sessionId);
        return ResponseResult.success("已经取消");
    }

    @GetMapping("/cancel-subscribe-product")
    @ApiOperation("取消订阅抢购商品")
    public ResponseResult<String> cancelSubscribeProduct(@RequestParam long productId,@RequestParam long skuId){
        flashPromotionService.unSubscribeFlashProduct(productId,skuId);
        return ResponseResult.success("已经取消");
    }

    @GetMapping("/history-subscribe-session")
    @ApiOperation("获取当前用户订阅的历史抢购场次 按时间降序")
    public ResponseResult<List<FlashSubscribeSessionHistory>> getSubscribeSessionHistoryList(
            @RequestParam int offset,@RequestParam int limit
    ){
        return ResponseResult.success(flashPromotionService.getSubscribeSessionHistoryList(offset,limit));
    }

    @GetMapping("/history-subscribe-product")
    @ApiOperation("获取当前用户订阅的历史抢购商品 按时间降序")
    public ResponseResult<List<FlashSubscribeProductHistory>> getSubscribeProductHistoryList(
            @RequestParam int offset,@RequestParam int limit
    ){
        return ResponseResult.success(flashPromotionService.getSubscribeProductHistoryList(offset,limit));
    }

    @GetMapping("/flash-behavior-had")
    @ApiOperation("获取用户秒杀行为记录")
    public ResponseResult<List<SmsFlashBehavior>> getFlashBehaviorByProductId(
            @RequestParam long sessionId,@RequestParam long productId
    ){
        return ResponseResult.success(flashPromotionService.getUserBehaviorList(sessionId,productId));
    }

    @GetMapping("/flash-behavior-history")
    @ApiOperation("获取用户秒杀行为历史记录")
    public ResponseResult<List<FlashBehavior>> getFlashBehaviorByProductId(
            @RequestParam int offset,@RequestParam int limit
    ){
        return ResponseResult.success(flashPromotionService.getFlashBehaviorList(offset,limit));
    }
}
