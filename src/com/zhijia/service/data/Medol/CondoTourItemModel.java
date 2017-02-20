package com.zhijia.service.data.Medol;

import java.util.List;

/**
 * 看房团列表模型
 */
public class CondoTourItemModel {

    private String id;
    private String title;
    private String privilege;
    private String recommend;
    private String nums;
    private String looktime;
    private List<String> pic;
    private String type;
    private String averageprice;
    private String address;
    private String special;
    private List<Line> line;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    public String getNums() {
        return nums;
    }

    public void setNums(String nums) {
        this.nums = nums;
    }

    public String getLooktime() {
        return looktime;
    }

    public void setLooktime(String looktime) {
        this.looktime = looktime;
    }

    public List<String> getPic() {
        return pic;
    }

    public void setPic(List<String> pic) {
        this.pic = pic;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAverageprice() {
        return averageprice;
    }

    public void setAverageprice(String averageprice) {
        this.averageprice = averageprice;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public List<Line> getLine() {
        return line;
    }

    public void setLine(List<Line> line) {
        this.line = line;
    }

    public static class Line {
        private String hid;
        private String name;
        private String averageprice;
        private String special;
        private String place;

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

        public String getAverageprice() {
            return averageprice;
        }

        public void setAverageprice(String averageprice) {
            this.averageprice = averageprice;
        }

        public String getSpecial() {
            return special;
        }

        public void setSpecial(String special) {
            this.special = special;
        }

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
        }
    }
}