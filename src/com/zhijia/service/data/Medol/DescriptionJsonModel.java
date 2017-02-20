package com.zhijia.service.data.Medol;

import java.util.List;

/**
 * 房源描述
 */
public class DescriptionJsonModel {

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public List<String> getAssort() {
        return assort;
    }

    public void setAssort(List<String> assort) {
        this.assort = assort;
    }

    private  String info ;//房源描述

    private List<String> assort ;
}
