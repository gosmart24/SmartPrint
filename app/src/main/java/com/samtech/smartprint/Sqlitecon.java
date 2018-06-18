package com.samtech.smartprint;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by CyberTech on 6/4/2013.
 */
public class Sqlitecon extends SQLiteOpenHelper {

    public final static String db_name = "user_db";
    public final static String tb_name = "user_tb";
    public final static String col_id = "Id";
    public final static String col_accountno = "AccountnO";
    public final static String col_username = "Usernsme";
    public final static String col_pin = "Pin";
    SQLiteDatabase database;
    public Sqlitecon(Context context) {
        super(context, db_name, null, 1);
        database = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + tb_name + "(" + col_id + "INTEGER AUTOINCREAMENT, " + col_accountno + " INTEGER PRIMARY KEY," + col_username + " TEXT ," + col_pin + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST " + tb_name);
        onCreate(db);
    }

    // this method is use to insert directly to sqlite Database.
    public boolean insert(int Account, String username, String pass) {
        ContentValues cv = new ContentValues();
        cv.put(col_accountno, Account);
        cv.put(col_username, username);
        cv.put(col_pin, pass);
        long result = database.insert(tb_name, null, cv);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getData(){
        Cursor result = database.rawQuery("select * from " + tb_name, null);
        return result;
    }
}
