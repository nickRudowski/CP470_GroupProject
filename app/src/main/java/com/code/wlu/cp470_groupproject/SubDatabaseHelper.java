package com.code.wlu.cp470_groupproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SubDatabaseHelper extends SQLiteOpenHelper {
    public static String ACTIVITY_NAME = "SubDatabaseHelper";
    public static String DATABASE_NAME = "Subscriptions.db";
    private static int VERSION_NUM = 4;

    public static final String KEY_ID = "id";
    public static final String KEY_MESSAGE = "SUBSCRIPTION";

    public static final String TABLE_NAME = "SUBSCRIPTIONS";

    SQLiteDatabase db;

    public SubDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
        db = this.getWritableDatabase();
        Log.i(ACTIVITY_NAME, "Constructor");

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(ACTIVITY_NAME, "AM I HERE?");
        String DB_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" + KEY_ID + " integer primary key autoincrement, " + KEY_MESSAGE + " BLOB);";
        Log.i(ACTIVITY_NAME, "Calling onCreate");
        db.execSQL(DB_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        Log.i(ACTIVITY_NAME, "Calling onUpgrade, oldVersion=" + oldVer + " newVersion=" + newVer);
        String cmd = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(cmd);
        onCreate(db);
    }
}
