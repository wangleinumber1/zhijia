package com.zhijia.ui.zhijiaActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import static com.zhijia.Global.USER_AUTH_STR;
import static com.zhijia.Global.SHARED_PREFERENCES_NAME;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Optional;
import com.zhijia.Global;
import com.zhijia.service.data.Medol.CheckUpdateModel;
import com.zhijia.service.data.Medol.JsonResult;
import com.zhijia.service.data.Medol.NewHouseDetailJsonModel;

import com.zhijia.service.data.Medol.UserLoginInfoModel;
import com.zhijia.service.network.HttpClientUtils;
import com.zhijia.service.network.SlideJsonModel;
import com.zhijia.ui.R;
import com.zhijia.ui.frame.Frame;
import com.zhijia.util.DownloadImageTask;
import com.zhijia.util.ScreenUtil;

import static com.zhijia.Global.NOW_CITY_ID;
import static com.zhijia.Global.NOW_CITY;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 导航的首页View
 */
@SuppressWarnings("unused")
public class IndexFrame extends Frame {

	// 每个图的切换时间间隔
	private static final int INTERVAL_TIME = 3000;
	private static final String UPDATE_URL = Global.API_WEB_URL
			+ "/common/update";
	private static final String USER_LOGIN_INFO_URL = Global.API_WEB_URL
			+ "/member/mylogin";
	private static final String URL = Global.API_WEB_URL + "/house/slide";
	// 当前图片的索引号
	private static Integer currentItem = 0;
	private String LOG_TAG = "com.zhijia.ui.tabwidget.IndexFrame";
	// android-support-v4中的滑动组件
	private ViewPager viewPager;
	private ViewPager contentViewPager;
	private String url ;
	private TextView mailSize;
	private final String DETAIL_URL = Global.API_WEB_URL + "/xinfang/get";
	// 滑动的图片集合
	private List<ImageView> imageViews;
	// 主内容的图片集合
	private List<View> contentViews;
	// 滚动图片ID
	private int[] imageResId;
	// 图片标题正文的那些点
	private List<View> dotsList;
	// 切换当前显示的图片
	private Handler handler = new Handler();
	// 图片轮播任务
	private Runnable task = new ScrollTask();
	// 点击事件的侦听
	private ClickListener clickListener = new ClickListener();
	private View footerView;

	@Override
	public View onCreateView(LayoutInflater inflater) {
		return inflater.inflate(R.layout.tab_index, null);
	}

	@Override
	public void onCreate() {
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		imageViews = new ArrayList<ImageView>();
		mailSize = (TextView) findViewById(R.id.index_mail_size);
		dotsList = new ArrayList<View>();
		findViewById(R.id.index_top_one).setOnClickListener(clickListener);
		findViewById(R.id.index_top_three).setOnClickListener(clickListener);
		findViewById(R.id.search_input).setOnClickListener(clickListener);
		iniContentViewPager();
		initContentListener();
		initContentView();
		contentViewPager.setCurrentItem(0);
		CheckUpdateAsyncTask checkUpdateAsyncTask = new CheckUpdateAsyncTask();
		checkUpdateAsyncTask.execute();
	}
	// 第二块ViewPager
	private void iniContentViewPager() {
		this.contentViewPager = (ViewPager) findViewById(R.id.index_content_viewPager);
	}

	private void initContentListener() {
		this.contentViewPager.setOnPageChangeListener(new ContentViewListener());
	}

	private void initContentView() {
		contentViews = new ArrayList<View>();
		// 导航
		LinearLayout linearLayout = (LinearLayout) getActivity()
				.getLayoutInflater().inflate(R.layout.tab_index_content, null);
		LinearLayout linearLayoutTwo = (LinearLayout) getActivity()
				.getLayoutInflater().inflate(R.layout.tab_index_content_two,
						null);
		contentViews.add(linearLayout);
		contentViews.add(linearLayoutTwo);
		this.contentViewPager.setAdapter(new ContentViewAdapter());
	}

	// 这个方法是在onStart之后执行的。
	@Override
	public void onResume() {
		super.onResume();
			IndexFrameAsyncTask indexFrameAsyncTask = new IndexFrameAsyncTask();
			indexFrameAsyncTask.execute();
			viewPager.post(task);
			TextView appGPS = (TextView) findViewById(R.id.index_top_app_gps);
			appGPS.setText(Global.NOW_CITY);
			GetUserInfoAsyncTask getUserInfoAsyncTask = new GetUserInfoAsyncTask();
			getUserInfoAsyncTask.execute();
		
	}
	// 在onStop之前
	@Override
	public void onPause() {
		// 当Activity不可见的时候停止切换
		handler.removeCallbacks(task);
		super.onPause();
	}

