package com.zhijia.service.data.Medol;

/**
 * 适配器模型
 */
public class AdapterModel {

    //目前暂时使用int，如果通过网络需要使用图形对象
    private int adapterImage ;

    //这个是表示房子的名字
    private String adapterHouseName ;

    //这个是表示离房源的距离
    private String adapterHouseDistance ;

    //那个区域的
    private String adapterHouseAreaName ;

    //房子的价钱
    private String adapterHouseMoney ;

    //地址
    private String adapterHouseAddress ;

    //描述
    private String adapterHouseDes ;


    public int getAdapterImage() {
        return adapterImage;
    }

    public void setAdapterImage(int adapterImage) {
        this.adapterImage = adapterImage;
    }

    public String getAdapterHouseName() {
        return adapterHouseName;
    }

    public void setAdapterHouseName(String adapterHouseName) {
        this.adapterHouseName = adapterHouseName;
    }

    public String getAdapterHouseDistance() {
        return adapterHouseDistance;
    }

    public void setAdapterHouseDistance(String adapterHouseDistance) {
        this.adapterHouseDistance = adapterHouseDistance;
    }

    public String getAdapterHouseAreaName() {
        return adapterHouseAreaName;
    }

    public void setAdapterHouseAreaName(String adapterHouseAreaName) {
        this.adapterHouseAreaName = adapterHouseAreaName;
    }

    public String getAdapterHouseMoney() {
        return adapterHouseMoney;
    }

    public void setAdapterHouseMoney(String adapterHouseMoney) {
        this.adapterHouseMoney = adapterHouseMoney;
    }

    public String getAdapterHouseAddress() {
        return adapterHouseAddress;
    }

    public void setAdapterHouseAddress(String adapterHouseAddress) {
        this.adapterHouseAddress = adapterHouseAddress;
    }

    public String getAdapterHouseDes() {
        return adapterHouseDes;
    }

    public void setAdapterHouseDes(String adapterHouseDes) {
        this.adapterHouseDes = adapterHouseDes;
    }


}
