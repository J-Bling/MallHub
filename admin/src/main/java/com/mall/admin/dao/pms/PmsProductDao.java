package com.mall.admin.dao.pms;

import com.mall.admin.domain.pms.CommentDetailDTO;
import com.mall.admin.domain.pms.ProductCommentQueryDTO;
import com.mall.admin.domain.pms.ProductQueryDTO;
import com.mall.mbg.model.*;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

public interface PmsProductDao {

    @Select("SELECT * FROM pms_product WHERE id = #{id}")
    PmsProduct selectProductById(Long id);

    @Insert("INSERT INTO pms_product (" +
            "brand_id, product_category_id, freight_template_id, product_attribute_category_id, " +
            "name, pic, product_sn, delete_status, publish_status, recommend_status, " +
            "new_status, verify_status, sort, gift_growth, gift_point, " +
            "use_point_limit, sub_title, description, original_price, price, " +
            "promotion_price, sale, stock, promotion_start_time, promotion_end_time, " +
            "low_stock, unit, weight, preview_status, service_ids, " +
            "keywords, note, album_pics, detail_title, detail_desc, " +
            "detail_html, detail_mobile_html, promotion_per_limit, promotion_type ,create_at" +
            ") VALUES (" +
            "#{brandId}, #{productCategoryId}, #{freightTemplateId}, #{productAttributeCategoryId}, " +
            "#{name}, #{pic}, #{productSn}, #{deleteStatus}, #{publishStatus}, #{recommendStatus}, " +
            "#{newStatus}, #{verifyStatus}, #{sort}, #{giftGrowth}, #{giftPoint}, " +
            "#{usePointLimit}, #{subTitle}, #{description}, #{originalPrice}, #{price}, " +
            "#{promotionPrice}, #{sale}, #{stock}, #{promotionStartTime}, #{promotionEndTime}, " +
            "#{lowStock}, #{unit}, #{weight}, #{previewStatus}, #{serviceIds}, " +
            "#{keywords}, #{note}, #{albumPics}, #{detailTitle}, #{detailDesc}, " +
            "#{detailHtml}, #{detailMobileHtml}, #{promotionPerLimit}, #{promotionType}, #{createAt}" +
            ")")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertProduct(PmsProduct product);

    @Update("UPDATE pms_product SET " +
            "brand_id=#{brandId}, product_category_id=#{productCategoryId}, freight_template_id=#{freightTemplateId}, " +
            "product_attribute_category_id=#{productAttributeCategoryId}, name=#{name}, pic=#{pic}, " +
            "product_sn=#{productSn}, delete_status=#{deleteStatus}, publish_status=#{publishStatus}, " +
            "recommend_status=#{recommendStatus}, new_status=#{newStatus}, verify_status=#{verifyStatus}, " +
            "sort=#{sort}, gift_growth=#{giftGrowth}, gift_point=#{giftPoint}, use_point_limit=#{usePointLimit}, " +
            "sub_title=#{subTitle}, description=#{description}, original_price=#{originalPrice}, price=#{price}, " +
            "promotion_price=#{promotionPrice}, sale=#{sale}, stock=#{stock}, " +
            "promotion_start_time=#{promotionStartTime}, promotion_end_time=#{promotionEndTime}, " +
            "low_stock=#{lowStock}, unit=#{unit}, weight=#{weight}, preview_status=#{previewStatus}, " +
            "service_ids=#{serviceIds}, keywords=#{keywords}, note=#{note}, album_pics=#{albumPics}, " +
            "detail_title=#{detailTitle}, detail_desc=#{detailDesc}, detail_html=#{detailHtml}, " +
            "detail_mobile_html=#{detailMobileHtml}, promotion_per_limit=#{promotionPerLimit}, " +
            "promotion_type=#{promotionType} " +
            "WHERE id=#{id}")
    int updateProduct(PmsProduct product);

