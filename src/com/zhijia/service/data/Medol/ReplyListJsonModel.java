package com.zhijia.service.data.Medol;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 */
public class ReplyListJsonModel {

    @JsonProperty(value = "cid")
    private String cid ;

    @JsonProperty(value = "info")
    private String info ;

    @JsonProperty(value = "posttime")
    private String postTime ;

    @JsonProperty(value = "username")
    private String userName ;

    @JsonProperty(value = "avatar")
    private String avatar ;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
