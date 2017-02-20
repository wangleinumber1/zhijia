package com.zhijia.service.data.Medol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 针对具有data的数据进行封装
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonResult<T> extends BaseJsonModel{

    private T data ;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
