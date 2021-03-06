package com.zhijia.ui.zhijiaActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.*;

import com.google.common.base.Optional;
import com.zhijia.Global;
import com.zhijia.service.data.Medol.JsonResult;
import com.zhijia.service.data.Medol.NewsCatModel;
import com.zhijia.service.data.Medol.NewsItemModel;
import com.zhijia.service.network.HttpClientUtils;
import com.zhijia.ui.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 新闻首页
 */
public class NewsListActivity extends CommonActivity {

    private static String NEWS_CAT_URL = Global.API_WEB_URL + "/news/cat";
    private static String NEWS_LIST_URL = Global.API_WEB_URL + "/news/list";
    private static String NEWS_SEARCH_LIST_URL = Global.API_WEB_URL + "/news/search";

    //新闻分类组
    private RadioGroup mRadioGroup;
    private RadioButton mRadioButton;

    private List<NewsItemModel> dataList = new ArrayList<NewsItemModel>();

    private ImageView imageView;

    private float mCurrentCheckedRadioLeft = 0.0f;

    private HorizontalScrollView mHorizontalScrollView;

    //可见视图最后的一个索引
    private int visibleLastIndex = 0, currentPage = 1;
    //当前窗口可见项总数
    private int visibleItemCount;
    private String catid = "";
    private String catName = "";

    private ListView listView;

    private View loadingView;

