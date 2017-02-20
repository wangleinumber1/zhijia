package com.zhijia.ui.zhijiaActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.common.base.Optional;
import com.zhijia.Global;
import com.zhijia.service.data.Medol.AlbumJsonModel;
import com.zhijia.service.data.Medol.HousePicture;
import com.zhijia.service.data.Medol.JsonResult;
import com.zhijia.service.network.HttpClientUtils;
import com.zhijia.ui.R;
import com.zhijia.ui.list.adapter.ItemAdapter;
import com.zhijia.util.DefaultDataUtils;

import java.util.*;

/**
 *
 */
public class NewHouseAlbumActivity extends BaseDetailsActivity {

    private final String ALBUM_URL = Global.API_WEB_URL + "/xinfang/album";
    private final int header_title = R.id.details_title;
    private String hid;
    private Map<String, HousePicture> pictureMap;
    private final int count=0;
    private Map<String, TextView> titleLinearMap = new TreeMap<String, TextView>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_house_album);
        pictureMap = new TreeMap<String, HousePicture>();
        hid = (String) getIntent().getSerializableExtra("hid");
        // addTitle() ;
        NavigationAsyncTask navigationAsyncTask = new NavigationAsyncTask();
        navigationAsyncTask.execute(ALBUM_URL);
    }


    public void setHeader(String headerName) {
        Map<Integer, Object> map = new HashMap<Integer, Object>();
        map.put(header_title, headerName);
        setHeader(map);
        Map<Integer, Object> map1 = new HashMap<Integer, Object>();
        setHeader(map1);
    }


    //标题头
    public void addTitle(Map<String, HousePicture> pictureMap) {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.new_house_album_navigation_linear_layout);
        int titleCount = 0;
        for (Map.Entry<String, HousePicture> temp : pictureMap.entrySet()) {
            LinearLayout itemLinearLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.new_house_album_item, null);
            TextView tempTextView = (TextView) itemLinearLayout.findViewById(R.id.new_house_album_item_text);
            tempTextView.setText(temp.getValue().getCatname());
            System.out.println("Album TextView:"+temp.getValue().getCatname());
            if (titleCount == 0) {
                Log.d("addTitle->>>", "red");
                tempTextView.setTextColor(Color.parseColor("#FF0033"));
                setImages(temp.getValue());
            }
            titleLinearMap.put(temp.getKey(), tempTextView);
            linearLayout.addView(itemLinearLayout);
            titleCount++;
        }

        for (Map.Entry<String, TextView> temp : titleLinearMap.entrySet()) {
            final TextView tempTextView = temp.getValue();
            final String keyStr = temp.getKey();
            temp.getValue().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tempTextView.setTextColor(Color.parseColor("#FF0033"));
                    for (Map.Entry<String, TextView> tempOther : titleLinearMap.entrySet()) {
                        if (!keyStr.equalsIgnoreCase(tempOther.getKey())) {
                            tempOther.getValue().setTextColor(Color.parseColor("#000000"));
                        }
                    }
                    setImages(getPictureMap().get(keyStr));
                }
            });
        }
    }


    public void setImages(final HousePicture picture) {
        Log.d("NewHouseAlbumActivity->count->", picture.getCount());
        TextView textView = (TextView) findViewById(R.id.new_house_album_title_text_view);
        textView.setText(picture.getCatname() + "(" + picture.getCount() + ")");
        GridView gridView = (GridView) findViewById(R.id.new_house_album_zj_grid_view);
        final List<Map<Integer, Object>> gridList = new ArrayList<Map<Integer, Object>>();
        for (HousePicture.ListJsonModel listJsonModel : picture.getList()) {
            Map<Integer, Object> map = new HashMap<Integer, Object>();
            Log.d("NewHouseAlbumActivity->pic->>>", "http://" + listJsonModel.getPic());
            map.put(R.id.new_house_album_grid_item_image_view, "http://" + listJsonModel.getPic());
            map.put(-1, listJsonModel.getPid());
            gridList.add(map);
        }
        ItemAdapter itemAdapter = new ItemAdapter(NewHouseAlbumActivity.this, R.layout.new_house_album_grid_item, gridList);
        gridView.setAdapter(itemAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	System.out.println("图片类型："+picture.getCatname());
                Map<Integer, Object> mapItem = gridList.get(position);
                Intent intent = new Intent(NewHouseAlbumActivity.this, NewHouseAlbumDetailsActivity.class);
                Log.d(">>>>>pid", (String)mapItem.get(-1));
                Log.d(">>>>>hid", hid);
                Log.d(">>>>>>>position", ""+position);
                intent.putExtra("position", position);	
                intent.putExtra("pid", (String) mapItem.get(-1));
                intent.putExtra("hid", hid);
                startActivity(intent);
            }
        });
    }

    private JsonResult<AlbumJsonModel> getNavigation(String url) {
        HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
        Map<String, String> map = new HashMap<String, String>();
        map.put("cityid", Global.NOW_CITY_ID);
        map.put("hid", hid);
        Optional<JsonResult<AlbumJsonModel>> optional = httpClientUtils.getUnsignedByData(url, map, AlbumJsonModel.class);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    public Map<String, HousePicture> getPictureMap() {
        return pictureMap;
    }

    public class NavigationAsyncTask extends AsyncTask<String, Void, JsonResult<AlbumJsonModel>> {

        @Override
        protected JsonResult<AlbumJsonModel> doInBackground(String... params) {
        	System.out.println("params:"+params[0]);
            return getNavigation(params[0]);
        }

        @Override
        protected void onPostExecute(JsonResult<AlbumJsonModel> jsonResult) {
            findViewById(R.id.new_house_album_list_wait_load_relative).setVisibility(View.GONE);
            findViewById(R.id.details_back).setOnClickListener(new DetailsOnClickListener());
            if (jsonResult != null && jsonResult.isStatus()) {
                findViewById(R.id.new_house_album_linear_layout).setVisibility(View.VISIBLE);
                AlbumJsonModel albumJsonModel = jsonResult.getData();
                Map<String, Object> map = jsonResult.getData().getPicture();
                for (Map.Entry<String, Object> tempMap : map.entrySet()) {
                    if (tempMap.getKey().equalsIgnoreCase("house_name")) {
                        albumJsonModel.setHouseName((String) tempMap.getValue());
                        Log.d("NavigationAsyncTask--->albumJsonModel.getHouseName()", albumJsonModel.getHouseName());
                    } else {
                        String listStr = tempMap.getValue().toString();
                        HousePicture hp = DefaultDataUtils.convertObjectByString(listStr, HousePicture.class);
                        Log.d("NavigationAsyncTask--->hp.getCatname()", hp.getCatname());
                        Log.d("NavigationAsyncTask--->hp.getList().get(0).getPic()", hp.getList().get(0).getPic());
                        pictureMap.put(tempMap.getKey(), hp);
                    }
                }
                setHeader(albumJsonModel.getHouseName());
                addTitle(pictureMap);
            } else {
                findViewById(R.id.new_house_album_list_wait_load_image).setVisibility(View.GONE);
                TextView textViewLoad = (TextView) findViewById(R.id.new_house_album__list_wait_load_content);
                textViewLoad.setText(Global.NOT_FIND_DATA);
            }
        }
    }
}
