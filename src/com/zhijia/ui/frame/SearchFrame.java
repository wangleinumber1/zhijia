package com.zhijia.ui.frame;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.google.common.base.Optional;
import com.zhijia.Global;
import com.zhijia.Enum.AreaTypeEnum;
import com.zhijia.service.data.Medol.AreaJsonModel;
import com.zhijia.service.data.Medol.ConditionJsonModel;
import com.zhijia.service.data.Medol.DecorateJsonModel;
import com.zhijia.service.data.Medol.FeatureJsonModel;
import com.zhijia.service.data.Medol.JsonResult;
import com.zhijia.service.data.Medol.NewHouseSearchHistoryModel;
import com.zhijia.service.data.Medol.NewsSearchHistoryModel;
import com.zhijia.service.data.Medol.OldHouseSearchHistoryModel;
import com.zhijia.service.data.Medol.OpentimeJsonModel;
import com.zhijia.service.data.Medol.PlaceJsonModel;
import com.zhijia.service.data.Medol.PriceJsonModel;
import com.zhijia.service.data.Medol.RentHouseSearchHistoryModel;
import com.zhijia.service.data.Medol.RoomJsonModel;
import com.zhijia.service.data.Medol.SearchHistoryBaseModel;
import com.zhijia.service.data.Medol.SearchModel;
import com.zhijia.service.data.Medol.SearchOptionItemModel;
import com.zhijia.service.data.Medol.TypeJsonModel;
import com.zhijia.service.network.HttpClientUtils;
import com.zhijia.ui.R;
import com.zhijia.ui.zhijiaActivity.NewHouseActivity;
import com.zhijia.ui.zhijiaActivity.NewsListActivity;
import com.zhijia.ui.zhijiaActivity.OldHouseActivity;
import com.zhijia.ui.zhijiaActivity.RentHouseActivity;
import com.zhijia.util.DBUtil;
import com.zhijia.util.SearchHistoryDBUtil;
import com.zhijia.util.SearchHistoryDBUtil.SearchType;

import java.util.*;

/**
 * 搜索的View
 */
public class SearchFrame extends Frame {

    private final String NEW_CONDITION_URL = Global.API_WEB_URL + "/xinfang/field";

    private final String OLD_CONDITION_URL =  Global.API_WEB_URL + "/house/field" ;

    private final String RENT_CONDITION_URL = Global.API_WEB_URL + "/house/rentfield";

    //标题Id
    private int[] titleIds = {R.id.new_house, R.id.old_house, R.id.rent_house, R.id.information};

    //点击事件的侦听
    private ClickListener clickListener = new ClickListener();

    //当前的搜索类型
    private SearchType currentSearchType = SearchType.NEW_HOUSE;

    //搜索关键字
    private EditText keywords;

    //搜索
    private Button search;

    //搜索选项列表Layerout
    private LinearLayout searchOptionLayout;

    //搜索历史列表Layerout
    private LinearLayout searchHistoryLayout;

    //hide的索引，默认是3，代表显示4个选项
    private int hideIndex = 3;

    //选项的列表
    private List<SearchOptionItemModel> optionItemModelList;

    //记录搜索日志用
    private SQLiteDatabase database;
    private DBUtil dbUtil;

    private PopupWindow popupWindow;

    private JsonResult<ConditionJsonModel> jsonResult;

    private SearchModel searchModel;

