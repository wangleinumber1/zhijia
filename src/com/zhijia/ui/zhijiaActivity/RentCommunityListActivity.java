package com.zhijia.ui.zhijiaActivity;

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
import com.zhijia.service.data.Medol.RentHouseJsonModel;
import com.zhijia.service.network.HttpClientUtils;
import com.zhijia.ui.R;
import com.zhijia.ui.list.adapter.ItemAdapter;
import com.zhijia.ui.list.interfaces.IHouseDetails;
import com.zhijia.ui.list.interfaces.IListDataNetWork;
import com.zhijia.ui.list.interfaces.ILoadData;
import com.zhijia.ui.list.page.Page;
import com.zhijia.util.DefaultDataUtils;
import com.zhijia.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class RentCommunityListActivity extends BaseHouseConditionActivity implements IListDataNetWork<RentHouseJsonModel>,ILoadData,IHouseDetails {

    private final String[] texts = new String[]{"面积", "租金", "户型", "排序"};

    private final int[] areas = new int[]{R.id.house_area_one, R.id.house_area_two, R.id.house_area_three, R.id.house_area_four};

    private final String RENT_COMMUNITY_CONDITION_URL = Global.API_WEB_URL + "/community/rentfield";

    private final String RENT_COMMUNITY_CONDITION_LIST_URL = Global.API_WEB_URL + "/community/rent";

    private String cid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rent_community_list);
        cid = getIntent().getStringExtra("cid")  ;

        this.setConditionNetwork(this);
        this.setListDataNetWork(this);
        this.setLoadData(this);
        this.setHouseDetails(this);
        //注册事件绑定
        findViewById(R.id.house_back).setOnClickListener(new BaseHouseActivity.HouseOnClickListener());
        BaseModel baseModel = new BaseModel();
        baseModel.setListViewId(R.id.rent_community_list);
        baseModel.setTexts(texts);
        baseModel.setHouseTopViewId(R.id.house_top_app_gps);
        baseModel.setHouseTitleId(R.string.rent_community_title);
        baseModel.setAreas(areas);
        baseModel.setConditionURL(RENT_COMMUNITY_CONDITION_URL);
        baseModel.setItemLayoutInflaterId(R.layout.adapter_house_item_two);
        //this.setHouseDetails(this);
        this.setAdapterData(baseModel);
        this.bindEventArea(areas, relativeId, ListType.RENT_LOOK_HOUSE);
    }

    @Override
    public JsonResult<List<RentHouseJsonModel>> getListDataByNetWork(String url, BaseModel baseModel) {
        HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
        Map<String, String> map = new HashMap<String, String>();
        if (baseModel.getSearchModel() != null) {
            map = baseModel.getSearchModel().toMap();
        }
        map.put("cityid", Global.NOW_CITY_ID);
        map.put("cid", cid);
        if (baseModel.getPage() != null) {
            map.put("page", String.valueOf(baseModel.getPage().getPage()));
        } else {
            map.put("page", "1");
        }
        Optional<JsonResult<List<RentHouseJsonModel>>> optional = httpClientUtils.getUnsignedListByData(url, map, RentHouseJsonModel.class);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    @Override
    public void startListTask(BaseModel baseModel) {
        RentCommunityListAsyncTask rentCommunityListAsyncTask = new RentCommunityListAsyncTask() ;
        rentCommunityListAsyncTask.execute(baseModel) ;
    }

    @Override
    public void setNetWorkErrorValue(BaseModel baseModel) {
        List<Map<Integer, Object>> resultList = new ArrayList<Map<Integer, Object>>();
        DefaultDataUtils.setDefaultHint(resultList, Global.ERROR_NET_WORK);
        baseModel.setListData(resultList);
        ListView listView = (ListView) findViewById(baseModel.getListViewId());
        ItemAdapter itemAdapter = new ItemAdapter(RentCommunityListActivity.this, baseModel.getItemLayoutInflaterId(), baseModel.getListData());
        setItemAdapter(itemAdapter);
        listView.setAdapter(itemAdapter);
        Toast.makeText(RentCommunityListActivity.this, Global.ERROR_NET_WORK, Toast.LENGTH_SHORT).show();
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
            //设置下页面作为当前页
            baseModel.getPage().setPage(nextPage);
            RentCommunityListPageAsyncTask pageAsyncTask = new RentCommunityListPageAsyncTask();
            pageAsyncTask.execute(baseModel);

        }

        //刨除去第一页因为上来已经加载过了
        if (nowPage == totalPage && nowPage != 1) {
            RentCommunityListPageAsyncTask pageAsyncTask = new RentCommunityListPageAsyncTask();
            pageAsyncTask.execute(baseModel);

        }
    }

    @Override
    public void showDetail(final Context packageContext, ListView listView) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (parent.getId()) {
                    case R.id.rent_community_list:
                        ListView listView1 = (ListView) parent;
                        Log.d("showDetail->Old", listView1.getCount() + "");
                        ListAdapter listAdapter = listView1.getAdapter();
                        ItemAdapter itemAdapter = null;
                        //由于在ListView里添加了addHeader 所以你获取的时候就有HeaderViewListAdapter 然后通过它在获取。
                        if (listAdapter instanceof HeaderViewListAdapter) {
                            itemAdapter = (ItemAdapter) (((HeaderViewListAdapter) listAdapter).getWrappedAdapter());
                        } else {
                            itemAdapter = (ItemAdapter) listAdapter;
                        }
                        Object object = itemAdapter.getItem(position);
                        String parameter = "";
                        if (object != null) {
                            Map<Integer, Object> map = (Map<Integer, Object>) object;
                            Log.d("showDetail->map->id", map.get(-1) + "::" + "position==" + position);
                            parameter = (String) map.get(-1);
                        } else {
                            Log.d("showDetail->map->position", position + "");
                        }
                        Intent rentHouseAreaDetails = new Intent(packageContext, OldHouseDetailsActivity.class);
                        rentHouseAreaDetails.putExtra("hid", parameter);
                        rentHouseAreaDetails.putExtra("housetype","2");
                        rentHouseAreaDetails.putExtra("identity","2") ;
                        startActivity(rentHouseAreaDetails);
                        break;
                }
            }
        });
    }


    public class RentCommunityListAsyncTask extends AsyncTask<BaseModel, Void, JsonResult<List<RentHouseJsonModel>>>{

        private BaseModel baseModel;

        @Override
        protected JsonResult<List<RentHouseJsonModel>> doInBackground(BaseModel... params) {
            baseModel = params[0];
            if (baseModel.getPage() != null) {
                baseModel.getPage().setPage(1);
            }
            return getListDataByNetWork(RENT_COMMUNITY_CONDITION_LIST_URL,baseModel);
        }

        @Override
        protected void onPostExecute(JsonResult<List<RentHouseJsonModel>> jsonResult) {
            if (jsonResult != null && jsonResult.isStatus()) {
                Page page = new Page(Integer.valueOf(jsonResult.getTotal()), Global.PAGE_SIZE);
                baseModel.setPage(page);
                ((TextView) findViewById(R.id.community_house_count)).setText("共有" + jsonResult.getTotal() + "套租房");
                List<RentHouseJsonModel> list = jsonResult.getData();
                List<Map<Integer, Object>> resultList = new ArrayList<Map<Integer, Object>>();
                setListData(list, resultList, null);
                DefaultDataUtils.setDefaultHint(resultList, Global.NOT_FIND_DATA);
                baseModel.setListData(resultList);
                ListView listView = (ListView) findViewById(baseModel.getListViewId());
                ItemAdapter itemAdapter = new ItemAdapter(RentCommunityListActivity.this, baseModel.getItemLayoutInflaterId(), baseModel.getListData());
                setItemAdapter(itemAdapter);
                if (listView.getFooterViewsCount() == 0 && itemAdapter.getCount() >9) {
                    listView.addFooterView(footerView);
                }
                listView.setAdapter(itemAdapter);
            } else if(jsonResult==null){
                setNetWorkErrorValue(baseModel);
            }
        }
    }

    private void setListData(List<RentHouseJsonModel> list, List<Map<Integer, Object>> resultList, ItemAdapter itemAdapter){

        for (RentHouseJsonModel jsonModel : list) {
            Map<Integer, Object> map = new HashMap<Integer, Object>();
            map.put(R.id.adapter_house_item_two_image, jsonModel.getTitlepic());
            map.put(-1, jsonModel.getHid());
            String tag = jsonModel.getTag();
            if (tag != null) {
                if (tag.equals("1")) {
                    map.put(R.id.adapter_house_item_two_top_mark, R.drawable.house_icons_1 + ":VISIBLE");
                } else if (tag.equals("2")) {
                    map.put(R.id.adapter_house_item_two_top_mark, R.drawable.house_icons_5 + ":VISIBLE");
                } else if (tag.equals("3")) {
                    map.put(R.id.adapter_house_item_two_top_mark, R.drawable.house_icons_7 + ":VISIBLE");
                }
            }
            String usertype = jsonModel.getUsertype();
            if (usertype != null) {
                if (usertype.equals("1")) {
                    map.put(R.id.adapter_house_item_two_bottom, R.drawable.singler + ":VISIBLE");
                }
            }
            map.put(R.id.adapter_house_item_two_name, StringUtils.replace(jsonModel.getTitle()));
            map.put(R.id.adapter_house_item_two_area, jsonModel.getAreaname() + " " + jsonModel.getCommunityname());
            map.put(R.id.adapter_house_item_two_type, jsonModel.getUnit() + " " + jsonModel.getArea());
            List<String> tagtextList = jsonModel.getTagtext();
            if (tagtextList != null && tagtextList.size() > 0) {
                switch (tagtextList.size()) {
                    case 1:
                        //由于tagtext给的是一个空字符串，所以这块要判断一下
                        if (!tagtextList.get(0).equals("")) {
                            map.put(R.id.adapter_house_item_two_des, tagtextList.get(0) + ":VISIBLE");
                        }
                        break;
                    case 2:
                        map.put(R.id.adapter_house_item_two_des, tagtextList.get(0) + ":VISIBLE");
                        map.put(R.id.adapter_house_item_two_des_two, tagtextList.get(1) + ":VISIBLE");
                        break;
                    case 3:
                        map.put(R.id.adapter_house_item_two_des, tagtextList.get(0) + ":VISIBLE");
                        map.put(R.id.adapter_house_item_two_des_two, tagtextList.get(1) + ":VISIBLE");
                        map.put(R.id.adapter_house_item_two_des_three, tagtextList.get(2) + ":VISIBLE");
                        break;
                    case 4:
                        map.put(R.id.adapter_house_item_two_des, tagtextList.get(0) + ":VISIBLE");
                        map.put(R.id.adapter_house_item_two_des_two, tagtextList.get(1) + ":VISIBLE");
                        map.put(R.id.adapter_house_item_two_des_three, tagtextList.get(2) + ":VISIBLE");
                        map.put(R.id.adapter_house_item_two_des_four, tagtextList.get(3) + ":VISIBLE");
                        break;
                }
            }
            map.put(R.id.adapter_house_item_two_price, jsonModel.getPrice());
            if (resultList != null) {
                resultList.add(map);
            }
            if (itemAdapter != null) {
                itemAdapter.addItem(map);
                //一定要做好通知不要会出现java.lang.IllegalStateException: The content of the adapter has changed but ListView did not receive
                itemAdapter.notifyDataSetChanged();
            }
        }
    }

    public class RentCommunityListPageAsyncTask extends AsyncTask<BaseModel, Void, JsonResult<List<RentHouseJsonModel>>>{

        private BaseModel baseModel;

        @Override
        protected JsonResult<List<RentHouseJsonModel>> doInBackground(BaseModel... params) {
            baseModel = params[0];
            return getListDataByNetWork(RENT_COMMUNITY_CONDITION_LIST_URL,baseModel);
        }

        @Override
        protected void onPostExecute(JsonResult<List<RentHouseJsonModel>> jsonResult) {
            if (jsonResult != null && jsonResult.isStatus()) {
                List<RentHouseJsonModel> list = jsonResult.getData();
                setListData(list, null, getItemAdapter());
                //刨除去最后一页重复加载
                if (baseModel.getPage().getPage() == baseModel.getPage().getTotalPage()) {
                    baseModel.getPage().setPage(baseModel.getPage().getTotalPage() + 1);
                    Log.d("new nowPage", baseModel.getPage().getPage() + "");
                }
            } else if (jsonResult == null) {
                setNetWorkErrorValue(baseModel);
            }
        }
    }
}