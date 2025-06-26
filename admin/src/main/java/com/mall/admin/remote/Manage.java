package com.mall.admin.remote;

import com.mall.admin.components.MethodInvokeSend;
import com.mall.common.constant.enums.remote.RemoteMethodEnum;
import com.mall.common.domain.ParamItem;

public interface Manage {
    MethodInvokeSend methodInvokeSend();

    default void send(RemoteMethodEnum methodEnum,Object ...args){
        methodInvokeSend().send(
                methodEnum.getBeanName(),
                methodEnum.getMethodName(),
                ParamItem.getParams(args)
        );
    }
}
