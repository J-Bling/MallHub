package com.mall.admin.cache;


import com.mall.mbg.model.PmsProductCategory;

import java.util.List;

public interface ProductCateCacheManage {
    /**
     * 删除一个 缓存
     */
    void deleteCateCache(Long cateId);
    /**
     * 清空所有缓存
     */
    void cleanCateCache();
    /**
     * 增加一个 缓存
     */
    void add(PmsProductCategory category);
    /**
     * 添加多个缓存
     */
    void addAdd(List<PmsProductCategory> categoryList);
}
