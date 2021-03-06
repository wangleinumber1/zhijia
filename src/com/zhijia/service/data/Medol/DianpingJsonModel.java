package com.zhijia.service.data.Medol;

import java.util.List;

/**
 * 点评
 */
public class DianpingJsonModel {

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

    public static class ListJsonModel{

        private String cid ;

        private String title ;

        private String grade ;

        private String replynum ;

        private String digg ;

        private String down ;

        private String posttime ;

        private String username ;

        private String avatar ;

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getReplynum() {
            return replynum;
        }

        public void setReplynum(String replynum) {
            this.replynum = replynum;
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

        public String getPosttime() {
            return posttime;
        }

        public void setPosttime(String posttime) {
            this.posttime = posttime;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }
}
