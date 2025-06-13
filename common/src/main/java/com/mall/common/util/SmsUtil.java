package com.mall.common.util;

public interface SmsUtil {

    String generateCode();

    void send(String phone,String code);
}
