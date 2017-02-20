package com.zhijia.ui.zhijiaActivity;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.google.common.base.Optional;
import com.zhijia.Global;
import com.zhijia.service.data.Medol.EarnCommissionModel;
import com.zhijia.service.data.Medol.JsonResult;
import com.zhijia.service.network.HttpClientUtils;
import com.zhijia.ui.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 已赚取的佣金
 */
public class MyAlreadyEarnCommissionActivity extends Activity {

    private static String EARN_COMMISSION_URL = Global.API_WEB_URL + "/member/zhuanamount";

    //点击事件的侦听
    private ClickListener clickListener = new ClickListener();

    private ListView listView;

    private View loadingView;

    private List<EarnCommissionModel> earnCommissionModelList = new ArrayList<EarnCommissionModel>();

    //可见视图最后的一个索引
    private int visibleLastIndex = 0, currentPage = 1;
    //当前窗口可见项总数
    private int visibleItemCount;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_already_earn_commission);

        loadingView = findViewById(R.id.loading_relative);
        listView = (ListView) findViewById(R.id.my_already_earn_commission_list);
        listView.setOnScrollListener(new OnScrollListener());

        EarnCommissionPageAsyncTask earnCommissionPageAsyncTask = new EarnCommissionPageAsyncTask();
        earnCommissionPageAsyncTask.execute(String.valueOf(currentPage));

        //注册事件绑定
        findViewById(R.id.back).setOnClickListener(clickListener);
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
                EarnCommissionPageAsyncTask earnCommissionPageAsyncTask = new EarnCommissionPageAsyncTask();
                earnCommissionPageAsyncTask.execute(String.valueOf(currentPage));
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


    class EarnCommissionPageAsyncTask extends AsyncTask<String, Void, JsonResult<List<EarnCommissionModel>>> {
        private String page;

        @Override
        protected JsonResult<List<EarnCommissionModel>> doInBackground(String... params) {
            page = params[0];
            if (!Global.USER_AUTH_STR.equals("")) {
                HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
                Map<String, String> map = new HashMap<String, String>();
                map.put("cityid", Global.NOW_CITY_ID);
                map.put("authstr", Global.USER_AUTH_STR);
                map.put("page", page.toString());
                Optional<JsonResult<List<EarnCommissionModel>>> optional = httpClientUtils.getUnsignedListByData(EARN_COMMISSION_URL, map, EarnCommissionModel.class);
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
        protected void onPostExecute(JsonResult<List<EarnCommissionModel>> listJsonResult) {
            loadingView.setVisibility(View.GONE);
            if (listJsonResult != null && String.valueOf(listJsonResult.isStatus()).equalsIgnoreCase("true")) {//成功
                if (Integer.parseInt(listJsonResult.getTotal()) > 0 && listJsonResult.getData() != null) {
                    List<EarnCommissionModel> tempList = listJsonResult.getData();
                    if (tempList != null) {
                        earnCommissionModelList.addAll(tempList);
                        listView.setAdapter(new MyAlreadyEarnCommissionListAdapter(getApplicationContext(), earnCommissionModelList));//数据集变化后,通知adapter
                        listView.setSelection(visibleLastIndex - visibleItemCount + 1); //设置选中项
                    }
                }
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

    /**
     * 我赚取的佣金的Adapter
     */
    private class MyAlreadyEarnCommissionListAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private List<EarnCommissionModel> list;

        public MyAlreadyEarnCommissionListAdapter(Context context, List<EarnCommissionModel> list) {
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
                convertView = inflater.inflate(R.layout.my_already_earn_commission_list_item, parent, false);
                holder = new ViewHolder();

                holder.commissionAmount = (TextView) convertView.findViewById(R.id.commission_amount);
                holder.dealCustomer = (TextView) convertView.findViewById(R.id.deal_customer);
                holder.mobile = (TextView) convertView.findViewById(R.id.mobile);
                holder.dealStateImg = (ImageView) convertView.findViewById(R.id.deal_state_image_view);
                holder.dealState = (TextView) convertView.findViewById(R.id.deal_state_desc);
                holder.dealTime = (TextView) convertView.findViewById(R.id.deal_time);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            EarnCommissionModel temp = list.get(position);

            holder.dealCustomer.setText(temp.getTruename());
            holder.mobile.setText(" (" + temp.getMobile() + ")");
            holder.dealTime.setText(temp.getPosttime());
            holder.commissionAmount.setText(temp.getAmount());
            holder.dealState.setText(temp.getTag().getName());

            switch (temp.getTag().getStatus()) {//1 蓝 2 灰 3 绿 4红
                case 0:
                case 2:
                    holder.dealState.setTextColor(convertView.getResources().getColor(R.color.font_desc));
                    holder.dealStateImg.setImageResource(R.drawable.time);
                    break;
                case 4://提现失败
                    holder.dealState.setTextColor(convertView.getResources().getColor(R.color.red));
                    holder.dealStateImg.setImageResource(R.drawable.error);
                    break;
                case 3://提现成功
                    holder.dealState.setTextColor(convertView.getResources().getColor(R.color.green));
                    holder.dealStateImg.setImageResource(R.drawable.success);
                    break;
                case 1://
                    holder.dealState.setTextColor(convertView.getResources().getColor(R.color.blue));
                    holder.dealStateImg.setImageResource(R.drawable.done);
                    break;
            }

            return convertView;
        }

        private class ViewHolder {
            TextView commissionAmount;
            TextView dealCustomer;
            TextView mobile;
            ImageView dealStateImg;
            TextView dealState;
            TextView dealTime;
        }

    }
}