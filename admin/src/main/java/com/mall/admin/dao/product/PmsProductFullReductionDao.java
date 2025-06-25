package com.mall.admin.dao.product;

import com.mall.mbg.model.PmsProductFullReduction;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品满减自定义Dao
 */
public interface PmsProductFullReductionDao {
    /**
     * 批量创建
     */
    int insertList(@Param("list") List<PmsProductFullReduction> productFullReductionList);
}
