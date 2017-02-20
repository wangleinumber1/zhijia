package com.zhijia.service.data.Medol;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 */
public class MyBrowseOldHouseJsonModel {

    @JsonProperty(value = "hid")
    private String hid ;

    @JsonProperty(value = "communityname")
    private String communityName ;

    @JsonProperty(value = "address")
    private String address ;

    @JsonProperty(value = "price")
    private String price ;

    @JsonProperty(value = "titlepic")
    private String titlePic ;

    @JsonProperty(value = "areaname")
    private String areaName;

    @JsonProperty(value = "renttype")
    private String rentType ;

    public String getHid() {
        return hid;
    }

    public void setHid(String hid) {
        this.hid = hid;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTitlePic() {
        return titlePic;
    }

    public void setTitlePic(String titlePic) {
        this.titlePic = titlePic;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getRentType() {
        return rentType;
    }

    public void setRentType(String rentType) {
        this.rentType = rentType;
    }
}
