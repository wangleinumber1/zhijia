package com.zhijia.service.data.Medol;

/**
 * 搜索条件的数据模型
 */
public class SearchHistoryBaseModel {

    private String keyword = "";

    public String toShortString() {
        String returnStr = getKeyword();
        return returnStr.trim();
    }

    @Override
    public String toString() {
        return "keyword='" + keyword + '\'';
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

}
