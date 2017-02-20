package com.zhijia.ui.zhijiaActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.*;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.google.common.base.Optional;
import com.zhijia.Global;
import com.zhijia.Enum.*;
import com.zhijia.service.data.Medol.BaseModel;
import com.zhijia.service.data.Medol.ConditionJsonModel;
import com.zhijia.service.data.Medol.HouseMapListJsonModel;
import com.zhijia.service.data.Medol.JsonResult;
import com.zhijia.service.data.Medol.OldHouseJsonModel;
import com.zhijia.service.data.Medol.RoomJsonModel;
import com.zhijia.service.data.Medol.SearchModel;
import com.zhijia.service.network.HttpClientUtils;
import com.zhijia.ui.R;
import com.zhijia.ui.list.adapter.ItemAdapter;
import com.zhijia.ui.list.interfaces.IListDataNetWork;
import com.zhijia.ui.list.page.Page;
import com.zhijia.util.BaiduMapUtil;
import com.zhijia.util.DefaultDataUtils;
import com.zhijia.util.MapOverLayModel;
import com.zhijia.util.ScreenUtil;
import com.zhijia.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 租房地图页
 */
public class RentHouseMapActivity extends BaseHouseConditionActivity implements IListDataNetWork<HouseMapListJsonModel>, AbsListView.OnScrollListener{

    private final int[] areas = new int[]{R.id.house_area_one, R.id.house_area_two, R.id.house_area_three, R.id.house_area_four};

    private final String[] texts = new String[]{"区域", "租金", "面积", "更多"};

    private final String LIST_URL = Global.API_WEB_URL + "/house/map" ;
    private final String CONDITION_URL = Global.API_WEB_URL + "/house/rentfield";
    private final String LIST_MAP_URL = Global.API_WEB_URL+"/house/listmap" ;
    //是否手动触发请求定位
    boolean isRequest = false;
    //MapView 是地图主控件
    private MapView mMapView = null;
    //用MapController完成地图控制
    private MapController mMapController = null;
    //定位图层
    private MyLocationOverlay myLocationOverlay = null;
    private HourseCountOverlay myOverlay = null;
    //地图定位必须的侦听用于定位后回调，以及本地API引用。
    private LocationClient mLocationClient = null;
    private BDLocationListener myListener = new BaiduLocationListener();
    private LocationData locData = null;
    //所有点的坐标列表，有序
    private List<MapOverLayModel> pointList;
    private String minX,maxX,minY,maxY ;

    private String currentHid = "";

    private AlertDialog dialog ;
    //Item的列表视图父控件
    private ListView listView  ;

    private ItemAdapter itemAdapter ;

    //最后的可视项索引
    private int visibleLastIndex = 0;
    //当前窗口可见项总数
    private int visibleItemCount;

    private View popupWindowView ;
    private  Global app;
    private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Map<Integer, Object> map = (Map<Integer, Object>) parent.getAdapter().getItem(position);
            String hid = (String) map.get(-1);
            Intent rentHouseDetails = new Intent(getApplicationContext(), OldHouseDetailsActivity.class);
            rentHouseDetails.putExtra("hid", hid);
            rentHouseDetails.putExtra("housetype", "2");
            rentHouseDetails.putExtra("identity", "2");
            startActivity(rentHouseDetails);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Global app = (Global) this.getApplication();
        if (app.bMapManager == null) {
            app.bMapManager = new BMapManager(getApplicationContext());
            /**
             * 如果BMapManager没有初始化则初始化BMapManager
             */
            app.bMapManager.init(new BaiduMapUtil.MapGeneralListener());
        }

