package com.mall.admin.domain.pms;

import lombok.Data;

import java.util.Date;

@Data//评价查询DTO
public class ProductCommentQueryDTO {
    private Long productId;
    private Long memberId;
    private Integer star;
    private Integer showStatus;
    private Date startTime;
    private Date endTime;
    private Integer pageNum;
    private Integer pageSize;
}
