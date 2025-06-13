package com.mall.common.exception;

import com.mall.common.api.ResultCode;

public class Assert {
    public static void fail(String message)throws ApiException{
        throw new ApiException(message);
    }

    public static void fail(String message, ResultCode resultCode)throws ApiException{
        throw new ApiException(message,resultCode);
    }

    public static void fail(ResultCode resultCode)throws ApiException{
        throw new ApiException(resultCode);
    }
}
