package com.mall.admin.remote;

import com.mall.common.constant.enums.remote.RemoteMethodEnum;

import java.util.List;

public interface ProAttributeManage extends Manage{
    // 属性分类操作
    default void addAttributeCategory(Long cateId) {
        send(RemoteMethodEnum.ATTRIBUTE_ADD_CATE, cateId);
    }

    default void deleteAttributeCategory(Long id) {
        send(RemoteMethodEnum.ATTRIBUTE_DEL_CATEGORY_BY_ID, id);
    }

    default void cleanAllAttributeCategories() {
        send(RemoteMethodEnum.ATTRIBUTE_DEL_CATEGORY_ALL);
    }

    // 属性操作
    default void addAttribute(Long attributeId) {
        send(RemoteMethodEnum.ATTRIBUTE_ADD, attributeId);
    }

    default void batchAddAttributes(List<Long> attributeIds) {
        send(RemoteMethodEnum.ATTRIBUTE_ADD_ALL, attributeIds);
    }

    default void deleteAttribute(Long id, Long categoryId) {
        send(RemoteMethodEnum.ATTRIBUTE_DEL_BY_ID, id, categoryId);
    }

    default void deleteAttributesByCategory(Long categoryId) {
        send(RemoteMethodEnum.ATTRIBUTE_DEL_ALL_BY_CATEGORY, categoryId);
    }
}
