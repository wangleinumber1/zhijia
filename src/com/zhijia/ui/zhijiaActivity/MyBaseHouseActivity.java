package com.zhijia.ui.zhijiaActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import com.zhijia.Global;
import com.zhijia.service.data.Medol.JsonResult;
import com.zhijia.ui.R;
import com.zhijia.ui.list.adapter.ItemAdapter;
import com.zhijia.ui.list.page.Page;
import com.zhijia.ui.my.interfaces.IMyListDataNetWork;
import com.zhijia.ui.my.interfaces.IMyLoadData;
import com.zhijia.util.DefaultDataUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 和我的先关的基类
 */
public abstract class MyBaseHouseActivity<T> extends Activity implements AbsListView.OnScrollListener, IMyLoadData, IMyListDataNetWork<T> {

    private int backId;

    //这个是要加载出来的一个视图
    private View footerView;

    private ListView listView ;

    private ItemAdapter adapter ;

    private int itemLayoutInflaterId ;

    private Page page;

    private int visibleLastIndex = 0;

    //当前窗口可见项总数
    private int visibleItemCount;

    private Map<String,String> paramMap ;

    private int countId ;

    private int countStr ;

    private WeakReference<Activity> week;

    public abstract void convertToAdapterList(List<T> list,List<Map<Integer, Object>> resultList, ItemAdapter itemAdapter) ;

    //初始化视图一定需要的。
    public void initView(ListView listView,int itemLayoutInflaterId,Map<String,String> paramMap){
        this.listView = listView ;
        footerView = getLayoutInflater().inflate(R.layout.house_list_wait_load, null);
        footerView.findViewById(R.id.house_list_wait_load_relative).setVisibility(View.GONE);
        this.itemLayoutInflaterId = itemLayoutInflaterId ;
        this.paramMap = paramMap ;
        listView.setOnScrollListener(this);
    }

    /**
     * 设置标题使用的 同时绑定返回事件。
     *
     * @param titleMap --- 为了扩展
     * @param backId
     */
    public void setTitle(Map<Integer, String> titleMap, int backId) {
        this.backId = backId;
        findViewById(backId).setOnClickListener(new ClickListener());
        for(Map.Entry<Integer,String> temp:titleMap.entrySet()){
            ((TextView)findViewById(temp.getKey())).setText(temp.getValue());
        }
    }


