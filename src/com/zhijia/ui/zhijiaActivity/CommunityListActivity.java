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
import com.zhijia.service.data.Medol.CommunityListJsonModel;
import com.zhijia.service.data.Medol.JsonResult;
import com.zhijia.service.network.HttpClientUtils;
import com.zhijia.ui.R;
import com.zhijia.ui.list.adapter.ItemAdapter;
import com.zhijia.ui.list.interfaces.IHouseDetails;
import com.zhijia.ui.list.interfaces.IListDataNetWork;
import com.zhijia.ui.list.interfaces.ILoadData;
import com.zhijia.ui.list.page.Page;
import com.zhijia.util.DefaultDataUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 小区列表
 */
public class CommunityListActivity extends BaseHouseConditionActivity implements IListDataNetWork<CommunityListJsonModel>, ILoadData,IHouseDetails {

    private final String[] texts = new String[]{"区域", "均价", "租金", "类型"};
    private final int[] areas = new int[]{R.id.house_area_one, R.id.house_area_two, R.id.house_area_three, R.id.house_area_four};

    private final String COMMUNITY_CONDITION_URL = Global.API_WEB_URL + "/community/field";

    private final String COMMUNITY_LIST_URL = Global.API_WEB_URL + "/community/list";


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_list);
        this.setCommonHouseTitle(R.id.house_top_app_gps, R.string.communtiy_title);
        this.setConditionNetwork(this);
        this.setLoadData(this);
        this.setListDataNetWork(this);
        //注册事件绑定
        findViewById(R.id.house_back).setOnClickListener(new BaseHouseActivity.HouseOnClickListener());
        findViewById(R.id.new_house_map).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent communityMapIntent = new Intent(CommunityListActivity.this.getApplicationContext(), CommunityMapActivity.class);
                startActivity(communityMapIntent);
                CommunityListActivity.this.finish();
            }
        });
        BaseModel baseModel = new BaseModel();
        baseModel.setListViewId(R.id.community_list);
        baseModel.setTexts(texts);
        baseModel.setHouseTopViewId(R.id.house_top_app_gps);
        baseModel.setAreas(areas);
        baseModel.setConditionURL(COMMUNITY_CONDITION_URL);
        baseModel.setItemLayoutInflaterId(R.layout.community_list_item);
        this.setHouseDetails(this);
        this.setAdapterData(baseModel);
        this.bindEventArea(linearIds,areas, relativeId, ListType.LOOK_HOUSE);

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
            CommunityPageAsyncTask pageAsyncTask = new CommunityPageAsyncTask();
            pageAsyncTask.execute(baseModel);

        }

        //刨除去第一页因为上来已经加载过了
        if (nowPage == totalPage && nowPage != 1) {
            CommunityPageAsyncTask pageAsyncTask = new CommunityPageAsyncTask();
            pageAsyncTask.execute(baseModel);

        }
    }

    @Override
    public JsonResult<List<CommunityListJsonModel>> getListDataByNetWork(String url, BaseModel baseModel) {
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
        Optional<JsonResult<List<CommunityListJsonModel>>> optional = httpClientUtils.getUnsignedListByData(url, map, CommunityListJsonModel.class);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    @Override
    public void startListTask(BaseModel baseModel) {
        CommunityListAsyncTask communityListAsyncTask = new CommunityListAsyncTask();
        communityListAsyncTask.execute(baseModel);
    }

    @Override
    public void setNetWorkErrorValue(BaseModel baseModel) {
        List<Map<Integer, Object>> resultList = new ArrayList<Map<Integer, Object>>();
        DefaultDataUtils.setDefaultHint(resultList, Global.ERROR_NET_WORK);
        baseModel.setListData(resultList);
        ListView listView = (ListView) findViewById(baseModel.getListViewId());
        ItemAdapter itemAdapter = new ItemAdapter(CommunityListActivity.this, baseModel.getItemLayoutInflaterId(), baseModel.getListData());
        setItemAdapter(itemAdapter);
        listView.setAdapter(itemAdapter);
        Toast.makeText(CommunityListActivity.this, Global.ERROR_NET_WORK, Toast.LENGTH_SHORT).show();
    }

    private void setListData(List<CommunityListJsonModel> list, List<Map<Integer, Object>> resultList, ItemAdapter itemAdapter) {

        for (CommunityListJsonModel jsonModel : list) {
            Map<Integer, Object> map = new HashMap<Integer, Object>();
            map.put(-1,jsonModel.getCommunityid()) ;
            if ((jsonModel.getSellprice() != null && !jsonModel.getSellprice().isEmpty()) && (jsonModel.getRentprice() != null && !jsonModel.getRentprice().isEmpty())) {
                setIsUpAndDown(View.VISIBLE, View.GONE, View.GONE, View.GONE, map);
                setCommonListData(jsonModel, map);
                //每平米的价钱
                map.put(R.id.community_list_type, jsonModel.getSellprice());

                //每平米的涨幅
                String strSellratio = jsonModel.getSellratio();
                //租金
                map.put(R.id.community_list_split_price, jsonModel.getRentprice());
                String strRentratio = jsonModel.getRentratio();


                try{
                    if (strSellratio.indexOf("-") != 0) {//没有减号，表示是属于正数
                        float floatSellratio = Float.parseFloat(strSellratio);
                        if (floatSellratio == 0) {
                            map.put(R.id.community_list_split_up_rate, "text_color:" + R.color.black + ":" + "无涨幅");
                            map.put(R.id.community_list_split_rate_up_image, "visibility:" + View.GONE);
                        } else {
                            int temp = (int) (floatSellratio * 100);
                            map.put(R.id.community_list_split_up_rate, temp + "%");
                            map.put(R.id.community_list_split_rate_up_image, R.drawable.house_up);
                        }
                    } else {
                        float floatSellratio = Float.parseFloat(strSellratio.substring(strSellratio.indexOf("-") + 1));
                        int temp = (int) (floatSellratio * 100);
                        map.put(R.id.community_list_split_up_rate, temp + "%");
                        map.put(R.id.community_list_split_rate_up_image, R.drawable.house_down);
                        //如果是跌了就换下字体颜色
                        map.put(R.id.community_list_type, "text_color:" + R.color.green + ":" + jsonModel.getSellprice());
                    }

                    if (strRentratio.indexOf("-") != 0) {
                        float floatRentratio = Float.parseFloat(strRentratio);
                        if (floatRentratio == 0) {
                            map.put(R.id.community_list_split_down_rate, "text_color:" + R.color.black + ":" + "无涨幅");
                            map.put(R.id.community_list_split_rate_down_image, "visibility:" + View.GONE);
                        } else {
                            int temp = (int) (floatRentratio * 100);
                            map.put(R.id.community_list_split_down_rate, temp + "%");
                            map.put(R.id.community_list_split_price, "text_color:" + R.color.red + ":" + jsonModel.getRentprice());
                        }
                    } else {
                        float floatRentratio = Float.parseFloat(strRentratio.substring(strRentratio.indexOf("-") + 1));
                        int tempRentratio = (int) (floatRentratio * 100);
                        map.put(R.id.community_list_split_down_rate, tempRentratio + "%");
                        map.put(R.id.community_list_split_rate_down_image, R.drawable.house_down);
                    }
                } catch (NumberFormatException e){
                    map.put(R.id.community_list_split_up_rate, "text_color:" + R.color.black + ":" + "无涨幅");
                    map.put(R.id.community_list_split_rate_up_image, "visibility:" + View.GONE);
                    map.put(R.id.community_list_split_down_rate, "text_color:" + R.color.black + ":" + "无涨幅");
                    map.put(R.id.community_list_split_rate_down_image, "visibility:" + View.GONE);
                }



            } else if ((jsonModel.getSellprice() != null && !jsonModel.getSellprice().isEmpty()) && (jsonModel.getRentprice() == null || jsonModel.getRentprice().isEmpty())) {
                setIsUpAndDown(View.GONE, View.GONE, View.VISIBLE, View.GONE, map);
                setCommonListData(jsonModel, map);
                //每平米的价钱
                map.put(R.id.community_list_type, jsonModel.getSellprice());
                //每平米的涨幅
                String strSellratio = jsonModel.getSellratio();
                try{
                    if (strSellratio.indexOf("-") != 0) {//没有减号，表示是属于正数
                        float floatSellratio = Float.parseFloat(strSellratio);
                        if (floatSellratio == 0) {
                            map.put(R.id.community_list_split_up_rate, "无涨幅");
                            map.put(R.id.community_list_split_rate_up_image, "visibility:" + View.GONE);
                            map.put(R.id.community_list_type, "text_color:" + R.color.black + ":" + jsonModel.getSellprice());
                        } else {
                            int temp = (int) (floatSellratio * 100);
                            map.put(R.id.community_list_split_up_rate, temp + "%");
                            map.put(R.id.community_list_split_rate_up_image, R.drawable.house_up);
                        }
                    } else {
                        float floatSellratio = Float.parseFloat(strSellratio.substring(strSellratio.indexOf("-") + 1));
                        int temp = (int) (floatSellratio * 100);
                        map.put(R.id.community_list_split_up_rate, temp + "%");
                        map.put(R.id.community_list_split_rate_up_image, R.drawable.house_down);
                        //如果是跌了就换下字体颜色
                        map.put(R.id.community_list_type, "text_color:" + R.color.green + ":" + jsonModel.getSellprice());
                    }
                } catch (NumberFormatException e){
                    map.put(R.id.community_list_split_up_rate, "无涨幅");
                    map.put(R.id.community_list_split_rate_up_image, "visibility:" + View.GONE);
                    map.put(R.id.community_list_type, "text_color:" + R.color.black + ":" + jsonModel.getSellprice());
                }

            } else if ((jsonModel.getSellprice() == null || jsonModel.getSellprice().isEmpty()) && (jsonModel.getRentprice() != null && !jsonModel.getRentprice().isEmpty())) {
                setIsUpAndDown(View.GONE, View.GONE, View.GONE, View.VISIBLE, map);
                setCommonListData(jsonModel, map);
                //租金
                map.put(R.id.community_list_split_price, jsonModel.getRentprice());
                String strRentratio = jsonModel.getRentratio();
                try{
                    if (strRentratio.indexOf("-") != 0) {
                        float floatRentratio = Float.parseFloat(strRentratio);
                        if (floatRentratio == 0) {
                            map.put(R.id.community_list_split_down_rate, "无涨幅");
                            map.put(R.id.community_list_split_rate_down_image, "visibility:" + View.GONE);
                            map.put(R.id.community_list_split_price, "text_color:" + R.color.black + ":" + jsonModel.getRentprice());
                        } else {
                            int temp = (int) (floatRentratio * 100);
                            map.put(R.id.community_list_split_down_rate, temp + "%");
                            map.put(R.id.community_list_split_price, "text_color:" + R.color.red + ":" + jsonModel.getRentprice());
                        }
                    } else {
                        float floatRentratio = Float.parseFloat(strRentratio.substring(strRentratio.indexOf("-") + 1));
                        int tempRentratio = (int) (floatRentratio * 100);
                        map.put(R.id.community_list_split_down_rate, tempRentratio + "%");
                        map.put(R.id.community_list_split_rate_down_image, R.drawable.house_down);
                    }
                }catch (NumberFormatException e){
                    map.put(R.id.community_list_split_down_rate, "无涨幅");
                    map.put(R.id.community_list_split_rate_down_image, "visibility:" + View.GONE);
                    map.put(R.id.community_list_split_price, "text_color:" + R.color.black + ":" + jsonModel.getRentprice());
                }

            } else {
                setIsUpAndDown(View.GONE, View.VISIBLE, View.GONE, View.GONE, map);
                setCommonListData(jsonModel, map);
            }
            if (resultList != null) {
                resultList.add(map);
            }
            if (itemAdapter != null) {
                itemAdapter.addItem(map);
                itemAdapter.notifyDataSetChanged();
            }
        }
    }

    private void setCommonListData(CommunityListJsonModel jsonModel, Map<Integer, Object> map) {
        map.put(R.id.community_list_image, jsonModel.getTitlepic());
        map.put(R.id.community_list_name, jsonModel.getCommunityname());
        map.put(R.id.community_list_area, jsonModel.getAreaname() + " " + jsonModel.getAddress());
        if (jsonModel.getSellnum() != null && !jsonModel.getSellnum().isEmpty()) {
            map.put(R.id.community_list_sell_houses, jsonModel.getSellnum());
        } else {
            map.put(R.id.community_list_sell_houses, "售0套");
        }
        if (jsonModel.getRentnum() != null && !jsonModel.getRentnum().isEmpty()) {
            map.put(R.id.community_list_rent_houses, jsonModel.getRentnum());
        } else {
            map.put(R.id.community_list_rent_houses, "租0套");
        }

    }

    private void setIsUpAndDown(int one, int two, int three, int four, Map<Integer, Object> map) {
        map.put(R.id.community_list_details, "Visibility:" + one);
        map.put(R.id.community_list_simple, "Visibility:" + two);
        map.put(R.id.community_list_details_sellratio, "Visibility:" + three);
        map.put(R.id.community_list_details_rentratio, "Visibility:" + four);
    }

    @Override
    public void showDetail(final Context packageContext, ListView listView) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (parent.getId()) {
                    case R.id.community_list:
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
                        Intent oldHouseAreaDetails = new Intent(packageContext, OldHouseAreaDetailsActivity.class);
                        oldHouseAreaDetails.putExtra("cid", parameter);
                        startActivity(oldHouseAreaDetails);
                        break;
                }
            }
        });
    }

    public class CommunityPageAsyncTask extends AsyncTask<BaseModel, Void, JsonResult<List<CommunityListJsonModel>>> {

        private BaseModel baseModel;

        @Override
        protected JsonResult<List<CommunityListJsonModel>> doInBackground(BaseModel... params) {
            baseModel = params[0];
            return getListDataByNetWork(COMMUNITY_LIST_URL, baseModel);
        }


        @Override
        protected void onPostExecute(JsonResult<List<CommunityListJsonModel>> jsonResult) {
            if (jsonResult != null && jsonResult.isStatus()) {
                List<CommunityListJsonModel> list = jsonResult.getData();
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
     * 搜索的
     */
    public class CommunityListAsyncTask extends AsyncTask<BaseModel, Void, JsonResult<List<CommunityListJsonModel>>> {

        private BaseModel baseModel;

        @Override
        protected JsonResult<List<CommunityListJsonModel>> doInBackground(BaseModel... params) {
            baseModel = params[0];
            if (baseModel.getPage() != null) {
                baseModel.getPage().setPage(1);
            }
            return getListDataByNetWork(COMMUNITY_LIST_URL, baseModel);
        }

        @Override
        protected void onPostExecute(JsonResult<List<CommunityListJsonModel>> jsonResult) {

            if (jsonResult != null && jsonResult.isStatus()) {
                Page page = new Page(Integer.valueOf(jsonResult.getTotal()), Global.PAGE_SIZE);
                baseModel.setPage(page);
                List<CommunityListJsonModel> list = jsonResult.getData();
                List<Map<Integer, Object>> resultList = new ArrayList<Map<Integer, Object>>();
                setListData(list, resultList, null);
                //如果是0表示没有数据，这块就要提示一下用户木有数据
                DefaultDataUtils.setDefaultHint(resultList, Global.NOT_FIND_DATA);

                baseModel.setListData(resultList);
                ListView listView = (ListView) findViewById(baseModel.getListViewId());
                ItemAdapter itemAdapter = new ItemAdapter(CommunityListActivity.this, baseModel.getItemLayoutInflaterId(), baseModel.getListData());
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