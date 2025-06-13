package com.mall.common.api;

public class ResponseResult<T> {
    private int code;
    private String message;
    private T data;

    protected ResponseResult(){}
    protected ResponseResult(int code,String message,T data){
        this.code=code;
        this.message=message;
        this.data=data;
    }

    public static <T> ResponseResult<T> success(T data){
        return new ResponseResult<>(ResultCode.SUCCESS.getCode(),ResultCode.SUCCESS.getMessage(),data);
    }

    public static <T> ResponseResult<T> success(String message , T data){
        return new ResponseResult<>(ResultCode.SUCCESS.getCode(), message,data);
    }

    public static <T> ResponseResult<T> faild(ResultCode code){
        return new ResponseResult<>(code.getCode(),code.getMessage(),null);
    }

    public static <T> ResponseResult<T> faild(ResultCode code ,String message){
        return new ResponseResult<>(code.getCode(),message,null);
    }

    public static <T> ResponseResult<T> error(){
        return faild(ResultCode.INTERNAL_SERVER_ERROR);
    }

    public static <T> ResponseResult<T> badFaild(){
        return faild(ResultCode.BAD_REQUEST);
    }

    public static <T> ResponseResult<T> badfaild(String message){
        return faild(ResultCode.BAD_REQUEST,message);
    }

    public static <T> ResponseResult<T> unauthorized(String message){
        return faild(ResultCode.UNAUTHORIZED,message);
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public T getData() {
        return data;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(T data) {
        this.data = data;
    }
}