        /**
         * 由于MapView在setContentView()中初始化,所以它需要在BMapManager初始化之后
         */
        setContentView(R.layout.new_house_map);
        mMapView = (MapView) findViewById(R.id.map_view);
        mMapView.regMapViewListener(Global.bMapManager, new MyMKMapViewListener());
        //获取地图控制器
        mMapController = mMapView.getController();
        //设置地图是否响应点击事件
        mMapController.enableClick(true);
        //设置地图缩放级别
        mMapController.setZoom(16);
        //显示内置缩放控件
        mMapView.setBuiltInZoomControls(true);
        //设定地图中心点
        GeoPoint p = new GeoPoint((int) (Double.parseDouble(Global.NOW_CITY_LATICOOR) * 1E6), (int) (Double.parseDouble(Global.NOW_CITY_LONGCOOR) * 1E6));
        mMapController.setCenter(p);

        //定位图层初始化
        myLocationOverlay = new MyLocationOverlay(mMapView);
        //设置定位数据
        myLocationOverlay.setData(locData);
        //添加定位图层
        mMapView.getOverlays().add(myLocationOverlay);
        myLocationOverlay.enableCompass();
        //initOverlay();
        //修改定位数据后刷新图层生效
        mMapView.refresh();

        mLocationClient = new LocationClient(getApplicationContext()); //声明LocationClient类
        mLocationClient.registerLocationListener(myListener); //注册监听函数
        locData = new LocationData();

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置定位模式
        option.setCoorType("bd09ll");//返回的定位结果是百度经纬度，默认值gcj02
        option.setScanSpan(1000);//设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);//返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向
        mLocationClient.setLocOption(option);
        mLocationClient.start();

        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.requestLocation();
            mLocationClient.requestPoi();
        } else {
            Log.d("LocSDK3", "mLocationClient is null or not started");
        }
