package com.zhijia.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.zhijia.Global;

import java.io.File;

/**
 * 数据库工具类
 */
public class DBUtil {

    public static final String DB_NAME = "zhi_jia.db";

    public static final String CITY_TABLE = "all_city";
    private static final String CITY_TABLE_CREATE_SQL = "CREATE TABLE IF NOT EXISTS " + CITY_TABLE + " (cityid varchar(20) primary key, letter varchar(5), letters varchar(40), name varchar(30), hot varchar(5), laticoor varchar(20), longcoor varchar(20), status varchar(5))";
    public static final String SEARCH_NEW_HOUSE_HISTORY_TABLE = "search_new_house_history";
    private static final String SEARCH_NEW_HOUSE_HISTORY_TABLE_CREATE_SQL = "CREATE TABLE IF NOT EXISTS " + SEARCH_NEW_HOUSE_HISTORY_TABLE + " (keyword varchar(100), area_id varchar(20), area varchar(100), circle_id varchar(20), price_id varchar(20), price varchar(100), type_id varchar(20), type varchar(100), feature_id varchar(20), feature varchar(100), decoration_id varchar(20), decoration varchar(100), open_time_id varchar(20), open_time varchar(100), uni_str varchar(800), millisecond NUMERIC)";
    public static final String SEARCH_OLD_HOUSE_HISTORY_TABLE = "search_old_house_history";
    private static final String SEARCH_OLD_HOUSE_HISTORY_TABLE_CREATE_SQL = "CREATE TABLE IF NOT EXISTS " + SEARCH_OLD_HOUSE_HISTORY_TABLE + " (keyword varchar(100), area_id varchar(20), area varchar(100), circle_id varchar(20), total_price_id varchar(20), total_price varchar(100), proportion_id varchar(20), proportion varchar(100), room_type_id varchar(20), room_type varchar(100), source_id varchar(20), source varchar(100), decoration_id varchar(20), decoration varchar(100), uni_str varchar(800), millisecond NUMERIC)";
    public static final String SEARCH_RENT_HOUSE_HISTORY_TABLE = "search_rent_house_history";
    private static final String SEARCH_RENT_HOUSE_HISTORY_TABLE_CREATE_SQL = "CREATE TABLE IF NOT EXISTS " + SEARCH_RENT_HOUSE_HISTORY_TABLE + " (keyword varchar(100), area_id varchar(20), area varchar(100), circle_id varchar(20), rental_id varchar(20), rental varchar(100), proportion_id varchar(20), proportion varchar(100), room_type_id varchar(20), room_type varchar(100), source_id varchar(20), source varchar(100), rent_method_id varchar(20), rent_method varchar(100), decoration_id varchar(20), decoration varchar(100), uni_str varchar(800), millisecond NUMERIC)";
    public static final String SEARCH_NEWS_HISTORY_TABLE = "search_news_history";
    private static final String SEARCH_NEWS_HISTORY_TABLE_CREATE_SQL = "CREATE TABLE IF NOT EXISTS " + SEARCH_NEWS_HISTORY_TABLE + " (keyword varchar(100), millisecond NUMERIC)";
    public static final String BROWSE_HISTORY_TABLE = "browse_history";
    private static final String BROWSE_HISTORY_TABLE_CREATE_SQL = "CREATE TABLE IF NOT EXISTS " + BROWSE_HISTORY_TABLE + " (hid varchar(25), type varchar(20), millisecond NUMERIC)";

    private Context context;

    private SQLiteDatabase database;

    public DBUtil(Context context) {
        this.context = context;
    }

    /**
     * 被调用方法
     */
    public SQLiteDatabase openDataBase() {
        this.database = this.openDataBase(Global.DB_PATH + "/" + DB_NAME);

        return database;
    }

    /**
     * 打开数据库
     *
     * @param dbFile
     * @return SQLiteDatabase
     */
    private SQLiteDatabase openDataBase(String dbFile) {
        Log.d(Global.LOG_TAG, dbFile);
        File file = new File(dbFile);

        if (!file.exists()) {
            database = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
            this.database.close();
        }
        database = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        database.execSQL(CITY_TABLE_CREATE_SQL);
        database.execSQL(SEARCH_NEW_HOUSE_HISTORY_TABLE_CREATE_SQL);
        database.execSQL(SEARCH_OLD_HOUSE_HISTORY_TABLE_CREATE_SQL);
        database.execSQL(SEARCH_RENT_HOUSE_HISTORY_TABLE_CREATE_SQL);
        database.execSQL(SEARCH_NEWS_HISTORY_TABLE_CREATE_SQL);
        database.execSQL(BROWSE_HISTORY_TABLE_CREATE_SQL);

        return database;
    }

    /**
     * 关闭数据库
     */
    public void closeDatabase() {
        if (database != null && database.isOpen()) {
            this.database.close();
        }
    }
}
