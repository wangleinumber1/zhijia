package com.zhijia.ui.list.interfaces;

import com.zhijia.service.data.Medol.JsonResult;

/**
 * 详细数据
 */
public interface IListDetailDataNetWork<T> {

    public JsonResult<T> getDetailDataByNetWork(String id) ;

    public void startListTask(String hid) ;
}
