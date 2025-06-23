package com.mall.admin.dao.pms;

import com.mall.mbg.model.PmsSkuStock;
import org.apache.ibatis.annotations.*;
import java.util.List;

public interface PmsSkuStockDao {

    int batchInsert(List<PmsSkuStock> skuStockList);

    void deleteByProductId(Long productId);

    List<PmsSkuStock> selectByProductId(Long productId);

    PmsSkuStock selectById(Long id);

    int countByProductId(Long productId);

    int lockStock(@Param("skuId") Long skuId, @Param("quantity") Integer quantity);

    int releaseStock(@Param("skuId") Long skuId, @Param("quantity") Integer quantity);

    void incrementStock(@Param("skuId") Long skuId, @Param("quantity") Integer quantity);

    int countSkuCodeByProductId(Long productId);
}