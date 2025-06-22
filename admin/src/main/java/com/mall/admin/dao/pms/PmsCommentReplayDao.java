package com.mall.admin.dao.pms;

import com.mall.mbg.model.PmsCommentReplay;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface PmsCommentReplayDao {

    @Insert("INSERT INTO pms_comment_replay (" +
            "comment_id, member_nick_name, member_icon, content, create_time, type" +
            ") VALUES (" +
            "#{commentId}, #{memberNickName}, #{memberIcon}, #{content}, #{createTime}, #{type}" +
            ")")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(PmsCommentReplay commentReplay);

    @Select("SELECT * FROM pms_comment_replay WHERE comment_id = #{commentId} ORDER BY create_time ASC")
    List<PmsCommentReplay> selectByCommentId(Long commentId);

    @Delete("DELETE FROM pms_comment_replay WHERE comment_id = #{commentId}")
    int deleteByCommentId(Long commentId);
}