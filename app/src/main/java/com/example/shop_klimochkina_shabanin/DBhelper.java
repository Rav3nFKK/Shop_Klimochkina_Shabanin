package com.example.shop_klimochkina_shabanin;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.Nullable;


public class DBhelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 13;
    public static final String DATABASE_NAME = "storeDB";
    public static final String TABLE_PRODUCT = "contacts";//
    public static final String TABLE_USERS = "сс";
    public static final String KEY_ID = "_id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_COST = "cost";
    public static final String KEY_LOGIN = "login";
    public static final String KEY_PASSWORD = "pass";
    private static final String CREATE_TABLE = "create table " + TABLE_PRODUCT + "(" + KEY_ID
            + " integer primary key," + " text," + KEY_TITLE + " text," + KEY_COST + " integer" + ")";
    private static final String CREATE_TABLE2 = "create table " + TABLE_USERS + "(" + KEY_LOGIN + " text," + KEY_PASSWORD + " text" + ")";

    public DBhelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_TABLE2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists " + TABLE_PRODUCT);
        db.execSQL("drop table if exists " + TABLE_USERS);
        onCreate(db);
    }
}