	@Override
	public void onTabClick() {

	}
	/**
	 * 得到当前城市的轮播图片
	 * 
	 * @return List<SlideJsonModel>
	 */
	public JsonResult<List<SlideJsonModel>> getSlideByNetwork(){
		HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
		Log.d("NOW_CITY_ID_VALUE indexFrame", Global.NOW_CITY_ID);
		Map<String, String> map = new HashMap<String, String>();
		if (Global.NOW_CITY_ID.equals("")) {
			return null;
		}else {
			map.put("cityid", Global.NOW_CITY_ID);
			Optional<JsonResult<List<SlideJsonModel>>> optional = httpClientUtils.getUnsignedListByData(URL, map, SlideJsonModel.class);
			if (optional.isPresent()) {
				return optional.get();
			}else {
				return null;
			}
		}
	/*	map.put("cityid", Global.NOW_CITY_ID);
		Optional<JsonResult<List<SlideJsonModel>>> optional = httpClientUtils
				.getUnsignedListByData(URL, map, SlideJsonModel.class);
		if (optional.isPresent()) {
			return optional.get();
		}*/
		//return null;
	}

	/**
	 * 获得当前登录用户的用户信息
	 * 
	 * @return Map<String, String>
	 */
	private Map<String, String> getUserInfo() {
		Map<String, String> resultMap = new HashMap<String, String>();
		System.out.println("IndexFrame--->登陆标志位:"+Global.USER_AUTH_STR);
		if (!Global.USER_AUTH_STR.equals("")) {
			System.out.println("IndexFrame--->登陆标志位判断是否为空:"+Global.USER_AUTH_STR);
			HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
			Map<String, String> map = new HashMap<String, String>();
			map.put("authstr", Global.USER_AUTH_STR);
			Optional<JsonResult<UserLoginInfoModel>> optional = httpClientUtils.getUnsignedByData(USER_LOGIN_INFO_URL, map,UserLoginInfoModel.class);
			//System.out.println("IndexFrame--->1状态判断:"+optional.get().isStatus());
			//为不为空
			if (optional.isPresent()) {
				resultMap.put("status",String.valueOf(optional.get().isStatus()));
				System.out.println("IndexFrame--->2状态判断:"+optional.get().isStatus());
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
	/**
	*  检查更新
	*  @return Map<String, String>
	*/
	private Map<String, String> checkUpdate(){
		Map<String, String> resultMap = new HashMap<String, String>();
		HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
		Map<String, String> map = new HashMap<String, String>();
		//map.put("version", String.valueOf(Global.VERSION));
		try {
			map.put("version", String.valueOf(Global.getVersionCode()));
			map.put("type", "2");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		Optional<JsonResult<CheckUpdateModel>> optional = httpClientUtils.getUnsignedByData(UPDATE_URL, map, CheckUpdateModel.class);
		if (optional.isPresent()) {
			resultMap.put("status", String.valueOf(optional.get().isStatus()));
			resultMap.put("message", optional.get().getMessage());
			if (optional.get().getData() != null) {
				resultMap.put("version", optional.get().getData().getVersion().toString());
				resultMap.put("url", optional.get().getData().getUrl());
			}
		}

		return resultMap;
	}

/**
*  首页点击事件的公共类
*/
	class ClickListener implements View.OnClickListener {
		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.mid_icons_1: // 新房
				Intent newHouseIntent = new Intent(getActivity(),NewHouseActivity.class);
				startActivity(newHouseIntent);
				break;
			case R.id.mid_icons_2: // 二手房
				Intent oldHouseIntent = new Intent(getActivity(),OldHouseActivity.class);
				startActivity(oldHouseIntent);
				break;
			case R.id.mid_icons_3: // 租房
				Intent rentHouseIntent = new Intent(getActivity(),RentHouseActivity.class);
				startActivity(rentHouseIntent);
				break;
			case R.id.mid_icons_4: // 赚佣金
				Intent earnCommissionIntent = new Intent(getActivity(),EarnCommissionActivity.class);
				startActivity(earnCommissionIntent);
				break;
			case R.id.mid_icons_5: // 限时特惠
				Intent housePrivilegeIntent = new Intent(getActivity(),HouseCommonListActivity.class);
				housePrivilegeIntent.putExtra("listType", "Privilege");
				startActivity(housePrivilegeIntent);
				break;
			case R.id.mid_icons_6: // 看房团
				Intent condoTourListIntent = new Intent(getActivity(),CondoTourListActivity.class);
				startActivity(condoTourListIntent);
				break;
			case R.id.mid_icons_7: // 资讯
				Intent newsIntent = new Intent(getActivity(),NewsListActivity.class);
				startActivity(newsIntent);
				break;
			case R.id.mid_icons_8: // 小区房价
				Intent communityIntent = new Intent(getActivity(),CommunityMapActivity.class);
				startActivity(communityIntent);
				break;
			case R.id.mid_icons_9: // 计算器
				Intent calculatorIntent = new Intent(getActivity(),CalculatorActivity.class);
				startActivity(calculatorIntent);
				break;
			case R.id.index_top_one: // 选择城市
				Intent locationIntent = new Intent(getActivity(),SelectCityActivity.class);
				startActivityForResult(locationIntent, 100);
				break;
			case R.id.index_top_three: // 我的消息
				Intent messageIntent = new Intent(getActivity(),MessageIndexActivity.class);
				startActivity(messageIntent);
				break;
			case R.id.search_input: // 搜索
				Global.BOTTOM_TAB.setCurrentTab(1);
				break;
			}
		}
	}

	/**
	 * 换行切换任务
	 */
	private class ScrollTask implements Runnable {
		// 记录最后一次运行的时间，毫秒
		private long lastTime = 0;
		public void run() {
			synchronized (currentItem) {
				long time = System.currentTimeMillis();
				if (imageViews.size() > 0) {
					// 其实可以不这样，但是小米3同一时间点会run2次，所以控制下。
					if ((time - lastTime) > INTERVAL_TIME) {
						currentItem = (currentItem + 1) % imageViews.size();
						viewPager.setCurrentItem(currentItem);
						lastTime = time;
					}
					// Thread.currentThread().getName());
				}

				if (handler != null) {
					handler.postDelayed(this, INTERVAL_TIME); // 继续循环
				}
			}
		}

	}

	/**
	 * 轮转图片的点击事件
	 */
	class ImageViewOnClickListener implements View.OnClickListener {

		private SlideJsonModel slideJsonModel;

		public ImageViewOnClickListener(SlideJsonModel slideJsonModel) {
			this.slideJsonModel = slideJsonModel;
		}

		@Override
		public void onClick(View v) {
			// type 3直接打开URL地址 ， 2是新闻url，1 是新房id
			System.out.println(slideJsonModel.getUrl());
			if (slideJsonModel.getType().equals("3")) {
				Intent intentThree = new Intent(getContext(),
						IndexFrame_one.class);
				String url = slideJsonModel.getUrl();
				System.out.println("幻灯片URL:" + url);
				if (url.equals("#") || url.equals("")) {
					Toast.makeText(getContext(), "url null", Toast.LENGTH_LONG)
							.show();
				} else {
					intentThree.putExtra("url", url);
					startActivity(intentThree);
				}

			} else if (slideJsonModel.getType().equals("2")) {
				String url = slideJsonModel.getUrl();
				if (url.equals("#") || url.equals("")) {
					Toast.makeText(getContext(), "url null", Toast.LENGTH_LONG).show();
				} else {
					Intent newsDetailIntent = new Intent(getContext(),NewsDetailActivity.class);
					newsDetailIntent.putExtra("newsTypeName", "轮播资讯");
					newsDetailIntent.putExtra("url", slideJsonModel.getUrl());
					startActivity(newsDetailIntent);
				}

			} else if (slideJsonModel.getType().equals("1")) {
				System.out.println(slideJsonModel.getUrl());
				url = slideJsonModel.getUrl();
				if (url.equals("#") || url.equals("")) {
					Toast.makeText(getContext(), "url null", Toast.LENGTH_LONG).show();
				} else {
					NewHouseDetailsAsyncTask newHouseDetailsAsyncTask=new NewHouseDetailsAsyncTask();
					newHouseDetailsAsyncTask.execute(url);
				}

			}
		}
	}

	/*
	 * 新房跳转任务
	 * */
	public class NewHouseDetailsAsyncTask extends AsyncTask<String, Void, JsonResult<NewHouseDetailJsonModel>> {

		@Override
		protected JsonResult<NewHouseDetailJsonModel> doInBackground(
				String... params) {
			return getDetailDataByNetWork(params[0]);
		}

		@Override
		protected void onPostExecute(JsonResult<NewHouseDetailJsonModel> jsonResult) {
			if (jsonResult != null && jsonResult.isStatus()) {
				Intent newHouseDetails = new Intent(getContext(),NewHouseDetailsActivity.class);
				newHouseDetails.putExtra("hid", url);// type=1的url就是ID
				startActivity(newHouseDetails);
			}else {
				Toast.makeText(getContext(),"该楼盘不存在",Toast.LENGTH_LONG).show();
			}
		}
	}

	/*
	 * 若type=1 则是新房id 跳转到新房的详情页
	 * 这个方法是判断得到的新房id是否存在
	 * */
	public JsonResult<NewHouseDetailJsonModel> getDetailDataByNetWork(String id) {
		HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
		Map<String, String> map = new HashMap<String, String>();
		map.put("cityid", Global.NOW_CITY_ID);
		map.put("hid", id);
		Optional<JsonResult<NewHouseDetailJsonModel>> optional = httpClientUtils.getUnsignedByData(DETAIL_URL, map,
						NewHouseDetailJsonModel.class);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	class ContentViewListener implements ViewPager.OnPageChangeListener {

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {

		}

		@Override
		public void onPageSelected(int position) {
			Log.d(LOG_TAG, "ContentViewListener onPageSelected position==="
					+ position);
			contentViewPager.setCurrentItem(position);
			if (position == 0) {
				findViewById(R.id.v_dot0).setBackgroundResource(R.drawable.index_content_dot_selected);
				findViewById(R.id.v_dot1).setBackgroundResource(R.drawable.index_content_dot_unselected);
			} else if (position == 1) {
				findViewById(R.id.v_dot0).setBackgroundResource(R.drawable.index_content_dot_unselected);
				findViewById(R.id.v_dot1).setBackgroundResource(R.drawable.index_content_dot_selected);
			}
		}

		@Override
		public void onPageScrollStateChanged(int state) {

		}
	}

	class IndexFrameAsyncTask extends AsyncTask<Void, Void, JsonResult<List<SlideJsonModel>>> {

		@Override
		protected void onPreExecute() {
			
			super.onPreExecute();
		}
		@Override
		protected JsonResult<List<SlideJsonModel>> doInBackground(
				Void... params) {
			return getSlideByNetwork();
		}

		@Override
		protected void onPostExecute(JsonResult<List<SlideJsonModel>> jsonResult) {
			LinearLayout linearLayout = (LinearLayout) findViewById(R.id.dotsId);
			imageViews.clear(); 
			// 通过网络获取数据加载图片
			if (jsonResult != null && jsonResult.getData() != null) {
				Log.d("IndexFrameAsyncTask->jsonResult->size", jsonResult.getData().size() + "");
				List<SlideJsonModel> slideJsonModels = jsonResult.getData();
				for (SlideJsonModel tempSlideJson : slideJsonModels) {
					final ImageView imageView = new ImageView(getActivity());
					Log.d("IndexFrameAsyncTask->jsonResult->size",tempSlideJson.getPicUrl());
					new DownloadImageTask().doInBackground(( tempSlideJson).getPicUrl(), imageView,R.drawable.house_bg);
					imageView.setScaleType(ImageView.ScaleType.FIT_XY);
					imageView.setOnClickListener(new ImageViewOnClickListener(tempSlideJson));
					imageViews.add(imageView);
				}
			} else {
				imageResId = new int[3];
				imageResId[0] = getResources().getIdentifier("house_bg",
						"drawable", Global.PACKAGE_NAME);
				imageResId[1] = getResources().getIdentifier("house_bg",
						"drawable", Global.PACKAGE_NAME);
				imageResId[2] = getResources().getIdentifier("house_bg",
						"drawable", Global.PACKAGE_NAME);
				// 初始化图片资源
				for (int i = 0; i < imageResId.length; i++) {
					ImageView imageView = new ImageView(getActivity());
					imageView.setImageResource(imageResId[i]);
					imageView.setScaleType(ImageView.ScaleType.FIT_XY);
					imageViews.add(imageView);
				}
			}
			dotsList.clear();
			linearLayout.removeAllViews();
			for (int i = 0; i < imageViews.size(); i++) {
				View dotView = new View(getActivity());
				dotView.setBackgroundResource(R.drawable.index_top_dot_unselected);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						ScreenUtil.dps2pixels(5, getContext()),
						ScreenUtil.dps2pixels(5, getContext()));
				params.setMargins(ScreenUtil.dps2pixels(1.5, getContext()),
						ScreenUtil.dps2pixels(15, getContext()),
						ScreenUtil.dps2pixels(1.5, getContext()),
						ScreenUtil.dps2pixels(15, getContext()));
				dotView.setLayoutParams(params);
				dotsList.add(dotView);
				linearLayout.addView(dotView);
			}

			// 设置填充ViewPager页面的适配器
			viewPager.setAdapter(new SplashImageAdapter());
			// 设置一个监听器，当ViewPager中的页面改变时调用
			viewPager.setOnPageChangeListener(new SplashImageListener());

		}
	}

	/**
	 * 当ViewPager中页面的状态发生改变时调用
	 */
	class SplashImageListener implements ViewPager.OnPageChangeListener {
		private int oldPosition = 0;

		public void onPageSelected(int position) {
			currentItem = position;
			dotsList.get(oldPosition).setBackgroundResource(R.drawable.index_top_dot_unselected);
			dotsList.get(position).setBackgroundResource(R.drawable.index_top_dot_selected);
			oldPosition = position;
		}

		public void onPageScrollStateChanged(int state) {

		}

		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {

		}
	}

	class ContentViewAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return contentViews.size();
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = contentViews.get(position);
			// 注册点击事件
			LinearLayout imgLinearLayout = (LinearLayout) view;
			int layoutCount = imgLinearLayout.getChildCount();
			for (int i = 0; i < layoutCount; i++) {
				//第一層佈局
				LinearLayout tempLayout = (LinearLayout) imgLinearLayout
						.getChildAt(i);
				int imageCount = tempLayout.getChildCount();
				//子控件
				for (int j = 0; j < imageCount; j++) {
					ImageView imageView = (ImageView) tempLayout.getChildAt(j);
					if (imageView.getVisibility() == View.VISIBLE) {
						imageView.setOnClickListener(clickListener);
					}
				}
			}

			((ViewPager) container).addView(view);
			return contentViews.get(position);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			Log.d(LOG_TAG, "ContentViewAdapter destroyItem position==="
					+ position);
		}
	}

