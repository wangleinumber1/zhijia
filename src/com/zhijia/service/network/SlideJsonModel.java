package com.zhijia.service.network;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 幻灯片的json数据。
 */
public class SlideJsonModel {

    private String picUrl ;

    private String type ;

    private String url ;

    @JsonProperty(value="pic_url")
    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
