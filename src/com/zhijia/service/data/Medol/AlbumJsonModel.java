package com.zhijia.service.data.Medol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

/**
 * Created by xiaozhi on 14-5-23.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AlbumJsonModel {

    private String total ;

    private Map<String,Object> picture ;

    private String houseName ;

    public String getHouseName() {
        return houseName;
    }

    public void setHouseName(String houseName) {
        this.houseName = houseName;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public Map<String, Object> getPicture() {
        return picture;
    }

    public void setPicture(Map<String, Object> picture) {
        this.picture = picture;
    }
}
