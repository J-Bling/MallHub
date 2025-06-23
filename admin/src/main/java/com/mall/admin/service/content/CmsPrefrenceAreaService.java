package com.mall.admin.service.content;

import com.mall.mbg.model.CmsPrefrenceArea;

import java.util.List;

/**
 * 优选专区管理Service
 */
public interface CmsPrefrenceAreaService {
    /**
     * 获取所有优选专区
     */
    List<CmsPrefrenceArea> listAll();
}
