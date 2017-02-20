package com.zhijia.ui.zhijiaActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.platform.comapi.map.a.n;
import com.google.common.base.Optional;
import com.zhijia.Global;
import com.zhijia.service.data.Medol.CityAboutUsModel;
import com.zhijia.service.data.Medol.CityJsonModel;
import com.zhijia.service.data.Medol.CityModel;
import com.zhijia.service.data.Medol.JsonResult;
import com.zhijia.service.network.HttpClientUtils;
import com.zhijia.ui.R;
import com.zhijia.util.BaiduMapUtil;
import com.zhijia.util.DBUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zhijia.Global.HOT_LINE;

/**
 * 选择城市页面
 */
public class SelectCityActivity extends Activity {

	private final String URL = Global.API_WEB_URL + "/city/list?cityid=1";
	// 单击事件侦听
	private ClickListener clickListener = new ClickListener();
	// 地图定位必须的侦听用于定位后回调，以及本地API引用。
	private LocationClient mLocationClient = null;
	private BDLocationListener myListener = new BaiduLocationListener();
	// 定位出的城市
	private TextView gpsLocationView = null;
	private TextView fush;
	// ListView的头布局
	private View selectCityMidView = null;
	// 查找城市的输入框
	private EditText selectCitySearchInput = null;
	// 全部城市下拉框
	private ListView searchCityResultListView = null;
	// 搜索结果
	private ListView allCityListView = null;
	private ArrayList<CityModel> allCityNamesList;
	private SQLiteDatabase database;
	private DBUtil dbUtil;
	// 定位城市的一个Map
	//private static final Map<String, String> gpsCityMap = new HashMap<String, String>();
	private Map<String, CityJsonModel> getCityModelByNetwork(){
		long currentTimeMillis = System.currentTimeMillis();
		Optional<Map<String, CityJsonModel>> optional = HttpClientUtils.getInstance().getUnsignedMap(URL, null, CityJsonModel.class);
		System.out.println("时间： "+ String.valueOf(System.currentTimeMillis()- currentTimeMillis));
		if (optional.isPresent()) {
			return optional.get();
		}else {
			return null;
		}	
	}
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_city);
		dbUtil = new DBUtil(SelectCityActivity.this);
		long currentTimeMillis = System.currentTimeMillis();
		CityAsyncTask task = new CityAsyncTask(SelectCityActivity.this);
		task.execute();
		System.out.println("CityAsynctask时间： "+ String.valueOf(System.currentTimeMillis()- currentTimeMillis));
	}

	/**
	 * 设置热门城市列表
	 */
	public void setAdapterHotCityItem() {
		final ArrayList<CityModel> hotCityList = this.getHotCityNames();
		GridView hotCityGridView = (GridView) selectCityMidView.findViewById(R.id.hot_city_view);
		// 生成动态数组，并且转入数据
		final ArrayList<HashMap<String, String>> hotCityItemList = new ArrayList<HashMap<String, String>>();
		for (CityModel cityModel : hotCityList) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("cityName", cityModel.getCityName());
			hotCityItemList.add(map);
		}
		// 生成适配器的动态数组的元素，两者一一对应
		SimpleAdapter simpleAdapter = new SimpleAdapter(this,// 没什么解释
				hotCityItemList,// 数据来源
				R.layout.adapter_hot_city_item,// 热门城市元素布局的XML实现
				new String[] {"cityName" }, // 动态数组与hotCityItemList对应的子项
				new int[] { R.id.hot_city_item });
		// 添加并且显示
		hotCityGridView.setAdapter(simpleAdapter);
		hotCityGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
						Global.NOW_CITY = hotCityList.get(position).getCityName();
						Global.NOW_CITY_ID = hotCityList.get(position).getCityId();
						Global.NOW_CITY_LATICOOR = hotCityList.get(position).getLaticoor();
						Global.NOW_CITY_LONGCOOR = hotCityList.get(position).getLongcoor();
						System.out.println("城市:" + Global.NOW_CITY);
						writeNowCity();
						setResult(RESULT_OK, new Intent());
						finish();
					}
				});
		// 作为ListView的HeadView，必须计算好高度，否则显示不全。
		Drawable drawable = (Drawable) getResources().getDrawable(R.drawable.button_city);
		LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) hotCityGridView.getLayoutParams();
		// 这块必须要加2因为上下有间隔 所以要把间隔加上。
		int count = (hotCityList.size() % 3 == 0) ? (hotCityList.size() / 3) + 1 : (hotCityList.size() / 3) + 2;
		linearParams.height = drawable.getIntrinsicHeight() * count;
		hotCityGridView.setLayoutParams(linearParams);
	}

	@Override
	protected void onDestroy() {
		if (mLocationClient != null) {
			mLocationClient.stop();
		}
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 截获后退键
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			goBack();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 将当前城市持久化
	 */
	private void writeNowCity() {
		SharedPreferences spf = this.getSharedPreferences(Global.SHARED_PREFERENCES_NAME,
				Context.MODE_PRIVATE);
		spf.edit().putString("nowCity", Global.NOW_CITY).commit();
		spf.edit().putString("nowCityId", Global.NOW_CITY_ID).commit();
		spf.edit().putString("nowCityLaticoor", Global.NOW_CITY_LATICOOR).commit();
		spf.edit().putString("nowCityLooncoor", Global.NOW_CITY_LONGCOOR).commit();
	}

	/**
	 * 点后退按钮的处理
	 */
	private void goBack() {
		if (!Global.NOW_CITY.isEmpty()) {
			finish();
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					SelectCityActivity.this);
			builder.setMessage(this.getResources().getString(
					R.string.select_one_city));
			builder.setTitle(this.getResources().getString(R.string.prompt));
			builder.setPositiveButton(this.getResources().getString(R.string.confirm),new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			builder.create().show();
		}
	}

	/**
	 * 从数据库获取城市数据
	 * 
	 * @param nameLike
	 * 数据库Like匹配
	 * @return
	 */
	private ArrayList<CityModel> getCityNames(String nameLike) {
		database = dbUtil.openDataBase();
		ArrayList<CityModel> names = new ArrayList<CityModel>();
		String query = "SELECT * FROM " + DBUtil.CITY_TABLE+ " where name like '%" + nameLike + "%' or letters like '%"
				+ nameLike + "%' ORDER BY letter";
		Cursor cursor = database.rawQuery(query, null);
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			CityModel cityModel = new CityModel();
			cityModel.setCityName(cursor.getString(cursor.getColumnIndex("name")));
			cityModel.setNameSort(cursor.getString(cursor.getColumnIndex("letter")));
			cityModel.setCityId(cursor.getString(cursor.getColumnIndex("cityid")));
			cityModel.setLaticoor(cursor.getString(cursor.getColumnIndex("laticoor")));
			cityModel.setLongcoor(cursor.getString(cursor.getColumnIndex("longcoor")));
			names.add(cityModel);
		}
		dbUtil.closeDatabase();
		return names;
	}
	/**
	 * 从数据库获取热门城市数据
	 * 
	 * @return
	 */
	private ArrayList<CityModel> getHotCityNames() {
		database = dbUtil.openDataBase();
		ArrayList<CityModel> names = new ArrayList<CityModel>();
		String query = "SELECT * FROM " + DBUtil.CITY_TABLE + " where hot = 1";
		Cursor cursor = database.rawQuery(query, null);
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			CityModel cityModel = new CityModel();
			cityModel.setCityName(cursor.getString(cursor.getColumnIndex("name")));
			cityModel.setNameSort(cursor.getString(cursor.getColumnIndex("letter")));
			cityModel.setCityId(cursor.getString(cursor.getColumnIndex("cityid")));
			cityModel.setLaticoor(cursor.getString(cursor.getColumnIndex("laticoor")));
			cityModel.setLongcoor(cursor.getString(cursor.getColumnIndex("longcoor")));
			names.add(cityModel);
		}
		dbUtil.closeDatabase();
		return names;
	}

	class CityAsyncTask extends AsyncTask<Void, Integer, Map<String, CityJsonModel>> {

		   private ProgressDialog mProgressBar;  
		       
		   CityAsyncTask(Context context)  
		    {  
	         mProgressBar = new ProgressDialog(context);  
		     mProgressBar.setCancelable(true);  
		     mProgressBar.setIndeterminate(false);
	         mProgressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);   
		     }  

            @Override
           protected void onPreExecute() {
			    SharedPreferences spf = SelectCityActivity.this.getSharedPreferences("SelectCity", 0);
				boolean isFirstIn = true;
				isFirstIn = spf.getBoolean("isFirstIn", true);
				System.out.println("是否第一次进入：" + isFirstIn);
				if (isFirstIn) {
			      mProgressBar.setMessage("正在加载中...");
			      mProgressBar.show();
				}else {
					mProgressBar.dismiss();
				}
		}
		@Override
		protected Map<String, CityJsonModel> doInBackground(Void... params) {
		    //记录用户是否第一次进入
			SharedPreferences spf = SelectCityActivity.this.getSharedPreferences("SelectCity", 0);
			boolean isFirstIn = true;
			isFirstIn = spf.getBoolean("isFirstIn", true);
			System.out.println("是否第一次进入：" + isFirstIn);
			if (isFirstIn) {	
				Map<String, CityJsonModel> returnMap = getCityModelByNetwork();
				if (returnMap!=null) {
					// 初始化数据库和当前数据
					//判断是否加载完城市数据
					SharedPreferences pref1 = SelectCityActivity.this.getSharedPreferences("SelectCity", 0);
					Editor editor1 = pref1.edit();
					editor1.putBoolean("isCity", true);
					editor1.commit();
					database = dbUtil.openDataBase();
					database.execSQL("delete from " + DBUtil.CITY_TABLE);
					for (Map.Entry<String, CityJsonModel> temp : returnMap.entrySet()) {
					//Global.gpsCityMap.put(temp.getValue().getName(), temp.getKey());
					database.execSQL("insert into "+ DBUtil.CITY_TABLE
				    + "(cityid, letter, letters, name, hot, laticoor, longcoor, status) values(?,?,?,?,?,?,?,?)",
								new Object[] { temp.getValue().getCityid(),temp.getValue().getLetter(),temp.getValue().getLetters(),temp.getValue().getName(),
										temp.getValue().getHot(),
										temp.getValue().getLaticoor(),
										temp.getValue().getLongcoor(),
										temp.getValue().getStatus()});
					}
				dbUtil.closeDatabase();
				SharedPreferences pref = SelectCityActivity.this.getSharedPreferences("SelectCity", 0);
				Editor editor = pref.edit();
				editor.putBoolean("isFirstIn", false);
				editor.commit();
				return returnMap;
				}else {
					return null;
				}} else {
					/*Map<String, CityJsonModel> returnMap = getCityModelByNetwork();
					for (Map.Entry<String, CityJsonModel> temp : returnMap.entrySet()) {
						Global.gpsCityMap.put(temp.getValue().getName(), temp.getKey());
					}*/
					return null;
			}

		}

		@Override
		protected void onPostExecute(Map<String, CityJsonModel> map) {
			mProgressBar.dismiss();
			mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
			mLocationClient.registerLocationListener(myListener); // 注册监听函数
			LocationClientOption option = new LocationClientOption();
			option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
			option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度，默认值gcj02
			option.setScanSpan(1000);// 设置发起定位请求的间隔时间为5000ms
			option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
			option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
			mLocationClient.setLocOption(option);
			mLocationClient.start();
			if (mLocationClient != null && mLocationClient.isStarted()) { 
				mLocationClient.requestLocation();
				mLocationClient.requestPoi();
			} else {
				Log.d("LocSDK3", "mLocationClient is null or not started");
			}
			selectCitySearchInput = (EditText) findViewById(R.id.select_city_search_input);
			searchCityResultListView = (ListView) findViewById(R.id.select_city_search_result_list);
			selectCityMidView = LayoutInflater.from(SelectCityActivity.this).inflate(R.layout.select_city_mid, null);
			gpsLocationView = (TextView) selectCityMidView.findViewById(R.id.gps_location);
			fush=(TextView)selectCityMidView.findViewById(R.id.gps_city);
			fush.setOnClickListener(clickListener);
			// 注册侦听
			findViewById(R.id.back).setOnClickListener(clickListener);
			selectCitySearchInput.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence charSequence, int i,int i2, int i3) {

				}
				@Override
				public void onTextChanged(CharSequence charSequence, int i,int i2, int i3) {
					if (charSequence.length() == 0) {
						searchCityResultListView.setVisibility(View.GONE);
					} else {
						List<CityModel> searchResultList = SelectCityActivity.this.getCityNames(charSequence.toString().trim());
						if (searchResultList.size() > 0) {
							searchCityResultListView.setAdapter(new AllCityListAdapter(SelectCityActivity.this,searchResultList));
							searchCityResultListView.setVisibility(View.VISIBLE);
							searchCityResultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
										@Override
										public void onItemClick(AdapterView<?> parent,View view, int position, long id) {
											Global.NOW_CITY = ((CityModel) searchCityResultListView.getAdapter().getItem(position)).getCityName();
											Global.NOW_CITY_ID = ((CityModel) searchCityResultListView.getAdapter().getItem(position)).getCityId();
											Global.NOW_CITY_LATICOOR = ((CityModel) searchCityResultListView.getAdapter().getItem(position)).getLaticoor();
											Global.NOW_CITY_LONGCOOR = ((CityModel) searchCityResultListView.getAdapter().getItem(position)).getLongcoor();
											writeNowCity();
											setResult(RESULT_OK, new Intent());
											finish();
										}
									});
						} else {
							searchCityResultListView.setVisibility(View.GONE);
						}
					}
				}

				@Override
				public void afterTextChanged(Editable editable) {

				}
			});
			// 通过网络接口进行数据装载。。。。
			setAdapterHotCityItem();
			//原始高度
			Log.d(Global.LOG_TAG, selectCityMidView.getMeasuredHeight()+ "=======");
			// 初始化全部城市
			allCityListView = (ListView) findViewById(R.id.city_list);
			allCityListView.addHeaderView(selectCityMidView);
			allCityNamesList = getCityNames("");
			allCityListView.setAdapter(new AllCityListAdapter(SelectCityActivity.this, allCityNamesList));
			allCityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent,View view, int position, long id) {
							CityModel tempCityModel = (CityModel) allCityListView.getAdapter().getItem(position);
							Global.NOW_CITY = tempCityModel.getCityName();
							Global.NOW_CITY_ID = tempCityModel.getCityId();
							Global.NOW_CITY_LATICOOR = tempCityModel.getLaticoor();
							Global.NOW_CITY_LONGCOOR = tempCityModel.getLongcoor();
							writeNowCity();
							setResult(RESULT_OK, new Intent());
							finish();
					}
			});
		}						
	}
	// 点击事件的公共类
	class ClickListener implements View.OnClickListener {
		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.back: // 后退
				goBack();
				break;
			case R.id.gps_city:
				SharedPreferences spf = SelectCityActivity.this.getSharedPreferences("SelectCity", 0);
				boolean isFirstIn;
				isFirstIn = spf.getBoolean("isCity", false);
				if (isFirstIn) {
					Toast.makeText(SelectCityActivity.this, "城市数据已加载",Toast.LENGTH_SHORT).show();
					Intent intent=new Intent(SelectCityActivity.this,SelectCityActivity.class);
					startActivity(intent);
					finish();
				}else {
					SharedPreferences pref = SelectCityActivity.this.getSharedPreferences("SelectCity", 0);
					Editor editor = pref.edit();
					editor.putBoolean("isFirstIn", true);
					editor.commit();
					Intent intent=new Intent(SelectCityActivity.this,SelectCityActivity.class);
					startActivity(intent);
					finish();
				}					
			}
		}
	}

	/**
	 * 百度定位侦听回调
	 */
	public class BaiduLocationListener implements BDLocationListener {	
		@Override
		public void onReceiveLocation(final BDLocation location) {
			gpsLocationView.setText(location.getCity());
			gpsLocationView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					String tempCityName = ((TextView) view).getText().toString();
					if (gpsLocationView.getText().equals("")) {
						Toast.makeText(SelectCityActivity.this, "没有定位到城市",Toast.LENGTH_SHORT).show();
					}
					// 根据定位的城市名字获取对应城市id 获取不到给默认值
					/*if (gpsCityMap.get(tempCityName) != null) {*/
					if (Global.gpsCityMap.get(tempCityName) != null) {
						Global.NOW_CITY = tempCityName;
						Global.NOW_CITY_ID = Global.gpsCityMap.get(tempCityName);
						Global.NOW_CITY_LATICOOR = String.valueOf(location.getLatitude());
						Global.NOW_CITY_LONGCOOR = String.valueOf(location.getLongitude());
						writeNowCity();
						setResult(RESULT_OK, new Intent());
						finish();
					} else if (Global.gpsCityMap.get(tempCityName.substring(0,tempCityName.length() - 1)) != null) {
						Global.NOW_CITY = tempCityName.substring(0,tempCityName.length() - 1);
						Global.NOW_CITY_ID = Global.gpsCityMap.get(tempCityName.substring(0, tempCityName.length() - 1));
						Global.NOW_CITY_LATICOOR = String.valueOf(location.getLatitude());
						Global.NOW_CITY_LONGCOOR = String.valueOf(location.getLongitude());
						writeNowCity();
						setResult(RESULT_OK, new Intent());
						finish();
					} else {
	Toast.makeText(getApplicationContext(),"你所定位的城市不在我们支持范围，请选择其他城市。", Toast.LENGTH_SHORT)
					.show();
					}
				}
			});
			// 定位成功一次就行
			mLocationClient.stop();
			Log.d(Global.LOG_TAG, BaiduMapUtil.bdLocationToString(location));
		}

		@Override
		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}
			Log.d(Global.LOG_TAG, BaiduMapUtil.poiLocationToString(poiLocation));
		}
	}

	/**
	 * 全部城市的Adapter
	 */
	private class AllCityListAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private List<CityModel> list;

		public AllCityListAdapter(Context context, List<CityModel> list) {
			this.inflater = LayoutInflater.from(context);
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.adapter_all_city_item,null);
				holder = new ViewHolder();
				holder.alpha = (TextView) convertView.findViewById(R.id.city_letter);
				holder.name = (TextView) convertView.findViewById(R.id.city_name);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.name.setText(list.get(position).getCityName());
			String currentStr = list.get(position).getNameSort();
			System.out.println();
			String previewStr = (position - 1) >= 0 ? list.get(position - 1).getNameSort() : " ";
			if (!previewStr.equals(currentStr)) {
				holder.alpha.setVisibility(View.VISIBLE);
				holder.alpha.setText(currentStr);
			} else {
				holder.alpha.setVisibility(View.GONE);
			}
			return convertView;
		}

		private class ViewHolder {
			TextView alpha;
			TextView name;
		}

	}
}