package com.mall.admin.dao.pms;

import com.mall.mbg.model.PmsProductFullReduction;

import java.util.List;

public interface PmsProductFullReductionDao {

    int batchInsert(List<PmsProductFullReduction> reductionList);

    void deleteByProductId(Long productId);

    List<PmsProductFullReduction> selectByProductId(Long productId);
}