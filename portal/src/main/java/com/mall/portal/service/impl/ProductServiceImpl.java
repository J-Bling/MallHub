package com.mall.portal.service.impl;

import com.mall.mbg.mapper.*;
import com.mall.mbg.model.*;
import com.mall.portal.cache.BrandCacheService;
import com.mall.portal.cache.ProductAttributeCacheService;
import com.mall.portal.cache.ProductCacheService;
import com.mall.portal.cache.ProductCategoryCacheService;
import com.mall.portal.domain.enums.SortTypeEnum;
import com.mall.portal.domain.model.product.ProductDetail;
import com.mall.portal.service.CouponService;
import com.mall.portal.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired private PmsProductMapper productMapper;
    @Autowired private ProductCategoryCacheService categoryCacheService;
    @Autowired private ProductCacheService productCacheService;
    @Autowired private ProductAttributeCacheService productAttributeCacheService;
    @Autowired private BrandCacheService brandCacheService;
    @Autowired private CouponService couponService;
    @Autowired private PmsProductFullReductionMapper fullReductionMapper;
    @Autowired private PmsProductLadderMapper ladderMapper;

    @Value("${cache.allow.product}")
    private Boolean allowProductCache;


    @Override
    public List<PmsProduct> search(String keyword, Long brandId, Long productCategoryId, Integer pageNum, Integer pageSize, SortTypeEnum sort) {
        if(pageNum == null || pageNum < 0) pageNum = 0;
        if(pageSize == null || pageSize < 5 || pageSize > 30) pageSize = 6;
        PmsProductExample example = new PmsProductExample();
        PmsProductExample.Criteria criteria = example.createCriteria();
        criteria.andDeleteStatusEqualTo(0);
        criteria.andPublishStatusEqualTo(1);
        if (keyword!=null && !keyword.isEmpty()){
            criteria.andNameLike("%"+keyword+"%");
        }
        if (brandId!=null && brandId >0){
            criteria.andBrandIdEqualTo(brandId);
        }
        if (productCategoryId!=null && productCategoryId>0){
            criteria.andProductCategoryIdEqualTo(productCategoryId);
        }
        example.setOrderByClause(sort.getOrderByClause()+" limit "+pageNum*pageSize+" ,"+pageSize);
        return productMapper.selectByExample(example);
    }

    @Override
    public List<PmsProductCategory> categoryTreeList() {
        return this.categoryCacheService.getAll();
    }


    @Override
    public ProductDetail detail(Long id) {
        ProductDetail productDetail = new ProductDetail();
        //获取商品
        PmsProduct product = productCacheService.getProduct(id);
        productDetail.setProduct(product);
        //获取 商品品牌
        PmsBrand brand = brandCacheService.getBrand(product.getBrandId());
        productDetail.setBrand(brand);
        //获取商品可用优惠券
        List<SmsCoupon> couponList =couponService.listByProduct(product.getId());
        productDetail.setCouponList(couponList);
        //获取商品属性分类
        PmsProductAttributeCategory category = productAttributeCacheService.getAttributeCategory(product.getProductAttributeCategoryId());
        //获取商品属性
        List<PmsProductAttribute> productAttributeList = productAttributeCacheService.getAttributeList(category.getId());
        productDetail.setProductAttributeList(productAttributeList);
        //获取商品所有 sku 库存信息
        List<PmsSkuStock> skuStockList = productCacheService.getSkuStockList(product.getId());
        productDetail.setSkuStockList(skuStockList);
        //获取 productModel
        ProductCacheService.ProductSubModel productModel = productCacheService.getProductModel(id);
        //获取 相册集
        productDetail.setProductAlbums(productModel.getProductAlbums());
        //获取商品减满
        productDetail.setProductFullReductionList(productModel.getProductFullReductionList());
        //获取商品价格阶梯
        productDetail.setProductLadderList(productModel.getProductLadderList());
        //获取商品属性参数值
        productDetail.setProductAttributeValueList(productModel.getProductAttributeValueList());
        return productDetail;
    }

    @Override
    public PmsProduct getProduct(long productId) {
        return this.productCacheService.getProduct(productId);
    }

    @Override
    public List<PmsSkuStock> getSkuStock(long productId) {
        return this.productCacheService.getSkuStockList(productId);
    }

    @Override
    public List<PmsProductAttribute> getProductAttribute(long productId) {
        return this.productAttributeCacheService.getAttributeList(productId);
    }

    @Override
    public PmsSkuStock getSkuStock(Long skuId,Long productId) {
        return this.productCacheService.getSkuStock(productId,skuId);
    }

    @Override
    public List<PmsProductFullReduction> getProductFullReductions(long productId) {
        PmsProductFullReductionExample example = new PmsProductFullReductionExample();
        example.createCriteria().andProductIdEqualTo(productId);
        return fullReductionMapper.selectByExample(example);
    }

    @Override
    public List<PmsProductLadder> getProductLadders(long productId) {
        PmsProductLadderExample example = new PmsProductLadderExample();
        example.createCriteria().andProductIdEqualTo(productId);
        return ladderMapper.selectByExample(example);
    }

    @Override
    public List<PmsProduct> recommendProducts(int offset, int limit,int type) {
        List<PmsProduct> productList = new ArrayList<>();
        List<Long> productIds = this.generateRecommendIds(offset,limit,type);
        if (productIds==null || productIds.isEmpty()){
            return productList;
        }
        if (allowProductCache==null||!allowProductCache){
            PmsProductExample example = new PmsProductExample();
            example.createCriteria().andIdIn(productIds);
            return productMapper.selectByExample(example);
        }
        for (Long id : productIds){
            productList.add(productCacheService.getProduct(id));
        }
        return productList;
    }

    private List<Long> generateRecommendIds(int offset, int limit,int type){
        List<Long> ids = null;
        if (type==1){
            //推荐算法

        } else if (type==2) {
            //销量排行
            ids= productCacheService.getSaleRankList(offset,limit);
        }else if (type==3){
            //新品排行
            ids=productCacheService.getNewRankList(offset,limit);
        }
        return ids;
    }
}