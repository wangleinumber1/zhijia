package com.zhijia.service.data.Medol;

/**
 * 城市属性实体类
 */
public class CityModel {

    private String cityId;

    //城市名字
    private String cityName;

    //城市首字母
    private String nameSort;

    private String laticoor;

    private String longcoor;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getNameSort() {
        return nameSort;
    }

    public void setNameSort(String nameSort) {
        this.nameSort = nameSort;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
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
}
