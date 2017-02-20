package com.zhijia.ui.zhijiaActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.google.common.base.Optional;
import com.zhijia.Global;
import com.zhijia.service.data.Medol.JsonResult;
import com.zhijia.service.data.Medol.MyBrowseOldHouseJsonModel;
import com.zhijia.service.network.HttpClientUtils;
import com.zhijia.ui.R;
import com.zhijia.ui.list.adapter.ItemAdapter;
import com.zhijia.util.HouseDBUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我浏览的二手房列表
 */
public class MyBrowseOldHouseActivity extends MyBaseHouseActivity<MyBrowseOldHouseJsonModel>{


   private final String  BROWSE_OLD_HOUSE_URL = Global.API_WEB_URL+"/house/history" ;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("登陆标识:"+Global.USER_AUTH_STR);
        setContentView(R.layout.my_common_house_list);
        setCountStr(R.string.total_browse_old_house_count);
        setCountId(R.id.total_count_desc);
        ListView listView = (ListView) findViewById(R.id.my_common_house_list);
        Map<String, String> tempMap = new HashMap<String, String>();
        tempMap.put("cityid", Global.NOW_CITY_ID);
        tempMap.put("housetype","1") ;
        tempMap.put("hids", HouseDBUtil.getBrowseHistoryIds(HouseDBUtil.HouseType.OLD_HOUSE)) ;
        initView(listView, R.layout.my_browse_old_house_list_item, tempMap);
        Map<Integer, String> titleMap = new HashMap<Integer, String>();
        titleMap.put(R.id.title, getString(R.string.my_browse_old_house));
        setTitle(titleMap, R.id.back);
        startListTask(tempMap);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView1 = (ListView) parent;
                ListAdapter listAdapter = listView1.getAdapter();
                ItemAdapter tempAdapter = null;
                Log.d("bindEvent->", position + "");

                if (listAdapter instanceof HeaderViewListAdapter) {
                    tempAdapter = (ItemAdapter) (((HeaderViewListAdapter) listAdapter).getWrappedAdapter());
                } else {
                    tempAdapter = (ItemAdapter) listAdapter;
                }
                Map<Integer,Object> tempMap= (Map<Integer,Object>) tempAdapter.getItem(position);
                Intent intent  = new Intent(MyBrowseOldHouseActivity.this, OldHouseDetailsActivity.class) ;
                intent.putExtra("hid",(String)tempMap.get(-1));
                intent.putExtra("housetype","1") ;
                intent.putExtra("identity","1") ;
                startActivity(intent);
            }
        });
    }

    @Override
    public void convertToAdapterList(List<MyBrowseOldHouseJsonModel> list, List<Map<Integer, Object>> resultList, ItemAdapter itemAdapter) {

        for(MyBrowseOldHouseJsonModel jsonModel:list){
            Map<Integer, Object> map = new HashMap<Integer, Object>();
            map.put(-1,jsonModel.getHid()) ;
            map.put(R.id.adapter_house_image_one, jsonModel.getTitlePic());
            map.put(R.id.adapter_house_content_one, jsonModel.getCommunityName());
            map.put(R.id.adapter_house_content_two, jsonModel.getPrice());
            map.put(R.id.adapter_house_content_three, jsonModel.getAreaName());
            map.put(R.id.adapter_house_content_four, jsonModel.getAddress());
            map.put(R.id.adapter_house_content_five, HouseDBUtil.getBrowseDateTime(jsonModel.getHid(), HouseDBUtil.HouseType.OLD_HOUSE));
            map.put(R.id.item_action, "收藏");
            if (resultList != null) {
                resultList.add(map);
            }
            if (itemAdapter != null) {
                itemAdapter.addItem(map);
                itemAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public JsonResult<List<MyBrowseOldHouseJsonModel>> getListDataByNetWork(Map<String, String> paramMap) {
        HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
        if (getPage() != null) {
            paramMap.put("page", String.valueOf(getPage().getPage()));
        } else {
            paramMap.put("page", "1");
        }
        Optional<JsonResult<List<MyBrowseOldHouseJsonModel>>> optional = httpClientUtils.getUnsignedListByData(BROWSE_OLD_HOUSE_URL, paramMap, MyBrowseOldHouseJsonModel.class);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }
}