package com.mall.admin.remote.impl;

import com.mall.admin.components.MethodInvokeSend;
import com.mall.admin.remote.BrandManage;
import com.mall.common.constant.enums.remote.RemoteMethodEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BrandManageImpl implements BrandManage {
    @Autowired
    private MethodInvokeSend methodInvokeSend;

    @Override
    public void addBrand(Long brandId) {
        send(RemoteMethodEnum.BRAND_ADD,brandId);
    }

    @Override
    public void addAllBrands() {
        send(RemoteMethodEnum.BRAND_ADD_ALL);
    }

    @Override
    public void deleteBrand(Long brandId) {
        send(RemoteMethodEnum.BRAND_DELETE,brandId);
    }

    @Override
    public void updateBrand(Long brandId) {
        send(RemoteMethodEnum.BRAND_UPDATE,brandId);
    }

    @Override
    public void cleanBrandCache() {
        send(RemoteMethodEnum.BRAND_CLEAN);
    }

    @Override
    public MethodInvokeSend methodInvokeSend() {
        return methodInvokeSend;
    }
}
