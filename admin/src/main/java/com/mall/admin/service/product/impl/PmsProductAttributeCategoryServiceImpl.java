package com.mall.admin.service.product.impl;

import com.github.pagehelper.PageHelper;
import com.mall.admin.dao.product.PmsProductAttributeCategoryDao;
import com.mall.admin.domain.product.PmsProductAttributeCategoryItem;
import com.mall.admin.productor.AttributeManage;
import com.mall.admin.service.product.PmsProductAttributeCategoryService;
import com.mall.mbg.mapper.PmsProductAttributeCategoryMapper;
import com.mall.mbg.model.PmsProductAttributeCategory;
import com.mall.mbg.model.PmsProductAttributeCategoryExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品属性分类管理Service实现类
 */
@Service
public class PmsProductAttributeCategoryServiceImpl implements PmsProductAttributeCategoryService {
    @Autowired
    private PmsProductAttributeCategoryMapper productAttributeCategoryMapper;
    @Autowired
    private PmsProductAttributeCategoryDao productAttributeCategoryDao;
    @Autowired
    private AttributeManage attributeManage;

    private final Logger logger = LoggerFactory.getLogger(PmsProductAttributeCategoryServiceImpl.class);

    @Override
    public int create(String name) {
        PmsProductAttributeCategory productAttributeCategory = new PmsProductAttributeCategory();
        productAttributeCategory.setName(name);
        int status = productAttributeCategoryMapper.insertSelective(productAttributeCategory);
        try{
            attributeManage.delAttributeCategoryAll();
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return status;
    }

    @Override
    public int update(Long id, String name) {
        PmsProductAttributeCategory productAttributeCategory = new PmsProductAttributeCategory();
        productAttributeCategory.setName(name);
        productAttributeCategory.setId(id);
        int status = productAttributeCategoryMapper.updateByPrimaryKeySelective(productAttributeCategory);
        try{
            attributeManage.delAttributeCategoryAll();
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return status;
    }

    @Override
    public int delete(Long id) {
        int status = productAttributeCategoryMapper.deleteByPrimaryKey(id);
        try{
            attributeManage.delAttributeCategoryById(id);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return status;
    }

    @Override
    public PmsProductAttributeCategory getItem(Long id) {
        return productAttributeCategoryMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<PmsProductAttributeCategory> getList(Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        return productAttributeCategoryMapper.selectByExample(new PmsProductAttributeCategoryExample());
    }

    @Override
    public List<PmsProductAttributeCategoryItem> getListWithAttr() {
        return productAttributeCategoryDao.getListWithAttr();
    }
}
