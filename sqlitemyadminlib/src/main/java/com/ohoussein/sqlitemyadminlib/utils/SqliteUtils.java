package com.ohoussein.sqlitemyadminlib.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ohoussein.sqlitemyadminlib.model.DbCol;
import com.ohoussein.sqlitemyadminlib.model.DbRow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by houssein.elouerghemmi on 08/12/2016.
 */

public class SqliteUtils {

    private final static String TAG = SqliteUtils.class.getSimpleName();
    public final static int DEFAULT_LIMIT = 30;
    private Context mContext;
    private SQLiteDatabase mDB;

    public SqliteUtils(Context context) {
        this.mContext = context;
    }

    public String[] getDatabases() {
        return mContext.getApplicationContext().databaseList();
    }

    public void useDb(String name) {
        mDB = SQLiteDatabase.openDatabase(mContext.getDatabasePath(name).getPath(), null, 0);
    }

    public void closeCurrentDb() {
        checkUseDb();
        mDB.close();
        mDB = null;
    }

    public List<String> getTables() {
        checkUseDb();
        ArrayList<String> tables = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = mDB.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    tables.add(cursor.getString(0));
                    cursor.moveToNext();
                }
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return tables;
    }

    @SuppressLint("DefaultLocale")
    public List<DbRow> getQueryResults(String query) {
        checkUseDb();
        ArrayList<DbRow> rows = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = mDB.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    DbRow dbRow = new DbRow();

                    for (String colName : cursor.getColumnNames()) {
                        dbRow.getCols().add(new DbCol(colName));
                    }
                    for (DbCol dbCol : dbRow.getCols()) {
                        String colValue = cursor.getString(cursor.getColumnIndex(dbCol.getName()));
                        dbCol.setValue(colValue);
                    }
                    rows.add(dbRow);
                    cursor.moveToNext();
                }
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return rows;
    }

    public String getTableSchema(String tableName) {
        checkUseDb();
        ArrayList<String> cols = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = mDB.rawQuery("SELECT sql FROM sqlite_master WHERE tbl_name = '" + tableName + "' AND type = 'table'", null);

            if (cursor.moveToFirst()) {
                return cursor.getString(0);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return null;
    }

    public List<String> getTableCols(String tableName) {
        checkUseDb();
        ArrayList<String> cols = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = mDB.rawQuery("pragma table_info('" + tableName + "')", null);

            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    cols.add(cursor.getString(1));
                    cursor.moveToNext();
                }
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return cols;
    }

    public long getTableCountRows(String tableName) {
        checkUseDb();
        Cursor cursor = null;
        try {
            cursor = mDB.rawQuery(String.format("SELECT COUNT(*) FROM %s ", tableName), null);
            if (cursor.moveToFirst()) {
                return cursor.getLong(0);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return 0;
    }

    private void checkUseDb() {
        if (mDB == null)
            throw new RuntimeException("You must choose a db");
    }
}
