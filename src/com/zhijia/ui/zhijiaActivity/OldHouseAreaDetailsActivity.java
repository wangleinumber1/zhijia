package com.zhijia.ui.zhijiaActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Optional;
import com.zhijia.Global;
import com.zhijia.service.data.Medol.CommunityDetailInfoJsonModel;
import com.zhijia.service.data.Medol.CommunityDetailJsonModel;
import com.zhijia.service.data.Medol.JsonResult;
import com.zhijia.service.data.Medol.ListJsonModel;
import com.zhijia.service.network.HttpClientUtils;
import com.zhijia.ui.R;
import com.zhijia.ui.SlideJsonModel;
import com.zhijia.util.DefaultDataUtils;
import com.zhijia.util.DownloadImageTask;
import com.zhijia.util.HouseDBUtil;
import com.zhijia.util.ScreenUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 小区详情页
 */
public class OldHouseAreaDetailsActivity extends BaseDetailsActivity {

    private final String COMMUNITY_DETAIL_URL = Global.API_WEB_URL + "/community/detail";

    private final String ATTENTION_COMMUNITY_URL= Global.API_WEB_URL+"/community/attention" ;

    private String cid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.old_house_area_details);
        findViewById(R.id.details_back).setOnClickListener(new DetailsOnClickListener());
        cid = getIntent().getStringExtra("cid");
        HouseDBUtil.addBrowseHistory(cid, HouseDBUtil.HouseType.COMMUNITY);
        CommunityDetailAsyncTask detailAsyncTask = new CommunityDetailAsyncTask();
        detailAsyncTask.execute();
        findViewById(R.id.old_house_area_linear_attention).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if(!Global.USER_AUTH_STR.equals("")) {
                     AttentionCommunityAsyncTask attentionCommunityAsyncTask = new AttentionCommunityAsyncTask();
                     attentionCommunityAsyncTask.execute();
                 }else{
                     Intent loginIntent = new Intent(OldHouseAreaDetailsActivity.this, LoginActivity.class);
                     startActivityForResult(loginIntent, 101);
                 }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Log.d("onActivityResult", cid);
            AttentionCommunityAsyncTask attentionCommunityAsyncTask = new AttentionCommunityAsyncTask() ;
            attentionCommunityAsyncTask.execute() ;
        } else {
            finish();
        }
    }

    public void setTitle(String areaName) {
        Map<Integer, Object> map = new HashMap<Integer, Object>();
        map.put(R.id.details_title, areaName);
        this.setHeader(map);
    }


    @SuppressWarnings("unused")
	public void setLoopPic(SlideJsonModel jsonModel) {
        List<ListJsonModel> slideList = jsonModel.getList();
        if (slideList.size()==0) {
        	imageViews = new ArrayList<ImageView>();
        	viewPager = (ViewPager) findViewById(R.id.old_house_area_details_house_image);
        	LinearLayout linearLayout = (LinearLayout) findViewById(R.id.old_house_details_house_dotsId);
            ((TextView) findViewById(R.id.old_house_area_details_house_image_count)).setText("共" + jsonModel.getTotal() + "图");
        	ImageView imageView = new ImageView(OldHouseAreaDetailsActivity.this);
            imageView.setImageResource(R.drawable.house_bg);
            imageViews.add(imageView);
		}
        if (slideList.size()!= 0) {
            imageViews = new ArrayList<ImageView>();
            dotsList = new ArrayList<View>();
            viewPager = (ViewPager) findViewById(R.id.old_house_area_details_house_image);
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.old_house_details_house_dotsId);
            ((TextView) findViewById(R.id.old_house_area_details_house_image_count)).setText("共" + jsonModel.getTotal() + "图");
            for (ListJsonModel tempJson : jsonModel.getList()) {
                final ImageView imageView = new ImageView(OldHouseAreaDetailsActivity.this);
                Log.d("OldHouseAreaDetailsActivity->jsonResult->size", tempJson.getPicUrl());
                new DownloadImageTask().doInBackground(tempJson.getPicUrl(), imageView, R.drawable.a);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent communityIntent = new Intent(OldHouseAreaDetailsActivity.this,CommunityAlbumDetailsActivity.class) ;
                        communityIntent.putExtra("cid",cid) ;
                        communityIntent.putExtra("housetype","3") ;
                        startActivity(communityIntent);
                    }
                });
                //imageView.setOnClickListener(new ImageViewOnClickListener(tempSlideJson));
                imageViews.add(imageView);
            }

            for (int i = 0; i < imageViews.size(); i++) {
                View dotView = new View(OldHouseAreaDetailsActivity.this);
                if (i == 0) {
                    dotView.setBackgroundResource(R.drawable.index_top_dot_selected);
                } else {
                    dotView.setBackgroundResource(R.drawable.index_top_dot_unselected);
                }
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ScreenUtil.dps2pixels(5, OldHouseAreaDetailsActivity.this), ScreenUtil.dps2pixels(5, OldHouseAreaDetailsActivity.this));
                params.setMargins(ScreenUtil.dps2pixels(1.5, OldHouseAreaDetailsActivity.this)
                        , ScreenUtil.dps2pixels(15, OldHouseAreaDetailsActivity.this)
                        , ScreenUtil.dps2pixels(1.5, OldHouseAreaDetailsActivity.this)
                        , ScreenUtil.dps2pixels(15, OldHouseAreaDetailsActivity.this));
                dotView.setLayoutParams(params);
                dotsList.add(dotView);
                linearLayout.addView(dotView);
            }
            // 设置一个监听器，当ViewPager中的页面改变时调用
            viewPager.setOnPageChangeListener(new SplashImageListener());
            viewPager.setCurrentItem(currentItem);
        } else {
            ImageView imageView = new ImageView(OldHouseAreaDetailsActivity.this);
            imageView.setImageResource(getResources().getIdentifier("house_bg", "drawable", Global.PACKAGE_NAME));
            imageViews.add(imageView);
        }
        // 设置填充ViewPager页面的适配器
        viewPager.setAdapter(new SplashImageAdapter());
    }

    public void setDetail(CommunityDetailJsonModel jsonModel) {
        //小区简介
        TextView nameTextView = (TextView) findViewById(R.id.old_house_details_house_intro_relative_name_value);
        TextView priceTextView = (TextView) findViewById(R.id.old_house_details_house_intro_relative_price_value);
        TextView ratioTextView = (TextView) findViewById(R.id.old_house_details_house_intro_relative_compare_value);
        TextView sellNumTextView = (TextView) findViewById(R.id.old_house_details_house_intro_sell_old_house);
        TextView rentNumTextView = (TextView) findViewById(R.id.old_house_details_house_intro_rent_old_house);
        String communityName = jsonModel.getCommunityName();
        String price = jsonModel.getCommunityDetail().getSellPrice();
        String ratio = jsonModel.getCommunityDetail().getSellRatio();
        String sellNum = jsonModel.getCommunityDetail().getSellNum();
        String rentNum = jsonModel.getCommunityDetail().getRentNum();
        DefaultDataUtils.setValue(communityName, nameTextView);
        DefaultDataUtils.setValue(price, priceTextView);
        DefaultDataUtils.setValue(ratio, ratioTextView);

        if (sellNum != null && !sellNum.isEmpty()) {
            sellNumTextView.setText("正在出售房源" + sellNum );
        } else {
            sellNumTextView.setText("正在出售房源");
        }
        sellNumTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OldHouseAreaDetailsActivity.this, OldCommunityListActivity.class);
                intent.putExtra("cid", cid);
                startActivity(intent);
            }
        });
        if (rentNum != null && !rentNum.isEmpty()) {
            rentNumTextView.setText("正在出租房源" + rentNum );
        } else {
            rentNumTextView.setText("正在出租房源");
        }
        rentNumTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(OldHouseAreaDetailsActivity.this, RentCommunityListActivity.class);
                intent4.putExtra("cid", cid);
                startActivity(intent4);
            }
        });

        CommunityDetailInfoJsonModel.InfoJsonModel infoModel = jsonModel.getCommunityDetail().getInfo();
        CommunityDetailInfoJsonModel.TrendJsonModel trendJsonModel = jsonModel.getCommunityDetail().getTrend();
        CommunityDetailInfoJsonModel.MapJsonModel mapJsonModel = jsonModel.getCommunityDetail().getMap();
        if (infoModel != null) {
            TextView areaNameTextView = (TextView) findViewById(R.id.old_house_area_details_area_value);
            TextView addressTextView = (TextView) findViewById(R.id.old_house_area_details_address_value);
            TextView projectTypeTextView = (TextView) findViewById(R.id.old_house_area_details_project_type_value);
            TextView placeTextView = (TextView) findViewById(R.id.old_house_area_details_car_place_value);
            TextView serviceCostTextView = (TextView) findViewById(R.id.old_house_area_details_service_cost_value);
            TextView serviceCompanyTextView = (TextView) findViewById(R.id.old_house_area_details_service_company_value);
            TextView proportionTextView = (TextView) findViewById(R.id.old_house_area_details_proportion_value);
            TextView occupiedTextView = (TextView) findViewById(R.id.old_house_area_details_occupied_proportion_value);
            TextView afforestTextView = (TextView) findViewById(R.id.old_house_area_details_afforest_value);
            TextView volumeTextView = (TextView) findViewById(R.id.old_house_area_details_volume_value);
            TextView timeTextView = (TextView) findViewById(R.id.old_house_area_details_end_time_value);
            TextView summaryTextView = (TextView) findViewById(R.id.old_house_area_details_summary_value);
            TextView implementTextView = (TextView) findViewById(R.id.old_house_area_details_implement_value);
            String areaName = infoModel.getAreaName();
            String address = infoModel.getAddress();
            String projectType = infoModel.getProjectType();
            String place = infoModel.getCarport();
            String serviceCost = infoModel.getPropertyPrice();
            String serviceCompany = infoModel.getPropertyCompany();
            String proportion = infoModel.getBuildArea();
            String occupied = infoModel.getLandArea();
            String afforest = infoModel.getViresCence();
            String volume = infoModel.getCubAge();
            String time = infoModel.getSendDate();
            String summary = infoModel.getInfo();
            String implement = infoModel.getAssort();
            DefaultDataUtils.setValue(areaName, areaNameTextView);
            DefaultDataUtils.setValue(address, addressTextView);
            DefaultDataUtils.setValue(projectType, projectTypeTextView);
            DefaultDataUtils.setValue(place, placeTextView);
            DefaultDataUtils.setValue(serviceCost, serviceCostTextView);
            DefaultDataUtils.setValue(serviceCompany, serviceCompanyTextView);
            DefaultDataUtils.setValue(proportion, proportionTextView);
            DefaultDataUtils.setValue(occupied, occupiedTextView);
            DefaultDataUtils.setValue(afforest, afforestTextView);
            DefaultDataUtils.setValue(volume, volumeTextView);
            DefaultDataUtils.setValue(time, timeTextView);
            DefaultDataUtils.setValue(summary, summaryTextView);
            DefaultDataUtils.setValue(implement, implementTextView);
        } else {
            findViewById(R.id.old_house_area_details_details_linear_layout).setVisibility(View.GONE);
        }

        if (trendJsonModel != null) {

            final String sell = trendJsonModel.getSell();
            final ImageView imageView = (ImageView) findViewById(R.id.old_house_area_details_house_trend_image);
            final TextView trendRentTextView = (TextView) findViewById(R.id.old_house_area_details_house_trend_rent_button);
            final String rent = trendJsonModel.getRent();
            setImage(imageView, sell);
            final TextView trendOldTextView = (TextView) findViewById(R.id.old_house_area_details_house_trend_old_button);
            trendOldTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    trendOldTextView.setTextColor(Color.parseColor("#000000"));//#ff0000
                    trendOldTextView.setBackgroundColor(Color.parseColor("#FFC2C2C2"));//#FFFFFFFF
                    trendRentTextView.setTextColor(Color.parseColor("#ff0000"));//#000000
                    trendRentTextView.setBackgroundColor(Color.parseColor("#FFFFFFFF"));//#FFC2C2C2
                    setImage(imageView, sell);
                }
            });

            trendRentTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    trendRentTextView.setTextColor(Color.parseColor("#000000"));//#ff0000
                    trendRentTextView.setBackgroundColor(Color.parseColor("#FFC2C2C2"));//#FFFFFFFF
                    trendOldTextView.setTextColor(Color.parseColor("#ff0000"));//#000000
                    trendOldTextView.setBackgroundColor(Color.parseColor("#FFFFFFFF"));//#FFC2C2C2
                    setImage(imageView, rent);
                }
            });

        } else {
            findViewById(R.id.old_house_area_details_house_trend_relative_layout).setVisibility(View.GONE);
        }

        if (mapJsonModel != null) {
           final ImageView  trafficImageView =  (ImageView)findViewById(R.id.old_house_area_details_house_traffic_image) ;
           TextView contentTextView = (TextView)findViewById(R.id.old_house_area_details_house_traffic_address_content) ;
           final String mapURL = mapJsonModel.getMap();
           setImage(trafficImageView,mapURL);
           DefaultDataUtils.setValue(mapJsonModel.getAddress(),contentTextView);
        } else {
            findViewById(R.id.old_house_area_details_house_traffic_relative_layout).setVisibility(View.GONE);
        }

    }

    public JsonResult<CommunityDetailJsonModel> getCommunityDetail() {
        HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
        Map<String, String> map = new HashMap<String, String>();
        map.put("cityid", Global.NOW_CITY_ID);
        map.put("cid", cid);
        Optional<JsonResult<CommunityDetailJsonModel>> optional = httpClientUtils.getUnsignedByData(COMMUNITY_DETAIL_URL, map, CommunityDetailJsonModel.class);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    public Map<String,String> sendPostRequest(){
        Map<String, String> resultMap = new HashMap<String, String>();
        HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
        Map<String, String> map = new HashMap<String, String>();
        map.put("authstr", Global.USER_AUTH_STR);
        map.put("cityid", Global.NOW_CITY_ID);
        map.put("id", cid);
        Optional<Map<String, String>> optional = httpClientUtils.postSignedMap(ATTENTION_COMMUNITY_URL, map, String.class);
        if (optional.isPresent()) {
            resultMap.put("status", optional.get().get("status"));
            resultMap.put("message", optional.get().get("message"));
            return resultMap;
        }
        return null;
    }
    public void setImage(final ImageView imageView, String url) {
        new DownloadImageTask().doInBackground(url, imageView, null);
    }

    public class CommunityDetailAsyncTask extends AsyncTask<String, Void, JsonResult<CommunityDetailJsonModel>> {

        @Override
        protected JsonResult<CommunityDetailJsonModel> doInBackground(String... params) {
            return getCommunityDetail();
        }

        @Override
        protected void onPostExecute(JsonResult<CommunityDetailJsonModel> jsonResult) {
            if (jsonResult != null && jsonResult.isStatus()) {
                CommunityDetailJsonModel jsonModel = jsonResult.getData();
                setTitle(jsonModel.getCommunityName());
                setLoopPic(jsonModel.getSlide());
                setDetail(jsonModel);
            } else if (jsonResult == null) {

            }
        }
    }

    public class AttentionCommunityAsyncTask extends AsyncTask<String, Void, Map<String, String>> {

        @Override
        protected Map<String, String> doInBackground(String... params) {
            return sendPostRequest();
        }

        @Override
        protected void onPostExecute(Map<String, String> stringStringMap) {
            if (stringStringMap != null) {
                Toast toast = Toast.makeText(OldHouseAreaDetailsActivity.this, stringStringMap.get("message"), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

            } else {
                Toast toast = Toast.makeText(OldHouseAreaDetailsActivity.this, "网络数据获取失败", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    }
}
