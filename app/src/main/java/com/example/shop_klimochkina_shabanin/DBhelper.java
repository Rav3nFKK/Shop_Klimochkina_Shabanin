package com.example.shop_klimochkina_shabanin;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.Nullable;


public class DBhelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 4    ;
    public static final String DATABASE_NAME = "storeDB";
    public static final String TABLE_PRODUCT = "contacts";//

    public static final String KEY_ID = "_id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_COST = "cost";

    public DBhelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_PRODUCT + "(" + KEY_ID
                + " integer primary key,"  + " text," + KEY_TITLE +" text," + KEY_COST + " integer" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists " + TABLE_PRODUCT);
        onCreate(db);
    }
}