package com.mall.portal.cache;

import com.mall.common.constant.interfaces.Cache;
import com.mall.mbg.model.PmsProductAttribute;
import com.mall.mbg.model.PmsProductAttributeCategory;

import java.util.List;

public interface ProductAttributeCacheService extends Cache {
    /**
     * 添加属性类型
     */
    void addAttributeCate(long cateId);
    /**
     * 添加属性
     */
    void addAttribute(long attributeId);
    /**
     * 批量添加属性
     */
    void addAttributeAll(List<Long> attributeIds);
    /**
     * 获取商品属性分类
     */
    PmsProductAttributeCategory getAttributeCategory(long categoryId);
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


    void delAttributeAllByCategory(long categoryId);
    void delAttributeById(long id,long categoryId);
    void delAttributeCategoryById(long id);
    void delAttributeCategoryAll();

    class CacheKeys{
        public static String Field(long id){return ""+id;}
        public static String AttributeCateSetKey(long cateId){return "product-attribute-cate-attribute:"+cateId;}
        public static String ProductAttributeKey(long id){return "product-attribute-key:"+id;}
        public static String ProductAttributeCategoryHashKey = "product-attribute-category-hash-key";
        public static String CategoryLock(long categoryId){return "attribute-category-lock:"+categoryId;}
    }
}
