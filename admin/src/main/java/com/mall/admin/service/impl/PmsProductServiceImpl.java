package com.mall.admin.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mall.admin.dao.pms.*;
import com.mall.admin.domain.pms.ProductAttributeDetail;
import com.mall.admin.domain.pms.ProductParam;
import com.mall.admin.productor.ProductManage;
import com.mall.admin.service.PmsProductService;
import com.mall.common.exception.Assert;
import com.mall.mbg.mapper.CmsPrefrenceAreaProductRelationMapper;
import com.mall.mbg.mapper.CmsSubjectProductRelationMapper;
import com.mall.mbg.mapper.PmsProductMapper;
import com.mall.mbg.mapper.PmsSkuStockMapper;
import com.mall.mbg.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PmsProductServiceImpl implements PmsProductService {
    @Autowired
    private PmsProductDao productDao;

    @Autowired
    private PmsProductMapper productMapper;

    @Autowired
    private PmsAlbumDao albumDao;

    @Autowired
    private PmsAlbumPicDao albumPicDao;

    @Autowired
    private PmsAlbumProductRelationDao albumProductRelationDao;

    @Autowired
    private PmsBrandDao brandDao;

    @Autowired
    private PmsCommentDao commentDao;

    @Autowired
    private PmsMemberPriceDao memberPriceDao;

    @Autowired
    private PmsProductAttributeDao attributeDao;

    @Autowired
    private PmsProductCategoryDao productCategoryDao;

    @Autowired
    private PmsProductFullReductionDao fullReductionDao;

    @Autowired
    private PmsProductLadderDao ladderDao;

    @Autowired
    private PmsSkuStockDao skuStockDao;

    @Autowired
    private ProductManage productManage;

    @Autowired
    private CmsPrefrenceAreaProductRelationMapper areaProductRelationMapper;

    @Autowired
    private CmsSubjectProductRelationMapper subjectProductRelationMapper;

    @Autowired
    private PmsSkuStockMapper skuStockMapper;



    @Override
    @Transactional
    public void create(ProductParam productParam) throws JsonProcessingException {
        //创建商品
        PmsProduct product = productParam.getProduct();
        if (product==null){
            Assert.fail("参数错误");
        }
        product.setId(null);
        PmsProductExample pmsProductExample = new PmsProductExample();
        pmsProductExample.createCriteria().andNameEqualTo(product.getName());
        if (!productMapper.selectByExample(pmsProductExample).isEmpty()){
            Assert.fail("商品名称已经存在");
        }
        //设置商品类型
        PmsProductCategory productCategory = productParam.getProductCategory();
        if (productCategory!=null){
            product.setProductCategoryId(productCategory.getId());
            product.setProductCategoryName(productCategory.getName());
        }
        //设置属性类型
        PmsProductAttributeCategory attributeCategory = productParam.getProductAttributeDetailList().get(0).getAttributeCategory();
        if (attributeCategory!=null){
            product.setProductAttributeCategoryId(attributeCategory.getId());
        }
        //设置创建时间
        product.setCreateAt(System.currentTimeMillis());
        productMapper.insert(productParam.getProduct());
        Long productId = product.getId();
        if (productId==null){
            Assert.fail("插入商品失败");
        }
        //该商品属性商品数量增加 1
        productCategoryDao.updateProductCount(product.getId(),1);
        //会员价格
        List<PmsMemberPrice> memberPriceList = productParam.getMemberPriceList();
        if (memberPriceList!=null && !memberPriceList.isEmpty()){
            for (PmsMemberPrice memberPrice : memberPriceList){
                memberPrice.setProductId(productId);
            }
            memberPriceDao.batchInsert(memberPriceList);
        }
        //阶梯价格
        List<PmsProductLadder> ladderList = productParam.getProductLadderList();
        if (ladderList!=null && !ladderList.isEmpty()){
            for (PmsProductLadder ladder : ladderList){
                ladder.setProductId(productId);
            }
            ladderDao.batchInsert(ladderList);
        }
        //减满价格
        List<PmsProductFullReduction> fullReductionList = productParam.getProductFullReductionList();
        if (fullReductionList!=null && !fullReductionList.isEmpty()){
            for (PmsProductFullReduction reduction : fullReductionList){
                reduction.setProductId(productId);
            }
            fullReductionDao.batchInsert(fullReductionList);
        }
        //处理sku库存
        List<PmsSkuStock> skuStockList = productParam.getSkuStockList();
        if (skuStockList!=null && !skuStockList.isEmpty()){
            for (int i=0;i<skuStockList.size();i++){
                PmsSkuStock stock = skuStockList.get(i);
                SimpleDateFormat format = new SimpleDateFormat("yyyMMdd");
                String stringBuilder = format.format(new Date()) +
                        String.format("%04d", productId) +
                        String.format("%03d", i + 1);
                stock.setSkuCode(stringBuilder);
            }
            skuStockDao.batchInsert(skuStockList);
        }
        //添加自定义商品属性规格
        List<PmsProductAttributeValue> attributeValueList = new ArrayList<>();
        if (productParam.getProductAttributeDetailList()!=null){
            for (ProductAttributeDetail detail : productParam.getProductAttributeDetailList()){
                if (detail.getAttributeValueList()!=null && !detail.getAttributeValueList().isEmpty()){
                    attributeValueList.addAll(detail.getAttributeValueList());
                }
            }
            attributeDao.batchInsertProductAttributeValue(attributeValueList);
        }
        //关联专题
        if (productParam.getSubjectProductRelationList()!=null) {
            for (CmsSubjectProductRelation relation : productParam.getSubjectProductRelationList()) {
                subjectProductRelationMapper.insert(relation);
            }
        }
        //关联优选
        if (productParam.getPrefrenceAreaProductRelationList()!=null){
            for (CmsPrefrenceAreaProductRelation relation : productParam.getPrefrenceAreaProductRelationList()){
                areaProductRelationMapper.insert(relation);
            }
        }
        productManage.addProduct(productId);
    }

    @Override
    public ProductParam getProductParam(Long id) {
        ProductParam productParam = new ProductParam();
        PmsProduct product = productMapper.selectByPrimaryKey(id);
        if (product==null){
            Assert.fail("没有该商品");
        }
        productParam.setProduct(product);
        PmsProductCategory productCategory = productCategoryDao.selectById(product.getProductCategoryId());
        productParam.setProductCategory(productCategory);
        PmsBrand brand = brandDao.selectById(product.getBrandId());
        productParam.setBrand(brand);
        List<PmsMemberPrice> memberPriceList = memberPriceDao.selectByProductId(product.getId());
        productParam.setMemberPriceList(memberPriceList);
        List<PmsProductLadder> ladderList = ladderDao.selectByProductId(product.getId());
        productParam.setProductLadderList(ladderList);
        List<PmsProductFullReduction> fullReductionList = fullReductionDao.selectByProductId(product.getId());
        productParam.setProductFullReductionList(fullReductionList);
        List<PmsSkuStock> skuStockList = skuStockDao.selectByProductId(product.getId());
        productParam.setSkuStockList(skuStockList);
        CmsSubjectProductRelationExample subjectProductRelationExample = new CmsSubjectProductRelationExample();
        subjectProductRelationExample.createCriteria().andProductIdEqualTo(product.getId());
        List<CmsSubjectProductRelation> subjectProductRelationList = subjectProductRelationMapper.selectByExample(subjectProductRelationExample);
        productParam.setSubjectProductRelationList(subjectProductRelationList);
        CmsPrefrenceAreaProductRelationExample areaProductRelationExample = new CmsPrefrenceAreaProductRelationExample();
        areaProductRelationExample.createCriteria().andProductIdEqualTo(product.getId());
        productParam.setPrefrenceAreaProductRelationList(areaProductRelationMapper.selectByExample(areaProductRelationExample));
        List<ProductAttributeDetail> productAttributeDetailList = new ArrayList<>();
        PmsProductAttributeCategory productAttributeCategory = attributeDao.getProductAttributeCategory(product.getProductAttributeCategoryId());
        List<PmsProductAttribute> productAttributeList = attributeDao.getProductAttributeByProductCategoryId(productAttributeCategory.getId());
        for (PmsProductAttribute attribute : productAttributeList){
            ProductAttributeDetail detail = new ProductAttributeDetail();
            detail.setAttributeCategory(productAttributeCategory);
            detail.setAttribute(attribute);
            List<PmsProductAttributeValue> pmsProductAttributeValueList = attributeDao.getProductAttributeValueList(product.getId(),attribute.getId());
            detail.setAttributeValueList(pmsProductAttributeValueList);
            productAttributeDetailList.add(detail);
        }
        productParam.setProductAttributeDetailList(productAttributeDetailList);
        return productParam;
    }

    @Override
    @Transactional
    public void updateProduct(Long id, PmsProduct product) throws JsonProcessingException {
        PmsProduct pmsProduct = productMapper.selectByPrimaryKey(id);
        if (pmsProduct==null){
            Assert.fail("该商品不存在");
        }
        product.setId(id);
        product.setCreateAt(null);
        product.setProductAttributeCategoryId(null);
        product.setStock(null);
        product.setSale(null);
        productMapper.updateByPrimaryKeySelective(product);
        productManage.upToDelProductCache(id);
    }

    @Override
    public void updateSku(Long productId, List<PmsSkuStock> skuStockList) throws JsonProcessingException {
        if (skuStockList!=null && !skuStockList.isEmpty()){
            for (PmsSkuStock stock : skuStockList){
                stock.setProductId(null);
                stock.setSale(null);
                stock.setStock(null);
                skuStockMapper.updateByPrimaryKeySelective(stock);
            }
        }
        productManage.upToDelSkuStockCache(productId);
    }

    @Override
    @Transactional
    public void updateStockCount(Long productId, Map<Long, Integer> skuMap) throws JsonProcessingException {
        if (productId==null || skuMap==null || skuMap.isEmpty()){
            Assert.fail("参数错误");
        }
        Integer tol = 0;
        for (Map.Entry<Long,Integer> entry : skuMap.entrySet()){
            if (skuStockDao.selectById(entry.getKey())==null){
                Assert.fail("sku不存在");
            }
            skuStockDao.incrementStock(entry.getKey(),entry.getValue());
            tol+=entry.getValue();
        }
        productDao.incrementStock(productId,tol);
        for (Map.Entry<Long,Integer> entry : skuMap.entrySet()){
            productManage.upToDelStats(productId,entry.getKey());
        }
    }

    @Override
    public void updateProductPublishStatus(List<Long> ids, Integer publishStatus) {

    }

    @Override
    public void updateNewStatus(List<Long> ids, Integer newStatus) {

    }

    @Override
    public void updateDeleteStatus(List<Long> ids, Integer deleteStatus) {

    }

    @Override
    public void updateRecommendStatus(List<Long> ids, Integer recommendStatus) {

    }

    @Override
    public List<ProductParam> getByBrandId(Long brandId, int offset, int limit) {
        return Collections.emptyList();
    }

    @Override
    public List<ProductParam> getByCateId(Long cateId, int offset, int limit) {
        return Collections.emptyList();
    }
}
