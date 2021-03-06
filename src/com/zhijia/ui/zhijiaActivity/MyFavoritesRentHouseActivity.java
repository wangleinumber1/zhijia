package com.zhijia.ui.zhijiaActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.google.common.base.Optional;
import com.zhijia.Global;
import com.zhijia.service.data.Medol.BaseModel;
import com.zhijia.service.data.Medol.JsonResult;
import com.zhijia.service.data.Medol.MyFavoritesOldHouseJsonModel;
import com.zhijia.service.network.HttpClientUtils;
import com.zhijia.ui.R;
import com.zhijia.ui.list.adapter.ItemAdapter;
import com.zhijia.ui.list.interfaces.ILoadData;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我收藏的租房
 */
public class MyFavoritesRentHouseActivity  extends MyBaseHouseActivity<MyFavoritesOldHouseJsonModel>{


    private final String FAVORITES_RENT_HOUSE_URL = Global.API_WEB_URL+"/member/collection" ;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_common_house_list);

        //设置总数的
        setCountStr(R.string.total_favorites_rent_house_count);
        setCountId(R.id.total_count_desc);
        setWeek(new WeakReference<Activity>(MyFavoritesRentHouseActivity.this));
        ListView listView = (ListView) findViewById(R.id.my_common_house_list);
        Map<String, String> tempMap = new HashMap<String, String>();
        tempMap.put("cityid", Global.NOW_CITY_ID);
        tempMap.put("identity","2") ;
        //初始化列表，布局 和条件
        initView(listView, R.layout.my_favorites_rent_house_list_item, tempMap);
        Map<Integer, String> titleMap = new HashMap<Integer, String>();
        titleMap.put(R.id.title, getString(R.string.my_favorites_rent_house));
        setTitle(titleMap, R.id.back);
        setLogin(MyFavoritesRentHouseActivity.this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView1 = (ListView) parent;
                ListAdapter listAdapter = listView1.getAdapter();
                ItemAdapter tempAdapter = null;
                if (listAdapter instanceof HeaderViewListAdapter) {
                    tempAdapter = (ItemAdapter) (((HeaderViewListAdapter) listAdapter).getWrappedAdapter());
                } else {
                    tempAdapter = (ItemAdapter) listAdapter;
                }
                Map<Integer,Object> tempMap= (Map<Integer,Object>) tempAdapter.getItem(position);
                Intent intent  = new Intent(MyFavoritesRentHouseActivity.this, OldHouseDetailsActivity.class) ;
                intent.putExtra("hid",(String)tempMap.get(-1));
                intent.putExtra("housetype","2") ;
                intent.putExtra("identity","2") ;
                startActivity(intent);
            }
        });
    }


    @Override
    public void convertToAdapterList(List<MyFavoritesOldHouseJsonModel> list, List<Map<Integer, Object>> resultList, ItemAdapter itemAdapter) {
        for(MyFavoritesOldHouseJsonModel jsonModel:list){
            Map<Integer, Object> map = new HashMap<Integer, Object>();
            map.put(-1,jsonModel.getHid()) ;
            map.put(R.id.adapter_house_image_one, jsonModel.getPicUrl());
            map.put(R.id.adapter_house_content_one, jsonModel.getCommunityName());
            map.put(R.id.adapter_house_content_two, jsonModel.getDayPrice());
            map.put(R.id.adapter_house_content_three, jsonModel.getAreaName()+" "+jsonModel.getAddress());
            map.put(R.id.adapter_house_content_four, jsonModel.getAveragePrice());
            map.put(R.id.adapter_house_content_five, jsonModel.getArea()+" "+jsonModel.getUnit());
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
    public JsonResult<List<MyFavoritesOldHouseJsonModel>> getListDataByNetWork(Map<String, String> paramMap) {
        HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
        if (getPage() != null) {
            paramMap.put("page", String.valueOf(getPage().getPage()));
        } else {
            paramMap.put("page", "1");
        }
        Optional<JsonResult<List<MyFavoritesOldHouseJsonModel>>> optional = httpClientUtils.getUnsignedListByData(FAVORITES_RENT_HOUSE_URL, paramMap, MyFavoritesOldHouseJsonModel.class);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }


}