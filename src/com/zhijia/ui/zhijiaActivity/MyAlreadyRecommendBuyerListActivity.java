package com.zhijia.ui.zhijiaActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.zhijia.service.data.Medol.RecommendBuyerModel;
import com.zhijia.service.network.HttpClientUtils;
import com.zhijia.ui.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 已推荐买房人列表
 */
public class MyAlreadyRecommendBuyerListActivity extends CommonActivity implements RadioGroup.OnCheckedChangeListener {

    private static String RECOMMEND_BUYER_LIST_URL = Global.API_WEB_URL + "/member/zhuanlist";

    //分类组
    private RadioGroup mRadioGroup;

    //分类，1:待联系，2：已联系 3：已到访 4：已成交
    private String[] menu = {"全部", "待联系", "已联系", "已到访", "已成交"};

    private ListView listView;

    private View loadingView;

    private RadioButton mRadioButton1;
    private RadioButton mRadioButton2;
    private RadioButton mRadioButton3;
    private RadioButton mRadioButton4;
    private RadioButton mRadioButton5;

    private ImageView imageView;

    private float mCurrentCheckedRadioLeft;

    private HorizontalScrollView mHorizontalScrollView;

    //可见视图最后的一个索引
    private int visibleLastIndex = 0, currentPage = 1;
    //当前窗口可见项总数
    private int visibleItemCount;
    private String tag = "";

