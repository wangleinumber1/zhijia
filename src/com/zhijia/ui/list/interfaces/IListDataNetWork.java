package com.zhijia.ui.list.interfaces;

import com.zhijia.service.data.Medol.BaseModel;
import com.zhijia.service.data.Medol.JsonResult;

import java.util.List;

/**
 * 网络列表接口
 */
public interface IListDataNetWork<T> {

    public JsonResult<List<T>> getListDataByNetWork(String url, BaseModel baseModel);

    public void startListTask(BaseModel baseModel);

    public void setNetWorkErrorValue(BaseModel baseModel);
}
