package com.mall.admin.controller.pms;

import com.mall.admin.domain.PageResult;
import com.mall.admin.domain.pms.CommentDetailDTO;
import com.mall.admin.domain.pms.ProductCommentQueryDTO;
import com.mall.admin.service.PmsProductCommentService;
import com.mall.mbg.model.PmsComment;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/api/productComment")
@Api(tags = "PmsProductCommentController", description = "商品评论管理")
public class PmsProductCommentController {

    @Autowired
    private PmsProductCommentService productCommentService;

    @ApiOperation("分页查询评论列表")
    @GetMapping("/list")
    public PageResult<PmsComment> listComments(ProductCommentQueryDTO queryDTO) {
        return productCommentService.listComments(queryDTO);
    }

    @ApiOperation("获取评论详情")
    @GetMapping("/detail/{commentId}")
    public CommentDetailDTO getCommentDetail(@PathVariable Long commentId) {
        return productCommentService.getCommentDetail(commentId);
    }

    @ApiOperation("批量更新评论显示状态")
    @PostMapping("/updateShowStatus")
    public String updateShowStatus(@RequestParam List<Long> ids,
                                   @RequestParam Integer showStatus) {
        int count = productCommentService.updateShowStatus(ids, showStatus);
        return count > 0 ? "更新成功" : "更新失败";
    }
}