package com.mall.common.api;

public enum ResultCode {
    SUCCESS(200,"成功"),
    BAD_REQUEST(400,"错误请求"),
    UNAUTHORIZED(401,"未授权"),
    FORBIDDEN(403,"权限不足"),
    NOT_FOUND(404,"未找到"),
    CONFLICT(409,"发生冲突"),
    TOO_MANY_REQUEST(429,"频繁操作"),
    INTERNAL_SERVER_ERROR(500,"服务器错误"),
    SERVICE_UNAVAILABLE(503,"服务不可用"),
    GATEWAY_TIMEOUT(504,"网关超时");

    private Integer code;
    private String message;

    ResultCode(Integer code,String message){
        this.code=code;
        this.message=message;
    }

    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}
