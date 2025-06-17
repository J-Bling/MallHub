package com.mall.portal.cache;

import com.mall.common.constant.interfaces.Cache;
import com.mall.mbg.model.*;
import com.mall.portal.domain.model.ProductAlbums;

import java.io.Serializable;
import java.util.List;

public interface ProductCacheService extends Cache {
    /**
     * 新增 product 销量排行榜
     */
    Boolean addProductInSaleRank(long id,double score);
    /**
     * 新增新品榜
     */
    Boolean addProductNewRank(long id);
    /**
     * 获取 product 缓存
     */
    PmsProduct getProduct(long id);
    /**
     * 按销量排行降序分页获取 productList
     */
    List<PmsProduct> geSaleRanktList(int offset,int limit);
    /**
     * 按新品排行榜降序 分页获取productList
     */
    List<PmsProduct> getNewRankList(int offset,int limit);
    /**
     * 获取 ProductModel
     */
    ProductModel getProductModel(long productId);
    /**
     * 获取单个 sku 库存
     */
    PmsSkuStock getSkuStock(long productId,long skuId);
    /**
     * 获取该商品所有 sku 库存信息
     */
    List<PmsSkuStock> getSkuStockList(long productId);
    /**
     * 删除单个缓存 不删排行榜
     */
    void delProductCache(long id);
    /**
     * 删除 productMode 缓存 -> 修改了 productLadder价格阶梯  productFullReduction减满 productAlbum相册 productAttributeValueList商品属性值
     */
    void delProductModelCache(long productId);
    /**
     * 根据 productId 删除 该商品skuStock 全部缓存
     */
    void delSkuStock(long productId);
    /**
     * 增加销售额
     */
    void increaseSales(long id,int sales);


    class ProductModel implements Serializable {
        private Long productId;
        private List<PmsProductLadder> productLadderList;
        private List<PmsProductFullReduction> productFullReductionList;
        private ProductAlbums productAlbums;
        private List<PmsProductAttributeValue> productAttributeValueList;



        public Long getProductId() {
            return productId;
        }


        public ProductAlbums getProductAlbums() {
            return productAlbums;
        }

        public List<PmsProductLadder> getProductLadderList() {
            return productLadderList;
        }

        public List<PmsProductFullReduction> getProductFullReductionList() {
            return productFullReductionList;
        }

        public List<PmsProductAttributeValue> getProductAttributeValueList() {
            return productAttributeValueList;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public void setProductAlbums(ProductAlbums productAlbums) {
            this.productAlbums = productAlbums;
        }


        public void setProductFullReductionList(List<PmsProductFullReduction> productFullReductionList) {
            this.productFullReductionList = productFullReductionList;
        }

        public void setProductLadderList(List<PmsProductLadder> productLadderList) {
            this.productLadderList = productLadderList;
        }

        public void setProductAttributeValueList(List<PmsProductAttributeValue> productAttributeValueList) {
            this.productAttributeValueList = productAttributeValueList;
        }
    }

    class CacheKeys{
        public static String ProductKey(long id){return "product-key:"+id;}
        public static String ProductModelKey(long productId){return "product-model-key:"+productId;}
        public static String ProductKeyLock(long id){return "product-lock-key:"+id;}
        public static String ProductModelKeyLock(long productId){return "product-model-lock-key:"+productId;}
        public static String SkuStockHashKey(long productId){return "sku-stock-hash-key:"+productId;}
        public static String Field(long id){return ""+id;}
        public static String ProductNewRank = "product-new-rank";
        public static String ProductSaleRank = "product-sale-rank";
    }
}
