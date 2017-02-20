package com.zhijia.ui.list.interfaces;

import com.zhijia.service.data.Medol.ConditionJsonModel;
import com.zhijia.service.data.Medol.JsonResult;

/**
 * 有条件的 需要实现这个接口
 */
public interface IConditionNetwork {

    public JsonResult<ConditionJsonModel> getConditionData(String url) ;
}
