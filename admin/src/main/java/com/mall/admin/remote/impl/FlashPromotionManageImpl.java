package com.mall.admin.remote.impl;

import com.mall.admin.components.MethodInvokeSend;
import com.mall.admin.remote.FlashPromotionManage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlashPromotionManageImpl implements FlashPromotionManage {
    @Autowired private MethodInvokeSend methodInvokeSend;


    @Override
    public MethodInvokeSend methodInvokeSend() {
        return methodInvokeSend;
    }
}
