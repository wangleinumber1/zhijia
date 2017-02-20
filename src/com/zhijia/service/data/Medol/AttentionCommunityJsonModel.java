package com.zhijia.service.data.Medol;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 关注小区列表的JSON
 */
public class AttentionCommunityJsonModel {

    @JsonProperty(value = "address")
    private String address ;

    @JsonProperty(value = "communityname")
    private String communityName ;

    @JsonProperty(value = "id")
    private String id;//小区ID

    @JsonProperty(value = "place")
    private String place ; //小区名字

    @JsonProperty(value = "rentnum")
    private String rentNum ;//租4套

    @JsonProperty(value = "rentprice")
    private String rentPrice ;//出租价格

    @JsonProperty(value = "rentratio")
    private String rentRatio ;//出租涨幅指数

    @JsonProperty(value = "sellnum")
    private String sellNum ;

    @JsonProperty(value = "sellprice")
    private String sellPrice ;

    @JsonProperty(value = "sellratio")
    private String sellRatio ;

    @JsonProperty(value = "titlepic")
    private String titlePic ;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getRentNum() {
        return rentNum;
    }

    public void setRentNum(String rentNum) {
        this.rentNum = rentNum;
    }

    public String getRentPrice() {
        return rentPrice;
    }

    public void setRentPrice(String rentPrice) {
        this.rentPrice = rentPrice;
    }

    public String getRentRatio() {
        return rentRatio;
    }

    public void setRentRatio(String rentRatio) {
        this.rentRatio = rentRatio;
    }

    public String getSellNum() {
        return sellNum;
    }

    public void setSellNum(String sellNum) {
        this.sellNum = sellNum;
    }

    public String getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(String sellPrice) {
        this.sellPrice = sellPrice;
    }

    public String getSellRatio() {
        return sellRatio;
    }

    public void setSellRatio(String sellRatio) {
        this.sellRatio = sellRatio;
    }

    public String getTitlePic() {
        return titlePic;
    }

    public void setTitlePic(String titlePic) {
        this.titlePic = titlePic;
    }
}
