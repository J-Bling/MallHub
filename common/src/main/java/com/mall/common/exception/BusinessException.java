package com.mall.common.exception;


import com.mall.common.constant.enums.BusinessErrorCode;

public class BusinessException  extends RuntimeException{
    private final BusinessErrorCode errorCode;
    private final String message;

    public BusinessException(BusinessErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = errorCode.getDescription();
    }

    public BusinessException(BusinessErrorCode errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public BusinessErrorCode getErrorCode() {
        return errorCode;
    }
}
