package com.ohoussein.sqlitemyadminlib.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.ohoussein.sqlitemyadminlib.R;
import com.ohoussein.sqlitemyadminlib.adapter.SimpleAdapter;
import com.ohoussein.sqlitemyadminlib.utils.Constants;
import com.ohoussein.sqlitemyadminlib.utils.DialogFactory;
import com.ohoussein.sqlitemyadminlib.utils.SqliteUtils;

import java.util.List;

import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.ohoussein.sqlitemyadminlib.utils.Constants.KEY_DB_NAME;

public class TableListActivity extends BaseActivity {

    private String dbName;

    public static void navigate(Activity activity, String dbName) {
        Intent intent = new Intent(activity, TableListActivity.class);
        intent.putExtra(KEY_DB_NAME, dbName);
        activity.startActivityForResult(intent, Constants.REQUEST_TABLE_LIST);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_list);

        dbName = getIntent().getStringExtra(KEY_DB_NAME);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(dbName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        SqliteUtils mSqliteUtils = new SqliteUtils(this);
        try {
            mSqliteUtils.useDb(dbName);
        } catch (SQLiteCantOpenDatabaseException e) {
            DialogFactory.createGenericErrorDialog(this, e.getMessage()).show();
            e.printStackTrace();
            return;
        }
        SimpleAdapter adapter = new SimpleAdapter(R.layout.item_table);

        RecyclerView rv = ButterKnife.findById(this, R.id.rv_tables);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        DividerItemDecoration divider = new DividerItemDecoration(this,
                llm.getOrientation());
        rv.addItemDecoration(divider);
        rv.setAdapter(adapter);

        rv.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                QueryResultActivity.navigateByTable(TableListActivity.this, dbName, (String) baseQuickAdapter.getData().get(position));
            }
        });
        Observable.create((Observable.OnSubscribe<List<String>>) sub -> {
            try {
                final List<String> tables = mSqliteUtils.getTables();
                sub.onNext(tables);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_create_query) {
            CreateQueryActivity.navigate(this, dbName, true);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.db_menu, menu);
        return true;
    }
}
