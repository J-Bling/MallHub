package com.mall.admin.controller.flash;

import com.mall.admin.domain.flash.SmsFlashPromotionProduct;
import com.mall.admin.service.flash.SmsFlashPromotionProductRelationService;
import com.mall.common.api.ResponsePage;
import com.mall.common.api.ResponseResult;
import com.mall.mbg.model.SmsFlashProductRelation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 限时购和商品关系管理Controller
 */
@RestController
@Api(tags = "SmsFlashPromotionProductRelationController")
@Tag(name = "SmsFlashPromotionProductRelationController", description = "限时购和商品关系管理")
@RequestMapping("/flashProductRelation")
public class SmsFlashPromotionProductRelationController {
    @Autowired
    private SmsFlashPromotionProductRelationService relationService;

    @ApiOperation("批量选择商品添加关联")
    @PostMapping("/create")
    public ResponseResult<Integer> create(@RequestBody List<SmsFlashProductRelation> relationList) {
        int count = relationService.create(relationList);
        if (count > 0) {
            return ResponseResult.success(count);
        }
        return ResponseResult.error();
    }

    @ApiOperation("修改关联信息")
    @PostMapping("/update/{id}")
    public ResponseResult<Integer> update(@PathVariable Long id, @RequestBody SmsFlashProductRelation relation) {
        int count = relationService.update(id, relation);
        if (count > 0) {
            return ResponseResult.success(count);
        }
        return ResponseResult.error();
    }

    @ApiOperation("删除关联")
    @PostMapping("/delete/{id}")
    public ResponseResult<Integer> delete(@PathVariable Long id) {
        int count = relationService.delete(id);
        if (count > 0) {
            return ResponseResult.success(count);
        }
        return ResponseResult.error();
    }

    @ApiOperation("获取关联商品促销信息")
    @GetMapping("/{id}")
    public ResponseResult<SmsFlashProductRelation> getItem(@PathVariable Long id) {
        SmsFlashProductRelation relation = relationService.getItem(id);
        return ResponseResult.success(relation);
    }

    @ApiOperation("分页查询不同场次关联及商品信息")
    @GetMapping("/list")
    public ResponseResult<ResponsePage<SmsFlashPromotionProduct>> list(@RequestParam(value = "flashPromotionId") Long flashPromotionId,
                                                                       @RequestParam(value = "flashPromotionSessionId") Long flashPromotionSessionId,
                                                                       @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                                       @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<SmsFlashPromotionProduct> flashPromotionProductList = relationService.list(flashPromotionId, flashPromotionSessionId, pageSize, pageNum);
        return ResponseResult.success(ResponsePage.getPage(flashPromotionProductList));
    }
}
