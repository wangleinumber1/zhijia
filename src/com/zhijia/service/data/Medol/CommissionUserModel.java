package com.zhijia.service.data.Medol;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 浣ｉ噾鐢ㄦ埛淇℃伅
 */
public class CommissionUserModel {

    //鐢ㄦ埛ID
    private String uid;

   private String username;

    //鎵嬫満
    private String mobile;

    //鐢ㄦ埛澶村儚
    private String avatar;

    //鎵嬫満楠岃瘉鐘舵� 0锛氭湭楠岃瘉
    @JsonProperty(value = "mobile_status")
    private String mobileStatus;

    //韬唤
    @JsonProperty(value = "identity_name")
    private String identityName;

    //閭楠岃瘉鐘舵� 0锛氭湭楠岃瘉
    @JsonProperty(value = "email_status")
    private String emailStatus;

    //瀹炲悕璁よ瘉鐘舵� 0锛氭湭楠岃瘉,1:閫氳繃璁よ瘉,2:瀹℃牳涓�3:鏈�杩�  
    private String identity;

    //浣ｉ噾
    private String amount;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getMobileStatus() {
        return mobileStatus;
    }

    public void setMobileStatus(String mobileStatus) {
        this.mobileStatus = mobileStatus;
    }

    public String getIdentityName() {
        return identityName;
    }

    public void setIdentityName(String identityName) {
        this.identityName = identityName;
    }

    public String getEmailStatus() {
        return emailStatus;
    }

    public void setEmailStatus(String emailStatus) {
        this.emailStatus = emailStatus;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}