package com.mall.portal.repository;

import com.mall.portal.domain.model.product.AttentionProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AttentionProductRepository extends MongoRepository<AttentionProduct,String> {
    /**
     *  查询单个收藏记录
     */
    AttentionProduct findByMemberIdAndProductId(long memberId,long productId);
    /**
     * 分页查询多个收藏记录
     */
    Page<AttentionProduct> findByMemberId(long memberId, Pageable pageable);
    /**
     * 删除单个记录
     */
    void deleteByMemberIdAndProductId(long memberId,long productId);
    /**
     * 清空所有
     */
    void deleteAllByMemberId(long memberId);
}
