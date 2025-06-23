package com.mall.admin.service.flash.impl;

import com.mall.admin.domain.flash.SmsFlashPromotionSessionDetail;
import com.mall.admin.service.flash.SmsFlashPromotionProductRelationService;
import com.mall.admin.service.flash.SmsFlashPromotionSessionService;
import com.mall.mbg.mapper.SmsFlashSessionMapper;
import com.mall.mbg.model.SmsFlashSession;
import com.mall.mbg.model.SmsFlashSessionExample;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 限时购场次管理Service实现类
 */
@Service
public class SmsFlashPromotionSessionServiceImpl implements SmsFlashPromotionSessionService {
    @Autowired
    private SmsFlashSessionMapper promotionSessionMapper;
    @Autowired
    private SmsFlashPromotionProductRelationService relationService;

    @Override
    public int create(SmsFlashSession promotionSession) {
        promotionSession.setCreateTime(new Date());
        return promotionSessionMapper.insert(promotionSession);
    }

    @Override
    public int update(Long id, SmsFlashSession promotionSession) {
        promotionSession.setId(id);
        return promotionSessionMapper.updateByPrimaryKey(promotionSession);
    }

    @Override
    public int updateStatus(Long id, Integer status) {
        SmsFlashSession promotionSession = new SmsFlashSession();
        promotionSession.setId(id);
        promotionSession.setStatus(status==1);
        return promotionSessionMapper.updateByPrimaryKeySelective(promotionSession);
    }

    @Override
    public int delete(Long id) {
        return promotionSessionMapper.deleteByPrimaryKey(id);
    }

    @Override
    public SmsFlashSession getItem(Long id) {
        return promotionSessionMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<SmsFlashSession> list() {
        SmsFlashSessionExample example = new SmsFlashSessionExample();
        return promotionSessionMapper.selectByExample(example);
    }

    @Override
    public List<SmsFlashPromotionSessionDetail> selectList(Long flashPromotionId) {
        List<SmsFlashPromotionSessionDetail> result = new ArrayList<>();
        SmsFlashSessionExample example = new SmsFlashSessionExample();
        example.createCriteria().andStatusEqualTo(true);
        List<SmsFlashSession> list = promotionSessionMapper.selectByExample(example);
        for (SmsFlashSession promotionSession : list) {
            SmsFlashPromotionSessionDetail detail = new SmsFlashPromotionSessionDetail();
            BeanUtils.copyProperties(promotionSession, detail);
            long count = relationService.getCount(flashPromotionId, promotionSession.getId());
            detail.setProductCount(count);
            result.add(detail);
        }
        return result;
    }
}
