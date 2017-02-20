package com.zhijia.service.data.Medol;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * 楼盘简介
 */
public class IntroductionJsonModel {

    private String name ;

    private List<String> buildfeature ;

    private String averageprice ;

    private String priceexplain ;

    private String opentimeYearMonth ;

    private String propertyage ;

    private String roomarea ;

    private String roomtype ;

    @JsonProperty(value = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty(value = "buildfeature")
    public List<String> getBuildfeature() {
        return buildfeature;
    }

    public void setBuildfeature(List<String> buildfeature) {
        this.buildfeature = buildfeature;
    }

    @JsonProperty(value = "averageprice")
    public String getAverageprice() {
        return averageprice;
    }

    public void setAverageprice(String averageprice) {
        this.averageprice = averageprice;
    }

    @JsonProperty(value = "priceexplain")
    public String getPriceexplain() {
        return priceexplain;
    }

    public void setPriceexplain(String priceexplain) {
        this.priceexplain = priceexplain;
    }

    @JsonProperty(value = "opentime_year_month")
    public String getOpentimeYearMonth() {
        return opentimeYearMonth;
    }

    public void setOpentimeYearMonth(String opentimeYearMonth) {
        this.opentimeYearMonth = opentimeYearMonth;
    }

    @JsonProperty(value = "propertyage")
    public String getPropertyage() {
        return propertyage;
    }

    public void setPropertyage(String propertyage) {
        this.propertyage = propertyage;
    }

    @JsonProperty(value = "roomarea")
    public String getRoomarea() {
        return roomarea;
    }

    public void setRoomarea(String roomarea) {
        this.roomarea = roomarea;
    }

    @JsonProperty(value = "roomtype")
    public String getRoomtype() {
        return roomtype;
    }

    public void setRoomtype(String roomtype) {
        this.roomtype = roomtype;
    }
}