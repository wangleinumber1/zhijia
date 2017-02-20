package com.zhijia.ui;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zhijia.service.data.Medol.ListJsonModel;

import java.util.List;

/**
 *
 */
public class SlideJsonModel {

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
}
