package com.zhijia.service.data.Medol;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * 问答
 */
public class WendaJsonModel {

    private String total ;

    private List<ListJsonModel> list ;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<ListJsonModel> getList() {
        return list;
    }

    public void setList(List<ListJsonModel> list) {
        this.list = list;
    }

    public static  class ListJsonModel{

        private String qid ;

        private String content ;

        private String bestAnswer ;

        private String updatetime ;

        private String answer ;

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

        @JsonProperty(value = "updatetime")
        public String getUpdatetime() {
            return updatetime;
        }

        public void setUpdatetime(String updatetime) {
            this.updatetime = updatetime;
        }

        @JsonProperty(value = "answer")
        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }
    }
}
