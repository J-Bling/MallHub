package com.mall.admin.controller.orders;

import com.mall.admin.service.orders.OmsOrderReturnReasonService;
import com.mall.common.api.ResponsePage;
import com.mall.common.api.ResponseResult;
import com.mall.mbg.model.OmsOrderReturnReason;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 退货原因管理Controller
 */
@RestController
@Api(tags = "OmsOrderReturnReasonController")
@Tag(name = "OmsOrderReturnReasonController", description = "退货原因管理")
@RequestMapping("/returnReason")
public class OmsOrderReturnReasonController {
    @Autowired
    private OmsOrderReturnReasonService orderReturnReasonService;

    @ApiOperation("添加退货原因")
    @PostMapping("/create")
    public ResponseResult<Integer> create(@RequestBody OmsOrderReturnReason returnReason) {
        int count = orderReturnReasonService.create(returnReason);
        if (count > 0) {
            return ResponseResult.success(count);
        }
        return ResponseResult.error();
    }

    @ApiOperation("修改退货原因")
    @PutMapping("/update/{id}")
    public ResponseResult<Integer> update(@PathVariable Long id, @RequestBody OmsOrderReturnReason returnReason) {
        int count = orderReturnReasonService.update(id, returnReason);
        if (count > 0) {
            return ResponseResult.success(count);
        }
        return ResponseResult.error();
    }

    @ApiOperation("批量删除退货原因")
    @DeleteMapping("/delete")
    public ResponseResult<Integer> delete(@RequestBody List<Long> ids) {
        int count = orderReturnReasonService.delete(ids);
        if (count > 0) {
            return ResponseResult.success(count);
        }
        return ResponseResult.error();
    }

    @ApiOperation("分页查询退货原因")
    @GetMapping("/list")
    public ResponseResult<ResponsePage<OmsOrderReturnReason>> list(@RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                               @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<OmsOrderReturnReason> reasonList = orderReturnReasonService.list(pageSize, pageNum);
        return ResponseResult.success(ResponsePage.getPage(reasonList));
    }

    @ApiOperation("获取单个退货原因详情信息")
    @GetMapping("/{id}")
    public ResponseResult<OmsOrderReturnReason> getItem(@PathVariable Long id) {
        OmsOrderReturnReason reason = orderReturnReasonService.getItem(id);
        return ResponseResult.success(reason);
    }

    @ApiOperation("修改退货原因启用状态")
    @PutMapping("/update/status")
    public ResponseResult<Integer> updateStatus(@RequestParam(value = "status") Integer status,
                                     @RequestBody List<Long> ids) {
        int count = orderReturnReasonService.updateStatus(ids, status);
        if (count > 0) {
            return ResponseResult.success(count);
        }
        return ResponseResult.error();
    }
}
