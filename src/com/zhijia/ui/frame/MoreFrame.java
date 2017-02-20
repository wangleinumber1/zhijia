package com.zhijia.ui.frame;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.common.base.Optional;
import com.zhijia.DownloadDialog;
import com.zhijia.Global;
import com.zhijia.service.data.Medol.CheckUpdateModel;
import com.zhijia.service.data.Medol.CityAboutUsModel;
import com.zhijia.service.data.Medol.JsonResult;
import com.zhijia.service.network.HttpClientUtils;
import com.zhijia.ui.R;
import com.zhijia.ui.zhijiaActivity.AboutUSActivity;
import com.zhijia.ui.zhijiaActivity.DisclaimerActivity;
import com.zhijia.ui.zhijiaActivity.FeedbackActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import static com.zhijia.Global.HOT_LINE;

/**
 * 更多的View
 */
public class MoreFrame extends Frame {

	private String SETTING_URL = Global.API_WEB_URL + "/common/setting";
	private static final String UPDATE_URL = Global.API_WEB_URL
			+ "/common/update";
	private ClickListener clickListener = new ClickListener();
	//private Map<String, String> resultMap;
	private String webSiteStr = "http://www.zhijia.com";
	private Button hot_line;
	private static String tel = "031166683355";

	@Override
	public View onCreateView(LayoutInflater inflater) {
		return inflater.inflate(R.layout.tab_more, null);

	}

	@Override
	public void onCreate() {
		super.onCreate();
		hot_line = (Button) this.findViewById(R.id.more_hot_line);
		this.findViewById(R.id.back).setOnClickListener(clickListener);
		this.findViewById(R.id.more_hot_line).setOnClickListener(clickListener);
		this.findViewById(R.id.feedback_layout).setOnClickListener(clickListener);
		this.findViewById(R.id.about_us_layout).setOnClickListener(clickListener);
		this.findViewById(R.id.disclaimer_layout).setOnClickListener(clickListener);
		this.findViewById(R.id.check_update_layout).setOnClickListener(clickListener);
		this.findViewById(R.id.clear_cache_layout).setOnClickListener(clickListener);
		/*for (int i = 0; i < 1000; i++) {
			getSettings();
		}*/
		HotLineAsyncTask hotLineAsyncTask = new HotLineAsyncTask();
		hotLineAsyncTask.execute();
		Log.d("MoreFrame------>>", "end");
	}

	@Override
	public void onDestroy() {

		super.onDestroy();
	}

