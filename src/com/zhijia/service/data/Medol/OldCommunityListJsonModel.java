package com.zhijia.service.data.Medol;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 *
 */
public class OldCommunityListJsonModel {

    @JsonProperty(value = "area")
    private String area ;
    @JsonProperty(value = "areaname")
    private String areaName ;
    @JsonProperty(value = "communityname")
    private String communityName ;
    @JsonProperty(value = "hid")
    private String hid ;
    @JsonProperty(value = "price")
    private String price ;
    @JsonProperty(value = "tag")
    private String tag ;
    @JsonProperty(value = "title")
    private String title;
    @JsonProperty(value = "titlepic")
    private String titlePic ;
    @JsonProperty(value = "unit")
    private String unit ;
    @JsonProperty(value = "usertype")
    private String userType ;
    @JsonProperty(value = "tagtext")
    private List<String> tagText ;


    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getHid() {
        return hid;
    }

    public void setHid(String hid) {
        this.hid = hid;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitlePic() {
        return titlePic;
    }

    public void setTitlePic(String titlePic) {
        this.titlePic = titlePic;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public List<String> getTagText() {
        return tagText;
    }

    public void setTagText(List<String> tagText) {
        this.tagText = tagText;
    }
}
