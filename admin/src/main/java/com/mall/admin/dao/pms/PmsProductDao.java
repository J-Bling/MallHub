package com.mall.admin.dao.pms;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface PmsProductDao {

    @Update("update pms_product set stock = stock + #{count} where id = #{id}")
    void incrementStock(@Param("id") Long id,@Param("count") Integer count);
}