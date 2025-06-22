package com.mall.admin.domain.pms;

import com.mall.mbg.model.PmsComment;
import com.mall.mbg.model.PmsCommentReplay;
import lombok.Data;

import java.util.List;

@Data
public class CommentDetailDTO {
    private PmsComment comment;
    // 回复列表
    private List<PmsCommentReplay> replies;
    // 商品快照信息
    private ProductSnapshotDTO productSnapshot;

    @Data
    public static class ProductSnapshotDTO {
        private String productName;
        private String productPic;
        private String productAttributes;
    }
}
