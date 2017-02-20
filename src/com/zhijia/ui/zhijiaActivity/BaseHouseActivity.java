package com.zhijia.ui.zhijiaActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.zhijia.Global;
import com.zhijia.Enum.AreaTypeEnum;
import com.zhijia.Enum.ListType;
import com.zhijia.service.data.Medol.AreaJsonModel;
import com.zhijia.service.data.Medol.AspectJsonModel;
import com.zhijia.service.data.Medol.AveragePriceJsonModel;
import com.zhijia.service.data.Medol.BaseModel;
import com.zhijia.service.data.Medol.CircleLineJsonModel;
import com.zhijia.service.data.Medol.ConditionJsonModel;
import com.zhijia.service.data.Medol.DecorateJsonModel;
import com.zhijia.service.data.Medol.FeatureJsonModel;
import com.zhijia.service.data.Medol.FloorJsonModel;
import com.zhijia.service.data.Medol.HouseageJsonModel;
import com.zhijia.service.data.Medol.JsonResult;
import com.zhijia.service.data.Medol.OpentimeJsonModel;
import com.zhijia.service.data.Medol.OrderJsonModel;
import com.zhijia.service.data.Medol.PlaceJsonModel;
import com.zhijia.service.data.Medol.PriceJsonModel;
import com.zhijia.service.data.Medol.ProjectTypeJsonModel;
import com.zhijia.service.data.Medol.RoomJsonModel;
import com.zhijia.service.data.Medol.SearchModel;
import com.zhijia.service.data.Medol.TypeJsonModel;
import com.zhijia.ui.R;
import com.zhijia.ui.list.adapter.ItemAdapter;
import com.zhijia.ui.list.interfaces.IConditionNetwork;
import com.zhijia.ui.list.interfaces.IHouseDetails;
import com.zhijia.ui.list.interfaces.IListDataNetWork;
import com.zhijia.ui.list.interfaces.ILoadData;

import java.util.*;

/**
 * 所有房子的基类，主要是用来设置公共的信息
 */
public abstract class BaseHouseActivity extends Activity {

    public final int[] imageIds = new int[]{R.id.house_image_one, R.id.house_image_two, R.id.house_image_three, R.id.house_image_four};
    public final int[] areaIds = new int[]{R.id.house_area_one, R.id.house_area_two, R.id.house_area_three, R.id.house_area_four};
    public final int[] linearIds = new int[]{R.id.house_area_linear_one,R.id.house_area_linear_two,R.id.house_area_linear_three,R.id.house_area_linear_four} ;
    public final int[] goneLinearIds = new int[]{R.id.gone_house_area_linear_one,R.id.gone_house_area_linear_two,R.id.gone_house_area_linear_three,R.id.gone_house_area_linear_four} ;
//    public final String ADAPTER_IMAGE = "adapter_image";
//    public final String ADAPTER_HOUSE_NAME = "adapter_house_name";
//    public final String ADAPTER_HOUSE_DISTANCE = "adapter_house_distance";
//    public final String ADAPTER_HOUSE_AREA_NAME = "adapter_house_area_name";
//    public final String ADAPTER_HOUSE_MONEY = "adapter_house_money";
//    public final String ADAPTER_HOUSE_ADDRESS = "adapter_house_address";
//    public final String ADAPTER_HOUSE_DES = "adapter_house_des";
//    public final String[] ADAPTER_FROM = {ADAPTER_IMAGE, ADAPTER_HOUSE_NAME,
//            ADAPTER_HOUSE_DISTANCE, ADAPTER_HOUSE_AREA_NAME, ADAPTER_HOUSE_MONEY, ADAPTER_HOUSE_ADDRESS,
//            ADAPTER_HOUSE_DES};
    public final String ADAPTER_HOUSE_CHOOSE_AREA = "adapter_house_choose_area";
    public final String[] ADAPTER_CHOOSE_FROM = {ADAPTER_HOUSE_CHOOSE_AREA};
//    public final int[] ADAPTER_TO = {R.id.adapter_image, R.id.adapter_house_name,
//            R.id.adapter_house_distance, R.id.adapter_house_area_name, R.id.adapter_house_money,
//            R.id.adapter_house_address, R.id.adapter_house_des};
    public final int[] ADAPTER_CHOOSE_TO = {R.id.adapter_house_choose_area};
    protected final int relativeId = R.id.house_area_bottom_image;
    protected final int goneRelativeId = R.id.gone_house_area_bottom_image;
    protected final int[] goneAreaIds = new int[]{R.id.gone_house_area_one, R.id.gone_house_area_two, R.id.gone_house_area_three, R.id.gone_house_area_four};
    protected PopupWindow popupWindow;
    //这个是要加载出来的一个视图
    protected View footerView;
    private String LOG_TAG = "BaseHouseActivity";
    private ItemAdapter itemAdapter;
    private ListView listView;
    //可见视图最后的一个索引
    private int visibleLastIndex = 0;
    //当前窗口可见项总数
    private int visibleItemCount;
    //private ProgressBar progressBar;

    private View layoutCenterView;


    //如果要是有详细页必须要设置 这个接口
    private IHouseDetails houseDetails;

    //有条件的需要实现这个接口
    private IConditionNetwork conditionNetwork;

    //有列表异步加载的 实现这个接口
    private ILoadData loadData;


    private IListDataNetWork listDataNetWork;


    private BaseModel baseModel;
    //搜索条件对象
    private SearchModel searchModel = new SearchModel();
    private JsonResult<ConditionJsonModel> jsonResult;

    public IListDataNetWork getListDataNetWork() {
        return listDataNetWork;
    }

