package com.mall.portal.controller;

import com.mall.common.api.ResponseResult;
import com.mall.portal.component.AlipayProperties;
import com.mall.portal.domain.model.AlipayParam;
import com.mall.portal.service.AlipayService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/portal/api/alipay")
public class AlipayController {
    @Autowired private AlipayProperties alipayProperties;
    @Autowired private AlipayService alipayService;

    @ApiOperation("支付宝电脑网站支付")
    @RequestMapping(value = "/pay", method = RequestMethod.GET)
    public void pay(AlipayParam aliPayParam, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=" + alipayProperties.getCharset());
        response.getWriter().write(alipayService.pay(aliPayParam));
        response.getWriter().flush();
        response.getWriter().close();
    }

    @ApiOperation("支付宝手机网站支付")
    @RequestMapping(value = "/webPay", method = RequestMethod.GET)
    public void webPay(AlipayParam aliPayParam, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=" + alipayProperties.getCharset());
        response.getWriter().write(alipayService.mobilePay(aliPayParam));
        response.getWriter().flush();
        response.getWriter().close();
    }

    @ApiOperation(value = "支付宝异步回调",notes = "必须为POST请求，执行成功返回success，执行失败返回failure")
    @RequestMapping(value = "/notify", method = RequestMethod.POST)
    public String notify(HttpServletRequest request){
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            params.put(name, request.getParameter(name));
        }
        return alipayService.notify(params);
    }

    /**
     * outTradeNode 商户订单号    tradeNode 支付宝交易号
     */
    @ApiOperation(value = "支付宝统一收单线下交易查询",notes = "订单支付成功返回交易状态：TRADE_SUCCESS")
    @RequestMapping(value = "/query", method = RequestMethod.GET)
    @ResponseBody
    public ResponseResult<String> query(String outTradeNo, String tradeNo){
        return ResponseResult.success(alipayService.query(outTradeNo,tradeNo));
    }
}
