package com.zhijia.service.data.Medol;

/**
 * 小区信息
 */
public class CommunityJsonModel {

    private String communityid ;

    private String communityname ;

    private String sellprice ;

    private String sellratio ;

    private String sellnum ;

    private String rentnum ;

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

    public String getRentnum() {
        return rentnum;
    }

    public void setRentnum(String rentnum) {
        this.rentnum = rentnum;
    }
}
