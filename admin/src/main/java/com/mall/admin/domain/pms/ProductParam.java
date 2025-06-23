package com.mall.admin.domain.pms;

import com.mall.mbg.model.*;
import lombok.Data;

import java.util.List;

@Data
public class ProductParam{
    private PmsProduct product;//商品
    private PmsProductCategory productCategory;//商品分类
    private PmsBrand brand;//品牌信息
    private List<PmsMemberPrice> memberPriceList;//会员价格设置
    private List<PmsProductLadder> productLadderList;//商品价格阶梯
    private List<PmsProductFullReduction> productFullReductionList;//商品减满
    private List<PmsSkuStock> skuStockList;//库存信息
    private List<ProductAttributeDetail> productAttributeDetailList;//商品属性详细
    private List<CmsSubjectProductRelation> subjectProductRelationList;//关联专题
    private List<CmsPrefrenceAreaProductRelation> prefrenceAreaProductRelationList;//优选区关联商品
}
