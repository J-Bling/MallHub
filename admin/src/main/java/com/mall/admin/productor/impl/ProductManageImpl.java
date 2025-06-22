package com.mall.admin.productor.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mall.admin.productor.ProductManage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductManageImpl implements ProductManage {
    @Autowired private ObjectMapper objectMapper;



    @Override
    public void delRank() {

    }

    @Override
    public void addProduct(long productId) {

    }

    @Override
    public void deleteProduct(long productId) {

    }

    @Override
    public void upToDelProductCache(long productId) {

    }

    @Override
    public void upToDelProductSubModelCache(long productId) {

    }

    @Override
    public void upToDelSkuStockCache(long productId) {

    }

    @Override
    public void upToDelStats(long productId, long skuId) {

    }
}