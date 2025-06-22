package com.mall.admin.dao.pms;

import com.mall.admin.domain.pms.ProductCommentQueryDTO;
import com.mall.mbg.model.PmsComment;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface PmsCommentDao {

    @Select("<script>" +
            "SELECT COUNT(*) FROM pms_comment " +
            "<where>" +
            "<if test='productId != null'>AND product_id = #{productId}</if>" +
            "<if test='memberId != null'>AND member_id = #{memberId}</if>" +
            "<if test='star != null'>AND star = #{star}</if>" +
            "<if test='showStatus != null'>AND show_status = #{showStatus}</if>" +
            "<if test='startTime != null'>AND create_time &gt;= #{startTime}</if>" +
            "<if test='endTime != null'>AND create_time &lt;= #{endTime}</if>" +
            "</where>" +
            "</script>")
    int countByCondition(ProductCommentQueryDTO queryDTO);

    @Select("<script>" +
            "SELECT * FROM pms_comment " +
            "<where>" +
            "<if test='productId != null'>AND product_id = #{productId}</if>" +
            "<if test='memberId != null'>AND member_id = #{memberId}</if>" +
            "<if test='star != null'>AND star = #{star}</if>" +
            "<if test='showStatus != null'>AND show_status = #{showStatus}</if>" +
            "<if test='startTime != null'>AND create_time &gt;= #{startTime}</if>" +
            "<if test='endTime != null'>AND create_time &lt;= #{endTime}</if>" +
            "</where>" +
            "ORDER BY create_time DESC " +
            "LIMIT #{pageSize} OFFSET #{offset}" +
            "</script>")
    List<PmsComment> selectByCondition(ProductCommentQueryDTO queryDTO);

    @Select("SELECT * FROM pms_comment WHERE id = #{id}")
    PmsComment selectById(Long id);

    @Select("<script>" +
            "SELECT * FROM pms_comment WHERE id IN " +
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    List<PmsComment> selectByIds(@Param("ids") List<Long> ids);

    @Update("<script>" +
            "UPDATE pms_comment SET show_status = #{showStatus} WHERE id IN " +
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    int updateShowStatus(@Param("ids") List<Long> ids, @Param("showStatus") Integer showStatus);

    @Delete("DELETE FROM pms_comment WHERE id = #{id}")
    int deleteById(Long id);
}