    @Override
    public View onCreateView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.tab_search, null);
    }

    @Override
	public void onCreat(){
    super.onCreate();

        dbUtil = new DBUtil(getContext());
        keywords = (EditText) findViewById(R.id.search_input);
        search = (Button) findViewById(R.id.search);
        searchOptionLayout = (LinearLayout) findViewById(R.id.search_option_layout);
        searchHistoryLayout = (LinearLayout) findViewById(R.id.search_history_layout);

        reDrawSearchItem(SearchType.NEW_HOUSE);
        initSearchHistoryList(currentSearchType);
        //注册事件
        for (int id : titleIds) {
            findViewById(id).setOnClickListener(clickListener);
        }
        findViewById(R.id.back).setOnClickListener(clickListener);
        findViewById(R.id.reset).setOnClickListener(clickListener);
        search.setOnClickListener(clickListener);
    }

    @Override
    public void onTabClick() {

    }

    /**
     * 高亮标题
     *
     * @param type
     */
    private void reHighLightMenu(SearchType type) {
        for (int id : titleIds) {
            ((TextView) findViewById(id)).setTextColor(getResources().getColor(R.color.black));
        }

        switch (type) {
            case NEW_HOUSE:
                ((TextView) findViewById(titleIds[0])).setTextColor(getResources().getColor(R.color.high_light_font));
                break;
            case OLD_HOUSE:
                ((TextView) findViewById(titleIds[1])).setTextColor(getResources().getColor(R.color.high_light_font));
                break;
            case RENT_HOUSE:
                ((TextView) findViewById(titleIds[2])).setTextColor(getResources().getColor(R.color.high_light_font));
                break;
            case INFORMATION:
                ((TextView) findViewById(titleIds[3])).setTextColor(getResources().getColor(R.color.high_light_font));
                break;
        }
    }

    /**
     * 重画搜索选项
     *
     * @param type
     */
    private void reDrawSearchItem(SearchType type) {
        searchOptionLayout.removeAllViews();
        initSearchOptionList(type);

        for (final SearchOptionItemModel searchOptionItemModel : optionItemModelList) {
            final View optionView = getActivity().getLayoutInflater().inflate(R.layout.search_option_item, searchOptionLayout, false);

            ImageView iconImageView = (ImageView) optionView.findViewById(R.id.icon);
            iconImageView.setImageDrawable(getResources().getDrawable(searchOptionItemModel.getDrawableId()));

            TextView nameView = (TextView) optionView.findViewById(R.id.name);
            nameView.setText(searchOptionItemModel.getTypeName());

            TextView valueView = (TextView) optionView.findViewById(R.id.value);
            valueView.setText(searchOptionItemModel.getOptionName());

            final AreaTypeEnum areaTypeEnum = AreaTypeEnum.getAreaType(searchOptionItemModel.getTypeValue());

            optionView.findViewById(R.id.search_option_item).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initPopuptWindow(areaTypeEnum,optionView);
                    popupWindow.showAsDropDown(keywords);
                }
            });

            searchOptionLayout.addView(optionView);
        }
    }

    /**
     * 重置
     */
    private void reset() {
        keywords.setText("");
        reDrawSearchItem(currentSearchType);
    }

    private void initSearchHistoryList(SearchType type) {
        List<SearchHistoryBaseModel> list;
        searchHistoryLayout.removeAllViews();

        switch (type) {
            case NEW_HOUSE:
                list = SearchHistoryDBUtil.getSearchHistory(SearchType.NEW_HOUSE, 5);

                if (list.size() > 0) {
                    for (SearchHistoryBaseModel tempObj : list) {
                        final NewHouseSearchHistoryModel obj = (NewHouseSearchHistoryModel) tempObj;
                        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.tab_search_history_item, null);

                        TextView view = (TextView) linearLayout.findViewById(R.id.history_name);
                        view.setText(obj.toShortString());
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                searchModel = resumeSearchHistory(SearchType.NEW_HOUSE, obj);
                            }
                        });
                        searchHistoryLayout.addView(linearLayout);
                    }
                } else {
                    TextView noDataView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.tab_search_history_item_nodata, null);
                    searchHistoryLayout.addView(noDataView);
                }
                break;
            case OLD_HOUSE:
                list = SearchHistoryDBUtil.getSearchHistory(SearchType.OLD_HOUSE, 5);

                if (list.size() > 0) {
                    for (SearchHistoryBaseModel tempObj : list) {
                        final OldHouseSearchHistoryModel obj = (OldHouseSearchHistoryModel) tempObj;
                        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.tab_search_history_item, null);

                        TextView view = (TextView) linearLayout.findViewById(R.id.history_name);
                        view.setText(obj.toShortString());
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                searchModel = resumeSearchHistory(SearchType.OLD_HOUSE, obj);
                            }
                        });
                        searchHistoryLayout.addView(linearLayout);
                    }
                } else {
                    TextView noDataView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.tab_search_history_item_nodata, null);
                    searchHistoryLayout.addView(noDataView);
                }

                break;
            case RENT_HOUSE:
                list = SearchHistoryDBUtil.getSearchHistory(SearchType.RENT_HOUSE, 5);

                if (list.size() > 0) {
                    for (SearchHistoryBaseModel tempObj : list) {
                        final RentHouseSearchHistoryModel obj = (RentHouseSearchHistoryModel) tempObj;
                        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.tab_search_history_item, null);

                        TextView view = (TextView) linearLayout.findViewById(R.id.history_name);
                        view.setText(obj.toShortString());
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                searchModel = resumeSearchHistory(SearchType.RENT_HOUSE, obj);
                            }
                        });
                        searchHistoryLayout.addView(linearLayout);
                    }
                } else {
                    TextView noDataView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.tab_search_history_item_nodata, null);
                    searchHistoryLayout.addView(noDataView);
                }

                break;
            case INFORMATION:
                list = SearchHistoryDBUtil.getSearchHistory(SearchType.INFORMATION, 5);

                if (list.size() > 0) {
                    for (SearchHistoryBaseModel tempObj : list) {
                        final NewsSearchHistoryModel obj = (NewsSearchHistoryModel) tempObj;
                        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.tab_search_history_item, null);

                        TextView view = (TextView) linearLayout.findViewById(R.id.history_name);
                        view.setText(obj.toShortString());
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                keywords.setText(obj.getKeyword());
                            }
                        });
                        searchHistoryLayout.addView(linearLayout);
                    }
                } else {
                    TextView noDataView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.tab_search_history_item_nodata, null);
                    searchHistoryLayout.addView(noDataView);
                }

                break;
        }
    }

    /**
     * 初始化选项
     *
     * @param type 搜索类型
     */
    private void initSearchOptionList(SearchType type) {
        optionItemModelList = new ArrayList<SearchOptionItemModel>();
        //初始化自己的条件模型
        searchModel = new SearchModel();
        setJsonResult(null);
        switch (type) {
            case NEW_HOUSE:
                optionItemModelList.add(new SearchOptionItemModel(R.drawable.house_area, getString(R.string.area), "area", getString(R.string.all), "-1"));
                optionItemModelList.add(new SearchOptionItemModel(R.drawable.house_price, getString(R.string.price), "price", getString(R.string.all), "-1"));
                optionItemModelList.add(new SearchOptionItemModel(R.drawable.house_type, getString(R.string.type), "type", getString(R.string.all), "-1"));
                optionItemModelList.add(new SearchOptionItemModel(R.drawable.house_feature, getString(R.string.feature), "feature", getString(R.string.all), "-1"));
                optionItemModelList.add(new SearchOptionItemModel(R.drawable.house_decoration, getString(R.string.decoration), "decorate", getString(R.string.all), "-1"));
                optionItemModelList.add(new SearchOptionItemModel(R.drawable.house_open_time, getString(R.string.open_time), "opentime", getString(R.string.all), "-1"));
                //初始化类型的时候 装载好数据
                startConditionAsyncTask(NEW_CONDITION_URL);
                break;
            case OLD_HOUSE://默认
                optionItemModelList.add(new SearchOptionItemModel(R.drawable.house_area, getString(R.string.area), "area", getString(R.string.all), "-1"));
                optionItemModelList.add(new SearchOptionItemModel(R.drawable.house_total_price, getString(R.string.total_price), "price", getString(R.string.all), "-1"));
                optionItemModelList.add(new SearchOptionItemModel(R.drawable.house_proportion, getString(R.string.proportion), "proportion", getString(R.string.all), "-1"));
                optionItemModelList.add(new SearchOptionItemModel(R.drawable.house_room_type, getString(R.string.room_type), "room", getString(R.string.all), "-1"));
                optionItemModelList.add(new SearchOptionItemModel(R.drawable.house_source, getString(R.string.source), "source", getString(R.string.all), "-1"));
                optionItemModelList.add(new SearchOptionItemModel(R.drawable.house_decoration, getString(R.string.decoration), "decorate", getString(R.string.all), "-1"));
                startConditionAsyncTask(OLD_CONDITION_URL);
                break;
            case RENT_HOUSE://默认
                optionItemModelList.add(new SearchOptionItemModel(R.drawable.house_area, getString(R.string.area), "area", getString(R.string.all), "-1"));
                optionItemModelList.add(new SearchOptionItemModel(R.drawable.house_rental, getString(R.string.rental), "price", getString(R.string.all), "-1"));
                optionItemModelList.add(new SearchOptionItemModel(R.drawable.house_proportion, getString(R.string.proportion), "proportion", getString(R.string.all), "-1"));
                optionItemModelList.add(new SearchOptionItemModel(R.drawable.house_room_type, getString(R.string.room_type), "room", getString(R.string.all), "-1"));
                optionItemModelList.add(new SearchOptionItemModel(R.drawable.house_source, getString(R.string.source), "source", getString(R.string.all), "-1"));
                optionItemModelList.add(new SearchOptionItemModel(R.drawable.house_rent_method, getString(R.string.rent_method), "rent_type", getString(R.string.all), "-1"));
                optionItemModelList.add(new SearchOptionItemModel(R.drawable.house_decoration, getString(R.string.decoration), "decorate", getString(R.string.all), "-1"));
                startConditionAsyncTask(RENT_CONDITION_URL);
                break;
            case INFORMATION:
                //Do nothing
                break;
        }
    }


    /**
     * 通过历史搜索记录模型恢复UI及搜索条件模型
     *
     * @param type             搜索类型
     * @param historyBaseModel 搜索历史记录模型
     * @return
     */
    private SearchModel resumeSearchHistory(SearchType type, SearchHistoryBaseModel historyBaseModel) {
        reset();
        SearchModel returnObj = new SearchModel();

        switch (type) {
            case NEW_HOUSE:
                NewHouseSearchHistoryModel newHouseReturnObj = (NewHouseSearchHistoryModel) historyBaseModel;

                if (!newHouseReturnObj.getKeyword().equalsIgnoreCase("")) {
                    returnObj.setKeyword(newHouseReturnObj.getKeyword());
                    keywords.setText(newHouseReturnObj.getKeyword());
                }

                if (!newHouseReturnObj.getAreaId().equalsIgnoreCase("")) {
                    returnObj.setAreaid(newHouseReturnObj.getAreaId());
                    ((TextView) searchOptionLayout.getChildAt(0).findViewById(R.id.value)).setText(newHouseReturnObj.getArea());
                }

                if (!newHouseReturnObj.getCircleid().equalsIgnoreCase("")) {
                    returnObj.setCircleid(newHouseReturnObj.getCircleid());
                    ((TextView) searchOptionLayout.getChildAt(0).findViewById(R.id.value)).setText(newHouseReturnObj.getArea());
                }

                if (!newHouseReturnObj.getPriceId().equalsIgnoreCase("")) {
                    returnObj.setPrice(newHouseReturnObj.getPriceId());
                    ((TextView) searchOptionLayout.getChildAt(1).findViewById(R.id.value)).setText(newHouseReturnObj.getPrice());
                }

                if (!newHouseReturnObj.getTypeId().equalsIgnoreCase("")) {
                    ((TextView) searchOptionLayout.getChildAt(2).findViewById(R.id.value)).setText(newHouseReturnObj.getType());
                    returnObj.setProjecttype(newHouseReturnObj.getTypeId());
                }

                if (!newHouseReturnObj.getFeatureId().equalsIgnoreCase("")) {
                    ((TextView) searchOptionLayout.getChildAt(3).findViewById(R.id.value)).setText(newHouseReturnObj.getFeature());
                    returnObj.setFeature(newHouseReturnObj.getFeatureId());
                }

                if (!newHouseReturnObj.getDecorationId().equalsIgnoreCase("")) {
                    ((TextView) searchOptionLayout.getChildAt(4).findViewById(R.id.value)).setText(newHouseReturnObj.getDecoration());
                    returnObj.setDecorate(newHouseReturnObj.getDecorationId());
                }

                if (!newHouseReturnObj.getOpenTimeId().equalsIgnoreCase("")) {
                    ((TextView) searchOptionLayout.getChildAt(5).findViewById(R.id.value)).setText(newHouseReturnObj.getOpenTime());
                    returnObj.setOpentime(newHouseReturnObj.getOpenTimeId());
                }

                break;
            case OLD_HOUSE:
                OldHouseSearchHistoryModel oldHouseReturnObj = (OldHouseSearchHistoryModel) historyBaseModel;

                if (!oldHouseReturnObj.getKeyword().equalsIgnoreCase("")) {
                    returnObj.setKeyword(oldHouseReturnObj.getKeyword());
                    keywords.setText(oldHouseReturnObj.getKeyword());
                }

                if (!oldHouseReturnObj.getAreaId().equalsIgnoreCase("")) {
                    ((TextView) searchOptionLayout.getChildAt(0).findViewById(R.id.value)).setText(oldHouseReturnObj.getArea());
                    returnObj.setAreaid(oldHouseReturnObj.getAreaId());
                }

                if (!oldHouseReturnObj.getCircleid().equalsIgnoreCase("")) {
                    returnObj.setCircleid(oldHouseReturnObj.getCircleid());
                    ((TextView) searchOptionLayout.getChildAt(0).findViewById(R.id.value)).setText(oldHouseReturnObj.getArea());
                }

                if (!oldHouseReturnObj.getTotalPriceId().equalsIgnoreCase("")) {
                    ((TextView) searchOptionLayout.getChildAt(1).findViewById(R.id.value)).setText(oldHouseReturnObj.getTotalPrice());
                    returnObj.setPrice(oldHouseReturnObj.getTotalPriceId());
                }

                if (!oldHouseReturnObj.getProportionId().equalsIgnoreCase("")) {
                    ((TextView) searchOptionLayout.getChildAt(2).findViewById(R.id.value)).setText(oldHouseReturnObj.getProportion());
                    returnObj.setProportion(oldHouseReturnObj.getProportionId());
                }

                if (!oldHouseReturnObj.getRoomTypeId().equalsIgnoreCase("")) {
                    ((TextView) searchOptionLayout.getChildAt(3).findViewById(R.id.value)).setText(oldHouseReturnObj.getRoomType());
                    returnObj.setRoom(oldHouseReturnObj.getRoomTypeId());
                }

                if (!oldHouseReturnObj.getSourceId().equalsIgnoreCase("")) {
                    ((TextView) searchOptionLayout.getChildAt(4).findViewById(R.id.value)).setText(oldHouseReturnObj.getSource());
                    returnObj.setSource(oldHouseReturnObj.getSourceId());
                }

                if (!oldHouseReturnObj.getDecorationId().equalsIgnoreCase("")) {
                    ((TextView) searchOptionLayout.getChildAt(5).findViewById(R.id.value)).setText(oldHouseReturnObj.getDecoration());
                    returnObj.setDecorate(oldHouseReturnObj.getDecorationId());
                }

                break;
            case RENT_HOUSE:
                RentHouseSearchHistoryModel rentHouseReturnObj = (RentHouseSearchHistoryModel) historyBaseModel;

                if (!rentHouseReturnObj.getKeyword().equalsIgnoreCase("")) {
                    returnObj.setKeyword(rentHouseReturnObj.getKeyword());
                    keywords.setText(rentHouseReturnObj.getKeyword());
                }

                if (!rentHouseReturnObj.getAreaId().equalsIgnoreCase("")) {
                    ((TextView) searchOptionLayout.getChildAt(0).findViewById(R.id.value)).setText(rentHouseReturnObj.getArea());
                    returnObj.setAreaid(rentHouseReturnObj.getAreaId());
                }

                if (!rentHouseReturnObj.getCircleid().equalsIgnoreCase("")) {
                    returnObj.setCircleid(rentHouseReturnObj.getCircleid());
                    ((TextView) searchOptionLayout.getChildAt(0).findViewById(R.id.value)).setText(rentHouseReturnObj.getArea());
                }

                if (!rentHouseReturnObj.getRentalId().equalsIgnoreCase("")) {
                    ((TextView) searchOptionLayout.getChildAt(1).findViewById(R.id.value)).setText(rentHouseReturnObj.getRental());
                    returnObj.setPrice(rentHouseReturnObj.getRentalId());
                }

                if (!rentHouseReturnObj.getProportionId().equalsIgnoreCase("")) {
                    ((TextView) searchOptionLayout.getChildAt(2).findViewById(R.id.value)).setText(rentHouseReturnObj.getProportion());
                    returnObj.setProportion(rentHouseReturnObj.getProportionId());
                }

                if (!rentHouseReturnObj.getRoomTypeId().equalsIgnoreCase("")) {
                    ((TextView) searchOptionLayout.getChildAt(3).findViewById(R.id.value)).setText(rentHouseReturnObj.getRoomType());
                    returnObj.setRoom(rentHouseReturnObj.getRoomTypeId());
                }

                if (!rentHouseReturnObj.getSourceId().equalsIgnoreCase("")) {
                    ((TextView) searchOptionLayout.getChildAt(4).findViewById(R.id.value)).setText(rentHouseReturnObj.getSource());
                    returnObj.setSource(rentHouseReturnObj.getSourceId());
                }

                if (!rentHouseReturnObj.getRentMethodId().equalsIgnoreCase("")) {
                    ((TextView) searchOptionLayout.getChildAt(5).findViewById(R.id.value)).setText(rentHouseReturnObj.getRentMethod());
                    returnObj.setRentType(rentHouseReturnObj.getRentMethodId());
                }

                if (!rentHouseReturnObj.getDecorationId().equalsIgnoreCase("")) {
                    ((TextView) searchOptionLayout.getChildAt(6).findViewById(R.id.value)).setText(rentHouseReturnObj.getDecoration());
                    returnObj.setDecorate(rentHouseReturnObj.getDecorationId());
                }

                break;
        }

        Log.d(Global.LOG_TAG, returnObj.toString());

        return returnObj;
    }

    /**
     * 将搜索模型转换成搜索历史记录模型
     *
     * @param type        搜索类型
     * @param searchModel 搜索历史记录模型
     * @return
     */
    private SearchHistoryBaseModel searchModel2HistoryModel(SearchType type, SearchModel searchModel) {
        SearchHistoryBaseModel returnObj = null;

        switch (type) {
            case NEW_HOUSE:
                NewHouseSearchHistoryModel newHouseReturnObj = new NewHouseSearchHistoryModel();
                returnObj = newHouseReturnObj;

                if (searchModel.getKeyword() != null) {
                    newHouseReturnObj.setKeyword(searchModel.getKeyword());
                }

                if (searchModel.getAreaid() != null) {
                    newHouseReturnObj.setAreaId(searchModel.getAreaid());
                    newHouseReturnObj.setArea(((TextView) searchOptionLayout.getChildAt(0).findViewById(R.id.value)).getText().toString());
                }

                if (searchModel.getCircleid() != null) {
                    newHouseReturnObj.setCircleid(searchModel.getCircleid());
                    newHouseReturnObj.setArea(((TextView) searchOptionLayout.getChildAt(0).findViewById(R.id.value)).getText().toString());
                }

                if (searchModel.getPrice() != null) {
                    newHouseReturnObj.setPriceId(searchModel.getPrice());
                    newHouseReturnObj.setPrice(((TextView) searchOptionLayout.getChildAt(1).findViewById(R.id.value)).getText().toString());

                }

                if (searchModel.getProjecttype() != null) {
                    newHouseReturnObj.setType(((TextView) searchOptionLayout.getChildAt(2).findViewById(R.id.value)).getText().toString());
                    newHouseReturnObj.setTypeId(searchModel.getProjecttype());
                }

                if (searchModel.getFeature() != null) {
                    newHouseReturnObj.setFeature(((TextView) searchOptionLayout.getChildAt(3).findViewById(R.id.value)).getText().toString());
                    newHouseReturnObj.setFeatureId(searchModel.getFeature());
                }

                if (searchModel.getDecorate() != null) {
                    newHouseReturnObj.setDecoration(((TextView) searchOptionLayout.getChildAt(4).findViewById(R.id.value)).getText().toString());
                    newHouseReturnObj.setDecorationId(searchModel.getDecorate());
                }

                if (searchModel.getOpentime() != null) {
                    newHouseReturnObj.setOpenTime(((TextView) searchOptionLayout.getChildAt(5).findViewById(R.id.value)).getText().toString());
                    newHouseReturnObj.setOpenTimeId(searchModel.getOpentime());
                }

                break;
            case OLD_HOUSE:
                OldHouseSearchHistoryModel oldHouseReturnObj = new OldHouseSearchHistoryModel();
                returnObj = oldHouseReturnObj;

                if (searchModel.getKeyword() != null) {
                    oldHouseReturnObj.setKeyword(searchModel.getKeyword());
                }

                if (searchModel.getAreaid() != null) {
                    oldHouseReturnObj.setArea(((TextView) searchOptionLayout.getChildAt(0).findViewById(R.id.value)).getText().toString());
                    oldHouseReturnObj.setAreaId(searchModel.getAreaid());
                }

                if (searchModel.getCircleid() != null) {
                    oldHouseReturnObj.setCircleid(searchModel.getCircleid());
                    oldHouseReturnObj.setArea(((TextView) searchOptionLayout.getChildAt(0).findViewById(R.id.value)).getText().toString());
                }

                if (searchModel.getPrice() != null) {
                    oldHouseReturnObj.setTotalPrice(((TextView) searchOptionLayout.getChildAt(1).findViewById(R.id.value)).getText().toString());
                    oldHouseReturnObj.setTotalPriceId(searchModel.getPrice());
                }

                if (searchModel.getProportion() != null) {
                    oldHouseReturnObj.setProportion(((TextView) searchOptionLayout.getChildAt(2).findViewById(R.id.value)).getText().toString());
                    oldHouseReturnObj.setProportionId(searchModel.getProportion());
                }

                if (searchModel.getRoom() != null) {
                    oldHouseReturnObj.setRoomType(((TextView) searchOptionLayout.getChildAt(3).findViewById(R.id.value)).getText().toString());
                    oldHouseReturnObj.setRoomTypeId(searchModel.getRoom());
                }

                if (searchModel.getSource() != null) {
                    oldHouseReturnObj.setSource(((TextView) searchOptionLayout.getChildAt(4).findViewById(R.id.value)).getText().toString());
                    oldHouseReturnObj.setSourceId(searchModel.getSource());
                }

                if (searchModel.getDecorate() != null) {
                    oldHouseReturnObj.setDecoration(((TextView) searchOptionLayout.getChildAt(5).findViewById(R.id.value)).getText().toString());
                    oldHouseReturnObj.setDecorationId(searchModel.getDecorate());
                }

                break;
            case RENT_HOUSE:
                RentHouseSearchHistoryModel rentHouseReturnObj = new RentHouseSearchHistoryModel();
                returnObj = rentHouseReturnObj;

                if (searchModel.getKeyword() != null) {
                    rentHouseReturnObj.setKeyword(searchModel.getKeyword());
                }

                if (searchModel.getAreaid() != null) {
                    rentHouseReturnObj.setArea(((TextView) searchOptionLayout.getChildAt(0).findViewById(R.id.value)).getText().toString());
                    rentHouseReturnObj.setAreaId(searchModel.getAreaid());
                }

                if (searchModel.getCircleid() != null) {
                    rentHouseReturnObj.setCircleid(searchModel.getCircleid());
                    rentHouseReturnObj.setArea(((TextView) searchOptionLayout.getChildAt(0).findViewById(R.id.value)).getText().toString());
                }

                if (searchModel.getPrice() != null) {
                    rentHouseReturnObj.setRental(((TextView) searchOptionLayout.getChildAt(1).findViewById(R.id.value)).getText().toString());
                    rentHouseReturnObj.setRentalId(searchModel.getPrice());
                }

                if (searchModel.getProportion() != null) {
                    rentHouseReturnObj.setProportion(((TextView) searchOptionLayout.getChildAt(2).findViewById(R.id.value)).getText().toString());
                    rentHouseReturnObj.setProportionId(searchModel.getProportion());
                }

                if (searchModel.getRoom() != null) {
                    rentHouseReturnObj.setRoomType(((TextView) searchOptionLayout.getChildAt(3).findViewById(R.id.value)).getText().toString());
                    rentHouseReturnObj.setRoomTypeId(searchModel.getRoom());
                }

                if (searchModel.getSource() != null) {
                    rentHouseReturnObj.setSource(((TextView) searchOptionLayout.getChildAt(4).findViewById(R.id.value)).getText().toString());
                    rentHouseReturnObj.setSourceId(searchModel.getSource());
                }

                if (searchModel.getRentType() != null) {
                    rentHouseReturnObj.setRentMethod(((TextView) searchOptionLayout.getChildAt(5).findViewById(R.id.value)).getText().toString());
                    rentHouseReturnObj.setRentMethodId(searchModel.getRentType());
                }

                if (searchModel.getDecorate() != null) {
                    rentHouseReturnObj.setDecoration(((TextView) searchOptionLayout.getChildAt(6).findViewById(R.id.value)).getText().toString());
                    rentHouseReturnObj.setDecorationId(searchModel.getDecorate());
                }

                break;
        }

        Log.d(Global.LOG_TAG, returnObj.toString());

        return returnObj;
    }

    /**
     * 传递参数给对应的列表页面
     */
    private void gotoSearchResultListView() {
        switch (currentSearchType) {
            case NEW_HOUSE:
                if (!keywords.getText().toString().trim().equalsIgnoreCase("")) {
                    searchModel.setKeyword(keywords.getText().toString().trim());
                }
                Intent newHouseIntent = new Intent(getActivity(), NewHouseActivity.class);
                newHouseIntent.putExtra("searchModel", searchModel);
                startActivity(newHouseIntent);

                NewHouseSearchHistoryModel newHouseSearchHistoryModel = (NewHouseSearchHistoryModel) searchModel2HistoryModel(currentSearchType, searchModel);
                SearchHistoryDBUtil.addSearchHistory(newHouseSearchHistoryModel);

                break;
            case OLD_HOUSE:
                if (!keywords.getText().toString().trim().equalsIgnoreCase("")) {
                    searchModel.setKeyword(keywords.getText().toString().trim());
                }
                Intent oldHouseIntent = new Intent(getActivity(), OldHouseActivity.class);
                oldHouseIntent.putExtra("searchModel", searchModel);
                startActivity(oldHouseIntent);

                OldHouseSearchHistoryModel oldHouseSearchHistoryModel = (OldHouseSearchHistoryModel) searchModel2HistoryModel(currentSearchType, searchModel);
                SearchHistoryDBUtil.addSearchHistory(oldHouseSearchHistoryModel);

                break;
            case RENT_HOUSE:
                if (!keywords.getText().toString().trim().equalsIgnoreCase("")) {
                    searchModel.setKeyword(keywords.getText().toString().trim());
                }
                Intent rentHouseIntent = new Intent(getActivity(), RentHouseActivity.class);
                rentHouseIntent.putExtra("searchModel", searchModel);
                startActivity(rentHouseIntent);

                RentHouseSearchHistoryModel rentHouseSearchHistoryModel = (RentHouseSearchHistoryModel) searchModel2HistoryModel(currentSearchType, searchModel);
                SearchHistoryDBUtil.addSearchHistory(rentHouseSearchHistoryModel);

                break;
            case INFORMATION:
                if (!keywords.getText().toString().trim().equalsIgnoreCase("")) {
                    Intent newsIntent = new Intent(getActivity(), NewsListActivity.class);
                    newsIntent.putExtra("keyword", keywords.getText().toString().trim());
                    startActivity(newsIntent);

                    NewsSearchHistoryModel obj = new NewsSearchHistoryModel();
                    obj.setKeyword(keywords.getText().toString().trim());

                    SearchHistoryDBUtil.addSearchHistory(obj);
                } else {
                    Toast.makeText(getContext(), "关键字不能为空", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    public Map<String, List<String>> getAreaData(AreaTypeEnum areaTypeEnum) {
        if (getJsonResult() != null && getJsonResult().getData() != null && getJsonResult().isStatus()) {
            return getAreaDataByNetWork(getJsonResult().getData(), areaTypeEnum);
        } else {
            return null;
        }
    }

    private Map<String, List<String>> getAreaDataByNetWork(ConditionJsonModel conditionJsonModel, AreaTypeEnum areaTypeEnum) {
        Map<String, List<String>> resultMap = new LinkedHashMap<String, List<String>>();
        switch (areaTypeEnum) {
            case AREA:
                setAreaByNetWork(resultMap, conditionJsonModel);
                break;
            case PRICE:
                setPriceByNetWork(resultMap, conditionJsonModel);
                break;
            case TYPE:
                setTypeByNetWork(resultMap, conditionJsonModel) ;
                break;
            case FEATURE:
                setFeatureByNetWork(resultMap,conditionJsonModel) ;
                break;
            case DECORATE:
                setDecorate(resultMap,conditionJsonModel) ;
                break;
            case OPENTIME:
                setOpenTime(resultMap,conditionJsonModel) ;
                break;
            case PROPORTION:
                setProportion(resultMap,conditionJsonModel) ;
                break;
            case ROOM:
                setRoom(resultMap,conditionJsonModel) ;
                break;
            case SOURCE:
                setSource(resultMap,conditionJsonModel) ;
                break;
            case RENT_YPE:
                setRentType(resultMap,conditionJsonModel) ;
                break;
        }
        return resultMap;
    }

    private SearchModel setSearchModelByMap(Map<String, Object> map){
        String areaType = (String) map.get("areaType");
        String id = (String) map.get("id");
        AreaTypeEnum areaTypeEnum = AreaTypeEnum.getAreaType(areaType);
        switch (areaTypeEnum) {
            case AREA:
                searchModel.setAreaid(id);
                break;
            case CIRCLE:
                searchModel.setCircleid(id);
                break;
            case PRICE:
                searchModel.setPrice(id);
                break;
            case TYPE:
                searchModel.setProjecttype(id);
                break;
            case FEATURE:
                searchModel.setFeature(id);
                break;
            case DECORATE:
                searchModel.setDecorate(id);
                break;
            case CIRCLELINE:
                searchModel.setCircleline(id);
                break;
            case ORDER:
                searchModel.setOrder(id);
                break;
            case PROPORTION:
                searchModel.setProportion(id);
                break;
            case ROOM:
                searchModel.setRoom(id);
                break;
            case ASPECT:
                searchModel.setAspect(id);
                break;
            case HOUSEAGE:
                searchModel.setHouseage(id);
                break;
            case FLOOR:
                searchModel.setFloor(id);
                break;
            case SOURCE:
                searchModel.setSource(id);
                break;
            case TAG:
                searchModel.setTag(id);
                break;
            case RENT_YPE:
                searchModel.setRentType(id);
                break;
            case PROJECT_TYPE:
                searchModel.setRentProjectType(id);
                break;
            case AVERAGE_PRICE:
                searchModel.setSellprice(id);
                break;
            case SELL_PRICE:
                searchModel.setSellprice(id);
                break;
            case SELLPRICE :
                searchModel.setSellprice(id);
                break;
            case RENT_TYPE:
                searchModel.setType(id);
                break;
            case RENTPRICE:
                searchModel.setRentprice(id);
                break;
            case OPENTIME:
                searchModel.setOpentime(id);
                break;
        }

        return searchModel;
    }

    private void setAreaByNetWork(Map<String, List<String>> resultMap, ConditionJsonModel conditionJsonModel) {
        //如果是0就全部搜索
        //resultMap.put("全部区域:0:area", null);
        for (PlaceJsonModel placeJsonModel : conditionJsonModel.getPlace()) {
            if (placeJsonModel.getChild() != null) {
                List<String> subList = new ArrayList<String>();
                for (PlaceJsonModel tempPlaceJsonModel : placeJsonModel.getChild()) {
                    subList.add(tempPlaceJsonModel.getName() + ":" + tempPlaceJsonModel.getPid() + ":circle");
                }
                resultMap.put(placeJsonModel.getName() + ":" + placeJsonModel.getPid() + ":area", subList);
            } else {//如果为空表示没有子级。
                resultMap.put(placeJsonModel.getName() + ":" + placeJsonModel.getPid() + ":area", null);
            }

        }
    }

    private void setPriceByNetWork(Map<String, List<String>> resultMap, ConditionJsonModel conditionJsonModel) {
        for (Map.Entry<String, PriceJsonModel> priceJsonModel : conditionJsonModel.getPrice().entrySet()) {
            resultMap.put(priceJsonModel.getValue().getName() + ":" + priceJsonModel.getValue().getFid() + ":price", null);
        }
    }

    private void setTypeByNetWork(Map<String, List<String>> resultMap, ConditionJsonModel conditionJsonModel){
        for (Map.Entry<String, TypeJsonModel> typeJsonModel : conditionJsonModel.getType().entrySet()){
            resultMap.put(typeJsonModel.getValue().getName() + ":" + typeJsonModel.getValue().getFid() + ":type", null);
        }
    }

    private void setFeatureByNetWork(Map<String, List<String>> resultMap, ConditionJsonModel conditionJsonModel){
        for (Map.Entry<String, FeatureJsonModel> featureJsonModel : conditionJsonModel.getFeature().entrySet()) {
            resultMap.put(featureJsonModel.getValue().getName() + ":" + featureJsonModel.getValue().getFid() + ":feature", null);
        }
    }

    private void setDecorate(Map<String, List<String>> resultMap, ConditionJsonModel conditionJsonModel){
        for (Map.Entry<String, DecorateJsonModel> decorateJsonModel : conditionJsonModel.getDecorate().entrySet()) {
            resultMap.put(decorateJsonModel.getValue().getName() + ":" + decorateJsonModel.getValue().getFid() + ":decorate", null);
        }
    }

    private void setOpenTime(Map<String, List<String>> resultMap, ConditionJsonModel conditionJsonModel){
        for (Map.Entry<String, OpentimeJsonModel> opentimeJsonModel : conditionJsonModel.getOpentime().entrySet()) {
            resultMap.put(opentimeJsonModel.getValue().getName() + ":" + opentimeJsonModel.getValue().getFid() + ":opentime", null);
        }
    }

    private void setProportion(Map<String, List<String>> resultMap, ConditionJsonModel conditionJsonModel){
        for (Map.Entry<String, AreaJsonModel> areaJsonModel : conditionJsonModel.getArea().entrySet()) {
            resultMap.put(areaJsonModel.getValue().getName() + ":" + areaJsonModel.getValue().getFid() + ":proportion", null);
        }
    }

    private void setRoom(Map<String, List<String>> resultMap, ConditionJsonModel conditionJsonModel){
        for (Map.Entry<String, RoomJsonModel> roomJsonModel : conditionJsonModel.getRoom().entrySet()) {
            resultMap.put(roomJsonModel.getValue().getName() + ":" + roomJsonModel.getValue().getFid() + ":room", null);
        }
    }

    private void setSource(Map<String, List<String>> resultMap, ConditionJsonModel conditionJsonModel){
        for (Map.Entry<String, String> sourceJsonModel : conditionJsonModel.getSource().entrySet()) {
            resultMap.put(sourceJsonModel.getValue() + ":" + sourceJsonModel.getKey() + ":source", null);
        }
    }

    private void setRentType(Map<String, List<String>> resultMap, ConditionJsonModel conditionJsonModel){
        for (Map.Entry<String, TypeJsonModel> typeJsonModel : conditionJsonModel.getType().entrySet()) {
            resultMap.put(typeJsonModel.getValue().getName() + ":" + typeJsonModel.getValue().getFid() + ":rent_type", null);
        }
    }

    /**
     * 创建PopupWindow
     */
    protected void initPopuptWindow(AreaTypeEnum areaTypeEnum,final View optionView) {
        // 获取自定义布局文件pop.xml的视图
        View popupWindow_view = getActivity().getLayoutInflater().inflate(R.layout.common_popup, null, false);
        // 创建PopupWindow实例,200,150分别是宽度和高度
        popupWindow = new PopupWindow(popupWindow_view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        //点击其他地方消失
        popupWindow_view.setOnTouchListener(new HouseOnTouchListener());
        ListView listView = (ListView) popupWindow_view.findViewById(R.id.house_listView_popup);
        final ListView subListView = (ListView) popupWindow_view.findViewById(R.id.house_listView_subpopup);
        final String[] key = {"name"};
        final int[] to = {R.id.adapter_house_choose_area};

        final List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();

        final Map<String, List<String>> resultMap = getAreaData(areaTypeEnum);

        if (resultMap == null) {
            Toast.makeText(getContext(), Global.ERROR_NET_WORK, Toast.LENGTH_SHORT).show();
            return;
        }
        for (Map.Entry<String,List<String>> temp :resultMap.entrySet()) {
            Map<String, Object> map = new HashMap<String, Object>();
            String[] tempStrs = temp.getKey().split(":");
            map.put("name", tempStrs[0]);
            map.put("id", tempStrs[1]);
            map.put("areaType", tempStrs[2]);
            listItems.add(map);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), listItems, R.layout.common_popup_adapter_item, key, to);
        listView.setAdapter(simpleAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> tempMap = listItems.get(position);
                final String parentId = (String) tempMap.get("id");
                String name = (String)tempMap.get("name") ;
                //设置条件
                searchModel = setSearchModelByMap(tempMap);

                if (resultMap.values() != null && resultMap.values().size() >0){
                    final List<Map<String, Object>> subListItems = new ArrayList<Map<String, Object>>();
                    TextView textView = (TextView) view.findViewById(R.id.adapter_house_choose_area);
                    String parentKey = textView.getText().toString() + ":" + parentId + ":area";
                    List<String> subList = resultMap.get(parentKey);
                    if (subList != null && subList.size() >0) {
                        SimpleAdapter simpleAdapter1 = new SimpleAdapter(getActivity(), subListItems, R.layout.common_popup_adapter_item, key, to);
                        subListView.setAdapter(simpleAdapter1);
                        for (String subStr : subList) {
                            String[] tempSubStr = subStr.split(":");
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("name", tempSubStr[0]);
                            map.put("id", tempSubStr[1]);
                            map.put("areaType", tempSubStr[2]);
                            subListItems.add(map);
                        }
                        simpleAdapter1.notifyDataSetChanged();
                        subListView.setVisibility(View.VISIBLE);
                        //当触发字集的时候进行搜索
                        subListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                destroyPop();
                                Map<String,Object> subMap = subListItems.get(position) ;
                                searchModel = setSearchModelByMap(subMap) ;
                                String subName = (String)subMap.get("name") ;
                                TextView valueView = (TextView) optionView.findViewById(R.id.value);
                                valueView.setText(subName);
                                //Toast.makeText(getActivity(), "parentId=" + parentId + "& subId=" + subListItems.get(position).get("id"), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Log.d("initPopuptWindow","destroy");
                        TextView valueView = (TextView) optionView.findViewById(R.id.value);
                        valueView.setText(name);
                        subListView.setVisibility(View.GONE);
                        destroyPop();
                    }
                }else{
                    Log.d("initPopuptWindow","destroy");
                    destroyPop();
                }
            }
        });
    }

    public void destroyPop() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }

    public JsonResult<ConditionJsonModel> getJsonResult() {
        return jsonResult;
    }

    public void setJsonResult(JsonResult<ConditionJsonModel> jsonResult) {
        this.jsonResult = jsonResult;
    }

    /**
     * 启动条件线程
     */
    public void startConditionAsyncTask(String url) {
        ConditionAsyncTask conditionAsyncTask = new ConditionAsyncTask();
        conditionAsyncTask.execute(url);
    }

    //获取条件数据
    public JsonResult<ConditionJsonModel> getConditionData(String url) {
        HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
        Map<String, String> map = new HashMap<String, String>();
        map.put("cityid", Global.NOW_CITY_ID);
        Optional<JsonResult<ConditionJsonModel>> optional = httpClientUtils.getUnsignedByData(url, map, ConditionJsonModel.class);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    //点击事件的公共类
    class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back: //后退
                    Global.BOTTOM_TAB.setCurrentTab(0);
                    break;
                case R.id.new_house: //新房
                    currentSearchType = SearchHistoryDBUtil.SearchType.NEW_HOUSE;
                    reHighLightMenu(currentSearchType);
                    reDrawSearchItem(currentSearchType);
                    initSearchHistoryList(currentSearchType);
                    keywords.setText("");

                    break;
                case R.id.old_house: //二手房
                    currentSearchType = SearchType.OLD_HOUSE;
                    reHighLightMenu(currentSearchType);
                    reDrawSearchItem(currentSearchType);
                    initSearchHistoryList(currentSearchType);
                    keywords.setText("");

                    break;
                case R.id.rent_house: //租房
                    currentSearchType = SearchType.RENT_HOUSE;
                    reHighLightMenu(currentSearchType);
                    reDrawSearchItem(currentSearchType);
                    initSearchHistoryList(currentSearchType);
                    keywords.setText("");

                    break;
                case R.id.information: //资讯
                    currentSearchType = SearchType.INFORMATION;
                    reHighLightMenu(currentSearchType);
                    reDrawSearchItem(currentSearchType);
                    initSearchHistoryList(currentSearchType);
                    keywords.setText("");

                    break;
                case R.id.reset: //重置
                    reset();
                    break;
                case R.id.search: //搜索
                    gotoSearchResultListView();
                    finish();
                    break;
            }
        }
    }

    public class HouseOnTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
                popupWindow = null;
            }
            return false;
        }
    }

    public class ConditionAsyncTask extends AsyncTask<String, Void, JsonResult<ConditionJsonModel>> {

        private String url;

        @Override
        protected JsonResult<ConditionJsonModel> doInBackground(String... params) {
            this.url = params[0];
            return getConditionData(url);

        }

        @Override
        protected void onPostExecute(JsonResult<ConditionJsonModel> jsonResult) {
            if (jsonResult != null && jsonResult.isStatus()) {
                setJsonResult(jsonResult);
            } else {
                setJsonResult(null);
            }

        }
    }
}
