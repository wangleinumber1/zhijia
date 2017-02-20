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
import com.zhijia.service.data.Medol.OldHouseJsonModel;
import com.zhijia.service.data.Medol.SearchModel;
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
 * 二手房
 */
public class OldHouseActivity extends BaseHouseConditionActivity implements IHouseDetails, ILoadData, IListDataNetWork<OldHouseJsonModel> {

    private final int[] drawableIds = new int[]{R.drawable.old_house_image_one, R.drawable.old_house_image_two, R.drawable.old_house_image_three, R.drawable.old_house_image_four};

    private final int[] areas = new int[]{R.id.house_area_one, R.id.house_area_two, R.id.house_area_three, R.id.house_area_four};

    private final int[] goneAreas = new int[]{R.id.gone_house_area_one, R.id.gone_house_area_two, R.id.gone_house_area_three, R.id.gone_house_area_four};

    private final String[] texts = new String[]{"区域", "总价", "面积", "更多"};

    private final String LIST_URL = Global.API_WEB_URL + "/house/search";

    private final String CONDITION_URL = Global.API_WEB_URL + "/house/field";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.old_house);
        findViewById(R.id.house_back).setOnClickListener(new HouseOnClickListener());

        this.setHouseDetails(this);
        this.setLoadData(this);
        this.setConditionNetwork(this);
        this.setListDataNetWork(this);


        BaseModel baseModel = new BaseModel(R.layout.common_adapter_house_centre, R.id.house_centre, ListType.OLD);
        baseModel.setTextViewId(R.id.house_count);
        baseModel.setCount("0");
        baseModel.setCountStr(OLD_RENT_COMMUNITY_COUNT);
        baseModel.setHouseTopViewId(R.id.house_top_app_gps);
        baseModel.setHouseTitleId(R.string.old_house_title);
        baseModel.setImageIds(imageIds);
        baseModel.setDrawableIds(drawableIds);
        baseModel.setAreas(areas);
        baseModel.setTexts(texts);
        baseModel.setGoneAreas(goneAreas);
        //这个必须设置
        baseModel.setConditionURL(CONDITION_URL);

        SearchModel oldSearchModel = (SearchModel)getIntent().getSerializableExtra("searchModel") ;
        if(oldSearchModel !=null){
            baseModel.setSearchModel(oldSearchModel);
        }

        baseModel.setItemLayoutInflaterId(R.layout.adapter_house_item_two);
        //baseModel.setListData(DefaultDataUtils.getListData(ListType.OLD));
        this.setAdapterData(baseModel);


        this.bindEventArea(linearIds,areas, relativeId, ListType.OLD);
        this.bindEventArea(goneLinearIds,goneAreas, goneRelativeId, ListType.OLD);

        this.findViewById(R.id.new_house_map).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent oldHouseMapIntent = new Intent(OldHouseActivity.this.getApplicationContext(), OldHouseMapActivity.class);
                startActivity(oldHouseMapIntent);
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
            //设置下页面作为当前页
            baseModel.getPage().setPage(nextPage);
            OldHousePageAsyncTask pageAsyncTask = new OldHousePageAsyncTask();
            pageAsyncTask.execute(baseModel);

        }

        //刨除去第一页因为上来已经加载过了
        if (nowPage == totalPage && nowPage != 1) {
            OldHousePageAsyncTask pageAsyncTask = new OldHousePageAsyncTask();
            pageAsyncTask.execute(baseModel);

        }
    }

    @Override
    public JsonResult<List<OldHouseJsonModel>> getListDataByNetWork(String url, BaseModel baseModel) {
        HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
        Map<String, String> map = new HashMap<String, String>();
        if (baseModel.getSearchModel() != null) {
            map = baseModel.getSearchModel().toMap();
        }
        map.put("cityid", Global.NOW_CITY_ID);
        map.put("housetype", "1");
        if (baseModel.getPage() != null) {
            map.put("page", String.valueOf(baseModel.getPage().getPage()));
        } else {
            map.put("page", "1");
        }
        Optional<JsonResult<List<OldHouseJsonModel>>> optional = httpClientUtils.getUnsignedListByData(url, map, OldHouseJsonModel.class);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    @Override
    public void startListTask(BaseModel baseModel) {
        OldHouseAsyncTask oldHouseAsyncTask = new OldHouseAsyncTask();
        oldHouseAsyncTask.execute(baseModel);
    }

    @Override
    public void setNetWorkErrorValue(BaseModel baseModel) {
        setCommonHouseCount(baseModel.getTextViewId(), "0",OLD_RENT_COMMUNITY_COUNT);
        setCommonHouseCount(R.id.gone_house_count, "0",OLD_RENT_COMMUNITY_COUNT);
        List<Map<Integer, Object>> resultList = new ArrayList<Map<Integer, Object>>();
        DefaultDataUtils.setDefaultHint(resultList, Global.ERROR_NET_WORK);
        baseModel.setListData(resultList);
        ListView listView = (ListView) findViewById(baseModel.getListViewId());
        ItemAdapter itemAdapter = new ItemAdapter(OldHouseActivity.this, baseModel.getItemLayoutInflaterId(), baseModel.getListData());
        setItemAdapter(itemAdapter);
        listView.setAdapter(itemAdapter);
        Toast.makeText(OldHouseActivity.this, Global.ERROR_NET_WORK, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showDetail(final Context packageContext, ListView listView) {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (parent.getId()) {
                    case R.id.house_listView:
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
                        Object object = itemAdapter.getItem(position - 1);
                        String parameter = "";
                        if (object != null) {
                            Map<Integer, Object> map = (Map<Integer, Object>) object;
                            Log.d("showDetail->map->id", map.get(-1) + "::" + "position==" + position);
                            parameter = (String) map.get(-1);
                        } else {
                            Log.d("showDetail->map->position", position + "");
                        }

                        Intent oldHouseDetails = new Intent(packageContext, OldHouseDetailsActivity.class);
//                        TextView textViewArea = (TextView) view.findViewById(R.id.adapter_house_item_two_area);
//                        oldHouseDetails.putExtra("adapter_house_item_two_area", textViewArea.getText().toString());
                        oldHouseDetails.putExtra("hid", parameter);
                        oldHouseDetails.putExtra("housetype","1") ;
                        oldHouseDetails.putExtra("identity","1") ;
                        startActivity(oldHouseDetails);
                        break;
                }

            }
        });
    }

    private void setListData(List<OldHouseJsonModel> list, List<Map<Integer, Object>> resultList, ItemAdapter itemAdapter) {
        for (OldHouseJsonModel jsonModel : list) {
            Map<Integer, Object> map = new HashMap<Integer, Object>();
            Log.d("setListData===", jsonModel.getHid() + "");
            map.put(-1, jsonModel.getHid());
            map.put(R.id.adapter_house_item_two_image, jsonModel.getTitlepic());
            String tag = jsonModel.getTag();
            if (tag != null) {
                if (tag.equals("1")) {
                    map.put(R.id.adapter_house_item_two_top_mark, R.drawable.house_icons_1 + ":VISIBLE");
                } else if (tag.equals("2")) {
                    map.put(R.id.adapter_house_item_two_top_mark, R.drawable.house_icons_5 + ":VISIBLE");
                } else if (tag.equals("3")) {
                    map.put(R.id.adapter_house_item_two_top_mark, R.drawable.house_icons_4 + ":VISIBLE");
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
                itemAdapter.notifyDataSetChanged();
            }
        }
    }

    public class OldHousePageAsyncTask extends AsyncTask<BaseModel, Void, JsonResult<List<OldHouseJsonModel>>> {

        private BaseModel baseModel;

        @Override
        protected JsonResult<List<OldHouseJsonModel>> doInBackground(BaseModel... params) {
            baseModel = params[0];
            return getListDataByNetWork(LIST_URL, baseModel);
        }


        @Override
        protected void onPostExecute(JsonResult<List<OldHouseJsonModel>> jsonResult) {
            if (jsonResult != null && jsonResult.isStatus()) {
                List<OldHouseJsonModel> list = jsonResult.getData();
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

    /**
     * 列表页
     */
    public class OldHouseAsyncTask extends AsyncTask<BaseModel, Void, JsonResult<List<OldHouseJsonModel>>> {
        private BaseModel baseModel;

        @Override
        protected JsonResult<List<OldHouseJsonModel>> doInBackground(BaseModel... params) {
            baseModel = params[0];
            //默认上来必须都是第一页数据
            if (baseModel.getPage() != null) {
                baseModel.getPage().setPage(1);
            }
            return getListDataByNetWork(LIST_URL, baseModel);
        }

        @Override
        protected void onPostExecute(JsonResult<List<OldHouseJsonModel>> jsonResult) {

            if (jsonResult != null && jsonResult.isStatus()) {
                Log.d("OldHouseActivity->onPostExecute", "jsonResult is not null");
                Page page = new Page(Integer.valueOf(jsonResult.getTotal()), Global.PAGE_SIZE);
                baseModel.setPage(page);
                setCommonHouseCount(baseModel.getTextViewId(), jsonResult.getTotal(),OLD_RENT_COMMUNITY_COUNT);
                setCommonHouseCount(R.id.gone_house_count, jsonResult.getTotal(),OLD_RENT_COMMUNITY_COUNT);

                List<OldHouseJsonModel> list = jsonResult.getData();
                List<Map<Integer, Object>> resultList = new ArrayList<Map<Integer, Object>>();

                setListData(list, resultList, null);

                DefaultDataUtils.setDefaultHint(resultList, Global.NOT_FIND_DATA);
                baseModel.setListData(resultList);
                ListView listView = (ListView) findViewById(baseModel.getListViewId());
                ItemAdapter itemAdapter = new ItemAdapter(OldHouseActivity.this, baseModel.getItemLayoutInflaterId(), baseModel.getListData());
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
}
