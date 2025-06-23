package com.mall.admin.controller.pms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mall.admin.domain.pms.ProductParam;
import com.mall.admin.service.PmsProductService;
import com.mall.mbg.model.PmsProduct;
import com.mall.mbg.model.PmsSkuStock;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/api/product")
@Api(tags = "商品管理")
public class PmsProductController {
    @Autowired private PmsProductService productService;


    @PostMapping
    @ApiOperation("创建商品")
    public String create(@RequestBody ProductParam productParam) throws JsonProcessingException {
        productService.create(productParam);
        return "创建成功";
    }

    @GetMapping("/{id}")
    @ApiOperation("获取商品详情")
    public ProductParam getProductParam(@PathVariable Long id) {
        return productService.getProductParam(id);
    }

    @PutMapping("/{id}")
    @ApiOperation("更新商品基础信息")
    public String updateProduct(@PathVariable Long id, @RequestBody PmsProduct product) throws JsonProcessingException {
        productService.updateProduct(id, product);
        return "修改成功";
    }

    @PutMapping("/{productId}/sku")
    @ApiOperation("更新SKU信息")
    public String updateSku(@PathVariable Long productId, @RequestBody List<PmsSkuStock> skuStockList) throws JsonProcessingException {
        productService.updateSku(productId, skuStockList);
        return "修改成功";
    }

    @PutMapping("/{productId}/stock")
    @ApiOperation("更新库存数量")
    public String updateStock(@PathVariable Long productId, @RequestBody Map<Long, Integer> skuMap) throws JsonProcessingException {
        productService.updateStockCount(productId, skuMap);
        return "库存更新成功";
    }

    @PutMapping("/publishStatus")
    @ApiOperation("批量修改上架状态")
    public String updatePublishStatus(@RequestParam List<Long> ids, @RequestParam Integer publishStatus) throws JsonProcessingException {
        productService.updateProductPublishStatus(ids, publishStatus);
        return publishStatus == 1 ? "上架成功" : "下架成功";
    }

    @PutMapping("/newStatus")
    @ApiOperation("批量修改新品状态")
    public String updateNewStatus(@RequestParam List<Long> ids, @RequestParam Integer newStatus) throws JsonProcessingException {
        productService.updateNewStatus(ids, newStatus);
        return newStatus == 1 ? "设为新品成功" : "取消新品成功";
    }

    @PutMapping("/deleteStatus")
    @ApiOperation("批量修改删除状态")
    public String updateDeleteStatus(@RequestParam List<Long> ids, @RequestParam Integer deleteStatus) throws JsonProcessingException {
        productService.updateDeleteStatus(ids, deleteStatus);
        return deleteStatus == 1 ? "删除成功" : "恢复成功";
    }

    @PutMapping("/recommendStatus")
    @ApiOperation("批量修改推荐状态")
    public String updateRecommendStatus(@RequestParam List<Long> ids, @RequestParam Integer recommendStatus) throws JsonProcessingException {
        productService.updateRecommendStatus(ids, recommendStatus);
        return recommendStatus == 1 ? "推荐设置成功" : "取消推荐成功";
    }

    @GetMapping("/byBrand")
    @ApiOperation("根据品牌查询商品")
    public List<ProductParam> getByBrandId(
            @RequestParam Long brandId,
            @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(defaultValue = "10") Integer limit) {
        return productService.getByBrandId(brandId, offset, limit);
    }

    @GetMapping("/byCategory")
    @ApiOperation("根据分类查询商品")
    public List<ProductParam> getByCateId(
            @RequestParam Long cateId,
            @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(defaultValue = "10") Integer limit) {
        return productService.getByCateId(cateId, offset, limit);
    }
}