    @Update("UPDATE pms_product SET delete_status = #{deleteStatus} WHERE id IN " +
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>#{id}</foreach>")
    int updateDeleteStatus(@Param("ids") List<Long> ids, @Param("deleteStatus") Integer deleteStatus);

    @Update("UPDATE pms_product SET publish_status = #{publishStatus} WHERE id IN " +
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>#{id}</foreach>")
    int updatePublishStatus(@Param("ids") List<Long> ids, @Param("publishStatus") Integer publishStatus);

    @Update("UPDATE pms_product SET recommend_status = #{recommendStatus} WHERE id IN " +
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>#{id}</foreach>")
    int updateRecommendStatus(@Param("ids") List<Long> ids, @Param("recommendStatus") Integer recommendStatus);

    @Update("UPDATE pms_product SET new_status = #{newStatus} WHERE id IN " +
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>#{id}</foreach>")
    int updateNewStatus(@Param("ids") List<Long> ids, @Param("newStatus") Integer newStatus);

    @Update("UPDATE pms_product SET verify_status = #{verifyStatus}, verify_detail = #{detail} WHERE id IN " +
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>#{id}</foreach>")
    int updateVerifyStatus(@Param("ids") List<Long> ids, @Param("verifyStatus") Integer verifyStatus,
                           @Param("detail") String detail);

    @Select("SELECT * FROM pms_product WHERE (name LIKE CONCAT('%', #{keyword}, '%') OR product_sn LIKE CONCAT('%', #{keyword}, '%')) AND delete_status = 0 LIMIT 20")
    List<PmsProduct> selectByKeyword(@Param("keyword") String keyword);

    @Insert("<script>" +
            "INSERT INTO pms_product_attribute_value (product_id, product_attribute_id, value) VALUES " +
            "<foreach collection='list' item='item' separator=','>" +
            "(#{item.productId}, #{item.productAttributeId}, #{item.value})" +
            "</foreach>" +
            "</script>")
    int insertProductAttributeValueList(List<PmsProductAttributeValue> attributeValueList);

    @Select("SELECT * FROM pms_product_attribute_value WHERE product_id = #{productId}")
    List<PmsProductAttributeValue> selectAttributeValueByProductId(Long productId);

    @Delete("DELETE FROM pms_product_attribute_value WHERE product_id = #{productId}")
    int deleteAttributeValueByProductId(Long productId);

    // ============= SKU库存相关操作 =============
    @Insert("<script>" +
            "INSERT INTO pms_sku_stock (product_id, sku_code, price, stock, promotion_price, low_stock, pic, sale, sp_data) VALUES " +
            "<foreach collection='list' item='item' separator=','>" +
            "(#{item.productId}, #{item.skuCode}, #{item.price}, #{item.stock}, #{item.promotionPrice}, " +
            "#{item.lowStock}, #{item.pic}, #{item.sale}, #{item.spData})" +
            "</foreach>" +
            "</script>")
    int insertSkuStockList(List<PmsSkuStock> skuStockList);

    @Select("SELECT * FROM pms_sku_stock WHERE product_id = #{productId}")
    List<PmsSkuStock> selectSkuStockByProductId(Long productId);

    @Delete("DELETE FROM pms_sku_stock WHERE product_id = #{productId}")
    int deleteSkuStockByProductId(Long productId);

    @Update("UPDATE pms_sku_stock SET stock = stock - #{quantity}, lock_stock = lock_stock + #{quantity} " +
            "WHERE id = #{skuId} AND stock >= #{quantity}")
    int lockStock(@Param("skuId") Long skuId, @Param("quantity") Integer quantity);

    @Update("UPDATE pms_sku_stock SET stock = stock + #{quantity}, lock_stock = lock_stock - #{quantity} " +
            "WHERE id = #{skuId}")
    int releaseStock(@Param("skuId") Long skuId, @Param("quantity") Integer quantity);

    // ============= 商品促销相关操作 =============
    @Insert("<script>" +
            "INSERT INTO pms_product_ladder (product_id, count, discount, price) VALUES " +
            "<foreach collection='list' item='item' separator=','>" +
            "(#{item.productId}, #{item.count}, #{item.discount}, #{item.price})" +
            "</foreach>" +
            "</script>")
    int insertProductLadderList(List<PmsProductLadder> productLadderList);

    @Select("SELECT * FROM pms_product_ladder WHERE product_id = #{productId}")
    List<PmsProductLadder> selectProductLadderByProductId(Long productId);

    @Delete("DELETE FROM pms_product_ladder WHERE product_id = #{productId}")
    int deleteProductLadderByProductId(Long productId);

    @Insert("<script>" +
            "INSERT INTO pms_product_full_reduction (product_id, full_price, reduce_price) VALUES " +
            "<foreach collection='list' item='item' separator=','>" +
            "(#{item.productId}, #{item.fullPrice}, #{item.reducePrice})" +
            "</foreach>" +
            "</script>")
    int insertProductFullReductionList(List<PmsProductFullReduction> productFullReductionList);

    @Select("SELECT * FROM pms_product_full_reduction WHERE product_id = #{productId}")
    List<PmsProductFullReduction> selectProductFullReductionByProductId(Long productId);

    @Delete("DELETE FROM pms_product_full_reduction WHERE product_id = #{productId}")
    int deleteProductFullReductionByProductId(Long productId);

    @Insert("<script>" +
            "INSERT INTO pms_member_price (product_id, member_level_id, member_price, member_level_name) VALUES " +
            "<foreach collection='list' item='item' separator=','>" +
            "(#{item.productId}, #{item.memberLevelId}, #{item.memberPrice}, #{item.memberLevelName})" +
            "</foreach>" +
            "</script>")
    int insertMemberPriceList(List<PmsMemberPrice> memberPriceList);

    @Select("SELECT * FROM pms_member_price WHERE product_id = #{productId}")
    List<PmsMemberPrice> selectMemberPriceByProductId(Long productId);

    @Delete("DELETE FROM pms_member_price WHERE product_id = #{productId}")
    int deleteMemberPriceByProductId(Long productId);

    @Insert("<script>" +
            "INSERT INTO pms_product_album_relation (product_id, album_id) VALUES " +
            "<foreach collection='albumIds' item='albumId' separator=','>" +
            "(#{productId}, #{albumId})" +
            "</foreach>" +
            "</script>")
    int insertProductAlbumRelationList(@Param("productId") Long productId,
                                       @Param("albumIds") List<Long> albumIds);

    @Select("SELECT album_id FROM pms_product_album_relation WHERE product_id = #{productId}")
    List<Long> selectAlbumPicIdsByProductId(Long productId);

    @Delete("DELETE FROM pms_product_album_relation WHERE product_id = #{productId}")
    int deleteProductAlbumRelationByProductId(Long productId);

    @Select("SELECT * FROM pms_product_category WHERE parent_id = 0")
    List<PmsProductCategory> selectCategoryTree();

    @Select("SELECT * FROM pms_product_category WHERE parent_id = #{parentId}")
    List<PmsProductCategory> selectCategoryByParentId(Long parentId);

    @Update("UPDATE pms_product_category SET nav_status = #{navStatus} WHERE id = #{id}")
    int updateCategoryNavStatus(@Param("id") Long id, @Param("navStatus") Integer navStatus);

    @Update("UPDATE pms_product_category SET show_status = #{showStatus} WHERE id = #{id}")
    int updateCategoryShowStatus(@Param("id") Long id, @Param("showStatus") Integer showStatus);

    @Select("WITH RECURSIVE category_tree AS (" +
            "SELECT id FROM pms_product_category WHERE id = #{id} " +
            "UNION ALL " +
            "SELECT c.id FROM pms_product_category c " +
            "JOIN category_tree ct ON c.parent_id = ct.id" +
            ") SELECT id FROM category_tree")
    List<Long> selectCategoryAndChildrenIds(Long id);


    @Select("SELECT * FROM pms_comment WHERE product_id = #{productId} AND member_id = #{memberId} " +
            "AND star = #{star} AND show_status = #{showStatus} " +
            "AND create_time >= #{startTime} AND create_time <= #{endTime} " +
            "ORDER BY create_time DESC LIMIT #{pageSize} OFFSET #{pageNum}")
    List<PmsComment> selectCommentList(ProductCommentQueryDTO queryDTO);

    @Update("UPDATE pms_comment SET show_status = #{showStatus} WHERE id IN " +
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>#{id}</foreach>")
    int updateCommentShowStatus(@Param("ids") List<Long> ids, @Param("showStatus") Integer showStatus);

    @Insert("INSERT INTO pms_comment_replay (comment_id, member_nick_name, member_icon, content, create_time, type) " +
            "VALUES (#{commentId}, #{memberNickName}, #{memberIcon}, #{content}, #{createTime}, #{type})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertCommentReply(PmsCommentReplay commentReplay);

//    @Select("SELECT * FROM pms_product " +
//            "WHERE publish_status = 1 " +
//            "AND delete_status = 0 " +
//            "AND promotion_type > 0 " +
//            "AND (promotion_start_time IS NULL OR promotion_start_time <= #{now}) " +
//            "AND (promotion_end_time IS NULL OR promotion_end_time >= #{now}) " +
//            "ORDER BY promotion_type, sort DESC")
//    List<PmsProduct> selectPromotionProducts(@Param("now") Date now);
    @Select("SELECT * FROM pms_product " +
            "WHERE (promotion_start_time IS NULL OR promotion_start_time <= #{now}) " +
            "AND (promotion_end_time IS NULL OR promotion_end_time >= #{now})")
    List<PmsProduct> selectPromotionProducts(@Param("now") Date now);

    List<PmsProduct> selectProductList(ProductQueryDTO queryDTO);
    Long countProductList(ProductQueryDTO queryDTO);
    CommentDetailDTO selectCommentDetailById(Long commentId);

    @Select("SELECT * FROM pms_product WHERE id = #{id}")
    PmsProduct selectById(Long id);

    @Select("<script>" +
            "SELECT * FROM pms_product WHERE id IN " +
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    List<PmsProduct> selectByIds(@Param("ids") List<Long> ids);



    @Select("SELECT COUNT(*) FROM pms_product WHERE name = #{name}")
    int countByName(String name);

    @Select("SELECT COUNT(*) FROM pms_product WHERE product_sn = #{productSn}")
    int countByProductSn(String productSn);

    @Select("<script>" +
            "SELECT COUNT(*) FROM pms_product " +
            "<where>" +
            "<if test='keyword != null and keyword != \"\"'>AND (name LIKE CONCAT('%', #{keyword}, '%') OR product_sn LIKE CONCAT('%', #{keyword}, '%'))</if>" +
            "<if test='brandId != null'>AND brand_id = #{brandId}</if>" +
            "<if test='productCategoryId != null'>AND product_category_id = #{productCategoryId}</if>" +
            "<if test='publishStatus != null'>AND publish_status = #{publishStatus}</if>" +
            "<if test='verifyStatus != null'>AND verify_status = #{verifyStatus}</if>" +
            "AND delete_status = 0" +
            "</where>" +
            "</script>")
    int countByCondition(ProductQueryDTO queryDTO);

    @Select("<script>" +
            "SELECT * FROM pms_product " +
            "<where>" +
            "<if test='keyword != null and keyword != \"\"'>AND (name LIKE CONCAT('%', #{keyword}, '%') OR product_sn LIKE CONCAT('%', #{keyword}, '%'))</if>" +
            "<if test='brandId != null'>AND brand_id = #{brandId}</if>" +
            "<if test='productCategoryId != null'>AND product_category_id = #{productCategoryId}</if>" +
            "<if test='publishStatus != null'>AND publish_status = #{publishStatus}</if>" +
            "<if test='verifyStatus != null'>AND verify_status = #{verifyStatus}</if>" +
            "AND delete_status = 0" +
            "</where>" +
            "<if test='sortBy != null and sortOrder != null'>ORDER BY ${sortBy} ${sortOrder}</if>" +
            "LIMIT #{pageSize} OFFSET #{offset}" +
            "</script>")
    List<PmsProduct> selectByCondition(ProductQueryDTO queryDTO);
}