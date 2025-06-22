package com.mall.admin.service.impl;

import com.mall.admin.dao.pms.PmsProductCategoryDao;
import com.mall.admin.domain.pms.ProductCategoryTreeNodeDTO;
import com.mall.admin.service.PmsProductCategoryService;
import com.mall.common.exception.BusinessException;
import com.mall.common.enums.BusinessErrorCode;
import com.mall.mbg.model.PmsProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PmsProductCategoryServiceImpl implements PmsProductCategoryService {

    @Autowired
    private PmsProductCategoryDao categoryDao;

    @Override
    public int createCategory(PmsProductCategory category) {
        // 参数校验
        validateCategory(category);

        // 检查分类名称是否已存在
        if (categoryDao.countByNameAndParentId(category.getName(), category.getParentId()) > 0) {
            throw new BusinessException(BusinessErrorCode.CATEGORY_NAME_EXISTED);
        }

        // 设置默认值
        if (category.getLevel() == null) {
            category.setLevel(category.getParentId() == 0 ? 0 : 1);
        }
        if (category.getSort() == null) {
            category.setSort(0);
        }
        if (category.getShowStatus() == null) {
            category.setShowStatus(1);
        }
        if (category.getNavStatus() == null) {
            category.setNavStatus(1);
        }
        if (category.getProductCount() == null) {
            category.setProductCount(0);
        }

        int count = categoryDao.insertCategory(category);

        // 更新父分类的商品数量
        if (count > 0 && category.getParentId() != 0) {
            categoryDao.updateProductCount(category.getParentId(), 1);
        }

        return count;
    }

    @Override
    public int updateCategory(Long id, PmsProductCategory category) {
        // 参数校验
        if (id == null) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "分类ID不能为空");
        }
        validateCategory(category);

        // 检查分类是否存在
        PmsProductCategory existingCategory = categoryDao.selectById(id);
        if (existingCategory == null) {
            throw new BusinessException(BusinessErrorCode.CATEGORY_NOT_FOUND);
        }

        // 检查分类名称是否已被其他分类使用
        if (!existingCategory.getName().equals(category.getName()) ||
                !existingCategory.getParentId().equals(category.getParentId())) {
            if (categoryDao.countByNameAndParentId(category.getName(), category.getParentId()) > 0) {
                throw new BusinessException(BusinessErrorCode.CATEGORY_NAME_EXISTED);
            }
        }

        // 检查父分类是否合法
        if (category.getParentId() != null && !existingCategory.getParentId().equals(category.getParentId())) {
            if (id.equals(category.getParentId())) {
                throw new BusinessException(BusinessErrorCode.CATEGORY_PARENT_INVALID);
            }
            if (categoryDao.selectById(category.getParentId()) == null) {
                throw new BusinessException(BusinessErrorCode.CATEGORY_PARENT_NOT_FOUND);
            }
        }

        category.setId(id);
        return categoryDao.updateCategory(category);
    }

    @Override
    public int deleteCategory(Long id) {
        if (id == null) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "分类ID不能为空");
        }

        // 检查分类是否存在
        PmsProductCategory category = categoryDao.selectById(id);
        if (category == null) {
            throw new BusinessException(BusinessErrorCode.CATEGORY_NOT_FOUND);
        }

        // 检查是否有子分类
        if (categoryDao.countByParentId(id) > 0) {
            throw new BusinessException(BusinessErrorCode.CATEGORY_HAS_CHILDREN);
        }

        // 检查是否有商品关联该分类
        if (category.getProductCount() > 0) {
            throw new BusinessException(BusinessErrorCode.CATEGORY_HAS_PRODUCTS);
        }

        int count = categoryDao.deleteCategory(id);

        // 更新父分类的商品数量
        if (count > 0 && category.getParentId() != 0) {
            categoryDao.updateProductCount(category.getParentId(), -1);
        }

        return count;
    }

    @Override
    public PmsProductCategory getCategory(Long id) {
        if (id == null) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "分类ID不能为空");
        }

        PmsProductCategory category = categoryDao.selectById(id);
        if (category == null) {
            throw new BusinessException(BusinessErrorCode.CATEGORY_NOT_FOUND);
        }
        return category;
    }

    @Override
    public List<ProductCategoryTreeNodeDTO> getCategoryTree() {
        // 获取所有一级分类
        List<PmsProductCategory> rootCategories = categoryDao.selectByParentId(0L);

        return rootCategories.stream()
                .map(this::convertToTreeNode)
                .collect(Collectors.toList());
    }

    @Override
    public int updateNavStatus(Long id, Integer navStatus) {
        if (id == null || navStatus == null) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "参数不能为空");
        }

        // 检查分类是否存在
        if (categoryDao.selectById(id) == null) {
            throw new BusinessException(BusinessErrorCode.CATEGORY_NOT_FOUND);
        }

        return categoryDao.updateNavStatus(id, navStatus);
    }

    @Override
    public int updateShowStatus(Long id, Integer showStatus) {
        if (id == null || showStatus == null) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "参数不能为空");
        }

        // 检查分类是否存在
        if (categoryDao.selectById(id) == null) {
            throw new BusinessException(BusinessErrorCode.CATEGORY_NOT_FOUND);
        }

        return categoryDao.updateShowStatus(id, showStatus);
    }

    @Override
    public List<Long> getCategoryAndChildrenIds(Long id) {
        if (id == null) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "分类ID不能为空");
        }

        List<Long> ids = new ArrayList<>();
        ids.add(id);

        // 递归获取所有子分类ID
        List<PmsProductCategory> children = categoryDao.selectByParentId(id);
        for (PmsProductCategory child : children) {
            ids.addAll(getCategoryAndChildrenIds(child.getId()));
        }

        return ids;
    }

    /**
     * 将分类转换为树节点
     */
    private ProductCategoryTreeNodeDTO convertToTreeNode(PmsProductCategory category) {
        ProductCategoryTreeNodeDTO node = new ProductCategoryTreeNodeDTO();
        node.setCategory(category);

        // 获取子分类
        List<PmsProductCategory> children = categoryDao.selectByParentId(category.getId());
        if (!children.isEmpty()) {
            node.setChildren(children.stream()
                    .map(this::convertToTreeNode)
                    .collect(Collectors.toList()));
        }

        return node;
    }

    /**
     * 验证分类信息
     */
    private void validateCategory(PmsProductCategory category) {
        if (category == null) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "分类信息不能为空");
        }
        if (!StringUtils.hasText(category.getName())) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "分类名称不能为空");
        }
        if (category.getName().length() > 64) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "分类名称不能超过64个字符");
        }
        if (category.getParentId() == null) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "父分类ID不能为空");
        }
    }
}