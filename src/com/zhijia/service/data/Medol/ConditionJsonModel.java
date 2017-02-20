package com.zhijia.service.data.Medol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 条件的json
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConditionJsonModel {

    private List<PlaceJsonModel> place ;

    private TreeMap<String,PriceJsonModel> price ;

    private Map<String,TypeJsonModel> type ;

    private Map<String,FeatureJsonModel> feature ;

    private Map<String,DecorateJsonModel> decorate ;

    private Map<String,OpentimeJsonModel> opentime ;

    private Map<String,CircleLineJsonModel>  circleLine ;

    private Map<String,OrderJsonModel> order ;

    private TreeMap<String,AreaJsonModel> area ; //面积 2手

    private Map<String,RoomJsonModel> room ;//2手房 户型

    private Map<String,AspectJsonModel> aspect ; //朝向 2手

    private Map<String,HouseageJsonModel> houseage ;  //房龄 2手

    private Map<String,FloorJsonModel> floor ; //楼层 2手

    private Map<String,String> source ; // 来源 2手

    private Map<String,String> tag ; //标签 2手

    private Map<String,PriceJsonModel> sellPrice ;

    private Map<String,AveragePriceJsonModel> rentPrice ;


    private Map<String,AveragePriceJsonModel> averagePrice ;

    private Map<String,ProjectTypeJsonModel> projectType ;

    @JsonProperty(value="price")
    public TreeMap<String, PriceJsonModel> getPrice() {
        return price;
    }

    public void setPrice(TreeMap<String, PriceJsonModel> price) {
        this.price = price;
    }

    @JsonProperty(value="type")
    public Map<String, TypeJsonModel> getType() {
        return type;
    }


    public void setType(Map<String, TypeJsonModel> type) {
        this.type = type;
    }

    @JsonProperty(value="feature")
    public Map<String, FeatureJsonModel> getFeature() {
        return feature;
    }

    public void setFeature(Map<String, FeatureJsonModel> feature) {
        this.feature = feature;
    }

    @JsonProperty(value="decorate")
    public Map<String, DecorateJsonModel> getDecorate() {
        return decorate;
    }

    public void setDecorate(Map<String, DecorateJsonModel> decorate) {
        this.decorate = decorate;
    }

    @JsonProperty(value="opentime")
    public Map<String, OpentimeJsonModel> getOpentime() {
        return opentime;
    }

    public void setOpentime(Map<String, OpentimeJsonModel> opentime) {
        this.opentime = opentime;
    }


    @JsonProperty(value="circle_line")
    public Map<String, CircleLineJsonModel> getCircleLine() {
        return circleLine;
    }

    public void setCircleLine(Map<String, CircleLineJsonModel> circleLine) {
        this.circleLine = circleLine;
    }

    @JsonProperty(value="order")
    public Map<String, OrderJsonModel> getOrder() {
        return order;
    }

    public void setOrder(Map<String, OrderJsonModel> order) {
        this.order = order;
    }

    @JsonProperty(value="place")
    public List<PlaceJsonModel> getPlace() {
        return place;
    }

    public void setPlace(List<PlaceJsonModel> place) {
        this.place = place;
    }

    @JsonProperty(value="area")
    public TreeMap<String, AreaJsonModel> getArea() {
        return area;
    }

    public void setArea(TreeMap<String, AreaJsonModel> area) {
        this.area = area;
    }

    @JsonProperty(value="room")
    public Map<String, RoomJsonModel> getRoom() {
        return room;
    }

    public void setRoom(Map<String, RoomJsonModel> room) {
        this.room = room;
    }

    @JsonProperty(value="aspect")
    public Map<String, AspectJsonModel> getAspect() {
        return aspect;
    }

    public void setAspect(Map<String, AspectJsonModel> aspect) {
        this.aspect = aspect;
    }


    @JsonProperty(value="houseage")
    public Map<String, HouseageJsonModel> getHouseage() {
        return houseage;
    }

    public void setHouseage(Map<String, HouseageJsonModel> houseage) {
        this.houseage = houseage;
    }

    @JsonProperty(value="floor")
    public Map<String, FloorJsonModel> getFloor() {
        return floor;
    }

    public void setFloor(Map<String, FloorJsonModel> floor) {
        this.floor = floor;
    }

    @JsonProperty(value="source")
    public Map<String, String> getSource() {
        return source;
    }

    public void setSource(Map<String, String> source) {
        this.source = source;
    }

    @JsonProperty(value="tag")
    public Map<String, String> getTag() {
        return tag;
    }

    public void setTag(Map<String, String> tag) {
        this.tag = tag;
    }

    @JsonProperty(value="project_type")
    public Map<String, ProjectTypeJsonModel> getProjectType() {
        return projectType;
    }

    public void setProjectType(Map<String, ProjectTypeJsonModel> projectType) {
        this.projectType = projectType;
    }

    @JsonProperty(value="average_price")
    public Map<String, AveragePriceJsonModel> getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(Map<String, AveragePriceJsonModel> averagePrice) {
        this.averagePrice = averagePrice;
    }

    @JsonProperty(value="sellprice")
    public Map<String, PriceJsonModel> getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(Map<String, PriceJsonModel> sellPrice) {
        this.sellPrice = sellPrice;
    }

    @JsonProperty(value = "rentprice")
    public Map<String, AveragePriceJsonModel> getRentPrice() {
        return rentPrice;
    }

    public void setRentPrice(Map<String, AveragePriceJsonModel> rentPrice) {
        this.rentPrice = rentPrice;
    }
}
