package com.zhijia.service.data.Medol;

import java.util.List;

/**
 * 印象
 */
public class ImpressionJsonModel {

    private  String total ;

    private List<ListJsonModel> list ;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<ListJsonModel> getList() {
        return list;
    }

    public void setList(List<ListJsonModel> list) {
        this.list = list;
    }

    public static class ListJsonModel{

        private String id ;

        private String name ;

        private String bgcolor ;

        private String support ;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBgcolor() {
            return bgcolor;
        }

        public void setBgcolor(String bgcolor) {
            this.bgcolor = bgcolor;
        }

        public String getSupport() {
            return support;
        }

        public void setSupport(String support) {
            this.support = support;
        }
    }
}
