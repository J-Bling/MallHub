package com.mall.admin.productor.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mall.admin.productor.PromotionManage;
import com.mall.common.domain.ReSetPromotionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PromotionManageIMpl implements PromotionManage {
    @Autowired private ObjectMapper objectMapper;


    @Override
    public void setNextPromotion(ReSetPromotionModel promotionModel) {

    }

    @Override
    public void incrementProductStock(long productRelationId, long skuRelationId, int count) {

    }

    @Override
    public void delProduct(long sessionId, long productId, long productRelationId) {

    }

    @Override
    public void delSession(long sessionId) {

    }
}