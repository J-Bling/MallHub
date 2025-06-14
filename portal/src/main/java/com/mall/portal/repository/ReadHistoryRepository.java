package com.mall.portal.repository;

import com.mall.portal.domain.model.ReadHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReadHistoryRepository extends MongoRepository<ReadHistory,String> {
    /**
     * 分页获取浏览记录
     */
    Page<ReadHistory> findByMemberId(long memberId, Pageable pageable);
    /**
     * 清空当前用户浏览记录
     */
    void deleteAllByMemberId(long memberId);
    /**
     * 批量删除当前用户 的浏览记录
     */
    void deleteByMemberIdAndIdIn(long memberId, List<String> ids);
}
