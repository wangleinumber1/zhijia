package com.zhijia.service.data.Medol;

import java.util.List;

/**
 * 楼盘动态新的JSON
 */
public class HouseDynamicNewsJsonModel {

    private List<DynamicNewsJsonModel> dynamic ;

    private String total ;


    public List<DynamicNewsJsonModel> getDynamic() {
        return dynamic;
    }

    public void setDynamic(List<DynamicNewsJsonModel> dynamic) {
        this.dynamic = dynamic;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

}
