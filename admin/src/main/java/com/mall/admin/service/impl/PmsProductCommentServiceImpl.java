package com.mall.admin.service.impl;

import com.mall.admin.dao.pms.PmsCommentDao;
import com.mall.admin.dao.pms.PmsCommentReplayDao;
import com.mall.admin.dao.pms.PmsProductDao;
import com.mall.admin.domain.PageResult;
import com.mall.admin.domain.pms.CommentDetailDTO;
import com.mall.admin.domain.pms.ProductCommentQueryDTO;
import com.mall.admin.service.PmsProductCommentService;
import com.mall.common.exception.BusinessException;
import com.mall.common.enums.BusinessErrorCode;
import com.mall.mbg.model.PmsComment;
import com.mall.mbg.model.PmsCommentReplay;
import com.mall.mbg.model.PmsProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PmsProductCommentServiceImpl implements PmsProductCommentService {

    @Autowired
    private PmsCommentDao commentDao;

    @Autowired
    private PmsCommentReplayDao commentReplayDao;

    @Autowired
    private PmsProductDao productDao;

    @Override
    public PageResult<PmsComment> listComments(ProductCommentQueryDTO queryDTO) {
        // 参数校验
        if (queryDTO == null) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "查询参数不能为空");
        }
        if (queryDTO.getPageNum() == null || queryDTO.getPageSize() == null) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "分页参数不能为空");
        }

        // 查询评论总数
        int total = commentDao.countByCondition(queryDTO);

        // 查询评论列表
        List<PmsComment> comments = commentDao.selectByCondition(queryDTO);

        // 构建分页结果
        PageResult<PmsComment> result = new PageResult<>();
        result.setPageNum(queryDTO.getPageNum());
        result.setPageSize(queryDTO.getPageSize());
        result.setTotal(total);
        result.setList(comments);

        return result;
    }

    @Override
    public CommentDetailDTO getCommentDetail(Long commentId) {
        // 参数校验
        if (commentId == null) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "评论ID不能为空");
        }

        // 查询评论基本信息
        PmsComment comment = commentDao.selectById(commentId);
        if (comment == null) {
            throw new BusinessException(BusinessErrorCode.COMMENT_NOT_FOUND);
        }

        // 查询商品快照信息
        PmsProduct product = productDao.selectById(comment.getProductId());
        if (product == null) {
            throw new BusinessException(BusinessErrorCode.PRODUCT_NOT_FOUND);
        }

        // 查询回复列表
        List<PmsCommentReplay> replies = commentReplayDao.selectByCommentId(commentId);

        // 构建返回结果
        CommentDetailDTO detailDTO = new CommentDetailDTO();
        detailDTO.setComment(comment);
        detailDTO.setReplies(replies);

        // 设置商品快照
        CommentDetailDTO.ProductSnapshotDTO snapshot = new CommentDetailDTO.ProductSnapshotDTO();
        snapshot.setProductName(product.getName());
        snapshot.setProductPic(product.getPic());
        snapshot.setProductAttributes(product.getProductAttribute());
        detailDTO.setProductSnapshot(snapshot);

        return detailDTO;
    }

    @Override
    public int updateShowStatus(List<Long> ids, Integer showStatus) {
        // 参数校验
        if (CollectionUtils.isEmpty(ids)) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "评论ID列表不能为空");
        }
        if (showStatus == null) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "显示状态不能为空");
        }

        // 检查所有评论是否存在
        List<PmsComment> comments = commentDao.selectByIds(ids);
        if (comments.size() != ids.size()) {
            List<Long> existingIds = comments.stream().map(PmsComment::getId).collect(Collectors.toList());
            List<Long> notFoundIds = ids.stream().filter(id -> !existingIds.contains(id)).collect(Collectors.toList());
            throw new BusinessException(BusinessErrorCode.COMMENT_NOT_FOUND, "以下评论不存在: " + notFoundIds);
        }

        return commentDao.updateShowStatus(ids, showStatus);
    }

    @Override
    public int replyComment(PmsCommentReplay commentReplay) {
        // 参数校验
        validateCommentReplay(commentReplay);

        // 检查评论是否存在
        if (commentDao.selectById(commentReplay.getCommentId()) == null) {
            throw new BusinessException(BusinessErrorCode.COMMENT_NOT_FOUND);
        }

        // 设置默认值
        if (commentReplay.getType() == null) {
            commentReplay.setType(0); // 默认管理员回复
        }
        commentReplay.setCreateTime(new Date());

        return commentReplayDao.insert(commentReplay);
    }

    @Override
    public int deleteComment(Long id) {
        // 参数校验
        if (id == null) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "评论ID不能为空");
        }

        // 检查评论是否存在
        PmsComment comment = commentDao.selectById(id);
        if (comment == null) {
            throw new BusinessException(BusinessErrorCode.COMMENT_NOT_FOUND);
        }

        // 删除评论回复
        commentReplayDao.deleteByCommentId(id);

        // 删除评论
        return commentDao.deleteById(id);
    }

    /**
     * 验证评论回复信息
     */
    private void validateCommentReplay(PmsCommentReplay commentReplay) {
        if (commentReplay == null) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "回复信息不能为空");
        }
        if (commentReplay.getCommentId() == null) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "评论ID不能为空");
        }
        if (!StringUtils.hasText(commentReplay.getContent())) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "回复内容不能为空");
        }
        if (commentReplay.getContent().length() > 1000) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "回复内容不能超过1000个字符");
        }
    }
}