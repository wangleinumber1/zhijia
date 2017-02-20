package com.zhijia.service.data.Medol;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 */
public class WendaListJsonModel {

    private String qid ;

    private String content ;

    private String bestAnswer ;

    private String posttime ;

    private String reply ;

    @JsonProperty(value = "qid")
    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    @JsonProperty(value = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @JsonProperty(value = "best_answer")
    public String getBestAnswer() {
        return bestAnswer;
    }

    public void setBestAnswer(String bestAnswer) {
        this.bestAnswer = bestAnswer;
    }

    @JsonProperty(value = "posttime")
    public String getPosttime() {
        return posttime;
    }

    public void setPosttime(String posttime) {
        this.posttime = posttime;
    }

    @JsonProperty(value = "reply")
    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }
}
