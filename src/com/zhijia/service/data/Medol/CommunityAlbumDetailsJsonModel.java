package com.zhijia.service.data.Medol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommunityAlbumDetailsJsonModel {

    @JsonProperty(value = "pic")
    private String pic ;

    @JsonProperty(value = "pid")
    private String pid ;

    @JsonProperty(value = "info")
    private String info ;

    @JsonProperty(value = "pic_url")
    private String picUrl ;

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
