package com.mall.portal.controller;

import com.mall.common.api.ResponseResult;
import com.mall.common.exception.Assert;
import com.mall.mbg.model.UmsMemberReceiveAddress;
import com.mall.portal.service.AddressService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/portal/api/member/address")
@Tag(name = "地址管理",description = "地址crud")
public class AddressController {
    @Autowired private AddressService addressService;

    @PostMapping("/add")
    @ApiOperation("添加收货地址")
    public ResponseResult<String> add(@RequestBody UmsMemberReceiveAddress receiveAddress){
        int count = addressService.add(receiveAddress);
        if (count > 0){
            return ResponseResult.success("添加成功");
        }
        Assert.fail("添加失败");
        return null;
    }

    @DeleteMapping("/delete")
    @ApiOperation("根据id删除收货地址")
    public ResponseResult<String> delete(@RequestParam long id){
        int count = addressService.delete(id);
        if (count > 0){
            return ResponseResult.success("删除成功");
        }
        Assert.fail("删除失败");
        return null;
    }

    @PutMapping("/update")
    @ApiOperation("根据id修改收货地址")
    public ResponseResult<String> update(@RequestParam long id , @RequestBody UmsMemberReceiveAddress address){
        int count = addressService.update(id,address);
        if (count > 0){
            return ResponseResult.success("删除成功");
        }
        Assert.fail("删除失败");
        return null;
    }

    @GetMapping("/list")
    @ApiOperation("查询所有收货地址")
    public ResponseResult<List<UmsMemberReceiveAddress>> findAll(){
        return ResponseResult.success(addressService.list());
    }

    @GetMapping("/detail")
    @ApiOperation("查询详情")
    public ResponseResult<UmsMemberReceiveAddress> detail(@RequestParam long id){
        return ResponseResult.success(addressService.getItem(id));
    }
}