    private List<RecommendBuyerModel> dataList = new ArrayList<RecommendBuyerModel>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_already_recommend_buyer);

        this.setTopTitle(getString(R.string.already_recommend_buyer), R.id.common_two_view_text);

        this.setOnClickListener(this);

        initController();
        initListener();
        mRadioButton1.setChecked(true);

        AlreadyRecommendBuyerPageAsyncTask alreadyRecommendBuyerPageAsyncTask = new AlreadyRecommendBuyerPageAsyncTask();
        alreadyRecommendBuyerPageAsyncTask.execute(String.valueOf(currentPage));

        mCurrentCheckedRadioLeft = getCurrentCheckedRadioLeft();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        //应用多个动画
        AnimationSet animationSet = new AnimationSet(true);
        Log.d(Global.LOG_TAG, "checkedId==" + checkedId);
        if (checkedId == R.id.btn1) {
            setAnimation(animationSet, R.dimen.rdo1);
            currentPage = 1;
            tag = "";
            dataList = new ArrayList<RecommendBuyerModel>();
            listView.removeAllViewsInLayout();
            AlreadyRecommendBuyerPageAsyncTask alreadyRecommendBuyerPageAsyncTask = new AlreadyRecommendBuyerPageAsyncTask();
            alreadyRecommendBuyerPageAsyncTask.execute(String.valueOf(currentPage));
        } else if (checkedId == R.id.btn2) {
            setAnimation(animationSet, R.dimen.rdo2);
            currentPage = 1;
            tag = "1";
            dataList = new ArrayList<RecommendBuyerModel>();
            listView.removeAllViewsInLayout();
            AlreadyRecommendBuyerPageAsyncTask alreadyRecommendBuyerPageAsyncTask = new AlreadyRecommendBuyerPageAsyncTask();
            alreadyRecommendBuyerPageAsyncTask.execute(String.valueOf(currentPage));
        } else if (checkedId == R.id.btn3) {
            setAnimation(animationSet, R.dimen.rdo3);
            currentPage = 1;
            tag = "2";
            dataList = new ArrayList<RecommendBuyerModel>();
            listView.removeAllViewsInLayout();
            AlreadyRecommendBuyerPageAsyncTask alreadyRecommendBuyerPageAsyncTask = new AlreadyRecommendBuyerPageAsyncTask();
            alreadyRecommendBuyerPageAsyncTask.execute(String.valueOf(currentPage));
        } else if (checkedId == R.id.btn4) {
            setAnimation(animationSet, R.dimen.rdo4);
            currentPage = 1;
            tag = "3";
            dataList = new ArrayList<RecommendBuyerModel>();
            listView.removeAllViewsInLayout();
            AlreadyRecommendBuyerPageAsyncTask alreadyRecommendBuyerPageAsyncTask = new AlreadyRecommendBuyerPageAsyncTask();
            alreadyRecommendBuyerPageAsyncTask.execute(String.valueOf(currentPage));
        } else if (checkedId == R.id.btn5) {
            setAnimation(animationSet, R.dimen.rdo5);
            currentPage = 1;
            tag = "4";
            dataList = new ArrayList<RecommendBuyerModel>();
            listView.removeAllViewsInLayout();
            AlreadyRecommendBuyerPageAsyncTask alreadyRecommendBuyerPageAsyncTask = new AlreadyRecommendBuyerPageAsyncTask();
            alreadyRecommendBuyerPageAsyncTask.execute(String.valueOf(currentPage));
        }
        //mCurrentCheckedRadioLeft = getCurrentCheckedRadioLeft();

        //mHorizontalScrollView.smoothScrollTo((int) mCurrentCheckedRadioLeft - (int) getResources().getDimension(R.dimen.rdo2), 0);
    }

    private void setAnimation(AnimationSet animationSet, int dimensionId) {
        Log.d(Global.LOG_TAG, String.valueOf(getResources().getDimension(dimensionId)));
        Log.d(Global.LOG_TAG, "mCurrentCheckedRadioLeft==" + mCurrentCheckedRadioLeft);
        TranslateAnimation translateAnimation = new TranslateAnimation(mCurrentCheckedRadioLeft, getResources().getDimension(dimensionId), 0f, 0f);
        animationSet.addAnimation(translateAnimation);
        //起始位置不需要填充效果。这个必须设置false 要不容易出现闪动
        //animationSet.setFillBefore(false);
        //保留在当前得位置上
        animationSet.setFillAfter(true);
        animationSet.setFillEnabled(true);
        animationSet.setDuration(50);
        imageView.startAnimation(animationSet);
        mCurrentCheckedRadioLeft = getCurrentCheckedRadioLeft();

        Log.d(Global.LOG_TAG, "after mCurrentCheckedRadioLeft==" + mCurrentCheckedRadioLeft);
        mHorizontalScrollView.smoothScrollTo((int) mCurrentCheckedRadioLeft - (int) getResources().getDimension(R.dimen.rdo2), 0);
    }

    /**
     * 初始化监听器
     */
    public void initListener() {
        mRadioGroup.setOnCheckedChangeListener(this);
    }

    /**
     * 初始化控制器
     */
    private void initController() {
        mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        loadingView = findViewById(R.id.loading_relative);
        listView = (ListView) findViewById(R.id.already_recommend_list);
        listView.setOnScrollListener(new OnScrollListener());

        mRadioButton1 = (RadioButton) findViewById(R.id.btn1);
        mRadioButton1.setText(menu[0]);
        mRadioButton2 = (RadioButton) findViewById(R.id.btn2);
        mRadioButton2.setText(menu[1]);
        mRadioButton3 = (RadioButton) findViewById(R.id.btn3);
        mRadioButton3.setText(menu[2]);
        mRadioButton4 = (RadioButton) findViewById(R.id.btn4);
        mRadioButton4.setText(menu[3]);
        mRadioButton5 = (RadioButton) findViewById(R.id.btn5);
        mRadioButton5.setText(menu[4]);

        imageView = (ImageView) findViewById(R.id.img1);
        mHorizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
    }

    private float getCurrentCheckedRadioLeft() {
        if (mRadioButton1.isChecked()) {
            return getResources().getDimension(R.dimen.rdo1);
        } else if (mRadioButton2.isChecked()) {
            return getResources().getDimension(R.dimen.rdo2);
        } else if (mRadioButton3.isChecked()) {
            return getResources().getDimension(R.dimen.rdo3);
        } else if (mRadioButton4.isChecked()) {
            return getResources().getDimension(R.dimen.rdo4);
        } else if (mRadioButton5.isChecked()) {
            return getResources().getDimension(R.dimen.rdo5);
        }
        return 0f;
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
                AlreadyRecommendBuyerPageAsyncTask alreadyRecommendBuyerPageAsyncTask = new AlreadyRecommendBuyerPageAsyncTask();
                alreadyRecommendBuyerPageAsyncTask.execute(String.valueOf(currentPage));
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

    class AlreadyRecommendBuyerPageAsyncTask extends AsyncTask<String, Void, JsonResult<List<RecommendBuyerModel>>> {
        private String page;

        @Override
        protected JsonResult<List<RecommendBuyerModel>> doInBackground(String... params) {
            page = params[0];
            if (!Global.USER_AUTH_STR.equals("")) {
                HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
                Map<String, String> map = new HashMap<String, String>();
                map.put("cityid", Global.NOW_CITY_ID);
                map.put("authstr", Global.USER_AUTH_STR);
                map.put("page", page.toString());
                map.put("tag", tag);
                Optional<JsonResult<List<RecommendBuyerModel>>> optional = httpClientUtils.getUnsignedListByData(RECOMMEND_BUYER_LIST_URL, map, RecommendBuyerModel.class);
                if (optional.isPresent() && optional.get().getData() != null && optional.get().getData().size() > 0) {
                    currentPage++;
                    return optional.get();
                } else {
                    return null;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(JsonResult<List<RecommendBuyerModel>> listJsonResult) {
            loadingView.setVisibility(View.GONE);
            if (listJsonResult != null && String.valueOf(listJsonResult.isStatus()).equalsIgnoreCase("true")) {//成功
                if (Integer.parseInt(listJsonResult.getTotal()) > 0 && listJsonResult.getData() != null) {
                    List<RecommendBuyerModel> tempList = listJsonResult.getData();
                    if (tempList != null) {
                        dataList.addAll(tempList);
                        listView.setAdapter(new MyRecommendBuyerModelListAdapter(getApplicationContext(), dataList));//数据集变化后,通知adapter
                        listView.setSelection(visibleLastIndex - visibleItemCount + 1); //设置选中项
                    }
                }
            }
        }
    }

    private class MyRecommendBuyerModelListAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private List<RecommendBuyerModel> list;

        public MyRecommendBuyerModelListAdapter(Context context, List<RecommendBuyerModel> list) {
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
                convertView = inflater.inflate(R.layout.my_already_recommend_buyer_item, parent, false);
                holder = new ViewHolder();

                holder.relativeLayout = (RelativeLayout) convertView;
                holder.adapter_content_one = (TextView) convertView.findViewById(R.id.adapter_content_one);
                holder.adapter_content_two = (TextView) convertView.findViewById(R.id.adapter_content_two);
                holder.adapter_content_three = (TextView) convertView.findViewById(R.id.adapter_content_three);
                holder.adapter_content_four = (TextView) convertView.findViewById(R.id.adapter_content_four);
                holder.adapter_content_five = (TextView) convertView.findViewById(R.id.adapter_content_five);
                holder.stateImage = (TextView) convertView.findViewById(R.id.adapter_image_one);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final RecommendBuyerModel temp = list.get(position);

            holder.adapter_content_one.setText(temp.getTruename());
            holder.adapter_content_two.setText(temp.getMobile());
            holder.adapter_content_three.setText(temp.getHouse());
            holder.adapter_content_four.setText(temp.getAmount());
            holder.adapter_content_five.setText(temp.getPosttime());

            switch (Integer.parseInt(temp.getTag().getTagStatus())) {//0:蓝色,1:绿色,2:红色]
                case 0:
                    holder.relativeLayout.findViewById(R.id.deal_house).setVisibility(View.GONE);
                    holder.adapter_content_three.setVisibility(View.GONE);
                    holder.relativeLayout.findViewById(R.id.commission_amount).setVisibility(View.GONE);
                    holder.adapter_content_four.setVisibility(View.GONE);
                    holder.stateImage.setText(temp.getTag().getTag());
                    holder.stateImage.setTextColor(convertView.getResources().getColor(R.color.blue));
                    holder.stateImage.setBackgroundResource(R.drawable.recommended_state_contact);
                    break;
                case 1:
                    holder.relativeLayout.findViewById(R.id.deal_house).setVisibility(View.VISIBLE);
                    holder.adapter_content_three.setVisibility(View.VISIBLE);
                    holder.relativeLayout.findViewById(R.id.commission_amount).setVisibility(View.VISIBLE);
                    holder.adapter_content_four.setVisibility(View.VISIBLE);
                    holder.stateImage.setText(temp.getTag().getTag());
                    holder.stateImage.setTextColor(convertView.getResources().getColor(R.color.green));
                    holder.stateImage.setBackgroundResource(R.drawable.recommended_state_deal);
                    break;
                case 2:
                    holder.relativeLayout.findViewById(R.id.deal_house).setVisibility(View.GONE);
                    holder.adapter_content_three.setVisibility(View.GONE);
                    holder.relativeLayout.findViewById(R.id.commission_amount).setVisibility(View.GONE);
                    holder.adapter_content_four.setVisibility(View.GONE);
                    holder.stateImage.setText(temp.getTag().getTag());
                    holder.stateImage.setTextColor(convertView.getResources().getColor(R.color.red));
                    holder.stateImage.setBackgroundResource(R.drawable.recommended_state_expired);
                    break;
            }

            holder.stateImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myCustomerProgressDetail = new Intent(view.getContext(), MyCustomerProgressDetailActivity.class);
                    myCustomerProgressDetail.putExtra("rid", temp.getRid());
                    startActivity(myCustomerProgressDetail);
                }
            });

            return convertView;
        }

        private class ViewHolder {
            RelativeLayout relativeLayout;
            TextView adapter_content_one;
            TextView adapter_content_two;
            TextView adapter_content_three;
            TextView adapter_content_four;
            TextView adapter_content_five;
            TextView stateImage;
        }

    }
}