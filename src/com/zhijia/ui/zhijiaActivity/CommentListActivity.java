package com.zhijia.ui.zhijiaActivity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import com.google.common.base.Optional;
import com.zhijia.Global;
import com.zhijia.service.data.Medol.CommentListJsonModel;
import com.zhijia.service.data.Medol.JsonResult;
import com.zhijia.service.network.HttpClientUtils;
import com.zhijia.ui.R;
import com.zhijia.ui.list.adapter.ItemAdapter;
import com.zhijia.ui.list.page.Page;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class CommentListActivity extends BaseDetailsActivity implements AbsListView.OnScrollListener{

    private final String COMMENT_LIST_URL = Global.API_WEB_URL+"/xinfang/commentlist" ;

    private final String COMMENT_URL = Global.API_WEB_URL+"/xinfang/comment" ;


    //人物头像
    private final int COMMENT_IMAGE = R.id.comment_list_item_image_view ;

    private final int COMMENT_USER_TYPE = R.id.comment_list_item_user_type ;

    private final int COMMENT_RATING = R.id.comment_list_item_rating_bar ;

    private final int COMMENT_TIME = R.id.comment_list_item_time ;

    private final int COMMENT_CONTENT = R.id.comment_list_item_content_text_view ;

    private final int COMMENT_TRAMPLE = R.id.comment_list_item_reply_trample ;

    private final int COMMENT_TOP = R.id.comment_list_item_reply_top ;

    private final int COMMENT_REPLY = R.id.comment_list_item_reply ;

    private final int header_title = R.id.details_title;

    private OnClickListener onClickListener = new OnClickListener();

    private ItemAdapter itemAdapter ;

    private String hid ;

    private Page page;

    //这个是要加载出来的一个视图
    private View footerView;

    private  ListView listView ;

    //最后的可视项索引
    private int visibleLastIndex = 0;
    //当前窗口可见项总数
    private int visibleItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_list);
        hid = getIntent().getStringExtra("hid") ;
        findViewById(R.id.details_back).setOnClickListener(onClickListener);
        setHeader();
        final RatingBar bbsRatingBar = (RatingBar)findViewById(R.id.comment_rating_bar) ;
        bbsRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingBar.setRating(rating);
            }
        });
        findViewById(R.id.comment_list_button_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.comment_list_edit_text);
                int rating = (int) bbsRatingBar.getRating();
                String infoContent = editText.getText().toString();
                if (infoContent != null && !infoContent.isEmpty() && rating!=0) {
                    Map<String, String> commentMap = new HashMap<String, String>();
                    commentMap.put("grade",rating+"") ;
                    commentMap.put("info",editText.getText().toString()) ;
                    CommentAsyncTask commentAsyncTask = new CommentAsyncTask() ;
                    commentAsyncTask.execute(commentMap) ;
                } else if(infoContent == null){
                    Toast toast = Toast.makeText(getApplicationContext(), "评论数据不能为空", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "评分不能为空", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });
        listView = (ListView)findViewById(R.id.comment_list_content_list_view) ;
        footerView = getLayoutInflater().inflate(R.layout.house_list_wait_load, null);
        footerView.findViewById(R.id.house_list_wait_load_relative).setVisibility(View.GONE);
        listView.setOnScrollListener(this);
        startTask() ;
    }

    public void setTitle(String total){
        TextView textView = (TextView)findViewById(R.id.comment_list_count_text_view) ;
        textView.setText(String.format(getString(R.string.comment_count),total));
    }

    public void setHeader(){
        Map<Integer, Object> map = new HashMap<Integer, Object>();
        map.put(header_title, "楼盘点评");
        setHeader(map);
        Map<Integer, Object> map1 = new HashMap<Integer, Object>();
        setHeader(map1);
    }

    public JsonResult<List<CommentListJsonModel>> getCommentList(){
        HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
        Map<String, String> map = new HashMap<String, String>();
        map.put("cityid", Global.NOW_CITY_ID);
        map.put("hid",hid) ;
        if (getPage() != null) {
            map.put("page", String.valueOf(getPage().getPage()));
        } else {
            map.put("page", "1");
        }
        Optional<JsonResult<List<CommentListJsonModel>>> optional = httpClientUtils.getUnsignedListByData(COMMENT_LIST_URL, map, CommentListJsonModel.class);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }


    public void setListData(List<CommentListJsonModel> jsonModelList){

        List<Map<Integer,Object>> mapList = new ArrayList<Map<Integer, Object>>();
        for(CommentListJsonModel tempModel:jsonModelList){
            Map<Integer,Object> tempMap = new HashMap<Integer, Object>() ;
            String image = tempModel.getAvatar() ;
            String userType = tempModel.getUsername();
            String rating = tempModel.getGrade() ;
            String time = tempModel.getPosttime() ;
            String content = tempModel.getTitle() ;
            String trample = tempModel.getDown() ;
            String top = tempModel.getDigg() ;
            String reply = tempModel.getReplynum() ;
            String cid = tempModel.getCid() ;
            tempMap.put(-1,cid);
            tempMap.put(-2,hid) ;
            tempMap.put(COMMENT_IMAGE,image) ;
            tempMap.put(COMMENT_USER_TYPE,userType) ;
            if(rating !=null && ! rating.isEmpty()){
                tempMap.put(COMMENT_RATING,rating) ;
            }else {
                tempMap.put(COMMENT_RATING,"0.0") ;
            }

            if(time != null && !time.isEmpty()){
                tempMap.put(COMMENT_TIME,time);
            } else{
                tempMap.put(COMMENT_TIME,"暂无");
            }
            if(content != null && !content.isEmpty()){
                tempMap.put(COMMENT_CONTENT,content) ;
            } else {
                tempMap.put(COMMENT_CONTENT,"暂无") ;
            }
            if(trample != null && ! trample.isEmpty()){
                tempMap.put(COMMENT_TRAMPLE,"踩"+trample)  ;
            }else {
                tempMap.put(COMMENT_TRAMPLE,"踩0") ;
            }
            if(top != null && !top.isEmpty()){
                tempMap.put(COMMENT_TOP,"顶"+top) ;
            } else {
                tempMap.put(COMMENT_TOP,"顶0") ;
            }
            if(reply != null && !reply.isEmpty()){
                tempMap.put(COMMENT_REPLY,"回复"+reply) ;
            } else {
                tempMap.put(COMMENT_REPLY,"回复0") ;
            }
            mapList.add(tempMap);
        }

        itemAdapter = new ItemAdapter(CommentListActivity.this,R.layout.comment_list_item,mapList) ;
        itemAdapter.setWeek(new WeakReference<Activity>(CommentListActivity.this));
        listView.setAdapter(itemAdapter);

    }

    public  void setNetWorkErrorValue(){
        findViewById(R.id.comment_list_wait_load_relative).setVisibility(View.VISIBLE);
        findViewById(R.id.comment_list_relative_layout).setVisibility(View.GONE);
        findViewById(R.id.comment_list_wait_load__image).setVisibility(View.GONE);
        TextView textView = (TextView)findViewById(R.id.wenda_list_wait_load__content) ;
        textView.setText(Global.ERROR_NET_WORK);
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        Log.d("CommentListActivity->adapter", itemAdapter.getCount() + "");
        int itemsLastIndex = itemAdapter.getCount() - 1;    //数据集最后一项的索引


        int lastIndex = itemsLastIndex + ((ListView) view).getHeaderViewsCount() + ((ListView) view).getFooterViewsCount();
        Log.d("CommentListActivity", "visibleLastIndex==" + visibleLastIndex + "  itemsLastIndex==" + itemsLastIndex);
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
        Log.d("CommentListActivity", "visibleItemCount==" + visibleItemCount + "  firstVisibleItem====" + firstVisibleItem);
    }

    public Map<String,String> sendComment(Map<String,String> param){
        String grade = param.get("grade");
        String info = param.get("info");
        Map<String, String> resultMap = new HashMap<String, String>();
        HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
        Map<String, String> map = new HashMap<String, String>();
        map.put("hid", hid);
        map.put("grade", grade);
        map.put("info", info);
        map.put("cityid", Global.NOW_CITY_ID);
        if (!Global.USER_AUTH_STR.equals("")) {
            map.put("authstr", Global.USER_AUTH_STR);
        }
        Optional<Map<String, String>> optional = httpClientUtils.postSignedMap(COMMENT_URL, map, String.class);
        if (optional.isPresent()) {
            resultMap.put("status", optional.get().get("status"));
            resultMap.put("message", optional.get().get("message"));
            return resultMap;
        }
        return null;
    }

    public void startTask(){
        CommentListAsyncTask commentListAsyncTask = new CommentListAsyncTask();
        commentListAsyncTask.execute();
    }

    public void loadData() {
        int totalPage = getPage().getTotalPage();
        int nextPage = getPage().getNextPage();
        int nowPage = getPage().getPage();
        Log.d("NewHouseDynamicActivity page", totalPage + "");
        Log.d("NewHouseDynamicActivity page", nextPage + "");
        Log.d("NewHouseDynamicActivity page", nowPage + "");
        if (nowPage < totalPage) {
            //设置当前页要加载的页数
            getPage().setPage(nextPage);
            CommentListPageAsyncTask pageAsyncTask = new CommentListPageAsyncTask();
            pageAsyncTask.execute();
        }

        if (nowPage == totalPage && nowPage != 1) {
            CommentListPageAsyncTask pageAsyncTask = new CommentListPageAsyncTask();
            pageAsyncTask.execute();
        }
    }

    public class CommentListPageAsyncTask extends AsyncTask<String, Void, JsonResult<List<CommentListJsonModel>>> {

        @Override
        protected JsonResult<List<CommentListJsonModel>> doInBackground(String... params) {
            return getCommentList();
        }

        @Override
        protected void onPostExecute(JsonResult<List<CommentListJsonModel>> jsonResult) {
            if (jsonResult != null && jsonResult.isStatus()) {
                List<CommentListJsonModel> listJsonModels = jsonResult.getData();
                for (CommentListJsonModel jsonModel : listJsonModels) {
                    Map<Integer, Object> tempMap = new HashMap<Integer, Object>();
                    String image = jsonModel.getAvatar();
                    String userType = jsonModel.getUsername();
                    String rating = jsonModel.getGrade();
                    String time = jsonModel.getPosttime();
                    String content = jsonModel.getTitle();
                    String trample = jsonModel.getDown();
                    String top = jsonModel.getDigg();
                    String reply = jsonModel.getReplynum();
                    String cid = jsonModel.getCid();
                    tempMap.put(-1, cid);
                    tempMap.put(-2, hid);
                    tempMap.put(COMMENT_IMAGE, image);
                    tempMap.put(COMMENT_USER_TYPE, userType);
                    if (rating != null && !rating.isEmpty()) {
                        tempMap.put(COMMENT_RATING, rating);
                    } else {
                        tempMap.put(COMMENT_RATING, "0.0");
                    }

                    if (time != null && !time.isEmpty()) {
                        tempMap.put(COMMENT_TIME, time);
                    } else {
                        tempMap.put(COMMENT_TIME, "暂无");
                    }
                    if (content != null && !content.isEmpty()) {
                        tempMap.put(COMMENT_CONTENT, content);
                    } else {
                        tempMap.put(COMMENT_CONTENT, "暂无");
                    }
                    if (trample != null && !trample.isEmpty()) {
                        tempMap.put(COMMENT_TRAMPLE, "踩" + trample);
                    } else {
                        tempMap.put(COMMENT_TRAMPLE, "踩0");
                    }
                    if (top != null && !top.isEmpty()) {
                        tempMap.put(COMMENT_TOP, "顶" + top);
                    } else {
                        tempMap.put(COMMENT_TOP, "顶0");
                    }
                    if (reply != null && !reply.isEmpty()) {
                        tempMap.put(COMMENT_REPLY, "回复" + reply);
                    } else {
                        tempMap.put(COMMENT_REPLY, "回复0");
                    }
                    itemAdapter.addItem(tempMap);
                    itemAdapter.notifyDataSetChanged();
                }
                if (getPage().getPage() == getPage().getTotalPage()) {
                    getPage().setPage(getPage().getTotalPage() + 1);
                    Log.d("new nowPage", getPage().getPage() + "");
                }
            } else {
                setNetWorkErrorValue();
            }
        }
    }

    public class CommentListAsyncTask extends AsyncTask<String, Void, JsonResult<List<CommentListJsonModel>>> {

        @Override
        protected JsonResult<List<CommentListJsonModel>> doInBackground(String... params) {
            return getCommentList();
        }

        @Override
        protected void onPostExecute(JsonResult<List<CommentListJsonModel>> jsonResult) {
            if (jsonResult != null && jsonResult.isStatus()) {
                if (listView.getFooterViewsCount() == 0 && jsonResult.getData().size() > 9) {
                    listView.addFooterView(footerView);
                }
                Page page = new Page(Integer.valueOf(jsonResult.getTotal()), Global.PAGE_SIZE);
                setPage(page);
                setTitle(jsonResult.getTotal());
                setListData(jsonResult.getData());
            } else if (jsonResult == null) {
                setNetWorkErrorValue();
            }
        }
    }

    public class CommentAsyncTask extends AsyncTask<Map<String, String>, Void, Map<String, String>> {

        @Override
        protected Map<String, String> doInBackground(Map<String, String>... params) {
            return sendComment(params[0]);
        }

        @Override
        protected void onPostExecute(Map<String, String> resultMap) {
            if (resultMap != null) {
                Toast toast = Toast.makeText(getApplicationContext(), resultMap.get("message"), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                EditText editText = (EditText) findViewById(R.id.comment_list_edit_text);
                editText.setText("");
                editText.setHint(getString(R.string.message_body_hint));

            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "网络数据获取失败", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
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
            listView.setSelection(visibleLastIndex); //设置选中项
        }
    }

    public class OnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.details_back:
                    finish();
                    break;
            }
        }
    }



}