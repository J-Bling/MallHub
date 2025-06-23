package com.mall.admin.service.impl;

import com.mall.admin.service.PmsBrandService;
import com.mall.admin.dao.pms.PmsBrandDao;
import com.mall.mbg.model.PmsBrand;
import com.mall.common.exception.BusinessException;
import com.mall.common.enums.BusinessErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class PmsBrandServiceImpl implements PmsBrandService {

    @Autowired
    private PmsBrandDao brandDao;

    @Override
    public int createBrand(PmsBrand brand) {
        // 参数校验
        validateBrand(brand);

        // 检查品牌名称是否已存在
        if (brandDao.countByName(brand.getName()) > 0) {
            throw new BusinessException(BusinessErrorCode.BRAND_NAME_EXISTED);
        }

        // 设置默认值
        if (brand.getSort() == null) {
            brand.setSort(0);
        }
        if (brand.getShowStatus() == null) {
            brand.setShowStatus(1);
        }
        if (brand.getFactoryStatus() == null) {
            brand.setFactoryStatus(1);
        }

        return brandDao.insertBrand(brand);
    }

    @Override
    public int updateBrand(Long id, PmsBrand brand) {
        // 参数校验
        if (id == null) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "品牌ID不能为空");
        }
        validateBrand(brand);

        // 检查品牌是否存在
        PmsBrand existingBrand = brandDao.selectById(id);
        if (existingBrand == null) {
            throw new BusinessException(BusinessErrorCode.BRAND_NOT_FOUND);
        }

        // 检查品牌名称是否已被其他品牌使用
        if (!existingBrand.getName().equals(brand.getName())) {
            if (brandDao.countByName(brand.getName()) > 0) {
                throw new BusinessException(BusinessErrorCode.BRAND_NAME_EXISTED);
            }
        }

        brand.setId(id);
        return brandDao.updateBrand(brand);
    }

    @Override
    public int deleteBrand(Long id) {
        if (id == null) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "品牌ID不能为空");
        }

        // 检查品牌是否存在
        PmsBrand brand = brandDao.selectById(id);
        if (brand == null) {
            throw new BusinessException(BusinessErrorCode.BRAND_NOT_FOUND);
        }

        // 检查是否有商品关联该品牌
        if (brandDao.countProductByBrandId(id) > 0) {
            throw new BusinessException(BusinessErrorCode.BRAND_HAS_PRODUCTS);
        }

        return brandDao.deleteBrand(id);
    }

    @Override
    public PmsBrand getBrand(Long id) {
        if (id == null) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "品牌ID不能为空");
        }

        PmsBrand brand = brandDao.selectById(id);
        if (brand == null) {
            throw new BusinessException(BusinessErrorCode.BRAND_NOT_FOUND);
        }
        return brand;
    }

    @Override
    public List<PmsBrand> listBrands() {
        return brandDao.selectAll();
    }

    @Override
    public int updateShowStatus(Long id, Integer showStatus) {
        if (id == null || showStatus == null) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "参数不能为空");
        }

        // 检查品牌是否存在
        if (brandDao.selectById(id) == null) {
            throw new BusinessException(BusinessErrorCode.BRAND_NOT_FOUND);
        }

        return brandDao.updateShowStatus(id, showStatus);
    }

    @Override
    public int updateFactoryStatus(Long id, Integer factoryStatus) {
        if (id == null || factoryStatus == null) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "参数不能为空");
        }

        // 检查品牌是否存在
        if (brandDao.selectById(id) == null) {
            throw new BusinessException(BusinessErrorCode.BRAND_NOT_FOUND);
        }

        return brandDao.updateFactoryStatus(id, factoryStatus);
    }

    @Override
    public int incrementProductCount(Long id, Integer count) {
        return brandDao.incrementProductCount(id,count);
    }

    @Override
    public int incrementProductCommentCount(Long id, Integer count) {
        return brandDao.incrementProductCommentCount(id,count);
    }

    /**
     * 验证品牌信息
     */
    private void validateBrand(PmsBrand brand) {
        if (brand == null) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "品牌信息不能为空");
        }
        if (!StringUtils.hasText(brand.getName())) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "品牌名称不能为空");
        }
        if (brand.getName().length() > 64) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "品牌名称不能超过64个字符");
        }
        if (brand.getFirstLetter() != null && brand.getFirstLetter().length() > 8) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "品牌首字母不能超过8个字符");
        }
    }
}