package com.zhijia.service.data.Medol;

import android.graphics.drawable.Drawable;

/**
 * 新房列表中的限期特惠、热点楼盘、近期开盘列表条目的数据模型
 */
public class HouseListItemModel {

    private Drawable houseImage;

    private String imageURL;

    private String houseContentOne;

    private String houseContentTwo;

    private String houseContentThree;

    private String houseContentFour;

    private String houseContentFive;

    private String hid;

    private String errorDefault;
    /***楼盘动态的属性**/
    private String houseDynamicURL ;

    private String houseDynamicPublished ;

    private String houseDynamicTitle ;

    public HouseListItemModel() {
    }

    ;

    public HouseListItemModel(String errorDefault) {
        this.errorDefault = errorDefault;
    }

    public String getHid() {
        return hid;
    }

    public void setHid(String hid) {
        this.hid = hid;
    }

    public String getHouseContentOne() {
        return houseContentOne;
    }

    public void setHouseContentOne(String houseContentOne) {
        this.houseContentOne = houseContentOne;
    }

    public String getHouseContentTwo() {
        return houseContentTwo;
    }

    public void setHouseContentTwo(String houseContentTwo) {
        this.houseContentTwo = houseContentTwo;
    }

    public String getHouseContentThree() {
        return houseContentThree;
    }

    public void setHouseContentThree(String houseContentThree) {
        this.houseContentThree = houseContentThree;
    }

    public String getHouseContentFour() {
        return houseContentFour;
    }

    public void setHouseContentFour(String houseContentFour) {
        this.houseContentFour = houseContentFour;
    }

    public String getHouseContentFive() {
        return houseContentFive;
    }

    public void setHouseContentFive(String houseContentFive) {
        this.houseContentFive = houseContentFive;
    }

    public Drawable getHouseImage() {
        return houseImage;
    }

    public void setHouseImage(Drawable houseImage) {
        this.houseImage = houseImage;
    }

    public String getErrorDefault() {
        return errorDefault;
    }

    public void setErrorDefault(String errorDefault) {
        this.errorDefault = errorDefault;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getHouseDynamicURL() {
        return houseDynamicURL;
    }

    public void setHouseDynamicURL(String houseDynamicURL) {
        this.houseDynamicURL = houseDynamicURL;
    }

    public String getHouseDynamicPublished() {
        return houseDynamicPublished;
    }

    public void setHouseDynamicPublished(String houseDynamicPublished) {
        this.houseDynamicPublished = houseDynamicPublished;
    }

    public String getHouseDynamicTitle() {
        return houseDynamicTitle;
    }

    public void setHouseDynamicTitle(String houseDynamicTitle) {
        this.houseDynamicTitle = houseDynamicTitle;
    }
}
