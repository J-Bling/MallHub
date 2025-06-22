package com.mall.mbg.mapper;

import com.mall.mbg.model.SmsFlashProductRelation;
import com.mall.mbg.model.SmsFlashProductRelationExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SmsFlashProductRelationMapper {
    long countByExample(SmsFlashProductRelationExample example);

    int deleteByExample(SmsFlashProductRelationExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SmsFlashProductRelation row);

    int insertSelective(SmsFlashProductRelation row);

    List<SmsFlashProductRelation> selectByExample(SmsFlashProductRelationExample example);

    SmsFlashProductRelation selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") SmsFlashProductRelation row, @Param("example") SmsFlashProductRelationExample example);

    int updateByExample(@Param("row") SmsFlashProductRelation row, @Param("example") SmsFlashProductRelationExample example);

    int updateByPrimaryKeySelective(SmsFlashProductRelation row);

    int updateByPrimaryKey(SmsFlashProductRelation row);
}