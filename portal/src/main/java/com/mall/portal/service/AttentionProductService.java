package com.mall.portal.service;

import com.mall.portal.domain.model.AttentionProduct;

import java.util.List;

public interface AttentionProductService {
    /**
     * 添加收藏夹
     */
    void add(AttentionProduct attentionProduct);
    /**
     * 删除收藏商品
     */
    void delete(long productId);
    /**
     * 分页获取收藏列表
     */
    List<AttentionProduct> list(int pageNum,int pageSize);
    /**
     * 获取详情
     */
    AttentionProduct detail(long productId);
    /**
     * 清理收藏夹
     */
    void clean();
}
