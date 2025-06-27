package com.mall.mbg.mapper;

import com.mall.mbg.model.OmsOrderReturn;
import com.mall.mbg.model.OmsOrderReturnExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OmsOrderReturnMapper {
    long countByExample(OmsOrderReturnExample example);

    int deleteByExample(OmsOrderReturnExample example);

    int deleteByPrimaryKey(Long id);

    int insert(OmsOrderReturn row);

    int insertSelective(OmsOrderReturn row);

    List<OmsOrderReturn> selectByExample(OmsOrderReturnExample example);

    OmsOrderReturn selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") OmsOrderReturn row, @Param("example") OmsOrderReturnExample example);

    int updateByExample(@Param("row") OmsOrderReturn row, @Param("example") OmsOrderReturnExample example);

    int updateByPrimaryKeySelective(OmsOrderReturn row);

    int updateByPrimaryKey(OmsOrderReturn row);
}