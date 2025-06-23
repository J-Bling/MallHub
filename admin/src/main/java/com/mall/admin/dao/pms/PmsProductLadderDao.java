package com.mall.admin.dao.pms;

import com.mall.mbg.model.PmsProductLadder;

import java.util.List;

public interface PmsProductLadderDao {

    int batchInsert(List<PmsProductLadder> ladderList);

    void deleteByProductId(Long productId);

    List<PmsProductLadder> selectByProductId(Long productId);
}