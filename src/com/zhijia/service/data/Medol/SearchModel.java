package com.zhijia.service.data.Medol;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 条件搜索
 */
public class SearchModel implements Serializable {

    private String areaid ;//区域ID

    private String circleid ;//区域里的子类的ID

    private String price ;//价格ID

    private String projecttype;//项目类型ID

    private String feature ;//建筑特征ID

    private String decorate; //装修状况ID

    private String opentime ;//开盘时间ID

    private String keyword ;//搜索关键字

    private String order ;//排序字段ID

    private String circleline ; //环线

    private String minX ;//地图x标轴最小值

    private String maxX ;//地图x标轴最大值

    private String minY ;//地图y标轴最小值

    private String maxY ;//地图y标轴最大值

    private String proportion ;

    private String room ;

    private String aspect ;

    private String houseage ;

    private String floor ;

    private String source ;

    private String tag ;

    private String rentType;//租房更多的类型

    private String rentProjectType ; // 租房更多里的项目类型。


    private String rentprice ;  //小区找房里要用的对应的是average_price

    private String sellprice ; //小区找房用的对应的是price

    private String type ;//小区找房的类型

    private String cid ;//小区列表对应的二手房


    public String getProportion() {
        return proportion;
    }

    public void setProportion(String proportion) {
        this.proportion = proportion;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getAspect() {
        return aspect;
    }

    public void setAspect(String aspect) {
        this.aspect = aspect;
    }

    public String getHouseage() {
        return houseage;
    }

    public void setHouseage(String houseage) {
        this.houseage = houseage;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getAreaid() {
        return areaid;
    }

    public void setAreaid(String areaid) {
        this.areaid = areaid;
    }

    public String getCircleid() {
        return circleid;
    }

    public void setCircleid(String circleid) {
        this.circleid = circleid;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProjecttype() {
        return projecttype;
    }

    public void setProjecttype(String projecttype) {
        this.projecttype = projecttype;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getDecorate() {
        return decorate;
    }

    public void setDecorate(String decorate) {
        this.decorate = decorate;
    }

    public String getOpentime() {
        return opentime;
    }

    public void setOpentime(String opentime) {
        this.opentime = opentime;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getCircleline() {
        return circleline;
    }

    public void setCircleline(String circleline) {
        this.circleline = circleline;
    }

    public String getMinX() {
        return minX;
    }

    public void setMinX(String minX) {
        this.minX = minX;
    }

    public String getMaxX() {
        return maxX;
    }

    public void setMaxX(String maxX) {
        this.maxX = maxX;
    }

    public String getMinY() {
        return minY;
    }

    public void setMinY(String minY) {
        this.minY = minY;
    }

    public String getMaxY() {
        return maxY;
    }

    public void setMaxY(String maxY) {
        this.maxY = maxY;
    }

    public String getRentType() {
        return rentType;
    }

    public void setRentType(String rentType) {
        this.rentType = rentType;
    }

    public String getRentProjectType() {
        return rentProjectType;
    }

    public void setRentProjectType(String rentProjectType) {
        this.rentProjectType = rentProjectType;
    }


    public String getSellprice() {
        return sellprice;
    }

    public void setSellprice(String sellprice) {
        this.sellprice = sellprice;
    }

    public String getRentprice() {
        return rentprice;
    }

    public void setRentprice(String rentprice) {
        this.rentprice = rentprice;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    @Override
    public String toString() {

        StringBuffer sb =new StringBuffer() ;
        sb.append("areaid:"+this.getAreaid()+"\n");
        sb.append("circleid:"+this.getCircleid()+"\n");
        sb.append("price:"+this.getPrice()+"\n");
        sb.append("projecttype:"+this.getProjecttype()+"\n");
        sb.append("feature:"+this.getFeature()+"\n");
        sb.append("decorate:"+this.getDecorate()+"\n");
        sb.append("opentime:"+this.getOpentime()+"\n");
        sb.append("keyword:"+this.getKeyword()+"\n");
        sb.append("order:"+this.getOrder()+"\n");
        sb.append("circleline:"+this.getCircleline()+"\n");
        sb.append("minX:"+this.getMaxX()+"\n");
        sb.append("maxX:"+this.getMaxX()+"\n");
        sb.append("minY:"+this.getMinY()+"\n");
        sb.append("maxY:"+this.getMaxY()+"\n");
        return sb.toString();
    }


    public Map<String,String> toUnXYMap(){
        Map<String,String> resultMap = new HashMap<String, String>() ;

        if(this.getAreaid() !=null && !this.getAreaid().isEmpty()){
            resultMap.put("areaid",this.getAreaid()) ;
        }

        if(this.getCircleid() !=null && !this.getCircleid().isEmpty()){
            resultMap.put("circleid",this.getCircleid()) ;
        }

        if(this.getPrice() != null && !this.getPrice().isEmpty()) {
            resultMap.put("price",this.getPrice()) ;
        }

        if(this.getProjecttype() != null && !this.getProjecttype().isEmpty()){
            resultMap.put("projecttype",this.getProjecttype()) ;
        }

        if(this.getFeature() != null && !this.getFeature().isEmpty()){
            resultMap.put("feature",this.getFeature()) ;
        }

        if(this.getDecorate() != null && !this.getDecorate().isEmpty()){
            resultMap.put("decorate",this.getDecorate()) ;
        }

        if(this.getOpentime()!=null && !this.getOpentime().isEmpty()){
            resultMap.put("opentime",this.getOpentime()) ;
        }

        if(this.getKeyword() !=null && !this.getKeyword().isEmpty()){
            resultMap.put("keyword",this.getKeyword()) ;
        }

        if(this.getOrder() != null && !this.getOrder().isEmpty()){
            resultMap.put("order",this.getOrder()) ;
        }

        if(this.getCircleline() != null && !this.getCircleline().isEmpty()){
            resultMap.put("circleline",this.getCircleline()) ;
        }

        if(this.getProportion() != null && !this.getProportion().isEmpty()){
            resultMap.put("area",this.getProportion()) ;
        }

        if(this.getRoom() != null && !this.getRoom().isEmpty()){
            resultMap.put("room",this.getRoom());
        }

        if(this.getAspect() != null && !this.getAspect().isEmpty()){
            resultMap.put("aspect",this.getAspect()) ;
        }

        if(this.getHouseage() != null && !this.getHouseage().isEmpty()){
            resultMap.put("houseage",this.getHouseage()) ;
        }

        if(this.getFloor() != null && !this.getFloor().isEmpty()){
            resultMap.put("floor",this.getFloor()) ;
        }

        if(this.getSource() != null && !this.getSource().isEmpty()){
            resultMap.put("source",this.getSource()) ;
        }

        if(this.getTag() != null && !this.getTag().isEmpty()){
            resultMap.put("tag",this.getTag()) ;
        }

        if(this.getRentType() != null && !this.getRentType().isEmpty()){
            resultMap.put("renttype",this.getRentType()) ;
        }

        if(this.getRentProjectType() != null && !this.getRentProjectType().isEmpty()){
            resultMap.put("project_type",this.getRentProjectType()) ;
        }

        if (this.getSellprice() != null && !this.getSellprice().isEmpty()){
            resultMap.put("sellprice",this.getSellprice()) ;
        }

        if(this.getRentprice() != null && !this.getRentType().isEmpty()){
            resultMap.put("rentprice",this.getRentprice()) ;
        }

        if(this.getType() != null && !this.getType().isEmpty()){
            resultMap.put("type",this.getRentprice()) ;
        }

        if(this.getCid() != null && !this.getCid().isEmpty()){
            resultMap.put("cid",this.getCid()) ;
        }

        return resultMap ;
    }


    public Map<String,String> toMap(){
        Map<String,String> resultMap = new HashMap<String, String>() ;

        if(this.getAreaid() !=null && !this.getAreaid().isEmpty()){
            resultMap.put("areaid",this.getAreaid()) ;
        }

        if(this.getCircleid() !=null && !this.getCircleid().isEmpty()){
            resultMap.put("circleid",this.getCircleid()) ;
        }

        if(this.getPrice() != null && !this.getPrice().isEmpty()) {
            resultMap.put("price",this.getPrice()) ;
        }

        if(this.getProjecttype() != null && !this.getProjecttype().isEmpty()){
            resultMap.put("projecttype",this.getProjecttype()) ;
        }

        if(this.getFeature() != null && !this.getFeature().isEmpty()){
            resultMap.put("feature",this.getFeature()) ;
        }

        if(this.getDecorate() != null && !this.getDecorate().isEmpty()){
            resultMap.put("decorate",this.getDecorate()) ;
        }

        if(this.getOpentime()!=null && !this.getOpentime().isEmpty()){
            resultMap.put("opentime",this.getOpentime()) ;
        }

        if(this.getKeyword() !=null && !this.getKeyword().isEmpty()){
            resultMap.put("keyword",this.getKeyword()) ;
        }

        if(this.getOrder() != null && !this.getOrder().isEmpty()){
            resultMap.put("order",this.getOrder()) ;
        }

        if(this.getCircleline() != null && !this.getCircleline().isEmpty()){
            resultMap.put("circleline",this.getCircleline()) ;
        }

        if(this.getMinX() != null && !this.getMinX().isEmpty()){
            resultMap.put("minX",this.getMinX()) ;
        }

        if(this.getMaxX() !=null && !this.getMaxX().isEmpty()){
            resultMap.put("maxX",this.getMaxX()) ;
        }

        if(this.getMinY() != null && !this.getMinY().isEmpty()){
            resultMap.put("minY",this.getMinY()) ;
        }

        if(this.getMaxY() !=null && !this.getMaxY().isEmpty()){
            resultMap.put("maxY",this.getMaxY()) ;
        }

        if(this.getProportion() != null && !this.getProportion().isEmpty()){
            resultMap.put("area",this.getProportion()) ;
        }

        if(this.getRoom() != null && !this.getRoom().isEmpty()){
            resultMap.put("room",this.getRoom());
        }

        if(this.getAspect() != null && !this.getAspect().isEmpty()){
            resultMap.put("aspect",this.getAspect()) ;
        }

        if(this.getHouseage() != null && !this.getHouseage().isEmpty()){
            resultMap.put("houseage",this.getHouseage()) ;
        }

        if(this.getFloor() != null && !this.getFloor().isEmpty()){
            resultMap.put("floor",this.getFloor()) ;
        }

        if(this.getSource() != null && !this.getSource().isEmpty()){
            resultMap.put("source",this.getSource()) ;
        }

        if(this.getTag() != null && !this.getTag().isEmpty()){
            resultMap.put("tag",this.getTag()) ;
        }

        if(this.getRentType() != null && !this.getRentType().isEmpty()){
            resultMap.put("renttype",this.getRentType()) ;
        }

        if(this.getRentProjectType() != null && !this.getRentProjectType().isEmpty()){
            resultMap.put("project_type",this.getRentProjectType()) ;
        }

        if (this.getSellprice() != null && !this.getSellprice().isEmpty()){
            resultMap.put("sellprice",this.getSellprice()) ;
        }

        if(this.getRentprice() != null && !this.getRentType().isEmpty()){
            resultMap.put("rentprice",this.getRentprice()) ;
        }

        if(this.getType() != null && !this.getType().isEmpty()){
            resultMap.put("type",this.getRentprice()) ;
        }

        if(this.getCid() != null && !this.getCid().isEmpty()){
            resultMap.put("cid",this.getCid()) ;
        }

        return resultMap ;
    }
}