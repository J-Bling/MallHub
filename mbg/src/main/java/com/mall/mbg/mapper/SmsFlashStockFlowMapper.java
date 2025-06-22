package com.mall.mbg.mapper;

import com.mall.mbg.model.SmsFlashStockFlow;
import com.mall.mbg.model.SmsFlashStockFlowExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SmsFlashStockFlowMapper {
    long countByExample(SmsFlashStockFlowExample example);

    int deleteByExample(SmsFlashStockFlowExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SmsFlashStockFlow row);

    int insertSelective(SmsFlashStockFlow row);

    List<SmsFlashStockFlow> selectByExample(SmsFlashStockFlowExample example);

    SmsFlashStockFlow selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") SmsFlashStockFlow row, @Param("example") SmsFlashStockFlowExample example);

    int updateByExample(@Param("row") SmsFlashStockFlow row, @Param("example") SmsFlashStockFlowExample example);

    int updateByPrimaryKeySelective(SmsFlashStockFlow row);

    int updateByPrimaryKey(SmsFlashStockFlow row);
}