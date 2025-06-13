package com.mall.portal.controller;

import com.mall.common.api.ResponseResult;
import com.mall.common.exception.Assert;
import com.mall.portal.domain.model.AttentionBrand;
import com.mall.portal.service.AttentionBrandService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/portal/api/attention/brand")
public class AttentionBrandController {
    @Autowired private AttentionBrandService attentionBrandService;

    @PostMapping("/add")
    @ApiOperation("添加品牌收藏")
    public ResponseResult<String> add(@RequestBody AttentionBrand brand){
        attentionBrandService.add(brand);
        return ResponseResult.success("收藏成功");
    }


    @DeleteMapping("/delete/{brandId}")
    @ApiOperation("删除收藏品牌")
    public ResponseResult<String> delete(@PathVariable("brandId") long brandId){
        attentionBrandService.delete(brandId);
        return ResponseResult.success("删除收藏成功");
    }

    @GetMapping("/list/{pageNum}/{pageSize}")
    @ApiOperation("分页获取收藏品牌列表 pageNum 页数 ， pageSize 条数")
    public ResponseResult<List<AttentionBrand>> list(@PathVariable("pageNum") int pageNum,@PathVariable("pageSize") int pageSize){
        if (pageNum < 0) {
            Assert.fail("");
        }
        if (pageSize < 10){
            pageSize = 10;
        }
        return ResponseResult.success(attentionBrandService.list(pageNum,pageSize));
    }

    @GetMapping("/detail/{brandId}")
    @ApiOperation("获取单个详情")
    public ResponseResult<AttentionBrand> detail(@PathVariable("brandId") int brandId){
        return ResponseResult.success(attentionBrandService.detail(brandId));
    }

    @DeleteMapping("/clean")
    @ApiOperation("清空收藏夹")
    public ResponseResult<String> clean(){
        attentionBrandService.clean();
        return ResponseResult.success("清空成功");
    }
}
