package com.mall.admin.dao.pms;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface PmsProductDao {

    @Update("update pms_product set stock = stock + #{count} where id = #{id}")
    void incrementStock(@Param("id") Long id,@Param("count") Integer count);


    // 批量修改上架状态
    int batchUpdatePublishStatus(@Param("ids") List<Long> ids,
                                 @Param("status") Integer publishStatus);

    // 批量修改新品状态
    int batchUpdateNewStatus(@Param("ids") List<Long> ids,
                             @Param("status") Integer newStatus);

    // 批量删除商品（逻辑删除）
    int batchUpdateDeleteStatus(@Param("ids") List<Long> ids,
                                @Param("status") Integer deleteStatus);

    // 批量修改推荐状态
    int batchUpdateRecommendStatus(@Param("ids") List<Long> ids,
                                   @Param("status") Integer recommendStatus);

    // 根据品牌查询商品ID列表
    List<Long> selectIdsByBrandId(@Param("brandId") Long brandId,@Param("offset") int offset,@Param("offset") int limit);

    // 根据分类查询商品ID列表
    List<Long> selectIdsByCategoryId(@Param("categoryId") Long categoryId,@Param("offset") int offset,@Param("offset") int limit);
}