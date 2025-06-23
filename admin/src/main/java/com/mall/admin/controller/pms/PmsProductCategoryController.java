package com.mall.admin.controller.pms;

import com.mall.admin.domain.pms.ProductCategoryTreeNodeDTO;
import com.mall.admin.service.PmsProductCategoryService;
import com.mall.mbg.model.PmsProductCategory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/api/productCategory")
@Api(tags = "PmsProductCategoryController", description = "商品分类管理")
public class PmsProductCategoryController {

    @Autowired
    private PmsProductCategoryService productCategoryService;

    @ApiOperation("添加商品分类")
    @PostMapping("/create")
    public String create(@RequestBody PmsProductCategory category) {
        int count = productCategoryService.createCategory(category);
        return count > 0 ? "添加成功" : "添加失败";
    }

    @ApiOperation("修改商品分类")
    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id,
                         @RequestBody PmsProductCategory category) {
        int count = productCategoryService.updateCategory(id, category);
        return count > 0 ? "修改成功" : "修改失败";
    }

    @ApiOperation("删除商品分类")
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        int count = productCategoryService.deleteCategory(id);
        return count > 0 ? "删除成功" : "删除失败";
    }

    @ApiOperation("根据id获取商品分类详情")
    @GetMapping("/{id}")
    public PmsProductCategory getItem(@PathVariable Long id) {
        return productCategoryService.getCategory(id);
    }

    @ApiOperation("查询所有一级分类及子分类")
    @GetMapping("/tree")
    public List<ProductCategoryTreeNodeDTO> treeList() {
        return productCategoryService.getCategoryTree();
    }

    @ApiOperation("修改导航栏显示状态")
    @PostMapping("/update/navStatus")
    public String updateNavStatus(@RequestParam Long id,
                                  @RequestParam Integer navStatus) {
        int count = productCategoryService.updateNavStatus(id, navStatus);
        return count > 0 ? "修改成功" : "修改失败";
    }

    @ApiOperation("修改显示状态")
    @PostMapping("/update/showStatus")
    public String updateShowStatus(@RequestParam Long id,
                                   @RequestParam Integer showStatus) {
        int count = productCategoryService.updateShowStatus(id, showStatus);
        return count > 0 ? "修改成功" : "修改失败";
    }

    @ApiOperation("获取分类及其子分类ID列表")
    @GetMapping("/list/{id}")
    public List<Long> getChildrenIds(@PathVariable Long id) {
        return productCategoryService.getCategoryAndChildrenIds(id);
    }
}