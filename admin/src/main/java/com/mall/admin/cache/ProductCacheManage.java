package com.mall.admin.cache;


public interface ProductCacheManage {
    /**
     * 删除一个 缓存
     */
    void deleteCateCache(Long cateId);
    /**
     * 清空所有缓存
     */
    void cleanCateCache();
}
