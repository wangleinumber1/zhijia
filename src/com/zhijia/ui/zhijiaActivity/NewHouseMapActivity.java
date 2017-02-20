package com.zhijia.ui.zhijiaActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.*;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.google.common.base.Optional;
import com.zhijia.Global;
import com.zhijia.Enum.ListType;
import com.zhijia.service.data.Medol.BaseModel;
import com.zhijia.service.data.Medol.JsonResult;
import com.zhijia.service.data.Medol.NewMapListJsonModel;
import com.zhijia.service.data.Medol.SearchModel;
import com.zhijia.service.network.HttpClientUtils;
import com.zhijia.ui.R;
import com.zhijia.ui.list.interfaces.IListDataNetWork;
import com.zhijia.util.BaiduMapUtil;
import com.zhijia.util.DownloadImageTask;
import com.zhijia.util.MapOverLayModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 新房地图页
 * 如果出现网格，上百度API上更新key密钥
 */
public class NewHouseMapActivity extends BaseHouseConditionActivity implements IListDataNetWork<NewMapListJsonModel> {

    private final String CONDITION_URL = Global.API_WEB_URL + "/xinfang/field";

    private final String MAP_LIST_URL = Global.API_WEB_URL + "/xinfang/search";

    //MapView 是地图主控件
    private MapView mMapView = null;
    //用MapController完成地图控制
    private MapController mMapController = null;
    private HourseOverlay myOverlay = null;
    //定位图层
    private MyLocationOverlay myLocationOverlay = null;
    //地图定位必须的侦听用于定位后回调，以及本地API引用。
    private LocationClient mLocationClient = null;
    private BDLocationListener myListener = new BaiduLocationListener();
    private LocationData locData = null;
    //是否手动触发请求定位
    private boolean isRequest = false;
    //所有点的坐标列表，有序
    private List<MapOverLayModel> pointList;

    private String minX, maxX, minY, maxY;

    private String currentHid = "";
    private  Global app;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = (Global) this.getApplication();
        if (app.bMapManager == null) {
            app.bMapManager = new BMapManager(getApplicationContext());
            /**
             * 如果BMapManager没有初始化则初始化BMapManager
             */
            //app.bMapManager.init(key);
            app.bMapManager.init(new BaiduMapUtil.MapGeneralListener());
        }
        app.bMapManager.start();

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
        mMapController.setZoom(13);
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

        //设置条件
        setCondition();

        //设置列表
        //setListData() ;

        //注册事件绑定
        findViewById(R.id.house_back).setOnClickListener(new HouseOnClickListener());
        findViewById(R.id.new_house_list_map).setBackgroundResource(R.drawable.new_house_button_map);
        findViewById(R.id.new_house_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newHouseIntent = new Intent(NewHouseMapActivity.this.getApplicationContext(), NewHouseActivity.class);
                startActivity(newHouseIntent);
                NewHouseMapActivity.this.finish();
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

    private void setListData() {
        BaseModel baseModel = new BaseModel();
        setBaseModel(baseModel);
        this.setListDataNetWork(this);
        startListTask(baseModel);
    }

    private void setCondition() {
        this.setConditionNetwork(this);
        this.setCommonHouseTitle(R.id.house_top_app_gps, R.string.new_house_title);

        this.bindEventArea(areaIds, relativeId, ListType.NEW);

        this.startConditionAsyncTask(CONDITION_URL);
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
                Intent newHouseDetails = new Intent(getApplicationContext(), NewHouseDetailsActivity.class);
                newHouseDetails.putExtra("hid", currentHid);
                startActivity(newHouseDetails);
            }
        };

        //创建自定义overlay
        myOverlay = new HourseOverlay(getResources().getDrawable(R.drawable.map_location_icons), mMapView, getApplicationContext(), popListener);
        mMapView.getOverlays().clear();
        //准备overlay 数据
        for (MapOverLayModel point : pointList) {
            myOverlay.addItem(new OverlayItem(point.getGeoPoint(), "", ""));
        }
        mMapView.getOverlays().add(myOverlay);
        //刷新地图
        mMapView.refresh();
    }

    @Override
    public JsonResult<List<NewMapListJsonModel>> getListDataByNetWork(String url, BaseModel baseModel) {
        HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
        SearchModel searchModel = baseModel.getSearchModel();
        if (searchModel == null) {
            searchModel = new SearchModel();
        }
        searchModel.setMinX(minX);
        searchModel.setMaxX(maxX);
        searchModel.setMinY(minY);
        searchModel.setMaxY(maxY);
        Map<String, String> map = searchModel.toMap();
        map.put("cityid", Global.NOW_CITY_ID);
        Optional<JsonResult<List<NewMapListJsonModel>>> optional = httpClientUtils.getUnsignedListByData(url, map, NewMapListJsonModel.class);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    @Override
    public void startListTask(BaseModel baseModel) {
        NewHouseMapAsyncTask newHouseMapAsyncTask = new NewHouseMapAsyncTask();
        newHouseMapAsyncTask.execute(baseModel);
    }

    @Override
    public void setNetWorkErrorValue(BaseModel baseModel) {

    }

    public class NewHouseMapAsyncTask extends AsyncTask<BaseModel, Void, JsonResult<List<NewMapListJsonModel>>> {

        @Override
        protected JsonResult<List<NewMapListJsonModel>> doInBackground(BaseModel... params) {
            return getListDataByNetWork(MAP_LIST_URL, params[0]);
        }

        @Override
        protected void onPostExecute(JsonResult<List<NewMapListJsonModel>> jsonResult) {
            if (jsonResult != null && jsonResult.isStatus()) {
                List<NewMapListJsonModel> mapListJsonModels = jsonResult.getData();
                Log.d("NewHouseMapAsyncTask == mapListJsonModels", mapListJsonModels.size() + "");

                pointList = new ArrayList<MapOverLayModel>();

                if (mapListJsonModels != null && mapListJsonModels.size() > 0) {
                    for (NewMapListJsonModel obj : mapListJsonModels) {
                        pointList.add(new MapOverLayModel(obj.getHid(), obj.getName(), new GeoPoint((int) (Double.parseDouble(obj.getLaticoor()) * 1E6), (int) (Double.parseDouble(obj.getLongcoor()) * 1E6)), obj.getTitlePic(), obj.getAveragePrice()));
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
    public class HourseOverlay extends BaiduMapUtil.AbstractOverlay {
        public HourseOverlay(Drawable defaultMarker, MapView mapView, Context context, PopupClickListener popListener) {
            super(defaultMarker, mapView, context, popListener);
        }

        @Override
        public boolean onTap(int index) {
            //弹出自定义View
            View popView = LayoutInflater.from(this.getContext()).inflate(R.layout.map_detail_overlay, null);
            final ImageView imageView = ((ImageView) popView.findViewById(R.id.map_house_image));
            Log.d("NewHouseMap->jsonResult->pic_url", pointList.get(index).getDrawable());
            new DownloadImageTask().doInBackground(pointList.get(index).getDrawable(), imageView, R.drawable.a);

            ((TextView) popView.findViewById(R.id.map_house_desc)).setText(Html.fromHtml(pointList.get(index).getName() + "<br/>" + pointList.get(index).getHtmlText()));

            this.getPopupOverlay().showPopup(popView, pointList.get(index).getGeoPoint(), 32);
            currentHid = pointList.get(index).getHid();

            return true;
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
                Toast.makeText(NewHouseMapActivity.this, title, Toast.LENGTH_SHORT).show();
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

}
