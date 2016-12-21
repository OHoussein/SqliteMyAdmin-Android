package com.ohoussein.sqlitemyadminlib.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by houssein.elouerghemm on 08/12/2016.
 */

public class DbRow implements Parcelable {

    private ArrayList<DbCol> cols = new ArrayList<>();

    public ArrayList<DbCol> getCols() {
        return cols;
    }

    public void setCols(ArrayList<DbCol> cols) {
        this.cols = cols;
    }

    @Override
    public String toString() {
        return "DbRow{" +
                "cols=" + cols +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.cols);
    }

    public DbRow() {
    }

    protected DbRow(Parcel in) {
        this.cols = in.createTypedArrayList(DbCol.CREATOR);
    }

    public static final Parcelable.Creator<DbRow> CREATOR = new Parcelable.Creator<DbRow>() {
        @Override
        public DbRow createFromParcel(Parcel source) {
            return new DbRow(source);
        }

        @Override
        public DbRow[] newArray(int size) {
            return new DbRow[size];
        }
    };
}
