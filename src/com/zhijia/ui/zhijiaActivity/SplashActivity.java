package com.zhijia.ui.zhijiaActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.*;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.zhijia.Global;
import com.zhijia.ui.R;
import com.zhijia.util.IntentUtil;
import com.zhijia.util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 过渡页，如果已经进来过，并且软件版本没有更新则直接进首页。
 */
public class SplashActivity extends BaseActivity {
	// 屏幕持续时间，毫秒
	private final long SPLASH_DURATION = 1000L;
	private ViewPager viewPager;
	private List<View> dotsList;
	private ViewGroup main, group;
	private ImageView imageView;
	private ImageView[] imageViews;
	private static final String Tag = "SplashActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 全屏
		// 隐去标题栏（应用程序的名字）
		// this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 隐去状态栏部分(电池等图标和一切修饰部分)
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		SharedPreferences spf = this.getSharedPreferences(
				Global.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
		boolean isFirstIn = spf.getBoolean("isFirstIn", true);
		try {
			int version = spf.getInt("version", Global.getVersionCode());
			// 如果第一次进来，或者因为升级更新版本号更高则显示Splash，否则跳过。
			if (isFirstIn || version < Global.getVersionCode()) {
				setContentView(R.layout.splash);
				LayoutInflater inflater = getLayoutInflater();
				dotsList = new ArrayList<View>();
				for (int i = 1; i <= Global.SPLASH_PIC_COUNT; i++) {
					View tempView = inflater
							.inflate(R.layout.splash_item, null);
					ImageView imageView = (ImageView) tempView
							.findViewById(R.id.splashImage);
					imageView.setBackgroundDrawable(getResources().getDrawable(
							this.getApplicationContext()
									.getResources()
									.getIdentifier(
											"splash" + String.valueOf(i),
											"drawable", Global.PACKAGE_NAME)));
					// 最后一张处理点击事件，直接保存版本和已经非第一次进入，然后跳转到首屏
					if (i == Global.SPLASH_PIC_COUNT) {
						imageView
								.setOnClickListener(new ImageView.OnClickListener() {// 创建监听
									public void onClick(View v) {
										// 先记录版本和已经非第一次进入
										SharedPreferences spf = v
												.getContext()
												.getSharedPreferences(
														Global.SHARED_PREFERENCES_NAME,
														Context.MODE_PRIVATE);
										spf.edit()
												.putBoolean("isFirstIn", false)
												.commit();
										try {
											spf.edit()
													.putInt("version",
															Global.getVersionCode())
													.commit();
										} catch (Exception e) {
											e.printStackTrace();
										}
										// Intent intent = new Intent(
										// SplashActivity.this,
										// MainActivity.class);
										// SplashActivity.this
										// .startActivity(intent);
										// SplashActivity.this.finish();
										IntentUtil.activityForward(
												SplashActivity.this,
												MainActivity.class, null, true);
									}
								});
					}
					dotsList.add(tempView);
				}
				imageViews = new ImageView[Global.SPLASH_PIC_COUNT];
				ViewGroup splashMain = (ViewGroup) inflater.inflate(
						R.layout.splash, null);
				// group是R.layou.main中的负责包裹小圆点的LinearLayout.
				ViewGroup group = (ViewGroup) splashMain
						.findViewById(R.id.splashGroup);
				viewPager = (ViewPager) splashMain
						.findViewById(R.id.splashViewPager);
				viewPager.setAdapter(new SplashImageAdapter());
				viewPager.setOnPageChangeListener(new SplashImageListener());
				for (int i = 0; i < dotsList.size(); i++) {
					imageView = new ImageView(this.getApplicationContext());
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
							ScreenUtil.dps2pixels(5,
									this.getApplicationContext()),
							ScreenUtil.dps2pixels(5,
									this.getApplicationContext()));
					params.setMargins(
							ScreenUtil.dps2pixels(1.5,
									this.getApplicationContext()),
							0,
							ScreenUtil.dps2pixels(1.5,
									this.getApplicationContext()), 0);
					imageView.setLayoutParams(params);
					imageViews[i] = imageView;
					if (i == 0) {
						// 默认进入程序后第一张图片被选中;
						imageViews[i]
								.setBackgroundResource(R.drawable.splash_dot_selected);
					} else {
						imageViews[i]
								.setBackgroundResource(R.drawable.splash_dot_unselected);
					}
					group.addView(imageView);
				}
				setContentView(splashMain);
			} else {// 启动主页面
				System.out.println("启动主页面");
				SplashActivity.this.setContentView(R.layout.splash_default);
				// new InitAsyncTask().execute();
				IntentUtil.activityForward(SplashActivity.this,
						MainActivity.class, null, true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	class SplashImageAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return dotsList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView(dotsList.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			((ViewPager) container).addView(dotsList.get(position));
			return dotsList.get(position);
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
		}

		@Override
		public void startUpdate(ViewGroup container) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void finishUpdate(ViewGroup container) {
		}
	}

	class SplashImageListener implements OnPageChangeListener {
		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {

		}

		@Override
		public void onPageSelected(int position) {
			for (int i = 0; i < imageViews.length; i++) {
				imageViews[position]
						.setBackgroundResource(R.drawable.splash_dot_selected);
				if (position != i) {
					imageViews[i]
							.setBackgroundResource(R.drawable.splash_dot_unselected);
				}
			}
		}

		@Override
		public void onPageScrollStateChanged(int state) {

		}
	}

	// 在这里做一些初始化工作
//	class InitAsyncTask extends AsyncTask<Void, Void, Void> {
//		@Override
//		protected Void doInBackground(Void... voids) {
//			try {
//				Thread.sleep(SPLASH_DURATION);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(Void aVoid) {
//			super.onPostExecute(aVoid);
//			// Intent intent = new Intent(SplashActivity.this,
//			// MainActivity.class);
//			// SplashActivity.this.startActivity(intent);
//			// SplashActivity.this.finish();
//
//		}
//	}
}
