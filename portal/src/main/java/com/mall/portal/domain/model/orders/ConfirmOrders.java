package com.mall.portal.domain.model.orders;

import com.mall.mbg.model.UmsIntegrationConsumeSetting;
import com.mall.mbg.model.UmsMemberReceiveAddress;
import com.mall.portal.domain.model.product.CartPromotionItem;
import com.mall.portal.domain.model.flash.CouponHistoryDetail;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ConfirmOrders implements Serializable {
    //"包含优惠信息的购物车信息"
    private List<CartPromotionItem> cartPromotionItemList;
    //"用户收货地址列表"
    private List<UmsMemberReceiveAddress> memberReceiveAddressList;
    //"用户可用优惠券列表"
    private List<CouponHistoryDetail> couponHistoryDetailList;
    //"积分使用规则"
    private UmsIntegrationConsumeSetting integrationConsumeSetting;
    //"会员持有的积分"
    private Integer memberIntegration;
    //"计算的金额"
    private CalcAmount calcAmount;

    @Data
    public static class CalcAmount{
        //"订单商品总金额"
        private BigDecimal totalAmount;
        //"运费"
        private BigDecimal freightAmount;
        //"活动优惠"
        private BigDecimal promotionAmount;
        //"应付金额"
        private BigDecimal payAmount;
    }
}
