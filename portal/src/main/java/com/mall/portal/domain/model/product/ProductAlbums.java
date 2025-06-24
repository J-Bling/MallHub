package com.mall.portal.domain.model.product;

import com.mall.mbg.model.PmsAlbum;
import com.mall.mbg.model.PmsAlbumPic;

import java.util.List;

public class ProductAlbums extends PmsAlbum{
    private List<PmsAlbumPic> albumPicList;

    public List<PmsAlbumPic> getAlbumPicList() {
        return albumPicList;
    }

    public void setAlbumPicList(List<PmsAlbumPic> albumPicList) {
        this.albumPicList = albumPicList;
    }
}
