package com.mall.portal.controller;


import com.mall.common.api.ResponseResult;
import com.mall.mbg.model.CmsSubject;
import com.mall.mbg.model.PmsProduct;
import com.mall.mbg.model.PmsProductCategory;
import com.mall.portal.domain.model.HomeContent;
import com.mall.portal.service.HomeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/portal/api/home")
public class HomeController {
    @Autowired private HomeService homeService;


    @GetMapping("/content")
    @ApiOperation("首页信息")
    public ResponseResult<HomeContent> getContent(){
        return ResponseResult.success(homeService.content());
    }


    @GetMapping("/recommend.product/{pageNum}/{pageSize}}")
    @ApiOperation("分页获取推荐商品")
    public ResponseResult<List<PmsProduct>> recommendProductList(@PathVariable("pageNum") int pageNum,@PathVariable("pageSize") int pageSize){
        if (pageNum < 0){
            pageNum = 0;
        }
        if (pageSize < 10){
            pageSize = 10;
        }
        return ResponseResult.success(homeService.recommendProductList(pageNum,pageSize));
    }


    @GetMapping("/product.category.list/{parentId}")
    @ApiOperation("获取商品分类 parentId = 0获取第一级")
    public ResponseResult<List<PmsProductCategory>> getProductCategoryList(@PathVariable("parentId") long parentId){
        return ResponseResult.success(homeService.getProductCategoryList(parentId));
    }


    @GetMapping("/subject.list/{categoryId}/{pageNum}/{pageSize}")
    @ApiOperation("根据分类id分页获取")
    public ResponseResult<List<CmsSubject>> getSubject(
            @PathVariable("categoryId") long categoryId,
            @PathVariable("pageNum") int pageNum,
            @PathVariable("pageSize") int pageSize
    ){
        if (pageNum < 0) {
            pageNum = 0;
        }
        if (pageSize < 10){
            pageSize = 10;
        }
        return ResponseResult.success(homeService.getSubject(categoryId,pageNum,pageSize));
    }


    @GetMapping("/getHotProductList/{pageNum}/{pageSize}")
    @ApiOperation("分页获取人气商品")
    public ResponseResult<List<PmsProduct>> getHotProductList(@PathVariable("pageNum") int pageNum,@PathVariable("pageSize") int pageSize){
        if (pageNum < 0) {
            pageNum = 0;
        }
        if (pageSize < 10){
            pageSize = 10;
        }
        return ResponseResult.success(homeService.getHotProduct(pageNum,pageSize));
    }

    @GetMapping("/getNewProductList/{pageNum}/{pageSize}")
    @ApiOperation("分页获取新品商品")
    public ResponseResult<List<PmsProduct>> getNewProductList(@PathVariable("pageNum") int pageNum,@PathVariable("pageSize") int pageSize){
        if (pageNum < 0) {
            pageNum = 0;
        }
        if (pageSize < 10){
            pageSize = 10;
        }
        return ResponseResult.success(homeService.getNewProduct(pageNum,pageSize));
    }
}