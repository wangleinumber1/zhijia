package com.zhijia.util;

import com.baidu.platform.comapi.basestruct.GeoPoint;

/**
 * 地图要打标记的点的模型
 */
public class MapOverLayModel {

    private String hid;

    //用于显示的某一点的名字
    private String name;

    //坐标点
    private GeoPoint geoPoint;

    //图片
    private String drawable;

    //文字描述，可以是HTML
    private String htmlText;

    public MapOverLayModel(String hid, String name, GeoPoint geoPoint, String drawable, String htmlText) {
        this.hid = hid;
        this.name = name;
        this.geoPoint = geoPoint;
        this.drawable = drawable;
        this.htmlText = htmlText;
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

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }

    public String getDrawable() {
        return drawable;
    }

    public void setDrawable(String drawable) {
        this.drawable = drawable;
    }

    public String getHtmlText() {
        return htmlText;
    }

    public void setHtmlText(String htmlText) {
        this.htmlText = htmlText;
    }
}
