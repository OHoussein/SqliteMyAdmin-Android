package com.ohoussein.sqlitemyadminlib.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.ohoussein.sqlitemyadminlib.R;
import com.ohoussein.sqlitemyadminlib.adapter.SimpleAdapter;
import com.ohoussein.sqlitemyadminlib.utils.SqliteUtils;

import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SqliteMyAdminActivity extends BaseActivity {

    public static void navigate(Activity activity) {
        activity.startActivity(new Intent(activity, SqliteMyAdminActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_list);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SqliteUtils mSqliteUtils = new SqliteUtils(this);

        SimpleAdapter adapter = new SimpleAdapter(R.layout.item_database);

        RecyclerView rv = ButterKnife.findById(this, R.id.rv_db);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        DividerItemDecoration divider = new DividerItemDecoration(this,
                llm.getOrientation());
        rv.addItemDecoration(divider);
        rv.setAdapter(adapter);

        rv.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                TableListActivity.navigate(SqliteMyAdminActivity.this, ((SimpleAdapter) baseQuickAdapter).getData().get(position));
            }
        });
        Observable.create((Observable.OnSubscribe<List<String>>) sub -> {
            try {
                final List<String> listDb = Arrays.asList(mSqliteUtils.getDatabases());
                sub.onNext(listDb);
            } catch (Exception e) {
                e.printStackTrace();
                sub.onError(e);
                return;
            }
            sub.onCompleted();
        })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(adapter::setNewData,
                        throwable -> Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show())
        ;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.default_menu, menu);
        return true;
    }
}
