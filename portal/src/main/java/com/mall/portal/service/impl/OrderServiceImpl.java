package com.mall.portal.service.impl;

import com.mall.mbg.mapper.*;
import com.mall.portal.dao.OrdersDao;
import com.mall.portal.domain.model.orders.ConfirmOrders;
import com.mall.portal.domain.model.orders.OrderDetail;
import com.mall.portal.domain.model.orders.OrderReturnApplyParam;
import com.mall.portal.domain.model.orders.OrdersParma;
import com.mall.portal.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class OrderServiceImpl implements OrdersService {
    @Autowired private OmsOrderMapper omsOrderMapper;
    @Autowired private OrdersDao ordersDao;
    @Autowired private CartItemService cartItemService;
    @Autowired private ConsumerService consumerService;
    @Autowired private AddressService addressService;
    @Autowired private CouponService couponService;
    @Autowired private UmsIntegrationConsumeSettingMapper consumeSettingMapper;
    @Autowired private OmsOrderItemMapper omsOrderItemMapper;
    @Autowired private OmsOrderReturnApplyMapper returnApplyMapper;
    @Autowired private OmsOrderReturnReasonMapper returnReasonMapper;


    @Override
    public ConfirmOrders generateConfirmOrders(List<Long> cartIds) {
        return null;
    }

    @Override
    public TotalOrders generateTotalOrders(OrdersParma parma) {
        return null;
    }

    @Override
    public void cancelTimeoutOrder() {

    }

    @Override
    public int paySuccess(long orderId, int payType) {
        return 0;
    }

    @Override
    public void cancelOrder(long orderId) {

    }

    @Override
    public void sendDelayMessageCancelOrder(long orderId) {

    }

    @Override
    public void confirmReceiveOrder(long orderId) {

    }

    @Override
    public List<OrderDetail> list(int status, int pageNum, int pageSize) {
        return Collections.emptyList();
    }

    @Override
    public OrderDetail detail(long orderId) {
        return null;
    }

    @Override
    public void deleteOrder(long orderId) {

    }

    @Override
    public void paySuccessByOrderSn(String orderSn, int payType) {

    }

    @Override
    public void orderReturnApply(OrderReturnApplyParam applyParam) {

    }
}
