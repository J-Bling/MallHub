package com.mall.portal.component;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "alipay")
public class AlipayProperties {
    /**
     * 支付宝网关
     */
    private String gatewayUrl;
    /**
     * 应用ID
     */
    private String appId;
    /**
     * 应用私钥
     */
    private String appPrivateKey;
    /**
     * 支付宝公钥
     */
    private String alipayPublicKey;
    /**
     * 用户确认支付后，支付宝调用的页面返回路径
     * 开发环境为：http://localhost:8060/#/pages/money/paySuccess
     */
    private String returnUrl;
    /**
     * 支付成功后，支付宝服务器主动通知商户服务器里的异步通知回调（需要公网能访问）
     * 开发环境为：http://localhost:8085/alipay/notify
     */
    private String notifyUrl;
    /**
     * 参数返回格式，只支持JSON
     */
    private String format = "JSON";
    /**
     * 请求使用的编码格式
     */
    private String charset = "UTF-8";
    /**
     * 生成签名字符串所使用的签名算法类型
     */
    private String signType = "RSA2";

    public String getAlipayPublicKey() {
        return alipayPublicKey;
    }

    public String getAppId() {
        return appId;
    }

    public String getAppPrivateKey() {
        return appPrivateKey;
    }

    public String getCharset() {
        return charset;
    }

    public String getFormat() {
        return format;
    }

    public String getGatewayUrl() {
        return gatewayUrl;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public String getSignType() {
        return signType;
    }

    public void setAlipayPublicKey(String alipayPublicKey) {
        this.alipayPublicKey = alipayPublicKey;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public void setAppPrivateKey(String appPrivateKey) {
        this.appPrivateKey = appPrivateKey;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setGatewayUrl(String gatewayUrl) {
        this.gatewayUrl = gatewayUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }
}
