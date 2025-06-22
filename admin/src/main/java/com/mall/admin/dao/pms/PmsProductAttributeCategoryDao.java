package com.mall.admin.dao.pms;

import com.mall.mbg.model.PmsProductAttributeCategory;
import org.apache.ibatis.annotations.*;

public interface PmsProductAttributeCategoryDao {

    @Select("SELECT * FROM pms_product_attribute_category WHERE id = #{id}")
    PmsProductAttributeCategory selectById(Long id);
}