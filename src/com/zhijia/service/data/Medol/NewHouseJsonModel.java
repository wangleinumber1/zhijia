package com.zhijia.service.data.Medol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 鏂版埧鐨凧son鏁版嵁
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewHouseJsonModel {

    private String hid ;

    private String name ;

    private String address ;

    private String averageprice ;

    private String titlepic ;

    private String salesstatus ;

    private String priceexplain ;

    private String areaname ;

    private String laticoor ;

    private String longcoor ;

    //鐢靛晢鐗规儬鐨勫墿浣欐椂闂� 
    private String time;

    private String opentimeCaption ;
    private String privilege;

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    public String getHid() {
        return hid;
    }

    public void setHid(String hid) {
        this.hid = hid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAverageprice() {
        return averageprice;
    }

    public void setAverageprice(String averageprice) {
        this.averageprice = averageprice;
    }

    public String getTitlepic() {
        return titlepic;
    }

    public void setTitlepic(String titlepic) {
        this.titlepic = titlepic;
    }

    public String getSalesstatus() {
        return salesstatus;
    }

    public void setSalesstatus(String salesstatus) {
        this.salesstatus = salesstatus;
    }

    public String getPriceexplain() {
        return priceexplain;
    }

    public void setPriceexplain(String priceexplain) {
        this.priceexplain = priceexplain;
    }

    public String getAreaname() {
        return areaname;
    }

    public void setAreaname(String areaname) {
        this.areaname = areaname;
    }

    public String getLaticoor() {
        return laticoor;
    }

    public void setLaticoor(String laticoor) {
        this.laticoor = laticoor;
    }

    public String getLongcoor() {
        return longcoor;
    }

    public void setLongcoor(String longcoor) {
        this.longcoor = longcoor;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @JsonProperty(value="opentime_caption")
    public String getOpentimeCaption() {
        return opentimeCaption;
    }

    public void setOpentimeCaption(String opentimeCaption) {
        this.opentimeCaption = opentimeCaption;
    }
}