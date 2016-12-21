package com.ohoussein.sqlitemyadminlib.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ohoussein.sqlitemyadminlib.R;
import com.ohoussein.sqlitemyadminlib.model.DbCol;
import com.ohoussein.sqlitemyadminlib.model.DbRow;

import java.util.List;

import rx.functions.Action2;

/**
 * Created by houssein.elouerghemmi on 09/12/2016.
 */

public class RowAdapter extends BaseQuickAdapter<DbRow, RowAdapter.ViewHolder> {

    private int countCols = 0;
    private Action2<View, DbRow> onItemClick;

    public RowAdapter() {
        super(R.layout.item_table_row, null);
    }

    public RowAdapter(List<DbRow> data, int countCols) {
        super(R.layout.item_table_row, data);
        this.countCols = countCols;
    }

    public void setOnItemClick(Action2<View, DbRow> onItemClick) {
        this.onItemClick = onItemClick;
    }

    public void setCountCols(int countCols) {
        this.countCols = countCols;
    }

    public int getCountCols() {
        return countCols;
    }

    @Override
    protected void convert(RowAdapter.ViewHolder vh, DbRow row) {
        Context ctx = vh.convertView.getContext();
        if (vh.mColsContainer.getChildCount() != countCols)
            setupColsContainer(vh);
        for (int i = 0; i < row.getCols().size(); i++) {
            DbCol col = row.getCols().get(i);
            TextView tv = (TextView) vh.mColsContainer.getChildAt(i);
            tv.setText(ctx.getString(R.string.col_value, col.getName(), col.getValue()));
            tv.setEllipsize(TextUtils.TruncateAt.END);
        }
        vh.itemView.setOnClickListener(view -> {
            if (onItemClick != null) {
                onItemClick.call(view, row);
            }
        });
    }

    private void setupColsContainer(RowAdapter.ViewHolder vh) {
        vh.mColsContainer.removeAllViews();
        for (int i = 0; i < countCols; i++) {
            LayoutInflater.from(vh.mColsContainer.getContext()).inflate(R.layout.item_column, vh.mColsContainer, true);
        }
    }

    @Override
    protected RowAdapter.ViewHolder createBaseViewHolder(View parent) {
        ViewGroup container = (ViewGroup) parent.findViewById(R.id.container);
        return new ViewHolder(parent, container);
    }

    static class ViewHolder extends BaseViewHolder {

        final ViewGroup mColsContainer;

        ViewHolder(View content, ViewGroup colsContainer) {
            super(content);
            mColsContainer = colsContainer;
        }
    }
}
