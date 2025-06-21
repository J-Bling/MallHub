package com.mall.portal.cache;

import com.mall.common.constant.interfaces.Cache;
import com.mall.mbg.model.*;
import com.mall.portal.domain.model.ProductAlbums;

import java.io.Serializable;
import java.util.List;

public interface ProductCacheService extends Cache {
    /**
     * 新增商品
     */
    void addProduct(long id);
    /**
     * 删除排行榜
     */
    void deleteRank();
    /**
     * 按销量排行降序分页获取 productList
     */
    List<Long> getSaleRankList(int offset, int limit);
    /**
     * 按新品排行榜降序 分页获取productIdsList
     */
    List<Long> getNewRankList(int offset,int limit);
    /**
     * 获取 product
     */
    PmsProduct getProduct(long id);
    /**
     * 获取 product 缓存
     */
    PmsProduct getProductCache(long id);
    /**
     * 获取 ProductModel
     */
    ProductSubModel getProductModel(long productId);
    /**
     * 获取单个 sku 库存
     */
    PmsSkuStock getSkuStock(long productId,long skuId);
    /**
     * 获取该商品所有 sku 库存信息
     */
    List<PmsSkuStock> getSkuStockList(long productId);
    /**
     * 获取单个sku 库存和销量
     */
    ProductStats getSkuStockStats(long skuId);
    /**
     * 获取商品总库存 和 总销量
     */
    ProductStats getProductStats(long productId);
    /**
     * 增加 product的销量
     */
    void incrementProductSale(long productId,int delta) throws InterruptedException;
    /**
     * 增加/减少 product 总库存
     */
    void incrementProductStock(long product,int delta) throws InterruptedException;
    /**
     * 增加或者减少sku库存 自动修改总库存
     */
    void incrementSkuStock(long productId,long skuId,int delta) throws InterruptedException;
    /**
     * 增加或者减少该sku的销量
     */
    void incrementSkuSale(long productId, long skuId,int delta) throws InterruptedException;
    /**
     * 对排行榜 增加销售额
     */
    void increaseSales(long id,int sales);
    /**
     * 下架商品
     */
    void deleteProduct(long id);
    /**
     *  删除单个缓存 不删排行榜  修改了 非 sale stock 字段
     */
    void delProductCache(long id);
    /**
     * 删除 productMode 缓存 -> 修改了 productLadder价格阶梯  productFullReduction减满 productAlbum相册 productAttributeValueList商品属性值
     */
    void delProductModelCache(long productId);
    /**
     * 根据 productId 删除 该商品skuStock 全部缓存 当修改到 所有非 stock 字段时
     */
    void delSkuStock(long productId);
    /**
     *  删除 skuStock 的库存缓存
     */
    void delSkuStockCount(long skuId);
    /**
     * 删除对 product 的 sale stock统计数据的缓存 修改了对 sale stock两个字段
     */
    void delProductStats(long productId);


    class CacheKeys{
        public static String ProductKey(long id){return "product-key:"+id;}
        public static String ProductModelKey(long productId){return "product-model-key:"+productId;}
        public static String ProductKeyLock(long id){return "product-lock-key:"+id;}
        public static String ProductModelKeyLock(long productId){return "product-model-lock-key:"+productId;}
        public static String SkuStockHashKey(long productId){return "sku-stock-hash-key:"+productId;}
        public static String Field(long id){return ""+id;}
        public static String ProductStats(long productId){return "product-stats-hash:"+productId;}
        public static String SkuStockStats(long skuId) {return  "sku-stock-stats-hash:"+skuId;}

        public static String ProductNewRank = "product-new-rank";
        public static String ProductSaleRank = "product-sale-rank";
        public static String Sale = "sale";
        public static String Stock = "stock";
    }


    class ProductSubModel implements Serializable {
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

    class ProductStats implements Serializable{
        private Integer sale;
        private Integer stock;

        public ProductStats(){}
        public ProductStats(Integer sale,Integer stock){
            this.sale = sale;
            this.stock = stock;
        }

        public Integer getStock() {
            return stock;
        }

        public Integer getSale() {
            return sale;
        }

        public void setStock(Integer stock) {
            this.stock = stock;
        }

        public void setSale(Integer sale) {
            this.sale = sale;
        }
    }
}
