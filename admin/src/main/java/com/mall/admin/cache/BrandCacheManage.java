package com.mall.admin.cache;

import com.mall.mbg.model.PmsBrand;

public interface BrandCacheManage {
    /**
     * 删除品牌缓存
     */
    void delBrandCache(Long id);
    /**
     * 添加品牌缓存
     */
    void add(PmsBrand brand);
}
