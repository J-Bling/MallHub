package com.mall.admin.service;

import com.mall.admin.domain.PageResult;
import com.mall.admin.domain.pms.CommentDetailDTO;
import com.mall.admin.domain.pms.ProductCommentQueryDTO;
import com.mall.mbg.model.PmsComment;
import java.util.List;

/**
 * 商品评论服务接口
 */
public interface PmsProductCommentService {
    /**
     * 分页查询评论列表
     */
    PageResult<PmsComment> listComments(ProductCommentQueryDTO queryDTO);

    /**
     * 获取评论详情
     */
    CommentDetailDTO getCommentDetail(Long commentId);

    /**
     * 批量更新评论显示状态
     */
    int updateShowStatus(List<Long> ids, Integer showStatus);
}