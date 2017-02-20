package com.zhijia.service.data.Medol;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 */
public class CommunityMapListJsonModel {

    @JsonProperty(value = "communityid")
    private String communityId;

    @JsonProperty(value = "communityname")
    private String communityName;

    @JsonProperty(value = "titlepic")
    private String titlePic;

    @JsonProperty(value = "sellprice")
    private String sellPrice;

    @JsonProperty(value = "sellnum")
    private String sellNum;

    @JsonProperty(value = "rentprice")
    private String rentPrice;

    @JsonProperty(value = "rentnum")
    private String rentNum;

    @JsonProperty(value = "laticoor")
    private String laticoor;

    @JsonProperty(value = "longcoor")
    private String longcoor;

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getTitlePic() {
        return titlePic;
    }

    public void setTitlePic(String titlePic) {
        this.titlePic = titlePic;
    }

    public String getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(String sellPrice) {
        this.sellPrice = sellPrice;
    }

    public String getSellNum() {
        return sellNum;
    }

    public void setSellNum(String sellNum) {
        this.sellNum = sellNum;
    }

    public String getRentPrice() {
        return rentPrice;
    }

    public void setRentPrice(String rentPrice) {
        this.rentPrice = rentPrice;
    }

    public String getRentNum() {
        return rentNum;
    }

    public void setRentNum(String rentNum) {
        this.rentNum = rentNum;
    }

    public String getLaticoor() {
        return laticoor;
    }

    public void setLaticoor(String laticoor) {
        this.laticoor = laticoor;
    }

    public String getLongcoor() {
        return longcoor;
    }

    public void setLongcoor(String longcoor) {
        this.longcoor = longcoor;
    }
}
