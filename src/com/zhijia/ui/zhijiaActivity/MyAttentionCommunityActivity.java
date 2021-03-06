package com.zhijia.ui.zhijiaActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.google.common.base.Optional;
import com.zhijia.Global;
import com.zhijia.service.data.Medol.AttentionCommunityJsonModel;
import com.zhijia.service.data.Medol.JsonResult;
import com.zhijia.service.network.HttpClientUtils;
import com.zhijia.ui.R;
import com.zhijia.ui.list.adapter.ItemAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我关注的小区列表
 */
public class MyAttentionCommunityActivity extends MyBaseHouseActivity<AttentionCommunityJsonModel> {

    private final String ATTENTION_COMMUNITY_RUL = Global.API_WEB_URL + "/member/attentionxqList";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_common_house_list);
        setCountStr(R.string.total_attention_community_count);
        setCountId(R.id.total_count_desc);
        ListView listView = (ListView) findViewById(R.id.my_common_house_list);
        Map<String, String> tempMap = new HashMap<String, String>();
        tempMap.put("cityid", Global.NOW_CITY_ID);
        initView(listView, R.layout.my_attention_community_list_item, tempMap);
        Map<Integer, String> titleMap = new HashMap<Integer, String>();
        titleMap.put(R.id.title, getString(R.string.my_attention_community));
        setTitle(titleMap, R.id.back);
        setLogin(MyAttentionCommunityActivity.this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView1 = (ListView) parent;
                ListAdapter listAdapter = listView1.getAdapter();
                ItemAdapter tempAdapter = null;
                Log.d("bindEvent->", position + "");

                if (listAdapter instanceof HeaderViewListAdapter) {
                    tempAdapter = (ItemAdapter) (((HeaderViewListAdapter) listAdapter).getWrappedAdapter());
                } else {
                    tempAdapter = (ItemAdapter) listAdapter;
                }
                Map<Integer,Object> tempMap= (Map<Integer,Object>) tempAdapter.getItem(position);
                Intent intent  = new Intent(MyAttentionCommunityActivity.this, OldHouseAreaDetailsActivity.class) ;
                intent.putExtra("cid",(String)tempMap.get(-2));
                startActivity(intent);
            }
        });
    }

    @Override
    public void convertToAdapterList(List<AttentionCommunityJsonModel> list, List<Map<Integer, Object>> resultList, ItemAdapter itemAdapter) {
        for (AttentionCommunityJsonModel jsonModel : list) {
            Map<Integer, Object> map = new HashMap<Integer, Object>();
            map.put(-2, jsonModel.getId());
            if ((jsonModel.getSellPrice() != null && !jsonModel.getSellPrice().isEmpty()) && (jsonModel.getRentPrice() != null && !jsonModel.getRentPrice().isEmpty())) {
                setIsUpAndDown(View.VISIBLE, View.GONE, View.GONE, View.GONE, map);
                setCommonListData(jsonModel, map);
                //每平米的价钱
                map.put(R.id.community_list_type, jsonModel.getSellPrice());

                //每平米的涨幅
                String strSellratio = jsonModel.getSellRatio();
                //租金
                map.put(R.id.community_list_split_price, jsonModel.getRentPrice());
                String strRentratio = jsonModel.getRentRatio();
                try {
                    if (strSellratio.indexOf("-") != 0) {//没有减号，表示是属于正数
                        float floatSellratio = Float.parseFloat(strSellratio);
                        if (floatSellratio == 0) {
                            map.put(R.id.community_list_split_up_rate, "text_color:" + R.color.black + ":" + "无涨幅");
                            map.put(R.id.community_list_split_rate_up_image, "visibility:" + View.GONE);
                        } else {
                            int temp = (int) (floatSellratio * 100);
                            map.put(R.id.community_list_split_up_rate, temp + "%");
                            map.put(R.id.community_list_split_rate_up_image, R.drawable.house_up);
                        }
                    } else {
                        float floatSellratio = Float.parseFloat(strSellratio.substring(strSellratio.indexOf("-") + 1));
                        int temp = (int) (floatSellratio * 100);
                        map.put(R.id.community_list_split_up_rate, temp + "%");
                        map.put(R.id.community_list_split_rate_up_image, R.drawable.house_down);
                        //如果是跌了就换下字体颜色
                        map.put(R.id.community_list_type, "text_color:" + R.color.green + ":" + jsonModel.getSellPrice());
                    }

                    if (strRentratio.indexOf("-") != 0) {
                        float floatRentratio = Float.parseFloat(strRentratio);
                        if (floatRentratio == 0) {
                            map.put(R.id.community_list_split_down_rate, "text_color:" + R.color.black + ":" + "无涨幅");
                            map.put(R.id.community_list_split_rate_down_image, "visibility:" + View.GONE);
                        } else {
                            int temp = (int) (floatRentratio * 100);
                            map.put(R.id.community_list_split_down_rate, temp + "%");
                            map.put(R.id.community_list_split_price, "text_color:" + R.color.red + ":" + jsonModel.getRentPrice());
                        }
                    } else {
                        float floatRentratio = Float.parseFloat(strRentratio.substring(strRentratio.indexOf("-") + 1));
                        int tempRentratio = (int) (floatRentratio * 100);
                        map.put(R.id.community_list_split_down_rate, tempRentratio + "%");
                        map.put(R.id.community_list_split_rate_down_image, R.drawable.house_down);
                    }

                } catch (NumberFormatException e) {
                    map.put(R.id.community_list_split_up_rate, "text_color:" + R.color.black + ":" + "无涨幅");
                    map.put(R.id.community_list_split_rate_up_image, "visibility:" + View.GONE);
                    map.put(R.id.community_list_split_down_rate, "text_color:" + R.color.black + ":" + "无涨幅");
                    map.put(R.id.community_list_split_rate_down_image, "visibility:" + View.GONE);
                }


            } else if ((jsonModel.getSellPrice() != null && !jsonModel.getSellPrice().isEmpty()) && (jsonModel.getRentPrice() == null || jsonModel.getRentPrice().isEmpty())) {
                setIsUpAndDown(View.GONE, View.GONE, View.VISIBLE, View.GONE, map);
                setCommonListData(jsonModel, map);
                //每平米的价钱
                map.put(R.id.community_list_type, jsonModel.getSellPrice());
                //每平米的涨幅
                String strSellratio = jsonModel.getSellRatio();
                try {
                    if (strSellratio.indexOf("-") != 0) {//没有减号，表示是属于正数
                        float floatSellratio = Float.parseFloat(strSellratio);
                        if (floatSellratio == 0) {
                            map.put(R.id.community_list_split_up_rate, "无涨幅");
                            map.put(R.id.community_list_split_rate_up_image, "visibility:" + View.GONE);
                            map.put(R.id.community_list_type, "text_color:" + R.color.black + ":" + jsonModel.getSellPrice());
                        } else {
                            int temp = (int) (floatSellratio * 100);
                            map.put(R.id.community_list_split_up_rate, temp + "%");
                            map.put(R.id.community_list_split_rate_up_image, R.drawable.house_up);
                        }
                    } else {
                        float floatSellratio = Float.parseFloat(strSellratio.substring(strSellratio.indexOf("-") + 1));
                        int temp = (int) (floatSellratio * 100);
                        map.put(R.id.community_list_split_up_rate, temp + "%");
                        map.put(R.id.community_list_split_rate_up_image, R.drawable.house_down);
                        //如果是跌了就换下字体颜色
                        map.put(R.id.community_list_type, "text_color:" + R.color.green + ":" + jsonModel.getSellPrice());
                    }
                } catch (NumberFormatException e) {
                    map.put(R.id.community_list_split_up_rate, "无涨幅");
                    map.put(R.id.community_list_split_rate_up_image, "visibility:" + View.GONE);
                    map.put(R.id.community_list_type, "text_color:" + R.color.black + ":" + jsonModel.getSellPrice());
                }

            } else if ((jsonModel.getSellPrice() == null || jsonModel.getSellPrice().isEmpty()) && (jsonModel.getRentPrice() != null && !jsonModel.getRentPrice().isEmpty())) {
                setIsUpAndDown(View.GONE, View.GONE, View.GONE, View.VISIBLE, map);
                setCommonListData(jsonModel, map);
                //租金
                map.put(R.id.community_list_split_price, jsonModel.getRentPrice());
                String strRentratio = jsonModel.getRentRatio();
                try {
                    if (strRentratio.indexOf("-") != 0) {
                        float floatRentratio = Float.parseFloat(strRentratio);
                        if (floatRentratio == 0) {
                            map.put(R.id.community_list_split_down_rate, "无涨幅");
                            map.put(R.id.community_list_split_rate_down_image, "visibility:" + View.GONE);
                            map.put(R.id.community_list_split_price, "text_color:" + R.color.black + ":" + jsonModel.getRentPrice());
                        } else {
                            int temp = (int) (floatRentratio * 100);
                            map.put(R.id.community_list_split_down_rate, temp + "%");
                            map.put(R.id.community_list_split_price, "text_color:" + R.color.red + ":" + jsonModel.getRentPrice());
                        }
                    } else {
                        float floatRentratio = Float.parseFloat(strRentratio.substring(strRentratio.indexOf("-") + 1));
                        int tempRentratio = (int) (floatRentratio * 100);
                        map.put(R.id.community_list_split_down_rate, tempRentratio + "%");
                        map.put(R.id.community_list_split_rate_down_image, R.drawable.house_down);
                    }
                } catch (NumberFormatException e) {
                    map.put(R.id.community_list_split_down_rate, "无涨幅");
                    map.put(R.id.community_list_split_rate_down_image, "visibility:" + View.GONE);
                    map.put(R.id.community_list_split_price, "text_color:" + R.color.black + ":" + jsonModel.getRentPrice());
                }

            } else {
                setIsUpAndDown(View.GONE, View.VISIBLE, View.GONE, View.GONE, map);
                setCommonListData(jsonModel, map);
            }
            if (resultList != null) {
                resultList.add(map);
            }
            if (itemAdapter != null) {
                itemAdapter.addItem(map);
                itemAdapter.notifyDataSetChanged();
            }
        }
    }

    private void setCommonListData(AttentionCommunityJsonModel jsonModel, Map<Integer, Object> map) {
        map.put(R.id.community_list_image, jsonModel.getTitlePic());
        map.put(R.id.community_list_name, jsonModel.getCommunityName());
        map.put(R.id.community_list_area, jsonModel.getPlace() + " " + jsonModel.getAddress());
        if (jsonModel.getSellNum() != null && !jsonModel.getSellNum().isEmpty()) {
            map.put(R.id.community_list_sell_houses, jsonModel.getSellNum());
        } else {
            map.put(R.id.community_list_sell_houses, "售0套");
        }

        if (jsonModel.getRentNum() != null && !jsonModel.getRentNum().isEmpty()) {
            map.put(R.id.community_list_rent_houses, jsonModel.getRentNum());
        } else {
            map.put(R.id.community_list_rent_houses, "租0套");
        }

    }

    private void setIsUpAndDown(int one, int two, int three, int four, Map<Integer, Object> map) {
        map.put(R.id.community_list_details, "Visibility:" + one);
        map.put(R.id.community_list_simple, "Visibility:" + two);
        map.put(R.id.community_list_details_sellratio, "Visibility:" + three);
        map.put(R.id.community_list_details_rentratio, "Visibility:" + four);
    }

    @Override
    public JsonResult<List<AttentionCommunityJsonModel>> getListDataByNetWork(Map<String, String> paramMap) {
        HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
        if (getPage() != null) {
            paramMap.put("page", String.valueOf(getPage().getPage()));
        } else {
            paramMap.put("page", "1");
        }
        Optional<JsonResult<List<AttentionCommunityJsonModel>>> optional = httpClientUtils.getUnsignedListByData(ATTENTION_COMMUNITY_RUL, paramMap, AttentionCommunityJsonModel.class);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }
}