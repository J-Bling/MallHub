package com.mall.portal.service;

import com.mall.portal.domain.model.AlipayParam;
import java.util.Map;

public interface AlipayService {
    /**
     * 提交电脑支付
     */
    String pay(AlipayParam alipayParam);
    /**
     * 支付宝回调
     */
    String notify(Map<String,String> params);
    /**
     * 查询支付宝交易状态
     * @param outTradeNo 商户订单编号
     * @param tradeNo 支付宝交易编号
     * @return 支付宝交易状态
     */
    String query(String outTradeNo,String tradeNo);
    /**
     * 提交移动支付
     */
    String mobilePay(AlipayParam alipayParam);
}
