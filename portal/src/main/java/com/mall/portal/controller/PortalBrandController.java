package com.mall.portal.controller;

import com.mall.common.api.ResponsePage;
import com.mall.common.api.ResponseResult;
import com.mall.common.exception.Assert;
import com.mall.mbg.model.PmsBrand;
import com.mall.mbg.model.PmsProduct;
import com.mall.portal.service.PortalBrandService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/portal/api/recommend.brand")
public class PortalBrandController {
    @Autowired private PortalBrandService brandService;

    @GetMapping("/brand.list/{pageNum}/{pageSize}")
    @ApiOperation("分页获取品牌")
    public ResponseResult<List<PmsBrand>> list(@PathVariable("pageNum") int pageNum,@PathVariable("pageSize") int pageSize){
        if (pageNum < 0){
            Assert.fail("");
        }
        if (pageSize < 10){
            pageSize = 10;
        }
        return ResponseResult.success(brandService.brandList(pageNum,pageSize));
    }


    @GetMapping("/detail/{brandId}")
    @ApiOperation("获取品牌详情")
    public ResponseResult<PmsBrand> detail(@PathVariable("brandId") long brandId){
        return ResponseResult.success(brandService.detail(brandId));
    }


    @GetMapping("/products/{brandId}/{pageNum}/{pageSize}")
    @ApiOperation("获取品牌相关商品")
    public ResponseResult<ResponsePage<PmsProduct>> productPage(
            @PathVariable("brandId") long brandId,
            @PathVariable("pageNum") int pageNum,
            @PathVariable("pageSize") int pageSize
    ){
        if (pageNum < 0){
            pageNum = 0;
        }
        if (pageSize < 10){
            pageSize=10;
        }
        return ResponseResult.success(brandService.productPage(brandId,pageNum,pageSize));
    }
}
