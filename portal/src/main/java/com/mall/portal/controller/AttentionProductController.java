package com.mall.portal.controller;

import com.mall.common.api.ResponseResult;
import com.mall.common.exception.Assert;
import com.mall.portal.domain.model.AttentionProduct;
import com.mall.portal.service.AttentionProductService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/portal/api/attention/product")
public class AttentionProductController {
    @Autowired private AttentionProductService attentionProductService;

    @PostMapping("/add")
    @ApiOperation("添加商品收藏")
    public ResponseResult<String> add(@RequestBody AttentionProduct attentionProduct){
        attentionProductService.add(attentionProduct);
        return ResponseResult.success("添加成功");
    }


    @DeleteMapping("/delete/{productId}")
    @ApiOperation("删除视频收藏")
    public ResponseResult<String> delete(@PathVariable("productId") long productId){
        attentionProductService.delete(productId);
        return ResponseResult.success("删除成功");
    }


    @GetMapping("/list/{pageNum}/{pageSize}")
    @ApiOperation("根据分页获取收藏视频的列表")
    public ResponseResult<List<AttentionProduct>> list(@PathVariable("pageNum") int pageNum,@PathVariable("pageSize") int pageSize){
        if (pageNum < 0) {
            pageNum = 0;
        }
        if (pageSize < 10){
            pageSize = 10;
        }
        return ResponseResult.success(attentionProductService.list(pageNum,pageSize));
    }


    @GetMapping("/detail/{productId}")
    @ApiOperation("获取详情")
    public ResponseResult<AttentionProduct> detail(@PathVariable("productId") long productId){
        return ResponseResult.success(attentionProductService.detail(productId));
    }


    @DeleteMapping("/clean")
    @ApiOperation("清空使用商品收藏")
    public ResponseResult<String> clean(){
        attentionProductService.clean();
        return ResponseResult.success("清空成功");
    }
}
