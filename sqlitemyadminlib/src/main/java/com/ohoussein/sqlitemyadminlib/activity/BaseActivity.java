package com.ohoussein.sqlitemyadminlib.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.ohoussein.sqlitemyadminlib.R;
import com.ohoussein.sqlitemyadminlib.utils.Constants;

/**
 * Created by houssein.elouerghemmi on 09/12/2016.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constants.RESULT_CODE_CLOSE) {
            setResult(Constants.RESULT_CODE_CLOSE);
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            onBackPressed();

        } else if (i == R.id.action_close) {
            setResult(Constants.RESULT_CODE_CLOSE);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}
