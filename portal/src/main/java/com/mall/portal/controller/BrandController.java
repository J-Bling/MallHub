package com.mall.portal.controller;

import com.mall.common.api.ResponseResult;
import com.mall.mbg.model.PmsBrand;
import com.mall.mbg.model.PmsProduct;
import com.mall.portal.service.BrandService;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/portal/api/recommend.brand")
public class BrandController {
    @Autowired private BrandService brandService;

    @GetMapping("/brand.list/{offset/{limit}")
    @ApiOperation("分页获取品牌")
    public ResponseResult<List<PmsBrand>> list(
            @PathVariable("offset") @Min(0) int offset, @PathVariable("limit") @Range(min = 5,max = 50) int limit){
        return ResponseResult.success(brandService.brandList(offset,limit));
    }


    @GetMapping("/detail/{brandId}")
    @ApiOperation("获取品牌详情")
    public ResponseResult<PmsBrand> detail(@PathVariable("brandId") long brandId){
        return ResponseResult.success(brandService.detail(brandId));
    }


    @GetMapping("/products/{brandId}/{offset}/{limit}")
    @ApiOperation("获取品牌相关商品")
    public ResponseResult<List<PmsProduct>> productPage(
            @PathVariable("brandId") long brandId,
            @PathVariable("offset") @Min(0) int offset,
            @PathVariable("limit") @Range(min = 5,max = 50) int limit
    ){
        return ResponseResult.success(brandService.productPage(brandId,offset,limit));
    }
}
