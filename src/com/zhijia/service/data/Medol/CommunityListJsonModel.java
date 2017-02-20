package com.zhijia.service.data.Medol;

/**
 * 小区找房对应的JSON数据
 */
public class CommunityListJsonModel {

    private String communityid;//小区ID

    private String communityname;//小区名称

    private String titlepic; //标题图

    private String areaid ;

    private String address ;

    private String sellprice ;

    private String sellratio ;  //均价的涨幅指数

    private String sellnum ;//出售套数

    private String rentprice ; // 租金

    private String rentratio ;//租金的涨幅指数

    private String rentnum ;//出租套数

    private String areaname ;//区域名

    public String getCommunityid() {
        return communityid;
    }

    public void setCommunityid(String communityid) {
        this.communityid = communityid;
    }

    public String getCommunityname() {
        return communityname;
    }

    public void setCommunityname(String communityname) {
        this.communityname = communityname;
    }

    public String getTitlepic() {
        return titlepic;
    }

    public void setTitlepic(String titlepic) {
        this.titlepic = titlepic;
    }

    public String getAreaid() {
        return areaid;
    }

    public void setAreaid(String areaid) {
        this.areaid = areaid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSellprice() {
        return sellprice;
    }

    public void setSellprice(String sellprice) {
        this.sellprice = sellprice;
    }

    public String getSellratio() {
        return sellratio;
    }

    public void setSellratio(String sellratio) {
        this.sellratio = sellratio;
    }

    public String getSellnum() {
        return sellnum;
    }

    public void setSellnum(String sellnum) {
        this.sellnum = sellnum;
    }

    public String getRentprice() {
        return rentprice;
    }

    public void setRentprice(String rentprice) {
        this.rentprice = rentprice;
    }

    public String getRentratio() {
        return rentratio;
    }

    public void setRentratio(String rentratio) {
        this.rentratio = rentratio;
    }

    public String getRentnum() {
        return rentnum;
    }

    public void setRentnum(String rentnum) {
        this.rentnum = rentnum;
    }

    public String getAreaname() {
        return areaname;
    }

    public void setAreaname(String areaname) {
        this.areaname = areaname;
    }
}
