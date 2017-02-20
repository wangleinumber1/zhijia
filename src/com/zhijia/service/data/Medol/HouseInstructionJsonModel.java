package com.zhijia.service.data.Medol;

import java.util.List;

/**
 *房子说明的json对象
 */
public class HouseInstructionJsonModel {

    //房源标题
    private String title ;

    //房源特色
    private List<String> tagtext ;

    //更新时间
    private String updatetime ;

    private String price ;

    private String area ;

    private String unit ;//户型

    private String aspect ;//朝向

    private String  floor;//楼层

    private String property ;//房屋类型

    private String decorate;//装修情况

    private String propertyright ;//产权

    private String houseage ;//房龄

    public String getPropertyright() {
        return propertyright;
    }

    public void setPropertyright(String propertyright) {
        this.propertyright = propertyright;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getTagtext() {
        return tagtext;
    }

    public void setTagtext(List<String> tagtext) {
        this.tagtext = tagtext;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getAspect() {
        return aspect;
    }

    public void setAspect(String aspect) {
        this.aspect = aspect;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getDecorate() {
        return decorate;
    }

    public void setDecorate(String decorate) {
        this.decorate = decorate;
    }

    public String getHouseage() {
        return houseage;
    }

    public void setHouseage(String houseage) {
        this.houseage = houseage;
    }


}
