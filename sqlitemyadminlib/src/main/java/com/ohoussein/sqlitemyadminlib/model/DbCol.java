package com.ohoussein.sqlitemyadminlib.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by houssein.elouerghemm on 08/12/2016.
 */

public class DbCol implements Parcelable {

    private String name;
    private String value;

    public DbCol() {
    }

    public DbCol(String name) {
        this.name = name;
    }

    public DbCol(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "DbCol{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.value);
    }

    protected DbCol(Parcel in) {
        this.name = in.readString();
        this.value = in.readString();
    }

    public static final Parcelable.Creator<DbCol> CREATOR = new Parcelable.Creator<DbCol>() {
        @Override
        public DbCol createFromParcel(Parcel source) {
            return new DbCol(source);
        }

        @Override
        public DbCol[] newArray(int size) {
            return new DbCol[size];
        }
    };
}
