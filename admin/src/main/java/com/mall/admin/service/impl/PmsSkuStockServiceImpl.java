package com.mall.admin.service.impl;

import com.mall.admin.dao.pms.PmsSkuStockDao;
import com.mall.admin.service.PmsSkuStockService;
import com.mall.common.exception.BusinessException;
import com.mall.common.enums.BusinessErrorCode;
import com.mall.mbg.model.PmsSkuStock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PmsSkuStockServiceImpl implements PmsSkuStockService {

    @Autowired
    private PmsSkuStockDao skuStockDao;

    @Override
    @Transactional
    public int updateSkuStock(Long productId, List<PmsSkuStock> skuStockList) {
        // 参数校验
        validateSkuStockParams(productId, skuStockList);

        // 检查商品是否存在
        if (skuStockDao.countByProductId(productId) == 0) {
            throw new BusinessException(BusinessErrorCode.PRODUCT_NOT_FOUND);
        }

        // 删除原有SKU库存
        skuStockDao.deleteByProductId(productId);

        // 插入新的SKU库存
        if (!CollectionUtils.isEmpty(skuStockList)) {
            // 设置默认值
            skuStockList.forEach(sku -> {
                sku.setProductId(productId);
                if (sku.getPrice() == null) {
                    sku.setPrice(new BigDecimal("0.0"));
                }
                if (sku.getStock() == null) {
                    sku.setStock(0);
                }
                if (sku.getLowStock() == null) {
                    sku.setLowStock(0);
                }
                if (sku.getPromotionPrice() == null) {
                    sku.setPromotionPrice(new BigDecimal("0.0"));
                }
                if (sku.getLockStock() == null) {
                    sku.setLockStock(0);
                }
            });
            return skuStockDao.batchInsert(skuStockList);
        }
        return 0;
    }

    @Override
    public List<PmsSkuStock> getSkuList(Long productId) {
        // 参数校验
        if (productId == null) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "商品ID不能为空");
        }

        return skuStockDao.selectByProductId(productId);
    }

    @Override
    @Transactional
    public int lockStock(Long skuId, Integer quantity) {
        // 参数校验
        if (skuId == null) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "SKU ID不能为空");
        }
        if (quantity == null || quantity <= 0) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "锁定数量必须大于0");
        }

        // 检查SKU是否存在
        PmsSkuStock skuStock = skuStockDao.selectById(skuId);
        if (skuStock == null) {
            throw new BusinessException(BusinessErrorCode.SKU_NOT_FOUND);
        }

        // 检查库存是否充足
        if (skuStock.getStock() - skuStock.getLockStock() < quantity) {
            throw new BusinessException(BusinessErrorCode.SKU_STOCK_NOT_ENOUGH);
        }

        // 锁定库存
        return skuStockDao.lockStock(skuId, quantity);
    }

    @Override
    @Transactional
    public int releaseStock(Long skuId, Integer quantity) {
        // 参数校验
        if (skuId == null) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "SKU ID不能为空");
        }
        if (quantity == null || quantity <= 0) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "释放数量必须大于0");
        }

        // 检查SKU是否存在
        PmsSkuStock skuStock = skuStockDao.selectById(skuId);
        if (skuStock == null) {
            throw new BusinessException(BusinessErrorCode.SKU_NOT_FOUND);
        }

        // 检查锁定库存是否足够释放
        if (skuStock.getLockStock() < quantity) {
            throw new BusinessException(BusinessErrorCode.SKU_LOCK_STOCK_NOT_ENOUGH);
        }

        // 释放库存
        return skuStockDao.releaseStock(skuId, quantity);
    }

    /**
     * 验证SKU库存参数
     */
    private void validateSkuStockParams(Long productId, List<PmsSkuStock> skuStockList) {
        if (productId == null) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "商品ID不能为空");
        }
        if (skuStockList == null) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "SKU列表不能为null");
        }

        // 检查SKU编码是否重复
        long distinctCount = skuStockList.stream()
                .map(PmsSkuStock::getSkuCode)
                .distinct()
                .count();
        if (distinctCount != skuStockList.size()) {
            throw new BusinessException(BusinessErrorCode.SKU_CODE_DUPLICATE);
        }
    }
}