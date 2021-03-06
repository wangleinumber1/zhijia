package com.zhijia.ui.zhijiaActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.common.base.Optional;
import com.zhijia.Global;
import com.zhijia.service.data.Medol.BaseModel;
import com.zhijia.service.data.Medol.JsonResult;
import com.zhijia.service.data.Medol.OrderWatchHouseModel;
import com.zhijia.service.network.HttpClientUtils;
import com.zhijia.ui.R;
import com.zhijia.ui.list.adapter.ItemAdapter;
import com.zhijia.ui.list.interfaces.IListDataNetWork;
import com.zhijia.ui.list.interfaces.ILoadData;
import com.zhijia.ui.list.page.Page;
import com.zhijia.util.DefaultDataUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的租房预约看房记录列表
 */
public class MyOrderWatchRentHouseRecordActivity extends BaseHouseActivity implements IListDataNetWork<OrderWatchHouseModel>, ILoadData {

    private static String ORDER_LIST_URL = Global.API_WEB_URL + "/member/appointment";

    //点击事件的侦听
    private ClickListener clickListener = new ClickListener();
    private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Map<Integer, Object> map = (Map<Integer, Object>) parent.getAdapter().getItem(position);
            String hid = (String) map.get(-1);
            Intent rentHouseDetails = new Intent(getApplicationContext(), OldHouseDetailsActivity.class);
            rentHouseDetails.putExtra("hid", hid);
            rentHouseDetails.putExtra("housetype", "2");
            rentHouseDetails.putExtra("identity", "2");
            startActivity(rentHouseDetails);
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_common_house_list);

        //注册事件绑定
        findViewById(R.id.back).setOnClickListener(clickListener);
        ((ListView) findViewById(R.id.my_common_house_list)).setOnItemClickListener(listener);
        this.setLoadData(this);

        BaseModel baseModel = new BaseModel();
        baseModel.setListViewId(R.id.my_common_house_list);
        baseModel.setHouseTopViewId(R.id.title);
        baseModel.setHouseTitleId(R.string.my_order_watch_rent_house_record);
        baseModel.setItemLayoutInflaterId(R.layout.my_order_watch_rent_house_record_list_item);
        setListDataNetWork(this);
        this.setAdapterData(baseModel);
    }

    /**
     * 更新总条数显示
     */
    private void updateTotalCountUI(int totalCount) {
        ((TextView) findViewById(R.id.total_count_desc)).setText(String.format(getString(R.string.total_order_watch_rent_house_record_count), String.valueOf(totalCount)));
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
            ListPageAsyncTask pageAsyncTask = new ListPageAsyncTask();
            pageAsyncTask.execute(baseModel);
        }

        if (nowPage == totalPage && nowPage != 1) {
            ListPageAsyncTask pageAsyncTask = new ListPageAsyncTask();
            pageAsyncTask.execute(baseModel);
        }
    }

    @Override
    public JsonResult<List<OrderWatchHouseModel>> getListDataByNetWork(String url, BaseModel baseModel) {
        HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
        Map<String, String> map = new HashMap<String, String>();
        map.put("cityid", Global.NOW_CITY_ID);
        map.put("authstr", Global.USER_AUTH_STR);
        map.put("identity", "2");
        if (baseModel.getPage() != null) {
            map.put("page", String.valueOf(baseModel.getPage().getPage()));
        } else {
            map.put("page", "1");
        }

        Optional<JsonResult<List<OrderWatchHouseModel>>> optional = httpClientUtils.getUnsignedListByData(ORDER_LIST_URL, map, OrderWatchHouseModel.class);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            return null;
        }
    }

    @Override
    public void startListTask(BaseModel baseModel) {
        ListPageAsyncTask pageAsyncTask = new ListPageAsyncTask();
        pageAsyncTask.execute(baseModel);
    }

    @Override
    public void setNetWorkErrorValue(BaseModel baseModel) {
        updateTotalCountUI(0);
        List<Map<Integer, Object>> resultList = new ArrayList<Map<Integer, Object>>();
        DefaultDataUtils.setDefaultHint(resultList, Global.ERROR_NET_WORK);
        baseModel.setListData(resultList);
        ListView listView = (ListView) findViewById(baseModel.getListViewId());
        ItemAdapter itemAdapter = new ItemAdapter(getApplicationContext(), baseModel.getItemLayoutInflaterId(), baseModel.getListData());
        setItemAdapter(itemAdapter);
        listView.setAdapter(itemAdapter);
        Toast.makeText(getApplicationContext(), Global.ERROR_NET_WORK, Toast.LENGTH_SHORT).show();
    }

    /**
     * 处理分页的
     */
    public class ListPageAsyncTask extends AsyncTask<BaseModel, Void, JsonResult<List<OrderWatchHouseModel>>> {
        private BaseModel baseModel;

        @Override
        protected JsonResult<List<OrderWatchHouseModel>> doInBackground(BaseModel... params) {
            baseModel = params[0];
            return getListDataByNetWork(ORDER_LIST_URL, baseModel);
        }

        @Override
        protected void onPostExecute(JsonResult<List<OrderWatchHouseModel>> jsonResult) {

            if (jsonResult != null && jsonResult.isStatus()) {
                Page page = new Page(Integer.valueOf(jsonResult.getTotal()), Global.PAGE_SIZE);
                baseModel.setPage(page);

                List<OrderWatchHouseModel> list = jsonResult.getData();
                if (list.size() > 0) {
                    for (OrderWatchHouseModel obj : list) {
                        Map<Integer, Object> map = new HashMap<Integer, Object>();
                        map.put(-1, obj.getHid());
                        map.put(R.id.adapter_house_content_one, obj.getCommunityname());
                        map.put(R.id.adapter_house_content_two, "(" + obj.getArea() + " " + obj.getUnit() + " " + obj.getPrice() + ")");
                        map.put(R.id.adapter_house_content_three, obj.getTime() + " " + obj.getHour());
                        map.put(R.id.adapter_house_content_four, obj.getDescription());
                        map.put(R.id.adapter_house_content_five, obj.getPosttime());
                        getItemAdapter().addItem(map);
                        getItemAdapter().notifyDataSetChanged();
                    }

                    if (baseModel.getPage().getPage() == baseModel.getPage().getTotalPage()) {

                        baseModel.getPage().setPage(baseModel.getPage().getTotalPage() + 1);
                        Log.d("new nowPage", baseModel.getPage().getPage() + "");
                    }

                    if (baseModel.getPage().getPage() == baseModel.getPage().getTotalPage()) {

                        baseModel.getPage().setPage(baseModel.getPage().getTotalPage() + 1);
                        Log.d("new nowPage", baseModel.getPage().getPage() + "");
                    }
                    updateTotalCountUI(getItemAdapter().getCount());
                } else if (jsonResult.getTotal().equalsIgnoreCase("0")) {
                    findViewById(R.id.house_list_wait_load_relative).setVisibility(View.GONE);
                    findViewById(R.id.my_common_house_list).setVisibility(View.GONE);
                    findViewById(R.id.no_date_tip).setVisibility(View.VISIBLE);
                    updateTotalCountUI(0);
                }
            } else if (jsonResult == null) {
                findViewById(R.id.house_list_wait_load_relative).setVisibility(View.GONE);
                ((ListView) findViewById(R.id.my_common_house_list)).setVisibility(View.GONE);
                findViewById(R.id.my_common_house_list).setVisibility(View.GONE);
                findViewById(R.id.no_date_tip).setVisibility(View.VISIBLE);
                updateTotalCountUI(0);
            }
        }
    }

    //点击事件的公共类
    class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.back: //后退
                    finish();
                    break;
            }
        }
    }
}