package com.zhijia.service.data.Medol;

import java.util.List;

/**
 * 房子的幻灯片
 */
public class PicdataJsonModel {

    private String count ;

    private String tag ;

    private List<CommonPicDataJsonModel> list ;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<CommonPicDataJsonModel> getList() {
        return list;
    }

    public void setList(List<CommonPicDataJsonModel> list) {
        this.list = list;
    }
}
