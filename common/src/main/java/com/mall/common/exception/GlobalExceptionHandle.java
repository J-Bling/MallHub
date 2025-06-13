package com.mall.common.exception;

import com.mall.common.api.ResponseResult;
import com.mall.common.api.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;

@ControllerAdvice
public class GlobalExceptionHandle {

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandle.class);

    @ResponseBody
    @ExceptionHandler(value = ApiException.class)
    public ResponseEntity<ResponseResult> handleAipException(ApiException e){
        ResultCode resultCode = e.getResultCode();
        if(resultCode!=null){
            try {
                ResponseResult result = ResponseResult.faild(resultCode, e.getMessage());
                return ResponseEntity.status(HttpStatus.valueOf(resultCode.getCode())).body(result);
            }catch (Exception exception){
                logger.error("fail : {}",e.getMessage(),e);
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseResult.error());
    }


    /**
     * 当MVC controller的  @RequestBody请求体 的参数校验失败抛出异常
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseResult> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        BindingResult bindingResult= e.getBindingResult();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseResult.badfaild(generateMessage(bindingResult)));
    }


    /**
     * 当url绑定失败或者 @ModelAttribute表单提交校验失败抛出的异常
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = BindException.class)
    public ResponseEntity<ResponseResult> handleBindException(BindException e){
        BindingResult bindingResult= e.getBindingResult();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseResult.badfaild(generateMessage(bindingResult)));
    }

    private String generateMessage(BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            FieldError fieldError= bindingResult.getFieldError();
            if(fieldError!=null){
                return fieldError.getField() + " : "+fieldError.getDefaultMessage();
            }
        }
        return "";
    }


    @ResponseBody
    @ExceptionHandler(value = SQLException.class)
    public ResponseEntity<ResponseResult> handleSQLException(SQLException e){
        logger.error("sql失败 : {} ",e.getMessage(),e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseResult.error());
    }
}
