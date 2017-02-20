package com.zhijia.service.data.Medol;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 用户实名认证信息对象
 */
public class UserCertificationInfo {

    //姓名
    @JsonProperty(value = "truename")
    private String trueName;

    //身份证图片
    @JsonProperty(value = "identityfile")
    private String identityFile;

    //身份证号
    @JsonProperty(value = "identity_num")
    private String identityNum;

    //认证状态 0:您的信息未认证,    1:您的信息已审核通过！,2:您的信息正在审核中！,3:您的信息未审核通过'
    @JsonProperty(value = "status")
    private String status;

    //审核信息
    @JsonProperty(value = "identity_status")
    private String identityStatus;

    //开户行
    @JsonProperty(value = "bankname")
    private String bankName;

    //银行卡号
    @JsonProperty(value = "bankcard")
    private String bankCard;

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getIdentityFile() {
        return identityFile;
    }

    public void setIdentityFile(String identityFile) {
        this.identityFile = identityFile;
    }

    public String getIdentityNum() {
        return identityNum;
    }

    public void setIdentityNum(String identityNum) {
        this.identityNum = identityNum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIdentityStatus() {
        return identityStatus;
    }

    public void setIdentityStatus(String identityStatus) {
        this.identityStatus = identityStatus;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }
}
