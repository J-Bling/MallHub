package com.mall.admin.service;

import com.mall.admin.domain.PageResult;
import com.mall.admin.domain.pms.ProductCreateOrUpdateDTO;
import com.mall.admin.domain.pms.ProductDetailDTO;
import com.mall.admin.domain.pms.ProductListItemDTO;
import com.mall.admin.domain.pms.ProductQueryDTO;
import com.mall.mbg.model.PmsProduct;
import java.util.List;

/**
 * 商品基础服务接口
 */
public interface PmsProductService {

    /**
     * 创建商品
     */
    int createProduct(ProductCreateOrUpdateDTO productDTO);

    /**
     * 更新商品
     */
    int updateProduct(Long id, ProductCreateOrUpdateDTO productDTO);

    /**
     * 批量更新删除状态
     */
    int updateDeleteStatus(List<Long> ids, Integer deleteStatus);

    /**
     * 批量更新上架状态
     */
    int updatePublishStatus(List<Long> ids, Integer publishStatus);

    /**
     * 批量更新推荐状态
     */
    int updateRecommendStatus(List<Long> ids, Integer recommendStatus);

    /**
     * 批量更新新品状态
     */
    int updateNewStatus(List<Long> ids, Integer newStatus);

    /**
     * 批量更新审核状态
     */
    int updateVerifyStatus(List<Long> ids, Integer verifyStatus, String detail);

    /**
     * 根据ID获取商品详情
     */
    ProductDetailDTO getProductDetail(Long id);

    /**
     * 分页查询商品列表
     */
    PageResult<ProductListItemDTO> listProducts(ProductQueryDTO queryDTO);

    /**
     * 根据关键词搜索商品
     */
    List<PmsProduct> searchProducts(String keyword);
}