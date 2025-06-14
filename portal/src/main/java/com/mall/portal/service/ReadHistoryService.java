package com.mall.portal.service;

import com.mall.portal.domain.model.ReadHistory;

import java.util.List;

public interface ReadHistoryService {
    /**
     * 添加新浏览记录
     */
    void add(ReadHistory readHistory);
    /**
     * 批量删除浏览记录
     */
    void delete(List<String> ids);
    /**
     * 分页查看浏览记录
     */
    List<ReadHistory> list(int pageNum,int pageSize);
    /**
     * 清空浏览记录
     */
    void clean();
}
