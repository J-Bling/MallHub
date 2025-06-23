package com.mall.admin.dao.pms;

import com.mall.mbg.model.PmsAlbumPic;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Delete;
import java.util.List;

public interface PmsAlbumPicDao {
    int batchInsert(List<PmsAlbumPic> pics);

    @Select("SELECT * FROM pms_album_pic WHERE album_id = #{albumId} ORDER BY create_time DESC")
    List<PmsAlbumPic> selectByAlbumId(Long albumId);

    @Select("select pms_album_pic where id = #{id}")
    PmsAlbumPic selectById(Long id);

    @Delete("DELETE FROM pms_album_pic WHERE album_id = #{albumId}")
    int deleteByAlbumId(Long albumId);
}