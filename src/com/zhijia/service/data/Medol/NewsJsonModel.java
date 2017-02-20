package com.zhijia.service.data.Medol;

import java.util.List;

/**
 * 动态新闻
 */
public class NewsJsonModel {

    private String total ;

    private List<DynamicJsonModel> dynamic ;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<DynamicJsonModel> getDynamic() {
        return dynamic;
    }

    public void setDynamic(List<DynamicJsonModel> dynamic) {
        this.dynamic = dynamic;
    }

    public static class DynamicJsonModel{

        private String url ;

        private String title ;

        private String published ;

        public String getPublished() {
            return published;
        }

        public void setPublished(String published) {
            this.published = published;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
