package com.mall.portal.repository;

import com.mall.portal.domain.model.product.AttentionBrand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AttentionBrandRepository extends MongoRepository<AttentionBrand,String> {
    /**
     * 根据用户id 和 品牌id查找关注品牌详情
     */
    AttentionBrand findByMemberIdAndBrandId(long memberId , long brandId);
    /**
     * 分页查询关注的品牌
     */
    Page<AttentionBrand> findByMemberId(long memberId, Pageable pageable);
    /**
     * 根据 用户id 和 品牌id删除指定的 关注
     */
    void deleteByMemberIdAndBrandId(long memberId,long brandId);
    /**
     * 清空用户所有关注品牌
     */
    void deleteAllByMemberId(long memberId);
}
