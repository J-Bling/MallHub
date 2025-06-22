package com.mall.admin.domain.pms;

import lombok.Data;

@Data
//商品查询参数DTO
public class ProductQueryDTO {
    private String keyword;
    private Long brandId;
    private Long productCategoryId;
    private Integer publishStatus;
    private Integer verifyStatus;
    private Integer pageNum;
    private Integer pageSize;

    // 排序字段
    private String sortBy;
    // 排序方式：asc/desc
    private String sortOrder;
}
