package com.zhijia.service.data.Medol;

/**
 * 公共JSON类
 */
public abstract  class CommonJsonModel {

    private String fid ;

    private String name ;

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}