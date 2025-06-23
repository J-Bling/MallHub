package com.mall.admin.controller.pms;

import com.mall.admin.service.PmsBrandService;
import com.mall.mbg.model.PmsBrand;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/api/brand")
@Api(tags = "PmsBrandController", description = "商品品牌管理")
public class PmsBrandController {

    @Autowired
    private PmsBrandService brandService;

    @ApiOperation("创建品牌")
    @PostMapping("/create")
    public String create(@RequestBody PmsBrand brand) {
        int count = brandService.createBrand(brand);
        return count > 0 ? "创建成功" : "创建失败";
    }

    @ApiOperation("更新品牌")
    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, @RequestBody PmsBrand brand) {
        int count = brandService.updateBrand(id, brand);
        return count > 0 ? "更新成功" : "更新失败";
    }

    @ApiOperation("删除品牌")
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        int count = brandService.deleteBrand(id);
        return count > 0 ? "删除成功" : "删除失败";
    }

    @ApiOperation("获取品牌详情")
    @GetMapping("/{id}")
    public PmsBrand getBrand(@PathVariable Long id) {
        return brandService.getBrand(id);
    }

    @ApiOperation("获取品牌列表")
    @GetMapping("/list")
    public List<PmsBrand> listBrands() {
        return brandService.listBrands();
    }

    @ApiOperation("更新品牌显示状态")
    @PostMapping("/update/showStatus")
    public String updateShowStatus(@RequestParam Long id,
                                   @RequestParam Integer showStatus) {
        int count = brandService.updateShowStatus(id, showStatus);
        return count > 0 ? "更新成功" : "更新失败";
    }

    @ApiOperation("更新品牌厂家状态")
    @PostMapping("/update/factoryStatus")
    public String updateFactoryStatus(@RequestParam Long id,
                                      @RequestParam Integer factoryStatus) {
        int count = brandService.updateFactoryStatus(id, factoryStatus);
        return count > 0 ? "更新成功" : "更新失败";
    }

    @ApiOperation("增加品牌商品数量")
    @PostMapping("/increment/productCount")
    public String incrementProductCount(@RequestParam Long id,
                                        @RequestParam Integer count) {
        int result = brandService.incrementProductCount(id, count);
        return result > 0 ? "更新成功" : "更新失败";
    }

    @ApiOperation("增加品牌商品评论数")
    @PostMapping("/increment/commentCount")
    public String incrementProductCommentCount(@RequestParam Long id,
                                               @RequestParam Integer count) {
        int result = brandService.incrementProductCommentCount(id, count);
        return result > 0 ? "更新成功" : "更新失败";
    }
}