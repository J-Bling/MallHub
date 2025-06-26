package com.mall.common.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ParamItem implements Serializable {
    private Integer index;
    private String paramType;
    private Object paramValue;

    public ParamItem(){}
    public ParamItem(Integer index,String paramType,Object paramValue){
        this.index = index;
        this.paramType = paramType;
        this.paramValue = paramValue;
    }

    public static List<ParamItem> getParams(Object ...args){
        List<ParamItem> paramItems = new ArrayList<>();
        for (Object o : args){
            ParamItem item = new ParamItem(0,o.getClass().getName(),o);
            paramItems.add(item);
        }
        return paramItems;
    }

    public Object getParamValue() {
        return paramValue;
    }

    public Integer getIndex() {
        return index;
    }

    public String getParamType() {
        return paramType;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public void setParamValue(Object paramValue) {
        this.paramValue = paramValue;
    }
}