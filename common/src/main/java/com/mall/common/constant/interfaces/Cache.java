package com.mall.common.constant.interfaces;

public interface Cache {
    long defaultLockTime = 200;
    String defaultNULL ="NULL_";
    long defaultLockExpired = 1000;
    int retryCount = 5;
    String lockValue = "lock";
}
