package com.zhijia.service.data.Medol;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 */
public class ListJsonModel {

    @JsonProperty(value = "pid")
    private String pid ;

    @JsonProperty(value = "pic_url")
    private String picUrl ;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