    public int getBackId() {
        return backId;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public int getCountStr() {
        return countStr;
    }

    public void setCountStr(int countStr) {
        this.countStr = countStr;
    }

    public int getCountId() {
        return countId;
    }

    public void setCountId(int countId) {
        this.countId = countId;
    }

    public WeakReference<Activity> getWeek() {
        return week;
    }

    public void setWeek(WeakReference<Activity> week) {
        this.week = week;
    }

    public void setLogin(Context context){
        if (Global.USER_AUTH_STR.equals("")) {
            Intent loginIntent = new Intent(context, LoginActivity.class);
            startActivityForResult(loginIntent, 123);
        } else{
            paramMap.put("authstr",Global.USER_AUTH_STR) ;
            startListTask(paramMap);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Log.d("MyBaseHouseActivity->authstr", Global.USER_AUTH_STR);
            paramMap.put("authstr",Global.USER_AUTH_STR) ;
            startListTask(paramMap) ;
        } else {
            finish();
        }
    }

    @Override
    public void startListTask(Map<String,String> paramMap) {
        MyBaseHouseAsyncTask myBaseHouseAsyncTask = new MyBaseHouseAsyncTask();
        myBaseHouseAsyncTask.execute(paramMap) ;
    }

    @Override
    public void setNetWorkErrorValue() {

    }

    @Override
    public void loadData() {
        int totalPage = getPage().getTotalPage();
        int nextPage = getPage().getNextPage();
        int nowPage = getPage().getPage();
        Log.d("BaseModel page", totalPage + "");
        Log.d("BaseModel page", nextPage + "");
        Log.d("BaseModel page", nowPage + "");

        if (nowPage < totalPage) {
            //设置下页面作为当前页
           getPage().setPage(nextPage);
            MyBaseHousePageAsyncTask pageAsyncTask = new MyBaseHousePageAsyncTask();
            pageAsyncTask.execute(paramMap);

        }

        //刨除去第一页因为上来已经加载过了
        if (nowPage == totalPage && nowPage != 1) {
            MyBaseHousePageAsyncTask pageAsyncTask = new MyBaseHousePageAsyncTask();
            pageAsyncTask.execute(paramMap);

        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        Log.d("MyBaseHouseActivity->adapter", adapter.getCount() + "");
        int itemsLastIndex = adapter.getCount() - 1;    //数据集最后一项的索引
        int lastIndex = itemsLastIndex + ((ListView) view).getHeaderViewsCount() + ((ListView) view).getFooterViewsCount();
        Log.d("MyBaseHouseActivity", "visibleLastIndex==" + visibleLastIndex + "  lastIndex==" + lastIndex);
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && visibleLastIndex == lastIndex) {
            footerView.setVisibility(View.VISIBLE);
            HousePrivilegeTask task = new HousePrivilegeTask();
            task.execute();
        } else {
            footerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCounts, int totalItemCount) {
        this.visibleItemCount = visibleItemCounts;
        this.visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
        Log.d("MyBaseHouseActivity", "visibleItemCount==" + visibleItemCount + "  firstVisibleItem====" + firstVisibleItem);
    }


    public class MyBaseHousePageAsyncTask extends AsyncTask<Map<String,String>, Void, JsonResult<List<T>>>{

        @Override
        protected JsonResult<List<T>> doInBackground(Map<String, String>... params) {
            return getListDataByNetWork(params[0]);
        }

        @Override
        protected void onPostExecute(JsonResult<List<T>> jsonResult) {
            if(jsonResult != null && jsonResult.isStatus()){
                List<T> list = jsonResult.getData();
                convertToAdapterList(list, null, adapter);
                //刨除去最后一页重复加载
                if (getPage().getPage() == getPage().getTotalPage()) {
                    getPage().setPage(getPage().getTotalPage() + 1);
                    Log.d("new nowPage", getPage().getPage() + "");
                }
            }
        }
    }


    public class MyBaseHouseAsyncTask extends AsyncTask<Map<String,String>, Void, JsonResult<List<T>>>{
        @Override
        protected JsonResult<List<T>> doInBackground(Map<String, String>... params) {
            //默认上来必须都是第一页数据
            if (getPage() != null) {
                getPage().setPage(1);
            }
            return getListDataByNetWork(params[0]);
        }

        @Override
        protected void onPostExecute(JsonResult<List<T>> jsonResult) {
           if(jsonResult != null && jsonResult.isStatus()){
               Page page = new Page(Integer.valueOf(jsonResult.getTotal()), Global.PAGE_SIZE);
               setPage(page);
               ((TextView)findViewById(countId)).setText(String.format(getString(countStr),jsonResult.getTotal()));
               List<T> list = jsonResult.getData() ;
               List<Map<Integer, Object>> resultList = new ArrayList<Map<Integer, Object>>();
               convertToAdapterList(list,resultList,null) ;
               //如果没有查询到就提示。
               DefaultDataUtils.setDefaultHint(resultList, Global.NOT_FIND_DATA);
               adapter = new ItemAdapter(getApplicationContext(), itemLayoutInflaterId, resultList);
               if(getWeek()!=null){
                   adapter.setWeek(getWeek());
               }
               if(listView.getFooterViewsCount() == 0 && resultList.size() > 9){
                   listView.addFooterView(footerView);
               }
               listView.setAdapter(adapter);
           }else{
               ((TextView)findViewById(countId)).setText(String.format(getString(countStr),0));
               List<Map<Integer, Object>> resultList = new ArrayList<Map<Integer, Object>>();
               DefaultDataUtils.setDefaultHint(resultList, Global.NOT_FIND_DATA);
               adapter = new ItemAdapter(getApplicationContext(), itemLayoutInflaterId, resultList);
               if(getWeek()!=null){
                   adapter.setWeek(getWeek());
               }
               if(listView.getFooterViewsCount() == 0 && resultList.size() > 9){
                   listView.addFooterView(footerView);
               }
               listView.setAdapter(adapter);
           }
        }
    }


    public class HousePrivilegeTask extends AsyncTask<Integer, Integer, Void>{

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

    public class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.getId() == getBackId()) {
                finish();
            }
        }
    }
}
