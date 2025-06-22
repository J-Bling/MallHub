package com.mall.admin.domain;

import lombok.Data;

import java.util.List;

/**
 * 分页结果DTO
 */
@Data
public class PageResult<T> {
    private Integer pageNum;
    private Integer pageSize;
    private Integer total;
    private List<T> list;
}