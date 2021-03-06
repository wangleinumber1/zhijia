package com.zhijia.ui.zhijiaActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Optional;
import com.zhijia.Global;
import com.zhijia.service.network.HttpClientUtils;
import com.zhijia.ui.R;

import java.util.HashMap;
import java.util.Map;

/**
 * 注册第一步
 */
public class RegisterStepOneActivity extends Activity {

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
        setContentView(R.layout.register_step_one);

        findViewById(R.id.back).setOnClickListener(clickListener);
        findViewById(R.id.next).setOnClickListener(clickListener);
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
        map.put("module", "register");
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
        map.put("register", "register");

        Optional<Map<String, String>> optional = httpClientUtils.getUnsignedMap(VERIFY_MOBILR_CODE_URL, map, String.class);
        if (optional.isPresent()) {
            resultMap.put("status", optional.get().get("status"));
            resultMap.put("message", optional.get().get("message"));
        }

        return resultMap;
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
                RegisterStepOneActivity.this.authstr = resultMap.get("authstr");
                Toast.makeText(RegisterStepOneActivity.this, "验证码已经发送", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RegisterStepOneActivity.this,"发送验证码失败!", Toast.LENGTH_SHORT).show();
            }

            findViewById(R.id.get_mobile_code).setClickable(true);
        }
    }

    class VerifyMobileCodeAsyncTask extends AsyncTask<Void, Void, Map<String, String>> {
    	
    	 private ProgressDialog mProgressBar;  
	       
    	 VerifyMobileCodeAsyncTask(Context context)  
		    {  
	         mProgressBar = new ProgressDialog(context);  
		     mProgressBar.setCancelable(true);  
		     mProgressBar.setIndeterminate(false);
	         mProgressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);  
		     mProgressBar.setMax(100);  
		     }  

		   @Override
		protected void onPreExecute() {
			mProgressBar.setProgress(0);
			mProgressBar.setMessage("正在提交数据...");
			mProgressBar.show();
		}
        @Override
        protected Map<String, String> doInBackground(Void... params) {
            return verifyMobileCode();
        }

        @Override
        protected void onPostExecute(Map<String, String> resultMap) {
            super.onPostExecute(resultMap);

            if (resultMap.size() > 0 && resultMap.get("status").equalsIgnoreCase("true")) {//成功
            	mProgressBar.dismiss();
                Intent findPasswordIntent = new Intent(RegisterStepOneActivity.this, RegisterStepTwoActivity.class);
                findPasswordIntent.putExtra("mobile", RegisterStepOneActivity.this.mobileNumber);
                findPasswordIntent.putExtra("authstr", RegisterStepOneActivity.this.authstr);
                startActivity(findPasswordIntent);
                finish();
            } else {
            	mProgressBar.dismiss();
                Toast.makeText(RegisterStepOneActivity.this, "提交数据失败!请检查数据或网络", Toast.LENGTH_LONG).show();
            }

            findViewById(R.id.next).setClickable(true);
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
                case R.id.next://下一步
                	EditText messageEditText=(EditText)findViewById(R.id.message_authentication_code);
                	EditText phoneText=(EditText)findViewById(R.id.cellphone);
                	String phoneString=phoneText.getText().toString();
                	String message=messageEditText.getText().toString();
                	if (phoneString.equals("") && message.equals("")) {
						Toast.makeText(RegisterStepOneActivity.this, "数据不能为空", Toast.LENGTH_LONG).show();
					}else {
						  VerifyMobileCodeAsyncTask verifyMobileCodeAsyncTask = new VerifyMobileCodeAsyncTask(RegisterStepOneActivity.this);
		                  verifyMobileCodeAsyncTask.execute();
		                  findViewById(R.id.next).setClickable(false);
					}         
                    break;
                case R.id.get_mobile_code://获取验证码
                	EditText phoneEditText=(EditText)findViewById(R.id.cellphone);
                	if (phoneEditText.getText().toString().equals("")) {
						Toast.makeText(RegisterStepOneActivity.this, "手机号不能为空", Toast.LENGTH_LONG).show();
					}else {
						 GetMobileCodeAsyncTask getMobileCodeAsyncTask = new GetMobileCodeAsyncTask();
		                    getMobileCodeAsyncTask.execute();
		                    findViewById(R.id.get_mobile_code).setClickable(false);
					}           
                    break;
            }
        }
    }
}