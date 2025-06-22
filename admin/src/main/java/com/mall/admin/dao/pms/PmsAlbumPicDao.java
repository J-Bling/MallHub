package com.mall.admin.dao.pms;

import com.mall.mbg.model.PmsAlbumPic;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface PmsAlbumPicDao {

    @Insert("<script>" +
            "INSERT INTO pms_album_pic (album_id, pic, create_time) VALUES " +
            "<foreach collection='list' item='item' separator=','>" +
            "(#{item.albumId}, #{item.pic}, #{item.createTime})" +
            "</foreach>" +
            "</script>")
    int batchInsert(List<PmsAlbumPic> pics);

    @Select("SELECT * FROM pms_album_pic WHERE album_id = #{albumId} ORDER BY create_time DESC")
    List<PmsAlbumPic> selectByAlbumId(Long albumId);

    @Delete("DELETE FROM pms_album_pic WHERE album_id = #{albumId}")
    int deleteByAlbumId(Long albumId);
}