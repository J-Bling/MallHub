package com.mall.admin.service.flash;
import com.mall.admin.domain.flash.SmsFlashPromotionSessionDetail;
import com.mall.mbg.model.SmsFlashSession;

import java.util.List;

/**
 * 限时购场次管理Service
 */
public interface SmsFlashPromotionSessionService {
    /**
     * 添加场次
     */
    int create(SmsFlashSession promotionSession);

    /**
     * 修改场次
     */
    int update(Long id, SmsFlashSession promotionSession);

    /**
     * 修改场次启用状态
     */
    int updateStatus(Long id, Integer status);

    /**
     * 删除场次
     */
    int delete(Long id);

    /**
     * 获取场次详情
     */
    SmsFlashSession getItem(Long id);

    /**
     * 获取全部场次列表
     */
    List<SmsFlashSession> list();

    /**
     * 获取全部可选场次及其数量
     */
    List<SmsFlashPromotionSessionDetail> selectList(Long flashPromotionId);
}
