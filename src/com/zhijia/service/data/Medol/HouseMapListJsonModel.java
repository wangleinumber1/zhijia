package com.zhijia.service.data.Medol;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 */
public class HouseMapListJsonModel {

    @JsonProperty(value = "communityid")
    private String communityId;

    @JsonProperty(value = "communityname")
    private String communityName;

     @JsonProperty(value = "mapx")
    private String mapx;

    @JsonProperty(value = "mapy")
    private String mapy;

    @JsonProperty(value = "housenums")
    private String housenums;

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getMapx() {
        return mapx;
    }

    public void setMapx(String mapx) {
        this.mapx = mapx;
    }

    public String getMapy() {
        return mapy;
    }

    public void setMapy(String mapy) {
        this.mapy = mapy;
    }

    public String getHousenums() {
        return housenums;
    }

    public void setHousenums(String housenums) {
        this.housenums = housenums;
    }
}
