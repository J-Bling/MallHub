package com.mall.admin.controller.pms;

import com.mall.admin.domain.pms.ProductAttributeDetail;
import com.mall.admin.service.PmsProductAttributeService;
import com.mall.mbg.model.PmsProductAttribute;
import com.mall.mbg.model.PmsProductAttributeCategory;
import com.mall.mbg.model.PmsProductAttributeValue;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/api/productAttribute")
@Api(tags = "PmsProductAttributeController", description = "商品属性管理")
public class PmsProductAttributeController {

    @Autowired
    private PmsProductAttributeService productAttributeService;

    // ==================== 属性分类操作 ====================

    @ApiOperation("新增商品属性类型")
    @PostMapping("/category/create")
    public String createAttributeCategory(@RequestBody PmsProductAttributeCategory category) {
        int count = productAttributeService.createAttributeCategory(category);
        return count > 0 ? "添加成功" : "添加失败";
    }

    @ApiOperation("更新商品属性类型名称")
    @PostMapping("/category/updateName")
    public String updateAttributeCategoryName(@RequestBody PmsProductAttributeCategory category) {
        int count = productAttributeService.updateAttributeCategoryName(category);
        return count > 0 ? "更新成功" : "更新失败";
    }

    @ApiOperation("删除商品属性类型")
    @PostMapping("/category/delete/{categoryId}")
    public String deleteAttributeCategory(@PathVariable Long categoryId) {
        int count = productAttributeService.deleteAttributeCategory(categoryId);
        return count > 0 ? "删除成功" : "删除失败";
    }

    @ApiOperation("获取所有商品属性类型")
    @GetMapping("/category/list")
    public List<PmsProductAttributeCategory> getProductAttributeCategoryAll() {
        return productAttributeService.getProductAttributeCategoryAll();
    }

    // ==================== 属性操作 ====================

    @ApiOperation("创建商品属性")
    @PostMapping("/create")
    public String createAttribute(@RequestBody PmsProductAttribute attribute) {
        int count = productAttributeService.createAttribute(attribute);
        return count > 0 ? "添加成功" : "添加失败";
    }

    @ApiOperation("更新商品属性")
    @PostMapping("/update/{id}")
    public String updateAttribute(@PathVariable Long id, @RequestBody PmsProductAttribute attribute) {
        int count = productAttributeService.updateAttribute(id, attribute);
        return count > 0 ? "更新成功" : "更新失败";
    }

    @ApiOperation("删除商品属性")
    @PostMapping("/delete/{id}")
    public String deleteAttribute(@PathVariable Long id) {
        int count = productAttributeService.deleteAttribute(id);
        return count > 0 ? "删除成功" : "删除失败";
    }

    @ApiOperation("获取属性详情")
    @GetMapping("/detail/{id}")
    public ProductAttributeDetail getAttributeDetail(@PathVariable Long id) {
        return productAttributeService.getAttributeDetail(id);
    }

    @ApiOperation("获取分类属性列表")
    @GetMapping("/list/{attributeCategoryId}")
    public List<PmsProductAttribute> listAttributes(@PathVariable Long attributeCategoryId) {
        return productAttributeService.listAttributes(attributeCategoryId);
    }

    // ==================== 属性值操作 ====================

    @ApiOperation("增加一个属性值")
    @PostMapping("/value/create")
    public String createAttributeValue(@RequestBody PmsProductAttributeValue value) {
        int count = productAttributeService.createAttributeValue(value);
        return count > 0 ? "添加成功" : "添加失败";
    }

    @ApiOperation("批量增加属性值")
    @PostMapping("/value/batchCreate")
    public String createAttributeValues(@RequestBody List<PmsProductAttributeValue> values) {
        int count = productAttributeService.createAttributeValues(values);
        return count > 0 ? "添加成功" : "添加失败";
    }

    @ApiOperation("删除属性值")
    @PostMapping("/value/delete/{attributeValueId}")
    public String deleteAttributeValue(@PathVariable Long attributeValueId) {
        productAttributeService.deleteAttributeValue(attributeValueId);
        return "删除成功";
    }

    @ApiOperation("修改属性值")
    @PostMapping("/value/update")
    public String updateAttributeValue(@RequestParam Long attributeValueId,
                                       @RequestParam String value) {
        productAttributeService.updateAttributeValue(attributeValueId, value);
        return "修改成功";
    }

    @ApiOperation("清空商品属性值")
    @PostMapping("/value/clean")
    public String cleanAttribute(@RequestParam Long productId,
                                 @RequestParam Long attributeId) {
        productAttributeService.cleanAttribute(productId, attributeId);
        return "清空成功";
    }
}