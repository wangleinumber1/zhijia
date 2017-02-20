package com.zhijia.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Toast;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.*;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.zhijia.Global;
import java.util.Calendar;

/**
 * 百度地图工具类
 */
public class BaiduMapUtil {

    /**
     * 地球半径（单位：公里）
     */
    public final static double EARTH_RADIUS_KM = 6378.137;
    private static double DEF_PI = 3.14159265359; // PI
    private static double DEF_2PI = 6.28318530712; // 2*PI
    private static double DEF_PI180 = 0.01745329252; // PI/180.0
    private static double DEF_R = 6370693.5; // radius of earth

    public static double getShortDistance(double lon1, double lat1, double lon2, double lat2) {
        double ew1, ns1, ew2, ns2;
        double dx, dy, dew;
        double distance;
        // 角度转换为弧度
        ew1 = lon1 * DEF_PI180;
        ns1 = lat1 * DEF_PI180;
        ew2 = lon2 * DEF_PI180;
        ns2 = lat2 * DEF_PI180;
        // 经度差
        dew = ew1 - ew2;
        // 若跨东经和西经180 度，进行调整
        if (dew > DEF_PI)
            dew = DEF_2PI - dew;
        else if (dew < -DEF_PI)
            dew = DEF_2PI + dew;
        dx = DEF_R * Math.cos(ns1) * dew; // 东西方向长度(在纬度圈上的投影长度)
        dy = DEF_R * (ns1 - ns2); // 南北方向长度(在经度圈上的投影长度)
        // 勾股定理求斜边长
        distance = Math.sqrt(dx * dx + dy * dy);
        return distance;
    }

    public static double getLongDistance(double lon1, double lat1, double lon2, double lat2) {
        double ew1, ns1, ew2, ns2;
        double distance;
        // 角度转换为弧度
        ew1 = lon1 * DEF_PI180;
        ns1 = lat1 * DEF_PI180;
        ew2 = lon2 * DEF_PI180;
        ns2 = lat2 * DEF_PI180;
        // 求大圆劣弧与球心所夹的角(弧度)
        distance = Math.sin(ns1) * Math.sin(ns2) + Math.cos(ns1) * Math.cos(ns2) * Math.cos(ew1 - ew2);
        // 调整到[-1..1]范围内，避免溢出
        if (distance > 1.0)
            distance = 1.0;
        else if (distance < -1.0)
            distance = -1.0;
        // 求大圆劣弧长度
        distance = DEF_R * Math.acos(distance);
        return distance;
    }

