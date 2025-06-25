package com.mall.admin.controller.flash;

import com.mall.admin.domain.flash.SmsFlashPromotionSessionDetail;
import com.mall.admin.service.flash.SmsFlashPromotionSessionService;
import com.mall.common.api.ResponseResult;
import com.mall.mbg.model.SmsFlashSession;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 限时购场次管理Controller
 */
@RestController
@Api(tags = "SmsFlashPromotionSessionController")
@Tag(name = "SmsFlashPromotionSessionController", description = "限时购场次管理")
@RequestMapping("/flashSession")
public class SmsFlashPromotionSessionController {
    @Autowired
    private SmsFlashPromotionSessionService flashPromotionSessionService;

    @ApiOperation("添加场次")
    @PostMapping("/create")
    public ResponseResult<Integer> create(@RequestBody SmsFlashSession promotionSession) {
        int count = flashPromotionSessionService.create(promotionSession);
        if (count > 0) {
            return ResponseResult.success(count);
        }
        return ResponseResult.error();
    }

    @ApiOperation("修改场次")
    @PutMapping("/update/{id}")
    public ResponseResult<Integer> update(@PathVariable Long id, @RequestBody SmsFlashSession promotionSession) {
        int count = flashPromotionSessionService.update(id, promotionSession);
        if (count > 0) {
            return ResponseResult.success(count);
        }
        return ResponseResult.error();
    }

    @ApiOperation("修改启用状态")
    @PutMapping("/update/status/{id}")
    public ResponseResult<Integer> updateStatus(@PathVariable Long id, Integer status) {
        int count = flashPromotionSessionService.updateStatus(id, status);
        if (count > 0) {
            return ResponseResult.success(count);
        }
        return ResponseResult.error();
    }

    @ApiOperation("删除场次")
    @DeleteMapping("/delete/{id}")
    public ResponseResult<Integer> delete(@PathVariable Long id) {
        int count = flashPromotionSessionService.delete(id);
        if (count > 0) {
            return ResponseResult.success(count);
        }
        return ResponseResult.error();
    }

    @ApiOperation("获取场次详情")
    @GetMapping("/{id}")
    public ResponseResult<SmsFlashSession> getItem(@PathVariable Long id) {
        SmsFlashSession promotionSession = flashPromotionSessionService.getItem(id);
        return ResponseResult.success(promotionSession);
    }

    @ApiOperation("获取全部场次")
    @GetMapping("/list")
    public ResponseResult<List<SmsFlashSession>> list() {
        List<SmsFlashSession> promotionSessionList = flashPromotionSessionService.list();
        return ResponseResult.success(promotionSessionList);
    }

    @ApiOperation("获取全部可选场次及其数量")
    @GetMapping("/selectList")
    public ResponseResult<List<SmsFlashPromotionSessionDetail>> selectList(Long flashPromotionId) {
        List<SmsFlashPromotionSessionDetail> promotionSessionList = flashPromotionSessionService.selectList(flashPromotionId);
        return ResponseResult.success(promotionSessionList);
    }
}