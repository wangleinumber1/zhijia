package com.zhijia.service.data.Medol;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 *区域的json数据
 */
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY)
public class PlaceJsonModel {

    private String name ;

    private String parentId ;

    private String pid ;

    private String laticoor ;

    private String longcoor ;

    private List<PlaceJsonModel>  child = new ArrayList<PlaceJsonModel>() ;

    @JsonProperty(value="name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty(value="parentid")
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @JsonProperty(value="pid")
    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }


    @JsonProperty(value="laticoor")
    public String getLaticoor() {
        return laticoor;
    }

    public void setLaticoor(String laticoor) {
        this.laticoor = laticoor;
    }


    @JsonProperty(value="longcoor")
    public String getLongcoor() {
        return longcoor;
    }

    public void setLongcoor(String longcoor) {
        this.longcoor = longcoor;
    }


    @JsonProperty(value="child")
    public List<PlaceJsonModel> getChild() {
        return child;
    }

    public void setChild(List<PlaceJsonModel> child) {
        this.child = child;
    }
}
