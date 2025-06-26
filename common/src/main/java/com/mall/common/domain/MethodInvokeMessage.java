package com.mall.common.domain;


import java.io.Serializable;
import java.util.List;

public class MethodInvokeMessage implements Serializable{
    private String beanName;
    private String methodName;
    private List<ParamItem> paramItemList;

    public MethodInvokeMessage(){}
    public MethodInvokeMessage(String beanName,String methodName,List<ParamItem> paramItemList){
        this.beanName = beanName;
        this.methodName = methodName;
        this.paramItemList = paramItemList;
    }

    public String getBeanName() {
        return beanName;
    }

    public String getMethodName() {
        return methodName;
    }

    public List<ParamItem> getParamItemList() {
        return paramItemList;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setParamItemList(List<ParamItem> paramItemList) {
        this.paramItemList = paramItemList;
    }

}