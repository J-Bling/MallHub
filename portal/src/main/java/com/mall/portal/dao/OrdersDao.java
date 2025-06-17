package com.mall.portal.dao;

import org.apache.ibatis.annotations.Update;

import java.util.Date;

public interface OrdersDao {

    @Update("update oms_cart_item set quantity = quantity + #{delta} ,modify_date = #{modify} where id = #{cartId}")
    void incrementCartQuantityAndModify(long cartId, int delta, Date modify);
}
