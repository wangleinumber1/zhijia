package com.zhijia.service.data.Medol;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 浜屾墜鎴胯鎯� */
public class OldHouseDetailJsonModel {

   //灏忓尯鍚嶇О
    private String communityname ;

    //缁忕邯浜虹數璇�  
    private String agencyMobile ;
    private String agency_name;

    private String usertype ;

    private PicdataJsonModel picdata ;

    private HouseInfoJsonModel houseiInfo ;

    private HouseInstructionJsonModel houseInstruction ;


	@JsonProperty(value = "agency_name")
    public String getAgency_name() {
		return agency_name;
	}

	public void setAgency_name(String agency_name) {
		this.agency_name = agency_name;
	}

	@JsonProperty(value = "communityname")
    public String getCommunityname() {
        return communityname;
    }

    public void setCommunityname(String communityname) {
        this.communityname = communityname;
    }

    @JsonProperty(value = "agency_mobile")
    public String getAgencyMobile() {
        return agencyMobile;
    }

    public void setAgencyMobile(String agencyMobile) {
        this.agencyMobile = agencyMobile;
    }

    @JsonProperty(value = "usertype")
    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    @JsonProperty(value = "picdata")
    public PicdataJsonModel getPicdata() {
        return picdata;
    }

    public void setPicdata(PicdataJsonModel picdata) {
        this.picdata = picdata;
    }

    @JsonProperty(value="house_info")
    public HouseInfoJsonModel getHouseiInfo() {
        return houseiInfo;
    }

    public void setHouseiInfo(HouseInfoJsonModel houseiInfo) {
        this.houseiInfo = houseiInfo;
    }

    @JsonProperty(value="house_instruction")
    public HouseInstructionJsonModel getHouseInstruction() {
        return houseInstruction;
    }

    public void setHouseInstruction(HouseInstructionJsonModel houseInstruction) {
        this.houseInstruction = houseInstruction;
    }
}