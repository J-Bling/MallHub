package com.mall.portal.cache;

import com.mall.common.constant.interfaces.Cache;
import com.mall.mbg.model.PmsProductAttribute;
import com.mall.mbg.model.PmsProductAttributeCategory;

import java.util.List;

public interface ProductAttributeCacheService extends Cache {
    /**
     * 获取商品属性分类
     */
    PmsProductAttributeCategory getCategory(long categoryId);
    /**
     * 获取所有商品属性分类
     */
    List<PmsProductAttributeCategory> getCategoryList();
    /**
     * 获取商品属性
     */
    PmsProductAttribute getAttribute(long attributeId);
    /**
     * 根据product_attribute_category_id 获取所有属性
     */
    List<PmsProductAttribute> getAttributeList(long categoryId);

    void delAttributeByCategory(long categoryId);
    void delAttributeById(long id);
    void delAttributeCategoryById(long id);

    class CacheKeys{
        public static String Field(long id){return ""+id;}
        public static String ProductAttributeHashKey(long categoryId){return "product-attribute-hash-key:"+categoryId;}
        public static String ProductAttributeCategoryHashKey = "product-attribute-category-hash-key";
    }
}
