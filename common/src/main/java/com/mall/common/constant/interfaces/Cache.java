package com.mall.common.constant.interfaces;

public interface Cache {
    long defaultLockTime = 200;
    String defaultNULL ="NULL_";
    String Zero ="0";
    long defaultLockExpired = 1000;
    int retryCount = 5;
    String lockValue = "lock";
    long oneDayExpired = 86400000;
}
