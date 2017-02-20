package com.zhijia;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TabHost;
import android.widget.Toast;
import com.baidu.mapapi.BMapManager;
import com.nostra13.universalimageloader.cache.disc.impl.LimitedAgeDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.zhijia.util.BaiduMapUtil;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * 全局变量 Created by Along on 14-2-24.
 */
public class Global extends Application {

	// 接口URL域名前缀
	public static final String API_WEB_URL = "http://api.zhijia.com/test";
	// Server端的Cookie中sessionId Key
	public static final String SESSION_ID_KEY = "PHPSESSID";
	/**
	 * ImageLoader SD卡缓存时间.
	 */
	public static final long IMAGE_LOADER_DISK_CACHE_TTL = 1000 * 60 * 60 * 24;
	// SharedPreferences的名称
	public static final String SHARED_PREFERENCES_NAME = "ZHI_JIA";
	// 版本号，越来越大的整数
	public static final int VERSION = 1;
	// 引导屏的图片数量，计数器从1开始，图片资源的命名规则为：splash{index}.9.png
	public static final int SPLASH_PIC_COUNT = 5;
	// 包名，同AndroidManifest.xml
	public static final String PACKAGE_NAME = "com.zhijia.ui";
	// 日志的tag
	public static final String LOG_TAG = PACKAGE_NAME;
	// SQLLite数据库存放路径
	public static final String DB_PATH = "/data"
			+ Environment.getDataDirectory().getAbsolutePath() + "/"
			+ PACKAGE_NAME; // 存放路径
	// 设置日期格式
	public static final SimpleDateFormat DEFAULT_DF = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	public static final DecimalFormat DEFAULT_NF = new DecimalFormat("0.00");
	public static final String NOT_FIND_DATA = "暂无数据";
	public static final String ERROR_NET_WORK = "网络出问题了";
	// Server端的sessionId
	public static String SESSION_ID = null;
	public static int WINDOW_HEIGHT;

	// 用户登录后的凭证，空“”为未登录。
	public static String USER_AUTH_STR = "";
	// 已经认证手机号
	public static boolean USER_AUTHENTICATION_MOBILE = false;
	public static String USER_AVATAR = "http://v4.static.zhijia.com/images/common/nophoto_180x180.gif";
	// 当前城市，持久化到本地，第一次启动的时候加载。设置后会覆盖本地持久保存的值。
	public static String NOW_CITY = "";
	// 当前城市的ID
	public static String NOW_CITY_ID = "";
	public static String NOW_CITY_LATICOOR = "";
	public static String NOW_CITY_LONGCOOR = "";

	// 每页显示的条目
	public static int PAGE_SIZE = 10;

	// 最底端的TAB引用
	public static TabHost BOTTOM_TAB = null;
	// 服务热线
	public static String HOT_LINE = "031166683355";
	public static String WEB_SITE = "http://www.zhijia.com";
	/**
	 * 使用地图sdk前需先初始化BMapManager. BMapManager是全局的，可为多个MapView共用，它需要地图模块创建前创建，
	 * 并在地图地图模块销毁后销毁，只要还有地图模块在使用，BMapManager就不应该销毁
	 */
	public static BMapManager bMapManager = null;
	private static Global instance = null;

	public static Global getInstance() {
		return instance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;// 当前类
		initEngineManager(this);// 调用初始化地图管理类
		initImageLoader();//加载图片
		// 获取屏幕的高度
		WINDOW_HEIGHT = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay().getHeight();
		// 从SharedPreferences获取数据
		// Global.SHARED_PREFERENCES_NAME 存储市的名称
		// Context.MODE_PRIVATE：为默认操作模式,代表该文件是私有数据,只能被应用本身访问,在该模式下,写入的内容会覆盖原文件的内容
		SharedPreferences spf = this.getSharedPreferences(
				Global.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
		Global.NOW_CITY = spf.getString("nowCity", "");
		Global.NOW_CITY_ID = spf.getString("nowCityId", "");
		Global.NOW_CITY_LATICOOR = spf.getString("nowCityLaticoor", "");
		Global.NOW_CITY_LONGCOOR = spf.getString("nowCityLooncoor", "");
		Global.USER_AUTH_STR = spf.getString("authstr", "");
		Global.USER_AUTHENTICATION_MOBILE = spf.getBoolean(
				"isAuthenticationMobile", false);
	}

	/**
	 * 初始化地图管理类
	 * 
	 * @param context
	 */
	public void initEngineManager(Context context) {
		if (bMapManager == null) {
			bMapManager = new BMapManager(context);
		}

		if (!bMapManager.init(new BaiduMapUtil.MapGeneralListener())) {
			// getApplicationContext() 返回应用的上下文，生命周期是整个应用，应用摧毁它才摧毁
			Toast.makeText(getInstance().getApplicationContext(), "地图引擎初始化错误!",
					Toast.LENGTH_LONG).show();
		}
	}

	// 图片缓存
	// Universal-image-loader特征
	// 多线程下载图片，图片可以来源于网络，文件系统，项目文件夹assets中以及drawable中等
	// 支持随意的配置ImageLoader，例如线程池，图片下载器，内存缓存策略，硬盘缓存策略，图片显示选项以及其他的一些配置
	// 支持图片的内存缓存，文件系统缓存或者SD卡缓存
	// 支持图片下载过程的监听
	// /根据控件(ImageView)的大小对Bitmap进行裁剪，减少Bitmap占用过多的内存
	// /
	// 较好的控制图片的加载过程，例如暂停图片加载，重新开始加载图片，一般使用在ListView,GridView中，滑动过程中暂停加载图片，停止滑动的时候去加载图片
	// 提供在较慢的网络下对图片进行加载
	private void initImageLoader() {
		Log.d(Global.LOG_TAG, "INIT IMAGE CACHE");
		File cacheDir = StorageUtils.getCacheDirectory(this);
		// 继承Application
		// DisplayImageOptions，他可以配置一些图片显示的选项，比如图片在加载中ImageView显示的图片，
		//是否需要使用内存缓存，是否需要使用文件缓存等等，可供我们选择的配置如下
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisk(true).build();
		// ImageLoaderConfiguration是图片加载器ImageLoader的配置参数，使用了建造者模式，
		// 这里是直接使用了createDefault()方法创建一个默认的ImageLoaderConfiguration，当然我们还可以自己设置ImageLoaderConfiguration，设置如下
		// [java] view plaincopy在CODE上查看代码片派生到我的代码片
		ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(
				this)
				.memoryCache(new WeakMemoryCache())
				.discCache(
						new LimitedAgeDiscCache(cacheDir,
								Global.IMAGE_LOADER_DISK_CACHE_TTL))
				.defaultDisplayImageOptions(defaultOptions).build();
		ImageLoader.getInstance().init(configuration);
	}
}
