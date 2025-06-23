package com.mall.admin.service.impl;

import com.mall.admin.service.PmsProductAlbumService;
import com.mall.admin.dao.pms.PmsAlbumDao;
import com.mall.admin.dao.pms.PmsAlbumPicDao;
import com.mall.mbg.model.PmsAlbum;
import com.mall.mbg.model.PmsAlbumPic;
import com.mall.common.exception.BusinessException;
import com.mall.common.enums.BusinessErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PmsProductAlbumServiceImpl implements PmsProductAlbumService {

    @Autowired
    private PmsAlbumDao albumDao;

    @Autowired
    private PmsAlbumPicDao albumPicDao;

    @Override
    public int createAlbum(PmsAlbum album) {
        // 参数校验
        validateAlbum(album);

        // 检查相册名称是否已存在
        if (albumDao.countByName(album.getName()) > 0) {
            throw new BusinessException(BusinessErrorCode.ALBUM_NAME_EXISTED);
        }

        // 设置默认值
        if (album.getSort() == null) {
            album.setSort(0);
        }
        if (album.getPicCount() == null) {
            album.setPicCount(0);
        }
        album.setCreateTime(new Date());

        return albumDao.insertAlbum(album);
    }

    @Override
    public int updateAlbum(Long id, PmsAlbum album) {
        // 参数校验
        if (id == null) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "相册ID不能为空");
        }
        validateAlbum(album);

        // 检查相册是否存在
        PmsAlbum existingAlbum = albumDao.selectById(id);
        if (existingAlbum == null) {
            throw new BusinessException(BusinessErrorCode.ALBUM_NOT_FOUND);
        }

        // 检查相册名称是否已被其他相册使用
        if (!existingAlbum.getName().equals(album.getName())) {
            if (albumDao.countByName(album.getName()) > 0) {
                throw new BusinessException(BusinessErrorCode.ALBUM_NAME_EXISTED);
            }
        }

        album.setId(id);
        return albumDao.updateAlbum(album);
    }

    @Override
    public int deleteAlbum(Long id) {
        if (id == null) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "相册ID不能为空");
        }

        // 检查相册是否存在
        PmsAlbum album = albumDao.selectById(id);
        if (album == null) {
            throw new BusinessException(BusinessErrorCode.ALBUM_NOT_FOUND);
        }

        // 检查相册中是否有图片
        if (album.getPicCount() > 0) {
            throw new BusinessException(BusinessErrorCode.ALBUM_NOT_EMPTY);
        }

        return albumDao.deleteAlbum(id);
    }

    @Override
    public PmsAlbum getAlbum(Long id) {
        if (id == null) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "相册ID不能为空");
        }

        PmsAlbum album = albumDao.selectById(id);
        if (album == null) {
            throw new BusinessException(BusinessErrorCode.ALBUM_NOT_FOUND);
        }
        return album;
    }

    @Override
    public List<PmsAlbum> listAlbums() {
        return albumDao.selectAll();
    }

    @Override
    public int uploadPictures(Long albumId, List<String> pictureUrls) {
        // 参数校验
        if (albumId == null) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "相册ID不能为空");
        }
        if (CollectionUtils.isEmpty(pictureUrls)) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "图片URL列表不能为空");
        }

        // 检查相册是否存在
        PmsAlbum album = albumDao.selectById(albumId);
        if (album == null) {
            throw new BusinessException(BusinessErrorCode.ALBUM_NOT_FOUND);
        }

        // 构建图片列表
        List<PmsAlbumPic> pics = new ArrayList<>();
        for (String url : pictureUrls) {
            PmsAlbumPic pic = new PmsAlbumPic();
            pic.setAlbumId(albumId);
            pic.setPic(url);
            pic.setCreateTime(new Date());
            pics.add(pic);
        }

        // 批量插入图片
        int count = albumPicDao.batchInsert(pics);

        // 更新相册图片数量
        albumDao.incrementPicCount(albumId, count);
        //检查相册集也没有封面 没有就获取第一张插入作为封面
        if (album.getCoverPic()==null){
            album.setCoverPic(pictureUrls.get(0));
            this.updateAlbum(album.getId(),album);
        }

        return count;
    }

    @Override
    public List<PmsAlbumPic> getAlbumPictures(Long albumId) {
        if (albumId == null) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "相册ID不能为空");
        }

        // 检查相册是否存在
        if (albumDao.selectById(albumId) == null) {
            throw new BusinessException(BusinessErrorCode.ALBUM_NOT_FOUND);
        }

        return albumPicDao.selectByAlbumId(albumId);
    }

    @Override
    @Transactional
    public int deleteAlumPic(Long id) {
        if (id==null){
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "图片ID不能为空");
        }

        PmsAlbumPic pic = albumPicDao.selectById(id);
        if (pic==null){
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "图片不存在不能为空");
        }
        PmsAlbum album = this.getAlbum(pic.getAlbumId());

        if (pic.getPic().equals(album.getCoverPic())){
            album.setCoverPic(null);
        }
        album.setPicCount(album.getPicCount()-1);
        int status = albumPicDao.deleteByAlbumId(id);
        if (status==0){
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "删除图片失败");
        }
        status = this.updateAlbum(album.getId(),album);
        return status;
    }

    /**
     * 验证相册信息
     */
    private void validateAlbum(PmsAlbum album) {
        if (album == null) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "相册信息不能为空");
        }
        if (!StringUtils.hasText(album.getName())) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "相册名称不能为空");
        }
        if (album.getName().length() > 64) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "相册名称不能超过64个字符");
        }
    }
}