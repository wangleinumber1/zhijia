package com.zhijia.service.data.Medol;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 地图列表
 */
public class NewMapListJsonModel {

    @JsonProperty(value = "hid")
    private String hid ;

    @JsonProperty(value = "name")
    private String name ;

    @JsonProperty(value = "address")
    private String address ;

    @JsonProperty(value = "averageprice")
    private String averagePrice ;

    @JsonProperty(value = "titlepic")
    private String titlePic ;

    @JsonProperty(value = "salesstatus")
    private String salesStatus ;

    @JsonProperty(value = "priceexplain")
    private String priceExplain ;

    @JsonProperty(value = "areaname")
    private String areaName ;

    @JsonProperty(value = "laticoor")
    private String laticoor ;

    @JsonProperty(value = "longcoor")
    private String longcoor ;

    public String getHid() {
        return hid;
    }

    public void setHid(String hid) {
        this.hid = hid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(String averagePrice) {
        this.averagePrice = averagePrice;
    }

    public String getTitlePic() {
        return titlePic;
    }

    public void setTitlePic(String titlePic) {
        this.titlePic = titlePic;
    }

    public String getSalesStatus() {
        return salesStatus;
    }

    public void setSalesStatus(String salesStatus) {
        this.salesStatus = salesStatus;
    }

    public String getPriceExplain() {
        return priceExplain;
    }

    public void setPriceExplain(String priceExplain) {
        this.priceExplain = priceExplain;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
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
