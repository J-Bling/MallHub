package com.mall.portal.dao;

import com.mall.mbg.model.PmsBrand;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BrandDao {
    @Select("select * from pms_brand order id asc limit #{offset},#{limit}")
    List<PmsBrand> findBrands(int offset,int limit);
}
