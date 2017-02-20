package com.zhijia.service.data.Medol;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 */
public class MyBrowseCommunityJsonModel {

    @JsonProperty(value ="communityid" )
    private String communityId ;

    @JsonProperty(value ="communityname" )
    private String communityName ;

    @JsonProperty(value ="address" )
    private String address ;

    @JsonProperty(value = "sellratio")
    private String sellRatio ;

    @JsonProperty(value ="titlepic" )
    private String titlePic ;

    @JsonProperty(value ="areaname" )
    private String areaName ;

    @JsonProperty(value = "price")
    private String price ;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
