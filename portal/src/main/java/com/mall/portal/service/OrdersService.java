package com.mall.portal.service;

import com.alipay.api.domain.OrderDetail;
import com.mall.mbg.model.OmsOrder;
import com.mall.mbg.model.OmsOrderItem;
import com.mall.portal.domain.model.ConfirmOrders;
import com.mall.portal.domain.model.OrderReturnApplyParam;
import com.mall.portal.domain.model.OrdersParma;
import java.util.List;

public interface OrdersService {
    /**
     * 根据购物车信息生产订单确认信息
     */
    ConfirmOrders generateConfirmOrders(List<Long> cartIds);
    /**
     * 根据提交信息生产订单
     */
    TotalOrders generateTotalOrders(OrdersParma parma);
    /**
     * 自动取消超市订单
     */
    void cancelTimeoutOrder();
    /**
     * 支付成功回调
     */
    int paySuccess(long orderId,int payType);
    /**
     * 取消单个订单
     */
    void cancelOrder(long orderId);
    /**
     * 发送延迟消息取消订单
     */
    void sendDelayMessageCancelOrder(long orderId);
    /**
     * 确认收货
     */
    void confirmReceiveOrder(long orderId);
    /**
     * 分页获取用户订单
     */
    List<OrderDetail> list(int status, int pageNum, int pageSize);

    /**
     * 根据订单ID获取订单详情
     */
    OrderDetail detail(long orderId);
    /**
     * 用户根据订单ID删除订单
     */
    void deleteOrder(long orderId);
    /**
     * 根据orderSn来实现的支付成功逻辑
     */
    void paySuccessByOrderSn(String orderSn, int payType);
    /**
     * 订单退货
     */
    void orderReturnApply(OrderReturnApplyParam applyParam);

    class TotalOrders{
        private OmsOrder order;
        private List<OmsOrderItem> orderItemList;

        public OmsOrder getOrder() {
            return order;
        }

        public List<OmsOrderItem> getOrderItemList() {
            return orderItemList;
        }

        public void setOrder(OmsOrder order) {
            this.order = order;
        }

        public void setOrderItemList(List<OmsOrderItem> orderItemList) {
            this.orderItemList = orderItemList;
        }
    }
}
