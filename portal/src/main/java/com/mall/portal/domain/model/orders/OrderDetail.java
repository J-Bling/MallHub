package com.mall.portal.domain.model.orders;

import com.mall.mbg.model.OmsOrder;
import com.mall.mbg.model.OmsOrderItem;

import java.util.List;

public class OrderDetail extends OmsOrder {
    List<OmsOrderItem> omsOrderItemList;

    public List<OmsOrderItem> getOmsOrderItemList() {
        return omsOrderItemList;
    }

    public void setOmsOrderItemList(List<OmsOrderItem> omsOrderItemList) {
        this.omsOrderItemList = omsOrderItemList;
    }
}
