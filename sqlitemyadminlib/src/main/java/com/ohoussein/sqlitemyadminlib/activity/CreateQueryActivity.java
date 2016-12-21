package com.ohoussein.sqlitemyadminlib.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.ohoussein.sqlitemyadminlib.R;
import com.ohoussein.sqlitemyadminlib.utils.Constants;

import static com.ohoussein.sqlitemyadminlib.utils.Constants.KEY_DB_NAME;
import static com.ohoussein.sqlitemyadminlib.utils.Constants.KEY_QUERY_RESULT_NEW_ACTIVITY;

public class CreateQueryActivity extends BaseActivity {

    private EditText etQuery;
    private boolean resultINewWindow = false;


    public static void navigate(Activity activity, String dbName, String query, boolean resultINewWindow) {
        Intent intent = new Intent(activity, CreateQueryActivity.class);
        intent.putExtra(KEY_QUERY_RESULT_NEW_ACTIVITY, resultINewWindow);
        intent.putExtra(Constants.KEY_QUERY, query);
        intent.putExtra(KEY_DB_NAME, dbName);
        activity.startActivityForResult(intent, Constants.REQUEST_QUERY);
    }

    public static void navigate(Activity activity, String dbName, boolean resultINewWindow) {
        Intent intent = new Intent(activity, CreateQueryActivity.class);
        intent.putExtra(KEY_QUERY_RESULT_NEW_ACTIVITY, resultINewWindow);
        intent.putExtra(KEY_DB_NAME, dbName);
        activity.startActivityForResult(intent, Constants.REQUEST_QUERY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_query);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        resultINewWindow = getIntent().getBooleanExtra(Constants.KEY_QUERY_RESULT_NEW_ACTIVITY, resultINewWindow);
        etQuery = (EditText) findViewById(R.id.input_query);

        String query = getIntent().getStringExtra(Constants.KEY_QUERY);
        etQuery.setText(query);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_query_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_execute) {
            String query = etQuery.getText().toString();
            if (resultINewWindow) {
                QueryResultActivity.navigateByQuery(this, getIntent().getStringExtra(KEY_DB_NAME), query);
            } else {
                Intent result = new Intent();
                result.putExtra(Constants.KEY_QUERY, query);
                setResult(RESULT_OK, result);
            }
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
