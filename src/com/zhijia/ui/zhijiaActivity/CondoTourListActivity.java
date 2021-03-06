package com.zhijia.ui.zhijiaActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.google.common.base.Optional;
import com.zhijia.Global;
import com.zhijia.service.data.Medol.CondoTourItemModel;
import com.zhijia.service.data.Medol.HouseModel;
import com.zhijia.service.data.Medol.JsonResult;
import com.zhijia.service.network.HttpClientUtils;
import com.zhijia.ui.R;
import com.zhijia.util.DownloadImageTask;
import com.zhijia.util.ScreenUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 看房团列表
 */
public class CondoTourListActivity extends Activity {

    private static String GROUP_LIST_URL = Global.API_WEB_URL + "/xinfang/kanfang";
    private static String FIND_HOUSE_URL = Global.API_WEB_URL + "/common/letter";

    private RelativeLayout condoTourListLayout;

    private ListView listView;

    private View loadingView;

    //可见视图最后的一个索引
    private int visibleLastIndex = 0, currentPage = 1;
    //当前窗口可见项总数
    private int visibleItemCount;

    private List<CondoTourItemModel> dataList = new ArrayList<CondoTourItemModel>();

    //无数据页的元素
    private ListView selectResultListView;
    private TextView intentionHouse;

    private boolean isSetText = false;
    private String intentionHouseId = "";

