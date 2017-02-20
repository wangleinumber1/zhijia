package com.zhijia.ui.zhijiaActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

import com.baidu.platform.comapi.map.a.l;
import com.google.common.base.Optional;
import com.zhijia.Global;
import com.zhijia.service.data.Medol.CommonPicDataJsonModel;
import com.zhijia.service.data.Medol.CommunityJsonModel;
import com.zhijia.service.data.Medol.DescriptionJsonModel;
import com.zhijia.service.data.Medol.HouseInfoJsonModel;
import com.zhijia.service.data.Medol.HouseInstructionJsonModel;
import com.zhijia.service.data.Medol.JsonResult;
import com.zhijia.service.data.Medol.OldHouseDetailJsonModel;

import com.zhijia.service.network.HttpClientUtils;
import com.zhijia.ui.R;
import com.zhijia.ui.list.interfaces.IListDetailDataNetWork;
import com.zhijia.ui.list.oldhouse.OldHouseDetailsGridItem;
import com.zhijia.ui.view.ZjGridView;
import com.zhijia.util.DownloadImageTask;
import com.zhijia.util.HouseDBUtil;
import com.zhijia.util.ScreenUtil;

import static com.zhijia.Global.PACKAGE_NAME;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 二手房的详情页面
 */
public class OldHouseDetailsActivity extends BaseDetailsActivity implements IListDetailDataNetWork<OldHouseDetailJsonModel> {

    private final int header_title = R.id.details_title;

    private final String[] GRID_ADAPTER_FROM = {"old_house_details_description_grid_image", "old_house_details_description_grid_text"};

    private final String[] LIST_ADAPTER_FROM = {"old_house_details_list_text"};

    private final int[] LIST_ADAPTER_TO = {R.id.house_details_list_text};

    private final int[] GRID_ADAPTER_TO = {R.id.house_details_description_grid_image, R.id.house_details_description_grid_text};

    private final String DETAIL_URL = Global.API_WEB_URL + "/house/detail";

    private final String COLLECTION_URL = Global.API_WEB_URL + "/house/collection";

    private final String ATTENTION_COMMUNITY_URL = Global.API_WEB_URL + "/community/attention";

    private String oldHid = null;

