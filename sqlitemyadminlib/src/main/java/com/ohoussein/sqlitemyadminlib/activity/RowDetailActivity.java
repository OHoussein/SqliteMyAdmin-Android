package com.ohoussein.sqlitemyadminlib.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ohoussein.sqlitemyadminlib.R;
import com.ohoussein.sqlitemyadminlib.model.DbCol;
import com.ohoussein.sqlitemyadminlib.model.DbRow;
import com.ohoussein.sqlitemyadminlib.utils.Constants;

public class RowDetailActivity extends BaseActivity {

    public static void navigate(Activity activity, DbRow row) {
        Intent intent = new Intent(activity, RowDetailActivity.class);
        intent.putExtra(Constants.KEY_ROW, row);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_row_detail);
        DbRow dbRow = getIntent().getParcelableExtra(Constants.KEY_ROW);
        ViewGroup container = (ViewGroup) findViewById(R.id.container);
        for (int i = 0; i < dbRow.getCols().size(); i++) {
            LayoutInflater.from(this).inflate(R.layout.item_column, container, true);

            DbCol col = dbRow.getCols().get(i);
            TextView tv = (TextView) container.getChildAt(i);
            tv.setMaxLines(Integer.MAX_VALUE);
            tv.setText(getString(R.string.col_value, col.getName(), col.getValue()));
        }
        container.invalidate();
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.default_menu, menu);
        return true;
    }
}
