package com.vidrary.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.vidrary.classes.Article;

import java.lang.reflect.Member;
import java.util.ArrayList;

public class MainDB {
    // database constants
    public static final String DB_NAME = "vidrary.db";
    public static final int DB_VERSION = 9;

    private final String TAG = MainDB.class.getSimpleName().toString();

    public static final String WATCH_LATER_TABLE = "watch_later";

    public static final String WATCH_LATER_ID = "_id";
    public static final int WATCH_LATER_ID_INDEX = 0;

    public static final String WATCH_LATER_ARTICLE_ID = "article_id";
    public static final int WATCH_LATER_ARTICLE_ID_INDEX = 1;

    public static final String WATCH_LATER_LINK = "link";
    public static final int WATCH_LATER_LINK_INDEX = 2;

    public static final String WATCH_HISTORY_TABLE = "watch_history";

    public static final String WATCH_HISTORY_ID = "_id";
    public static final int WATCH_HISTORY_ID_INDEX = 0;

    public static final String WATCH_HISTORY_ARTICLE_ID = "article_id";
    public static final int WATCH_HISTORY_ARTICLE_ID_INDEX = 1;

    public static final String WATCH_HISTORY_LINK = "link";
    public static final int WATCH_HISTORY_LINK_INDEX = 2;

    public static final String WATCH_HISTORY_CATEGORY_ID = "category_id";
    public static final int WATCH_HISTORY_CATEGORY_ID_INDEX = 3;


    public static final String CREATE_WATCH_LATER_TABLE =
            "CREATE TABLE IF NOT EXISTS " + WATCH_LATER_TABLE + " (" +
                    WATCH_LATER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    WATCH_LATER_ARTICLE_ID + " TEXT, " +
                    WATCH_LATER_LINK + " TEXT)";

    public static final String CREATE_WATCH_HISTORY_TABLE =
            "CREATE TABLE IF NOT EXISTS " + WATCH_HISTORY_TABLE + " (" +
                    WATCH_HISTORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    WATCH_HISTORY_ARTICLE_ID + " TEXT, " +
                    WATCH_HISTORY_LINK + " TEXT, " +
                    WATCH_HISTORY_CATEGORY_ID + " INTEGER)";

    public static final String DROP_WATCH_LATER_TABLE =
            "DROP TABLE IF EXISTS " + WATCH_LATER_TABLE;

    public static final String DROP_WATCH_HISTORY_TABLE =
            "DROP TABLE IF EXISTS " + WATCH_HISTORY_TABLE;


    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name,
                        SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_WATCH_LATER_TABLE);
            db.execSQL(CREATE_WATCH_HISTORY_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(MainDB.DROP_WATCH_LATER_TABLE);
            db.execSQL(MainDB.DROP_WATCH_HISTORY_TABLE);
            onCreate(db);
        }
    }

    private SQLiteDatabase db;
    private DBHelper dbHelper;
    private Context context;

    public MainDB (Context context) {
        this.context = context;
        dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
    }

    public void openReadableDB() { db = dbHelper.getReadableDatabase(); }

    public void openWriteableDB() { db = dbHelper.getWritableDatabase(); }

    private void closeDB() {
        if (db != null)
            db.close();
    }

    public ArrayList<String> getWatchLaterList() {

        this.openReadableDB();
        Cursor cursor = db.query(WATCH_LATER_TABLE, null, null, null, null, null, null);

        ArrayList<String> watchLaterList = new ArrayList<String>();

        while (cursor.moveToNext()) {
            watchLaterList.add(cursor.getString(WATCH_LATER_ARTICLE_ID_INDEX));
        }

        if (cursor != null) cursor.close();
        this.closeDB();

        return watchLaterList;
    }

    public ArrayList<String> getWatchHistoryList() {

        this.openReadableDB();
        String orderBy = WATCH_HISTORY_ID + " DESC";
        Cursor cursor = db.query(WATCH_HISTORY_TABLE, null, null, null, null, null, orderBy);

        ArrayList<String> watchHistoryList = new ArrayList<String>();
        while (cursor.moveToNext()) {
            watchHistoryList.add(cursor.getString(WATCH_HISTORY_ARTICLE_ID_INDEX));
        }

        if (cursor != null) cursor.close();
        this.closeDB();

        return watchHistoryList;
    }

    public ArrayList<String> getWatchHistoryCategoriesList() {

        this.openReadableDB();
        String orderBy = WATCH_HISTORY_ID + " DESC";
        Cursor cursor = db.query(WATCH_HISTORY_TABLE, null, null, null, null, null, orderBy);

        ArrayList<String> watchHistoryList = new ArrayList<String>();
        while (cursor.moveToNext()) {
            watchHistoryList.add(cursor.getString(WATCH_HISTORY_CATEGORY_ID_INDEX));
        }

        if (cursor != null) cursor.close();
        this.closeDB();

        return watchHistoryList;
    }

    public long insertWatchLater(Article article){

        ContentValues cv = new ContentValues();
        cv.put(WATCH_LATER_ARTICLE_ID, article.getId());
        cv.put(WATCH_LATER_LINK, article.getLink());

        this.openWriteableDB();
        long rowID = db.insert(WATCH_LATER_TABLE, null, cv);
        this.closeDB();

        return rowID;
    }

    public long insertWatchHistory (Article article){

        ContentValues cv = new ContentValues();
        cv.put(WATCH_HISTORY_ARTICLE_ID, article.getId());
        cv.put(WATCH_HISTORY_LINK, article.getLink());
        cv.put(WATCH_HISTORY_CATEGORY_ID, article.getCategoryId());

        this.openWriteableDB();
        long rowID = db.insert(WATCH_HISTORY_TABLE, null, cv);
        this.closeDB();

        return rowID;
    }

    public int deleteWatchLater(long id) {
        String where = WATCH_LATER_ARTICLE_ID + "= ?";
        String[] whereArgs = {String.valueOf(id)};

        this.openWriteableDB();
        int rowCount = db.delete(WATCH_LATER_TABLE, where, whereArgs);
        this.closeDB();

        return rowCount;
    }


    public int deleteWatchHistory (long id) {
        String where = WATCH_HISTORY_ARTICLE_ID + "= ?";
        String[] whereArgs = {String.valueOf(id)};

        this.openWriteableDB();
        int rowCount = db.delete(WATCH_HISTORY_TABLE, where, whereArgs);
        this.closeDB();

        return rowCount;
    }


}
