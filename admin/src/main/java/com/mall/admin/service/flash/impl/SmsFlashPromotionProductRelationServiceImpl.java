package com.mall.admin.service.flash.impl;

import com.github.pagehelper.PageHelper;
import com.mall.admin.dao.flash.SmsFlashPromotionProductRelationDao;
import com.mall.admin.domain.flash.SmsFlashPromotionProduct;
import com.mall.admin.service.flash.SmsFlashPromotionProductRelationService;
import com.mall.mbg.mapper.SmsFlashProductRelationMapper;
import com.mall.mbg.model.SmsFlashProductRelation;
import com.mall.mbg.model.SmsFlashProductRelationExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 限时购商品关联管理Service实现类
 */
@Service
public class SmsFlashPromotionProductRelationServiceImpl implements SmsFlashPromotionProductRelationService {
    @Autowired
    private SmsFlashProductRelationMapper relationMapper;
    @Autowired
    private SmsFlashPromotionProductRelationDao relationDao;
    @Override
    public int create(List<SmsFlashProductRelation> relationList) {
        for (SmsFlashProductRelation relation : relationList) {
            relationMapper.insert(relation);
        }
        return relationList.size();
    }

    @Override
    public int update(Long id, SmsFlashProductRelation relation) {
        relation.setId(id);
        return relationMapper.updateByPrimaryKey(relation);
    }

    @Override
    public int delete(Long id) {
        return relationMapper.deleteByPrimaryKey(id);
    }

    @Override
    public SmsFlashProductRelation getItem(Long id) {
        return relationMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<SmsFlashPromotionProduct> list(Long flashPromotionId, Long flashPromotionSessionId, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        return relationDao.getList(flashPromotionId,flashPromotionSessionId);
    }

    @Override
    public long getCount(Long flashPromotionId, Long flashPromotionSessionId) {
        SmsFlashProductRelationExample example = new SmsFlashProductRelationExample();
        example.createCriteria()
                .andPromotionIdEqualTo(flashPromotionId)
                .andSessionIdEqualTo(flashPromotionSessionId);
        return relationMapper.countByExample(example);
    }
}
