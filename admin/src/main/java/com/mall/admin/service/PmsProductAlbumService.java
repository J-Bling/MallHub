package com.mall.admin.service;

import com.mall.mbg.model.PmsAlbum;
import com.mall.mbg.model.PmsAlbumPic;
import java.util.List;

/**
 * 商品相册服务接口
 */
public interface PmsProductAlbumService {

    /**
     * 创建相册
     */
    int createAlbum(PmsAlbum album);

    /**
     * 更新相册
     */
    int updateAlbum(Long id, PmsAlbum album);

    /**
     * 删除相册
     */
    int deleteAlbum(Long id);

    /**
     * 获取相册详情
     */
    PmsAlbum getAlbum(Long id);

    /**
     * 获取相册列表
     */
    List<PmsAlbum> listAlbums();

    /**
     * 上传图片到相册
     */
    int uploadPictures(Long albumId, List<String> pictureUrls);

    /**
     * 获取相册图片列表
     */
    List<PmsAlbumPic> getAlbumPictures(Long albumId);
}