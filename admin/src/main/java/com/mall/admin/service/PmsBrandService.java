package com.mall.admin.service;

import com.mall.mbg.model.PmsBrand;
import java.util.List;

/**
 * 商品品牌服务接口
 */
public interface PmsBrandService {

    /**
     * 创建品牌
     */
    int createBrand(PmsBrand brand);

    /**
     * 更新品牌
     */
    int updateBrand(Long id, PmsBrand brand);

    /**
     * 删除品牌
     */
    int deleteBrand(Long id);

    /**
     * 获取品牌详情
     */
    PmsBrand getBrand(Long id);

    /**
     * 获取品牌列表
     */
    List<PmsBrand> listBrands();

    /**
     * 更新品牌显示状态
     */
    int updateShowStatus(Long id, Integer showStatus);

    /**
     * 更新品牌厂家状态
     */
    int updateFactoryStatus(Long id, Integer factoryStatus);

    /**
     * 更新品牌 商品数量
     */
    int incrementProductCount(Long id ,Integer count);

    /**
     * 更新产品评论数
     */
    int incrementProductCommentCount(Long id,Integer count);
}