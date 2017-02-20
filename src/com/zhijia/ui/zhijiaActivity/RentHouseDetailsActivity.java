package com.zhijia.ui.zhijiaActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.zhijia.Global;
import com.zhijia.ui.R;
import com.zhijia.ui.list.oldhouse.OldHouseDetailsGridItem;
import com.zhijia.ui.view.ZjGridView;
import com.zhijia.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 租房详情
 */
public class RentHouseDetailsActivity extends BaseDetailsActivity {


    private final int header_title = R.id.details_title;

    private final String[] GRID_ADAPTER_FROM = {"old_house_details_description_grid_image", "old_house_details_description_grid_text"};

    private final String[] LIST_ADAPTER_FROM = {"old_house_details_list_text"};

    private final int[] LIST_ADAPTER_TO = {R.id.house_details_list_text};

    private final int[] GRID_ADAPTER_TO = {R.id.house_details_description_grid_image, R.id.house_details_description_grid_text};

    private final String DETAIL_URL = Global.API_WEB_URL + "/house/detail";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rent_house_details);
        findViewById(R.id.details_back).setOnClickListener(new DetailsOnClickListener());
        Map<Integer, Object> map = new HashMap<Integer, Object>();
        String areaName = (String) getIntent().getSerializableExtra("adapter_house_item_two_area");
        map.put(header_title, StringUtils.trimStringWithAppointedChar(areaName, "[", "]"));
        map.put(R.id.details_button_collect, getString(R.string.old_house_details_head_button_collect));
        this.setHeader(map);
        Map<Integer, Object> map1 = new HashMap<Integer, Object>();
        map1.put(R.id.details_button_collect, "visibility:" + View.VISIBLE);
        this.setHeader(map1);
        ZjGridView gridView = (ZjGridView) this.findViewById(R.id.rent_house_details_description_grid);
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, getGridListData(), R.layout.house_details_grid_item, GRID_ADAPTER_FROM, GRID_ADAPTER_TO);
        gridView.setAdapter(simpleAdapter);
        ListView listView = (ListView) this.findViewById(R.id.rent_house_details_description_list_view);
        SimpleAdapter listViewSimpleAdapter = new SimpleAdapter(this, getListData(), R.layout.house_details_list_item, LIST_ADAPTER_FROM, LIST_ADAPTER_TO);
        listView.setAdapter(listViewSimpleAdapter);
        setListViewHeightBasedOnChildren(listView);

        this.setShowImage(R.id.rent_house_details_house_trend_image, "http://img6.ph.126.net/hBiG96B8egigBULxUWcOpA==/109212290980771276.jpg");
        //跳转到小区二手列表页
        findViewById(R.id.rent_house_details_house_intro_sell_old_house_image).setOnClickListener(new RentHouseDetailsOnClickListener());
        findViewById(R.id.rent_house_details_house_intro_sell_old_house).setOnClickListener(new RentHouseDetailsOnClickListener());

        //租房的跳转到小区列表页
        findViewById(R.id.rent_house_details_house_intro_rent_old_house_image).setOnClickListener(new RentHouseDetailsOnClickListener());
        findViewById(R.id.rent_house_details_house_intro_rent_old_house).setOnClickListener(new RentHouseDetailsOnClickListener());

        //预约看房
        findViewById(R.id.rent_house_details_immediately_apply).setOnClickListener(new RentHouseDetailsOnClickListener());

        //小区详细页
        RentHouseDetailsOnClickListener detailsListener = new RentHouseDetailsOnClickListener();
        detailsListener.setAreaName(((TextView) findViewById(R.id.rent_house_details_house_intro_relative_name_value)).getText().toString());
        findViewById(R.id.rent_house_details_house_trend_detail).setOnClickListener(detailsListener);

        //HouseDBUtil.addBrowseHistory(newHid, HouseDBUtil.HouseType.RENT_HOUSE);
    }


    public List<Map<String, Object>> getGridListData() {

        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

        for (OldHouseDetailsGridItem temp : getRentHouseDetailsGridItem()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("old_house_details_description_grid_image", temp.getImageId());
            map.put("old_house_details_description_grid_text", temp.getGridContent());
            resultList.add(map);
        }

        return resultList;
    }


    public List<Map<String, Object>> getListData() {

        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

        for (int i = 0; i < 10; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("old_house_details_list_text", "房子一所经过精心装修过的两居室，室内整洁干净，配备齐全 配备齐全 配备齐全 配备齐全配备齐全配备齐全配备齐全配备齐全配备齐全");

            resultList.add(map);
        }

        return resultList;
    }

    private List<OldHouseDetailsGridItem> getRentHouseDetailsGridItem() {
        OldHouseDetailsGridItem temp = new OldHouseDetailsGridItem();
        List<OldHouseDetailsGridItem> resultList = new ArrayList<OldHouseDetailsGridItem>();
        temp.setImageId(R.drawable.yes);
        temp.setGridContent("有线电视机");
        resultList.add(temp);
        temp = new OldHouseDetailsGridItem();
        temp.setImageId(R.drawable.yes);
        temp.setGridContent("双人床");
        resultList.add(temp);
        temp = new OldHouseDetailsGridItem();
        temp.setImageId(R.drawable.yes);
        temp.setGridContent("家电");
        resultList.add(temp);
        temp = new OldHouseDetailsGridItem();
        temp.setImageId(R.drawable.yes);
        temp.setGridContent("灶具");
        resultList.add(temp);
        temp = new OldHouseDetailsGridItem();
        temp.setImageId(R.drawable.yes);
        temp.setGridContent("家具");
        resultList.add(temp);
        temp = new OldHouseDetailsGridItem();
        temp.setImageId(R.drawable.yes);
        temp.setGridContent("宽带");
        resultList.add(temp);
        temp = new OldHouseDetailsGridItem();
        temp.setImageId(R.drawable.yes);
        temp.setGridContent("暖气");
        resultList.add(temp);
        return resultList;
    }

    public class RentHouseDetailsOnClickListener implements View.OnClickListener {

        private String areaName;

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rent_house_details_house_intro_sell_old_house_image:
                    Intent intent = new Intent(RentHouseDetailsActivity.this, CommunityListActivity.class);
                    startActivity(intent);
                    break;
                case R.id.rent_house_details_house_intro_sell_old_house:
                    Intent intent1 = new Intent(RentHouseDetailsActivity.this, CommunityListActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.rent_house_details_house_trend_detail:
                    Intent intent2 = new Intent(RentHouseDetailsActivity.this, OldHouseAreaDetailsActivity.class);
                    intent2.putExtra("areaName", this.getAreaName());
                    startActivity(intent2);
                    break;
                case R.id.rent_house_details_immediately_apply:
                    Intent intent3 = new Intent(RentHouseDetailsActivity.this, LookHouseActivity.class);
                    startActivity(intent3);
                    break;
                case R.id.rent_house_details_house_intro_rent_old_house_image:
                    Intent intent4 = new Intent(RentHouseDetailsActivity.this, CommunityListActivity.class);
                    startActivity(intent4);
                    break;
                case R.id.rent_house_details_house_intro_rent_old_house:
                    Intent intent5 = new Intent(RentHouseDetailsActivity.this, CommunityListActivity.class);
                    startActivity(intent5);
                    break;
            }

        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }
    }
}
