package com.anl.wxb.jieqi.db;

import android.content.Context;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;


/**
 * Created by admin on 2015/8/11.
 */
public class Db extends SQLiteOpenHelper{
    private String TAG = "Db";

    public Db(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE user (_id INTEGER PRIMARY KEY AUTOINCREMENT, name, text, explain)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
