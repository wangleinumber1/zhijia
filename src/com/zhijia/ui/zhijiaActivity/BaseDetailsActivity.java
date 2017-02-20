package com.zhijia.ui.zhijiaActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.google.common.base.Optional;
import com.zhijia.Global;
import com.zhijia.service.network.HttpClientUtils;
import com.zhijia.ui.R;
import com.zhijia.util.DownloadImageTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 详细页的基类
 */
public class BaseDetailsActivity extends Activity {

	// 当前图片的索引号
	protected static Integer currentItem = 0;
	// 滑动的图片集合
	protected List<ImageView> imageViews;
	// 图片标题正文的那些点
	protected List<View> dotsList;
	protected ViewPager viewPager;

	// 设置头部信息
	public void setHeader(Map<Integer, Object> map) {

		for (Map.Entry<Integer, Object> temp : map.entrySet()) {
			View view = this.findViewById(temp.getKey());
			if (view instanceof TextView) {
				Object valueObject = temp.getValue();
				if (valueObject instanceof String) {
					Log.d("BaseDetailsActivity", valueObject.toString());
					String[] values = ((String) valueObject).split(":");
					if (values.length == 2) {
						if (values[0].equalsIgnoreCase("visibility")) {
							view.setVisibility(Integer.parseInt(values[1]));
						}
						continue;
					}
				}
				((TextView) view).setText(temp.getValue().toString());
			}
		}

	}

	/**
	 * 这个高度必须要计算出来 要不listView和ScrollView有冲突
	 * 
	 * @param listView
	 */
	public void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();

		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;

		for (int i = 0; i < listAdapter.getCount(); i++) {

			View listItem = listAdapter.getView(i, null, listView);

			listItem.measure(0, 0);

			totalHeight += listItem.getMeasuredHeight();

		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();

		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));

		listView.setLayoutParams(params);

	}

	public void setShowImage(int imageId, String url) {
		final ImageView imageView = (ImageView) findViewById(imageId);
		new DownloadImageTask().doInBackground(url, imageView, null);
	}
	private Map<String, String> attention(Map<String, String> param) {

		String url = param.get("url");
		String hid = param.get("hid");
		Map<String, String> resultMap = new HashMap<String, String>();
		HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", hid);
		map.put("authstr", Global.USER_AUTH_STR);
		map.put("cityid", Global.NOW_CITY_ID);
		Optional<Map<String, String>> optional = httpClientUtils.postSignedMap(
				url, map, String.class);
		if (optional.isPresent()) {
			resultMap.put("status", optional.get().get("status"));
			resultMap.put("message", optional.get().get("message"));
			return resultMap;
		}
		return null;
	}

	public class DetailsOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.details_back:
				finish();
				break;
			}
		}
	}

	/*
	 * 关注任务
	 */
	public class AttentionAsyncTask extends
			AsyncTask<Map<String, String>, Void, Map<String, String>> {

		/*private ProgressDialog mProgressBar;

		public AttentionAsyncTask(Context context) {
			mProgressBar = new ProgressDialog(context);
			mProgressBar.setCancelable(true);
			mProgressBar.setIndeterminate(false);
			mProgressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressBar.setMax(100);
		}

		public AttentionAsyncTask(NewHouseDetailsActivity context) {
			mProgressBar = new ProgressDialog(context);
			mProgressBar.setCancelable(true);
			mProgressBar.setIndeterminate(false);
			mProgressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressBar.setMax(100);
		}

		@Override
		protected void onPreExecute() {
			mProgressBar.setProgress(0);
			mProgressBar.setMessage("正在提交您的数据...");
			mProgressBar.show();
		}
*/
		@Override
		protected Map<String, String> doInBackground(
				Map<String, String>... params) {
			return attention(params[0]);
		}

		@Override
		protected void onPostExecute(Map<String, String> resultMap) {
			if (resultMap != null) {
				//mProgressBar.dismiss();
				Toast.makeText(getApplicationContext(),
						resultMap.get("message"), Toast.LENGTH_SHORT).show();
			} else {
				//mProgressBar.dismiss();
				Toast.makeText(getApplicationContext(), "网络数据获取失败",
						Toast.LENGTH_SHORT).show();
			}

		}
	}

	/**
	 * 填充ViewPager页面的适配器
	 */
	public class SplashImageAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return imageViews.size();
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = imageViews.get(position);
			((ViewPager) container).addView(view);
			return imageViews.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object obj) {
			return view == obj;
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}
	}

	/**
	 * 当ViewPager中页面的状态发生改变时调用
	 */
	public class SplashImageListener implements ViewPager.OnPageChangeListener {
		private int oldPosition = 0;

		public void onPageSelected(int position) {
			currentItem = position;
			dotsList.get(oldPosition).setBackgroundResource(
					R.drawable.index_top_dot_unselected);
			dotsList.get(position).setBackgroundResource(
					R.drawable.index_top_dot_selected);
			oldPosition = position;

		}

		public void onPageScrollStateChanged(int state) {

		}

		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {

		}
	}

}