    public void setListDataNetWork(IListDataNetWork listDataNetWork) {
        this.listDataNetWork = listDataNetWork;
    }

    public BaseModel getBaseModel() {
        return baseModel;
    }

    public void setBaseModel(BaseModel baseModel) {
        this.baseModel = baseModel;
    }

    public ILoadData getLoadData() {
        return loadData;
    }

    public void setLoadData(ILoadData loadData) {
        this.loadData = loadData;
    }

    public IHouseDetails getHouseDetails() {
        return houseDetails;
    }

    public void setHouseDetails(IHouseDetails houseDetails) {
        this.houseDetails = houseDetails;
    }

    public IConditionNetwork getConditionNetwork() {
        return conditionNetwork;
    }

    public void setConditionNetwork(IConditionNetwork conditionNetwork) {
        this.conditionNetwork = conditionNetwork;
    }

    /**
     * 根据View的Id和数量设置公共的View
     *
     * @param textViewId
     * @param count
     */
    public void setCommonHouseCount(int textViewId, String count,String countStr) {
        TextView textView = (TextView) findViewById(textViewId);
        textView.setText(Html.fromHtml("共有<font color='#FF0000'>" + count + "</font>"+countStr));
    }

    /**
     * 设置标题
     *
     * @param houseTopViewId
     * @param houseTitleId
     */
    public void setCommonHouseTitle(int houseTopViewId, int houseTitleId) {
        TextView appGPS = (TextView) findViewById(houseTopViewId);
        appGPS.setText(String.format(getString(houseTitleId), Global.NOW_CITY));
    }


