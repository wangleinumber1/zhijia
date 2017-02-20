package com.zhijia.ui.zhijiaActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.google.common.base.Optional;
import com.zhijia.Global;
import com.zhijia.service.data.Medol.DynamicNewsJsonModel;
import com.zhijia.service.data.Medol.HouseDynamicNewsJsonModel;
import com.zhijia.service.data.Medol.HouseListItemModel;
import com.zhijia.service.data.Medol.JsonResult;
import com.zhijia.service.network.HttpClientUtils;
import com.zhijia.ui.R;
import com.zhijia.ui.list.adapter.AbstractHouseListViewAdapter;
import com.zhijia.ui.list.adapter.HouseDynamicListViewAdapter;
import com.zhijia.ui.list.page.Page;
import com.zhijia.util.DefaultDataUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 房子的动态信息
 */
public class NewHouseDynamicActivity extends Activity implements AbsListView.OnScrollListener {

    private String DYNAMIC_NEWS_URL = Global.API_WEB_URL + "/xinfang/news";

    //最后的可视项索引
    private int visibleLastIndex = 0;
    //当前窗口可见项总数
    private int visibleItemCount;

    private AbstractHouseListViewAdapter adapter;

    private OnClickListener onClickListener = new OnClickListener();

    //这个是要加载出来的一个视图
    private View footerView;
    private ListView listView;

