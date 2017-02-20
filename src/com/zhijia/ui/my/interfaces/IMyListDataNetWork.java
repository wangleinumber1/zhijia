package com.zhijia.ui.my.interfaces;

import com.zhijia.service.data.Medol.JsonResult;

import java.util.List;
import java.util.Map;

/**
 * 根据网络获取列表数据
 */
public interface IMyListDataNetWork<T> {

    /**
     *
     *
     * @param paramMap  请求后面的参数。
     *
     * @return
     */
    public JsonResult<List<T>> getListDataByNetWork(Map<String,String> paramMap) ;

    public void startListTask(Map<String,String> paramMap) ;

    public void setNetWorkErrorValue() ;
}
