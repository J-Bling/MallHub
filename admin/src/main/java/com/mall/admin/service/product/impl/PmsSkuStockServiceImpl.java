package com.mall.admin.service.product.impl;

import cn.hutool.core.util.StrUtil;
import com.mall.admin.dao.product.PmsSkuStockDao;
import com.mall.admin.productor.ProductManage;
import com.mall.admin.service.product.PmsSkuStockService;
import com.mall.mbg.mapper.PmsSkuStockMapper;
import com.mall.mbg.model.PmsSkuStock;
import com.mall.mbg.model.PmsSkuStockExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品SKU库存管理Service实现类
 */
@Service
public class PmsSkuStockServiceImpl implements PmsSkuStockService {
    @Autowired
    private PmsSkuStockMapper skuStockMapper;
    @Autowired
    private PmsSkuStockDao skuStockDao;
    @Autowired
    private ProductManage productManage;

    private final Logger logger = LoggerFactory.getLogger(PmsSkuStockServiceImpl.class);

    @Override
    public List<PmsSkuStock> getList(Long pid, String keyword) {
        PmsSkuStockExample example = new PmsSkuStockExample();
        PmsSkuStockExample.Criteria criteria = example.createCriteria().andProductIdEqualTo(pid);
        if (!StrUtil.isEmpty(keyword)) {
            criteria.andSkuCodeLike("%" + keyword + "%");
        }
        return skuStockMapper.selectByExample(example);
    }

    @Override
    public int update(Long pid, List<PmsSkuStock> skuStockList) {
        List<PmsSkuStock> filterSkuList = skuStockList.stream()
                .filter(item -> pid.equals(item.getProductId()))
                .collect(Collectors.toList());
        int status = skuStockDao.replaceList(filterSkuList);
        try{
            for (PmsSkuStock skuStock : filterSkuList){
                productManage.upToDelStats(pid,skuStock.getId());
            }
            productManage.upToDelSkuStockCache(pid);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return status;
    }
}
