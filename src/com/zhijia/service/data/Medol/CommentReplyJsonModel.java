package com.zhijia.service.data.Medol;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 *
 */
public class CommentReplyJsonModel {

    @JsonProperty(value = "cid")
    private String cid ;

    @JsonProperty(value = "hid")
    private String hid ;

    @JsonProperty(value = "grade")
    private String grade ;

    @JsonProperty(value = "info")
    private String info ;

    @JsonProperty(value = "replynum")
    private String replyNum ;

    @JsonProperty(value = "digg")
    private String  digg ;

    @JsonProperty(value = "down")
    private String down ;

    @JsonProperty(value = "posttime")
    private String postTime ;

    @JsonProperty(value = "username")
    private String userName ;

    @JsonProperty(value = "avatar")
    private String avatar ;

    @JsonProperty(value = "reply")
    private List<ReplyListJsonModel> reply ;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getHid() {
        return hid;
    }

    public void setHid(String hid) {
        this.hid = hid;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(String replyNum) {
        this.replyNum = replyNum;
    }

    public String getDigg() {
        return digg;
    }

    public void setDigg(String digg) {
        this.digg = digg;
    }

    public String getDown() {
        return down;
    }

    public void setDown(String down) {
        this.down = down;
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

    public List<ReplyListJsonModel> getReply() {
        return reply;
    }

    public void setReply(List<ReplyListJsonModel> reply) {
        this.reply = reply;
    }
}
