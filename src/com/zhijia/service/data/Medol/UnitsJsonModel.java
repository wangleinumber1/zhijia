package com.zhijia.service.data.Medol;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * 主力户型
 */
public class UnitsJsonModel {

    private String total ;

    private List<UnitsListJsonModel>   list ;

    @JsonProperty(value = "list")
    public List<UnitsListJsonModel> getList() {
        return list;
    }

    public void setList(List<UnitsListJsonModel> list) {
        this.list = list;
    }

    @JsonProperty(value = "total")
    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public static class UnitsListJsonModel{

        private String area ;

        private String pic ;

        private String pid ;

        private String room ;

        private String title ;

        @JsonProperty(value = "area")
        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        @JsonProperty(value = "pic")
        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        @JsonProperty(value = "pid")
        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        @JsonProperty(value = "room")
        public String getRoom() {
            return room;
        }

        public void setRoom(String room) {
            this.room = room;
        }

        @JsonProperty(value = "title")
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
