package com.zhijia.ui.zhijiaActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.common.base.Optional;
import com.zhijia.Global;
import com.zhijia.service.data.Medol.CommissionUserModel;
import com.zhijia.service.data.Medol.JsonResult;
import com.zhijia.service.network.HttpClientUtils;
import com.zhijia.ui.R;
import com.zhijia.util.DownloadImageTask;

import java.util.HashMap;
import java.util.Map;

/**
 * 我的佣金
 */
public class MyCommissionActivity extends Activity {

    private static String COMMISSION_URL = Global.API_WEB_URL + "/member/zhuan";

    private static String WITH_DRAWAL_URL = Global.API_WEB_URL + "/member/zhuantx";

    private OnClickListener clickListener = new OnClickListener();

    private ImageView userHeadImageView, authenticationMobileImageView, authenticationEmailImageView, certificationImageView;

    private TextView myNameTextView, myLevelDescTextView, immediateWithdrawalTextView, myCommissionAmountTextView;

    //是否手机号已经验证
    private boolean isAuthenticationMobile = false;
    //是否实名认证, 是否绑定银行卡
    private boolean isCertification = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_commission);

        userHeadImageView = (ImageView) findViewById(R.id.user_head);
        authenticationMobileImageView = (ImageView) findViewById(R.id.authentication_mobile);
        authenticationEmailImageView = (ImageView) findViewById(R.id.authentication_email);
        certificationImageView = (ImageView) findViewById(R.id.certification);

        myNameTextView = (TextView) findViewById(R.id.my_name);
        myLevelDescTextView = (TextView) findViewById(R.id.my_level_desc);
        immediateWithdrawalTextView = (TextView) findViewById(R.id.immediate_withdrawal);
        myCommissionAmountTextView = (TextView) findViewById(R.id.my_commission_amount);

        findViewById(R.id.back).setOnClickListener(clickListener);
        findViewById(R.id.recommended_rules).setOnClickListener(clickListener);
        findViewById(R.id.immediate_withdrawal).setOnClickListener(clickListener);
        findViewById(R.id.i_want_recommend_buyer).setOnClickListener(clickListener);
        findViewById(R.id.my_certification).setOnClickListener(clickListener);
        findViewById(R.id.already_earn_commission).setOnClickListener(clickListener);
        findViewById(R.id.already_recommend_buyer).setOnClickListener(clickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();

        GetUserInfoAsyncTask getUserInfoAsyncTask = new GetUserInfoAsyncTask();
        getUserInfoAsyncTask.execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //从认证页面回来，才会返回RESULT_OK。
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
    private Map<String, Object> getUserInfo() {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (!Global.USER_AUTH_STR.equals("")) {
            HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
            Map<String, String> map = new HashMap<String, String>();
            map.put("cityid", Global.NOW_CITY_ID);
            map.put("authstr", Global.USER_AUTH_STR);
            Optional<JsonResult<CommissionUserModel>> optional = httpClientUtils.getUnsignedByData(COMMISSION_URL, map, CommissionUserModel.class);
            if (optional.isPresent()) {
                resultMap.put("status", String.valueOf(optional.get().isStatus()));
                resultMap.put("message", optional.get().getMessage());
                if (optional.get().getData() != null) {
                    resultMap.put("userInfo", optional.get().getData());
                }
            }
        }

        return resultMap;
    }

    private Map<String, String> withdrawal() {
        Map<String, String> resultMap = new HashMap<String, String>();
        HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
        Map<String, String> map = new HashMap<String, String>();
        map.put("cityid", Global.NOW_CITY_ID);
        map.put("authstr", Global.USER_AUTH_STR);
        Optional<Map<String, String>> optional = httpClientUtils.getUnsignedMap(WITH_DRAWAL_URL, map, String.class);
        if (optional.isPresent()) {
            resultMap.put("status", optional.get().get("status"));
            resultMap.put("message", optional.get().get("message"));
            resultMap.put("is_status", optional.get().get("is_status"));
        }

        return resultMap;
    }

    public class OnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.back:
                    finish();
                    break;
                case R.id.recommended_rules://推荐规则
                    Intent earnCommissionIntent = new Intent(view.getContext(), EarnCommissionActivity.class);
                    startActivity(earnCommissionIntent);
                    break;
                case R.id.immediate_withdrawal://立即提现
                    if (!isCertification) {
                        Intent myCertificationInfoIntent = new Intent(view.getContext(), CertificationInfoActivity.class);
                        startActivity(myCertificationInfoIntent);
                    } else {
                        WithdrawalAsyncTask withdrawalAsyncTask = new WithdrawalAsyncTask();
                        withdrawalAsyncTask.execute();
                        findViewById(R.id.immediate_withdrawal).setClickable(false);
                    }
                    break;
                case R.id.i_want_recommend_buyer://我要推荐买房
                    if (Global.USER_AUTH_STR.equalsIgnoreCase("")) {
                        Intent loginIntent = new Intent(getApplicationContext(), EarnCommissionActivity.class);
                        startActivity(loginIntent);
                    } else if (!Global.USER_AUTHENTICATION_MOBILE) {
                        Intent authenticationMobileIntent = new Intent(view.getContext(), AuthenticationMobileActivity.class);
                        startActivityForResult(authenticationMobileIntent, 100);
                    } else {
                        Intent recommendedBuyHouseIntent = new Intent(view.getContext(), RecommendedBuyHouseActivity.class);
                        startActivity(recommendedBuyHouseIntent);
                        finish();
                    }
                    break;
                case R.id.already_recommend_buyer://已推荐买房人
                    Intent myAlreadyRecommendBuyerListIntent = new Intent(view.getContext(), MyAlreadyRecommendBuyerListActivity.class);
                    startActivity(myAlreadyRecommendBuyerListIntent);
                    break;
                case R.id.already_earn_commission://已赚取的佣金
                    Intent myAlreadyEarnCommissionIntent = new Intent(view.getContext(), MyAlreadyEarnCommissionActivity.class);
                    startActivity(myAlreadyEarnCommissionIntent);
                    break;
                case R.id.my_certification://我的实名认证
                    if (isCertification) {
                        Intent myCertificationInfoIntent = new Intent(view.getContext(), CertificationInfoActivity.class);
                        startActivity(myCertificationInfoIntent);
                    } else {
                        Intent certificationIntent = new Intent(view.getContext(), CertificationActivity.class);
                        startActivity(certificationIntent);
                    }
                    break;
            }
        }
    }

    class GetUserInfoAsyncTask extends AsyncTask<Void, Void, Map<String, Object>> {
        @Override
        protected Map<String, Object> doInBackground(Void... params) {
            return getUserInfo();
        }

        @Override
        protected void onPostExecute(Map<String, Object> resultMap) {
            super.onPostExecute(resultMap);
            isAuthenticationMobile = false;

            if (resultMap.size() > 0 && ((String) resultMap.get("status")).equalsIgnoreCase("true")) {//成功
                CommissionUserModel userInfo = (CommissionUserModel) resultMap.get("userInfo");
                Global.USER_AVATAR = userInfo.getAvatar();

                myNameTextView.setText(userInfo.getUsername());
                myLevelDescTextView.setText(userInfo.getIdentityName());
                myCommissionAmountTextView.setText(userInfo.getAmount());

                if (userInfo.getMobileStatus() != null && userInfo.getMobileStatus().equalsIgnoreCase("1")) {
                    authenticationMobileImageView.setImageResource(R.drawable.yes);
                    isAuthenticationMobile = true;
                } else {
                    authenticationMobileImageView.setImageResource(R.drawable.no);
                }

                SharedPreferences spf = MyCommissionActivity.this.getSharedPreferences(Global.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
                spf.edit().putBoolean("isAuthenticationMobile", isAuthenticationMobile).commit();
                Global.USER_AUTHENTICATION_MOBILE = isAuthenticationMobile;

                if (userInfo.getEmailStatus() != null && userInfo.getEmailStatus().equalsIgnoreCase("1")) {
                    authenticationEmailImageView.setImageResource(R.drawable.yes);
                } else {
                    authenticationEmailImageView.setImageResource(R.drawable.no);
                }
                if (userInfo.getIdentity() != null && userInfo.getIdentity().equalsIgnoreCase("1")) {

                    certificationImageView.setImageResource(R.drawable.yes);
                } else {
                    certificationImageView.setImageResource(R.drawable.no);
                }
                if (userInfo.getIdentity() != null && (userInfo.getIdentity().equalsIgnoreCase("1") || userInfo.getIdentity().equalsIgnoreCase("2") || userInfo.getIdentity().equalsIgnoreCase("3"))) {
                    isCertification = true;
                } else {
                    isCertification = false;
                }
                if (userInfo.getAmount() != null && (userInfo.getAmount().equalsIgnoreCase("0元") || (userInfo.getAmount().equalsIgnoreCase("暂无")))) {
                    immediateWithdrawalTextView.setVisibility(View.INVISIBLE);
                } else {
                    immediateWithdrawalTextView.setVisibility(View.VISIBLE);
                }

                new DownloadImageTask().doInBackground(Global.USER_AVATAR, userHeadImageView, R.drawable.default_head);
            } else {
                Toast.makeText(getApplicationContext(), (String) resultMap.get("message"), Toast.LENGTH_SHORT).show();
            }
        }
    }

    class WithdrawalAsyncTask extends AsyncTask<Void, Void, Map<String, String>> {
        @Override
        protected Map<String, String> doInBackground(Void... params) {
            return withdrawal();
        }

        @Override
        protected void onPostExecute(Map<String, String> resultMap) {
            super.onPostExecute(resultMap);

            if (resultMap.size() > 0 && resultMap.get("status").equalsIgnoreCase("true")) {//成功
                Toast.makeText(MyCommissionActivity.this, resultMap.get("message"), Toast.LENGTH_SHORT).show();
                if (resultMap.get("is_status") == null || resultMap.get("is_status").equalsIgnoreCase("0")) {
                    GetUserInfoAsyncTask getUserInfoAsyncTask = new GetUserInfoAsyncTask();
                    getUserInfoAsyncTask.execute();
                } else if (resultMap.get("is_status") != null && resultMap.get("is_status").equalsIgnoreCase("3")) {
                    Intent validationBankCardActivityIntent = new Intent(MyCommissionActivity.this, ValidationBankCardActivity.class);
                    startActivity(validationBankCardActivityIntent);
                }
            } else {
                Toast.makeText(MyCommissionActivity.this, resultMap.get("message"), Toast.LENGTH_SHORT).show();
            }

            findViewById(R.id.immediate_withdrawal).setClickable(true);
        }
    }
}