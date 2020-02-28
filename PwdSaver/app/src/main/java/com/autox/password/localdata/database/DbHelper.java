package com.autox.password.localdata.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import androidx.annotation.Nullable;

import com.autox.password.EApplication;
import com.autox.password.localdata.database.items.PwdItem;
import com.autox.password.localdata.sharedprefs.SharedPrefKeys;
import com.autox.password.localdata.sharedprefs.SharedPrefUtils;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {
    private static final String TAG = DbHelper.class.getName();

    public static final String DB_NAME_PWD = "pwd";

    private static volatile DbHelper sInstance;

    public static DbHelper getInstance() {
        if (sInstance == null) {
            synchronized (DbHelper.class) {
                if (sInstance == null) {
                    sInstance = new DbHelper(EApplication.getContext(), "DbHelper", null, DbConstant.DB_VERSION);
                }
            }
        }
        return sInstance;
    }

    private DbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    private DbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // -------------- PrayRecord ------------------------
        //  id: text
        //  buddhaName: text
        //  prayTime: text
        //  prayIng: text
        Log.e(DbHelper.class.getName(), "Echo , onCreate");
        db.execSQL("Create Table if not exists " + DB_NAME_PWD + "(id Integer primary key autoincrement, type text, platform text, account text, pwd text, saveTime text, upload Integer)");
        SharedPrefUtils.setInteger(SharedPrefKeys.KEY_DB_VERSION, DbConstant.DB_VERSION);
    }

    public void uploadToServer(int type, String time) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("upload", 1);
        db.update(DB_NAME_PWD, values, "saveTime=?", new String[]{time});
        db.close();
    }

    public void insert(PwdItem item, boolean upload) {
        int uploadInt = 0;
        if (upload) {
            uploadInt = 1;
        }
        SQLiteDatabase db = getWritableDatabase();
        String sql = "select * from " + DB_NAME_PWD + " where platform=\"" + item.platform() + "\" and account=\"" + item.account() + "\"";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                if (item.pwd().equals(cursor.getString(4))) {
                    db.close();
                    return;
                }
            }
            update(db, item);
            db.close();
            return;
        }
        ContentValues values = new ContentValues();
        values.put("type", item.type() + "");
        values.put("platform", item.platform() + "");
        values.put("account", item.account() + "");
        values.put("pwd", item.pwd() + "");
        values.put("saveTime", item.saveTime() + "");
        values.put("upload", uploadInt);
        db.insert(DB_NAME_PWD, null, values);
        db.close();
    }

    public List<PwdItem> getPwdList() {
        List<PwdItem> items = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DB_NAME_PWD, new String[]{});
        while (cursor.moveToNext()) {
            PwdItem item = new PwdItem(
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    Long.parseLong(cursor.getString(5))
            );
            items.add(item);
        }
        return items;
    }

    public List<PwdItem> getPwdSizeByType(String type) {
        List<PwdItem> items = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DB_NAME_PWD + " where type=\"" + type + "\"", new String[]{});
        while (cursor.moveToNext()) {
            PwdItem item = new PwdItem(
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    Long.parseLong(cursor.getString(5))
            );
            items.add(item);
        }
        return items;
    }

    public void update(SQLiteDatabase db, PwdItem item) {
        ContentValues values = new ContentValues();
        values.put("type", item.type() + "");
        values.put("platform", item.platform() + "");
        values.put("account", item.account() + "");
        values.put("pwd", item.pwd() + "");
        values.put("upload", 0);
        db.update(DB_NAME_PWD, values, "type=? and platform=? and account=?", new String[]{item.type() + "", item.platform() + "", item.account() + ""});
    }

    public void delete(SQLiteDatabase db, PwdItem item) {
        db.delete(DB_NAME_PWD, "type=? and platform=? and account=?", new String[]{item.type() + "", item.platform() + "", item.account() + ""});
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        int currentVersion = SharedPrefUtils.getInteger(SharedPrefKeys.KEY_DB_VERSION, 1000);

        Log.e(DbHelper.class.getName(), "Echo , onUpgrade, current: " + currentVersion + ", target: " + DbConstant.DB_VERSION);
        for (int i = currentVersion; i < DbConstant.DB_VERSION; i++) {
            dealVersion(db, i + 1);
        }
    }

    private void dealVersion(SQLiteDatabase db, int version) {
        switch (version) {
            case 1001:
//                String freeSql = "Create Table if not exists " + DB_NAME_FREE_RECORD + "(id Integer primary key autoincrement, animalName text, freeTime text, freeIng text, upload Integer)";
//                String praySql = "Create Table if not exists " + DB_NAME_PRAY_RECORD + "(id Integer primary key autoincrement, buddhaName text, prayTime text, prayIng text, upload Integer)";
//                if (haveTable(db, DB_NAME_PRAY_RECORD)) {
//                    praySql = "ALTER TABLE " + DB_NAME_PRAY_RECORD + " ADD upload Integer" ;
//                }
//                if (haveTable(db, DB_NAME_FREE_RECORD)) {
//                    freeSql = "ALTER TABLE " + DB_NAME_FREE_RECORD + " ADD upload Integer" ;
//                }
//                try {
//                    db.execSQL(praySql);
//                    db.execSQL(freeSql);
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//                SharedPrefUtils.setInteger(SharedPrefKeys.KEY_DB_VERSION, 1002);
                break;
        }
    }

    public static boolean haveTable(SQLiteDatabase db, String tablename){
        Cursor cursor;
        boolean a=false;
        cursor = db.rawQuery("select name from sqlite_master where type='table' ", null);
        while(cursor.moveToNext()){
            //遍历出表名
            String name = cursor.getString(0);
            if(name.equals(tablename))
            {
                a=true;
            }
        }
        return a;
    }
//
//    public static boolean haveData(SQLiteDatabase db,String tablename){
//        Cursor cursor;
//        boolean a=false;
//        cursor = db.rawQuery("select name from sqlite_master where type='table' ", null);
//        while(cursor.moveToNext()){
//            //遍历出表名
//            String name = cursor.getString(0);
//            if(name.equals(tablename))
//            {
//                a=true;
//            }
//        }
//        if(a)
//        {
//            cursor=db.query(tablename,null,null,null,null,null,null);
//            //检查是不是空表
//            if(cursor.getCount()>0)
//                return true;
//            else
//                return false;
//        }
//        else
//            return false;
//
//    }
}
