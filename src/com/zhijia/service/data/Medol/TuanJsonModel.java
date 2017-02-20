package com.zhijia.service.data.Medol;

/**
 * 团信息
 */
public class TuanJsonModel {

    //标题
    private String title ;

    //优惠
    private String privilege ;

    //报名人数
    private String nums ;

    //截止日期
    private String endtime ;

    //剩余天数
    private String end ;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNums() {
        return nums;
    }

    public void setNums(String nums) {
        this.nums = nums;
    }

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }


}
