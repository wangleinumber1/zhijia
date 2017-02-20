package com.zhijia.service.data.Medol;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 我浏览的新房json数据
 */
public class MyBrowseNewHouseJsonModel {

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

    @JsonProperty(value = "areaname")
    private String areaName ;

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

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
}
