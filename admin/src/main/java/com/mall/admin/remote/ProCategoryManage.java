package com.mall.admin.remote;

import com.mall.common.constant.enums.remote.RemoteMethodEnum;

public interface ProCategoryManage extends Manage{
    default void addCategory(Long id) {
        send(RemoteMethodEnum.CATEGORY_ADD, id);
    }

    default void deleteCategory(Long id) {
        send(RemoteMethodEnum.CATEGORY_DELETE, id);
    }

    default void cleanCategoryCache() {
        send(RemoteMethodEnum.CATEGORY_CLEAN);
    }
}
