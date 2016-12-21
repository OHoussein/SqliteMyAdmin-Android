package com.ohoussein.sqlitemyadminlib.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.ohoussein.sqlitemyadminlib.R;
import com.ohoussein.sqlitemyadminlib.adapter.RowAdapter;
import com.ohoussein.sqlitemyadminlib.adapter.SimpleAdapter;
import com.ohoussein.sqlitemyadminlib.model.DbRow;
import com.ohoussein.sqlitemyadminlib.utils.Constants;
import com.ohoussein.sqlitemyadminlib.utils.DialogFactory;
import com.ohoussein.sqlitemyadminlib.utils.SqliteUtils;

import java.util.List;

import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.ohoussein.sqlitemyadminlib.utils.Constants.KEY_DB_NAME;
import static com.ohoussein.sqlitemyadminlib.utils.Constants.KEY_QUERY;
import static com.ohoussein.sqlitemyadminlib.utils.Constants.KEY_TABLE_NAME;

public class QueryResultActivity extends BaseActivity {

    private TextView tvQuery;
    private SqliteUtils sqliteUtils;
    private RowAdapter adapter;
    private String mQuery;
    private String dbName;
    private String tableName;

    public static void navigateByTable(Activity activity, String dbName, String tableName) {
        Intent intent = new Intent(activity, QueryResultActivity.class);
        intent.putExtra(KEY_DB_NAME, dbName);
        intent.putExtra(KEY_TABLE_NAME, tableName);
        activity.startActivityForResult(intent, Constants.REQUEST_TABLE_ROWS);
    }

    public static void navigateByQuery(Activity activity, String dbName, String query) {
        Intent intent = new Intent(activity, QueryResultActivity.class);
        intent.putExtra(KEY_DB_NAME, dbName);
        intent.putExtra(KEY_QUERY, query);
        activity.startActivityForResult(intent, Constants.REQUEST_TABLE_ROWS);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rows);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbName = getIntent().getStringExtra(KEY_DB_NAME);
        tableName = getIntent().getStringExtra(KEY_TABLE_NAME);
        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_QUERY))
            mQuery = savedInstanceState.getString(KEY_QUERY);
        else
            mQuery = getIntent().getStringExtra(KEY_QUERY);

        RecyclerView rv = ButterKnife.findById(this, R.id.rv_rows);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        DividerItemDecoration divider = new DividerItemDecoration(this,
                llm.getOrientation());
        rv.addItemDecoration(divider);

        View header = LayoutInflater.from(this).inflate(R.layout.query_text, rv, false);
        tvQuery = (TextView) header.findViewById(R.id.text_query);
        header.setOnClickListener(view -> toCreateQueryActivity());

        printTitle();

        sqliteUtils = new SqliteUtils(this);
        sqliteUtils.useDb(dbName);
        adapter = new RowAdapter();
        rv.setAdapter(adapter);
        adapter.addHeaderView(header);

        if (mQuery == null) {
            mQuery = String.format("SELECT * FROM `%s`", tableName);
        }
        showQuery();
        load();
        adapter.setOnItemClick((view, row) -> RowDetailActivity.navigate(QueryResultActivity.this, row));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_QUERY, mQuery);
        super.onSaveInstanceState(outState);
    }

    private void printTitle() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(dbName + " / " + (tableName != null ? tableName : mQuery));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data.hasExtra(Constants.KEY_QUERY)) {
            mQuery = data.getStringExtra(Constants.KEY_QUERY);
            tableName = null;
            showQuery();
            printTitle();
            load();
        }
    }

    private void load() {
        if (mQuery == null) {
            adapter.setNewData(null);
            return;
        }
        Observable
                .create((Observable.OnSubscribe<List<DbRow>>) sub -> {
                    try {
                        final List<DbRow> rows = sqliteUtils.getQueryResults(mQuery);

                        if (rows != null && !rows.isEmpty()) {
                            adapter.setCountCols(rows.get(0).getCols().size());
                        }
                        sub.onNext(rows);
                    } catch (Exception e) {
                        e.printStackTrace();
                        sub.onError(e);
                        return;
                    }
                    sub.onCompleted();
                })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dbRows -> {
                            adapter.setNewData(dbRows);
                        },
                        throwable -> {
                            adapter.setNewData(null);
                            DialogFactory.createGenericErrorDialog(this, throwable.getMessage()).show();
                        })
        ;
    }

    private void showQuery() {
        tvQuery.setText(mQuery);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_create_query) {
            toCreateQueryActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.db_menu, menu);
        return true;
    }

    private void toCreateQueryActivity() {
        CreateQueryActivity.navigate(this, dbName, mQuery, false);
    }
}