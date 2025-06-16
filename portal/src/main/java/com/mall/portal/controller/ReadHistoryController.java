package com.mall.portal.controller;

import com.mall.common.api.ResponseResult;
import com.mall.common.exception.Assert;
import com.mall.portal.domain.model.ReadHistory;
import com.mall.portal.service.ReadHistoryService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/portal/api/readHistory")
@Tag(name = "用户商品浏览记录")
public class ReadHistoryController {
    @Autowired private ReadHistoryService readHistoryService;

    @PostMapping("/add")
    @ApiOperation("新增浏览记录")
    public ResponseResult<String> add(@RequestBody ReadHistory readHistory){
        readHistoryService.add(readHistory);
        return ResponseResult.success("添加成功");
    }

    @DeleteMapping("/clean")
    @ApiOperation("清空浏览记录")
    public ResponseResult<String> clean(){
        readHistoryService.clean();
        return ResponseResult.success("清空成功");
    }


    @PostMapping("/delete")
    @ApiOperation("批量清空浏览记录")
    public ResponseResult<String> delete(@RequestBody List<String> readHistoryIds){
        readHistoryService.delete(readHistoryIds);
        return ResponseResult.success("删除成功");
    }

    @GetMapping("/list/{pageNum}/{pageSize}")
    @ApiOperation("分页获取浏览记录")
    public ResponseResult<List<ReadHistory>> list(
            @PathVariable("pageNum") @Min(0) int pageNum, @PathVariable("pageSize") @Range(min = 5,max = 50) int pageSize){
        return ResponseResult.success(readHistoryService.list(pageNum,pageSize));
    }
}