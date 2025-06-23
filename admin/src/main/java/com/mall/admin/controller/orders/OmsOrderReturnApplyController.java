package com.mall.admin.controller.orders;

import com.mall.admin.domain.orders.OmsOrderReturnApplyResult;
import com.mall.admin.domain.orders.OmsReturnApplyQueryParam;
import com.mall.admin.domain.orders.OmsUpdateStatusParam;
import com.mall.admin.service.orders.OmsOrderReturnApplyService;
import com.mall.common.api.ResponsePage;
import com.mall.common.api.ResponseResult;
import com.mall.mbg.model.OmsOrderReturnApply;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单退货申请管理Controller
 */
@Controller
@Api(tags = "OmsOrderReturnApplyController")
@Tag(name = "OmsOrderReturnApplyController", description = "订单退货申请管理")
@RequestMapping("/returnApply")
public class OmsOrderReturnApplyController {
    @Autowired
    private OmsOrderReturnApplyService returnApplyService;

    @ApiOperation("分页查询退货申请")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public ResponseResult<ResponsePage<OmsOrderReturnApply>> list(OmsReturnApplyQueryParam queryParam,
                                                                @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                                @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<OmsOrderReturnApply> returnApplyList = returnApplyService.list(queryParam, pageSize, pageNum);
        return ResponseResult.success(ResponsePage.getPage(returnApplyList));
    }

    @ApiOperation("批量删除退货申请")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult delete(@RequestParam("ids") List<Long> ids) {
        int count = returnApplyService.delete(ids);
        if (count > 0) {
            return ResponseResult.success(count);
        }
        return  ResponseResult.error();
    }

    @ApiOperation("获取退货申请详情")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseResult getItem(@PathVariable Long id) {
        OmsOrderReturnApplyResult result = returnApplyService.getItem(id);
        return ResponseResult.success(result);
    }

    @ApiOperation("修改退货申请状态")
    @RequestMapping(value = "/update/status/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult updateStatus(@PathVariable Long id, @RequestBody OmsUpdateStatusParam statusParam) {
        int count = returnApplyService.updateStatus(id, statusParam);
        if (count > 0) {
            return ResponseResult.success(count);
        }
        return ResponseResult.error();
    }

}
