package com.mall.admin.dao.pms;

import com.mall.mbg.model.PmsProductCategory;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface PmsProductCategoryDao {

    int insertCategory(PmsProductCategory category);

    int updateCategory(PmsProductCategory category);

    int deleteCategory(Long id);

    PmsProductCategory selectById(Long id);

    List<PmsProductCategory> selectByParentId(Long parentId);

    int countByNameAndParentId(@Param("name") String name, @Param("parentId") Long parentId);

    int countByParentId(Long parentId);

    int updateNavStatus(@Param("id") Long id, @Param("navStatus") Integer navStatus);

    int updateShowStatus(@Param("id") Long id, @Param("showStatus") Integer showStatus);

    void updateProductCount(@Param("id") Long id, @Param("delta") Integer delta);

    int countProductByCategoryId(Long categoryId);
}