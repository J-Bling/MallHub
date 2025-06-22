package com.mall.mbg.mapper;

import com.mall.mbg.model.SmsFlashProductSubscribe;
import com.mall.mbg.model.SmsFlashProductSubscribeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SmsFlashProductSubscribeMapper {
    long countByExample(SmsFlashProductSubscribeExample example);

    int deleteByExample(SmsFlashProductSubscribeExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SmsFlashProductSubscribe row);

    int insertSelective(SmsFlashProductSubscribe row);

    List<SmsFlashProductSubscribe> selectByExample(SmsFlashProductSubscribeExample example);

    SmsFlashProductSubscribe selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") SmsFlashProductSubscribe row, @Param("example") SmsFlashProductSubscribeExample example);

    int updateByExample(@Param("row") SmsFlashProductSubscribe row, @Param("example") SmsFlashProductSubscribeExample example);

    int updateByPrimaryKeySelective(SmsFlashProductSubscribe row);

    int updateByPrimaryKey(SmsFlashProductSubscribe row);
}