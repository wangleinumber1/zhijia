package com.zhijia.service.data.Medol;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 鎺ㄨ崘瀹㈡埛璇︽儏妯″瀷
 */
public class CustomerProgressDetailModel {

    //鏈�柊鏍囩鐘舵�
    @JsonProperty(value = "tag_status")
    private String tagStatus;

    //褰撳墠杩涘害 ---1:鑱旂郴,2:棰勭害,3:鍒拌,4:浜ゅ畾閲�5:鎴愪氦,6:鍙戞斁浣ｉ噾
    @JsonProperty(value = "tag_now")
    private int tagNow;

    //鏈�柊鏍囩鍚�  
    private String tag;

    private Info info;

    private Status status;

    public String getTagStatus() {
        return tagStatus;
    }

    public void setTagStatus(String tagStatus) {
        this.tagStatus = tagStatus;
    }

    public int getTagNow() {
        return tagNow;
    }

    public void setTagNow(int tagNow) {
        this.tagNow = tagNow;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public static class Status {

        private String status1;

        private String updatetime1;

        @JsonProperty(value = "status1_tag")
        private String status1Tag;

        private String status2;

        private String updatetime2;

        @JsonProperty(value = "status2_tag")
        private String status2Tag;

        private String status3;

        private String updatetime3;

        @JsonProperty(value = "status3_tag")
        private String status3Tag;

        private String status4;

        private String updatetime4;

        @JsonProperty(value = "status4_tag")
        private String status4Tag;

        private String status5;

        private String updatetime5;

        @JsonProperty(value = "status5_tag")
        private String status5Tag;

        private String status6;

        private String updatetime6;

        @JsonProperty(value = "status6_tag")
        private String status6Tag;

        public String getStatus1() {
            return status1;
        }

        public void setStatus1(String status1) {
            this.status1 = status1;
        }

        public String getUpdatetime1() {
            return updatetime1;
        }

        public void setUpdatetime1(String updatetime1) {
            this.updatetime1 = updatetime1;
        }

        public String getStatus1Tag() {
            return status1Tag;
        }

        public void setStatus1Tag(String status1Tag) {
            this.status1Tag = status1Tag;
        }

        public String getStatus2() {
            return status2;
        }

        public void setStatus2(String status2) {
            this.status2 = status2;
        }

        public String getUpdatetime2() {
            return updatetime2;
        }

        public void setUpdatetime2(String updatetime2) {
            this.updatetime2 = updatetime2;
        }

        public String getStatus2Tag() {
            return status2Tag;
        }

        public void setStatus2Tag(String status2Tag) {
            this.status2Tag = status2Tag;
        }

        public String getStatus3() {
            return status3;
        }

        public void setStatus3(String status3) {
            this.status3 = status3;
        }

        public String getUpdatetime3() {
            return updatetime3;
        }

        public void setUpdatetime3(String updatetime3) {
            this.updatetime3 = updatetime3;
        }

        public String getStatus3Tag() {
            return status3Tag;
        }

        public void setStatus3Tag(String status3Tag) {
            this.status3Tag = status3Tag;
        }

        public String getStatus4() {
            return status4;
        }

        public void setStatus4(String status4) {
            this.status4 = status4;
        }

        public String getUpdatetime4() {
            return updatetime4;
        }

        public void setUpdatetime4(String updatetime4) {
            this.updatetime4 = updatetime4;
        }

        public String getStatus4Tag() {
            return status4Tag;
        }

        public void setStatus4Tag(String status4Tag) {
            this.status4Tag = status4Tag;
        }

        public String getStatus5() {
            return status5;
        }

        public void setStatus5(String status5) {
            this.status5 = status5;
        }

        public String getUpdatetime5() {
            return updatetime5;
        }

        public void setUpdatetime5(String updatetime5) {
            this.updatetime5 = updatetime5;
        }

        public String getStatus5Tag() {
            return status5Tag;
        }

        public void setStatus5Tag(String status5Tag) {
            this.status5Tag = status5Tag;
        }

        public String getStatus6() {
            return status6;
        }

        public void setStatus6(String status6) {
            this.status6 = status6;
        }

        public String getUpdatetime6() {
            return updatetime6;
        }

        public void setUpdatetime6(String updatetime6) {
            this.updatetime6 = updatetime6;
        }

        public String getStatus6Tag() {
            return status6Tag;
        }

        public void setStatus6Tag(String status6Tag) {
            this.status6Tag = status6Tag;
        }
    }

    public static class Info {
        //瀹㈡埛
        private String truename;

        //鎵嬫満
        private String mobile;

        //浣ｉ噾
        private String amount;

        //鎴愪氦妤肩洏ID
        private String hid;

        //鎺ㄨ崘鏃堕棿
        private String posttime;

        //鎴愪氦妤肩洏鍚嶇О
        private String name;

        public String getTruename() {
            return truename;
        }

        public void setTruename(String truename) {
            this.truename = truename;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getHid() {
            return hid;
        }

        public void setHid(String hid) {
            this.hid = hid;
        }

        public String getPosttime() {
            return posttime;
        }

        public void setPosttime(String posttime) {
            this.posttime = posttime;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
