package com.zhijia.service.data.Medol;

import com.zhijia.Enum.ListType;
import com.zhijia.ui.R;
import com.zhijia.ui.list.page.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 公共模型
 */
public class BaseModel {

    private int textViewId;
    private String count,countStr;

    private int houseTopViewId, houseTitleId;

    private int[] imageIds, drawableIds;

    private int[] areas;

    private int[] goneAreas;

    private String[] texts;

    //item的布局ID
    private int itemLayoutInflaterId ;

    //这个主要是装载数据的，Map里的key是对应的item的view id，value就是这个item的值
    private List<Map<Integer, Object>> listData;


    private List<AdapterModel> adapterModelList;

    //这个是中间图布局的int
    private int commonHouseCentre;


    //中间图布局的ID
    private int commonHouseCentreId;


    private ListType listType ;


    //所搜条件
    private SearchModel searchModel ;



    //分页对象
    private Page page ;


    //条件的URL
    private String conditionURL ;

    //这个是listView的id，你可以不写，直接用默认的。
    private int listViewId ;

    public BaseModel(){
        this.adapterModelList = new ArrayList<AdapterModel>();
        this.listData = new ArrayList<Map<Integer, Object>>() ;
        searchModel = new SearchModel() ;
    }

    public BaseModel(int commonHouseCentre, int commonHouseCentreId,ListType listType) {
        this.commonHouseCentre = commonHouseCentre;
        this.commonHouseCentreId = commonHouseCentreId;
        this.listType = listType ;
        this.adapterModelList = new ArrayList<AdapterModel>();
        this.listData = new ArrayList<Map<Integer, Object>>() ;
        searchModel = new SearchModel() ;
    }


    public List<Map<Integer, Object>> getListData() {
        return listData;
    }

    public void setListData(List<Map<Integer, Object>> listData) {
        this.listData = listData;
    }


    public int getItemLayoutInflaterId() {
        return itemLayoutInflaterId;
    }

    public void setItemLayoutInflaterId(int itemLayoutInflaterId) {
        this.itemLayoutInflaterId = itemLayoutInflaterId;
    }

    public int[] getGoneAreas() {
        return goneAreas;
    }

    public void setGoneAreas(int[] goneAreas) {
        this.goneAreas = goneAreas;
    }

    public int getCommonHouseCentreId() {
        return commonHouseCentreId;
    }

    public void setCommonHouseCentreId(int commonHouseCentreId) {
        this.commonHouseCentreId = commonHouseCentreId;
    }

    public int getCommonHouseCentre() {
        return commonHouseCentre;
    }

    public void setCommonHouseCentre(int commonHouseCentre) {
        this.commonHouseCentre = commonHouseCentre;
    }


    public int getTextViewId() {
        return textViewId;
    }

    public void setTextViewId(int textViewId) {
        this.textViewId = textViewId;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public int getHouseTopViewId() {
        return houseTopViewId;
    }

    public void setHouseTopViewId(int houseTopViewId) {
        this.houseTopViewId = houseTopViewId;
    }

    public int getHouseTitleId() {
        return houseTitleId;
    }

    public void setHouseTitleId(int houseTitleId) {
        this.houseTitleId = houseTitleId;
    }

    public int[] getImageIds() {
        return imageIds;
    }

    public void setImageIds(int[] imageIds) {
        this.imageIds = imageIds;
    }

    public int[] getDrawableIds() {
        return drawableIds;
    }

    public void setDrawableIds(int[] drawableIds) {
        this.drawableIds = drawableIds;
    }

    public int[] getAreas() {
        return areas;
    }

    public void setAreas(int[] areas) {
        this.areas = areas;
    }

    public String[] getTexts() {
        return texts;
    }

    public void setTexts(String[] texts) {
        this.texts = texts;
    }

    public List<AdapterModel> getAdapterModelList() {
        return adapterModelList;
    }

    public void setAdapterModelList(List<AdapterModel> adapterModelList) {
        this.adapterModelList = adapterModelList;
    }

    public int getListViewId() {
        if(this.listViewId == 0){
            return R.id.house_listView ;
        }
        return listViewId;
    }

    public void setListViewId(int listViewId) {
        this.listViewId = listViewId;
    }

    public ListType getListType() {

        return listType;
    }

    public void setListType(ListType listType) {
        this.listType = listType;
    }

    public SearchModel getSearchModel() {
        return searchModel;
    }

    public void setSearchModel(SearchModel searchModel) {
        this.searchModel = searchModel;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }


    public String getConditionURL() {
        return conditionURL;
    }

    public void setConditionURL(String conditionURL) {
        this.conditionURL = conditionURL;
    }

    public String getCountStr() {
        if(countStr != null && !countStr.isEmpty())
        return countStr;
        else{
            return "" ;
        }
    }

    public void setCountStr(String countStr) {
        this.countStr = countStr;
    }
}
