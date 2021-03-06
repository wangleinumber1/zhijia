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
import com.zhijia.service.data.Medol.HouseListItemModel;
import com.zhijia.service.data.Medol.JsonResult;
import com.zhijia.service.data.Medol.NewHouseJsonModel;
import com.zhijia.service.network.HttpClientUtils;
import com.zhijia.ui.R;
import com.zhijia.ui.list.adapter.*;
import com.zhijia.ui.list.page.Page;
import com.zhijia.util.DefaultDataUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 新房列表中的限期特惠、热点楼盘、近期开盘列表
 */
public class HouseCommonListActivity extends Activity implements AbsListView.OnScrollListener {

    private final String HOT_LIST_URL = Global.API_WEB_URL + "/xinfang/hot";
    private final String OPEN_HOUSE_LIST_URL = Global.API_WEB_URL + "/xinfang/opentime";
    private final String PRIVILEGE_LIST_URL = Global.API_WEB_URL + "/xinfang/business";
    private ListView listView;
    private OnClickListener onClickListener = new OnClickListener();
    //最后的可视项索引
    private int visibleLastIndex = 0;

    private String listType;

    //当前窗口可见项总数
    private int visibleItemCount;

    private AbstractHouseListViewAdapter adapter;

    private TextView totalCountDescView;

    private Page page;

    //这个是要加载出来的一个视图
    private View footerView;

