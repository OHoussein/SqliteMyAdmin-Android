package com.ohoussein.sqlitemyadmin;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ohoussein.sqlitemyadminlib.activity.SqliteMyAdminActivity;
import com.ohoussein.sqlitemyadminlib.utils.SqliteUtils;
import com.thedeanda.lorem.LoremIpsum;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final DBHelper dbHelper = DBHelper.getInstance(getApplication());

        findViewById(R.id.btn_add_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        addFakeData(dbHelper.getWritableDatabase());
                    }
                }).start();
            }
        });
        final SqliteUtils sqliteUtils = new SqliteUtils(this);
        findViewById(R.id.btn_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        sqliteUtils.useDb(DBHelper.DB_NAME);
                        dbHelper.clearData();
                    }
                }).start();
            }
        });
        findViewById(R.id.btn_sqlite_my_admin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               SqliteMyAdminActivity.navigate(MainActivity.this);
            }
        });
    }

    private void addFakeData(SQLiteDatabase db) {
        try {
            for (int i = 0; i < 5; i++) {
                ContentValues userValues = new ContentValues();
                userValues.put(DBHelper.COL_USER_NAME, LoremIpsum.getInstance().getName());
                long userRowId = db.insert(DBHelper.TABLE_USER, null, userValues);

                ContentValues taskValues = new ContentValues();
                taskValues.put(DBHelper.COL_TASK_TITLE, LoremIpsum.getInstance().getWords(2));
                taskValues.put(DBHelper.COL_TASK_USER_ID, userRowId);
                taskValues.put(DBHelper.COL_TASK_CREATED_AT, System.currentTimeMillis());
                db.insert(DBHelper.TABLE_TASK, null, taskValues);
            }
        } finally {
            db.close();
        }
    }
}
