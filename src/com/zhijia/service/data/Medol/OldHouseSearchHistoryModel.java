package com.zhijia.service.data.Medol;

/**
 * 二手房搜索条件
 */
public class OldHouseSearchHistoryModel extends SearchHistoryBaseModel {

    private String areaId = "";
    private String area = "";
    private String circleid = "";
    private String decorationId = "";
    private String decoration = "";
    private String totalPriceId = "";
    private String totalPrice = "";
    private String proportionId = "";
    private String proportion = "";
    private String roomTypeId = "";
    private String roomType = "";
    private String sourceId = "";
    private String source = "";

    @Override
    public String toShortString() {
        String returnStr = getKeyword() + " " + getArea() + " " + getTotalPrice() + " " + getProportion() + " " + getRoomType() + " " + getSource() + " " + getDecoration();
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
                ", totalPriceId='" + totalPriceId + '\'' +
                ", totalPrice='" + totalPrice + '\'' +
                ", proportionId='" + proportionId + '\'' +
                ", proportion='" + proportion + '\'' +
                ", roomTypeId='" + roomTypeId + '\'' +
                ", roomType='" + roomType + '\'' +
                ", sourceId='" + sourceId + '\'' +
                ", source='" + source + '\'';
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

    public String getTotalPriceId() {
        return totalPriceId;
    }

    public void setTotalPriceId(String totalPriceId) {
        this.totalPriceId = totalPriceId;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getProportionId() {
        return proportionId;
    }

    public void setProportionId(String proportionId) {
        this.proportionId = proportionId;
    }

    public String getProportion() {
        return proportion;
    }

    public void setProportion(String proportion) {
        this.proportion = proportion;
    }

    public String getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(String roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