    /**
     * 根据经纬度计算地球上任意两点间的距离
     *
     * @param lng1 起点经度
     * @param lat1 起点纬度
     * @param lng2 终点经度
     * @param lat2 终点纬度
     * @return 两点距离（单位：千米）
     */
    public static double getDistance(double lng1, double lat1, double lng2, double lat2) {
        double radLat1 = Math.toRadians(lat1);
        double radLat2 = Math.toRadians(lat2);
        double radLng1 = Math.toRadians(lng1);
        double radLng2 = Math.toRadians(lng2);
        double deltaLat = radLat1 - radLat2;
        double deltaLng = radLng1 - radLng2;
        double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(deltaLat / 2), 2)+ Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(deltaLng / 2), 2)));
        distance = distance * EARTH_RADIUS_KM;
        distance = Math.round(distance * 10000) / 10000;
        return distance;
    }

    public static void main(String args[]) {
        double mLat1 = 39.90923; // point1纬度
        double mLon1 = 116.357428; // point1经度
        double mLat2 = 39.90923;// point2纬度
        double mLon2 = 116.397428;// point2经度

        double distance = getShortDistance(mLon1, mLat1, mLon2, mLat2);
        System.out.println(distance + "米");

        distance = getLongDistance(mLon1, mLat1, mLon2, mLat2);
        System.out.println(distance + "米");

        distance = getDistance(mLon1, mLat1, mLon2, mLat2);
        System.out.println(distance + "千米");
    }

    /**
     * 将BDLocation转换成字符串
     *
     * @param location
     * @return
     */
    public static String bdLocationToString(BDLocation location) {
        if (location == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer(256);
        sb.append("\n百度地图API定位信息\n");
        sb.append("\n 当前时间：").append(Global.DEFAULT_DF.format(Calendar.getInstance().getTime()));
        sb.append("\n 定位时间：");
        sb.append(location.getTime());
        sb.append("\n 状态码：");
        sb.append(location.getLocType());
        sb.append("\n 纬度：");
        sb.append(location.getLatitude());
        sb.append("\n 经度：");
        sb.append(location.getLongitude());
        sb.append("\n 误差半径：");
        sb.append(location.getRadius());
        sb.append("\n 所在省份：");
        sb.append(location.getProvince());
        sb.append("\n 所在城市：");
        sb.append(location.getCity());
        sb.append("\n 所在区域：");
        sb.append(location.getDistrict());
        if (location.getLocType() == BDLocation.TypeGpsLocation) {
            sb.append("\n 速度：");
            sb.append(location.getSpeed());
            sb.append("\n 可用卫星数：");
            sb.append(location.getSatelliteNumber());
            sb.append("\n 方位：");
            sb.append(location.getDirection());
        } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
            sb.append("\n 所在地址：");
            sb.append(location.getAddrStr());
            //运营商信息
            sb.append("\n 运营商信息：");
            sb.append(location.getOperators());
        }

        return sb.toString();
    }

    /**
     * BDLocation的POI类型坐标转换成字符串
     *
     * @param poiLocation
     * @return
     */
    public static String poiLocationToString(BDLocation poiLocation) {
        if (poiLocation == null) {
            return "";
        }

        StringBuffer sb = new StringBuffer(256);
        sb.append("Poi time : ");
        sb.append(poiLocation.getTime());
        sb.append("\nerror code : ");
        sb.append(poiLocation.getLocType());
        sb.append("\nlatitude : ");
        sb.append(poiLocation.getLatitude());
        sb.append("\nlontitude : ");
        sb.append(poiLocation.getLongitude());
        sb.append("\nradius : ");
        sb.append(poiLocation.getRadius());
        if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
            sb.append("\naddr : ");
            sb.append(poiLocation.getAddrStr());
        }
        if (poiLocation.hasPoi()) {
            sb.append("\nPoi:");
            sb.append(poiLocation.getPoi());
        } else {
            sb.append("noPoi information");
        }

        return sb.toString();
    }

    /**
     * 获得屏幕上的坐标点
     *
     * @param mapview
     * @return [0]左上纬度[1]右下纬度[2]左上经度[3]右下经度
     */
    public static double[] getPointXY(MapView mapview) {
        double[] returnArray = new double[4];
        //地图中心坐标
        GeoPoint centerPoint = mapview.getMapCenter();
        int tbSpan = mapview.getLatitudeSpan();// 当前纬线的跨度（从地图的上边缘到下边缘）
        int lrSpan = mapview.getLongitudeSpan();// 当前经度的跨度(从地图的左边缘到地图的右边缘)
        GeoPoint ltPoint = new GeoPoint(centerPoint.getLatitudeE6() - tbSpan / 2, centerPoint.getLongitudeE6() - lrSpan / 2);// 左上角坐标
        GeoPoint rbPoint = new GeoPoint(centerPoint.getLatitudeE6() + tbSpan / 2, centerPoint.getLongitudeE6() + lrSpan / 2);// 右下角坐标

        returnArray[0] = ltPoint.getLatitudeE6() / 1000000.00d;
        returnArray[1] = rbPoint.getLatitudeE6() / 1000000.00d;
        returnArray[2] = ltPoint.getLongitudeE6() / 1000000.00d;
        returnArray[3] = rbPoint.getLongitudeE6() / 1000000.00d;

        return returnArray;
    }

    // 常用事件监听，用来处理通常的网络错误，授权验证错误等
    public static class MapGeneralListener implements MKGeneralListener {
        @Override
        public void onGetNetworkState(int iError) {
            if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
                Toast.makeText(Global.getInstance().getApplicationContext(), "您的网络出错啦！", Toast.LENGTH_LONG).show();
            } else if (iError == MKEvent.ERROR_NETWORK_DATA) {
                //Toast.makeText(Global.getInstance().getApplicationContext(), "输入正确的检索条件！", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onGetPermissionState(int iError) {
            //非零值表示key验证未通过
            if (iError != 0) {
                //授权Key错误：
                //Toast.makeText(Global.getInstance().getApplicationContext(), "请输入正确的授权Key,并检查您的网络连接是否正常！error: " + iError, Toast.LENGTH_LONG).show();
            } else {
               // Toast.makeText(Global.getInstance().getApplicationContext(), "key认证成功", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * 自定义浮动层
     */
    public static abstract class AbstractOverlay extends ItemizedOverlay {

        private Context context;

        //浮动层
        private PopupOverlay popupOverlay;

        public AbstractOverlay(Drawable defaultMarker, MapView mapView, Context context, PopupClickListener popListener) {
            super(defaultMarker, mapView);

            this.context = context;
            popupOverlay = new PopupOverlay(mapView, popListener);
        }

        @Override
        public boolean onTap(GeoPoint pt, MapView mMapView) {
            if (popupOverlay != null) {
                popupOverlay.hidePop();
            }
            return false;
        }

        public PopupOverlay getPopupOverlay() {
            return popupOverlay;
        }

        public void setPopupOverlay(PopupOverlay popupOverlay) {
            this.popupOverlay = popupOverlay;
        }

        public Context getContext() {
            return context;
        }

        public void setContext(Context context) {
            this.context = context;
        }
    }
}
