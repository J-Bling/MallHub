package com.mall.admin.dao.pms;
import com.mall.mbg.model.PmsAlbum;
import org.apache.ibatis.annotations.*;
import java.util.List;
public interface PmsAlbumDao {

    @Insert("INSERT INTO pms_album (name, cover_pic, pic_count, sort, description, create_time) " +
            "VALUES (#{name}, #{coverPic}, #{picCount}, #{sort}, #{description}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertAlbum(PmsAlbum album);

    @Update("UPDATE pms_album SET " +
            "name = #{name}, " +
            "cover_pic = #{coverPic}, " +
            "pic_count = #{picCount}, " +
            "sort = #{sort}, " +
            "description = #{description} " +
            "WHERE id = #{id}")
    int updateAlbum(PmsAlbum album);

    @Delete("DELETE FROM pms_album WHERE id = #{id}")
    int deleteAlbum(Long id);

    @Select("SELECT * FROM pms_album WHERE id = #{id}")
    PmsAlbum selectById(Long id);

    @Select("SELECT * FROM pms_album")
    List<PmsAlbum> selectAll();

    @Select("SELECT COUNT(*) FROM pms_album WHERE name = #{name}")
    int countByName(String name);

    @Update("UPDATE pms_album SET pic_count = pic_count + #{count} WHERE id = #{albumId}")
    void incrementPicCount(@Param("albumId") Long albumId, @Param("count") Integer count);
}