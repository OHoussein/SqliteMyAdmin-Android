package com.ohoussein.sqlitemyadmin;


import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by houssein.elouerghemmi on 08/12/2016.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = DBHelper.class.getSimpleName();

    private static DBHelper instance;
    public static final String DB_NAME = "test.db";
    private static final int DB_VERSION = 1;

    public static final String TABLE_TASK = "task";
    public static final String TABLE_USER = "user";

    public static final String COL_TASK_TITLE = "title";
    public static final String COL_TASK_CREATED_AT = "created_at";
    public static final String COL_TASK_TODO_ID = "_id";
    public static final String COL_TASK_USER_ID = "user_id";

    public static final String COL_USER_ID = "_id";
    public static final String COL_USER_NAME = "name";

    public static synchronized DBHelper getInstance(Application context) {
        if (instance == null) {
            instance = new DBHelper(context);
        }
        return instance;
    }

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Creating tables");
        createUserTable(db);
        createTableTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void createTableTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_TASK + " (" + COL_TASK_TODO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_TASK_USER_ID + " INTEGER," +
                COL_TASK_TITLE + " TEXT," +
                COL_TASK_CREATED_AT + " INTEGER);";
        db.execSQL(sql);
    }

    private void createUserTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_USER + " (" + COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_USER_NAME + " TEXT);";
        db.execSQL(sql);
    }

    public void clearData() {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.execSQL("DELETE FROM " + TABLE_TASK);
            db.execSQL("DELETE FROM " + TABLE_USER);
        } finally {
            db.close();
        }
    }

    public void reset() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }

}