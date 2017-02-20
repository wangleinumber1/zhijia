package com.zhijia.service.data.Medol;

/**
 *房子介绍
 */
public class HouseInfoJsonModel {

    private DescriptionJsonModel description ;

    private CommunityJsonModel community ;

    private String trend ;

    private String map  ;

    private String address ;

    private String bus ;

    public DescriptionJsonModel getDescription() {
        return description;
    }

    public void setDescription(DescriptionJsonModel description) {
        this.description = description;
    }

    public CommunityJsonModel getCommunity() {
        return community;
    }

    public void setCommunity(CommunityJsonModel community) {
        this.community = community;
    }

    public String getTrend() {
        return trend;
    }

    public void setTrend(String trend) {
        this.trend = trend;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBus() {
        return bus;
    }

    public void setBus(String bus) {
        this.bus = bus;
    }
}