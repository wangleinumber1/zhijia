package com.zhijia.service.data.Medol;

import java.util.List;

/**
 *
 */
public class CommunityAlbumJsonModel {

    private String total ;

    private List<CommunityAlbumDetailsJsonModel> list ;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<CommunityAlbumDetailsJsonModel> getList() {
        return list;
    }

    public void setList(List<CommunityAlbumDetailsJsonModel> list) {
        this.list = list;
    }
}
