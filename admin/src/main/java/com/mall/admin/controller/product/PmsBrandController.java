package com.mall.admin.controller.product;

import com.mall.admin.domain.product.PmsBrandParam;
import com.mall.admin.service.product.PmsBrandService;
import com.mall.common.api.ResponsePage;
import com.mall.common.api.ResponseResult;
import com.mall.mbg.model.PmsBrand;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品品牌管理Controller
 */
@RestController
@Api(tags = "PmsBrandController")
@Tag(name = "PmsBrandController", description = "商品品牌管理")
@RequestMapping("/brand")
public class PmsBrandController {
    @Autowired
    private PmsBrandService brandService;

    @ApiOperation(value = "获取全部品牌列表")
    @GetMapping("/listAll")
    public ResponseResult<List<PmsBrand>> getList() {
        return ResponseResult.success(brandService.listAllBrand());
    }

    @ApiOperation(value = "添加品牌")
    @PostMapping("/create")
    public ResponseResult<Integer> create(@Validated @RequestBody PmsBrandParam pmsBrand) {
        ResponseResult<Integer> commonResult;
        int count = brandService.createBrand(pmsBrand);
        if (count == 1) {
            commonResult = ResponseResult.success(count);
        } else {
            commonResult = ResponseResult.error();
        }
        return commonResult;
    }

    @ApiOperation(value = "更新品牌")
    @PostMapping("/update/{id}")
    public ResponseResult<Integer> update(@PathVariable("id") Long id,
                               @Validated @RequestBody PmsBrandParam pmsBrandParam) {
        ResponseResult<Integer> commonResult;
        int count = brandService.updateBrand(id, pmsBrandParam);
        if (count == 1) {
            commonResult = ResponseResult.success(count);
        } else {
            commonResult = ResponseResult.error();
        }
        return commonResult;
    }

    @ApiOperation(value = "删除品牌")
    @DeleteMapping("/delete/{id}")
    public ResponseResult<Integer> delete(@PathVariable("id") Long id) {
        int count = brandService.deleteBrand(id);
        if (count == 1) {
            return ResponseResult.success(null);
        } else {
            return ResponseResult.error();
        }
    }

    @ApiOperation(value = "根据品牌名称分页获取品牌列表")
    @GetMapping("/list")
    public ResponseResult<ResponsePage<PmsBrand>> getList(@RequestParam(value = "keyword", required = false) String keyword,
                                                          @RequestParam(value = "showStatus",required = false) Integer showStatus,
                                                          @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                          @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        List<PmsBrand> brandList = brandService.listBrand(keyword,showStatus,pageNum, pageSize);
        return ResponseResult.success(ResponsePage.getPage(brandList));
    }

    @ApiOperation(value = "根据编号查询品牌信息")
    @GetMapping("/{id}")
    public ResponseResult<PmsBrand> getItem(@PathVariable("id") Long id) {
        return ResponseResult.success(brandService.getBrand(id));
    }

    @ApiOperation(value = "批量删除品牌")
    @PostMapping("/delete/batch")
    public ResponseResult<Integer> deleteBatch(@RequestBody List<Long> ids) {
        int count = brandService.deleteBrand(ids);
        if (count > 0) {
            return ResponseResult.success(count);
        } else {
            return ResponseResult.error();
        }
    }

    @ApiOperation(value = "批量更新显示状态")
    @PostMapping("/update/showStatus")
    public ResponseResult<Integer> updateShowStatus(@RequestBody List<Long> ids,
                                   @RequestParam("showStatus") Integer showStatus) {
        int count = brandService.updateShowStatus(ids, showStatus);
        if (count > 0) {
            return ResponseResult.success(count);
        } else {
            return ResponseResult.error();
        }
    }

    @ApiOperation(value = "批量更新厂家制造商状态")
    @PostMapping("/update/factoryStatus")
    public ResponseResult<Integer> updateFactoryStatus(@RequestBody List<Long> ids,
                                      @RequestParam("factoryStatus") Integer factoryStatus) {
        int count = brandService.updateFactoryStatus(ids, factoryStatus);
        if (count > 0) {
            return ResponseResult.success(count);
        } else {
            return ResponseResult.error();
        }
    }
}
