package com.anl.wxb.jieqi.db;

import android.content.Context;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;
import net.sqlcipher.database.SQLiteDatabase.CursorFactory;


/**
 * Created by admin on 2015/8/11.
 */
public class Db extends SQLiteOpenHelper{
    private String TAG = "Db";

//    public Db(Context context) {
//        super(context, "jieqi.db", null , 1);
//        Log.i(TAG, "db");
//    }
    public Db(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

        Log.i(TAG, "db");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE user (_id INTEGER PRIMARY KEY AUTOINCREMENT, name, text, explain)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
