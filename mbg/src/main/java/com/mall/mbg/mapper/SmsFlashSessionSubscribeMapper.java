package com.mall.mbg.mapper;

import com.mall.mbg.model.SmsFlashSessionSubscribe;
import com.mall.mbg.model.SmsFlashSessionSubscribeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SmsFlashSessionSubscribeMapper {
    long countByExample(SmsFlashSessionSubscribeExample example);

    int deleteByExample(SmsFlashSessionSubscribeExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SmsFlashSessionSubscribe row);

    int insertSelective(SmsFlashSessionSubscribe row);

    List<SmsFlashSessionSubscribe> selectByExample(SmsFlashSessionSubscribeExample example);

    SmsFlashSessionSubscribe selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") SmsFlashSessionSubscribe row, @Param("example") SmsFlashSessionSubscribeExample example);

    int updateByExample(@Param("row") SmsFlashSessionSubscribe row, @Param("example") SmsFlashSessionSubscribeExample example);

    int updateByPrimaryKeySelective(SmsFlashSessionSubscribe row);

    int updateByPrimaryKey(SmsFlashSessionSubscribe row);
}