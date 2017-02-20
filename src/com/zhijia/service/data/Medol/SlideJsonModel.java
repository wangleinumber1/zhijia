package com.zhijia.service.data.Medol;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * 新房详情的幻灯片
 */
public class SlideJsonModel {

    private String count ;

    private List<ListJsonModel> list ;


    @JsonProperty(value = "count")
    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    @JsonProperty(value = "list")
    public List<ListJsonModel> getList() {
        return list;
    }

    public void setList(List<ListJsonModel> list) {
        this.list = list;
    }

    public static class ListJsonModel{

        private String pid ;

        private String pic ;


        @JsonProperty(value = "pid")
        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        @JsonProperty(value = "pic")
        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }
    }
}
