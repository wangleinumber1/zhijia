package com.zhijia.ui.zhijiaActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.google.common.base.Optional;
import com.zhijia.Global;
import com.zhijia.Enum.*;
import com.zhijia.service.data.Medol.BaseModel;
import com.zhijia.service.data.Medol.JsonResult;
import com.zhijia.service.data.Medol.NewHouseJsonModel;
import com.zhijia.service.data.Medol.SearchModel;
import com.zhijia.service.network.HttpClientUtils;
import com.zhijia.ui.R;
import com.zhijia.ui.list.adapter.ItemAdapter;
import com.zhijia.ui.list.interfaces.IHouseDetails;
import com.zhijia.ui.list.interfaces.IListDataNetWork;
import com.zhijia.ui.list.interfaces.ILoadData;
import com.zhijia.ui.list.page.Page;
import com.zhijia.util.BaiduMapUtil;
import com.zhijia.util.DefaultDataUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 新房首页
 */
public class NewHouseActivity extends BaseHouseConditionActivity
implements IListDataNetWork<NewHouseJsonModel>, ILoadData, IHouseDetails {

    private final String LIST_URL = Global.API_WEB_URL + "/xinfang/search";
    private final String CONDITION_URL = Global.API_WEB_URL + "/xinfang/field";
    private OnClickListener onClickListener = new OnClickListener();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_house);
        findViewById(R.id.house_back).setOnClickListener(new HouseOnClickListener());
        //必须设置
        this.setConditionNetwork(this);
        this.setLoadData(this);
        this.setListDataNetWork(this);
        this.setHouseDetails(this);
        BaseModel baseModel = new BaseModel(R.layout.common_adapter_house_centre, R.id.house_centre, ListType.NEW);
        baseModel.setTextViewId(R.id.house_count);
        baseModel.setCount("0");
        baseModel.setCountStr(NEW_COUNT);
        baseModel.setHouseTopViewId(R.id.house_top_app_gps);
        baseModel.setHouseTitleId(R.string.new_house_title);
        baseModel.setItemLayoutInflaterId(R.layout.adapter_house_item);
        //baseModel.setListData(null);
        //这个必须设置
        baseModel.setConditionURL(CONDITION_URL);
        SearchModel newSearchModel = (SearchModel)getIntent().getSerializableExtra("searchModel") ;
        if(newSearchModel !=null){
            baseModel.setSearchModel(newSearchModel);
        }
        setAdapterData(baseModel);
        bindEventArea(linearIds,areaIds, relativeId, ListType.NEW);
        bindEventArea(goneLinearIds,goneAreaIds, goneRelativeId, ListType.NEW);
        findViewById(R.id.house_image_one).setOnClickListener(onClickListener);
        findViewById(R.id.house_image_two).setOnClickListener(onClickListener);
        findViewById(R.id.house_image_three).setOnClickListener(onClickListener);
        findViewById(R.id.house_image_four).setOnClickListener(onClickListener);
        findViewById(R.id.new_house_map).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent newHouseMapIntent = new Intent(NewHouseActivity.this.getApplicationContext(), NewHouseMapActivity.class);
                startActivity(newHouseMapIntent);
                finish();
            }
        });
    }

    @Override
    public void loadData(BaseModel baseModel) {
        int totalPage = baseModel.getPage().getTotalPage();
        int nextPage = baseModel.getPage().getNextPage();
        int nowPage = baseModel.getPage().getPage();
        Log.d("BaseModel page", totalPage + "");
        Log.d("BaseModel page", nextPage + "");
        Log.d("BaseModel page", nowPage + "");

        if (nowPage < totalPage) {
            //设置当前页要加载的页数
            baseModel.getPage().setPage(nextPage);
            NewHousePageAsyncTask pageAsyncTask = new NewHousePageAsyncTask();
            pageAsyncTask.execute(baseModel);
        }

        if (nowPage == totalPage && nowPage != 1) {
            NewHousePageAsyncTask pageAsyncTask = new NewHousePageAsyncTask();
            pageAsyncTask.execute(baseModel);
        }

    }

    @Override
    public JsonResult<List<NewHouseJsonModel>> getListDataByNetWork(String url, BaseModel baseModel) {
        HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
        Map<String, String> map = new HashMap<String, String>();
        if (baseModel.getSearchModel() != null) {
            map = baseModel.getSearchModel().toMap();
        }
        map.put("cityid", Global.NOW_CITY_ID);
        if (baseModel.getPage() != null) {
            map.put("page", String.valueOf(baseModel.getPage().getPage()));
        } else {
            map.put("page", "1");
        }
        Optional<JsonResult<List<NewHouseJsonModel>>> optional = httpClientUtils
        		.getUnsignedListByData(url, map, NewHouseJsonModel.class);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    @Override
    public void startListTask(BaseModel baseModel) {
        NewHouseAsyncTask task = new NewHouseAsyncTask();
        task.execute(baseModel);
    }

    @Override
    public void setNetWorkErrorValue(BaseModel baseModel) {
        setCommonHouseCount(baseModel.getTextViewId(), "0",NEW_COUNT);
        setCommonHouseCount(R.id.gone_house_count, "0",NEW_COUNT);
        List<Map<Integer, Object>> resultList = new ArrayList<Map<Integer, Object>>();
        DefaultDataUtils.setDefaultHint(resultList, Global.ERROR_NET_WORK);
        baseModel.setListData(resultList);
        ListView listView = (ListView) findViewById(baseModel.getListViewId());
        ItemAdapter itemAdapter = new ItemAdapter(NewHouseActivity.this, baseModel.getItemLayoutInflaterId(), baseModel.getListData());
        setItemAdapter(itemAdapter);
        listView.setAdapter(itemAdapter);
        Toast.makeText(NewHouseActivity.this, Global.ERROR_NET_WORK, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showDetail(final Context packageContext, ListView listView) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (parent.getId()) {
                    case R.id.house_listView:
                        ListView listView1 = (ListView) parent;
                        Log.d("showDetail->new", listView1.getCount() + "");
                        ListAdapter listAdapter = listView1.getAdapter();
                        ItemAdapter itemAdapter = null;
                        //由于在ListView里添加了addHeader 所以你获取的时候就有HeaderViewListAdapter 然后通过它在获取。
                        if (listAdapter instanceof HeaderViewListAdapter) {
                            itemAdapter = (ItemAdapter) (((HeaderViewListAdapter) listAdapter).getWrappedAdapter());
                        } else {
                            itemAdapter = (ItemAdapter) listAdapter;
                        }
                        Object object = itemAdapter.getItem(position - 1);
                        String parameter = "";
                        if (object != null) {
                            @SuppressWarnings("unchecked")
							Map<Integer, Object> map = (Map<Integer, Object>) object;
                            Log.d("showDetail->map->id", map.get(-1) + "::" + "position==" + position);
                            parameter = (String) map.get(-1);
                        } else {
                            Log.d("showDetail->map->position", position + "");
                        }
                        Intent newHouseDetails = new Intent(packageContext, NewHouseDetailsActivity.class);
                        newHouseDetails.putExtra("hid", parameter);
                        startActivity(newHouseDetails);
                        break;
                }

            }
        });
    }

    /**
     * 处理分页的
     */
    public class NewHousePageAsyncTask extends AsyncTask<BaseModel, Void, JsonResult<List<NewHouseJsonModel>>> {
        private BaseModel baseModel;

        @Override
        protected JsonResult<List<NewHouseJsonModel>> doInBackground(BaseModel... params) {
            baseModel = params[0];
            return getListDataByNetWork(LIST_URL, baseModel);
        }

        @SuppressLint("UseSparseArrays")
		@Override
        protected void onPostExecute(JsonResult<List<NewHouseJsonModel>> jsonResult) {

            if (jsonResult != null && jsonResult.isStatus()) {
                List<NewHouseJsonModel> list = jsonResult.getData();
                for (NewHouseJsonModel jsonModel : list) {
                    Map<Integer, Object> map = new HashMap<Integer, Object>();
                    setData(map,jsonModel);
//                    map.put(-1, jsonModel.getHid());
//                    map.put(R.id.adapter_image, jsonModel.getTitlepic());
//                    map.put(R.id.adapter_house_name, jsonModel.getName());
//                    String laticoor = jsonModel.getLaticoor();
//                    String longcoor = jsonModel.getLongcoor();
//                    Log.d("NewHouse->laticoor->longcoor", laticoor + ":" + longcoor);
//                    if ((laticoor != null && !laticoor.isEmpty()) && (longcoor != null && !longcoor.isEmpty()) && (!Global.NOW_CITY_LATICOOR.isEmpty()) && (!Global.NOW_CITY_LONGCOOR.isEmpty())) {
//                        double distance = BaiduMapUtil.getDistance(Double.parseDouble(Global.NOW_CITY_LATICOOR), Double.parseDouble(Global.NOW_CITY_LONGCOOR), Double.parseDouble(jsonModel.getLaticoor()), Double.parseDouble(jsonModel.getLongcoor()));
//                        Log.d("NewHouse->distance", distance + "");
//                        map.put(R.id.adapter_house_distance, String.valueOf(distance) + "KM");
//                    } else {
//                        map.put(R.id.adapter_house_distance, "暂无");
//                    }
//
//                    map.put(R.id.adapter_house_area_name, jsonModel.getAreaname());
//                    map.put(R.id.adapter_house_money, jsonModel.getAverageprice());
//                    map.put(R.id.adapter_house_address, jsonModel.getAddress());
//                    map.put(R.id.adapter_house_des, jsonModel.getPriceexplain());
                    getItemAdapter().addItem(map);
                    getItemAdapter().notifyDataSetChanged();
                }
                if (baseModel.getPage().getPage() == baseModel.getPage().getTotalPage()) {
                    baseModel.getPage().setPage(baseModel.getPage().getTotalPage() + 1);
                    Log.d("new nowPage", baseModel.getPage().getPage() + "");
                }
            } else if (jsonResult == null) {
                setNetWorkErrorValue(baseModel);
            }
        }
    }


    /**
     * 搜索的
     */
    public class NewHouseAsyncTask extends AsyncTask<BaseModel, Void, JsonResult<List<NewHouseJsonModel>>> {

        private BaseModel baseModel;

        @Override
        protected JsonResult<List<NewHouseJsonModel>> doInBackground(BaseModel... params) {
            baseModel = params[0];
            if (baseModel.getPage() != null) {
                baseModel.getPage().setPage(1);
            }
            return getListDataByNetWork(LIST_URL, baseModel);
        }

        @SuppressLint("UseSparseArrays")
		@Override
        protected void onPostExecute(JsonResult<List<NewHouseJsonModel>> jsonResult) {

            if (jsonResult != null && jsonResult.isStatus()) {
                Page page = new Page(Integer.valueOf(jsonResult.getTotal()), Global.PAGE_SIZE);
                baseModel.setPage(page);
                setCommonHouseCount(baseModel.getTextViewId(), jsonResult.getTotal(),NEW_COUNT);
                setCommonHouseCount(R.id.gone_house_count, jsonResult.getTotal(),NEW_COUNT);
                List<NewHouseJsonModel> list = jsonResult.getData();
                List<Map<Integer, Object>> resultList = new ArrayList<Map<Integer, Object>>();
                for (NewHouseJsonModel jsonModel : list) {
                    Map<Integer, Object> map = new HashMap<Integer, Object>();
                    setData(map,jsonModel);
                    resultList.add(map);
                }
                Log.d("NewHouse->Search->resultList", resultList.size() + "");
                //如果是0表示没有数据，这块就要提示一下用户木有数据
                DefaultDataUtils.setDefaultHint(resultList, Global.NOT_FIND_DATA);
                baseModel.setListData(resultList);
                ListView listView = (ListView) findViewById(baseModel.getListViewId());
                ItemAdapter itemAdapter = new ItemAdapter(NewHouseActivity.this, 
                baseModel.getItemLayoutInflaterId(), baseModel.getListData());
                setItemAdapter(itemAdapter);
                if (listView.getFooterViewsCount() == 0) {
                    listView.addFooterView(footerView);
                }
                listView.setAdapter(itemAdapter);
            } else if (jsonResult == null) {
                setNetWorkErrorValue(baseModel);
            }
        }
    }


    public void setData( Map<Integer, Object> map,NewHouseJsonModel jsonModel){
        map.put(-1, jsonModel.getHid());
        map.put(R.id.adapter_image, jsonModel.getTitlepic());
        String salesstatus  = jsonModel.getSalesstatus()  ;
        if(salesstatus != null && !salesstatus.isEmpty()) {
            if(salesstatus.equals("1")){//
                map.put(R.id.adapter_image_top_mark, R.drawable.house_icons_11 + ":VISIBLE");
            }
            if(salesstatus.equals("2")){
                map.put(R.id.adapter_image_top_mark, R.drawable.house_icons_10 + ":VISIBLE");
            }
            if(salesstatus.equals("3")){
                map.put(R.id.adapter_image_top_mark, R.drawable.house_icons_9 + ":VISIBLE");
            }
        }
        map.put(R.id.adapter_house_name, jsonModel.getName());
        String laticoor = jsonModel.getLaticoor();
        String longcoor = jsonModel.getLongcoor();
        Log.d("NewHouse->laticoor->longcoor", laticoor + ":" + longcoor);
        if ( (laticoor != null && !laticoor.isEmpty()) && 
        		(longcoor != null && !longcoor.isEmpty()) && 
        		(!Global.NOW_CITY_LATICOOR.isEmpty()) && 
        		(!Global.NOW_CITY_LONGCOOR.isEmpty())) {
            double distance = BaiduMapUtil.getDistance(Double.parseDouble(Global.NOW_CITY_LATICOOR), 
            		Double.parseDouble(Global.NOW_CITY_LONGCOOR),
            		Double.parseDouble(jsonModel.getLaticoor()),
            		Double.parseDouble(jsonModel.getLongcoor()));
            Log.d("NewHouse->distance", distance + "");
            map.put(R.id.adapter_house_distance, String.valueOf(distance) + "KM");
        } else {
            map.put(R.id.adapter_house_distance, "暂无");
        }
        map.put(R.id.adapter_house_area_name, jsonModel.getAreaname());
        map.put(R.id.adapter_house_money, jsonModel.getAverageprice());
        map.put(R.id.adapter_house_address, jsonModel.getAddress());
        map.put(R.id.adapter_house_des, jsonModel.getPriceexplain());
    }

    /**
     * 所有返回按钮的监听器
     */
    public class OnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.house_image_one://限时特惠
                    Intent housePrivilegeIntent = new Intent(NewHouseActivity.this, HouseCommonListActivity.class);
                    housePrivilegeIntent.putExtra("listType", "Privilege");
                    startActivity(housePrivilegeIntent);
                    break;
                case R.id.house_image_two://热点楼盘
                    Intent hotHouseIntent = new Intent(NewHouseActivity.this, HouseCommonListActivity.class);
                    hotHouseIntent.putExtra("listType", "HotHouse");
                    startActivity(hotHouseIntent);
                    break;
                case R.id.house_image_three://看房团
                    Intent condoTourListIntent = new Intent(NewHouseActivity.this, CondoTourListActivity.class);
                    startActivity(condoTourListIntent);
                    break;
                case R.id.house_image_four://近期开盘
                    Intent recentOpenHouseIntent = new Intent(NewHouseActivity.this, HouseCommonListActivity.class);
                    recentOpenHouseIntent.putExtra("listType", "RecentOpenHouse");
                    startActivity(recentOpenHouseIntent);
                    break;
            }
        }
    }
}
