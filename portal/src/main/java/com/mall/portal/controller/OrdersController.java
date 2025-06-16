package com.mall.portal.controller;

import com.alipay.api.domain.OrderDetail;
import com.mall.common.api.ResponseResult;
import com.mall.common.constant.enums.OrderStatusEnum;
import com.mall.common.exception.Assert;
import com.mall.portal.domain.model.ConfirmOrders;
import com.mall.portal.domain.model.OrderReturnApplyParam;
import com.mall.portal.domain.model.OrdersParma;
import com.mall.portal.service.OrdersService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/portal/api/orders")
public class OrdersController {
    @Autowired private OrdersService ordersService;


    @PostMapping("/generateConfigOrder")
    @ApiOperation("根据购物车信息生成确认单")
    public ResponseResult<ConfirmOrders> generateConfirmOrderByCartIds(@RequestBody List<Long> cartIds){
        return ResponseResult.success(ordersService.generateConfirmOrders(cartIds));
    }


    @PostMapping("/generateTotalOrders")
    @ApiOperation("根据提交信息生产订单")
    public ResponseResult<OrdersService.TotalOrders> generateTotalOrdersByOrders(@RequestBody OrdersParma ordersParma){
        return ResponseResult.success(ordersService.generateTotalOrders(ordersParma));
    }


    @DeleteMapping("/cancelTimeoutOrders")
    @ApiOperation("自动取消超时订单")
    public ResponseResult<String> cancelTimeoutOrder(){
        ordersService.cancelTimeoutOrder();
        return ResponseResult.success("取消超时订单成功");
    }


    @GetMapping("/paySuccess/{orderId}/{payType}")
    @ApiOperation("支付成功回调")
    public ResponseResult<String> paySuccess(@PathVariable("orderId") long orderId,@PathVariable("payType") int patType){
        int success = ordersService.paySuccess(orderId,patType);
        if (success < 1){
            Assert.fail("支付失败");
        }
        return ResponseResult.success("支付成功");
    }


    @DeleteMapping("/cancelOrder/{orderId}")
    @ApiOperation("取消订单")
    public ResponseResult<String> cancelOrder(@PathVariable("orderId") long orderId){
        ordersService.cancelOrder(orderId);
        return ResponseResult.success("取消成功");
    }


    @DeleteMapping("/sendDelayMessageCancelOrder/{orderId}")
    @ApiOperation("发送延迟消息取消订单")
    public ResponseResult<String> sendDelayMessageCancelOrder(@PathVariable("orderId") long orderId){
        ordersService.sendDelayMessageCancelOrder(orderId);
        return ResponseResult.success("延迟取消订单成功");
    }


    @PutMapping("/confirmReceiveOrder/{orderId}")
    @ApiOperation("确认收货")
    public ResponseResult<String> confirmReceiveOrder(@PathVariable("orderId") long orderId){
        ordersService.confirmReceiveOrder(orderId);
        return ResponseResult.success("收货成功");
    }


    @GetMapping("/detail/{orderId}")
    @ApiOperation("获取订单详情")
    public ResponseResult<OrderDetail> getDetail(@PathVariable("orderId") long orderId){
        return ResponseResult.success(ordersService.detail(orderId));
    }


    @GetMapping("/orderList/{status}/{pageNum}/{pageSize}")
    @ApiOperation("按状态分页获取用户订单列表")
    @ApiImplicitParam(name = "status", value = "订单状态：-1->全部；0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭",
            defaultValue = "-1", allowableValues = "-1,0,1,2,3,4", paramType = "query", dataType = "int")
    public ResponseResult<List<OrderDetail>> orderDetailList(
            @PathVariable("status") int status,
            @PathVariable("pageNum") @Min(0) int pageNum,
            @PathVariable("pageSize") @Range(min = 5,max = 50) int pageSize
    ){
        if (!OrderStatusEnum.isValidCode(status)){
            Assert.fail("错误请求");
        }
        return ResponseResult.success(ordersService.list(status,pageNum,pageSize));
    }



    @DeleteMapping("/deleteOrder/{orderId}")
    @ApiOperation("用户根据订单ID删除订单")
    public ResponseResult<String> deleteOrder(@PathVariable("orderId") long orderId){
        ordersService.deleteOrder(orderId);
        return ResponseResult.success("删除成功");
    }


    @PostMapping("/returnApply")
    @ApiOperation("订单退货")
    public ResponseResult<String> returnApply(@RequestBody OrderReturnApplyParam applyParam){
        ordersService.orderReturnApply(applyParam);
        return ResponseResult.success("退货成功");
    }
}