    private ProgressBar progressBar;

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.house_common_list);
        totalCountDescView = (TextView) findViewById(R.id.total_count_desc);
        Intent intent = getIntent();
        listType = intent.getStringExtra("listType");
        if (listType == null || listType.equalsIgnoreCase("")) {
            listType = "Privilege";
        }

        footerView = getLayoutInflater().inflate(R.layout.house_list_load, null);
        progressBar = (ProgressBar) footerView.findViewById(R.id.house_progressbar);
        listView = (ListView) findViewById(R.id.house_common_list_list_view);

        this.initAdapter();
        listView.setAdapter(this.adapter);
        listView.setOnScrollListener(this);


        setTitle();
        findViewById(R.id.back).setOnClickListener(onClickListener);

        startTask();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        int itemsLastIndex = adapter.getCount() - 1;    //数据集最后一项的索引

        Log.d(Global.LOG_TAG, "visibleLastIndex==" + visibleLastIndex + "  itemsLastIndex==" + itemsLastIndex);

        int lastIndex = itemsLastIndex + 1;             //加上底部的loadMoreView项
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && visibleLastIndex == lastIndex) {
            footerView.setVisibility(View.VISIBLE);
            HousePrivilegeTask task = new HousePrivilegeTask(this.progressBar);
            task.execute(1000);
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


    /**
     * 第一次进来初始化空的数据，然后进行异步装载数据
     */
    private void initAdapter() {
        if (listType.equalsIgnoreCase("Privilege")) {
            this.adapter = new HousePrivilegeListViewAdapter(this, null);
        } else if (listType.equalsIgnoreCase("HotHouse")) {
            this.adapter = new HotHouseListViewAdapter(this, null);
        } else if (listType.equalsIgnoreCase("RecentOpenHouse")) {
            this.adapter = new RecentOpenHouseListViewAdapter(this, null);
        }
    }


    private void getDataList(List<HouseListItemModel> items, List<NewHouseJsonModel> list) {

        for (NewHouseJsonModel jsonModel : list) {
            HouseListItemModel model = new HouseListItemModel();
            model.setHouseImage(getApplicationContext().getResources().getDrawable(R.drawable.house));
            model.setImageURL(jsonModel.getTitlepic());
            model.setHouseContentOne(jsonModel.getName());
            model.setHid(jsonModel.getHid());
            if (listType.equalsIgnoreCase("Privilege")) {
                model.setHouseContentTwo(jsonModel.getTime());
                model.setHouseContentThree(jsonModel.getPrivilege());
                model.setHouseContentFour(jsonModel.getAreaname());
                model.setHouseContentFive(jsonModel.getAddress());
            } else if (listType.equalsIgnoreCase("HotHouse")) {
                model.setHouseContentTwo(jsonModel.getAverageprice());
                model.setHouseContentThree(jsonModel.getAreaname());
                model.setHouseContentFour(jsonModel.getAddress());
                model.setHouseContentFive(jsonModel.getPriceexplain());
            } else {
                model.setHouseContentTwo(jsonModel.getAverageprice());
                model.setHouseContentThree(jsonModel.getAreaname());
                model.setHouseContentFour(jsonModel.getAddress());
                model.setHouseContentFive(jsonModel.getOpentimeCaption());
            }

            items.add(model);
        }
    }


    public JsonResult<List<NewHouseJsonModel>> getListDataByNetWork(String url) {
        HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
        Map<String, String> map = new HashMap<String, String>();
        map.put("cityid", Global.NOW_CITY_ID);
        if (getPage() != null) {
            map.put("page", String.valueOf(getPage().getPage()));
        } else {
            map.put("page", "1");
        }

        Optional<JsonResult<List<NewHouseJsonModel>>> optional = httpClientUtils.getUnsignedListByData(url, map, NewHouseJsonModel.class);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    private void setAdapterByListType(List<HouseListItemModel> items) {

        if (listType.equalsIgnoreCase("Privilege")) {
            adapter = new HousePrivilegeListViewAdapter(HouseCommonListActivity.this, items);
        } else if (listType.equalsIgnoreCase("HotHouse")) {
            adapter = new HotHouseListViewAdapter(HouseCommonListActivity.this, items);
        } else if (listType.equalsIgnoreCase("RecentOpenHouse")) {
            adapter = new RecentOpenHouseListViewAdapter(HouseCommonListActivity.this, items);
        }
    }

    public void setNetWorkErrorValue() {
        List<HouseListItemModel> items = new ArrayList<HouseListItemModel>();
        DefaultDataUtils.setDefaultHoseCommonList(items, Global.ERROR_NET_WORK);
        setAdapterByListType(items);
        listView.setAdapter(adapter);
        Toast.makeText(HouseCommonListActivity.this, Global.ERROR_NET_WORK, Toast.LENGTH_SHORT).show();
    }

    private void startTask() {
        NewHouseAsyncTask task = new NewHouseAsyncTask(listType);
        task.execute();
    }

    private void setTitle() {
        if (listType.equalsIgnoreCase("Privilege")) {
            ((TextView) this.findViewById(R.id.title)).setText(String.format(getString(R.string.house_privilege), Global.NOW_CITY));
            totalCountDescView.setText(String.format(getString(R.string.privilege_total_count_desc), listView.getAdapter().getCount()));
        } else if (listType.equalsIgnoreCase("HotHouse")) { //热点楼盘。。
            ((TextView) this.findViewById(R.id.title)).setText(String.format(getString(R.string.hot_house), Global.NOW_CITY));
            totalCountDescView.setText(String.format(getString(R.string.hot_house_total_count_desc), listView.getAdapter().getCount()));
        } else if (listType.equalsIgnoreCase("RecentOpenHouse")) {
            ((TextView) this.findViewById(R.id.title)).setText(String.format(getString(R.string.recent_open_house), Global.NOW_CITY));
            totalCountDescView.setText(String.format(getString(R.string.recent_open_house_total_count_desc), listView.getAdapter().getCount()));
        }

    }

    private void setCount(int total) {
        if (listType.equalsIgnoreCase("Privilege")) {
            totalCountDescView.setText(String.format(getString(R.string.privilege_total_count_desc), total));
        } else if (listType.equalsIgnoreCase("HotHouse")) { //热点楼盘。。
            totalCountDescView.setText(String.format(getString(R.string.hot_house_total_count_desc), total));
        } else if (listType.equalsIgnoreCase("RecentOpenHouse")) {
            totalCountDescView.setText(String.format(getString(R.string.recent_open_house_total_count_desc), total));
        }
    }

    private void loadData() {
        int totalPage = getPage().getTotalPage();
        int nextPage = getPage().getNextPage();
        int nowPage = getPage().getPage();
        Log.d("BaseModel page", totalPage + "");
        Log.d("BaseModel page", nextPage + "");
        Log.d("BaseModel page", nowPage + "");

        if (nowPage < totalPage) {
            //设置当前页要加载的页数
            getPage().setPage(nextPage);
            NewHousePageAsyncTask pageAsyncTask = new NewHousePageAsyncTask(listType);
            pageAsyncTask.execute();
        }

        if (nowPage == totalPage && nowPage != 1) {
            NewHousePageAsyncTask pageAsyncTask = new NewHousePageAsyncTask(listType);
            pageAsyncTask.execute();
        }

    }

    /**
     * 处理分页的
     */
    public class NewHousePageAsyncTask extends AsyncTask<Void, Void, JsonResult<List<NewHouseJsonModel>>> {

        private String listType;

        public NewHousePageAsyncTask(String listType) {
            this.listType = listType;
        }

        @Override
        protected JsonResult<List<NewHouseJsonModel>> doInBackground(Void... params) {

            if (listType.equalsIgnoreCase("Privilege")) {
                return getListDataByNetWork(PRIVILEGE_LIST_URL);
            } else if (listType.equalsIgnoreCase("HotHouse")) {
                return getListDataByNetWork(HOT_LIST_URL);
            } else if (listType.equalsIgnoreCase("RecentOpenHouse")) {
                return getListDataByNetWork(OPEN_HOUSE_LIST_URL);
            }
            return null;
        }

        @Override
        protected void onPostExecute(JsonResult<List<NewHouseJsonModel>> jsonResult) {

            if (jsonResult != null && jsonResult.isStatus()) {
                List<NewHouseJsonModel> list = jsonResult.getData();

                for (NewHouseJsonModel jsonModel : list) {
                    HouseListItemModel model = new HouseListItemModel();
                    model.setHouseImage(getApplicationContext().getResources().getDrawable(R.drawable.house));
                    model.setImageURL(jsonModel.getTitlepic());
                    model.setHouseContentOne(jsonModel.getName());
                    model.setHouseContentTwo(jsonModel.getAverageprice());
                    model.setHouseContentThree(jsonModel.getAreaname());
                    model.setHouseContentFour(jsonModel.getAddress());
                    model.setHid(jsonModel.getHid());
                    if (listType.equalsIgnoreCase("Privilege")) {
                        model.setHouseContentFive(jsonModel.getPrivilege());
                    } else {
                        model.setHouseContentFive(jsonModel.getOpentimeCaption());
                    }
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

    /**
     * 搜索的
     */
    public class NewHouseAsyncTask extends AsyncTask<Void, Void, JsonResult<List<NewHouseJsonModel>>> {

        private String listType;

        public NewHouseAsyncTask(String listType) {
            this.listType = listType;
        }

        @Override
        protected JsonResult<List<NewHouseJsonModel>> doInBackground(Void... params) {
            if (listType.equalsIgnoreCase("Privilege")) {
                return getListDataByNetWork(PRIVILEGE_LIST_URL);
            } else if (listType.equalsIgnoreCase("HotHouse")) {
                return getListDataByNetWork(HOT_LIST_URL);
            } else if (listType.equalsIgnoreCase("RecentOpenHouse")) {
                return getListDataByNetWork(OPEN_HOUSE_LIST_URL);
            }
            return null;
        }

        @Override
        protected void onPostExecute(JsonResult<List<NewHouseJsonModel>> jsonResult) {

            if (jsonResult != null && jsonResult.isStatus()) {
                Page page = new Page(Integer.valueOf(jsonResult.getTotal()), Global.PAGE_SIZE);
                setPage(page);
                List<NewHouseJsonModel> list = jsonResult.getData();
                List<HouseListItemModel> items = new ArrayList<HouseListItemModel>();

                getDataList(items, list);

                //如果是0表示没有数据，这块就要提示一下用户木有数据
                DefaultDataUtils.setDefaultHoseCommonList(items, "暂无数据");

                ListView listView = (ListView) findViewById(R.id.house_common_list_list_view);

                //设置相关的适配器
                setAdapterByListType(items);

                if (listView.getFooterViewsCount() == 0 && items.size() > 9) {
                    listView.addFooterView(footerView);
                }

                listView.setAdapter(adapter);
                setCount(Integer.valueOf(jsonResult.getTotal()));
            } else if (jsonResult == null) {
                setNetWorkErrorValue();
            }
        }
    }

    class HousePrivilegeTask extends AsyncTask<Integer, Integer, Void> {

        private ProgressBar progressBar;

        public HousePrivilegeTask(ProgressBar progressBar) {
            this.progressBar = progressBar;
        }

        @Override
        protected Void doInBackground(Integer... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (int i = 10; i <= 100; i += 10) {
                publishProgress(i);
            }

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

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
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
