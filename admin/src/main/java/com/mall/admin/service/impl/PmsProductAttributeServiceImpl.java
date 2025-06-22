package com.mall.admin.service.impl;

import com.mall.admin.dao.pms.PmsProductAttributeCategoryDao;
import com.mall.admin.dao.pms.PmsProductAttributeDao;
import com.mall.admin.domain.pms.CategoryAttributeRelationDTO;
import com.mall.admin.service.PmsProductAttributeService;
import com.mall.common.exception.BusinessException;
import com.mall.common.enums.BusinessErrorCode;
import com.mall.mbg.model.PmsProductAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class PmsProductAttributeServiceImpl implements PmsProductAttributeService {

    @Autowired
    private PmsProductAttributeDao attributeDao;

    @Autowired
    private PmsProductAttributeCategoryDao attributeCategoryDao;

    @Override
    public int createAttribute(PmsProductAttribute attribute) {
        // 参数校验
        validateAttribute(attribute);

        // 检查属性分类是否存在
        if (attributeCategoryDao.selectById(attribute.getProductAttributeCategoryId()) == null) {
            throw new BusinessException(BusinessErrorCode.ATTRIBUTE_CATEGORY_NOT_FOUND);
        }

        // 检查属性名称是否已存在
        if (attributeDao.countByNameAndCategoryId(attribute.getName(), attribute.getProductAttributeCategoryId()) > 0) {
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

        return attributeDao.insertAttribute(attribute);
    }

    @Override
    public int updateAttribute(Long id, PmsProductAttribute attribute) {
        // 参数校验
        if (id == null) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "属性ID不能为空");
        }
        validateAttribute(attribute);

        // 检查属性是否存在
        PmsProductAttribute existingAttribute = attributeDao.selectById(id);
        if (existingAttribute == null) {
            throw new BusinessException(BusinessErrorCode.ATTRIBUTE_NOT_FOUND);
        }

        // 检查属性分类是否存在
        if (attributeCategoryDao.selectById(attribute.getProductAttributeCategoryId()) == null) {
            throw new BusinessException(BusinessErrorCode.ATTRIBUTE_CATEGORY_NOT_FOUND);
        }

        // 检查属性名称是否已被其他属性使用
        if (!existingAttribute.getName().equals(attribute.getName()) ||
                !existingAttribute.getProductAttributeCategoryId().equals(attribute.getProductAttributeCategoryId())) {
            if (attributeDao.countByNameAndCategoryId(attribute.getName(), attribute.getProductAttributeCategoryId()) > 0) {
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
        PmsProductAttribute attribute = attributeDao.selectById(id);
        if (attribute == null) {
            throw new BusinessException(BusinessErrorCode.ATTRIBUTE_NOT_FOUND);
        }

        // 检查是否有商品关联该属性
        if (attributeDao.countProductByAttributeId(id) > 0) {
            throw new BusinessException(BusinessErrorCode.ATTRIBUTE_HAS_PRODUCTS);
        }

        return attributeDao.deleteAttribute(id);
    }

    @Override
    public PmsProductAttribute getAttribute(Long id) {
        if (id == null) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "属性ID不能为空");
        }

        PmsProductAttribute attribute = attributeDao.selectById(id);
        if (attribute == null) {
            throw new BusinessException(BusinessErrorCode.ATTRIBUTE_NOT_FOUND);
        }
        return attribute;
    }

    @Override
    public List<PmsProductAttribute> listAttributes(Long attributeCategoryId) {
        if (attributeCategoryId == null) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "属性分类ID不能为空");
        }

        // 检查属性分类是否存在
        if (attributeCategoryDao.selectById(attributeCategoryId) == null) {
            throw new BusinessException(BusinessErrorCode.ATTRIBUTE_CATEGORY_NOT_FOUND);
        }

        return attributeDao.selectByCategoryId(attributeCategoryId);
    }

    @Override
    public CategoryAttributeRelationDTO getCategoryAttributeRelation(Long categoryId) {
        if (categoryId == null) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "分类ID不能为空");
        }

        CategoryAttributeRelationDTO dto = new CategoryAttributeRelationDTO();
        dto.setCategoryId(categoryId);

        // 获取已关联的属性
        dto.setRelatedAttributes(attributeDao.selectByProductCategoryId(categoryId));

        // 获取可选的属性（同一属性分类下的其他属性）
        if (!dto.getRelatedAttributes().isEmpty()) {
            Long attributeCategoryId = dto.getRelatedAttributes().get(0).getProductAttributeCategoryId();
            dto.setOptionalAttributes(attributeDao.selectOptionalByCategoryId(categoryId, attributeCategoryId));
        }

        return dto;
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