package com.zhijia.ui.zhijiaActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
 * 意见反馈
 */
public class FeedbackActivity extends Activity {

	private static final String FEEDBACK_URL = Global.API_WEB_URL
			+ "/common/feedback";

	// 点击事件的侦听
	private ClickListener clickListener = new ClickListener();

	private TextView messageBody, mobile;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback);

		messageBody = (TextView) findViewById(R.id.message_body);
		mobile = (TextView) findViewById(R.id.mobile);

		this.findViewById(R.id.back).setOnClickListener(clickListener);
		this.findViewById(R.id.send_feedback).setOnClickListener(clickListener);
	}

	/**
	 * 找回密码
	 * 
	 * @return
	 */
	private Map<String, String> feedback() {
		Map<String, String> resultMap = new HashMap<String, String>();
		HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
		Map<String, String> map = new HashMap<String, String>();
		map.put("mobile", mobile.getText().toString());
		map.put("authstr", Global.USER_AUTH_STR);
		map.put("cityid", Global.NOW_CITY_ID);
		map.put("info", messageBody.getText().toString());
		Optional<Map<String, String>> optional = httpClientUtils.postSignedMap(
				FEEDBACK_URL, map, String.class);
		if (optional.isPresent()) {
			resultMap.put("status", optional.get().get("status"));
			resultMap.put("message", optional.get().get("message"));
		}

		return resultMap;
	}

	class FeedbackAsyncTask extends AsyncTask<Void, Void, Map<String, String>> {
		private ProgressDialog mProgressBar;

		FeedbackAsyncTask(Context context) {
			mProgressBar = new ProgressDialog(context);
			mProgressBar.setCancelable(true);
			mProgressBar.setIndeterminate(false);
			mProgressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressBar.setMax(100);
		}

		@Override
		protected void onPreExecute() {
			mProgressBar.setProgress(0);
			mProgressBar.setMessage("正在提交...");
			mProgressBar.show();
		}

		@Override
		protected Map<String, String> doInBackground(Void... params) {
			return feedback();
		}

		@Override
		protected void onPostExecute(Map<String, String> resultMap) {
			super.onPostExecute(resultMap);

			if (resultMap.size() > 0
					&& resultMap.get("status").equalsIgnoreCase("true")) {// 成功
				mProgressBar.setMessage("提交成功");
				mProgressBar.dismiss();
				Toast.makeText(FeedbackActivity.this,getResources().getString(R.string.feedback_submit_success),Toast.LENGTH_SHORT).show();
				finish();
			} else {
				mProgressBar.setMessage("提交失败");
				mProgressBar.dismiss();
				Toast.makeText(FeedbackActivity.this,"提交数据失败！",Toast.LENGTH_SHORT).show();
			}

			findViewById(R.id.send_feedback).setClickable(true);
		}
	}

	// 点击事件的公共类
	class ClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back: // 后退
				finish();
				break;
			case R.id.send_feedback: // 发送反馈意见
				if (messageBody.getText().toString().trim().length() > 0&& mobile.getText().toString().trim().length() > 0) {
					FeedbackAsyncTask feedbackAsyncTask = new FeedbackAsyncTask(FeedbackActivity.this);
					feedbackAsyncTask.execute();
					findViewById(R.id.send_feedback).setClickable(false);
				} else {
					Toast.makeText(getApplicationContext(),
							getString(R.string.empty_message),
							Toast.LENGTH_SHORT).show();
				}

				break;
			}
		}
	}
}