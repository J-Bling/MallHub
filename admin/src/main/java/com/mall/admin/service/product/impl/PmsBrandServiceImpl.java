package com.mall.admin.service.product.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.mall.admin.cache.BrandCacheManage;
import com.mall.admin.domain.product.PmsBrandParam;
import com.mall.admin.productor.ProductManage;
import com.mall.admin.service.product.PmsBrandService;
import com.mall.mbg.mapper.PmsBrandMapper;
import com.mall.mbg.mapper.PmsProductMapper;
import com.mall.mbg.model.PmsBrand;
import com.mall.mbg.model.PmsBrandExample;
import com.mall.mbg.model.PmsProduct;
import com.mall.mbg.model.PmsProductExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商品品牌管理Service实现类
 */
@Service
public class PmsBrandServiceImpl implements PmsBrandService {
    @Autowired
    private PmsBrandMapper brandMapper;
    @Autowired
    private PmsProductMapper productMapper;
    @Autowired
    private BrandCacheManage brandCacheManage;
    @Autowired
    private ProductManage productManage;

    private final Logger logger = LoggerFactory.getLogger(PmsBrandServiceImpl.class);

    @Override
    public List<PmsBrand> listAllBrand() {
        return brandMapper.selectByExample(new PmsBrandExample());
    }

    @Override
    public int createBrand(PmsBrandParam pmsBrandParam) {
        PmsBrand pmsBrand = new PmsBrand();
        BeanUtils.copyProperties(pmsBrandParam, pmsBrand);
        //如果创建时首字母为空，取名称的第一个为首字母
        if (StrUtil.isEmpty(pmsBrand.getFirstLetter())) {
            pmsBrand.setFirstLetter(pmsBrand.getName().substring(0, 1));
        }
        PmsBrandExample example = new PmsBrandExample();
        example.createCriteria().andNameEqualTo(pmsBrand.getName());
        List<PmsBrand> brandList = brandMapper.selectByExample(example);
        if (!CollectionUtil.isEmpty(brandList)){
            throw new RuntimeException("该品牌名称已经被注册");
        }
        int status = brandMapper.insertSelective(pmsBrand);
        if (status>0){
            brandCacheManage.add(pmsBrand);
        }
        return status;
    }

    @Override
    public int updateBrand(Long id, PmsBrandParam pmsBrandParam) {
        PmsBrand pmsBrand = new PmsBrand();
        BeanUtils.copyProperties(pmsBrandParam, pmsBrand);
        pmsBrand.setId(id);
        //如果创建时首字母为空，取名称的第一个为首字母
        if (StrUtil.isEmpty(pmsBrand.getFirstLetter())) {
            pmsBrand.setFirstLetter(pmsBrand.getName().substring(0, 1));
        }
        //更新品牌时要更新商品中的品牌名称
        PmsProduct product = new PmsProduct();
        product.setBrandName(pmsBrand.getName());
        PmsProductExample example = new PmsProductExample();
        example.createCriteria().andBrandIdEqualTo(id);
        productMapper.updateByExampleSelective(product,example);

        PmsProductExample pmsProductExample = new PmsProductExample();
        pmsProductExample.createCriteria().andBrandIdEqualTo(id);
        List<PmsProduct> productList = productMapper.selectByExample(pmsProductExample);
        if (!CollectionUtil.isEmpty(productList)){
            try{
                for (PmsProduct pmsProduct : productList){
                    productManage.upToDelProductCache(pmsProduct.getId());
                }
            }catch (Exception e){
                logger.error("发生错误:{}",e.getMessage());
            }
        }

        int status = brandMapper.updateByPrimaryKeySelective(pmsBrand);
        if (status>0)
            brandCacheManage.add(brandMapper.selectByPrimaryKey(id));
        return status;
    }

    @Override
    public int deleteBrand(Long id) {
        int status = brandMapper.deleteByPrimaryKey(id);
        brandCacheManage.delBrandCache(id);
        return status;
    }

    @Override
    @Transactional
    public int deleteBrand(List<Long> ids) {
        PmsBrandExample pmsBrandExample = new PmsBrandExample();
        pmsBrandExample.createCriteria().andIdIn(ids);
        for (Long id : ids){
            int status = brandMapper.deleteByPrimaryKey(id);
            if (status>0)
                brandCacheManage.delBrandCache(id);
        }
        return 1;
    }

    @Override
    public List<PmsBrand> listBrand(String keyword, Integer showStatus, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        PmsBrandExample pmsBrandExample = new PmsBrandExample();
        pmsBrandExample.setOrderByClause("sort desc");
        PmsBrandExample.Criteria criteria = pmsBrandExample.createCriteria();
        if (!StrUtil.isEmpty(keyword)) {
            criteria.andNameLike("%" + keyword + "%");
        }
        if(showStatus!=null){
            criteria.andShowStatusEqualTo(showStatus);
        }
        return brandMapper.selectByExample(pmsBrandExample);
    }

    @Override
    public PmsBrand getBrand(Long id) {
        return brandMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateShowStatus(List<Long> ids, Integer showStatus) {
        PmsBrand pmsBrand = new PmsBrand();
        pmsBrand.setShowStatus(showStatus);
        PmsBrandExample pmsBrandExample = new PmsBrandExample();
        pmsBrandExample.createCriteria().andIdIn(ids);
        int status = brandMapper.updateByExampleSelective(pmsBrand, pmsBrandExample);
        if (status>0){
            for (Long id : ids){
                PmsBrand brand = brandMapper.selectByPrimaryKey(id);
                if (brand!=null){
                    brandCacheManage.add(brand);
                }
            }
        }
        return status;
    }

    @Override
    public int updateFactoryStatus(List<Long> ids, Integer factoryStatus) {
        PmsBrand pmsBrand = new PmsBrand();
        pmsBrand.setFactoryStatus(factoryStatus);
        PmsBrandExample pmsBrandExample = new PmsBrandExample();
        pmsBrandExample.createCriteria().andIdIn(ids);
        int status = brandMapper.updateByExampleSelective(pmsBrand, pmsBrandExample);
        if (status>0){
            for (Long id : ids){
                PmsBrand brand = brandMapper.selectByPrimaryKey(id);
                if (brand!=null){
                    brandCacheManage.add(brand);
                }
            }
        }
        return status;
    }
}
