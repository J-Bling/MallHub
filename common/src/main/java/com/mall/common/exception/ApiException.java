package com.mall.common.exception;

import com.mall.common.api.ResultCode;

public class ApiException extends RuntimeException {
    private String message;
    private ResultCode resultCode;

    public ApiException(String message,ResultCode resultCode){
        super(message);
        this.message=message;
        this.resultCode=resultCode;
    }

    public ApiException(ResultCode resultCode){
        super(resultCode.getMessage());
        this.message=resultCode.getMessage();
        this.resultCode=resultCode;
    }

    public ApiException(String message) {
        super(message);
        this.message=message;
        this.resultCode = ResultCode.BAD_REQUEST;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode(){
        if(resultCode==null) return 0;
        return resultCode.getCode();
    }

    public ResultCode getResultCode(){
        return resultCode;
    }
}
