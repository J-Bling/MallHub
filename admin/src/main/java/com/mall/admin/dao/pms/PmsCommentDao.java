package com.mall.admin.dao.pms;

import com.mall.admin.domain.pms.ProductCommentQueryDTO;
import com.mall.mbg.model.PmsComment;
import com.mall.mbg.model.PmsCommentReplay;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface PmsCommentDao {

    int countByCondition(ProductCommentQueryDTO queryDTO);

    List<PmsComment> selectByCondition(ProductCommentQueryDTO queryDTO);

    @Select("SELECT * FROM pms_comment WHERE id = #{id}")
    PmsComment selectById(Long id);

    List<PmsComment> selectByIds(@Param("ids") List<Long> ids);

    int updateShowStatus(@Param("ids") List<Long> ids, @Param("showStatus") Integer showStatus);

    @Insert("INSERT INTO pms_comment_replay (" +
            "comment_id, member_nick_name, member_icon, content, create_time, type" +
            ") VALUES (" +
            "#{commentId}, #{memberNickName}, #{memberIcon}, #{content}, #{createTime}, #{type}" +
            ")")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(PmsCommentReplay commentReplay);

    @Select("SELECT * FROM pms_comment_replay WHERE comment_id = #{commentId} ORDER BY create_time ASC")
    List<PmsCommentReplay> selectByCommentId(Long commentId);
}