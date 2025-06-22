package com.mall.admin.service.impl;

import com.mall.admin.dao.pms.*;
import com.mall.admin.service.PmsProductPromotionService;
import com.mall.common.exception.BusinessException;
import com.mall.common.enums.BusinessErrorCode;
import com.mall.mbg.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class PmsProductPromotionServiceImpl implements PmsProductPromotionService {

    @Autowired
    private PmsProductLadderDao productLadderDao;

    @Autowired
    private PmsProductFullReductionDao productFullReductionDao;

    @Autowired
    private PmsMemberPriceDao memberPriceDao;

    @Autowired
    private PmsProductDao productDao;

    @Override
    @Transactional
    public int setProductLadder(Long productId, List<PmsProductLadder> ladderList) {
        // 参数校验
        validateProductId(productId);

        // 删除原有阶梯价格
        productLadderDao.deleteByProductId(productId);

        // 插入新的阶梯价格
        if (!CollectionUtils.isEmpty(ladderList)) {
            // 设置默认值
            ladderList.forEach(ladder -> {
                ladder.setProductId(productId);
                if (ladder.getDiscount() == null) {
                    ladder.setDiscount(new BigDecimal("00"));
                }
                if (ladder.getPrice() == null) {
                    ladder.setPrice(new BigDecimal("0.0"));
                }
            });
            return productLadderDao.batchInsert(ladderList);
        }
        return 0;
    }

    @Override
    @Transactional
    public int setProductFullReduction(Long productId, List<PmsProductFullReduction> fullReductionList) {
        // 参数校验
        validateProductId(productId);

        // 删除原有满减价格
        productFullReductionDao.deleteByProductId(productId);

        // 插入新的满减价格
        if (!CollectionUtils.isEmpty(fullReductionList)) {
            // 设置默认值
            fullReductionList.forEach(reduction -> {
                reduction.setProductId(productId);
                if (reduction.getFullPrice() == null) {
                    reduction.setFullPrice(new BigDecimal("0.0"));
                }
                if (reduction.getReducePrice() == null) {
                    reduction.setReducePrice(new BigDecimal("0.0"));
                }
            });
            return productFullReductionDao.batchInsert(fullReductionList);
        }
        return 0;
    }

    @Override
    @Transactional
    public int setMemberPrice(Long productId, List<PmsMemberPrice> memberPriceList) {
        // 参数校验
        validateProductId(productId);

        // 删除原有会员价格
        memberPriceDao.deleteByProductId(productId);

        // 插入新的会员价格
        if (!CollectionUtils.isEmpty(memberPriceList)) {
            // 设置默认值
            memberPriceList.forEach(price -> {
                price.setProductId(productId);
                if (price.getMemberPrice() == null) {
                    price.setMemberPrice(new BigDecimal("0.0"));
                }
            });
            return memberPriceDao.batchInsert(memberPriceList);
        }
        return 0;
    }

    @Override
    public List<PmsProduct> getPromotionProducts() {
        Date now = new Date();
        return productDao.selectPromotionProducts(now);
    }

    /**
     * 验证商品ID
     */
    private void validateProductId(Long productId) {
        if (productId == null) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "商品ID不能为空");
        }
        // 检查商品是否存在
        if (productDao.selectById(productId) == null) {
            throw new BusinessException(BusinessErrorCode.PRODUCT_NOT_FOUND);
        }
    }
}