package com.mall.mbg.mapper;

import com.mall.mbg.model.SmsFlashSkuRelation;
import com.mall.mbg.model.SmsFlashSkuRelationExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SmsFlashSkuRelationMapper {
    long countByExample(SmsFlashSkuRelationExample example);

    int deleteByExample(SmsFlashSkuRelationExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SmsFlashSkuRelation row);

    int insertSelective(SmsFlashSkuRelation row);

    List<SmsFlashSkuRelation> selectByExample(SmsFlashSkuRelationExample example);

    SmsFlashSkuRelation selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") SmsFlashSkuRelation row, @Param("example") SmsFlashSkuRelationExample example);

    int updateByExample(@Param("row") SmsFlashSkuRelation row, @Param("example") SmsFlashSkuRelationExample example);

    int updateByPrimaryKeySelective(SmsFlashSkuRelation row);

    int updateByPrimaryKey(SmsFlashSkuRelation row);
}