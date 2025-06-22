package com.mall.admin.service.impl;

import com.mall.admin.dao.pms.*;
import com.mall.admin.domain.PageResult;
import com.mall.admin.domain.pms.*;
import com.mall.admin.service.PmsProductService;
import com.mall.common.exception.BusinessException;
import com.mall.common.enums.BusinessErrorCode;
import com.mall.mbg.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PmsProductServiceImpl implements PmsProductService {

    @Autowired
    private PmsProductDao productDao;

    @Autowired
    private PmsBrandDao brandDao;

    @Autowired
    private PmsProductCategoryDao productCategoryDao;

    @Autowired
    private PmsProductAttributeValueDao productAttributeValueDao;

    @Autowired
    private PmsSkuStockDao skuStockDao;

    @Autowired
    private PmsProductLadderDao productLadderDao;

    @Autowired
    private PmsProductFullReductionDao productFullReductionDao;

    @Autowired
    private PmsMemberPriceDao memberPriceDao;

    @Autowired
    private PmsProductAlbumRelationDao productAlbumRelationDao;

    @Override
    @Transactional
    public int createProduct(ProductCreateOrUpdateDTO productDTO) {
        // 参数校验
        validateProductDTO(productDTO);

        // 检查商品名称是否已存在
        if (productDao.countByName(productDTO.getProduct().getName()) > 0) {
            throw new BusinessException(BusinessErrorCode.PRODUCT_NAME_EXISTED);
        }

        // 检查货号是否已存在
        if (StringUtils.hasText(productDTO.getProduct().getProductSn()) &&
                productDao.countByProductSn(productDTO.getProduct().getProductSn()) > 0) {
            throw new BusinessException(BusinessErrorCode.PRODUCT_SN_EXISTED);
        }

        // 检查品牌是否存在
        if (productDTO.getProduct().getBrandId() != null &&
                brandDao.selectById(productDTO.getProduct().getBrandId()) == null) {
            throw new BusinessException(BusinessErrorCode.BRAND_NOT_FOUND);
        }

        // 检查分类是否存在
        if (productDTO.getProduct().getProductCategoryId() != null &&
                productCategoryDao.selectById(productDTO.getProduct().getProductCategoryId()) == null) {
            throw new BusinessException(BusinessErrorCode.PRODUCT_CATEGORY_NOT_FOUND);
        }

        // 设置默认值
        PmsProduct product = productDTO.getProduct();
        if (product.getSort() == null) {
            product.setSort(0);
        }
        if (product.getGiftPoint() == null) {
            product.setGiftPoint(0);
        }
        if (product.getGiftGrowth() == null) {
            product.setGiftGrowth(0);
        }
        if (product.getUsePointLimit() == null) {
            product.setUsePointLimit(0);
        }
        if (product.getPublishStatus() == null) {
            product.setPublishStatus(0); // 默认下架
        }
        if (product.getVerifyStatus() == null) {
            product.setVerifyStatus(0); // 默认未审核
        }
        if (product.getDeleteStatus() == null) {
            product.setDeleteStatus(0); // 默认未删除
        }
        product.setCreateAt(System.currentTimeMillis());

        // 插入商品基本信息
        int count = productDao.insertProduct(product);
        if (count <= 0) {
            throw new BusinessException(BusinessErrorCode.PRODUCT_CREATE_FAILED);
        }

        Long productId = product.getId();

        // 保存商品属性值
        if (!CollectionUtils.isEmpty(productDTO.getProductAttributeValueList())) {
            productDTO.getProductAttributeValueList().forEach(value -> value.setProductId(productId));
            productAttributeValueDao.batchInsert(productDTO.getProductAttributeValueList());
        }

        // 保存SKU库存信息
        if (!CollectionUtils.isEmpty(productDTO.getSkuStockList())) {
            productDTO.getSkuStockList().forEach(sku -> sku.setProductId(productId));
            skuStockDao.batchInsert(productDTO.getSkuStockList());
        }

        // 保存阶梯价格
        if (!CollectionUtils.isEmpty(productDTO.getProductLadderList())) {
            productDTO.getProductLadderList().forEach(ladder -> ladder.setProductId(productId));
            productLadderDao.batchInsert(productDTO.getProductLadderList());
        }

        // 保存满减价格
        if (!CollectionUtils.isEmpty(productDTO.getProductFullReductionList())) {
            productDTO.getProductFullReductionList().forEach(reduction -> reduction.setProductId(productId));
            productFullReductionDao.batchInsert(productDTO.getProductFullReductionList());
        }

        // 保存会员价格
        if (!CollectionUtils.isEmpty(productDTO.getMemberPriceList())) {
            productDTO.getMemberPriceList().forEach(price -> price.setProductId(productId));
            memberPriceDao.batchInsert(productDTO.getMemberPriceList());
        }

        // 保存相册图片
        if (!CollectionUtils.isEmpty(productDTO.getAlbumPicIds())) {
            productAlbumRelationDao.batchInsert(productId, productDTO.getAlbumPicIds());
        }

        return count;
    }

    @Override
    @Transactional
    public int updateProduct(Long id, ProductCreateOrUpdateDTO productDTO) {
        // 参数校验
        if (id == null) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "商品ID不能为空");
        }
        validateProductDTO(productDTO);

        // 检查商品是否存在
        PmsProduct existingProduct = productDao.selectById(id);
        if (existingProduct == null) {
            throw new BusinessException(BusinessErrorCode.PRODUCT_NOT_FOUND);
        }

        // 检查商品名称是否已被其他商品使用
        if (!existingProduct.getName().equals(productDTO.getProduct().getName()) &&
                productDao.countByName(productDTO.getProduct().getName()) > 0) {
            throw new BusinessException(BusinessErrorCode.PRODUCT_NAME_EXISTED);
        }

        // 检查货号是否已被其他商品使用
        if (StringUtils.hasText(productDTO.getProduct().getProductSn()) &&
                !productDTO.getProduct().getProductSn().equals(existingProduct.getProductSn()) &&
                productDao.countByProductSn(productDTO.getProduct().getProductSn()) > 0) {
            throw new BusinessException(BusinessErrorCode.PRODUCT_SN_EXISTED);
        }

        // 检查品牌是否存在
        if (productDTO.getProduct().getBrandId() != null &&
                brandDao.selectById(productDTO.getProduct().getBrandId()) == null) {
            throw new BusinessException(BusinessErrorCode.BRAND_NOT_FOUND);
        }

        // 检查分类是否存在
        if (productDTO.getProduct().getProductCategoryId() != null &&
                productCategoryDao.selectById(productDTO.getProduct().getProductCategoryId()) == null) {
            throw new BusinessException(BusinessErrorCode.PRODUCT_CATEGORY_NOT_FOUND);
        }

        // 更新商品基本信息
        PmsProduct product = productDTO.getProduct();
        product.setId(id);
        int count = productDao.updateProduct(product);
        if (count <= 0) {
            throw new BusinessException(BusinessErrorCode.PRODUCT_UPDATE_FAILED);
        }

        // 更新商品属性值
        productAttributeValueDao.deleteByProductId(id);
        if (!CollectionUtils.isEmpty(productDTO.getProductAttributeValueList())) {
            productDTO.getProductAttributeValueList().forEach(value -> value.setProductId(id));
            productAttributeValueDao.batchInsert(productDTO.getProductAttributeValueList());
        }

        // 更新SKU库存信息
        skuStockDao.deleteByProductId(id);
        if (!CollectionUtils.isEmpty(productDTO.getSkuStockList())) {
            productDTO.getSkuStockList().forEach(sku -> sku.setProductId(id));
            skuStockDao.batchInsert(productDTO.getSkuStockList());
        }

        // 更新阶梯价格
        productLadderDao.deleteByProductId(id);
        if (!CollectionUtils.isEmpty(productDTO.getProductLadderList())) {
            productDTO.getProductLadderList().forEach(ladder -> ladder.setProductId(id));
            productLadderDao.batchInsert(productDTO.getProductLadderList());
        }

        // 更新满减价格
        productFullReductionDao.deleteByProductId(id);
        if (!CollectionUtils.isEmpty(productDTO.getProductFullReductionList())) {
            productDTO.getProductFullReductionList().forEach(reduction -> reduction.setProductId(id));
            productFullReductionDao.batchInsert(productDTO.getProductFullReductionList());
        }

        // 更新会员价格
        memberPriceDao.deleteByProductId(id);
        if (!CollectionUtils.isEmpty(productDTO.getMemberPriceList())) {
            productDTO.getMemberPriceList().forEach(price -> price.setProductId(id));
            memberPriceDao.batchInsert(productDTO.getMemberPriceList());
        }

        // 更新相册图片
        productAlbumRelationDao.deleteByProductId(id);
        if (!CollectionUtils.isEmpty(productDTO.getAlbumPicIds())) {
            productAlbumRelationDao.batchInsert(id, productDTO.getAlbumPicIds());
        }

        return count;
    }

    @Override
    public int updateDeleteStatus(List<Long> ids, Integer deleteStatus) {
        // 参数校验
        if (CollectionUtils.isEmpty(ids)) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "商品ID列表不能为空");
        }
        if (deleteStatus == null || (deleteStatus != 0 && deleteStatus != 1)) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "删除状态值无效");
        }

        // 检查所有商品是否存在
        List<PmsProduct> products = productDao.selectByIds(ids);
        if (products.size() != ids.size()) {
            List<Long> existingIds = products.stream().map(PmsProduct::getId).collect(Collectors.toList());
            List<Long> notFoundIds = ids.stream().filter(id -> !existingIds.contains(id)).collect(Collectors.toList());
            throw new BusinessException(BusinessErrorCode.PRODUCT_NOT_FOUND, "以下商品不存在: " + notFoundIds);
        }

        return productDao.updateDeleteStatus(ids, deleteStatus);
    }

    @Override
    public int updatePublishStatus(List<Long> ids, Integer publishStatus) {
        // 参数校验
        if (CollectionUtils.isEmpty(ids)) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "商品ID列表不能为空");
        }
        if (publishStatus == null || (publishStatus != 0 && publishStatus != 1)) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "上架状态值无效");
        }

        // 检查所有商品是否存在
        List<PmsProduct> products = productDao.selectByIds(ids);
        if (products.size() != ids.size()) {
            List<Long> existingIds = products.stream().map(PmsProduct::getId).collect(Collectors.toList());
            List<Long> notFoundIds = ids.stream().filter(id -> !existingIds.contains(id)).collect(Collectors.toList());
            throw new BusinessException(BusinessErrorCode.PRODUCT_NOT_FOUND, "以下商品不存在: " + notFoundIds);
        }

        return productDao.updatePublishStatus(ids, publishStatus);
    }

    @Override
    public int updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        // 参数校验
        if (CollectionUtils.isEmpty(ids)) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "商品ID列表不能为空");
        }
        if (recommendStatus == null || (recommendStatus != 0 && recommendStatus != 1)) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "推荐状态值无效");
        }

        // 检查所有商品是否存在
        List<PmsProduct> products = productDao.selectByIds(ids);
        if (products.size() != ids.size()) {
            List<Long> existingIds = products.stream().map(PmsProduct::getId).collect(Collectors.toList());
            List<Long> notFoundIds = ids.stream().filter(id -> !existingIds.contains(id)).collect(Collectors.toList());
            throw new BusinessException(BusinessErrorCode.PRODUCT_NOT_FOUND, "以下商品不存在: " + notFoundIds);
        }

        return productDao.updateRecommendStatus(ids, recommendStatus);
    }

    @Override
    public int updateNewStatus(List<Long> ids, Integer newStatus) {
        // 参数校验
        if (CollectionUtils.isEmpty(ids)) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "商品ID列表不能为空");
        }
        if (newStatus == null || (newStatus != 0 && newStatus != 1)) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "新品状态值无效");
        }

        // 检查所有商品是否存在
        List<PmsProduct> products = productDao.selectByIds(ids);
        if (products.size() != ids.size()) {
            List<Long> existingIds = products.stream().map(PmsProduct::getId).collect(Collectors.toList());
            List<Long> notFoundIds = ids.stream().filter(id -> !existingIds.contains(id)).collect(Collectors.toList());
            throw new BusinessException(BusinessErrorCode.PRODUCT_NOT_FOUND, "以下商品不存在: " + notFoundIds);
        }

        return productDao.updateNewStatus(ids, newStatus);
    }

    @Override
    public int updateVerifyStatus(List<Long> ids, Integer verifyStatus, String detail) {
        // 参数校验
        if (CollectionUtils.isEmpty(ids)) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "商品ID列表不能为空");
        }
        if (verifyStatus == null || (verifyStatus != 0 && verifyStatus != 1)) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "审核状态值无效");
        }

        // 检查所有商品是否存在
        List<PmsProduct> products = productDao.selectByIds(ids);
        if (products.size() != ids.size()) {
            List<Long> existingIds = products.stream().map(PmsProduct::getId).collect(Collectors.toList());
            List<Long> notFoundIds = ids.stream().filter(id -> !existingIds.contains(id)).collect(Collectors.toList());
            throw new BusinessException(BusinessErrorCode.PRODUCT_NOT_FOUND, "以下商品不存在: " + notFoundIds);
        }

        return productDao.updateVerifyStatus(ids, verifyStatus, detail);
    }

    @Override
    public ProductDetailDTO getProductDetail(Long id) {
        // 参数校验
        if (id == null) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "商品ID不能为空");
        }

        // 获取商品基本信息
        PmsProduct product = productDao.selectById(id);
        if (product == null) {
            throw new BusinessException(BusinessErrorCode.PRODUCT_NOT_FOUND);
        }

        // 构建返回结果
        ProductDetailDTO detailDTO = new ProductDetailDTO();
        detailDTO.setProduct(product);

        // 获取商品分类信息
        if (product.getProductCategoryId() != null) {
            PmsProductCategory category = productCategoryDao.selectById(product.getProductCategoryId());
            detailDTO.setCategory(category);

            // 获取父分类名称
            if (category != null && category.getParentId() != null && category.getParentId() != 0) {
                PmsProductCategory parentCategory = productCategoryDao.selectById(category.getParentId());
                if (parentCategory != null) {
                    detailDTO.setParentCategoryName(parentCategory.getName());
                }
            }
        }

        // 获取品牌信息
        if (product.getBrandId() != null) {
            PmsBrand brand = brandDao.selectById(product.getBrandId());
            detailDTO.setBrand(brand);
        }

        // 获取商品属性值
        List<PmsProductAttributeValue> attributeValues = productAttributeValueDao.selectByProductId(id);
        detailDTO.setProductAttributeValueList(attributeValues);

        // 获取SKU库存信息
        List<PmsSkuStock> skuStocks = skuStockDao.selectByProductId(id);
        detailDTO.setSkuStockList(skuStocks);

        // 获取阶梯价格
        List<PmsProductLadder> productLadders = productLadderDao.selectByProductId(id);
        detailDTO.setProductLadderList(productLadders);

        // 获取满减价格
        List<PmsProductFullReduction> productFullReductions = productFullReductionDao.selectByProductId(id);
        detailDTO.setProductFullReductionList(productFullReductions);

        // 获取会员价格
        List<PmsMemberPrice> memberPrices = memberPriceDao.selectByProductId(id);
        detailDTO.setMemberPriceList(memberPrices);

        // 获取相册图片
        List<Long> albumPicIds = productAlbumRelationDao.selectAlbumPicIdsByProductId(id);
        detailDTO.setAlbumPicIds(albumPicIds);

        return detailDTO;
    }

    @Override
    public PageResult<ProductListItemDTO> listProducts(ProductQueryDTO queryDTO) {
        // 参数校验
        if (queryDTO == null) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "查询参数不能为空");
        }
        if (queryDTO.getPageNum() == null || queryDTO.getPageSize() == null) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "分页参数不能为空");
        }

        // 查询商品总数
        int total = productDao.countByCondition(queryDTO);

        // 查询商品列表
        List<PmsProduct> products = productDao.selectByCondition(queryDTO);

        // 转换为DTO列表
        List<ProductListItemDTO> list = products.stream().map(product -> {
            ProductListItemDTO itemDTO = new ProductListItemDTO();
            itemDTO.setProduct(product);

            // 设置品牌名称
            if (product.getBrandId() != null) {
                PmsBrand brand = brandDao.selectById(product.getBrandId());
                if (brand != null) {
                    itemDTO.setBrandName(brand.getName());
                }
            }

            // 设置分类名称
            if (product.getProductCategoryId() != null) {
                PmsProductCategory category = productCategoryDao.selectById(product.getProductCategoryId());
                if (category != null) {
                    itemDTO.setCategoryName(category.getName());
                }
            }

            return itemDTO;
        }).collect(Collectors.toList());

        // 构建分页结果
        PageResult<ProductListItemDTO> result = new PageResult<>();
        result.setPageNum(queryDTO.getPageNum());
        result.setPageSize(queryDTO.getPageSize());
        result.setTotal(total);
        result.setList(list);

        return result;
    }

    @Override
    public List<PmsProduct> searchProducts(String keyword) {
        // 参数校验
        if (!StringUtils.hasText(keyword)) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "搜索关键词不能为空");
        }

        return productDao.selectByKeyword(keyword);
    }

    /**
     * 验证商品DTO
     */
    private void validateProductDTO(ProductCreateOrUpdateDTO productDTO) {
        if (productDTO == null) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "商品信息不能为空");
        }
        if (productDTO.getProduct() == null) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "商品基本信息不能为空");
        }
        if (!StringUtils.hasText(productDTO.getProduct().getName())) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "商品名称不能为空");
        }
        if (productDTO.getProduct().getName().length() > 200) {
            throw new BusinessException(BusinessErrorCode.INVALID_PARAMETER, "商品名称不能超过200个字符");
        }
    }
}