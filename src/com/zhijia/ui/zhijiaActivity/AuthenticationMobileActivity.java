package com.zhijia.ui.zhijiaActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.common.base.Optional;
import com.zhijia.Global;
import com.zhijia.service.network.HttpClientUtils;
import com.zhijia.ui.R;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证手机号
 */
public class AuthenticationMobileActivity extends Activity {

    private static final String MOBILE_CODE_URL = Global.API_WEB_URL + "/common/mobilecode";

    private static final String VERIFY_MOBILR_CODE_URL = Global.API_WEB_URL + "/common/verifymobilecode";

    //短信验证凭证
    private String authstr = "";

    //手机号
    private String mobileNumber = "";

    //点击事件的侦听
    private ClickListener clickListener = new ClickListener();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authentication_mobile);

        findViewById(R.id.back).setOnClickListener(clickListener);
        findViewById(R.id.submit).setOnClickListener(clickListener);
        findViewById(R.id.get_mobile_code).setOnClickListener(clickListener);
    }

    /**
     * 获得手机验证码
     *
     * @return
     */
    private Map<String, String> getMobileCode() {
        mobileNumber = ((TextView) findViewById(R.id.cellphone)).getText().toString();

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

    /**
     * 验证短信码
     *
     * @return
     */
    private Map<String, String> verifyMobileCode() {
        String messageAuthenticationCode = ((TextView) findViewById(R.id.message_authentication_code)).getText().toString();

        Map<String, String> resultMap = new HashMap<String, String>();
        HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
        Map<String, String> map = new HashMap<String, String>();
        map.put("mobile", mobileNumber);
        map.put("mobilecode", messageAuthenticationCode);
        map.put("authstr", authstr);

        Optional<Map<String, String>> optional = httpClientUtils.getUnsignedMap(VERIFY_MOBILR_CODE_URL, map, String.class);
        if (optional.isPresent()) {
            resultMap.put("status", optional.get().get("status"));
            resultMap.put("message", optional.get().get("message"));
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
                case R.id.submit: //提交
                    VerifyMobileCodeAsyncTask verifyMobileCodeAsyncTask = new VerifyMobileCodeAsyncTask();
                    verifyMobileCodeAsyncTask.execute();
                    findViewById(R.id.submit).setClickable(false);
                    break;
                case R.id.get_mobile_code://获取验证码
                    GetMobileCodeAsyncTask getMobileCodeAsyncTask = new GetMobileCodeAsyncTask();
                    getMobileCodeAsyncTask.execute();
                    findViewById(R.id.get_mobile_code).setClickable(false);
                    break;
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
                AuthenticationMobileActivity.this.authstr = resultMap.get("authstr");
                Toast.makeText(getApplicationContext(), resultMap.get("message"), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), resultMap.get("message"), Toast.LENGTH_SHORT).show();
            }

            findViewById(R.id.get_mobile_code).setClickable(true);
        }
    }

    class VerifyMobileCodeAsyncTask extends AsyncTask<Void, Void, Map<String, String>> {
        @Override
        protected Map<String, String> doInBackground(Void... params) {
            return verifyMobileCode();
        }

        @Override
        protected void onPostExecute(Map<String, String> resultMap) {
            super.onPostExecute(resultMap);

            if (resultMap.size() > 0 && resultMap.get("status").equalsIgnoreCase("true")) {//成功
                Toast.makeText(getApplicationContext(), getString(R.string.mobile_authentication_success), Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK, new Intent());
                finish();
            } else {
                Toast.makeText(getApplicationContext(), resultMap.get("message"), Toast.LENGTH_SHORT).show();
            }

            findViewById(R.id.submit).setClickable(true);
        }
    }
}