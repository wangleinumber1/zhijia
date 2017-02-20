package com.zhijia.ui.zhijiaActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
 * 找回密码第二步
 */
public class FindPasswordStepTwoActivity extends Activity {

    private static final String RESET_PASSWORD_URL = Global.API_WEB_URL + "/member/resetpassword";

    private String mobile, authstr;

    private TextView passwordTextView, passwordAgainTextView;

    //点击事件的侦听
    private ClickListener clickListener = new ClickListener();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_password_step_two);
        passwordTextView = (TextView) findViewById(R.id.password);
        passwordAgainTextView = (TextView) findViewById(R.id.password_again);

        mobile = getIntent().getStringExtra("mobile");
        authstr = getIntent().getStringExtra("authstr");

        findViewById(R.id.back).setOnClickListener(clickListener);
        findViewById(R.id.complete_change_password).setOnClickListener(clickListener);
    }

    /**
     * 找回密码
     *
     * @return
     */
    private Map<String, String> findPassword() {
        Map<String, String> resultMap = new HashMap<String, String>();
        HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
        Map<String, String> map = new HashMap<String, String>();
        map.put("mobile", mobile);
        map.put("authstr", authstr);
        map.put("userpass", passwordTextView.getText().toString());
        map.put("confirmpassword", passwordAgainTextView.getText().toString());
        Optional<Map<String, String>> optional = httpClientUtils.postSignedMap(RESET_PASSWORD_URL, map, String.class);
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
                case R.id.complete_change_password://重置密码
                    String password = passwordTextView.getText().toString();
                    String passwordAgain = passwordAgainTextView.getText().toString();

                    if (!password.equals(passwordAgain)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(FindPasswordStepTwoActivity.this);
                        builder.setMessage(view.getResources().getString(R.string.password_not_equal));
                        builder.setTitle(view.getResources().getString(R.string.prompt));
                        builder.setPositiveButton(view.getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                passwordTextView.setText("");
                                passwordAgainTextView.setText("");
                            }
                        });

                        builder.create().show();
                    } else {
                        ResetPasswordAsyncTask resetPasswordAsyncTask = new ResetPasswordAsyncTask();
                        resetPasswordAsyncTask.execute();
                        findViewById(R.id.complete_change_password).setClickable(false);
                    }
                    break;
            }
        }
    }

    class ResetPasswordAsyncTask extends AsyncTask<Void, Void, Map<String, String>> {
        @Override
        protected Map<String, String> doInBackground(Void... params) {
            return findPassword();
        }

        @Override
        protected void onPostExecute(Map<String, String> resultMap) {
            super.onPostExecute(resultMap);

            if (resultMap.size() > 0 && resultMap.get("status").equalsIgnoreCase("true")) {//成功
                Toast.makeText(FindPasswordStepTwoActivity.this, getResources().getString(R.string.change_password_success), Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(FindPasswordStepTwoActivity.this, resultMap.get("message"), Toast.LENGTH_SHORT).show();
            }

            findViewById(R.id.complete_change_password).setClickable(true);
        }
    }
}