//        BaseModel baseModel = new BaseModel(R.layout.common_adapater_house_three_centre,R.id.house_three_centre, ListType.RENT) ;
//        baseModel.setAreas(areas);
//        baseModel.setTexts(texts);
        //this.setCommonAdapter(baseModel);

        setCondition();
        //注册事件绑定
        findViewById(R.id.house_back).setOnClickListener(new HouseOnClickListener());
        findViewById(R.id.new_house_list_map).setBackgroundResource(R.drawable.new_house_button_map);
        findViewById(R.id.new_house_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent rentHouseIntent = new Intent(RentHouseMapActivity.this.getApplicationContext(), RentHouseActivity.class);
                startActivity(rentHouseIntent);
                RentHouseMapActivity.this.finish();
            }
        });
        //用户主动请求定位到当前位置
        findViewById(R.id.custom_loc_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isRequest = true;
            }
        });
    }

    private void setCondition() {
        this.setCommonHouseAreas(areas, texts);
        this.setConditionNetwork(this);
        this.setCommonHouseTitle(R.id.house_top_app_gps, R.string.rent_house_title);
        this.bindEventArea(areaIds, relativeId, ListType.RENT);
        this.startConditionAsyncTask(CONDITION_URL);
    }

    private void setListData() {
        BaseModel baseModel = new BaseModel();
        setBaseModel(baseModel);
        this.setListDataNetWork(this);
        startListTask(baseModel);
    }

    @Override
    protected void onPause() {
        //MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
        mMapView.onPause();
        if(app.bMapManager!=null) {
			app.bMapManager.stop();
		}
        super.onPause();
    }

    @Override
    protected void onResume() {
        //MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
        mMapView.onResume();
        if (app.bMapManager!=null) {
			app.bMapManager.start();
		}
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        //MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
        mMapView.destroy();
        mLocationClient.stop();
        if (app.bMapManager!=null) {
			app.bMapManager.destroy();
		    app.bMapManager=null;
		}
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mMapView.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * 初始化界面上自定义图层
     */
    private void initOverlay() {
        PopupClickListener popListener = new PopupClickListener() {
            @Override
            public void onClickedPopup(int index) {
                Intent rentHouseDetails = new Intent(getApplicationContext(), OldHouseDetailsActivity.class);
                rentHouseDetails.putExtra("hid", currentHid);
                rentHouseDetails.putExtra("housetype", "2");
                rentHouseDetails.putExtra("identity", "2");
                startActivity(rentHouseDetails);
            }
        };

        //创建自定义overlay
        myOverlay = new HourseCountOverlay(getResources().getDrawable(R.drawable.map_location_icons), mMapView, getApplicationContext(), popListener);
        mMapView.getOverlays().clear();

        //准备overlay 数据
        for (int i = 0; i < pointList.size(); i++) {
            OverlayItem overlayItem = new OverlayItem(pointList.get(i).getGeoPoint(), "", "");
            //弹出自定义View
            View popView = LayoutInflater.from(this.getApplicationContext()).inflate(R.layout.map_count_overlay, null);
            ((TextView) popView.findViewById(R.id.map_house_count)).setText(Html.fromHtml(pointList.get(i).getHtmlText()));
            overlayItem.setMarker(ScreenUtil.view2Drawable(popView));
            myOverlay.addItem(overlayItem);
        }
        mMapView.getOverlays().add(myOverlay);
        //刷新地图
        mMapView.refresh();
    }

    @Override
    public JsonResult<List<HouseMapListJsonModel>> getListDataByNetWork(String url, BaseModel baseModel) {
        HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
        Map<String, String> map = new HashMap<String, String>();
        SearchModel searchModel = baseModel.getSearchModel();
        if (searchModel == null) {
            searchModel = new SearchModel();
        }
        searchModel.setMinX(minX);
        searchModel.setMaxX(maxX);
        searchModel.setMinY(minY);
        searchModel.setMaxY(maxY);
        map = searchModel.toMap();
        map.put("cityid", Global.NOW_CITY_ID);
        map.put("housetype", "2");
        Optional<JsonResult<List<HouseMapListJsonModel>>> optional = httpClientUtils.getUnsignedListByData(url, map, HouseMapListJsonModel.class);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    @Override
    public void startListTask(BaseModel baseModel) {
        RentHouseMapAsyncTask rentHouseMapAsyncTask = new RentHouseMapAsyncTask();
        rentHouseMapAsyncTask.execute(baseModel);
    }

    @Override
    public void setNetWorkErrorValue(BaseModel baseModel) {
        setCommonHouseCount(baseModel.getTextViewId(), "0",OLD_RENT_COMMUNITY_COUNT);
        setCommonHouseCount(R.id.gone_house_count, "0",OLD_RENT_COMMUNITY_COUNT);
        List<Map<Integer, Object>> resultList = new ArrayList<Map<Integer, Object>>();
        DefaultDataUtils.setDefaultHint(resultList, Global.ERROR_NET_WORK);
        baseModel.setListData(resultList);
        ListView listView = (ListView) findViewById(baseModel.getListViewId());
        ItemAdapter itemAdapter = new ItemAdapter(RentHouseMapActivity.this, baseModel.getItemLayoutInflaterId(), baseModel.getListData());
        setItemAdapter(itemAdapter);
        listView.setAdapter(itemAdapter);
        Toast.makeText(RentHouseMapActivity.this, Global.ERROR_NET_WORK, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        Log.d("OldHouseMapActivity->adapter", itemAdapter.getCount() + "");
        int itemsLastIndex = itemAdapter.getCount() - 1;    //数据集最后一项的索引
        int lastIndex = itemsLastIndex + ((ListView) view).getHeaderViewsCount() + ((ListView) view).getFooterViewsCount();
        Log.d("OldHouseMapActivity", "visibleLastIndex==" + visibleLastIndex + "  lastIndex==" + lastIndex);
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && visibleLastIndex == lastIndex) {
            footerView.setVisibility(View.VISIBLE);
            HousePrivilegeTask task = new HousePrivilegeTask();
            task.execute();
        } else {
            footerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCounts, int totalItemCount) {
        visibleItemCount = visibleItemCounts;
        visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
        Log.d("OldHouseMapActivity", "visibleItemCount==" + visibleItemCount + "  firstVisibleItem====" + firstVisibleItem);
    }

    public JsonResult<List<OldHouseJsonModel>> getCommunityRentHouseList(BaseModel baseModel){
        HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
        Map<String, String> map = new HashMap<String, String>();
        SearchModel searchModel = baseModel.getSearchModel();
        if (searchModel != null) {
            map = searchModel.toUnXYMap();
        }
        map.put("cityid", Global.NOW_CITY_ID);
        map.put("housetype", "2");
        if (baseModel.getPage() != null) {
            map.put("page", String.valueOf(baseModel.getPage().getPage()));
        } else {
            map.put("page", "1");
        }
        Optional<JsonResult<List<OldHouseJsonModel>>> optional = httpClientUtils.getUnsignedListByData(LIST_MAP_URL, map, OldHouseJsonModel.class);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    private void setListData(List<OldHouseJsonModel> list, List<Map<Integer, Object>> resultList, ItemAdapter itemAdapter) {
        for (OldHouseJsonModel jsonModel : list) {
            Map<Integer, Object> map = new HashMap<Integer, Object>();
            Log.d("setListData===", jsonModel.getHid() + "");
            map.put(-1, jsonModel.getHid());
            map.put(R.id.adapter_house_item_two_image, jsonModel.getTitlepic());
            String tag = jsonModel.getTag();
            if (tag != null) {
                if (tag.equals("1")) {
                    map.put(R.id.adapter_house_item_two_top_mark, R.drawable.house_icons_1 + ":VISIBLE");
                } else if (tag.equals("2")) {
                    map.put(R.id.adapter_house_item_two_top_mark, R.drawable.house_icons_5 + ":VISIBLE");
                } else if (tag.equals("3")) {
                    map.put(R.id.adapter_house_item_two_top_mark, R.drawable.house_icons_4 + ":VISIBLE");
                }
            }
            String usertype = jsonModel.getUsertype();
            if (usertype != null) {
                if (usertype.equals("1")) {
                    map.put(R.id.adapter_house_item_two_bottom, R.drawable.singler + ":VISIBLE");
                }
            }
            map.put(R.id.adapter_house_item_two_name, StringUtils.replace(jsonModel.getTitle()));
            map.put(R.id.adapter_house_item_two_area, jsonModel.getAreaname() + " " + jsonModel.getCommunityname());
            map.put(R.id.adapter_house_item_two_type, jsonModel.getUnit() + " " + jsonModel.getArea());
            List<String> tagtextList = jsonModel.getTagtext();
            if (tagtextList != null && tagtextList.size() > 0) {
                switch (tagtextList.size()) {
                    case 1:
                        //由于tagtext给的是一个空字符串，所以这块要判断一下
                        if (!tagtextList.get(0).equals("")) {
                            map.put(R.id.adapter_house_item_two_des, tagtextList.get(0) + ":VISIBLE");
                        }
                        break;
                    case 2:
                        map.put(R.id.adapter_house_item_two_des, tagtextList.get(0) + ":VISIBLE");
                        map.put(R.id.adapter_house_item_two_des_two, tagtextList.get(1) + ":VISIBLE");
                        break;
                    case 3:
                        map.put(R.id.adapter_house_item_two_des, tagtextList.get(0) + ":VISIBLE");
                        map.put(R.id.adapter_house_item_two_des_two, tagtextList.get(1) + ":VISIBLE");
                        map.put(R.id.adapter_house_item_two_des_three, tagtextList.get(2) + ":VISIBLE");
                        break;
                    case 4:
                        map.put(R.id.adapter_house_item_two_des, tagtextList.get(0) + ":VISIBLE");
                        map.put(R.id.adapter_house_item_two_des_two, tagtextList.get(1) + ":VISIBLE");
                        map.put(R.id.adapter_house_item_two_des_three, tagtextList.get(2) + ":VISIBLE");
                        map.put(R.id.adapter_house_item_two_des_four, tagtextList.get(3) + ":VISIBLE");
                        break;
                }
            }
            map.put(R.id.adapter_house_item_two_price, jsonModel.getPrice());
            if (resultList != null) {
                resultList.add(map);
            }
            if (itemAdapter != null) {
                itemAdapter.addItem(map);
                itemAdapter.notifyDataSetChanged();
            }
        }
    }

    public void loadData(){
        BaseModel baseModel = getBaseModel();
        int totalPage = baseModel.getPage().getTotalPage();
        int nextPage = baseModel.getPage().getNextPage();
        int nowPage = baseModel.getPage().getPage();
        Log.d("BaseModel page", totalPage + "");
        Log.d("BaseModel page", nextPage + "");
        Log.d("BaseModel page", nowPage + "");

        if (nowPage < totalPage) {
            //设置下页面作为当前页
            baseModel.getPage().setPage(nextPage);
            CommunityRentHousePageAsyncTask pageAsyncTask = new CommunityRentHousePageAsyncTask();
            pageAsyncTask.execute(baseModel);

        }

        //刨除去第一页因为上来已经加载过了
        if (nowPage == totalPage && nowPage != 1) {
            CommunityRentHousePageAsyncTask pageAsyncTask = new CommunityRentHousePageAsyncTask();
            pageAsyncTask.execute(baseModel);

        }
    }

    public void setRoomByNetWork(final BaseModel baseModel,final int index){
        final TextView selectionView = (TextView) popupWindowView.findViewById(R.id.house_type_selection);
        ConditionJsonModel conditionJsonModel = getJsonResult().getData();
        final Map<String, String> resultMap = new HashMap<String, String>();
        List<String> params = new ArrayList<String>();
        final String[] paramStr;
        if (conditionJsonModel.getRoom() != null) {
            paramStr = new String[conditionJsonModel.getRoom().size()];
            for (Map.Entry<String, RoomJsonModel> roomJsonModel : conditionJsonModel.getRoom().entrySet()) {
                resultMap.put(roomJsonModel.getValue().getName(), roomJsonModel.getValue().getFid());
                params.add(roomJsonModel.getValue().getName());
            }
            dialog = new AlertDialog.Builder(RentHouseMapActivity.this).setItems(params.toArray(paramStr), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    popupWindow.dismiss();
                    String roomStr = resultMap.get(paramStr[which]);
                    selectionView.setText(paramStr[which]);
                    if (baseModel.getSearchModel() != null) {
                        baseModel.getSearchModel().setRoom(roomStr);
                    }
                    CommunityRentHouseAsyncTask task = new CommunityRentHouseAsyncTask(index);
                    task.execute(baseModel);
                }
            }).create();

            dialog.setCanceledOnTouchOutside(true);

            selectionView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.show();
                }
            });
        }
    }

    public class RentHouseMapAsyncTask extends AsyncTask<BaseModel, Void, JsonResult<List<HouseMapListJsonModel>>> {

        @Override
        protected JsonResult<List<HouseMapListJsonModel>> doInBackground(BaseModel... params) {
            return getListDataByNetWork(LIST_URL, params[0]);
        }

        @Override
        protected void onPostExecute(JsonResult<List<HouseMapListJsonModel>> jsonResult) {
            if (jsonResult != null && jsonResult.isStatus()) {
                List<HouseMapListJsonModel> mapListJsonModels = jsonResult.getData();
                Log.d("RentHouseMapAsyncTask == mapListJsonModels", mapListJsonModels.size() + "");

                pointList = new ArrayList<MapOverLayModel>();

                if (mapListJsonModels != null && mapListJsonModels.size() > 0) {
                    for (HouseMapListJsonModel obj : mapListJsonModels) {
                        pointList.add(new MapOverLayModel(obj.getCommunityId(), obj.getCommunityName(), new GeoPoint((int) (Double.parseDouble(obj.getMapy()) * 1E6), (int) (Double.parseDouble(obj.getMapx()) * 1E6)), null, obj.getHousenums() + "套"));
                    }
                }
                initOverlay();
            } else if (jsonResult == null) {

            }
        }
    }

    /**
     * 百度定位侦听回调
     */
    public class BaiduLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null) {
                return;
            }

            locData.latitude = location.getLatitude();
            locData.longitude = location.getLongitude();
            //如果不显示定位精度圈，将accuracy赋值为0即可
            locData.accuracy = location.getRadius();
            // 此处可以设置 locData的方向信息, 如果定位 SDK 未返回方向信息，用户可以自己实现罗盘功能添加方向信息。
            locData.direction = location.getDerect();
            //更新定位数据
            myLocationOverlay.setData(locData);
            //更新图层数据执行刷新后生效
            mMapView.refresh();
            //是手动触发请求或首次定位时，移动到定位点
            if (isRequest) {
                //移动地图到定位点
                Log.d(Global.LOG_TAG, "LocationOverlay receive location, animate to it");
                mMapController.animateTo(new GeoPoint((int) (locData.latitude * 1e6), (int) (locData.longitude * 1e6)));
                isRequest = false;
                //myLocationOverlay.setLocationMode(MyLocationOverlay.LocationMode.FOLLOWING);
            }

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
     * 楼房坐标浮动层
     */
    public class HourseCountOverlay extends BaiduMapUtil.AbstractOverlay {
        public HourseCountOverlay(Drawable defaultMarker, MapView mapView, Context context, PopupClickListener popListener) {
            super(defaultMarker, mapView, context, popListener);
        }

        @Override
        public boolean onTap(int index) {
            getBaseModel().getSearchModel().setCid(pointList.get(index).getHid());
            footerView = getLayoutInflater().inflate(R.layout.house_list_wait_load, null);
            footerView.findViewById(R.id.house_list_wait_load_relative).setVisibility(View.GONE);
            //获取自定义布局文件pop.xml的视图
            popupWindowView = getLayoutInflater().inflate(R.layout.house_map_pop_list, null, false);
            TextView nameTextView = (TextView) popupWindowView.findViewById(R.id.name);
            nameTextView.setText(pointList.get(index).getName());
            listView = (ListView) popupWindowView.findViewById(R.id.house_common_pop_list_list_view);
            listView.setOnScrollListener(RentHouseMapActivity.this);
            listView.setOnItemClickListener(listener);
            CommunityRentHouseAsyncTask communityRentHouseAsyncTask = new CommunityRentHouseAsyncTask(index);
            communityRentHouseAsyncTask.execute(getBaseModel());

            return true;
        }
    }

    public class CommunityRentHouseAsyncTask extends AsyncTask<BaseModel, Void, JsonResult<List<OldHouseJsonModel>>> {

        private int index;

        private BaseModel baseModel;

        public CommunityRentHouseAsyncTask(int index) {
            this.index = index;
        }

        @Override
        protected JsonResult<List<OldHouseJsonModel>> doInBackground(BaseModel... params) {
            baseModel = params[0];
            //默认上来必须都是第一页数据
            if (baseModel.getPage() != null) {
                baseModel.getPage().setPage(1);
            }
            return getCommunityRentHouseList(baseModel);
        }

        @Override
        protected void onPostExecute(JsonResult<List<OldHouseJsonModel>> jsonResult) {
            List<Map<Integer, Object>> listItems = new ArrayList<Map<Integer, Object>>();
            if (jsonResult != null && jsonResult.isStatus()) {
                Page page = new Page(Integer.valueOf(jsonResult.getTotal()), Global.PAGE_SIZE);
                baseModel.setPage(page);
                if (listView.getFooterViewsCount() == 0) {
                    listView.addFooterView(footerView);
                }
                setListData(jsonResult.getData(), listItems, null);
            } else if (jsonResult == null) {
                DefaultDataUtils.setDefaultHint(listItems, Global.ERROR_NET_WORK);
            }
            DefaultDataUtils.setDefaultHint(listItems, Global.NOT_FIND_DATA);
            itemAdapter = new ItemAdapter(RentHouseMapActivity.this, R.layout.adapter_house_item_two, listItems);
            listView.setAdapter(itemAdapter);

            // 创建PopupWindow实例
            popupWindow = new PopupWindow(popupWindowView, ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtil.getScreenHeightPixels(RentHouseMapActivity.this) * 2 / 3, true);
            //点击其他地方消失
            popupWindowView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                        popupWindow = null;
                    }
                    return false;
                }
            });
            popupWindow.showAtLocation(mMapView, Gravity.BOTTOM, 0, 0);
            currentHid = pointList.get(index).getHid();
            setRoomByNetWork(baseModel, index);
        }
    }

    //这块必须要加入这个监听器
    private class MyMKMapViewListener implements MKMapViewListener {
        @Override
        public void onMapLoadFinish() {
            Log.d(Global.LOG_TAG + "===========", String.valueOf(BaiduMapUtil.getPointXY(mMapView)[0]));
            double[] pointXY = BaiduMapUtil.getPointXY(mMapView);
            minX = String.valueOf(pointXY[0]);
            maxX = String.valueOf(pointXY[1]);
            minY = String.valueOf(pointXY[2]);
            maxY = String.valueOf(pointXY[3]);
            setListData();
        }

        @Override
        public void onMapMoveFinish() {
            /**
             * 在此处理地图移动完成回调
             * 缩放，平移等操作完成后，此回调被触发
             */
            double[] pointXY = BaiduMapUtil.getPointXY(mMapView);
            minX = String.valueOf(pointXY[0]);
            maxX = String.valueOf(pointXY[1]);
            minY = String.valueOf(pointXY[2]);
            maxY = String.valueOf(pointXY[3]);
            BaseModel baseModel = getBaseModel();
            if (baseModel == null) {
                baseModel = new BaseModel();
                setBaseModel(baseModel);
            }
            getListDataNetWork().startListTask(baseModel);
        }

        @Override
        public void onClickMapPoi(MapPoi mapPoiInfo) {
            /**
             * 在此处理底图poi点击事件
             * 显示底图poi名称并移动至该点
             * 设置过： mMapController.enableClick(true); 时，此回调才能被触发
             *
             */
            String title = "";
            if (mapPoiInfo != null) {
                title = mapPoiInfo.strText;
                Toast.makeText(RentHouseMapActivity.this, title, Toast.LENGTH_SHORT).show();
                mMapController.animateTo(mapPoiInfo.geoPt);
            }
        }

        @Override
        public void onGetCurrentMap(Bitmap b) {
            /**
             *  当调用过 mMapView.getCurrentMap()后，此回调会被触发
             *  可在此保存截图至存储设备
             */
        }

        @Override
        public void onMapAnimationFinish() {
            /**
             *  地图完成带动画的操作（如: animationTo()）后，此回调被触发
             */
        }
    }

    class HousePrivilegeTask extends AsyncTask<Integer, Integer, Void> {


        @Override
        protected Void doInBackground(Integer... params) {
            return null;
        }

        /**
         * 这里的String参数对应AsyncTask中的第三个参数（也就是接收doInBackground的返回值）
         * 在doInBackground方法执行结束之后在运行，并且运行在UI线程当中 可以对UI空间进行设置
         */
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            footerView.setVisibility(View.GONE);
            loadData();
            listView.setSelection(visibleLastIndex - visibleItemCount + 1); //设置选中项
        }
    }

    public class CommunityRentHousePageAsyncTask extends AsyncTask<BaseModel, Void, JsonResult<List<OldHouseJsonModel>>>{

        private BaseModel baseModel;

        @Override
        protected JsonResult<List<OldHouseJsonModel>> doInBackground(BaseModel... params) {
            baseModel = params[0];
            return getCommunityRentHouseList(baseModel);
        }

        @Override
        protected void onPostExecute(JsonResult<List<OldHouseJsonModel>> jsonResult) {
            if (jsonResult != null && jsonResult.isStatus()){
                setListData(jsonResult.getData(), null, itemAdapter);
                //刨除去最后一页重复加载
                if (baseModel.getPage().getPage() == baseModel.getPage().getTotalPage()) {
                    baseModel.getPage().setPage(baseModel.getPage().getTotalPage() + 1);
                    Log.d("new nowPage", baseModel.getPage().getPage() + "");
                }
            } else {

            }

        }
    }
}