	private Map<String, String> checkUpdate() {
		Map<String, String> resultMap = new HashMap<String, String>();
		HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
		Map<String, String> map = new HashMap<String, String>();
		// map.put("version", String.valueOf(Global.VERSION));
		try {
			map.put("version", String.valueOf(Global.getVersionCode()));
			map.put("type", "2");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		Optional<JsonResult<CheckUpdateModel>> optional = httpClientUtils
				.getUnsignedByData(UPDATE_URL, map, CheckUpdateModel.class);
		if (optional.isPresent()) {
			resultMap.put("status", String.valueOf(optional.get().isStatus()));
			resultMap.put("message", optional.get().getMessage());
			if (optional.get().getData() != null) {
				System.out.println(""+optional.get().getData().getVersion()+optional.get().getData().getVersionname());
				resultMap.put("version", optional.get().getData().getVersion().toString());
				resultMap.put("url", optional.get().getData().getUrl());
			}
		}
		//System.out.println("url:" + optional.get().getData().getUrl());
		return resultMap;
	}

	@Override
	public void onTabClick() {

	}

	// 点击事件的公共类
	class ClickListener implements View.OnClickListener {
		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.back: // 后退
				// 返回首页
				Global.BOTTOM_TAB.setCurrentTab(0);
				break;
			case R.id.feedback_layout:
				Intent feedbackIntent = new Intent(view.getContext(),
						FeedbackActivity.class);
				startActivity(feedbackIntent);
				break;
			case R.id.about_us_layout:
				Intent aboutUSIntent = new Intent(view.getContext(),
						AboutUSActivity.class);
				startActivity(aboutUSIntent);
				break;
			case R.id.disclaimer_layout:
				Intent disclaimerIntent = new Intent(view.getContext(),
						DisclaimerActivity.class);
				startActivity(disclaimerIntent);
				break;
			case R.id.more_hot_line:
				Log.d(Global.LOG_TAG, "hotLine onClick");
				if (tel.equals("")) {
					hot_line.setText("031166683355");
				} else {
					hot_line.setText(tel);
				}
				AlertDialog.Builder builder = new AlertDialog.Builder(
						view.getContext());
				builder.setMessage(getResources().getString(
						R.string.confirm_dial)
						+ "\n" + Global.HOT_LINE);
				builder.setTitle(getResources().getString(R.string.prompt));
				builder.setPositiveButton(
						getResources().getString(R.string.confirm),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Uri uri = Uri.parse("tel:" + Global.HOT_LINE); // 拨打电话号码的URI格式
								Intent intent = new Intent(); // 实例化Intent
								intent.setAction(Intent.ACTION_CALL); // 指定Action
								intent.setData(uri); // 设置数据
								MoreFrame.this.startActivity(intent);// 启动Acitivity
							}
						});
				builder.setNegativeButton(
						getResources().getString(R.string.cancel),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});

				builder.create().show();
				break;
			case R.id.check_update_layout:
				CheckUpdateAsyncTask checkUpdateAsyncTask = new CheckUpdateAsyncTask(getContext());
				checkUpdateAsyncTask.execute();
				findViewById(R.id.check_update_layout).setClickable(false);
				break;
			case R.id.clear_cache_layout:
				Toast.makeText(
						MoreFrame.this.getContext(),
						getContext().getResources().getString(
								R.string.clear_cache_finish),
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	}

	// 获得关于我们的信息
	private Map<String, String> getSettings() {
		Map<String, String> resultMap = new HashMap<String, String>();
		HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
		Map<String, String> map = new HashMap<String, String>();
		map.put("cityid", Global.NOW_CITY_ID != "" ? Global.NOW_CITY_ID : "1");
		Optional<JsonResult<CityAboutUsModel>> optional = httpClientUtils
				.getUnsignedByData(SETTING_URL, map, CityAboutUsModel.class);
		if (optional.isPresent()) {
			resultMap.put("status", String.valueOf(optional.get().isStatus()));
			resultMap.put("message", optional.get().getMessage());
			if (optional.get().getData() != null) {
				resultMap.put("tel", optional.get().getData().getTel());
				System.out.println("tel:" + optional.get().getData().getTel());
			}
		}

		return resultMap;
	}

	// 电话
	class HotLineAsyncTask extends AsyncTask<Void, Void, Map<String, String>> {

		@Override
		protected Map<String, String> doInBackground(Void... params) {
			return getSettings();
		}

		@Override
		protected void onPostExecute(Map<String, String> resultMap) {
			super.onPostExecute(resultMap);

			Log.d("MoreFrame------>> 1", Global.HOT_LINE);
			tel = resultMap.get("tel");
			Global.HOT_LINE = resultMap.get("tel");
			if (tel.equals("")) {
				hot_line.setText("031166683355");
			} else {
				hot_line.setText(tel);
			}

		}

	}

	// 更新
	class CheckUpdateAsyncTask extends
			AsyncTask<Void, Void, Map<String, String>> {
		private ProgressDialog mProgressBar;

		CheckUpdateAsyncTask(Context context) {
			mProgressBar = new ProgressDialog(context);
			mProgressBar.setCancelable(true);
			mProgressBar.setIndeterminate(false);
			mProgressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		}

		@Override
		protected void onPreExecute() {
			mProgressBar.setProgress(0);
			mProgressBar.setMessage("正在检查更新...");
			mProgressBar.show();
		}

		@Override
		protected Map<String, String> doInBackground(Void... params) {
			return checkUpdate();
		}

		@Override
		protected void onPostExecute(final Map<String, String> resultMap) {
			super.onPostExecute(resultMap);

			if (resultMap.size() > 0 && resultMap.get("status").equalsIgnoreCase("true")) {// 成功
				mProgressBar.dismiss();				
			try {
				System.out.println("版本:"+resultMap.get("version"));
				if (Global.getVersionCode()<Integer.parseInt(resultMap.get("version"))) {
					AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
					builder.setTitle("检查更新");
					builder.setMessage("发现新版本，是否更新:版本  "+Global.getVersionName());
					builder.setPositiveButton("更新", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {						
							Intent intentUpdate = new Intent(Intent.ACTION_VIEW,Uri.parse(resultMap.get("url")));
							startActivity(intentUpdate);					
						}
					});
					builder.setNegativeButton("取消",new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();			
						}
					});
					builder.create().show();				
					} else {
						AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
						builder.setTitle("检查更新");
						builder.setMessage("已是最新版本：版本号:"+Global.getVersionName());
						builder.setPositiveButton("确定",null);
						builder.create().show();
						//Toast.makeText(MoreFrame.this.getContext(),getContext().getResources().getString(R.string.no_update), Toast.LENGTH_SHORT).show();
					}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			} else {
				mProgressBar.dismiss();
				//Toast.makeText(MoreFrame.this.getContext(),resultMap.get("message"), Toast.LENGTH_SHORT).show();
			}
			findViewById(R.id.check_update_layout).setClickable(true);
		}
	}

	@Override
	public void onCreat() {
		// TODO Auto-generated method stub
		
	}
}
