package com.mall.mbg.mapper;

import com.mall.mbg.model.OmsReturnOperationLog;
import com.mall.mbg.model.OmsReturnOperationLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OmsReturnOperationLogMapper {
    long countByExample(OmsReturnOperationLogExample example);

    int deleteByExample(OmsReturnOperationLogExample example);

    int deleteByPrimaryKey(Long id);

    int insert(OmsReturnOperationLog row);

    int insertSelective(OmsReturnOperationLog row);

    List<OmsReturnOperationLog> selectByExample(OmsReturnOperationLogExample example);

    OmsReturnOperationLog selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") OmsReturnOperationLog row, @Param("example") OmsReturnOperationLogExample example);

    int updateByExample(@Param("row") OmsReturnOperationLog row, @Param("example") OmsReturnOperationLogExample example);

    int updateByPrimaryKeySelective(OmsReturnOperationLog row);

    int updateByPrimaryKey(OmsReturnOperationLog row);
}