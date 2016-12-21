package com.ohoussein.sqlitemyadminlib.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by houssein.elouerghemmi on 09/12/2016.
 */

public class SimpleAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public SimpleAdapter(int layoutResId) {
        super(layoutResId, null);
    }

    public SimpleAdapter(int layoutResId, List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder vh, String s) {
        vh.setText(android.R.id.text1, s)
                .addOnClickListener(android.R.id.text1);
    }
}
