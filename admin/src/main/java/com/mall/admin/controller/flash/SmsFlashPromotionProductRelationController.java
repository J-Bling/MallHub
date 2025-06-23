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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 限时购和商品关系管理Controller
 */
@Controller
@Api(tags = "SmsFlashPromotionProductRelationController")
@Tag(name = "SmsFlashPromotionProductRelationController", description = "限时购和商品关系管理")
@RequestMapping("/flashProductRelation")
public class SmsFlashPromotionProductRelationController {
    @Autowired
    private SmsFlashPromotionProductRelationService relationService;

    @ApiOperation("批量选择商品添加关联")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult create(@RequestBody List<SmsFlashProductRelation> relationList) {
        int count = relationService.create(relationList);
        if (count > 0) {
            return ResponseResult.success(count);
        }
        return ResponseResult.error();
    }

    @ApiOperation("修改关联信息")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult update(@PathVariable Long id, @RequestBody SmsFlashProductRelation relation) {
        int count = relationService.update(id, relation);
        if (count > 0) {
            return ResponseResult.success(count);
        }
        return ResponseResult.error();
    }

    @ApiOperation("删除关联")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult delete(@PathVariable Long id) {
        int count = relationService.delete(id);
        if (count > 0) {
            return ResponseResult.success(count);
        }
        return ResponseResult.error();
    }

    @ApiOperation("获取关联商品促销信息")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseResult<SmsFlashProductRelation> getItem(@PathVariable Long id) {
        SmsFlashProductRelation relation = relationService.getItem(id);
        return ResponseResult.success(relation);
    }

    @ApiOperation("分页查询不同场次关联及商品信息")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public ResponseResult<ResponsePage<SmsFlashPromotionProduct>> list(@RequestParam(value = "flashPromotionId") Long flashPromotionId,
                                                                       @RequestParam(value = "flashPromotionSessionId") Long flashPromotionSessionId,
                                                                       @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                                       @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<SmsFlashPromotionProduct> flashPromotionProductList = relationService.list(flashPromotionId, flashPromotionSessionId, pageSize, pageNum);
        return ResponseResult.success(ResponsePage.getPage(flashPromotionProductList));
    }
}
