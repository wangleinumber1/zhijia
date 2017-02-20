package com.zhijia.service.data.Medol;

import java.util.List;

/**
 *
 */
public class HousePicture {

    private String count;

    private String catname;

    private List<ListJsonModel> list;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getCatname() {
        return catname;
    }

    public void setCatname(String catname) {
        this.catname = catname;
    }

    public List<ListJsonModel> getList() {
        return list;
    }

    public void setList(List<ListJsonModel> list) {
        this.list = list;
    }

    public static class ListJsonModel {

        private String pid;

        private String pic;

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }
    }
}
