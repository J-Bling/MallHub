package com.mall.portal.cache;

import com.mall.common.constant.interfaces.Cache;
import com.mall.mbg.model.PmsProductCategory;

import java.util.List;

public interface ProductCategoryCacheService extends Cache {
    default String HashField(Long id){return ""+id;}
    void addCategory(long id);
    void delCategory(long id);
    void cleanCache();
    /**
     *获取 单个 商品类型
     */
    PmsProductCategory get(long id);
    /**
     * 获取所有商品类型
     */
    List<PmsProductCategory> getAll();
}