    private String keyword = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_list);
        this.setTopTitle(String.format(getString(R.string.news), Global.NOW_CITY), R.id.common_two_view_text);
        this.setOnClickListener(this);
        keyword = getIntent().getStringExtra("keyword");
        initController();
        GetNewsCatListAsyncTask getNewsCatListAsyncTask = new GetNewsCatListAsyncTask();
        getNewsCatListAsyncTask.execute();
    }

   /* private void InitImageView() {  
        imageView= (ImageView) findViewById(R.id.cursor);  
        bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.a).getWidth();// 获取图片宽度  
        DisplayMetrics dm = new DisplayMetrics();  
        getWindowManager().getDefaultDisplay().getMetrics(dm);  
        int screenW = dm.widthPixels;// 获取分辨率宽度  
        offset = (screenW / 3 - bmpW) / 2;// 计算偏移量  
        Matrix matrix = new Matrix();  
        matrix.postTranslate(offset, 0);  
        imageView.setImageMatrix(matrix);// 设置动画初始位置  
    } */ 
    private void setAnimation(AnimationSet animationSet, int index) {
        Log.d(Global.LOG_TAG, String.valueOf(index * getResources().getDimension(R.dimen.move_news)));
        Log.d(Global.LOG_TAG, "mCurrentCheckedRadioLeft==" + mCurrentCheckedRadioLeft);
        TranslateAnimation translateAnimation = new TranslateAnimation(mCurrentCheckedRadioLeft, (index * getResources().getDimension(R.dimen.move_news)), 0f, 0f);
        animationSet.addAnimation(translateAnimation);
        //起始位置不需要填充效果。这个必须设置false 要不容易出现闪动
       // animationSet.setFillBefore(false);
        //保留在当前得位置上
        animationSet.setFillAfter(true);
        animationSet.setFillEnabled(true);
        animationSet.setDuration(50);
        imageView.startAnimation(animationSet);
        mCurrentCheckedRadioLeft = (index * getResources().getDimension(R.dimen.rdo2));
        Log.d(Global.LOG_TAG, "after mCurrentCheckedRadioLeft==" + mCurrentCheckedRadioLeft);
        mHorizontalScrollView.smoothScrollTo((int) mCurrentCheckedRadioLeft - (int) getResources().getDimension(R.dimen.move_news), 0);
    }

    /**
     * 初始化控制器
     */
    private void initController() {
    	mRadioButton=(RadioButton)findViewById(R.id.btn1);
        mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        listView = (ListView) findViewById(R.id.news_list);
        listView.setOnScrollListener(new OnScrollListener());
        loadingView = findViewById(R.id.loading_relative);
        imageView = (ImageView) findViewById(R.id.img1);
        mHorizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
    }

    /**
     * 获得分类信息
     *
     * @return
     */
    private Map<String, Object> getNewsCat() {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
        Map<String, String> map = new HashMap<String, String>();
        map.put("cityid", Global.NOW_CITY_ID);
        Optional<JsonResult<List<NewsCatModel>>> optional = httpClientUtils.getUnsignedListByData(NEWS_CAT_URL, map, NewsCatModel.class);
        if (optional.isPresent()) {
            resultMap.put("status", String.valueOf(optional.get().isStatus()));
            resultMap.put("message", optional.get().getMessage());
            if (optional.get().getData() != null) {
                resultMap.put("catList", optional.get().getData());
            }
        }

        return resultMap;
    }

    public class OnScrollListener implements AbsListView.OnScrollListener {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

            int itemsLastIndex = listView.getAdapter().getCount() - 1;    //数据集最后一项的索引
            Log.d("itemsLastIndex", "itemsLastIndex===" + itemsLastIndex);
            //加上header和footer view的数量。
            int lastIndex = itemsLastIndex + ((ListView) view).getHeaderViewsCount() + ((ListView) view).getFooterViewsCount();
            Log.d("meessage-> lastIndex", lastIndex + ": visibleLastIndex==" + visibleLastIndex);
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && visibleLastIndex == lastIndex) {
                loadingView.setVisibility(View.VISIBLE);
                Log.d("footerView", "footerView end");
                GetNewsPageAsyncTask getNewsPageAsyncTask = new GetNewsPageAsyncTask();
                getNewsPageAsyncTask.execute(catid, String.valueOf(currentPage));
            } else {
                loadingView.setVisibility(View.GONE);
            }

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int _visibleItemCount, int totalItemCount) {
            visibleItemCount = _visibleItemCount;
            visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
        }
    }

    class GetNewsCatListAsyncTask extends AsyncTask<String, Void, Map<String, Object>> {
        @Override
        protected Map<String, Object> doInBackground(String... params) {
            return getNewsCat();
        }

        @Override
        protected void onPostExecute(Map<String, Object> resultMap) {
            super.onPostExecute(resultMap);

            if (resultMap.size() > 0 && resultMap.get("status").equals("true")) {//成功
                final List<NewsCatModel> resultList = (List<NewsCatModel>) resultMap.get("catList");
                if (resultList.size() > 0) {
                    findViewById(R.id.news_list_wait_load_relative).setVisibility(View.GONE);
                    findViewById(R.id.news_list_linear_layout).setVisibility(View.VISIBLE);
                    for (int i = 0; i < resultList.size(); i++) {
                        final int j = i;
                        final NewsCatModel cat = resultList.get(i);

                        RadioButton radioButton = (RadioButton) LayoutInflater.from(getApplicationContext()).inflate(R.layout.news_navigation_radio_button, null);
                        radioButton.setText(cat.getCname());
                        
                        System.out.println("radioButton:"+cat.getCname());
                        radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                Log.d(Global.LOG_TAG, String.valueOf(isChecked));
                                Log.d(Global.LOG_TAG, String.valueOf(j));
                                if (isChecked) {
                                    AnimationSet animationSet = new AnimationSet(true);
                                    setAnimation(animationSet, j);
                                    currentPage = 1;
                                    visibleLastIndex = 0;
                                    catid = cat.getCatid();
                                    catName = cat.getCname();
                                    dataList = new ArrayList<NewsItemModel>();
                                    keyword = null;
                                    listView.setAdapter(new NewsListAdapter(getApplicationContext(), dataList));//数据集变化后,通知adapter
                                    GetNewsPageAsyncTask getNewsPageAsyncTask = new GetNewsPageAsyncTask();
                                    getNewsPageAsyncTask.execute(catid, String.valueOf(currentPage));
                                }
                            }
                        });

                        mRadioGroup.addView(radioButton);
                        if ((i + 1) < resultList.size()) {
                            ImageView splitImageView = (ImageView) LayoutInflater.from(getApplicationContext()).inflate(R.layout.news_navigation_v_split_line, null);
                            mRadioGroup.addView(splitImageView);
                        }
                    }

                    if (keyword != null) {//搜索跳过来的
                        GetNewsPageAsyncTask getNewsPageAsyncTask = new GetNewsPageAsyncTask();
                        getNewsPageAsyncTask.execute(catid, String.valueOf(currentPage));
                    } else {
                        ((RadioButton) mRadioGroup.getChildAt(0)).setChecked(true);
                        catid = resultList.get(0).getCatid();
                        catName = resultList.get(0).getCname();
                    }
                } else {
                    findViewById(R.id.news_list_house_top).setVisibility(View.VISIBLE);
                    setTopTitle(String.format(getString(R.string.news), Global.NOW_CITY), R.id.gone_common_two_view_text);
                    findViewById(R.id.gone_common_two_view_back).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            NewsListActivity.this.finish();
                        }
                    });
                    ((TextView)findViewById(R.id.news_list_wait_load__content)).setText("您所在的城市：" + Global.NOW_CITY + " 暂时没有资讯栏目。");
                    //Toast.makeText(getApplicationContext(), "您所在的城市：" + Global.NOW_CITY + " 暂时没有资讯栏目。", Toast.LENGTH_SHORT).show();
                }

            } else {
                findViewById(R.id.news_list_house_top).setVisibility(View.VISIBLE);
                setTopTitle(String.format(getString(R.string.news), Global.NOW_CITY), R.id.gone_common_two_view_text);
                findViewById(R.id.gone_common_two_view_back).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NewsListActivity.this.finish();
                    }
                });
                ((TextView)findViewById(R.id.news_list_wait_load__content)).setText("您所在的城市：" + Global.NOW_CITY + " 暂时没有资讯栏目。");
                //Toast.makeText(getApplicationContext(), (String) resultMap.get("message"), Toast.LENGTH_SHORT).show();
            }
        }
    }

    class GetNewsPageAsyncTask extends AsyncTask<String, Void, JsonResult<List<NewsItemModel>>> {

        @Override
        protected JsonResult<List<NewsItemModel>> doInBackground(String... params) {
            HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
            Map<String, String> map = new HashMap<String, String>();
            map.put("cityid", Global.NOW_CITY_ID);
            map.put("catid", params[0]);
            map.put("page", params[1]);
            map.put("keyword", keyword);
            Optional<JsonResult<List<NewsItemModel>>> optional;

            if (keyword == null) {
                optional = httpClientUtils.getUnsignedListByData(NEWS_LIST_URL, map, NewsItemModel.class);
            } else {
                optional = httpClientUtils.getUnsignedListByData(NEWS_SEARCH_LIST_URL, map, NewsItemModel.class);
            }

            if (optional.isPresent() && optional.get().getData() != null && optional.get().getData().size() > 0) {
                currentPage++;
                return optional.get();
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(JsonResult<List<NewsItemModel>> listJsonResult) {
            loadingView.setVisibility(View.GONE);
            if (listJsonResult != null && String.valueOf(listJsonResult.isStatus()).equalsIgnoreCase("true")) {//成功
                List<NewsItemModel> tempList = listJsonResult.getData();
                if (tempList != null) {
                    dataList.addAll(tempList);
                    listView.setAdapter(new NewsListAdapter(getApplicationContext(), dataList));//数据集变化后,通知adapter
                    listView.setSelection(visibleLastIndex - visibleItemCount + 1); //设置选中项
                }
            }
        }
    }

    private class NewsListAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private List<NewsItemModel> list;

        public NewsListAdapter(Context context, List<NewsItemModel> list) {
            this.inflater = LayoutInflater.from(context);
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final NewsItemModel tempObj = list.get(position);
            LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.news_item, null);
            TextView title = (TextView) linearLayout.findViewById(R.id.news_title);
            title.setText(tempObj.getTitle());
            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent newsDetailIntent = new Intent(NewsListActivity.this, NewsDetailActivity.class);
                    if (keyword == null) {
                        newsDetailIntent.putExtra("newsTypeName", catName);
                    } else {
                        newsDetailIntent.putExtra("newsTypeName", getString(R.string.search));
                    }
                    newsDetailIntent.putExtra("url", tempObj.getUrl());
                    startActivity(newsDetailIntent);
                }
            });

            return linearLayout;
        }
    }
}
