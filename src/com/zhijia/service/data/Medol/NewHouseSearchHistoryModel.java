package com.zhijia.service.data.Medol;

/**
 * 新房搜索条件
 */
public class NewHouseSearchHistoryModel extends SearchHistoryBaseModel {

    private String areaId = "";
    private String area = "";
    private String circleid = "";
    private String decorationId = "";
    private String decoration = "";
    private String priceId = "";
    private String price = "";
    private String typeId = "";
    private String type = "";
    private String featureId = "";
    private String feature = "";
    private String openTimeId = "";
    private String openTime = "";

    @Override
    public String toShortString() {
        String returnStr = getKeyword() + " " + getArea() + " " + getPrice() + " " + getType() + " " + getFeature() + " " + getDecoration() + " " + getOpenTime();
        return returnStr.trim();
    }

    @Override
    public String toString() {
        return super.toString() +
                ", areaId='" + areaId + '\'' +
                ", area='" + area + '\'' +
                ", circleid='" + circleid + '\'' +
                ", decorationId='" + decorationId + '\'' +
                ", decoration='" + decoration + '\'' +
                ", priceId='" + priceId + '\'' +
                ", price='" + price + '\'' +
                ", typeId='" + typeId + '\'' +
                ", type='" + type + '\'' +
                ", featureId='" + featureId + '\'' +
                ", feature='" + feature + '\'' +
                ", openTimeId='" + openTimeId + '\'' +
                ", openTime='" + openTime + '\'';
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCircleid() {
        return circleid;
    }

    public void setCircleid(String circleid) {
        this.circleid = circleid;
    }

    public String getDecorationId() {
        return decorationId;
    }

    public void setDecorationId(String decorationId) {
        this.decorationId = decorationId;
    }

    public String getDecoration() {
        return decoration;
    }

    public void setDecoration(String decoration) {
        this.decoration = decoration;
    }

    public String getPriceId() {
        return priceId;
    }

    public void setPriceId(String priceId) {
        this.priceId = priceId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFeatureId() {
        return featureId;
    }

    public void setFeatureId(String featureId) {
        this.featureId = featureId;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getOpenTimeId() {
        return openTimeId;
    }

    public void setOpenTimeId(String openTimeId) {
        this.openTimeId = openTimeId;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }
}