    /**
     * 设置图片
     *
     * @param imageIds
     * @param drawableIds
     */
    private void setCommonHouseImages(int[] imageIds, int[] drawableIds, ListType listType) {

        for (int i = 0; i < imageIds.length; i++) {
            ImageView imageView = (ImageView) findViewById(imageIds[i]);
            imageView.setImageResource(drawableIds[i]);

            if (listType == ListType.OLD || listType == ListType.RENT) {

                findViewById(R.id.house_image_one).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), CommunityListActivity.class);
                        startActivity(intent);
                    }
                });

                findViewById(R.id.house_image_two).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SearchModel searchModel1 = getBaseModel().getSearchModel();
                        if (searchModel1 == null) {
                            searchModel1 = new SearchModel();
                        }
                        searchModel1.setTag("1");
                        if (getListDataNetWork() != null) {
                            listDataNetWork.startListTask(baseModel);
                        }
                    }
                });

                findViewById(R.id.house_image_three).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SearchModel searchModel1 = getBaseModel().getSearchModel();
                        if (searchModel1 == null) {
                            searchModel1 = new SearchModel();
                        }
                        searchModel1.setTag("2");
                        if (getListDataNetWork() != null) {
                            listDataNetWork.startListTask(baseModel);
                        }
                    }
                });

                findViewById(R.id.house_image_four).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SearchModel searchModel1 = getBaseModel().getSearchModel();
                        if (searchModel1 == null) {
                            searchModel1 = new SearchModel();
                        }
                        searchModel1.setTag("3");
                        if (getListDataNetWork() != null) {
                            listDataNetWork.startListTask(baseModel);
                        }
                    }
                });
            }
        }
    }

    /**
     * 设置区域的菜单内容
     *
     * @param areas
     * @param texts
     */
    public void setCommonHouseAreas(int[] areas, String[] texts) {

        for (int i = 0; i < areas.length; i++) {
            TextView textView = (TextView) findViewById(areas[i]);
            textView.setText(texts[i]);
        }
    }


    /**
     * 设置适配器
     *
     * @param baseModel
     */
    public void setAdapterData(BaseModel baseModel) {

        if (baseModel != null && baseModel.getItemLayoutInflaterId() != 0) {

            this.setBaseModel(baseModel);
            listView = (ListView) findViewById(baseModel.getListViewId());
            itemAdapter = new ItemAdapter(this, baseModel.getItemLayoutInflaterId(), baseModel.getListData());
            View goneView = null, visibleView = null, centreView = null, goneCountView = null;
            if (baseModel.getCommonHouseCentre() != 0) {
                layoutCenterView = getLayoutInflater().inflate(baseModel.getCommonHouseCentre(), null);
                listView.addHeaderView(layoutCenterView);
                goneView = findViewById(R.id.house_top_gone_area);
                visibleView = findViewById(R.id.house_visible_area);
                //中间的区域
                centreView = findViewById(baseModel.getCommonHouseCentreId());
                goneCountView = findViewById(R.id.house_top_gone_count);
            }

            //列表加载的布局
            footerView = getLayoutInflater().inflate(R.layout.house_list_wait_load, null);

            footerView.findViewById(R.id.house_list_wait_load_relative).setVisibility(View.GONE);

            //footerView = getLayoutInflater().inflate(R.layout.house_list_load, null);
            //progressBar = (ProgressBar) footerView.findViewById(R.id.house_progressbar);


            //listView.addFooterView(footerView);

            listView.setAdapter(itemAdapter);

            listView.setOnScrollListener(new HouseOnScrollListener(goneView, visibleView, centreView, goneCountView));

            if (this.houseDetails != null) {
                this.houseDetails.showDetail(BaseHouseActivity.this, listView);
            }

        } else {
            new NullPointerException("baseModel or getListData or getItemLayoutInflaterId is null");
        }
        if (baseModel.getTextViewId() != 0) {

            this.setCommonHouseCount(baseModel.getTextViewId(), baseModel.getCount(),baseModel.getCountStr());
            this.setCommonHouseCount(R.id.gone_house_count, baseModel.getCount(),baseModel.getCountStr());
        }

        if (baseModel.getHouseTopViewId() != 0 && baseModel.getHouseTitleId() != 0) {
            this.setCommonHouseTitle(baseModel.getHouseTopViewId(), baseModel.getHouseTitleId());
        }

        if (baseModel.getImageIds() != null && baseModel.getDrawableIds() != null) {
            this.setCommonHouseImages(baseModel.getImageIds(), baseModel.getDrawableIds(), baseModel.getListType());
        }

        if (baseModel.getGoneAreas() != null) {
            this.setCommonHouseAreas(baseModel.getGoneAreas(), baseModel.getTexts());
        }

        if (baseModel.getAreas() != null && baseModel.getTexts() != null) {
            this.setCommonHouseAreas(baseModel.getAreas(), baseModel.getTexts());
        }
        if (baseModel.getConditionURL() != null && !baseModel.getConditionURL().isEmpty()) {
            startConditionAsyncTask(baseModel.getConditionURL());
        }

        if (getListDataNetWork() != null) {
            listDataNetWork.startListTask(baseModel);
        }
    }

    public ItemAdapter getItemAdapter() {
        return itemAdapter;
    }

    public void setItemAdapter(ItemAdapter itemAdapter) {
        this.itemAdapter = itemAdapter;
    }

    /**
     * 设置要显示和隐藏的视图
     *
     * @param goneView
     * @param visibleView
     * @param centreView
     * @param goneCountView
     */
    private void setViewVisibility(View goneView, View visibleView, View centreView, View goneCountView) {
        int[] gone_xy = new int[2];
        int[] visible_xy = new int[2];
        goneView.getLocationInWindow(gone_xy);
        visibleView.getLocationInWindow(visible_xy);

        if (gone_xy[1] >= visible_xy[1]) {

            goneView.setVisibility(View.VISIBLE);
            goneCountView.setVisibility(View.VISIBLE);
            centreView.setVisibility(View.GONE);
        } else if (gone_xy[1] <= visible_xy[1]) {
            goneView.setVisibility(View.GONE);
            goneCountView.setVisibility(View.GONE);
            centreView.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 创建PopupWindow
     */
    protected void initPopuptWindow(int areaId, ListType listType) {
        final Map<String, List<String>> resultMap = getAreaData(areaId, listType);
        final TextView popTextView = (TextView)findViewById(areaId) ;
        Log.d("initPopuptWindow",popTextView.getText().toString()) ;
        // 获取自定义布局文件pop.xml的视图
        View popupWindow_view = getLayoutInflater().inflate(R.layout.common_popup, null, false);
        // 创建PopupWindow实例,200,150分别是宽度和高度
        popupWindow = new PopupWindow(popupWindow_view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        //点击其他地方消失
        popupWindow_view.setOnTouchListener(new HouseOnTouchListener());
        ListView listView = (ListView) popupWindow_view.findViewById(R.id.house_listView_popup);
        final ListView subListView = (ListView) popupWindow_view.findViewById(R.id.house_listView_subpopup);
        final List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        if (resultMap == null) {
            Toast.makeText(BaseHouseActivity.this, Global.ERROR_NET_WORK, Toast.LENGTH_SHORT).show();
            return;
        }
        for (Map.Entry<String, List<String>> temp : resultMap.entrySet()) {
            Map<String, Object> map = new HashMap<String, Object>();
            String[] tempStrs = temp.getKey().split(":");
            //debug模式可以开启这个
            //map.put(ADAPTER_HOUSE_CHOOSE_AREA, temp.getKey());
            //正式环境用这个
            map.put(ADAPTER_HOUSE_CHOOSE_AREA, tempStrs[0]);

            map.put("id", tempStrs[1]);
            map.put("areaType", tempStrs[2]);
            listItems.add(map);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems, R.layout.common_popup_adapter_item, ADAPTER_CHOOSE_FROM, ADAPTER_CHOOSE_TO);
        listView.setAdapter(simpleAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> tempMap = listItems.get(position);
                String parentId = (String) tempMap.get("id");
                String nameStr  = (String)tempMap.get(ADAPTER_HOUSE_CHOOSE_AREA) ;
                int count = parent.getChildCount() ;
                for(int i = 0 ; i <count ; i++){
                    if(i != position){
                        View tempView = parent.getChildAt(i) ;
                        ((TextView)tempView.findViewById(R.id.adapter_house_choose_area)).setTextColor(Color.parseColor("#FF666666"));
                    }
                }
                ((TextView)view.findViewById(R.id.adapter_house_choose_area)).setTextColor(Color.parseColor("#ff0000"));
                //设置条件
                searchModel = getSearchModelByMap(tempMap);
                Log.d("searchModel", searchModel.toString());
                //负责获取子元素
                if (resultMap.values() != null) {
                    final List<Map<String, Object>> subListItems = new ArrayList<Map<String, Object>>();
                    TextView textView = (TextView) view.findViewById(R.id.adapter_house_choose_area);

                    //debug模式
                    //List<String> subList = resultMap.get(textView.getText().toString());
                    //正式模式
                    String parentKey ;
                    if(parentId.equals("-1")){
                        parentKey  = textView.getText().toString() + ":" + parentId + ":more";
                    } else {
                        parentKey  = textView.getText().toString() + ":" + parentId + ":area";
                    }

                    Log.d("parentKey", parentKey);
                    List<String> subList = resultMap.get(parentKey);
                    if (subList != null && subList.size() >0) {
                        if (subListView.getAdapter() != null) {
                            ((SimpleAdapter) subListView.getAdapter()).notifyDataSetInvalidated();
                            ((SimpleAdapter) subListView.getAdapter()).notifyDataSetChanged();
                        }

                        for (String subStr : subList) {
                            String[] tempSubStr = subStr.split(":");
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put(ADAPTER_HOUSE_CHOOSE_AREA, tempSubStr[0]);
                            map.put("id", tempSubStr[1]);
                            map.put("areaType", tempSubStr[2]);
                            subListItems.add(map);
                        }

                        SimpleAdapter simpleAdapter = new SimpleAdapter(BaseHouseActivity.this, subListItems, R.layout.common_popup_adapter_item, ADAPTER_CHOOSE_FROM, ADAPTER_CHOOSE_TO);
                        subListView.setAdapter(simpleAdapter);
                        ((SimpleAdapter) subListView.getAdapter()).notifyDataSetChanged();
                        subListView.setVisibility(View.VISIBLE);

                        //当触发字集的时候进行搜索
                        subListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Map<String, Object> tempSubMap = subListItems.get(position);
                                popTextView.setText((String)tempSubMap.get(ADAPTER_HOUSE_CHOOSE_AREA));
                                searchModel = getSearchModelByMap(tempSubMap);
                                ((TextView)view.findViewById(R.id.adapter_house_choose_area)).setTextColor(Color.parseColor("#ff0000"));
                                if (getBaseModel() != null && getListDataNetWork() != null) {
                                    getBaseModel().setSearchModel(searchModel);
                                    destroyPop();
                                    listDataNetWork.startListTask(getBaseModel());
                                }
                            }
                        });
                    } else {
                        if (subListView.getAdapter() != null) {
                            subListItems.clear();
                            ((SimpleAdapter) subListView.getAdapter()).notifyDataSetChanged();
                            ((SimpleAdapter) subListView.getAdapter()).notifyDataSetInvalidated();
                        }

                        subListView.setVisibility(View.GONE);
                        popTextView.setText(nameStr);
                        if (getBaseModel() != null && getListDataNetWork() != null) {

                            getBaseModel().setSearchModel(searchModel);
                            destroyPop();
                            listDataNetWork.startListTask(getBaseModel());
                        }
                    }
                }
            }
        });
    }

    //设置条件
    private SearchModel getSearchModelByMap(Map<String, Object> map) {
        String areaType = (String) map.get("areaType");
        String id = (String) map.get("id");
        AreaTypeEnum areaTypeEnum = AreaTypeEnum.getAreaType(areaType);
        if(baseModel.getSearchModel() != null){
            searchModel = baseModel.getSearchModel() ;
        }
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

        }
        return searchModel;
    }

    private Map<String, List<String>> getAreaData(int areaId, ListType listType) {
        if (getJsonResult() != null && getJsonResult().isStatus()) {
            return getAreaDataByNetWork(getJsonResult().getData(), areaId, listType);
        } else {
            // return DefaultDataUtils.getDefaultArea(areaId, listType) ;
            return null;
        }
    }

    /**
     * 条件的数据全部在这里进行写
     *
     * @param conditionJsonModel
     * @param areaId
     * @param listType
     * @return
     */
    private Map<String, List<String>> getAreaDataByNetWork(ConditionJsonModel conditionJsonModel, int areaId, ListType listType) {
        Map<String, List<String>> resultMap = new LinkedHashMap<String, List<String>>();
        switch (listType) {
            case NEW:
                switch (areaId) {
                    case R.id.house_area_one:
                        Log.d("getAreaDataByNetWork", "begin");
                        setAreaByNetWork(resultMap, conditionJsonModel);
                        break;
                    case R.id.gone_house_area_one:
                        setAreaByNetWork(resultMap, conditionJsonModel);
                        break;

                    case R.id.house_area_two:
                        setPriceByNetWork(resultMap, conditionJsonModel);
                        break;
                    case R.id.gone_house_area_two:
                        setPriceByNetWork(resultMap, conditionJsonModel);
                        break;
                    case R.id.house_area_three:
                        setTypeByNetWork(resultMap, conditionJsonModel, ListType.NEW);
                        break;
                    case R.id.gone_house_area_three:
                        setTypeByNetWork(resultMap, conditionJsonModel, ListType.NEW);
                        break;
                    case R.id.house_area_four:
                        setNewMoreByNetWork(resultMap, conditionJsonModel);
                        break;
                    case R.id.gone_house_area_four:
                        setNewMoreByNetWork(resultMap, conditionJsonModel);
                        break;
                }
                break;
            case OLD:
                switch (areaId) {
                    case R.id.house_area_one:
                        setAreaByNetWork(resultMap, conditionJsonModel);
                        break;

                    case R.id.gone_house_area_one:
                        setAreaByNetWork(resultMap, conditionJsonModel);
                        break;

                    case R.id.house_area_two:
                        setPriceByNetWork(resultMap, conditionJsonModel);
                        break;

                    case R.id.gone_house_area_two:
                        setPriceByNetWork(resultMap, conditionJsonModel);
                        break;

                    case R.id.house_area_three:
                        setProportionByNetWork(resultMap, conditionJsonModel);
                        break;

                    case R.id.gone_house_area_three:
                        setProportionByNetWork(resultMap, conditionJsonModel);
                        break;

                    case R.id.house_area_four:
                        setOldMoreByNetWork(resultMap, conditionJsonModel);
                        break;

                    case R.id.gone_house_area_four:
                        setOldMoreByNetWork(resultMap, conditionJsonModel);
                        break;
                }
                break;
            case RENT:

                switch (areaId) {
                    case R.id.house_area_one:
                        setAreaByNetWork(resultMap, conditionJsonModel);
                        break;

                    case R.id.gone_house_area_one:
                        setAreaByNetWork(resultMap, conditionJsonModel);
                        break;

                    case R.id.house_area_two:
                        setPriceByNetWork(resultMap, conditionJsonModel);
                        break;

                    case R.id.gone_house_area_two:
                        setPriceByNetWork(resultMap, conditionJsonModel);
                        break;

                    case R.id.house_area_three:
                        setProportionByNetWork(resultMap, conditionJsonModel);
                        break;

                    case R.id.gone_house_area_three:
                        setProportionByNetWork(resultMap, conditionJsonModel);
                        break;

                    case R.id.house_area_four:
                        setRentMoreByNetWork(resultMap, conditionJsonModel);

                        break;

                    case R.id.gone_house_area_four:
                        setRentMoreByNetWork(resultMap, conditionJsonModel);

                        break;
                }
                break;

            case LOOK_HOUSE:
                switch (areaId) {
                    case R.id.house_area_one:
                        setAreaByNetWork(resultMap, conditionJsonModel);
                        break;


                    case R.id.house_area_two:
                        setPriceByNetWork(resultMap, conditionJsonModel);
                        break;

                    case R.id.house_area_three:
                        setAveragePrice(resultMap, conditionJsonModel);
                        break;

                    case R.id.house_area_four:
                        setTypeByNetWork(resultMap, conditionJsonModel, ListType.LOOK_HOUSE);
                        break;

                }
                break;
            case OLD_LOOK_HOUSE:
                switch (areaId) {
                    case R.id.house_area_one:
                        setOldCommunityAreaByNetWork(resultMap, conditionJsonModel);
                        break;


                    case R.id.house_area_two:
                        //setAveragePrice(resultMap, conditionJsonModel);
                        setRoomByNetWork(resultMap,conditionJsonModel) ;
                        break;

                    case R.id.house_area_three:
                        //setPriceByNetWork(resultMap, conditionJsonModel);
                        setSellPriceByNetWork(resultMap,conditionJsonModel) ;
                        break;

                    case R.id.house_area_four:
                        setOrderByNetWork(resultMap, conditionJsonModel);
                        break;

                }
                break;

            case RENT_LOOK_HOUSE:
                switch (areaId) {
                    case R.id.house_area_one:
                        setOldCommunityAreaByNetWork(resultMap, conditionJsonModel);
                        break;


                    case R.id.house_area_two:
                        setRentPriceByNetWork(resultMap,conditionJsonModel);
                        break;

                    case R.id.house_area_three:
                        setRoomByNetWork(resultMap,conditionJsonModel) ;
                        break;

                    case R.id.house_area_four:
                        setOrderByNetWork(resultMap, conditionJsonModel);
                        break;

                }
                break;
        }

        return resultMap;
    }




    private void setAveragePrice(Map<String, List<String>> resultMap, ConditionJsonModel conditionJsonModel) {
        for (Map.Entry<String, AveragePriceJsonModel> averagePriceJsonModel : conditionJsonModel.getAveragePrice().entrySet()) {
            resultMap.put(averagePriceJsonModel.getValue().getName() + ":" + averagePriceJsonModel.getValue().getFid() + ":average_price", null);
        }
    }

    /**
     * 区域类型
     *
     * @param resultMap
     * @param conditionJsonModel
     */
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

    private void setOldCommunityAreaByNetWork(Map<String, List<String>> resultMap, ConditionJsonModel conditionJsonModel){
        for (Map.Entry<String, AreaJsonModel> areaJsonModel : conditionJsonModel.getArea().entrySet()) {
            resultMap.put(areaJsonModel.getValue().getName() + ":" + areaJsonModel.getValue().getFid() + ":area", null);
        }
    }

    /**
     * 面积
     *
     * @param resultMap
     * @param conditionJsonModel
     */
    private void setProportionByNetWork(Map<String, List<String>> resultMap, ConditionJsonModel conditionJsonModel) {

        for (Map.Entry<String, AreaJsonModel> areaJsonModel : conditionJsonModel.getArea().entrySet()) {
            resultMap.put(areaJsonModel.getValue().getName() + ":" + areaJsonModel.getValue().getFid() + ":proportion", null);
        }
    }

    private void setSellPriceByNetWork(Map<String, List<String>> resultMap, ConditionJsonModel conditionJsonModel){
        for (Map.Entry<String, PriceJsonModel> priceJsonModel : conditionJsonModel.getSellPrice().entrySet()) {
            resultMap.put(priceJsonModel.getValue().getName() + ":" + priceJsonModel.getValue().getFid() + ":sellprice", null);
        }
    }

    /**
     * 价钱
     *
     * @param resultMap
     * @param conditionJsonModel
     */
    private void setPriceByNetWork(Map<String, List<String>> resultMap, ConditionJsonModel conditionJsonModel) {
        for (Map.Entry<String, PriceJsonModel> priceJsonModel : conditionJsonModel.getPrice().entrySet()) {
            Log.d("old price ==",conditionJsonModel.getPrice().getClass().getName()) ;
            resultMap.put(priceJsonModel.getValue().getName() + ":" + priceJsonModel.getValue().getFid() + ":price", null);
        }
    }

    /**
     * 类型
     *
     * @param resultMap
     * @param conditionJsonModel
     */
    private void setTypeByNetWork(Map<String, List<String>> resultMap, ConditionJsonModel conditionJsonModel, ListType listType) {

        for (Map.Entry<String, TypeJsonModel> typeJsonModel : conditionJsonModel.getType().entrySet()) {
            if (listType == ListType.LOOK_HOUSE) {
                resultMap.put(typeJsonModel.getValue().getName() + ":" + typeJsonModel.getValue().getFid() + ":renttype", null);
            } else {
                resultMap.put(typeJsonModel.getValue().getName() + ":" + typeJsonModel.getValue().getFid() + ":type", null);
            }
        }
    }

    private void setRoomByNetWork(Map<String, List<String>> resultMap, ConditionJsonModel conditionJsonModel) {
        for (Map.Entry<String, RoomJsonModel> roomJsonModel : conditionJsonModel.getRoom().entrySet()) {
            resultMap.put(roomJsonModel.getValue().getName() + ":" + roomJsonModel.getValue().getFid() + ":room", null);
        }
    }


    private void setRentPriceByNetWork(Map<String,List<String>> resultMap,ConditionJsonModel conditionJsonModel){
         for(Map.Entry<String ,AveragePriceJsonModel> rentPriceJsonModel:conditionJsonModel.getRentPrice().entrySet()){
             resultMap.put(rentPriceJsonModel.getValue().getName() + ":" + rentPriceJsonModel.getValue().getFid() + ":rentprice", null);
         }
    }


     private void setOrderByNetWork(Map<String, List<String>> resultMap,ConditionJsonModel conditionJsonModel){
         for (Map.Entry<String, OrderJsonModel> orderJsonModel : conditionJsonModel.getOrder().entrySet()) {
             resultMap.put(orderJsonModel.getValue().getName() + ":" + orderJsonModel.getValue().getFid() + ":order", null);
         }
     }


    private void setNewMoreByNetWork(Map<String, List<String>> resultMap, ConditionJsonModel conditionJsonModel){
       List<String> featureList =  new ArrayList<String>() ;
        for (Map.Entry<String, FeatureJsonModel> featureJsonModel : conditionJsonModel.getFeature().entrySet()){
            featureList.add(featureJsonModel.getValue().getName() + ":" + featureJsonModel.getValue().getFid() + ":feature");
        }
        if(featureList.size()>0){
            resultMap.put("特色:-1:more",featureList) ;
        }

        List<String> decorateList = new ArrayList<String>() ;
        for (Map.Entry<String, DecorateJsonModel> decorateJsonModel : conditionJsonModel.getDecorate().entrySet()){
            decorateList.add(decorateJsonModel.getValue().getName() + ":" + decorateJsonModel.getValue().getFid() + ":decorate");
        }
        if(decorateList.size()>0){
            resultMap.put("装修:-1:more",decorateList) ;
        }
        List<String> openTimeList = new ArrayList<String>() ;
        for (Map.Entry<String, OpentimeJsonModel> opentimeJsonModel : conditionJsonModel.getOpentime().entrySet()) {
            openTimeList.add(opentimeJsonModel.getValue().getName() + ":" + opentimeJsonModel.getValue().getFid() + ":opentime");
        }
        if(openTimeList.size() > 0){
            resultMap.put("开盘时间:-1:more",openTimeList) ;
        }
        List<String> orderList = new ArrayList<String>() ;
        for (Map.Entry<String, OrderJsonModel> orderJsonModel : conditionJsonModel.getOrder().entrySet()) {
            orderList.add(orderJsonModel.getValue().getName() + ":" + orderJsonModel.getValue().getFid() + ":order");
        }
        if(orderList.size() > 0){
            resultMap.put("排序:-1:more",orderList) ;
        }
        List<String> circleLineList = new ArrayList<String>() ;
        for (Map.Entry<String, CircleLineJsonModel> circleLineJsonModel : conditionJsonModel.getCircleLine().entrySet()) {
            circleLineList.add(circleLineJsonModel.getValue().getName() + ":" + circleLineJsonModel.getValue().getFid() + ":circleLine");
        }
        if(circleLineList.size()>0){
            resultMap.put("环线:-1:more",circleLineList) ;
        }
    }

    private void  setOldMoreByNetWork(Map<String, List<String>> resultMap, ConditionJsonModel conditionJsonModel){
        List<String> roomList = new ArrayList<String>() ;
        for (Map.Entry<String, RoomJsonModel> roomJsonModel : conditionJsonModel.getRoom().entrySet()) {
            roomList.add(roomJsonModel.getValue().getName() + ":" + roomJsonModel.getValue().getFid() + ":room");
        }
        if(roomList.size()>0){
            resultMap.put("户型:-1:more",roomList) ;
        }

        List<String> floorList = new ArrayList<String>() ;
        for (Map.Entry<String, FloorJsonModel> floorJsonModel : conditionJsonModel.getFloor().entrySet()) {
            floorList.add(floorJsonModel.getValue().getName() + ":" + floorJsonModel.getValue().getFid() + ":floor");
        }
        if(floorList.size()>0){
            resultMap.put("楼层:-1:more",floorList) ;
        }
        List<String> aspectList = new ArrayList<String>() ;
        for (Map.Entry<String, AspectJsonModel> aspectJsonModel : conditionJsonModel.getAspect().entrySet()) {
            aspectList.add(aspectJsonModel.getValue().getName() + ":" + aspectJsonModel.getValue().getFid() + ":aspect");
        }
        if(aspectList.size()>0){
            resultMap.put("朝向:-1:more",aspectList) ;
        }

        List<String> decorateList = new ArrayList<String>() ;
        for (Map.Entry<String, DecorateJsonModel> decorateJsonModel : conditionJsonModel.getDecorate().entrySet()){
            decorateList.add(decorateJsonModel.getValue().getName() + ":" + decorateJsonModel.getValue().getFid() + ":decorate");
        }
        if(decorateList.size()>0){
            resultMap.put("装修:-1:more",decorateList) ;
        }

        List<String> houseAgeList = new ArrayList<String>();
        for (Map.Entry<String, HouseageJsonModel> houseageJsonModel : conditionJsonModel.getHouseage().entrySet()) {
            houseAgeList.add(houseageJsonModel.getValue().getName() + ":" + houseageJsonModel.getValue().getFid() + ":houseage");
        }
        if(houseAgeList.size()>0){
            resultMap.put("房龄:-1:more",houseAgeList) ;
        }
        List<String> orderList = new ArrayList<String>() ;
        for (Map.Entry<String, OrderJsonModel> orderJsonModel : conditionJsonModel.getOrder().entrySet()) {
            orderList.add(orderJsonModel.getValue().getName() + ":" + orderJsonModel.getValue().getFid() + ":order");
        }
        if(orderList.size() > 0){
            resultMap.put("排序:-1:more",orderList) ;
        }
        List<String> sourceList = new ArrayList<String>();
        for (Map.Entry<String, String> sourceJsonModel : conditionJsonModel.getSource().entrySet()) {
            sourceList.add(sourceJsonModel.getValue() + ":" + sourceJsonModel.getKey() + ":source");
        }
        if(sourceList.size()>0){
            resultMap.put("来源:-1:more",sourceList) ;
        }
    }

    private void setRentMoreByNetWork(Map<String, List<String>> resultMap, ConditionJsonModel conditionJsonModel){
        setOldMoreByNetWork(resultMap,conditionJsonModel);
        List<String> rentTypeList = new ArrayList<String>() ;
        for (Map.Entry<String, TypeJsonModel> typeJsonModel : conditionJsonModel.getType().entrySet()) {
            rentTypeList.add(typeJsonModel.getValue().getName() + ":" + typeJsonModel.getValue().getFid() + ":rent_type");
        }
        if(rentTypeList.size()>0){
            resultMap.put("出租类型:-1:more",rentTypeList) ;
        }
    }

    /**
     * 更多
     *
     * @param resultMap
     * @param conditionJsonModel
     */
    private void setMoreByNetWork(Map<String, List<String>> resultMap, ConditionJsonModel conditionJsonModel, ListType listType) {

        if (conditionJsonModel.getFeature() != null) {
            for (Map.Entry<String, FeatureJsonModel> featureJsonModel : conditionJsonModel.getFeature().entrySet()) {
                resultMap.put(featureJsonModel.getValue().getName() + ":" + featureJsonModel.getValue().getFid() + ":feature", null);
            }
        }

        if (conditionJsonModel.getDecorate() != null) {
            for (Map.Entry<String, DecorateJsonModel> decorateJsonModel : conditionJsonModel.getDecorate().entrySet()) {
                resultMap.put(decorateJsonModel.getValue().getName() + ":" + decorateJsonModel.getValue().getFid() + ":decorate", null);
            }
        }

        if (conditionJsonModel.getOpentime() != null) {
            for (Map.Entry<String, OpentimeJsonModel> opentimeJsonModel : conditionJsonModel.getOpentime().entrySet()) {
                resultMap.put(opentimeJsonModel.getValue().getName() + ":" + opentimeJsonModel.getValue().getFid() + ":opentime", null);
            }
        }

        if (conditionJsonModel.getCircleLine() != null) {
            for (Map.Entry<String, CircleLineJsonModel> circleLineJsonModel : conditionJsonModel.getCircleLine().entrySet()) {
                resultMap.put(circleLineJsonModel.getValue().getName() + ":" + circleLineJsonModel.getValue().getFid() + ":circleLine", null);
            }
        }

        if (conditionJsonModel.getOrder() != null) {
            for (Map.Entry<String, OrderJsonModel> orderJsonModel : conditionJsonModel.getOrder().entrySet()) {
                resultMap.put(orderJsonModel.getValue().getName() + ":" + orderJsonModel.getValue().getFid() + ":order", null);
            }
        }

        if (conditionJsonModel.getRoom() != null) {
            for (Map.Entry<String, RoomJsonModel> roomJsonModel : conditionJsonModel.getRoom().entrySet()) {
                resultMap.put(roomJsonModel.getValue().getName() + ":" + roomJsonModel.getValue().getFid() + ":room", null);
            }

        }

        if (conditionJsonModel.getAspect() != null) {
            for (Map.Entry<String, AspectJsonModel> aspectJsonModel : conditionJsonModel.getAspect().entrySet()) {
                resultMap.put(aspectJsonModel.getValue().getName() + ":" + aspectJsonModel.getValue().getFid() + ":aspect", null);
            }
        }

        if (conditionJsonModel.getHouseage() != null) {
            for (Map.Entry<String, HouseageJsonModel> houseageJsonModel : conditionJsonModel.getHouseage().entrySet()) {
                resultMap.put(houseageJsonModel.getValue().getName() + ":" + houseageJsonModel.getValue().getFid() + ":houseage", null);
            }
        }

        if (conditionJsonModel.getFloor() != null) {
            for (Map.Entry<String, FloorJsonModel> floorJsonModel : conditionJsonModel.getFloor().entrySet()) {
                resultMap.put(floorJsonModel.getValue().getName() + ":" + floorJsonModel.getValue().getFid() + ":floor", null);
            }
        }

        if (conditionJsonModel.getSource() != null) {
            for (Map.Entry<String, String> sourceJsonModel : conditionJsonModel.getSource().entrySet()) {
                resultMap.put(sourceJsonModel.getValue() + ":" + sourceJsonModel.getKey() + ":source", null);
            }
        }

        if (conditionJsonModel.getTag() != null) {
            for (Map.Entry<String, String> tagJsonModel : conditionJsonModel.getTag().entrySet()) {
                resultMap.put(tagJsonModel.getValue() + ":" + tagJsonModel.getKey() + ":tag", null);
            }
        }

        //这个类型只有在租房里才有
        if (listType == ListType.RENT) {
            if (conditionJsonModel.getType() != null) {
                for (Map.Entry<String, TypeJsonModel> typeJsonModel : conditionJsonModel.getType().entrySet()) {
                    resultMap.put(typeJsonModel.getValue().getName() + ":" + typeJsonModel.getValue().getFid() + ":rent_type", null);
                }
            }
        }


        if (conditionJsonModel.getProjectType() != null) {
            for (Map.Entry<String, ProjectTypeJsonModel> projectTypeJsonModel : conditionJsonModel.getProjectType().entrySet()) {
                resultMap.put(projectTypeJsonModel.getValue().getName() + ":" + projectTypeJsonModel.getValue().getFid() + ":project_type", null);
            }
        }
    }

    /**
     * 获取PopupWindow实例
     */
    public void getPopupWindow(int areaId, ListType listType) {

        if (null != popupWindow) {
            popupWindow.dismiss();
            return;
        } else {
            initPopuptWindow(areaId, listType);
        }
    }


    /**
     * 对区域的事件绑定。按照列表类型绑定。
     *
     * @param areaIds
     * @param relativeId
     * @param listType
     */
    public void bindEventArea(int[] areaIds, final int relativeId, ListType listType) {
        for (int i = 0; i < areaIds.length; i++) {
            this.findViewById(areaIds[i]).setOnClickListener(new HousePopupOnClickListener(areaIds[i], relativeId, listType));
        }
    }


    public void bindEventArea(int[] linearIds,int[] areaIds, final int relativeId, ListType listType) {

        for(int i = 0 ; i < linearIds.length ; i++){
            this.findViewById(linearIds[i]).setOnClickListener(new HousePopupOnClickListener(areaIds[i], relativeId, listType));
        }
    }


    public JsonResult<ConditionJsonModel> getJsonResult() {
        return jsonResult;
    }

    public void setJsonResult(JsonResult<ConditionJsonModel> jsonResult) {
        this.jsonResult = jsonResult;
    }


    public void destroyPop() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }

    /**
     * 启动条件线程
     */
    public void startConditionAsyncTask(String url) {
        ConditionAsyncTask conditionAsyncTask = new ConditionAsyncTask();
        conditionAsyncTask.execute(url);
    }

    public class ConditionAsyncTask extends AsyncTask<String, Void, JsonResult<ConditionJsonModel>> {

        private String url;

        @Override
        protected JsonResult<ConditionJsonModel> doInBackground(String... params) {
            if (conditionNetwork != null) {
                this.url = params[0];
                return conditionNetwork.getConditionData(url);
            } else {
                return null;
            }

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

    public class HouseOnScrollListener implements AbsListView.OnScrollListener {


        private View goneView;
        private View visibleView;
        private View centreView;
        private View goneCountView;

        public HouseOnScrollListener() {

        }

        public HouseOnScrollListener(View goneView, View visibleView, View centreView, View goneCountView) {
            this.goneView = goneView;
            this.visibleView = visibleView;
            this.centreView = centreView;
            this.goneCountView = goneCountView;
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            int itemsLastIndex = itemAdapter.getCount() - 1;    //数据集最后一项的索引
            Log.d("itemsLastIndex", "itemsLastIndex===" + itemsLastIndex);
            //加上header和footer view的数量。
            int lastIndex = itemsLastIndex + ((ListView) view).getHeaderViewsCount() + ((ListView) view).getFooterViewsCount();
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && visibleLastIndex == lastIndex) {
                Log.d("footerView", "footerView start");
                footerView.setVisibility(View.VISIBLE);
                Log.d("footerView", "footerView end");
                //HousePrivilegeTask task = new HousePrivilegeTask(progressBar);
                HousePrivilegeTask task = new HousePrivilegeTask();
                task.execute(getBaseModel());
            } else {
                footerView.setVisibility(View.GONE);
            }


        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int _visibleItemCount, int totalItemCount) {
            if (isNotNullView()) {
                setViewVisibility(goneView, visibleView, centreView, goneCountView);
            }
            visibleItemCount = _visibleItemCount;
            visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
        }

        private boolean isNotNullView() {

            return (this.goneView != null) && (this.visibleView != null) && (this.centreView != null) && (this.goneCountView != null);
        }
    }

    /**
     * 分页异步交互
     */
    class HousePrivilegeTask extends AsyncTask<BaseModel, Integer, Void> {

//        private ProgressBar progressBar;

        private BaseModel baseModel;

        public HousePrivilegeTask() {
        }

        ;

//        public HousePrivilegeTask(ProgressBar progressBar) {
//            this.progressBar = progressBar;
//        }

        @Override
        protected Void doInBackground(BaseModel... params) {
            baseModel = params[0];
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//            for (int i = 10; i <= 100; i += 10) {
//                publishProgress(i);
//            }
//
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
            loadData.loadData(baseModel);
            itemAdapter.notifyDataSetChanged(); //数据集变化后,通知adapter
            listView.setSelection(visibleLastIndex - visibleItemCount + 1); //设置选中项
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // progressBar.setProgress(values[0]);
        }
    }

    /**
     * 所有返回按钮的监听器
     */
    public class HouseOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.house_back:
                    finish();
                    break;
            }
        }
    }

    public class HousePopupOnClickListener implements View.OnClickListener {

        private int areaId;

        private int relativeId;

        private ListType listType;

        public HousePopupOnClickListener(int areaId, int relativeId, ListType listType) {
            this.areaId = areaId;
            this.relativeId = relativeId;
            this.listType = listType;
        }

        @Override
        public void onClick(View v) {
            getPopupWindow(areaId, listType);
            //相对与谁显示
            popupWindow.showAsDropDown(findViewById(relativeId));
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
}