    private Page page;
    private TextView totalCountDescView;
    private String hid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_house_dynamic);
        totalCountDescView = (TextView) findViewById(R.id.new_house_dynamic_total_count_desc);
        Intent intent = getIntent();
        hid = intent.getStringExtra("hid");

        footerView = getLayoutInflater().inflate(R.layout.house_list_wait_load, null);
        footerView.findViewById(R.id.house_list_wait_load_relative).setVisibility(View.GONE);

        listView = (ListView) findViewById(R.id.new_house_dynamic_list_view);

        adapter = new HouseDynamicListViewAdapter(this, null);
        listView.setAdapter(this.adapter);
        listView.setOnScrollListener(this);

        findViewById(R.id.back).setOnClickListener(onClickListener);

        bindEvent();

        startTask();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        Log.d("NewHouseDynamicActivity->adapter", adapter.getCount() + "");
        int itemsLastIndex = adapter.getCount() - 1;    //数据集最后一项的索引


        int lastIndex = itemsLastIndex + ((ListView) view).getHeaderViewsCount() + ((ListView) view).getFooterViewsCount();
        Log.d("NewHouseDynamicActivity", "visibleLastIndex==" + visibleLastIndex + "  itemsLastIndex==" + itemsLastIndex);
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && visibleLastIndex == lastIndex) {
            footerView.setVisibility(View.VISIBLE);
            HousePrivilegeTask task = new HousePrivilegeTask();
            task.execute();
        } else {
            footerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.visibleItemCount = visibleItemCount;
        this.visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
        Log.d(Global.LOG_TAG, "visibleItemCount==" + visibleItemCount + "  firstVisibleItem====" + firstVisibleItem);
    }


    public void bindEvent() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ListView listView1 = (ListView) parent;
                ListAdapter listAdapter = listView1.getAdapter();
                HouseDynamicListViewAdapter itemAdapter = null;
                Log.d("bindEvent->", position + "");

                if (listAdapter instanceof HeaderViewListAdapter) {
                    itemAdapter = (HouseDynamicListViewAdapter) (((HeaderViewListAdapter) listAdapter).getWrappedAdapter());
                } else {
                    itemAdapter = (HouseDynamicListViewAdapter) listAdapter;
                }
                HouseListItemModel houseListItemModel = (HouseListItemModel) itemAdapter.getItem(position);
                Intent intent = new Intent(NewHouseDynamicActivity.this, NewHouseDynamicDetailActivity.class);
                intent.putExtra("url", houseListItemModel.getHouseDynamicURL());
                startActivity(intent);
            }
        });
    }

    public void setNetWorkErrorValue() {
        List<HouseListItemModel> items = new ArrayList<HouseListItemModel>();
        DefaultDataUtils.setDefaultHoseCommonList(items, Global.ERROR_NET_WORK);
        adapter = new HouseDynamicListViewAdapter(this, items);
        listView.setAdapter(adapter);
        Toast.makeText(NewHouseDynamicActivity.this, Global.ERROR_NET_WORK, Toast.LENGTH_SHORT).show();
    }

    private void startTask() {
        NewHouseDynamicAsyncTask newHouseDynamicAsyncTask = new NewHouseDynamicAsyncTask();
        newHouseDynamicAsyncTask.execute();
    }


    public JsonResult<HouseDynamicNewsJsonModel> getHouseDynamicNews() {

        HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
        Map<String, String> map = new HashMap<String, String>();
        map.put("cityid", Global.NOW_CITY_ID);
        map.put("hid", hid);
        if (getPage() != null) {
            map.put("page", String.valueOf(getPage().getPage()));
        } else {
            map.put("page", "1");
        }

        Optional<JsonResult<HouseDynamicNewsJsonModel>> optional = httpClientUtils.getUnsignedByData(DYNAMIC_NEWS_URL, map, HouseDynamicNewsJsonModel.class);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;

    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    private void loadData() {
        int totalPage = getPage().getTotalPage();
        int nextPage = getPage().getNextPage();
        int nowPage = getPage().getPage();
        Log.d("NewHouseDynamicActivity page", totalPage + "");
        Log.d("NewHouseDynamicActivity page", nextPage + "");
        Log.d("NewHouseDynamicActivity page", nowPage + "");
        if (nowPage < totalPage) {
            //设置当前页要加载的页数
            getPage().setPage(nextPage);
            NewHousePageDynamicAsyncTask pageAsyncTask = new NewHousePageDynamicAsyncTask();
            pageAsyncTask.execute();
        }

        if (nowPage == totalPage && nowPage != 1) {
            NewHousePageDynamicAsyncTask pageAsyncTask = new NewHousePageDynamicAsyncTask();
            pageAsyncTask.execute();
        }
    }

    private void getDataList(List<HouseListItemModel> items, List<DynamicNewsJsonModel> list) {

        for (DynamicNewsJsonModel temp : list) {
            HouseListItemModel model = new HouseListItemModel();
            model.setHouseDynamicPublished(temp.getPublished());
            model.setHouseDynamicTitle(temp.getTitle());
            model.setHouseDynamicURL(temp.getUrl());
            items.add(model);
        }
    }


    class NewHousePageDynamicAsyncTask extends AsyncTask<Void, Void, JsonResult<HouseDynamicNewsJsonModel>> {

        @Override
        protected JsonResult<HouseDynamicNewsJsonModel> doInBackground(Void... params) {
            return getHouseDynamicNews();
        }

        @Override
        protected void onPostExecute(JsonResult<HouseDynamicNewsJsonModel> jsonResult) {
            if (jsonResult != null && jsonResult.isStatus()) {
                List<DynamicNewsJsonModel> list = jsonResult.getData().getDynamic();
                for (DynamicNewsJsonModel temp : list) {
                    HouseListItemModel model = new HouseListItemModel();
                    model.setHouseDynamicURL(temp.getUrl());
                    model.setHouseDynamicTitle(temp.getTitle());
                    model.setHouseDynamicPublished(temp.getPublished());
                    adapter.addItem(model);
                    adapter.notifyDataSetChanged();
                }
                if (getPage().getPage() == getPage().getTotalPage()) {
                    getPage().setPage(getPage().getTotalPage() + 1);
                    Log.d("new nowPage", getPage().getPage() + "");
                }
            } else if (jsonResult == null) {
                setNetWorkErrorValue();
            }
        }
    }

    class NewHouseDynamicAsyncTask extends AsyncTask<Void, Void, JsonResult<HouseDynamicNewsJsonModel>> {

        @Override
        protected JsonResult<HouseDynamicNewsJsonModel> doInBackground(Void... params) {
            return getHouseDynamicNews();
        }

        @Override
        protected void onPostExecute(JsonResult<HouseDynamicNewsJsonModel> jsonResult) {
            if (jsonResult != null && jsonResult.isStatus()) {
                Page page = new Page(Integer.valueOf(jsonResult.getData().getTotal()), Global.PAGE_SIZE);
                setPage(page);
                List<DynamicNewsJsonModel> list = jsonResult.getData().getDynamic();
                List<HouseListItemModel> items = new ArrayList<HouseListItemModel>();
                getDataList(items, list);
                DefaultDataUtils.setDefaultHoseCommonList(items, Global.NOT_FIND_DATA);
                if (listView.getFooterViewsCount() == 0 && items.size() > 9) {
                    listView.addFooterView(footerView);
                }
                adapter = new HouseDynamicListViewAdapter(NewHouseDynamicActivity.this, items);
                listView.setAdapter(adapter);
                totalCountDescView.setText(String.format(getString(R.string.dynamic_news), jsonResult.getData().getTotal()));
            } else {
                setNetWorkErrorValue();
            }
        }
    }

    class HousePrivilegeTask extends AsyncTask<Integer, Integer, Void> {


        @Override
        protected Void doInBackground(Integer... params) {
            return null;
        }

        /**
         * 这里的String参数对应AsyncTask中的第三个参数（也就是接收doInBackground的返回值）
         * 在doInBackground方法执行结束之后在运行，并且运行在UI线程当中 可以对UI空间进行设置
         */
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            footerView.setVisibility(View.GONE);
            loadData();
            listView.setSelection(visibleLastIndex - visibleItemCount + 1); //设置选中项
        }
    }


    public class OnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    finish();
                    break;
            }
        }
    }
}
