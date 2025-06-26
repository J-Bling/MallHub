package com.mall.admin.remote.impl;

import com.mall.admin.components.MethodInvokeSend;
import com.mall.admin.remote.ProductManage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ProductManageImpl implements ProductManage {
    @Autowired private MethodInvokeSend methodInvokeSend;

    @Override
    public MethodInvokeSend methodInvokeSend() {
        return methodInvokeSend;
    }
}
