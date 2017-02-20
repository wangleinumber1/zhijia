package com.zhijia.ui.list.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.google.common.base.Optional;
import com.zhijia.Global;
import com.zhijia.service.network.HttpClientUtils;
import com.zhijia.ui.R;
import com.zhijia.ui.zhijiaActivity.CommentReplyActivity;
import com.zhijia.ui.zhijiaActivity.LookHouseActivity;
import com.zhijia.util.DownloadImageTask;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemAdapter extends BaseAdapter {

    private final String VIEW_POINR_URL = Global.API_WEB_URL + "/xinfang/viewpoint";

    private final String ATTENTION_URL = Global.API_WEB_URL + "/community/attention";

    private final String ATTENTION_NEW_HOUSE_URL = Global.API_WEB_URL + "/xinfang/attention";

    private final String CANCEL_COLLECTION_URL = Global.API_WEB_URL + "/member/collection";

    private final String COLLECTION_URL = Global.API_WEB_URL + "/house/collection";

    private final String ATTENTION_COMMUNITY_URL = Global.API_WEB_URL + "/community/attention";

    //布局的ID
    private int layoutInflaterId;
    private LayoutInflater mInflater;
    private List<Map<Integer, Object>> listData;
    private WeakReference<Activity> week;

    public ItemAdapter(Context context, int layoutInflaterId, List<Map<Integer, Object>> listData) {
        this.layoutInflaterId = layoutInflaterId;
        this.listData = listData;
        mInflater = LayoutInflater.from(context);
    }

    public List<Map<Integer, Object>> getListData() {
        return listData;
    }

    public void setListData(List<Map<Integer, Object>> listData) {
        this.listData = listData;
    }

    @Override
    public int getCount() {
        if (listData != null && !this.listData.isEmpty()) {
            return listData.size();
        } else {
            return 1;
        }
    }

    @Override
    public Object getItem(int position) {
        if (listData != null && !this.listData.isEmpty()) {
            return listData.get(position);
        } else {
            return null;
        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (this.listData != null && !this.listData.isEmpty()) {
            Map<Integer, Object> map = this.listData.get(position);
            if (this.listData.size() == 1 && map.get(0) != null) {
                String content = (String) map.get(0);
                convertView = mInflater.inflate(R.layout.house_list_wait_load, null);
                View imageView = convertView.findViewById(R.id.house_list_wait_load_image);
                imageView.setVisibility(View.GONE);
                TextView textView = (TextView) convertView.findViewById(R.id.house_list_wait_load_content);
                textView.setText(content);
                textView.setTextSize(16);
                textView.setTextColor(R.color.black);
                return convertView;
            }

            convertView = mInflater.inflate(layoutInflaterId, null);
            final Context context = convertView.getContext();
            Map<Integer, Object> tempMap = this.listData.get(position);
            switch (this.layoutInflaterId) {
                case R.layout.comment_list_item:
                    final String cid = (String) tempMap.get(-1);
                    final String hid = (String) tempMap.get(-2);
                    //踩的事件
                    convertView.findViewById(R.id.comment_list_item_reply_trample).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ViewpointAsyncTask viewpointAsyncTask = new ViewpointAsyncTask(context);
                            viewpointAsyncTask.execute(cid, "down");
                        }
                    });
                    convertView.findViewById(R.id.comment_list_item_reply_top).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ViewpointAsyncTask viewpointAsyncTask = new ViewpointAsyncTask(context);
                            viewpointAsyncTask.execute(cid, "digg");
                        }
                    });
                    convertView.findViewById(R.id.comment_list_item_reply).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent replyIntent = new Intent(context, CommentReplyActivity.class);
                            replyIntent.putExtra("cid", cid);
                            replyIntent.putExtra("hid", hid);
                            getWeek().get().startActivity(replyIntent);
                        }
                    });
                    break;
                case R.layout.my_attention_community_list_item:

                    // Map<Integer, Object> attentionMap = this.listData.get(position);
                    Map<String, String> paraMap = new HashMap<String, String>();
                    final ParamObject paramObject = new ParamObject();
                    paramObject.setUrl(ATTENTION_URL);
                    String id = (String) tempMap.get(-2);
                    paraMap.put("authstr", Global.USER_AUTH_STR);
                    paraMap.put("cityid", Global.NOW_CITY_ID);
                    paraMap.put("id", id);
                    paramObject.setMap(paraMap);
                    convertView.findViewById(R.id.community_list_cancel_attention).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            ItemAdapterAsyncTask itemAdapterAsyncTask = new ItemAdapterAsyncTask(context);
                            itemAdapterAsyncTask.execute(paramObject);
                        }
                    });
                    convertView.findViewById(R.id.community_list_cancel_attention_none).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            ItemAdapterAsyncTask itemAdapterAsyncTask = new ItemAdapterAsyncTask(context);
                            itemAdapterAsyncTask.execute(paramObject);
                        }
                    });


                    break;
                case R.layout.my_browse_new_house_list_item:
                    //  Map<Integer, Object> attentionNewHouseMap = this.listData.get(position);
                    View tempView = convertView.findViewById(R.id.attention_item_action);
                    final ParamObject paramObject1 = new ParamObject();
                    Map<String, String> paraMap1 = new HashMap<String, String>();
                    String id1 = (String) tempMap.get(-2);
                    paraMap1.put("authstr", Global.USER_AUTH_STR);
                    paraMap1.put("cityid", Global.NOW_CITY_ID);
                    paraMap1.put("id", id1);
                    paramObject1.setUrl(ATTENTION_NEW_HOUSE_URL);
                    paramObject1.setMap(paraMap1);
                    if (tempView != null) {
                        tempView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.d("attention_item_action", "begin");
                                ItemAdapterPostAsyncTask itemAdapterPostAsyncTask = new ItemAdapterPostAsyncTask(context);
                                itemAdapterPostAsyncTask.execute(paramObject1);
                            }
                        });
                    }

                    break;

                case R.layout.my_favorites_old_house_list_item:
                    View favoritesOldHouseView = convertView.findViewById(R.id.item_action_one);
                    View cancelFavoritesView = convertView.findViewById(R.id.item_action_two);
                    final String oldHid = (String) tempMap.get(-1);
                    final ParamObject favoritesParamObject = new ParamObject();
                    Map<String, String> favoritesMap = new HashMap<String, String>();
                    favoritesMap.put("authstr", Global.USER_AUTH_STR);
                    favoritesMap.put("cityid", Global.NOW_CITY_ID);
                    favoritesMap.put("id", oldHid);
                    favoritesMap.put("identity", "1");
                    favoritesParamObject.setMap(favoritesMap);
                    favoritesParamObject.setUrl(CANCEL_COLLECTION_URL);
                    favoritesOldHouseView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent3 = new Intent(context, LookHouseActivity.class);
                            intent3.putExtra("hid", oldHid);
                            intent3.putExtra("housetype", "1");
                            getWeek().get().startActivity(intent3);
                        }
                    });
                    cancelFavoritesView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ItemAdapterPostAsyncTask itemAdapterPostAsyncTask = new ItemAdapterPostAsyncTask(context);
                            itemAdapterPostAsyncTask.execute(favoritesParamObject);
                        }
                    });
                    break;

                case R.layout.my_browse_old_house_list_item:
                    View browseOldHouseView = convertView.findViewById(R.id.item_action);
                    String browseOldHid = (String) tempMap.get(-1);
                    final ParamObject browseOldHouseParamObject = new ParamObject();
                    Map<String, String> browseOldHouseMap = new HashMap<String, String>();
                    browseOldHouseMap.put("authstr", Global.USER_AUTH_STR);
                    browseOldHouseMap.put("cityid", Global.NOW_CITY_ID);
                    browseOldHouseMap.put("id", browseOldHid);
                    browseOldHouseMap.put("identity", "1");
                    browseOldHouseMap.put("housetype", "1");
                    browseOldHouseParamObject.setMap(browseOldHouseMap);
                    browseOldHouseParamObject.setUrl(COLLECTION_URL);
                    browseOldHouseView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ItemAdapterPostAsyncTask itemAdapterPostAsyncTask = new ItemAdapterPostAsyncTask(context);
                            itemAdapterPostAsyncTask.execute(browseOldHouseParamObject);
                        }
                    });
                    break;

                case R.layout.my_favorites_rent_house_list_item:
                    View favoritesRentHouseView = convertView.findViewById(R.id.item_action_one);
                    View cancelFavoritesRentView = convertView.findViewById(R.id.item_action_two);
                    final String rentId = (String) tempMap.get(-1);
                    final ParamObject favoritesRentParamObject = new ParamObject();
                    Map<String, String> favoritesRentMap = new HashMap<String, String>();
                    favoritesRentMap.put("authstr", Global.USER_AUTH_STR);
                    favoritesRentMap.put("cityid", Global.NOW_CITY_ID);
                    favoritesRentMap.put("id", rentId);
                    favoritesRentMap.put("identity", "2");
                    favoritesRentParamObject.setMap(favoritesRentMap);
                    favoritesRentParamObject.setUrl(CANCEL_COLLECTION_URL);
                    favoritesRentHouseView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent3 = new Intent(context, LookHouseActivity.class);
                            intent3.putExtra("hid", rentId);
                            intent3.putExtra("housetype", "2");
                            getWeek().get().startActivity(intent3);
                        }
                    });
                    cancelFavoritesRentView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ItemAdapterPostAsyncTask itemAdapterPostAsyncTask = new ItemAdapterPostAsyncTask(context);
                            itemAdapterPostAsyncTask.execute(favoritesRentParamObject);
                        }
                    });
                    break;

                case R.layout.my_browse_rent_house_list_item:
                    View browseRentHouseView = convertView.findViewById(R.id.item_action);
                    String browseRentHid = (String) tempMap.get(-1);
                    final ParamObject browseRentHouseParamObject = new ParamObject();
                    Map<String, String> browseRentHouseMap = new HashMap<String, String>();
                    browseRentHouseMap.put("authstr", Global.USER_AUTH_STR);
                    browseRentHouseMap.put("cityid", Global.NOW_CITY_ID);
                    browseRentHouseMap.put("id", browseRentHid);
                    browseRentHouseMap.put("identity", "2");
                    browseRentHouseMap.put("housetype", "2");
                    browseRentHouseParamObject.setMap(browseRentHouseMap);
                    browseRentHouseParamObject.setUrl(COLLECTION_URL);
                    browseRentHouseView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ItemAdapterPostAsyncTask itemAdapterPostAsyncTask = new ItemAdapterPostAsyncTask(context);
                            itemAdapterPostAsyncTask.execute(browseRentHouseParamObject);
                        }
                    });
                    break;

                case R.layout.my_browse_community_list_item:
                    View browseCommunityView = convertView.findViewById(R.id.item_action);
                    String browseCommunityId = (String) tempMap.get(-2);
                    final ParamObject browseCommunityParamObject = new ParamObject();
                    Map<String, String> browseCommunityMap = new HashMap<String, String>();
                    browseCommunityMap.put("authstr", Global.USER_AUTH_STR);
                    browseCommunityMap.put("cityid", Global.NOW_CITY_ID);
                    browseCommunityMap.put("id", browseCommunityId);
                    browseCommunityParamObject.setMap(browseCommunityMap);
                    browseCommunityParamObject.setUrl(ATTENTION_COMMUNITY_URL);
                    browseCommunityView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ItemAdapterPostAsyncTask itemAdapterPostAsyncTask = new ItemAdapterPostAsyncTask(context);
                            itemAdapterPostAsyncTask.execute(browseCommunityParamObject);
                        }
                    });
                    break;

            }
            for (Map.Entry<Integer, Object> tempEntry : map.entrySet()) {
                View view = convertView.findViewById(tempEntry.getKey());
                Object value = tempEntry.getValue();
                Log.d("itemAdapter->>", "value===" + value);
                if (value != null) {
                    String[] str = value.toString().split(":");
                    //主要是用来做不一样的数据交互的，例如：如果数据就显示123|456，没有数据就显示 暂时无报价
                    //map.put(R.id.community_list_simple,"Visibility:"+View.VISIBLE) ;
                    //map.put(R.id.community_list_details,"Visibility:"+View.GONE) ;
                    if (str.length == 2 && str[0].equalsIgnoreCase("visibility")) {
                        Log.d("itemAdapter->>", "key===" + tempEntry.getKey() + "::::" + str[1] + "");
                        view.setVisibility(Integer.parseInt(str[1]));
                    }
                    if (view instanceof ImageView) {
                        if (tempEntry.getValue() instanceof String) {
                            //有一种情况，是前面是图片的ID后面是表示它是否显示
                            if (str.length == 2 && str[1].equalsIgnoreCase("VISIBLE")) {
                                view.setVisibility(View.VISIBLE);
                                int imageResourceId = Integer.parseInt(str[0]);
                                ((ImageView) view).setImageResource(imageResourceId);
                            } else {
                                final ImageView imageView = ((ImageView) view);
                                new DownloadImageTask().doInBackground((String) tempEntry.getValue(), imageView, null);
                            }
                        } else {
                            ((ImageView) view).setImageResource((Integer) tempEntry.getValue());
                        }
                    } else if (view instanceof TextView) {

                        Integer key = tempEntry.getKey();

                        if (key == R.id.adapter_house_item_two_des || key == R.id.adapter_house_item_two_des_two || key == R.id.adapter_house_item_two_des_three || key == R.id.adapter_content_four) {
                            if (str.length == 2 && str[1].equalsIgnoreCase("VISIBLE")) {
                                ((TextView) view).setVisibility(View.VISIBLE);
                                ((TextView) view).setText(str[0]);
                            }
                        } else if (str.length == 3 && str[0].equalsIgnoreCase("text_color")) {
                            Log.d("ItemAdapter->>TextView", str[0] + ":" + str[1]);
                            ((TextView) view).setTextColor(Integer.valueOf(str[1]));
                            ((TextView) view).setText(str[2]);
                        } else if (str.length == 3 && str[0].equalsIgnoreCase("background")) {
                            Log.d("ItemAdapter->>background", str[0] + ":" + str[1]);
                            //view.setBackgroundColor(R.color.red);
                            view.setBackgroundColor(Color.parseColor(str[1]));
                            ((TextView) view).setText(str[2]);
                        } else {
                            Log.d("ItemAdapter->>TextView", (String) tempEntry.getValue());
                            ((TextView) view).setText((String) tempEntry.getValue());
                        }
                    } else if (view instanceof RatingBar) {
                        ((RatingBar) view).setRating(Float.parseFloat((String) tempEntry.getValue()));
                    }
                } else if (view instanceof TextView) {
                    ((TextView) view).setText("暂无");
                }

            }
        } else {
            convertView = mInflater.inflate(R.layout.house_list_wait_load, null);
        }

        return convertView;
    }

    public void addItem(Map<Integer, Object> map) {
        listData.add(map);
    }

    public Map<String, String> sendViewpoint(String cid, String field) {
        Map<String, String> resultMap = new HashMap<String, String>();
        HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
        Map<String, String> map = new HashMap<String, String>();
        map.put("cid", cid);
        map.put("field", field);
        Optional<Map<String, String>> optional = httpClientUtils.postSignedMap(VIEW_POINR_URL, map, String.class);
        if (optional.isPresent()) {
            resultMap.put("status", optional.get().get("status"));
            resultMap.put("message", optional.get().get("message"));
            return resultMap;
        }
        return null;
    }

    public WeakReference<Activity> getWeek() {
        return week;
    }

    public void setWeek(WeakReference<Activity> week) {
        this.week = week;
    }

    public Map<String, String> sendRequest(ParamObject paramObject) {
        Map<String, String> resultMap = new HashMap<String, String>();
        HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
        Optional<Map<String, String>> optional = httpClientUtils.getUnsignedMap(paramObject.getUrl(), paramObject.getMap(), String.class);
        if (optional.isPresent()) {
            resultMap.put("status", optional.get().get("status"));
            resultMap.put("message", optional.get().get("message"));
            return resultMap;
        }
        return null;
    }

    public Map<String, String> sendPostRequest(ParamObject paramObject) {
        Map<String, String> resultMap = new HashMap<String, String>();
        HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
        Optional<Map<String, String>> optional = httpClientUtils.postSignedMap(paramObject.getUrl(), paramObject.getMap(), String.class);
        if (optional.isPresent()) {
            resultMap.put("status", optional.get().get("status"));
            resultMap.put("message", optional.get().get("message"));
            return resultMap;
        }
        return null;
    }

    public class ParamObject {
        private String url;
        private Map<String, String> map;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Map<String, String> getMap() {
            return map;
        }

        public void setMap(Map<String, String> map) {
            this.map = map;
        }
    }

    public class ViewpointAsyncTask extends AsyncTask<String, Void, Map<String, String>> {

        Context context;

        public ViewpointAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected Map<String, String> doInBackground(String... params) {
            return sendViewpoint(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(Map<String, String> stringStringMap) {
            if (stringStringMap != null) {
                Toast toast = Toast.makeText(context, stringStringMap.get("message"), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

            } else {
                Toast toast = Toast.makeText(context, "网络数据获取失败", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    }

    public class ItemAdapterPostAsyncTask extends AsyncTask<ParamObject, Void, Map<String, String>> {

        private Context context;

        public ItemAdapterPostAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected Map<String, String> doInBackground(ParamObject... params) {
            return sendPostRequest(params[0]);
        }

        @Override
        protected void onPostExecute(Map<String, String> stringStringMap) {
            if (stringStringMap != null) {
                Toast toast = Toast.makeText(context, stringStringMap.get("message"), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

            } else {
                Toast toast = Toast.makeText(context, "网络数据获取失败", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    }

    public class ItemAdapterAsyncTask extends AsyncTask<ParamObject, Void, Map<String, String>> {

        private Context context;

        public ItemAdapterAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected Map<String, String> doInBackground(ParamObject... params) {
            return sendRequest(params[0]);
        }

        @Override
        protected void onPostExecute(Map<String, String> stringStringMap) {
            if (stringStringMap != null) {
                Toast toast = Toast.makeText(context, stringStringMap.get("message"), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

            } else {
                Toast toast = Toast.makeText(context, "网络数据获取失败", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    }
}
