package com.zhijia;

import android.app.AlertDialog;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TabHost;
import android.widget.Toast;

import com.baidu.frontia.FrontiaApplication;
import com.baidu.mapapi.BMapManager;
import com.google.common.base.Optional;
import com.nostra13.universalimageloader.cache.disc.impl.LimitedAgeDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.zhijia.service.data.Medol.CityJsonModel;
import com.zhijia.service.data.Medol.DeviceidModel;
import com.zhijia.service.data.Medol.JsonResult;
import com.zhijia.service.network.HttpClientUtils;
import com.zhijia.util.BaiduMapUtil;

import java.io.File;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局变量 Created by Along on 14-2-24.
 */
public class Global extends FrontiaApplication {
	/**
	 * 此策略下的类型为全局类型， 接口URL域名前缀
	 */
	public static final String API_WEB_URL = "http://api.zhijia.com/test";
	/**
	 * 此策略下的类型为全局类型，Server端的Cookie中sessionId Key
	 */
	public static final String SESSION_ID_KEY = "PHPSESSID";
	/**
	 * 此策略下的类型为全局类型，用以记录手机设备的唯一ID
	 */
	public static  String DeviceID="111111";
	public static boolean falg=isEmulator();
	/**
	 * ImageLoader SD卡缓存时间.
	 */
	public static final long IMAGE_LOADER_DISK_CACHE_TTL = 1000 * 60 * 60 * 24;
	// SharedPreferences的名称
	public static final String SHARED_PREFERENCES_NAME = "ZHI_JIA";
	//定位城市的一个Map
	public static final Map<String, String> gpsCityMap = new HashMap<String, String>();
	// 版本号，越来越大的整数
	public static final int VERSION = 1;
	public static final String VERSION1="1.2";
	// 引导屏的图片数量，计数器从1开始，图片资源的命名规则为：splash{index}.9.png
	public static final int SPLASH_PIC_COUNT = 5;
	// 包名，同AndroidManifest.xml
	public static final String PACKAGE_NAME = "com.zhijia.ui";
	// 日志的tag
	public static final String LOG_TAG = PACKAGE_NAME;
	// SQLLite数据库存放路径
	public static final String DB_PATH = "/data"+ Environment.getDataDirectory().getAbsolutePath() + "/"+ PACKAGE_NAME; // 存放路径
	// 设置日期格式
	public static final SimpleDateFormat DEFAULT_DF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
	public static long cachedTime = -1;
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

	private static boolean isEmulator() {
        return (Build.MODEL.equals("sdk")) || (Build.MODEL.equals("google_sdk"));
    }
	public static Global getInstance() {
		return instance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		//CrashHandler crashHandler = CrashHandler.getInstance();  
       // crashHandler.init(getApplicationContext()); 
		//Settings.System.putString(getContentResolver(), Settings.System.WIFI_STATIC_IP, "0.0.0.0");
		instance = this;// 当前类
		initEngineManager(this);// 调用初始化地图管理类
		initImageLoader();//加载图片
		// 获取屏幕的高度
		WINDOW_HEIGHT = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();
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
		System.out.println("Global:"+Global.USER_AUTH_STR);
		Global.USER_AUTHENTICATION_MOBILE = spf.getBoolean("isAuthenticationMobile", false);
		boolean fag=isNetworkAvailable(instance);
		System.out.println("是否连接网络:"+fag);
		//SharedPreferences spf = this.getSharedPreferences(Global.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
		boolean isFirstIn = spf.getBoolean("isFirstIn", true);
		if (fag || isFirstIn) {
			CityMapTask cityMapTask=new CityMapTask();
			cityMapTask.execute();
		}else {
			Toast.makeText(instance, "网络没有连接", Toast.LENGTH_SHORT).show();	
		}
    }     
	

	//获取版本
	 public static int getVersionCode() throws Exception
	   {
	           // 获取packagemanager的实例
		 int version = 0;
	           PackageManager packageManager = instance.getPackageManager();
	           // getPackageName()是你当前类的包名，0代表是获取版本信息
	           try {
	        	   PackageInfo packInfo = packageManager.getPackageInfo(instance.getPackageName(),0);
	        	   version = packInfo.versionCode;
	        	   } catch (NameNotFoundException e) {
	        	   // TODO Auto-generated catch block
	        	   e.printStackTrace();
	        	   }     
	           return version;
	   }
	 //获取版本号
	 public static String getVersionName() throws Exception
	   {
	           // 获取packagemanager的实例
		
	           PackageManager packageManager = instance.getPackageManager();
	     
	        	   PackageInfo packInfo = packageManager.getPackageInfo(instance.getPackageName(),0);
	        	   String version = packInfo.versionName;
	        	   
	           return version;
	   }
	 //城市模型任务
	public class CityMapTask extends AsyncTask<String, Void, Map<String, CityJsonModel> > {
    @Override
    protected Map<String, CityJsonModel>  doInBackground(String... params) {
	           return getCityModelByNetwork();
    }

    @Override
    protected void onPostExecute(Map<String, CityJsonModel>  jsonResult) {
    	if (jsonResult!=null) {
    	 for (Map.Entry<String, CityJsonModel> temp : jsonResult.entrySet()) {
    		Global.gpsCityMap.put(temp.getValue().getName(), temp.getKey());
    		}
		 }else {
			System.out.println("null");
		}
    	
     }
   }
	private Map<String, CityJsonModel> getCityModelByNetwork(){
		//long currentTimeMillis = System.currentTimeMillis();
		Optional<Map<String, CityJsonModel>> optional = HttpClientUtils.getInstance()
				.getUnsignedMap(Global.API_WEB_URL + "/city/list?cityid=1", null, CityJsonModel.class);
		//System.out.println("时间： "+ String.valueOf(System.currentTimeMillis()- currentTimeMillis));
		if (optional.isPresent()){
			return optional.get();
		}else {
			return null;
		}
		
	}

	/**
	 * 判断是否联网
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null) {
			
		} else {
			// 如果仅仅是用来判断网络连接 //则可以使用 cm.getActiveNetworkInfo().isAvailable();
			NetworkInfo[] info = cm.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
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
	@SuppressWarnings("deprecation")
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
		ImageLoaderConfiguration configuration =
		new ImageLoaderConfiguration.Builder(this)
		.memoryCache(new WeakMemoryCache()).discCache(new LimitedAgeDiscCache(cacheDir,Global.IMAGE_LOADER_DISK_CACHE_TTL))
		.defaultDisplayImageOptions(defaultOptions).build();
		ImageLoader.getInstance().init(configuration);
	}
}