    private String housetype = "1";
    //收藏
    private String identity = "1";
    private String cid = null;
    private LinearLayout linearLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.old_house_details);
        oldHid = (String) getIntent().getSerializableExtra("hid");
        housetype = getIntent().getStringExtra("housetype");
        identity = getIntent().getStringExtra("identity");
        if (housetype.equals("1")) {
            HouseDBUtil.addBrowseHistory(oldHid, HouseDBUtil.HouseType.OLD_HOUSE);
        } else {
            HouseDBUtil.addBrowseHistory(oldHid, HouseDBUtil.HouseType.RENT_HOUSE);
        }
        startListTask(oldHid);
    }


    public List<Map<String, Object>> getGridListData(List<String> list) {

        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

        for (OldHouseDetailsGridItem temp : getOldHouseDetailsGridItem(list)) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("old_house_details_description_grid_image", temp.getImageId());
            map.put("old_house_details_description_grid_text", temp.getGridContent());
            resultList.add(map);
        }

        return resultList;
    }


    public List<Map<String, Object>> getListData(String info) {

        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("old_house_details_list_text", Html.fromHtml(info));
        resultList.add(map);
        return resultList;
    }

    /**
     * 设置网格的数据
     *
     * @param list
     * @return
     */
    private List<OldHouseDetailsGridItem> getOldHouseDetailsGridItem(List<String> list) {
        List<OldHouseDetailsGridItem> resultList = new ArrayList<OldHouseDetailsGridItem>();
        for (String tempStr : list) {
            OldHouseDetailsGridItem temp = new OldHouseDetailsGridItem();
            temp.setImageId(R.drawable.yes);
            temp.setGridContent(tempStr);
            resultList.add(temp);
        }
        return resultList;
    }

    @Override
    public JsonResult<OldHouseDetailJsonModel> getDetailDataByNetWork(String id) {

        HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
        Map<String, String> map = new HashMap<String, String>();
        map.put("cityid", Global.NOW_CITY_ID);
        map.put("housetype", housetype);
        map.put("hid", id);
        System.out.println(DETAIL_URL);
        Optional<JsonResult<OldHouseDetailJsonModel>> optional = httpClientUtils.getUnsignedByData(DETAIL_URL, map, OldHouseDetailJsonModel.class);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    @Override
    public void startListTask(String hid) {
        OldHouseDetailsAsyncTask task = new OldHouseDetailsAsyncTask();
        task.execute(hid);
    }
//
    public void setDetailData(OldHouseDetailJsonModel jsonModel) {
        if(jsonModel.getUsertype() != null && !jsonModel.getUsertype().isEmpty()){
        	   String mobile1 = jsonModel.getAgencyMobile() ;
        	   String nameType=jsonModel.getUsertype();
        	   String name=jsonModel.getAgency_name();
        	   System.out.println("name:"+name);
        	   System.out.println("nameType:"+nameType);
        	   if (nameType.equals("1")) {
        		   ((TextView)findViewById(R.id.old_house_details_telephone)).setText("房主："+jsonModel.getAgency_name()+"\n"+mobile1);	
			}else {
				 ((TextView)findViewById(R.id.old_house_details_telephone)).setText("经纪人:"+jsonModel.getAgency_name()+"\n"+mobile1);
			}
        }

        if(jsonModel.getAgencyMobile() != null && !jsonModel.getAgencyMobile().isEmpty()){
            final String mobile = jsonModel.getAgencyMobile() ;
            findViewById(R.id.old_house_details_telephone_counseling).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(OldHouseDetailsActivity.this);
                    builder.setMessage(getResources().getString(R.string.confirm_dial) + "\n" + mobile);
                    builder.setTitle(getResources().getString(R.string.prompt));
                    builder.setPositiveButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Uri uri = Uri.parse("tel:" + mobile);   //拨打电话号码的URI格式
                            Intent intent = new Intent();   //实例化Intent
                            intent.setAction(Intent.ACTION_CALL);   //指定Action
                            intent.setData(uri);   //设置数据
                            startActivity(intent);
                        }
                    });
                    builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    builder.create().show();
                }
            });
        }


        HouseInstructionJsonModel instruction = jsonModel.getHouseInstruction();
        //房源简介-标题
        TextView introduce_content = (TextView) findViewById(R.id.old_house_details_house_introduce_content);
        introduce_content.setText(instruction.getTitle());
        //更新时间
        TextView detailsTime = (TextView) findViewById(R.id.old_house_details_time);
        String updateTime = instruction.getUpdatetime();
        if (updateTime != null && !updateTime.isEmpty()) {
            detailsTime.setText(updateTime);
        } else {
            detailsTime.setText("暂无");
        }
        TextView tagTextView = (TextView)findViewById(R.id.old_house_details_impression) ;
        TextView tagTextViewOne = (TextView)findViewById(R.id.old_house_details_impression_one) ;
        List<String> houseTags = instruction.getTagtext() ;
        if(houseTags != null && houseTags.size() > 0){
            if(houseTags.size() >=2){
                tagTextView.setText(houseTags.get(0));
                tagTextViewOne.setText(houseTags.get(1));
            }
            if(houseTags.size() < 2){
                tagTextView.setText(houseTags.get(0));
                tagTextViewOne.setVisibility(View.GONE);
            }
        }else{
            tagTextView.setVisibility(View.GONE);
            tagTextViewOne.setVisibility(View.GONE);
        }

        //售价
        TextView priceMoneyValue = (TextView) findViewById(R.id.old_house_details_introduce_price_money_value);
        String price = instruction.getPrice();
        if (price != null && !price.isEmpty()) {
            priceMoneyValue.setText(price);
        } else {
            priceMoneyValue.setText("暂无");
        }
        //面积
        TextView areaValue = (TextView) findViewById(R.id.old_house_details_introduce_price_area_value);
        String area = instruction.getArea();
        if (area != null && !area.isEmpty()) {
            areaValue.setText(area);
        } else {
            areaValue.setText("暂无");
        }
        //朝向
        TextView orientationValue = (TextView) findViewById(R.id.old_house_details_introduce_price_orientation_value);
        String aspect = instruction.getAspect();
        if (aspect != null && !aspect.isEmpty()) {
            orientationValue.setText(aspect);
        } else {
            orientationValue.setText("暂无");
        }
        //类型
        TextView typeValue = (TextView) findViewById(R.id.old_house_details_introduce_price_house_type_value);
        String property = instruction.getProperty();
        if (property != null && !property.isEmpty()) {
            typeValue.setText(property);
        } else {
            typeValue.setText("暂无");
        }
        //产权
        TextView equityValue = (TextView) findViewById(R.id.old_house_details_introduce_price_equity_value);
        String propertyright = instruction.getPropertyright();
        if (propertyright != null && !propertyright.isEmpty()) {
            equityValue.setText(propertyright);
        } else {
            equityValue.setText("暂无");
        }

        //户型
        TextView houseTypeValue = (TextView) findViewById(R.id.old_house_details_introduce_price_type_value);
        String unit = instruction.getUnit();
        if (unit != null && !unit.isEmpty()) {
            houseTypeValue.setText(unit);
        } else {
            houseTypeValue.setText("暂无");
        }

        //楼层
        TextView floorsValue = (TextView) findViewById(R.id.old_house_details_introduce_price_floors_value);
        String floor = instruction.getFloor();
        if (floor != null && !floor.isEmpty()) {
            floorsValue.setText(floor);
        } else {
            floorsValue.setText("暂无");
        }

        //装修
        TextView decorateValue = (TextView) findViewById(R.id.old_house_details_introduce_price_decorate_value);
        String decorate = instruction.getDecorate();
        if (decorate != null && !decorate.isEmpty()) {
            decorateValue.setText(decorate);
        } else {
            decorateValue.setText("暂无");
        }

        //房龄
        TextView ageValue = (TextView) findViewById(R.id.old_house_details_introduce_price_house_age_value);
        String houseAge = instruction.getHouseage();
        if (houseAge != null && !houseAge.isEmpty()) {
            ageValue.setText(houseAge);
        } else {
            ageValue.setText("暂无");
        }

        //房源描述
        HouseInfoJsonModel houseInfo = jsonModel.getHouseiInfo();
        if (houseInfo != null) {
            DescriptionJsonModel descriptionJsonModel = houseInfo.getDescription();
            CommunityJsonModel communityJsonModel = houseInfo.getCommunity();
            String trend = houseInfo.getTrend();
            String map = houseInfo.getMap();
            //描述布局
            View old_house_details_description_linear = findViewById(R.id.old_house_details_description_linear);
            //小区简介
            View old_house_details_house_intro_linear = findViewById(R.id.old_house_details_house_intro_linear);
            //走势
            View old_house_details_house_trend_relative = findViewById(R.id.old_house_details_house_trend_relative);
            //交通地图
            View old_house_details_house_traffic_relative = findViewById(R.id.old_house_details_house_traffic_relative);

            if (descriptionJsonModel != null) {
                if (old_house_details_description_linear.getVisibility() == View.GONE) {
                    old_house_details_description_linear.setVisibility(View.VISIBLE);
                }
                List<String> descriptionList = descriptionJsonModel.getAssort();
                ZjGridView gridView = (ZjGridView) this.findViewById(R.id.old_house_details_description_grid);
                SimpleAdapter simpleAdapter = new SimpleAdapter(this, getGridListData(descriptionList), R.layout.house_details_grid_item, GRID_ADAPTER_FROM, GRID_ADAPTER_TO);
                gridView.setAdapter(simpleAdapter);
                ListView listView = (ListView) this.findViewById(R.id.old_house_details_description_list_view);
                System.out.println(descriptionJsonModel.getInfo());
                //txt=descriptionJsonModel.getInfo();
                SimpleAdapter listViewSimpleAdapter = new SimpleAdapter(this, getListData(descriptionJsonModel.getInfo()), R.layout.house_details_list_item, LIST_ADAPTER_FROM, LIST_ADAPTER_TO);
                listView.setAdapter(listViewSimpleAdapter);
                listView.setEnabled(false);
                setListViewHeightBasedOnChildren(listView);
            } else {
                old_house_details_description_linear.setVisibility(View.GONE);
            }
            //小区简介
            if (communityJsonModel != null && communityJsonModel.getCommunityname() != null) {
                Log.d("communityJsonMode====l",communityJsonModel.toString()) ;
                if (old_house_details_house_intro_linear.getVisibility() == View.GONE) {
                    old_house_details_house_intro_linear.setVisibility(View.VISIBLE);
                }
                cid = communityJsonModel.getCommunityid();
                TextView textViewName = (TextView) findViewById(R.id.old_house_details_house_intro_relative_name_value);
                textViewName.setText(communityJsonModel.getCommunityname());
                TextView textViewPrice = (TextView) findViewById(R.id.old_house_details_house_intro_relative_price_value);
                String sellPrice = communityJsonModel.getSellprice();
                if (sellPrice != null && !sellPrice.isEmpty()) {
                    textViewPrice.setText(sellPrice);
                    System.out.println("sellPrice:"+sellPrice);
                } else {
                    textViewPrice.setText("暂无");
                }
                String sellratio = communityJsonModel.getSellratio();
                TextView compareValue = (TextView) findViewById(R.id.old_house_details_house_intro_relative_compare_value);

                if(sellratio != null && !sellratio.isEmpty()){
                    try {
                        float floatSellratio = Float.parseFloat(sellratio);
                        int tempSellratio = (int) (floatSellratio * 100);
                        compareValue.setText(tempSellratio + "%");
                    } catch (NumberFormatException e) {
                        compareValue.setText(sellratio);
                    }
                }else {
                    compareValue.setVisibility(View.GONE);
                }


                String sellNum = communityJsonModel.getSellnum();
                TextView textViewSellNum = (TextView) findViewById(R.id.old_house_details_house_intro_sell_old_house);
                ImageView textViewSellNumImage = (ImageView) findViewById(R.id.old_house_details_house_intro_sell_old_house_image);
                if (sellNum != null && !sellNum.isEmpty()) {
                    textViewSellNum.setText(communityJsonModel.getCommunityname() + "正在出售" + sellNum + "套");
                } else {
                    textViewSellNum.setText(communityJsonModel.getCommunityname() + "正在出售");
                }


                String rentNum = communityJsonModel.getRentnum();
                TextView textViewRentNum = (TextView) findViewById(R.id.old_house_details_house_intro_rent_old_house);
                ImageView  textViewRentNumImage = (ImageView)findViewById(R.id.old_house_details_house_intro_rent_old_house_image) ;
                if (rentNum != null && !rentNum.isEmpty()) {
                    textViewRentNum.setText(communityJsonModel.getCommunityname() + "正在出租" + rentNum + "套");
                } else {
                    textViewRentNum.setText(communityJsonModel.getCommunityname() + "正在出租");
                }


                if (cid != null && !cid.isEmpty()) { //进入小区二手房列表
                    OldHouseDetailsOnClickListener oldHouseSellNumListener = new OldHouseDetailsOnClickListener();
                    oldHouseSellNumListener.setCommunityId(cid);
                    //跳转到小区二手列表页
                    textViewSellNum.setOnClickListener(oldHouseSellNumListener);
                    textViewSellNumImage.setOnClickListener(oldHouseSellNumListener);

                    OldHouseDetailsOnClickListener rentHouseSellNumListener = new OldHouseDetailsOnClickListener();
                    rentHouseSellNumListener.setCommunityId(cid);
                    textViewRentNum.setOnClickListener(rentHouseSellNumListener);
                    textViewRentNumImage.setOnClickListener(rentHouseSellNumListener);
                }
            } else {
                old_house_details_house_intro_linear.setVisibility(View.GONE);
            }
            if (trend != null && !trend.isEmpty()) {
                if (old_house_details_house_trend_relative.getVisibility() == View.GONE) {
                    old_house_details_house_trend_relative.setVisibility(View.VISIBLE);
                }
                //设置图片
                this.setShowImage(R.id.old_house_details_house_trend_image, trend);
            } else {
                old_house_details_house_trend_relative.setVisibility(View.GONE);
            }
            if (map != null && !map.isEmpty()) {
                if (old_house_details_house_traffic_relative.getVisibility() == View.GONE) {
                    old_house_details_house_traffic_relative.setVisibility(View.VISIBLE);
                }
                this.setShowImage(R.id.old_house_details_house_traffic_image, map);
                TextView textViewAddress = (TextView) findViewById(R.id.old_house_details_house_traffic_address_content);
                String address = houseInfo.getAddress();
                if (address != null && !address.isEmpty()) {
                    textViewAddress.setText(address);
                } else {
                    textViewAddress.setText("暂无");
                }
                TextView textViewBus = (TextView) findViewById(R.id.old_house_details_house_traffic_way_content);
                String bus = houseInfo.getBus();
                if (bus != null && !bus.isEmpty()) {
                    textViewBus.setText(bus);
                } else {
                    textViewBus.setText("暂无");
                }

            } else {
                old_house_details_house_traffic_relative.setVisibility(View.GONE);
            }

        }


        //预约看房
        findViewById(R.id.old_house_details_immediately_apply).setOnClickListener(new OldHouseDetailsOnClickListener());

        if (cid != null && !cid.isEmpty()) {
            //小区详细页
            OldHouseDetailsOnClickListener detailsListener = new OldHouseDetailsOnClickListener();
            detailsListener.setCommunityId(cid);
            findViewById(R.id.old_house_details_house_trend_detail).setOnClickListener(detailsListener);
            OldHouseDetailsOnClickListener attentionListener = new OldHouseDetailsOnClickListener();
            attentionListener.setCommunityId(cid);
            findViewById(R.id.old_house_details_house_trend_attention).setOnClickListener(attentionListener);
        } else {
            findViewById(R.id.old_house_details_house_trend_detail).setVisibility(View.GONE);
            findViewById(R.id.old_house_details_house_trend_attention).setVisibility(View.GONE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 102 && resultCode == RESULT_OK) {
            CollectionAsyncTask collectionAsyncTask = new CollectionAsyncTask();
            collectionAsyncTask.execute();
        } else if (requestCode == 105 && resultCode == RESULT_OK) {
            Log.d("requestCode", "requestCode===" + requestCode);
            AttentionCommunityAsyncTask attentionCommunityAsyncTask = new AttentionCommunityAsyncTask();
            attentionCommunityAsyncTask.execute();
        } else {
            finish();
        }
    }

    public Map<String, String> sendCollection() {
        Map<String, String> resultMap = new HashMap<String, String>();
        HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
        Map<String, String> map = new HashMap<String, String>();
        map.put("cityid", Global.NOW_CITY_ID);
        map.put("id", oldHid);
        map.put("housetype", "1");
        map.put("identity", identity);
        if (!Global.USER_AUTH_STR.equals("")) {
            map.put("authstr", Global.USER_AUTH_STR);
        }
        Optional<Map<String, String>> optional = httpClientUtils.postSignedMap(COLLECTION_URL, map, String.class);
        if (optional.isPresent()) {
            resultMap.put("status", optional.get().get("status"));
            resultMap.put("message", optional.get().get("message"));
            return resultMap;
        }
        return null;
    }

    public Map<String, String> sendPostRequest() {
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
    /**
     * 二手房的详情页面任务
     */
    public class OldHouseDetailsAsyncTask extends AsyncTask<String, Void, JsonResult<OldHouseDetailJsonModel>> {

        @Override
        protected JsonResult<OldHouseDetailJsonModel> doInBackground(String... params) {
            return getDetailDataByNetWork(params[0]);
        }

        @SuppressWarnings("unused")
		@Override
        protected void onPostExecute(JsonResult<OldHouseDetailJsonModel> jsonResult) {

            if (jsonResult != null && jsonResult.isStatus()) {
                findViewById(R.id.house_list_wait_load_relative).setVisibility(View.GONE);
                findViewById(R.id.old_house_details_relative).setVisibility(View.VISIBLE);
                findViewById(R.id.details_back).setOnClickListener(new DetailsOnClickListener());
                imageViews = new ArrayList<ImageView>();
                dotsList = new ArrayList<View>();
                viewPager = (ViewPager) findViewById(R.id.old_house_details_house_image);
                linearLayout = (LinearLayout) findViewById(R.id.old_house_details_house_dotsId);
                OldHouseDetailJsonModel detailJsonModel = jsonResult.getData();
                //设置头消息
                Map<Integer, Object> map = new HashMap<Integer, Object>();
                map.put(header_title, detailJsonModel.getCommunityname());
                map.put(R.id.details_button_collect, getString(R.string.old_house_details_head_button_collect));
                setHeader(map);
                Map<Integer, Object> map1 = new HashMap<Integer, Object>();
                map1.put(R.id.details_button_collect, "visibility:" + View.VISIBLE);
                setHeader(map1);
                findViewById(R.id.details_button_collect).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Global.USER_AUTH_STR.equals("")) {
                            Intent loginIntent = new Intent(OldHouseDetailsActivity.this, LoginActivity.class);
                            startActivityForResult(loginIntent, 102);
                        } else {
                            CollectionAsyncTask collectionAsyncTask = new CollectionAsyncTask();
                            collectionAsyncTask.execute();
                        }

                    }
                });

                //轮播页面设置--------------------------------------------------------//
                List<CommonPicDataJsonModel> picDataList = detailJsonModel.getPicdata().getList();
                //System.out.println("this is image data:"+picDataList);
                if (picDataList.size()==0) {
                	System.out.println("轮播页已经执行"+picDataList);             	
                	ImageView imageView = new ImageView(OldHouseDetailsActivity.this);
                    imageView.setImageResource(R.drawable.house_bg);
                    imageViews.add(imageView);
				}
                if (picDataList != null) {
                    ((TextView) findViewById(R.id.old_house_details_house_image_text_view)).setText("共" + detailJsonModel.getPicdata().getCount() + "图");
                    for (CommonPicDataJsonModel tempJson : picDataList) {
                        final String pid = tempJson.getPid() ;
                        final ImageView imageView = new ImageView(OldHouseDetailsActivity.this);
							new DownloadImageTask().doInBackground(tempJson.getPicUrl(), imageView, R.drawable.house_bg);
	                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
	                        //imageView.setOnClickListener(new ImageViewOnClickListener(tempSlideJson));
	                        imageView.setOnClickListener(new View.OnClickListener() {
	                            @Override
	                            public void onClick(View v) {
	                                Intent intentCommunity = new Intent(OldHouseDetailsActivity.this,CommunityAlbumDetailsActivity.class) ;
	                                intentCommunity.putExtra("hid",oldHid) ;
	                                intentCommunity.putExtra("housetype",housetype) ;
	                                intentCommunity.putExtra("pid",pid) ;
	                                intentCommunity.putExtra("position", currentItem);
	                                startActivity(intentCommunity);
	                            }
	                        });
	                        imageViews.add(imageView);						                     
                    }
                    for (int i = 0; i < imageViews.size(); i++) {
                        View dotView = new View(OldHouseDetailsActivity.this);
                        if (i == 0) {
                            dotView.setBackgroundResource(R.drawable.index_top_dot_selected);
                        } else {
                            dotView.setBackgroundResource(R.drawable.index_top_dot_unselected);
                        }
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ScreenUtil.dps2pixels(5, OldHouseDetailsActivity.this), ScreenUtil.dps2pixels(5, OldHouseDetailsActivity.this));
                        params.setMargins(ScreenUtil.dps2pixels(1.5, OldHouseDetailsActivity.this)
                                , ScreenUtil.dps2pixels(15, OldHouseDetailsActivity.this)
                                , ScreenUtil.dps2pixels(1.5, OldHouseDetailsActivity.this)
                                , ScreenUtil.dps2pixels(15, OldHouseDetailsActivity.this));
                        dotView.setLayoutParams(params);
                        dotsList.add(dotView);
                        linearLayout.addView(dotView);
                    }
                    // 设置一个监听器，当ViewPager中的页面改变时调用
                    viewPager.setOnPageChangeListener(new SplashImageListener());
                    viewPager.setCurrentItem(currentItem);
                } else{
                	Log.d("if image count null", ""+getResources().getIdentifier("house_bg", "drawable", Global.PACKAGE_NAME));
                    ImageView imageView = new ImageView(OldHouseDetailsActivity.this);
                    imageView.setImageResource(getResources().getIdentifier("house_bg", "drawable", Global.PACKAGE_NAME));
                    imageViews.add(imageView);
                }
                // 设置填充ViewPager页面的适配器
                viewPager.setAdapter(new SplashImageAdapter());
                //----------------------------------------------------------------------------------------//
                setDetailData(jsonResult.getData());
            }else {
				System.out.println("jsonResult is null");
			}
        }
    }

    public class CollectionAsyncTask extends AsyncTask<Void, Void, Map<String, String>> {

        @Override
        protected Map<String, String> doInBackground(Void... params) {
            return sendCollection();
        }

        @Override
        protected void onPostExecute(Map<String, String> resultMap) {
            if (resultMap != null) {
                Toast toast = Toast.makeText(getApplicationContext(), resultMap.get("message"), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "网络数据获取失败", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
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
                Toast toast = Toast.makeText(OldHouseDetailsActivity.this, stringStringMap.get("message"), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

            } else {
                Toast toast = Toast.makeText(OldHouseDetailsActivity.this, "网络数据获取失败", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    }

    public class OldHouseDetailsOnClickListener implements View.OnClickListener {

        private String areaName;

        private String communityId;

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.old_house_details_house_intro_sell_old_house_image://二手房子
                    Intent intent = new Intent(OldHouseDetailsActivity.this, OldCommunityListActivity.class);
                    intent.putExtra("cid", communityId);
                    startActivity(intent);
                    break;
                case R.id.old_house_details_house_intro_sell_old_house: //二手房
                    Intent intent1 = new Intent(OldHouseDetailsActivity.this, OldCommunityListActivity.class);
                    intent1.putExtra("cid", communityId);
                    startActivity(intent1);
                    break;
                case R.id.old_house_details_house_trend_detail: //小区详细信息
                    Intent intent2 = new Intent(OldHouseDetailsActivity.this, OldHouseAreaDetailsActivity.class);
                    intent2.putExtra("areaName", this.getAreaName());
                    intent2.putExtra("cid", communityId);
                    startActivity(intent2);
                    break;
                case R.id.old_house_details_immediately_apply://预约看房
                    Intent intent3 = new Intent(OldHouseDetailsActivity.this, LookHouseActivity.class);
                    intent3.putExtra("hid", oldHid);
                    intent3.putExtra("housetype", housetype);
                    startActivity(intent3);
                    break;
                case R.id.old_house_details_house_intro_rent_old_house_image:  //小区出租房
                    Intent intent4 = new Intent(OldHouseDetailsActivity.this, RentCommunityListActivity.class);
                    intent4.putExtra("cid", communityId);
                    startActivity(intent4);
                    break;
                case R.id.old_house_details_house_intro_rent_old_house:   //小区出租房
                    Intent intent5 = new Intent(OldHouseDetailsActivity.this, RentCommunityListActivity.class);
                    intent5.putExtra("cid", communityId);
                    startActivity(intent5);
                    break;
                case R.id.old_house_details_house_trend_attention:
                    if (!Global.USER_AUTH_STR.equals("")) {
                        AttentionCommunityAsyncTask attentionCommunityAsyncTask = new AttentionCommunityAsyncTask();
                        attentionCommunityAsyncTask.execute();
                    } else {
                        Intent loginIntent = new Intent(OldHouseDetailsActivity.this, LoginActivity.class);
                        startActivityForResult(loginIntent, 105);
                    }
                    break;
            }

        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public String getCommunityId() {
            return communityId;
        }

        public void setCommunityId(String communityId) {
            this.communityId = communityId;
        }
    }
}