    //点击事件的侦听
    private ClickListener clickListener = new ClickListener();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.condo_tour_list);

        condoTourListLayout = (RelativeLayout) findViewById(R.id.condo_tour_list_layout);
        loadingView = findViewById(R.id.loading_relative);
        listView = (ListView) findViewById(R.id.condo_tour_list);
        listView.setOnScrollListener(new OnScrollListener());

        findViewById(R.id.back).setOnClickListener(clickListener);
        findViewById(R.id.condo_tour_must_know).setOnClickListener(clickListener);

        //loadCurrentView();
        CondoTourItemPageAsyncTask condoTourItemPageAsyncTask = new CondoTourItemPageAsyncTask();
        condoTourItemPageAsyncTask.execute(String.valueOf(currentPage));
    }

    /**
     * 获得信息
     * @return
     */
    private Map<String, Object> findHouseInfo(String keyword) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
        Map<String, String> map = new HashMap<String, String>();
        map.put("housetype", "1");
        map.put("cityid", Global.NOW_CITY_ID);
        map.put("key", keyword);
        Optional<JsonResult<List<HouseModel>>> optional = httpClientUtils.getUnsignedListByData(FIND_HOUSE_URL, map, HouseModel.class);
        if (optional.isPresent()) {
            resultMap.put("status", String.valueOf(optional.get().isStatus()));
            resultMap.put("message", optional.get().getMessage());
            if (optional.get().getData() != null) {
                resultMap.put("houseList", optional.get().getData());
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
                CondoTourItemPageAsyncTask condoTourItemPageAsyncTask = new CondoTourItemPageAsyncTask();
                condoTourItemPageAsyncTask.execute(String.valueOf(currentPage));
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

    //点击事件的公共类
    class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.back: //后退
                    finish();
                    break;
                case R.id.condo_tour_must_know: //看房须知
                    Intent mustKnowIntent = new Intent(view.getContext(), CondoTourMustKnowActivity.class);
                    startActivity(mustKnowIntent);
                    break;
            }
        }
    }

    /**
     * 当ViewPager中页面的状态发生改变时调用
     */
    class SplashImageListener implements ViewPager.OnPageChangeListener {

        private int oldPosition = 0;

        // 图片标题正文的那些点
        private List<View> dotsList;

        SplashImageListener(List<View> dotsList) {
            this.dotsList = dotsList;
        }

        public void onPageSelected(int position) {
            dotsList.get(oldPosition).setBackgroundResource(R.drawable.index_top_dot_unselected);
            dotsList.get(position).setBackgroundResource(R.drawable.index_top_dot_selected);
            oldPosition = position;
        }

        public void onPageScrollStateChanged(int state) {

        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }
    }

    class CondoTourItemPageAsyncTask extends AsyncTask<String, Void, JsonResult<List<CondoTourItemModel>>> {
        private String page;

        @Override
        protected JsonResult<List<CondoTourItemModel>> doInBackground(String... params) {
            page = params[0];
            HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
            Map<String, String> map = new HashMap<String, String>();
            map.put("cityid", Global.NOW_CITY_ID);
            map.put("page", page.toString());
            Optional<JsonResult<List<CondoTourItemModel>>> optional = httpClientUtils.getUnsignedListByData(GROUP_LIST_URL, map, CondoTourItemModel.class);

            if (optional == null) {
                return null;
            }

            if (optional.isPresent() && optional.get().getData() != null) {
                return optional.get();
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(JsonResult<List<CondoTourItemModel>> listJsonResult) {
            loadingView.setVisibility(View.GONE);
            if (listJsonResult != null && String.valueOf(listJsonResult.isStatus()).equalsIgnoreCase("true")) {//成功
                if (listJsonResult.getData() != null && listJsonResult.getData().size() > 0) {
                    currentPage++;

                    List<CondoTourItemModel> tempList = listJsonResult.getData();
                    dataList.addAll(tempList);
                    listView.setAdapter(new CondoTourItemListAdapter(getApplicationContext(), dataList));//数据集变化后,通知adapter
                    listView.setSelection(visibleLastIndex - visibleItemCount + 1); //设置选中项
                } else if (currentPage == 1) {//第一页就没有数据
                    condoTourListLayout.removeAllViews();
                    LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.condo_tour_list_no_data, null);

                    selectResultListView = (ListView) linearLayout.findViewById(R.id.select_result_list);
                    intentionHouse = (TextView) linearLayout.findViewById(R.id.input_intention_house);
                    intentionHouse.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                            if (isSetText || charSequence.length() == 0) {
                                selectResultListView.setVisibility(View.GONE);
                            } else {
                                FindHouseListAsyncTask findHouseListAsyncTask = new FindHouseListAsyncTask();
                                findHouseListAsyncTask.execute(charSequence.toString().trim());
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });

                    TextView immediatelyApply = (TextView) linearLayout.findViewById(R.id.immediately_apply_no_data);
                    immediatelyApply.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (intentionHouseId.equalsIgnoreCase("")) {
                                Toast.makeText(getApplicationContext(), getString(R.string.input_intention_house_hint), Toast.LENGTH_SHORT).show();
                                intentionHouse.requestFocus();
                            } else {
                                Intent immediatelyApplyIntent = new Intent(view.getContext(), CondoTourApplyActivity.class);
                                immediatelyApplyIntent.putExtra("source", 2);
                                immediatelyApplyIntent.putExtra("id", intentionHouseId);
                                startActivity(immediatelyApplyIntent);
                            }
                        }
                    });

                    condoTourListLayout.addView(linearLayout);
                }
            } else {
                Toast.makeText(CondoTourListActivity.this, Global.ERROR_NET_WORK, Toast.LENGTH_SHORT).show();
            }
        }
    }

    class FindHouseListAsyncTask extends AsyncTask<String, Void, Map<String, Object>> {
        @Override
        protected Map<String, Object> doInBackground(String... params) {
            return findHouseInfo(params[0]);
        }

        @Override
        protected void onPostExecute(Map<String, Object> resultMap) {
            super.onPostExecute(resultMap);

            if (resultMap.size() > 0 && resultMap.get("status").equals("true")) {//成功
                final List<HouseModel> resultList = (List<HouseModel>) resultMap.get("houseList");

                if (resultList.size() > 0) {
                    selectResultListView.setAdapter(new AllHouseListAdapter(CondoTourListActivity.this, resultList));
                    selectResultListView.setVisibility(View.VISIBLE);
                    selectResultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            isSetText = true;
                            intentionHouse.setText(resultList.get(position).getName());
                            intentionHouseId = resultList.get(position).getHid();
                            //主要是防止再次提示
                            isSetText = false;
                        }
                    });
                } else {
                    selectResultListView.setVisibility(View.GONE);
                }
            } else {
                Toast.makeText(getApplicationContext(), (String) resultMap.get("message"), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 全部房子的Adapter
     */
    private class AllHouseListAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private List<HouseModel> list;

        public AllHouseListAdapter(Context context, List<HouseModel> list) {
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
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.adapter_all_user_item, null);
                holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.user_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.name.setText(list.get(position).getName());

            return convertView;
        }

        private class ViewHolder {
            TextView name;
        }

    }

    private class CondoTourItemListAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private List<CondoTourItemModel> list;

        public CondoTourItemListAdapter(Context context, List<CondoTourItemModel> list) {
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
            final CondoTourItemModel tempObj = list.get(position);
            LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.condo_tour_item, null);
            TextView title = (TextView) linearLayout.findViewById(R.id.title);
            title.setText("[" + tempObj.getLooktime() + "] " + tempObj.getTitle());
            if (tempObj.getType().equalsIgnoreCase("line") && tempObj.getLine() != null && tempObj.getLine().size() > 0) {
                linearLayout.findViewById(R.id.condo_tour_privilege_layout).setVisibility(View.GONE);
                linearLayout.findViewById(R.id.condo_tour_price_layout).setVisibility(View.GONE);

                LinearLayout condoTourHouseLayout = (LinearLayout) linearLayout.findViewById(R.id.condo_tour_house_layout);
                for (CondoTourItemModel.Line line : tempObj.getLine()) {
                    RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.condo_tour_house_item, null);
                    TextView condoTourHouseTitle = (TextView) relativeLayout.findViewById(R.id.title);
                    condoTourHouseTitle.setText(line.getName());
                    TextView condoTourHousePrice = (TextView) relativeLayout.findViewById(R.id.price);
                    condoTourHousePrice.setText(line.getAverageprice());
                    TextView condoTourHousePrivilegeDesc = (TextView) relativeLayout.findViewById(R.id.privilege_desc);
                    condoTourHousePrivilegeDesc.setText(line.getSpecial());

                    condoTourHouseLayout.addView(relativeLayout);
                }
            } else {
                LinearLayout condoTourHouseLayout = (LinearLayout) linearLayout.findViewById(R.id.condo_tour_house_layout);
                condoTourHouseLayout.setVisibility(View.GONE);
                TextView condoTourPrivilegeDesc = (TextView) linearLayout.findViewById(R.id.condo_tour_privilege_desc);
                condoTourPrivilegeDesc.setText(tempObj.getPrivilege());
                TextView condoTourPriceDesc = (TextView) linearLayout.findViewById(R.id.condo_tour_price_desc);
                condoTourPriceDesc.setText(tempObj.getAverageprice());
            }
            TextView condoTourRecommend = (TextView) linearLayout.findViewById(R.id.condo_tour_recommend);
            condoTourRecommend.setText(tempObj.getRecommend());
            TextView condoTourArea = (TextView) linearLayout.findViewById(R.id.condo_tour_area);
            condoTourArea.setText(tempObj.getAddress());
            TextView condoTourApplyCountDesc = (TextView) linearLayout.findViewById(R.id.condo_tour_apply_count_desc);
            condoTourApplyCountDesc.setText(tempObj.getNums());

            //初始化被轮播的图片View
            List<ImageView> imageViews = new ArrayList<ImageView>();
            if (tempObj.getPic() == null || tempObj.getPic().size() == 0) {//没有图片
                ImageView imageView = new ImageView(linearLayout.getContext());
                imageView.setImageResource(R.drawable.a);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent condoTourDetailIntent = new Intent(view.getContext(), CondoTourDetailActivity.class);
                        condoTourDetailIntent.putExtra("source", 2);
                        condoTourDetailIntent.putExtra("id", tempObj.getId());
                        startActivity(condoTourDetailIntent);
                    }
                });
                imageViews.add(imageView);
            } else {
                // 初始化图片资源
                for (String imgURL : tempObj.getPic()) {
                    final ImageView imageView = new ImageView(getApplicationContext());
                    Log.d("CondoTourListActivity->imageUrl", imgURL);
                    new DownloadImageTask().doInBackground(imgURL, imageView, R.drawable.a);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent condoTourDetailIntent = new Intent(view.getContext(), CondoTourDetailActivity.class);
                            condoTourDetailIntent.putExtra("source", 2);
                            condoTourDetailIntent.putExtra("id", tempObj.getId());
                            startActivity(condoTourDetailIntent);
                        }
                    });
                    imageViews.add(imageView);
                }
            }

            //初始化轮播图的小圆点
            LinearLayout dotsLayout = (LinearLayout) linearLayout.findViewById(R.id.dots_layout);
            List<View> dotsList = new ArrayList<View>();
            for (int i = 0; i < imageViews.size(); i++) {
                View dotView = new View(linearLayout.getContext());
                if (i == 0) {
                    dotView.setBackgroundResource(R.drawable.index_top_dot_selected);
                } else {
                    dotView.setBackgroundResource(R.drawable.index_top_dot_unselected);
                }
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ScreenUtil.dps2pixels(5, linearLayout.getContext())
                        , ScreenUtil.dps2pixels(5, linearLayout.getContext()));
                params.setMargins(ScreenUtil.dps2pixels(1.5, linearLayout.getContext())
                        , ScreenUtil.dps2pixels(15, linearLayout.getContext())
                        , ScreenUtil.dps2pixels(1.5, linearLayout.getContext())
                        , ScreenUtil.dps2pixels(15, linearLayout.getContext()));
                dotView.setLayoutParams(params);
                dotsList.add(dotView);
                dotsLayout.addView(dotView);
            }

            ViewPager viewPager = (ViewPager) linearLayout.findViewById(R.id.condo_tour_view_pager);
            // 设置填充ViewPager页面的适配器
            viewPager.setAdapter(new SplashImageAdapter(imageViews));
            // 设置一个监听器，当ViewPager中的页面改变时调用
            viewPager.setOnPageChangeListener(new SplashImageListener(dotsList));

            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent condoTourDetailIntent = new Intent(view.getContext(), CondoTourDetailActivity.class);
                    condoTourDetailIntent.putExtra("source", 2);
                    condoTourDetailIntent.putExtra("id", tempObj.getId());
                    startActivity(condoTourDetailIntent);
                }
            });
            linearLayout.findViewById(R.id.immediately_apply).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent immediatelyApplyIntent = new Intent(view.getContext(), CondoTourApplyActivity.class);
                    immediatelyApplyIntent.putExtra("source", 2);
                    immediatelyApplyIntent.putExtra("id", tempObj.getId());
                    startActivity(immediatelyApplyIntent);
                }
            });

            return linearLayout;
        }
    }

    /**
     * 填充ViewPager页面的适配器
     */
    class SplashImageAdapter extends PagerAdapter {

        private List<ImageView> imageViewList;

        SplashImageAdapter(List<ImageView> imageViews) {
            imageViewList = imageViews;
        }

        @Override
        public int getCount() {
            return imageViewList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = imageViewList.get(position);
            ((ViewPager) container).addView(view);
            return imageViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }
    }
}