package com.mall.admin.service.impl;

import com.mall.admin.dao.pms.PmsProductAttributeDao;
import com.mall.admin.domain.pms.ProductAttributeDetail;
import com.mall.admin.service.PmsProductAttributeService;
import com.mall.common.exception.BusinessException;
import com.mall.common.enums.BusinessErrorCode;
import com.mall.mbg.mapper.PmsProductAttributeCategoryMapper;
import com.mall.mbg.mapper.PmsProductAttributeMapper;
import com.mall.mbg.mapper.PmsProductAttributeValueMapper;
import com.mall.mbg.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class PmsProductAttributeServiceImpl implements PmsProductAttributeService {

    @Autowired
    private PmsProductAttributeDao attributeDao;

    @Autowired
    private PmsProductAttributeCategoryMapper attributeCategoryMapper;

    @Autowired
    private PmsProductAttributeMapper attributeMapper;

    @Autowired
    private PmsProductAttributeValueMapper attributeValueMapper;


    @Override
    public int createAttributeCategory(PmsProductAttributeCategory category) {
        if (category==null || category.getName()==null){
            return 0;
        }
        if (category.getAttributeCount()==null){
            category.setAttributeCount(0);
        }
        if (category.getParamCount()==null){
            category.setParamCount(0);
        }
        PmsProductAttributeCategoryExample example = new PmsProductAttributeCategoryExample();
        example.createCriteria().andNameEqualTo(category.getName());
        List<PmsProductAttributeCategory> categoryList = attributeCategoryMapper.selectByExample(example);
        if (categoryList==null || categoryList.isEmpty()){
            return attributeDao.insertAttributeCategory(category);
        }
        return 0;
    }

    @Override
    public int updateAttributeCategoryName(PmsProductAttributeCategory category) {
        category.setParamCount(null);
        category.setAttributeCount(null);
        PmsProductAttributeCategoryExample example = new PmsProductAttributeCategoryExample();
        example.createCriteria().andNameEqualTo(category.getName());
        List<PmsProductAttributeCategory> categoryList = attributeCategoryMapper.selectByExample(example);
        if (categoryList==null || categoryList.isEmpty()){
            return attributeCategoryMapper.updateByPrimaryKeySelective(category);
        }
        return 0;
    }

    @Override
    public int deleteAttributeCategory(Long categoryId) {
        if (attributeDao.countProductByAttributeCategoryId(categoryId)>0){
            throw new BusinessException(BusinessErrorCode.ATTRIBUTE_HAS_PRODUCTS);
        }
        return attributeDao.deleteAttributeCategory(categoryId);
    }

    @Override
    public List<PmsProductAttributeCategory> getProductAttributeCategoryAll() {
        return attributeDao.getProductAttributeCategoryAll();
    }

    @Override
    public int createAttribute(PmsProductAttribute attribute) {
        // 参数校验
        validateAttribute(attribute);

        // 检查属性分类是否存在
        if (attributeDao.getProductAttributeCategory(attribute.getProductAttributeCategoryId()) == null) {
            throw new BusinessException(BusinessErrorCode.ATTRIBUTE_CATEGORY_NOT_FOUND);
        }

        // 检查属性名称是否已存在
        PmsProductAttributeExample example = new PmsProductAttributeExample();
        example.createCriteria().andNameEqualTo(attribute.getName())
                .andProductAttributeCategoryIdEqualTo(attribute.getProductAttributeCategoryId());
        List<PmsProductAttribute> attributeList = attributeMapper.selectByExample(example);
        if (!attributeList.isEmpty()) {
            throw new BusinessException(BusinessErrorCode.ATTRIBUTE_NAME_EXISTED);
        }

        // 设置默认值
        if (attribute.getSort() == null) {
            attribute.setSort(0);
        }
        if (attribute.getType() == null) {
            attribute.setType(0); // 默认为规格属性
        }
        if (attribute.getSelectType() == null) {
            attribute.setSelectType(0); // 默认为唯一选择
        }
        if (attribute.getInputType() == null) {
            attribute.setInputType(0); // 默认为手工录入
        }

        int status = attributeDao.insertAttribute(attribute);
        attributeDao.incrementAttributeCount(attribute.getProductAttributeCategoryId(),1);
        return status;
    }

    @Override
    public int updateAttribute(Long id, PmsProductAttribute attribute) {
        // 参数校验
        if (id == null) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "属性ID不能为空");
        }
        validateAttribute(attribute);

        // 检查属性是否存在
        PmsProductAttribute existingAttribute = attributeDao.selectAttributeById(id);
        if (existingAttribute == null) {
            throw new BusinessException(BusinessErrorCode.ATTRIBUTE_NOT_FOUND);
        }

        // 检查属性分类是否存在
        if (attributeDao.selectAttributeByAttributeCategoryId(attribute.getProductAttributeCategoryId()) == null) {
            throw new BusinessException(BusinessErrorCode.ATTRIBUTE_CATEGORY_NOT_FOUND);
        }

        // 检查属性名称是否已被其他属性使用
        if (!existingAttribute.getName().equals(attribute.getName()) ||
                !existingAttribute.getProductAttributeCategoryId().equals(attribute.getProductAttributeCategoryId())) {
            PmsProductAttributeExample example = new PmsProductAttributeExample();
            example.createCriteria().andProductAttributeCategoryIdEqualTo(attribute.getProductAttributeCategoryId())
                    .andNameEqualTo(attribute.getName());
            if (!attributeMapper.selectByExample(example).isEmpty()) {
                throw new BusinessException(BusinessErrorCode.ATTRIBUTE_NAME_EXISTED);
            }
        }

        attribute.setId(id);
        return attributeDao.updateAttribute(attribute);
    }

    @Override
    public int deleteAttribute(Long id) {
        if (id == null) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "属性ID不能为空");
        }

        // 检查属性是否存在
        PmsProductAttribute attribute = attributeDao.selectAttributeById(id);
        if (attribute == null) {
            throw new BusinessException(BusinessErrorCode.ATTRIBUTE_NOT_FOUND);
        }

        // 检查是否有商品关联该属性
        if (attributeDao.countProductByAttributeCategoryId(attribute.getProductAttributeCategoryId()) > 0) {
            throw new BusinessException(BusinessErrorCode.ATTRIBUTE_HAS_PRODUCTS);
        }

        return attributeDao.deleteAttribute(id);
    }

    @Override
    public ProductAttributeDetail getAttributeDetail(Long id) {
        PmsProductAttribute attribute = attributeDao.selectAttributeById(id);
        if (attribute==null){
            throw new BusinessException(BusinessErrorCode.ATTRIBUTE_NOT_FOUND);
        }
        PmsProductAttributeCategory attributeCategory = attributeDao.getProductAttributeCategory(attribute.getProductAttributeCategoryId());
        List<PmsProductAttributeValue> attributeValueList = attributeDao.getProductAttributeValueList(id);
        ProductAttributeDetail detail = new ProductAttributeDetail();
        detail.setAttribute(attribute);
        detail.setAttributeCategory(attributeCategory);
        detail.setAttributeValueList(attributeValueList);
        return detail;
    }


    @Override
    public List<PmsProductAttribute> listAttributes(Long attributeCategoryId) {
        if (attributeCategoryId == null) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "属性分类ID不能为空");
        }

        return attributeDao.getProductAttributeByProductCategoryId(attributeCategoryId);
    }

    @Override
    @Transactional
    public int createAttributeValue(PmsProductAttributeValue value) {
        PmsProductAttribute attribute = attributeDao.selectAttributeById(value.getProductAttributeId());
        if (attribute==null){
            throw new BusinessException(BusinessErrorCode.ATTRIBUTE_NOT_FOUND);
        }
        if (value.getValue()==null || value.getValue().length()>64){
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER,"属性字段字数应该小于64");
        }
        if (value.getProductId()!=null){
            PmsProductAttributeValueExample example = new PmsProductAttributeValueExample();
            example.createCriteria().andProductIdEqualTo(value.getProductId())
                    .andProductAttributeIdEqualTo(value.getProductAttributeId())
                    .andValueEqualTo(value.getValue());
            if (!attributeValueMapper.selectByExample(example).isEmpty()){
                throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER,"重复相同属性值");
            }
        }
        int status = attributeDao.insertProductAttributeValue(value);
        if (status>0){
            attributeDao.incrementAttributeParamCount(attribute.getProductAttributeCategoryId(),1);
        }
        return status;
    }

    @Override
    public int createAttributeValues(List<PmsProductAttributeValue> values) {
        for (PmsProductAttributeValue value : values){
            this.createAttributeValue(value);
        }
        return 1;
    }

    @Override
    @Transactional
    public void deleteAttributeValue(Long attributeValueId) {
        PmsProductAttributeValue value = attributeDao.getProductAttributeValue(attributeValueId);
        if (value==null){
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER,"属性值不存在");
        }
        PmsProductAttribute attribute = attributeDao.selectAttributeById(value.getProductAttributeId());
        if (attribute==null){
            throw new BusinessException(BusinessErrorCode.ATTRIBUTE_NOT_FOUND);
        }
        PmsProductAttributeCategory category = attributeDao.getProductAttributeCategory(attribute.getProductAttributeCategoryId());
        if (category==null){
            throw new BusinessException(BusinessErrorCode.CATEGORY_NOT_FOUND);
        }
        attributeDao.deleteProductAttributeValue(value.getId());
        attributeDao.incrementAttributeParamCount(category.getId(),-1);
    }

    @Override
    public void updateAttributeValue(Long attributeValueId, String value) {
        attributeDao.updateProductAttributeValue(attributeValueId,value);
    }

    @Override
    @Transactional
    public void cleanAttribute(Long productId, Long attributeId) {
        PmsProductAttribute attribute = attributeDao.selectAttributeById(attributeId);
        if (attribute==null){
            throw new BusinessException(BusinessErrorCode.ATTRIBUTE_NOT_FOUND);
        }
        int count = attributeDao.cleanProductAttributeValue(productId,attributeId);
        attributeDao.incrementAttributeParamCount(attribute.getProductAttributeCategoryId(),-count);
    }

    /**
     * 验证属性信息
     */
    private void validateAttribute(PmsProductAttribute attribute) {
        if (attribute == null) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "属性信息不能为空");
        }
        if (!StringUtils.hasText(attribute.getName())) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "属性名称不能为空");
        }
        if (attribute.getName().length() > 64) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "属性名称不能超过64个字符");
        }
        if (attribute.getProductAttributeCategoryId() == null) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "属性分类ID不能为空");
        }
    }
}