package com.mall.portal.consumer.impl;

import com.mall.portal.cache.ProductCacheService;
import com.mall.portal.consumer.ProductMqHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductMqHandleImpl implements ProductMqHandle {
    @Autowired private ProductCacheService productCacheService;

    @Override
    public void delRank() {
        productCacheService.deleteRank();
    }

    @Override
    public void addProduct(long productId) {
        productCacheService.addProduct(productId);
    }

    @Override
    public void deleteProduct(long productId) {
        productCacheService.deleteProduct(productId);
    }

    @Override
    public void upToDelProductCache(long productId) {
        productCacheService.delProductCache(productId);
    }

    @Override
    public void upToDelProductSubModelCache(long productId) {
        productCacheService.delProductModelCache(productId);
    }

    @Override
    public void upToDelSkuStockCache(long productId) {
        productCacheService.delSkuStock(productId);
    }

    @Override
    public void upToDelStats(long productId, long skuId) {
        productCacheService.delProductStats(productId);
        if (skuId>0){
            productCacheService.delSkuStockCount(productId);
        }
    }
}