	/**
	 * 填充ViewPager页面的适配器
	 */
	class SplashImageAdapter extends PagerAdapter {

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

	//用户登陆状态
	class GetUserInfoAsyncTask extends AsyncTask<Void, Void, Map<String, String>> {
		@Override
		protected Map<String, String> doInBackground(Void... params) {
			return getUserInfo();
		}

		@Override
		protected void onPostExecute(Map<String, String> resultMap) {
			super.onPostExecute(resultMap);
			System.out.println("登陆状态:"+resultMap.get("status")+"size"+resultMap.size());
			if (resultMap.size() > 0 && resultMap.get("status").equalsIgnoreCase("true")) {// 成功判断两个字符串是否相等（不区分大小写）
				if (Integer.parseInt(resultMap.get("count")) > 0) {
					mailSize.setText(resultMap.get("count"));
					mailSize.setVisibility(View.VISIBLE);
				} else {
					mailSize.setVisibility(View.INVISIBLE);
				}
			} else {// 失败
				/*Global.USER_AUTH_STR = "";
				Global.USER_AUTHENTICATION_MOBILE = false;
				SharedPreferences spf = getContext().getSharedPreferences(Global.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
				spf.edit().putString("authstr", "").commit();
				spf.edit().putBoolean("isAuthenticationMobile", false).commit();
				mailSize.setVisibility(View.INVISIBLE);
				if (resultMap.get("message") != null && resultMap.get("message").length() > 0) {
					Toast.makeText(IndexFrame.this.getContext(),resultMap.get("message"), Toast.LENGTH_SHORT).show();
				}*/
			}
		}
	}

	//检查更新
	class CheckUpdateAsyncTask extends AsyncTask<Void, Void, Map<String, String>> {
		@Override
		protected Map<String, String> doInBackground(Void... params) {
			return checkUpdate();
		}

		@Override
		protected void onPostExecute(final Map<String, String> resultMap) {
			super.onPostExecute(resultMap);
			if (resultMap.size() > 0&& resultMap.get("status").equalsIgnoreCase("true")) {// 成功	
				try {
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
						//Toast.makeText(IndexFrame.this.getContext(),getContext().getResources().getString(R.string.has_update), Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(IndexFrame.this.getContext(),getContext().getResources().getString(R.string.no_update), Toast.LENGTH_SHORT).show();
					}
				} catch (NumberFormatException e) {

					e.printStackTrace();
				} catch (NotFoundException e) {

					e.printStackTrace();
				} catch (Exception e) {

					e.printStackTrace();
				}
			} else {
				// Toast.makeText(IndexFrame.this.getContext(),
				// resultMap.get("message"), Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void onCreat() {
		// TODO Auto-generated method stub
		
	}
}
