package com.zhijia.ui.zhijiaActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.google.common.base.Optional;
import com.zhijia.Global;
import com.zhijia.service.data.Medol.JsonResult;
import com.zhijia.service.data.Medol.PrivilegeModel;
import com.zhijia.service.network.HttpClientUtils;
import com.zhijia.ui.R;

import java.util.HashMap;
import java.util.Map;

/**
 * 获取优惠
 */
public class GetPrivilegeActivity extends CommonActivity {

    private static final String MOBILE_CODE_URL = Global.API_WEB_URL + "/common/mobilecode";
    private static String GET_PRIVILEGE_URL = Global.API_WEB_URL + "/xinfang/preferential";
    private static String POST_PRIVILEGE_URL = Global.API_WEB_URL + "/xinfang/preferential";
    private ClickListener clickListener = new ClickListener();
    private String hid = "";

    private TextView privilege_title, privilege_time_range;

    private EditText buy_privilege_count, name, cellphone, message_authentication_code;

    private RadioGroup privilege_choose_radio_group;

    private String pid = "", amount = "1";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_privilege);

        privilege_title = (TextView) findViewById(R.id.privilege_title);
        privilege_time_range = (TextView) findViewById(R.id.privilege_time_range);
        privilege_choose_radio_group = (RadioGroup) findViewById(R.id.privilege_choose_radio_group);
        buy_privilege_count = (EditText) findViewById(R.id.buy_privilege_count);
        name = (EditText) findViewById(R.id.name);
        cellphone = (EditText) findViewById(R.id.cellphone);
        message_authentication_code = (EditText) findViewById(R.id.message_authentication_code);

        hid = getIntent().getStringExtra("hid");

        Log.d(Global.LOG_TAG, hid);

        this.setTopTitle(getString(R.string.get_privilege), R.id.common_two_view_text);
        this.setOnClickListener(this);
        findViewById(R.id.get_mobile_code).setOnClickListener(clickListener);
        findViewById(R.id.submit).setOnClickListener(clickListener);

        GetPrivilegeInfoAsyncTask getPrivilegeInfoAsyncTask = new GetPrivilegeInfoAsyncTask();
        getPrivilegeInfoAsyncTask.execute(hid);
    }

    private Map<String, String> postPrivilege() {
        Map<String, String> resultMap = new HashMap<String, String>();
        HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
        Map<String, String> map = new HashMap<String, String>();
        int buyCount = Integer.parseInt(buy_privilege_count.getText().toString());
        if (amount.equals("")) {
			amount="1";
		}
        int amountSingl = Integer.parseInt(amount);
        Log.d("amountString1--->", amountSingl+"");
        map.put("cityid", Global.NOW_CITY_ID);
        map.put("hid", hid);
        map.put("pid", pid);
        map.put("name", name.getText().toString());
        map.put("mobile", cellphone.getText().toString());
        map.put("code", message_authentication_code.getText().toString());
        map.put("nums", buy_privilege_count.getText().toString());
        map.put("amount", String.valueOf(amountSingl * buyCount));

        Log.d(Global.LOG_TAG, map.toString());

        Optional<Map<String, String>> optional = httpClientUtils.postSignedMap(POST_PRIVILEGE_URL, map, String.class);
        if (optional.isPresent()) {
            resultMap.put("status", optional.get().get("status"));
            resultMap.put("message", optional.get().get("message"));
        }

        return resultMap;
    }

    private Map<String, Object> getPrivilegeInfo(String hid) {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
        Map<String, String> map = new HashMap<String, String>();
        map.put("cityid", Global.NOW_CITY_ID);
        map.put("hid", hid);
        Optional<JsonResult<PrivilegeModel>> optional = httpClientUtils.getUnsignedByData(GET_PRIVILEGE_URL, map, PrivilegeModel.class);
        if (optional.isPresent()) {
            resultMap.put("status", String.valueOf(optional.get().isStatus()));
            resultMap.put("message", optional.get().getMessage());
            if (optional.get().getData() != null) {
                resultMap.put("privilegeInfo", optional.get().getData());
            }
        }

        return resultMap;
    }

    /**
     * 获得手机验证码
     *
     * @return
     */
    private Map<String, String> getMobileCode() {
        String mobileNumber = cellphone.getText().toString();

        Map<String, String> resultMap = new HashMap<String, String>();
        HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
        Map<String, String> map = new HashMap<String, String>();
        map.put("mobile", mobileNumber);
        Optional<Map<String, String>> optional = httpClientUtils.getUnsignedMap(MOBILE_CODE_URL, map, String.class);
        if (optional.isPresent()) {
            resultMap.put("status", optional.get().get("status"));
            resultMap.put("message", optional.get().get("message"));
            resultMap.put("authstr", optional.get().get("authstr"));//可能为null
        }

        return resultMap;
    }

    //点击事件的公共类
    class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.back: //后退
                    finish();
                    break;
                case R.id.get_mobile_code://获取验证码
                    if (!cellphone.getText().toString().trim().equalsIgnoreCase("")) {
                        GetMobileCodeAsyncTask getMobileCodeAsyncTask = new GetMobileCodeAsyncTask();
                        getMobileCodeAsyncTask.execute();
                        findViewById(R.id.get_mobile_code).setClickable(false);
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.input_cellphone_hint), Toast.LENGTH_SHORT).show();
                        cellphone.setFocusable(true);
                    }
                    break;
                case R.id.submit:
                    if (pid.equalsIgnoreCase("")) {
                        Toast.makeText(getApplicationContext(), "至少选择一个优惠选项", Toast.LENGTH_SHORT).show();
                    } else if (buy_privilege_count.getText().toString().trim().equalsIgnoreCase("")) {
                        Toast.makeText(getApplicationContext(), "输入购买套数", Toast.LENGTH_SHORT).show();
                        buy_privilege_count.setFocusable(true);
                    } else if (name.getText().toString().trim().equalsIgnoreCase("") || cellphone.getText().toString().trim().equalsIgnoreCase("") || message_authentication_code.getText().toString().trim().equalsIgnoreCase("")) {
                        Toast.makeText(getApplicationContext(), "信息输入不完整", Toast.LENGTH_SHORT).show();
                    } else {
                        PostPrivilegeAsyncTask postPrivilegeAsyncTask = new PostPrivilegeAsyncTask();
                        postPrivilegeAsyncTask.execute();
                        findViewById(R.id.submit).setClickable(false);
                    }
                    break;
            }
        }
    }

    class PostPrivilegeAsyncTask extends AsyncTask<Void, Void, Map<String, String>> {
        @Override
        protected Map<String, String> doInBackground(Void... params) {
            return postPrivilege();
        }

        @Override
        protected void onPostExecute(Map<String, String> resultMap) {
            super.onPostExecute(resultMap);

            if (resultMap.size() > 0 && resultMap.get("status").equalsIgnoreCase("true")) {//成功
                Toast.makeText(getApplicationContext(), resultMap.get("message"), Toast.LENGTH_SHORT).show();
                finish();
            } else {
                findViewById(R.id.submit).setClickable(true);
                Toast.makeText(getApplicationContext(), resultMap.get("message"), Toast.LENGTH_SHORT).show();
            }
        }
    }

    class GetPrivilegeInfoAsyncTask extends AsyncTask<String, Void, Map<String, Object>> {
        @Override
        protected Map<String, Object> doInBackground(String... params) {
            return getPrivilegeInfo(params[0]);
        }

        @Override
        protected void onPostExecute(Map<String, Object> resultMap) {
            super.onPostExecute(resultMap);

            if (resultMap.size() > 0 && ((String) resultMap.get("status")).equalsIgnoreCase("true")) {//成功
                PrivilegeModel tempObj = (PrivilegeModel) resultMap.get("privilegeInfo");
                privilege_title.setText(tempObj.getPrivilege());
                privilege_time_range.setText(tempObj.getTime());
                System.out.println("youhui:"+tempObj.getYouhui().toString());
                for (final PrivilegeModel.Youhui youhui : tempObj.getYouhui()) {
                    RadioButton radioButton = (RadioButton) getLayoutInflater().inflate(R.layout.get_privilege_radio_button, null);
                    radioButton.setText(youhui.getInfo());
                    System.out.println(youhui.getInfo()+youhui.getAmount());
                    radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                pid = youhui.getPid();
                                amount = youhui.getAmount();
                            }
                        }
                    });
                    privilege_choose_radio_group.addView(radioButton);
                }

            } else {
                Toast.makeText(getApplicationContext(), (String) resultMap.get("message"), Toast.LENGTH_SHORT).show();
            }
        }
    }

    class GetMobileCodeAsyncTask extends AsyncTask<Void, Void, Map<String, String>> {
        @Override
        protected Map<String, String> doInBackground(Void... params) {
            return getMobileCode();
        }

        @Override
        protected void onPostExecute(Map<String, String> resultMap) {
            super.onPostExecute(resultMap);

            if (resultMap.size() > 0 && resultMap.get("status").equalsIgnoreCase("true")) {//成功
                Toast.makeText(getApplicationContext(), resultMap.get("message"), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), resultMap.get("message"), Toast.LENGTH_SHORT).show();
            }

            findViewById(R.id.get_mobile_code).setClickable(true);
        }
    }

}