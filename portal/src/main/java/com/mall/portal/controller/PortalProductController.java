package com.mall.portal.controller;

import com.mall.common.api.ResponseResult;
import com.mall.mbg.model.PmsProduct;
import com.mall.portal.domain.enums.SortTypeEnum;
import com.mall.portal.domain.model.ProductCategory;
import com.mall.portal.domain.model.ProductDetail;
import com.mall.portal.service.PortalProductService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/portal/api/product")
public class PortalProductController {
    @Autowired private PortalProductService productService;

    @ApiOperation(value = "综合搜索、筛选、排序")
    @ApiImplicitParam(name = "sort", value = "排序字段:0->按相关度；1->按新品；2->按销量；3->价格从低到高；4->价格从高到低",
            defaultValue = "0", allowableValues = "0,1,2,3,4", paramType = "query", dataType = "integer")
    @GetMapping(value = "/search")
    public ResponseResult<List<PmsProduct>> search(@RequestParam(required = false) String keyword,
                                                           @RequestParam(required = false) Long brandId,
                                                           @RequestParam(required = false) Long productCategoryId,
                                                           @RequestParam(required = false, defaultValue = "0") Integer pageNum,
                                                           @RequestParam(required = false, defaultValue = "5") Integer pageSize,
                                                           @RequestParam(required = false, defaultValue = "0") Integer sort)
    {
         return ResponseResult.success(productService.search(keyword, brandId, productCategoryId, pageNum, pageSize, SortTypeEnum.fromCode(sort)));
    }

    @GetMapping("/categoryTreeList")
    @ApiOperation("以树形结构获取所有商品分类")
    public ResponseResult<List<ProductCategory>> categoryTreeList(){
        return ResponseResult.success(productService.categoryTreeList());
    }

    @GetMapping("/detail/{productId}")
    @ApiOperation("获取商品详情")
    public ResponseResult<ProductDetail> detail(@PathVariable("productId") long productId){
        return ResponseResult.success(productService.detail(productId));
    }
}
