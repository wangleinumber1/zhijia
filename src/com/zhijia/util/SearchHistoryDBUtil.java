package com.zhijia.util;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.zhijia.Global;
import com.zhijia.service.data.Medol.NewHouseSearchHistoryModel;
import com.zhijia.service.data.Medol.NewsSearchHistoryModel;
import com.zhijia.service.data.Medol.OldHouseSearchHistoryModel;
import com.zhijia.service.data.Medol.RentHouseSearchHistoryModel;
import com.zhijia.service.data.Medol.SearchHistoryBaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 和房子搜索相关的数据库工具类
 */
public class SearchHistoryDBUtil {

    private static DBUtil dbUtil = new DBUtil(Global.getInstance());

    /**
     * 往数据库里增加一个搜索选项历史
     *
     * @param obj HouseSearchHistoryBaseModel子类的对象
     */
    public static void addSearchHistory(SearchHistoryBaseModel obj) {
        SQLiteDatabase database = dbUtil.openDataBase();

        if (obj instanceof NewHouseSearchHistoryModel) {
            NewHouseSearchHistoryModel tempObj = (NewHouseSearchHistoryModel) obj;
            database.execSQL("delete from " + DBUtil.SEARCH_NEW_HOUSE_HISTORY_TABLE + " where uni_str = ?", new Object[]{obj.toString()});
            database.execSQL("insert into " + DBUtil.SEARCH_NEW_HOUSE_HISTORY_TABLE
                            + "(keyword, area_id, area, circle_id, price_id, price, type_id, type, feature_id, feature, decoration_id, decoration, open_time_id, open_time, uni_str, millisecond) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                    new Object[]{tempObj.getKeyword(), tempObj.getAreaId(), tempObj.getArea(), tempObj.getCircleid(), tempObj.getPriceId(), tempObj.getPrice(),
                            tempObj.getTypeId(), tempObj.getType(), tempObj.getFeatureId(), tempObj.getFeature(),
                            tempObj.getDecorationId(), tempObj.getDecoration(), tempObj.getOpenTimeId(), tempObj.getOpenTime(), tempObj.toString(), System.currentTimeMillis()}
            );
        } else if (obj instanceof OldHouseSearchHistoryModel) {
            OldHouseSearchHistoryModel tempObj = (OldHouseSearchHistoryModel) obj;
            database.execSQL("delete from " + DBUtil.SEARCH_OLD_HOUSE_HISTORY_TABLE + " where uni_str = ?", new Object[]{obj.toString()});
            database.execSQL("insert into " + DBUtil.SEARCH_OLD_HOUSE_HISTORY_TABLE
                            + "(keyword, area_id, area, circle_id, total_price_id, total_price, proportion_id, proportion, room_type_id, room_type, source_id, source, decoration_id, decoration, uni_str, millisecond) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                    new Object[]{tempObj.getKeyword(), tempObj.getAreaId(), tempObj.getArea(), tempObj.getCircleid(), tempObj.getTotalPriceId(), tempObj.getTotalPrice(),
                            tempObj.getProportionId(), tempObj.getProportion(), tempObj.getRoomTypeId(), tempObj.getRoomType(),
                            tempObj.getSourceId(), tempObj.getSource(), tempObj.getDecorationId(), tempObj.getDecoration(), tempObj.toString(), System.currentTimeMillis()}
            );
        } else if (obj instanceof RentHouseSearchHistoryModel) {
            RentHouseSearchHistoryModel tempObj = (RentHouseSearchHistoryModel) obj;
            database.execSQL("delete from " + DBUtil.SEARCH_RENT_HOUSE_HISTORY_TABLE + " where uni_str = ?", new Object[]{obj.toString()});
            database.execSQL("insert into " + DBUtil.SEARCH_RENT_HOUSE_HISTORY_TABLE
                            + "(keyword, area_id, area, circle_id, rental_id, rental, proportion_id, proportion, room_type_id, room_type, source_id, source, rent_method_id, rent_method, decoration_id, decoration, uni_str, millisecond) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                    new Object[]{tempObj.getKeyword(), tempObj.getAreaId(), tempObj.getArea(), tempObj.getCircleid(), tempObj.getRentalId(), tempObj.getRental(),
                            tempObj.getProportionId(), tempObj.getProportion(), tempObj.getRoomTypeId(), tempObj.getRoomType(),
                            tempObj.getSourceId(), tempObj.getSource(), tempObj.getRentMethodId(), tempObj.getRentMethod(), tempObj.getDecorationId(), tempObj.getDecoration(), tempObj.toString(), System.currentTimeMillis()}
            );
        } else if (obj instanceof NewsSearchHistoryModel) {
            database.execSQL("delete from " + DBUtil.SEARCH_NEWS_HISTORY_TABLE + " where keyword = ?", new Object[]{obj.getKeyword()});
            database.execSQL("insert into " + DBUtil.SEARCH_NEWS_HISTORY_TABLE + "(keyword, millisecond) values(?,?)",
                    new Object[]{obj.getKeyword(), System.currentTimeMillis()});
        }

        dbUtil.closeDatabase();
    }

    /**
     * 得到唯一的一个搜索历史对象
     *
     * @param type     搜索类型
     * @param shortStr unionString
     * @return
     */
    public static SearchHistoryBaseModel getSearchHistory(SearchType type, String shortStr) {
        SearchHistoryBaseModel returnObj = new SearchHistoryBaseModel();
        SQLiteDatabase database = dbUtil.openDataBase();
        String query;
        Cursor cursor;
        String[] param = {shortStr};

        switch (type) {
            case NEW_HOUSE:
                query = "SELECT * FROM " + DBUtil.SEARCH_NEW_HOUSE_HISTORY_TABLE + " where uni_str = ?";
                cursor = database.rawQuery(query, param);

                if (cursor.getCount() > 0) {
                    cursor.moveToPosition(0);
                    NewHouseSearchHistoryModel tempObj = new NewHouseSearchHistoryModel();
                    returnObj = tempObj;

                    tempObj.setKeyword(cursor.getString(cursor.getColumnIndex("keyword")));
                    tempObj.setArea(cursor.getString(cursor.getColumnIndex("area")));
                    tempObj.setAreaId(cursor.getString(cursor.getColumnIndex("area_id")));
                    tempObj.setCircleid(cursor.getString(cursor.getColumnIndex("circle_id")));
                    tempObj.setDecoration(cursor.getString(cursor.getColumnIndex("decoration")));
                    tempObj.setDecorationId(cursor.getString(cursor.getColumnIndex("decoration_id")));
                    tempObj.setPrice(cursor.getString(cursor.getColumnIndex("price")));
                    tempObj.setPriceId(cursor.getString(cursor.getColumnIndex("price_id")));
                    tempObj.setType(cursor.getString(cursor.getColumnIndex("type")));
                    tempObj.setTypeId(cursor.getString(cursor.getColumnIndex("type_id")));
                    tempObj.setFeature(cursor.getString(cursor.getColumnIndex("feature")));
                    tempObj.setFeatureId(cursor.getString(cursor.getColumnIndex("feature_id")));
                    tempObj.setOpenTime(cursor.getString(cursor.getColumnIndex("open_time")));
                    tempObj.setOpenTimeId(cursor.getString(cursor.getColumnIndex("open_time_id")));
                }

                break;

            case OLD_HOUSE:
                query = "SELECT * FROM " + DBUtil.SEARCH_OLD_HOUSE_HISTORY_TABLE + " where uni_str = ?";
                cursor = database.rawQuery(query, param);

                if (cursor.getCount() > 0) {
                    cursor.moveToPosition(0);
                    OldHouseSearchHistoryModel tempObj = new OldHouseSearchHistoryModel();
                    returnObj = tempObj;
                    tempObj.setKeyword(cursor.getString(cursor.getColumnIndex("keyword")));
                    tempObj.setArea(cursor.getString(cursor.getColumnIndex("area")));
                    tempObj.setAreaId(cursor.getString(cursor.getColumnIndex("area_id")));
                    tempObj.setCircleid(cursor.getString(cursor.getColumnIndex("circle_id")));
                    tempObj.setDecoration(cursor.getString(cursor.getColumnIndex("decoration")));
                    tempObj.setDecorationId(cursor.getString(cursor.getColumnIndex("decoration_id")));
                    tempObj.setTotalPrice(cursor.getString(cursor.getColumnIndex("total_price")));
                    tempObj.setTotalPriceId(cursor.getString(cursor.getColumnIndex("total_price_id")));
                    tempObj.setProportion(cursor.getString(cursor.getColumnIndex("proportion")));
                    tempObj.setProportionId(cursor.getString(cursor.getColumnIndex("proportion_id")));
                    tempObj.setRoomType(cursor.getString(cursor.getColumnIndex("room_type")));
                    tempObj.setRoomTypeId(cursor.getString(cursor.getColumnIndex("room_type_id")));
                    tempObj.setSource(cursor.getString(cursor.getColumnIndex("source")));
                    tempObj.setSourceId(cursor.getString(cursor.getColumnIndex("source_id")));
                }

                break;
            case RENT_HOUSE:
                query = "SELECT * FROM " + DBUtil.SEARCH_RENT_HOUSE_HISTORY_TABLE + " where uni_str = ?";
                cursor = database.rawQuery(query, param);

                if (cursor.getCount() > 0) {
                    cursor.moveToPosition(0);
                    RentHouseSearchHistoryModel tempObj = new RentHouseSearchHistoryModel();
                    returnObj = tempObj;
                    tempObj.setKeyword(cursor.getString(cursor.getColumnIndex("keyword")));
                    tempObj.setArea(cursor.getString(cursor.getColumnIndex("area")));
                    tempObj.setAreaId(cursor.getString(cursor.getColumnIndex("area_id")));
                    tempObj.setCircleid(cursor.getString(cursor.getColumnIndex("circle_id")));
                    tempObj.setDecoration(cursor.getString(cursor.getColumnIndex("decoration")));
                    tempObj.setDecorationId(cursor.getString(cursor.getColumnIndex("decoration_id")));
                    tempObj.setRental(cursor.getString(cursor.getColumnIndex("rental")));
                    tempObj.setRentalId(cursor.getString(cursor.getColumnIndex("rental_id")));
                    tempObj.setRentMethod(cursor.getString(cursor.getColumnIndex("rent_method")));
                    tempObj.setRentMethodId(cursor.getString(cursor.getColumnIndex("rent_method_id")));
                    tempObj.setProportion(cursor.getString(cursor.getColumnIndex("proportion")));
                    tempObj.setProportionId(cursor.getString(cursor.getColumnIndex("proportion_id")));
                    tempObj.setRoomType(cursor.getString(cursor.getColumnIndex("room_type")));
                    tempObj.setRoomTypeId(cursor.getString(cursor.getColumnIndex("room_type_id")));
                    tempObj.setSource(cursor.getString(cursor.getColumnIndex("source")));
                    tempObj.setSourceId(cursor.getString(cursor.getColumnIndex("source_id")));
                }
                break;
            case INFORMATION:
                query = "SELECT keyword FROM " + DBUtil.SEARCH_NEWS_HISTORY_TABLE + " where keyword = ?";
                cursor = database.rawQuery(query, param);

                if (cursor.getCount() > 0) {
                    cursor.moveToPosition(0);
                    NewsSearchHistoryModel tempObj = new NewsSearchHistoryModel();
                    tempObj.setKeyword(cursor.getString(cursor.getColumnIndex("keyword")));
                }
                break;
        }

        dbUtil.closeDatabase();

        return returnObj;
    }

    /**
     * 根据类型和长度，返回搜索历史
     *
     * @param type
     * @param limit 返回的条目数
     * @return 搜索历史对象列表，列表可能size为0，但永远不会为null。
     */
    public static List<SearchHistoryBaseModel> getSearchHistory(SearchType type, int limit) {
        List<SearchHistoryBaseModel> returnList = new ArrayList<SearchHistoryBaseModel>();
        SQLiteDatabase database = dbUtil.openDataBase();
        String query;
        Cursor cursor;

        switch (type) {
            case NEW_HOUSE:
                query = "SELECT * FROM " + DBUtil.SEARCH_NEW_HOUSE_HISTORY_TABLE + " ORDER BY millisecond DESC LIMIT 0," + limit;
                cursor = database.rawQuery(query, null);

                if (cursor.getCount() > 0) {
                    for (int i = 0; i < cursor.getCount(); i++) {
                        cursor.moveToPosition(i);
                        NewHouseSearchHistoryModel tempObj = new NewHouseSearchHistoryModel();

                        tempObj.setKeyword(cursor.getString(cursor.getColumnIndex("keyword")));
                        tempObj.setArea(cursor.getString(cursor.getColumnIndex("area")));
                        tempObj.setAreaId(cursor.getString(cursor.getColumnIndex("area_id")));
                        tempObj.setCircleid(cursor.getString(cursor.getColumnIndex("circle_id")));
                        tempObj.setDecoration(cursor.getString(cursor.getColumnIndex("decoration")));
                        tempObj.setDecorationId(cursor.getString(cursor.getColumnIndex("decoration_id")));
                        tempObj.setPrice(cursor.getString(cursor.getColumnIndex("price")));
                        tempObj.setPriceId(cursor.getString(cursor.getColumnIndex("price_id")));
                        tempObj.setType(cursor.getString(cursor.getColumnIndex("type")));
                        tempObj.setTypeId(cursor.getString(cursor.getColumnIndex("type_id")));
                        tempObj.setFeature(cursor.getString(cursor.getColumnIndex("feature")));
                        tempObj.setFeatureId(cursor.getString(cursor.getColumnIndex("feature_id")));
                        tempObj.setOpenTime(cursor.getString(cursor.getColumnIndex("open_time")));
                        tempObj.setOpenTimeId(cursor.getString(cursor.getColumnIndex("open_time_id")));

                        returnList.add(tempObj);
                    }
                }

                break;
            case OLD_HOUSE:
                query = "SELECT * FROM " + DBUtil.SEARCH_OLD_HOUSE_HISTORY_TABLE + " ORDER BY millisecond DESC LIMIT 0," + limit;
                cursor = database.rawQuery(query, null);

                if (cursor.getCount() > 0) {
                    for (int i = 0; i < cursor.getCount(); i++) {
                        cursor.moveToPosition(i);
                        OldHouseSearchHistoryModel tempObj = new OldHouseSearchHistoryModel();

                        tempObj.setKeyword(cursor.getString(cursor.getColumnIndex("keyword")));
                        tempObj.setArea(cursor.getString(cursor.getColumnIndex("area")));
                        tempObj.setAreaId(cursor.getString(cursor.getColumnIndex("area_id")));
                        tempObj.setCircleid(cursor.getString(cursor.getColumnIndex("circle_id")));
                        tempObj.setDecoration(cursor.getString(cursor.getColumnIndex("decoration")));
                        tempObj.setDecorationId(cursor.getString(cursor.getColumnIndex("decoration_id")));
                        tempObj.setTotalPrice(cursor.getString(cursor.getColumnIndex("total_price")));
                        tempObj.setTotalPriceId(cursor.getString(cursor.getColumnIndex("total_price_id")));
                        tempObj.setProportion(cursor.getString(cursor.getColumnIndex("proportion")));
                        tempObj.setProportionId(cursor.getString(cursor.getColumnIndex("proportion_id")));
                        tempObj.setRoomType(cursor.getString(cursor.getColumnIndex("room_type")));
                        tempObj.setRoomTypeId(cursor.getString(cursor.getColumnIndex("room_type_id")));
                        tempObj.setSource(cursor.getString(cursor.getColumnIndex("source")));
                        tempObj.setSourceId(cursor.getString(cursor.getColumnIndex("source_id")));

                        returnList.add(tempObj);
                    }
                }

                break;
            case RENT_HOUSE:
                query = "SELECT * FROM " + DBUtil.SEARCH_RENT_HOUSE_HISTORY_TABLE + " ORDER BY millisecond DESC LIMIT 0," + limit;
                cursor = database.rawQuery(query, null);

                if (cursor.getCount() > 0) {
                    for (int i = 0; i < cursor.getCount(); i++) {
                        cursor.moveToPosition(i);
                        RentHouseSearchHistoryModel tempObj = new RentHouseSearchHistoryModel();

                        tempObj.setKeyword(cursor.getString(cursor.getColumnIndex("keyword")));
                        tempObj.setArea(cursor.getString(cursor.getColumnIndex("area")));
                        tempObj.setAreaId(cursor.getString(cursor.getColumnIndex("area_id")));
                        tempObj.setCircleid(cursor.getString(cursor.getColumnIndex("circle_id")));
                        tempObj.setDecoration(cursor.getString(cursor.getColumnIndex("decoration")));
                        tempObj.setDecorationId(cursor.getString(cursor.getColumnIndex("decoration_id")));
                        tempObj.setRental(cursor.getString(cursor.getColumnIndex("rental")));
                        tempObj.setRentalId(cursor.getString(cursor.getColumnIndex("rental_id")));
                        tempObj.setRentMethod(cursor.getString(cursor.getColumnIndex("rent_method")));
                        tempObj.setRentMethodId(cursor.getString(cursor.getColumnIndex("rent_method_id")));
                        tempObj.setProportion(cursor.getString(cursor.getColumnIndex("proportion")));
                        tempObj.setProportionId(cursor.getString(cursor.getColumnIndex("proportion_id")));
                        tempObj.setRoomType(cursor.getString(cursor.getColumnIndex("room_type")));
                        tempObj.setRoomTypeId(cursor.getString(cursor.getColumnIndex("room_type_id")));
                        tempObj.setSource(cursor.getString(cursor.getColumnIndex("source")));
                        tempObj.setSourceId(cursor.getString(cursor.getColumnIndex("source_id")));

                        returnList.add(tempObj);
                    }
                }
                break;
            case INFORMATION:
                query = "SELECT keyword FROM " + DBUtil.SEARCH_NEWS_HISTORY_TABLE + " ORDER BY millisecond DESC LIMIT 0," + limit;
                cursor = database.rawQuery(query, null);

                if (cursor.getCount() > 0) {
                    for (int i = 0; i < cursor.getCount(); i++) {
                        cursor.moveToPosition(i);
                        NewsSearchHistoryModel tempObj = new NewsSearchHistoryModel();

                        tempObj.setKeyword(cursor.getString(cursor.getColumnIndex("keyword")));

                        returnList.add(tempObj);
                    }
                }
                break;
        }

        dbUtil.closeDatabase();

        return returnList;
    }

    /**
     * 搜索类型
     * 1.新房
     * 2.二手房
     * 3.租房
     * 4.资讯
     */
    public static enum SearchType {
        NEW_HOUSE, OLD_HOUSE, RENT_HOUSE, INFORMATION
    }
}
