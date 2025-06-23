package com.mall.admin.domain.pms;

import com.mall.mbg.model.PmsComment;
import com.mall.mbg.model.PmsCommentReplay;
import lombok.Data;

import java.util.List;

@Data
public class CommentDetailDTO {
    private PmsComment comment;
    private List<PmsCommentReplay> replies;
}
