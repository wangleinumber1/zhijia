package com.zhijia.service.data.Medol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 城市模型
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CityJsonModel extends BaseJsonModel {

    private String cityid;

    private String letter;

    private String letters;

    private String name;

    private String hot;

    private String laticoor;

    private String longcoor;

    private String status;

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public String getLetters() {
        return letters;
    }

    public void setLetters(String letters) {
        this.letters = letters;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHot() {
        return hot;
    }

    public void setHot(String hot) {
        this.hot = hot;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
