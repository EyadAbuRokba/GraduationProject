package com.example.eyad.ministryofinteriorproject.Model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.constraint.Placeholder;

public class Fisher  {

    private String id;
    private String name;
    private String governorate;
    private String Date;

    public Fisher(String id, String name, String governorate) {
        this.id = id;
        this.name = name;
        this.governorate = governorate;
    }

    public static final String COL_ID="id";
    public static final String COL_NAME="name";
    public static final String COL_GOVERNORATE="governorate";

    public static final String TABLE_NAME="fishers";
    public static final String DB_NAME="fishDB";

    public static final String CREATE_TABLE = "" +
            "create table fishers(id integer primary key," +
            " name text not null, governorate text not null)";

    public Fisher() {
    }

    public String getGovernorate() {
        return governorate;
    }

    public void setGovernorate(String governorate) {
        this.governorate = governorate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
