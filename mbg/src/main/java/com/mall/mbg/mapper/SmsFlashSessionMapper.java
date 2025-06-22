package com.mall.mbg.mapper;

import com.mall.mbg.model.SmsFlashSession;
import com.mall.mbg.model.SmsFlashSessionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SmsFlashSessionMapper {
    long countByExample(SmsFlashSessionExample example);

    int deleteByExample(SmsFlashSessionExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SmsFlashSession row);

    int insertSelective(SmsFlashSession row);

    List<SmsFlashSession> selectByExampleWithBLOBs(SmsFlashSessionExample example);

    List<SmsFlashSession> selectByExample(SmsFlashSessionExample example);

    SmsFlashSession selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") SmsFlashSession row, @Param("example") SmsFlashSessionExample example);

    int updateByExampleWithBLOBs(@Param("row") SmsFlashSession row, @Param("example") SmsFlashSessionExample example);

    int updateByExample(@Param("row") SmsFlashSession row, @Param("example") SmsFlashSessionExample example);

    int updateByPrimaryKeySelective(SmsFlashSession row);

    int updateByPrimaryKeyWithBLOBs(SmsFlashSession row);

    int updateByPrimaryKey(SmsFlashSession row);
}