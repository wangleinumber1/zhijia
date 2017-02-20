package com.zhijia.ui.zhijiaActivity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.common.base.Optional;
import com.zhijia.Global;
import com.zhijia.service.network.HttpClientUtils;
import com.zhijia.ui.R;

import java.util.HashMap;
import java.util.Map;

/**
 * 看房团报名
 */
public class CondoTourApplyActivity extends Activity {

    private static final String MOBILE_CODE_URL = Global.API_WEB_URL + "/common/mobilecode";
    private static String APPLY_GROUP_URL = Global.API_WEB_URL + "/xinfang/groupbuy";
    //点击事件的侦听
    private ClickListener clickListener = new ClickListener();

    private EditText cellphone, message_authentication_code;

    private int source;
    private String id;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.condo_tour_apply);

        cellphone = (EditText) findViewById(R.id.cellphone);
        message_authentication_code = (EditText) findViewById(R.id.message_authentication_code);

        source = getIntent().getIntExtra("source", 1);//1:新房,2:看房团
        id = getIntent().getStringExtra("id");

        findViewById(R.id.back).setOnClickListener(clickListener);
        findViewById(R.id.submit).setOnClickListener(clickListener);
        findViewById(R.id.get_mobile_code).setOnClickListener(clickListener);
    }

    private Map<String, String> postGroupApply() {
        Map<String, String> resultMap = new HashMap<String, String>();
        HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
        Map<String, String> map = new HashMap<String, String>();

        map.put("cityid", Global.NOW_CITY_ID);
        map.put("id", id);
        map.put("mobile", cellphone.getText().toString());
        map.put("code", message_authentication_code.getText().toString());
        map.put("source", String.valueOf(source));

        Log.d(Global.LOG_TAG, map.toString());

        Optional<Map<String, String>> optional = httpClientUtils.postSignedMap(APPLY_GROUP_URL, map, String.class);
        if (optional.isPresent()) {
            resultMap.put("status", optional.get().get("status"));
            resultMap.put("message", optional.get().get("message"));
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
                case R.id.submit: //马上报名
                    if (cellphone.getText().toString().trim().equalsIgnoreCase("") || message_authentication_code.getText().toString().trim().equalsIgnoreCase("")) {
                        Toast.makeText(getApplicationContext(), "信息输入不完整", Toast.LENGTH_SHORT).show();
                    } else {
                        PostGroupApplyAsyncTask postGroupApplyAsyncTask = new PostGroupApplyAsyncTask();
                        postGroupApplyAsyncTask.execute();
                        findViewById(R.id.submit).setClickable(false);
                    }
                    break;
            }
        }
    }

    class PostGroupApplyAsyncTask extends AsyncTask<Void, Void, Map<String, String>> {
        @Override
        protected Map<String, String> doInBackground(Void... params) {
            return postGroupApply();
        }

        @Override
        protected void onPostExecute(Map<String, String> resultMap) {
            super.onPostExecute(resultMap);

            if (resultMap.size() > 0 && resultMap.get("status").equalsIgnoreCase("true")) {//成功
                Toast.makeText(getApplicationContext(),resultMap.get("message"), Toast.LENGTH_SHORT).show();
                finish();
            } else {
                findViewById(R.id.submit).setClickable(true);
                Toast.makeText(getApplicationContext(), resultMap.get("message"), Toast.LENGTH_SHORT).show();
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