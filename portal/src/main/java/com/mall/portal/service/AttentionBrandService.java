package com.mall.portal.service;

import com.mall.portal.domain.model.AttentionBrand;
import java.util.List;

public interface AttentionBrandService {
    void add(AttentionBrand attentionBrand);
    void delete(long brandId);
    /**
     *  分页获取关注品牌列表
     */
    List<AttentionBrand> list(int pageNum,int pageSize);
    /**
     *获取关注品牌详情
     */
    AttentionBrand detail(long brandId);
    void clean();
}
