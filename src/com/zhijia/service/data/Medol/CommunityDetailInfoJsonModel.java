package com.zhijia.service.data.Medol;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 */
public class CommunityDetailInfoJsonModel {

    @JsonProperty(value = "sellprice")
    private String sellPrice ;

    @JsonProperty(value = "sellratio")
    private String sellRatio ;

    @JsonProperty(value = "sellnum")
    private String sellNum ;

    @JsonProperty(value = "rentnum")
    private String rentNum ;

    @JsonProperty(value = "info")
    private InfoJsonModel info ;


    @JsonProperty(value = "trend")
    private TrendJsonModel trend ;

    @JsonProperty(value = "map")
    private MapJsonModel map ;

    public class TrendJsonModel{

        private String sell ;

        private String rent ;

        public String getSell() {
            return sell;
        }

        public void setSell(String sell) {
            this.sell = sell;
        }

        public String getRent() {
            return rent;
        }

        public void setRent(String rent) {
            this.rent = rent;
        }
    }

    public class MapJsonModel{

        private  String map ;

        private String address ;

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
    }


    public TrendJsonModel getTrend() {
        return trend;
    }

    public void setTrend(TrendJsonModel trend) {
        this.trend = trend;
    }

    public MapJsonModel getMap() {
        return map;
    }

    public void setMap(MapJsonModel map) {
        this.map = map;
    }

    public class InfoJsonModel{

        @JsonProperty(value = "areaname")
        private String areaName ;

        @JsonProperty(value = "address")
        private String address ;

        @JsonProperty(value = "projecttype")
        private String projectType  ;

        @JsonProperty(value = "carport")
        private String carport;

        @JsonProperty(value = "propertyprice")
        private String propertyPrice ;

        @JsonProperty(value = "propertycompany")
        private String propertyCompany ;

        @JsonProperty(value = "buildarea")
        private String buildArea  ;

        @JsonProperty(value = "landarea")
        private String  landArea ;

        @JsonProperty(value = "virescence")
        private String viresCence;

        @JsonProperty(value = "cubage")
        private String cubAge ;

        @JsonProperty(value = "senddate")
        private String sendDate ;

        @JsonProperty(value = "info")
        private String info ;

        @JsonProperty(value = "assort")
        private String assort ;

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getProjectType() {
            return projectType;
        }

        public void setProjectType(String projectType) {
            this.projectType = projectType;
        }

        public String getCarport() {
            return carport;
        }

        public void setCarport(String carport) {
            this.carport = carport;
        }

        public String getPropertyPrice() {
            return propertyPrice;
        }

        public void setPropertyPrice(String propertyPrice) {
            this.propertyPrice = propertyPrice;
        }

        public String getPropertyCompany() {
            return propertyCompany;
        }

        public void setPropertyCompany(String propertyCompany) {
            this.propertyCompany = propertyCompany;
        }

        public String getBuildArea() {
            return buildArea;
        }

        public void setBuildArea(String buildArea) {
            this.buildArea = buildArea;
        }

        public String getLandArea() {
            return landArea;
        }

        public void setLandArea(String landArea) {
            this.landArea = landArea;
        }

        public String getViresCence() {
            return viresCence;
        }

        public void setViresCence(String viresCence) {
            this.viresCence = viresCence;
        }

        public String getCubAge() {
            return cubAge;
        }

        public void setCubAge(String cubAge) {
            this.cubAge = cubAge;
        }

        public String getSendDate() {
            return sendDate;
        }

        public void setSendDate(String sendDate) {
            this.sendDate = sendDate;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getAssort() {
            return assort;
        }

        public void setAssort(String assort) {
            this.assort = assort;
        }
    }



    public String getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(String sellPrice) {
        this.sellPrice = sellPrice;
    }

    public String getSellRatio() {
        return sellRatio;
    }

    public void setSellRatio(String sellRatio) {
        this.sellRatio = sellRatio;
    }

    public String getSellNum() {
        return sellNum;
    }

    public void setSellNum(String sellNum) {
        this.sellNum = sellNum;
    }

    public String getRentNum() {
        return rentNum;
    }

    public void setRentNum(String rentNum) {
        this.rentNum = rentNum;
    }

    public InfoJsonModel getInfo() {
        return info;
    }

    public void setInfo(InfoJsonModel info) {
        this.info = info;
    }


}
