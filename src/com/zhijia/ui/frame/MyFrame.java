package com.zhijia.ui.frame;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Optional;
import com.zhijia.Global;
import com.zhijia.service.data.Medol.JsonResult;
import com.zhijia.service.data.Medol.UserLoginInfoModel;
import com.zhijia.service.network.HttpClientUtils;
import com.zhijia.ui.R;

import static com.zhijia.Global.USER_AUTH_STR;
import static com.zhijia.Global.SHARED_PREFERENCES_NAME;

import com.zhijia.ui.my.*;
import com.zhijia.ui.zhijiaActivity.LoginActivity;
import com.zhijia.ui.zhijiaActivity.MessageIndexActivity;
import com.zhijia.ui.zhijiaActivity.MyAttentionCommunityActivity;
import com.zhijia.ui.zhijiaActivity.MyAttentionNewHouseActivity;
import com.zhijia.ui.zhijiaActivity.MyBrowseCommunityActivity;
import com.zhijia.ui.zhijiaActivity.MyBrowseNewHouseActivity;
import com.zhijia.ui.zhijiaActivity.MyBrowseOldHouseActivity;
import com.zhijia.ui.zhijiaActivity.MyBrowseRentHouseActivity;
import com.zhijia.ui.zhijiaActivity.MyCommissionActivity;
import com.zhijia.ui.zhijiaActivity.MyFavoritesOldHouseActivity;
import com.zhijia.ui.zhijiaActivity.MyFavoritesRentHouseActivity;
import com.zhijia.ui.zhijiaActivity.MyInfoActivity;
import com.zhijia.ui.zhijiaActivity.MyOrderWatchOldHouseRecordActivity;
import com.zhijia.ui.zhijiaActivity.MyOrderWatchRentHouseRecordActivity;
import com.zhijia.util.DownloadImageTask;

import java.util.HashMap;
import java.util.Map;

/**
 * 我的View
 */
@SuppressWarnings("unused")
public class MyFrame extends Frame {

    private static String USER_LOGIN_INFO_URL = Global.API_WEB_URL + "/member/mylogin";

    //是否在首页
    boolean isIndexMenu = true;
    //点击事件的侦听
    private ClickListener clickListener = new ClickListener();

    private TextView userName, messageCount;

    private ImageView userHead;

    private int nowMenuId = R.id.my_index_menu_layout;

