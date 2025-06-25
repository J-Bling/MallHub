package com.mall.admin.controller.orders;

import com.mall.admin.service.orders.OmsOrderSettingService;
import com.mall.common.api.ResponseResult;
import com.mall.mbg.model.OmsOrderSetting;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 订单设置管理Controller
 */
@RestController
@Api(tags = "OmsOrderSettingController")
@Tag(name = "OmsOrderSettingController", description = "订单设置管理")
@RequestMapping("/orderSetting")
public class OmsOrderSettingController {
    @Autowired
    private OmsOrderSettingService orderSettingService;

    @ApiOperation("获取指定订单设置")
    @GetMapping("/{id}")
    public ResponseResult<OmsOrderSetting> getItem(@PathVariable Long id) {
        OmsOrderSetting orderSetting = orderSettingService.getItem(id);
        return ResponseResult.success(orderSetting);
    }

    @ApiOperation("修改指定订单设置")
    @PostMapping("/update/{id}")
    public ResponseResult<Integer> update(@PathVariable Long id, @RequestBody OmsOrderSetting orderSetting) {
        int count = orderSettingService.update(id,orderSetting);
        if(count>0){
            return ResponseResult.success(count);
        }
        return ResponseResult.error();
    }
}
