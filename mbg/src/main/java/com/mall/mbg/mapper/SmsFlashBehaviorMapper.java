package com.mall.mbg.mapper;

import com.mall.mbg.model.SmsFlashBehavior;
import com.mall.mbg.model.SmsFlashBehaviorExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SmsFlashBehaviorMapper {
    long countByExample(SmsFlashBehaviorExample example);

    int deleteByExample(SmsFlashBehaviorExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SmsFlashBehavior row);

    int insertSelective(SmsFlashBehavior row);

    List<SmsFlashBehavior> selectByExample(SmsFlashBehaviorExample example);

    SmsFlashBehavior selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") SmsFlashBehavior row, @Param("example") SmsFlashBehaviorExample example);

    int updateByExample(@Param("row") SmsFlashBehavior row, @Param("example") SmsFlashBehaviorExample example);

    int updateByPrimaryKeySelective(SmsFlashBehavior row);

    int updateByPrimaryKey(SmsFlashBehavior row);
}