    @Override
    public View onCreateView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.tab_my, null);
    }

    @Override
	public void onCreate() {
        super.onCreate();
        userName = (TextView) findViewById(R.id.user_name);
        messageCount = (TextView) findViewById(R.id.my_message_count);
        userHead = (ImageView) findViewById(R.id.user_head_image_view);
        bindEvent();
        GetUserInfoAsyncTask getUserInfoAsyncTask = new GetUserInfoAsyncTask();
        getUserInfoAsyncTask.execute();
        
    }

    @Override
	public void onDestroy() {
        super.onDestroy();
        nowMenuId = R.id.my_index_menu_layout;
    }

    @Override
	public void onResume() {
        super.onResume();
        menuVisibility(nowMenuId);
        GetUserInfoAsyncTask getUserInfoAsyncTask = new GetUserInfoAsyncTask();
        getUserInfoAsyncTask.execute();
    }

    @Override
    public void onTabClick() {

    }

    /**
     * 设置菜单显示
     *
     * @param layoutId
     */
    private void menuVisibility(int layoutId) {
        if (layoutId == R.id.my_index_menu_layout) {
            isIndexMenu = true;
        } else {
            isIndexMenu = false;
        }
        findViewById(R.id.my_index_menu_layout).setVisibility(View.GONE);
        findViewById(R.id.my_index_new_house_layout).setVisibility(View.GONE);
        findViewById(R.id.my_index_old_house_layout).setVisibility(View.GONE);
        findViewById(R.id.my_index_rent_house_layout).setVisibility(View.GONE);
        findViewById(layoutId).setVisibility(View.VISIBLE);
        
    }

    /**
     * 绑定当前页面的事件
     */
    private void bindEvent() {
    	
        //首页部分栏目
        findViewById(R.id.back).setOnClickListener(clickListener);
        findViewById(R.id.login_button).setOnClickListener(clickListener);
        findViewById(R.id.user_head_image_view).setOnClickListener(clickListener);
        findViewById(R.id.user_name).setOnClickListener(clickListener);
        findViewById(R.id.after_login_layout).setOnClickListener(clickListener);
        findViewById(R.id.my_message_logo).setOnClickListener(clickListener);
        findViewById(R.id.my_message_title).setOnClickListener(clickListener);
        findViewById(R.id.my_message_count).setOnClickListener(clickListener);
        findViewById(R.id.my_new_house).setOnClickListener(clickListener);
        findViewById(R.id.my_old_house).setOnClickListener(clickListener);
        findViewById(R.id.my_rent_house).setOnClickListener(clickListener);
        findViewById(R.id.my_commission_logo).setOnClickListener(clickListener);
        findViewById(R.id.my_commission).setOnClickListener(clickListener);

        //新房二级页
        findViewById(R.id.my_attention_new_house).setOnClickListener(clickListener);
        findViewById(R.id.my_attention_community).setOnClickListener(clickListener);
        findViewById(R.id.my_browse_new_house).setOnClickListener(clickListener);

        //二手房二级页
        findViewById(R.id.my_order_watch_old_house_record).setOnClickListener(clickListener);
        findViewById(R.id.my_favorites_old_house).setOnClickListener(clickListener);
        findViewById(R.id.my_browse_old_house).setOnClickListener(clickListener);
        findViewById(R.id.my_browse_community).setOnClickListener(clickListener);

        //租房二级页
        findViewById(R.id.my_order_watch_rent_house_record).setOnClickListener(clickListener);
        findViewById(R.id.my_favorites_rent_house).setOnClickListener(clickListener);
        findViewById(R.id.my_browse_rent_house).setOnClickListener(clickListener);
    }

    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //只有登录成功，才会返回RESULT_OK。
        if (resultCode == Activity.RESULT_OK) {
            GetUserInfoAsyncTask getUserInfoAsyncTask = new GetUserInfoAsyncTask();
            getUserInfoAsyncTask.execute();
        }
    }

    /**
     * 获得当前登录用户的用户信息
     *
     * @return
     */
    private Map<String, String> getUserInfo() {
        Map<String, String> resultMap = new HashMap<String, String>();
        if (!Global.USER_AUTH_STR.equals("")) {
            HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
            Map<String, String> map = new HashMap<String, String>();
            map.put("authstr", Global.USER_AUTH_STR);
            Optional<JsonResult<UserLoginInfoModel>> optional = httpClientUtils.getUnsignedByData(USER_LOGIN_INFO_URL, map, UserLoginInfoModel.class);
            if (optional.isPresent()) {
                resultMap.put("status", String.valueOf(optional.get().isStatus()));
                resultMap.put("message", optional.get().getMessage());
                if (optional.get().getData() != null) {
                    resultMap.put("username", optional.get().getData().getUsername());
                    resultMap.put("count", optional.get().getData().getCount().toString());
                    resultMap.put("avatar", optional.get().getData().getAvatar());
                }
            }
        }

        return resultMap;
    }

    //点击事件的公共类
    class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent loginIntent = new Intent(MyFrame.this.getContext(), LoginActivity.class);

            switch (view.getId()) {
                case R.id.back: //后退
                    if (isIndexMenu) {
                        Global.BOTTOM_TAB.setCurrentTab(0);
                    } else {
                        menuVisibility(R.id.my_index_menu_layout);
                        nowMenuId = R.id.my_index_menu_layout;
                    }
                    break;
                case R.id.login_button: //登录
                    startActivityForResult(loginIntent, 100);
                    break;
                case R.id.user_head_image_view: // 个人信息
                case R.id.user_name:
                case R.id.after_login_layout:
                    if (Global.USER_AUTH_STR.equalsIgnoreCase("")) {
                        startActivityForResult(loginIntent, 100);
                    } else {
                        Intent myInfoIntent = new Intent(MyFrame.this.getContext(), MyInfoActivity.class);
                        startActivity(myInfoIntent);
                    }
                    break;
                case R.id.my_message_logo: // 个人消息
                case R.id.my_message_title:
                case R.id.my_message_count:
                    if (Global.USER_AUTH_STR.equalsIgnoreCase("")) {
                        startActivityForResult(loginIntent, 100);
                    } else {
                        Intent messageIntent = new Intent(getActivity(), MessageIndexActivity.class);
                        startActivity(messageIntent);
                    }
                    break;
                case R.id.my_commission_logo: // 我的佣金
                case R.id.my_commission:
                    if (Global.USER_AUTH_STR.equalsIgnoreCase("")) {
                        startActivityForResult(loginIntent, 100);
                    } else {
                        Intent myCommissionIntent = new Intent(getActivity(), MyCommissionActivity.class);
                        startActivity(myCommissionIntent);
                    }
                    break;
                case R.id.my_new_house://我的新房
                    if (Global.USER_AUTH_STR.equalsIgnoreCase("")) {
                        startActivityForResult(loginIntent, 100);
                    } else {
                        menuVisibility(R.id.my_index_new_house_layout);
                        nowMenuId = R.id.my_index_new_house_layout;
                    }
                    break;
                case R.id.my_old_house://我的二手房
                	System.out.println("我的二手房:"+Global.USER_AUTH_STR);
                    if (Global.USER_AUTH_STR.equalsIgnoreCase("")) {
                        startActivityForResult(loginIntent, 100);
                    } else {
                        menuVisibility(R.id.my_index_old_house_layout);
                        nowMenuId = R.id.my_index_old_house_layout;
                    }
                    break;
                case R.id.my_rent_house://我的租房
                    if (Global.USER_AUTH_STR.equalsIgnoreCase("")) {
                        startActivityForResult(loginIntent, 100);
                    } else {
                        menuVisibility(R.id.my_index_rent_house_layout);
                        nowMenuId = R.id.my_index_rent_house_layout;
                    }
                    break;
                case R.id.my_attention_new_house: // 我关注的新房
                    Intent attentionNewHouseIntent = new Intent(getActivity(), MyAttentionNewHouseActivity.class);
                    startActivity(attentionNewHouseIntent);
                    break;
                case R.id.my_attention_community: //我关注的小区
                    Intent attentionCommunityIntent = new Intent(getActivity(), MyAttentionCommunityActivity.class);
                    startActivity(attentionCommunityIntent);
                    break;
                case R.id.my_browse_new_house://我浏览的新房
                    Intent browseNewHouseIntent = new Intent(getActivity(), MyBrowseNewHouseActivity.class);
                    startActivity(browseNewHouseIntent); 
                    break;
                case R.id.my_order_watch_old_house_record://我的二手房预约看房记录
                    Intent myOrderWatchOldHouseRecordIntent = new Intent(getActivity(), MyOrderWatchOldHouseRecordActivity.class);
                    startActivity(myOrderWatchOldHouseRecordIntent);
                    break;
                case R.id.my_favorites_old_house://我收藏的二手房
                    Intent myFavoritesOldHouseIntent = new Intent(getActivity(), MyFavoritesOldHouseActivity.class);
                    startActivity(myFavoritesOldHouseIntent);
                    break;
                case R.id.my_browse_old_house://我浏览的二手房
                    Intent browseOldHouseIntent = new Intent(getActivity(), MyBrowseOldHouseActivity.class);
                    startActivity(browseOldHouseIntent);
                    break;
                case R.id.my_browse_community://我浏览到小区
                    Intent browseCommunityIntent = new Intent(getActivity(), MyBrowseCommunityActivity.class);
                    startActivity(browseCommunityIntent);
                    break;
                case R.id.my_order_watch_rent_house_record://我的租房预约看房记录
                    Intent myOrderWatchRentHouseRecordIntent = new Intent(getActivity(), MyOrderWatchRentHouseRecordActivity.class);
                    startActivity(myOrderWatchRentHouseRecordIntent);
                    break;
                case R.id.my_favorites_rent_house://我收藏的租房
                    Intent myFavoritesRentHouseIntent = new Intent(getActivity(), MyFavoritesRentHouseActivity.class);
                    startActivity(myFavoritesRentHouseIntent);
                    break;
                case R.id.my_browse_rent_house://我浏览的租房
                    Intent browseRentHouseIntent = new Intent(getActivity(), MyBrowseRentHouseActivity.class);
                    startActivity(browseRentHouseIntent);
                    break;
            }
        }
    }

    class GetUserInfoAsyncTask extends AsyncTask<Void, Void, Map<String, String>> {
        @Override
        protected Map<String, String> doInBackground(Void... params) {
            return getUserInfo();
        }

        @Override
        protected void onPostExecute(Map<String, String> resultMap) {
            if (resultMap != null) {
                if (resultMap.size() > 0 && resultMap.get("status").equalsIgnoreCase("true")) {//成功
                    findViewById(R.id.login_button).setVisibility(View.INVISIBLE);
                    userName.setText(resultMap.get("username"));
                    if (Integer.parseInt(resultMap.get("count")) > 0) {
                        messageCount.setText(resultMap.get("count"));
                        messageCount.setVisibility(View.VISIBLE);
                    } else {
                        messageCount.setVisibility(View.INVISIBLE);
                    }
                    new DownloadImageTask().doInBackground(resultMap.get("avatar"), userHead, R.drawable.default_head);
                    Log.d(Global.LOG_TAG, resultMap.get("avatar"));
                    findViewById(R.id.after_login_layout).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.login_button).setVisibility(View.VISIBLE);
                    findViewById(R.id.after_login_layout).setVisibility(View.INVISIBLE);
                    messageCount.setVisibility(View.INVISIBLE);

                    if (resultMap.get("message") != null && resultMap.get("message").length() > 0) {
                        Toast.makeText(MyFrame.this.getContext(), resultMap.get("message"), Toast.LENGTH_SHORT).show();

                    }
                }
            }
        }
    }

	@Override
	public void onCreat() {
		// TODO Auto-generated method stub
		
	}
}
