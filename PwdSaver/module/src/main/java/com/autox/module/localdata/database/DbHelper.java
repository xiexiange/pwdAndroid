package com.autox.module.localdata.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.autox.base.BaseUtil;
import com.autox.base.PrefUtil;
import com.autox.module.localdata.database.items.PwdItem;
import com.autox.module.localdata.sharedprefs.SharedPrefKeys;
import com.autox.module.util.ClientEncodeUtil;

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
                    sInstance = new DbHelper(BaseUtil.getInstance().getImp().getContext(), "DbHelper", null, DbConstant.DB_VERSION);
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
        db.execSQL("Create Table if not exists " + DB_NAME_PWD + "(id Integer primary key autoincrement, type text, platform text, account text, pwd text, saveTime text, note text, favor Integer, deleted Integer, upload Integer)");
        PrefUtil.setInteger(SharedPrefKeys.KEY_DB_VERSION, DbConstant.DB_VERSION);
    }

    public void uploadToServer(int type, String time) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("upload", 1);
        db.update(DB_NAME_PWD, values, "saveTime=?", new String[]{time});
        db.close();
    }

    public void setDeleted(PwdItem item) {
        SQLiteDatabase db = getWritableDatabase();
//        String sql = "update table " + DB_NAME_PWD + " set deleted=" + 1 + " where platform=\"" + item.platform() + "\" and account=\"" + item.account() + "\"";
//        db.delete(DB_NAME_PWD, "platform=? and account=?", new String[]{item.platform(), item.account()});
        ContentValues values = new ContentValues();
        values.put("deleted", 1);
        db.update(DB_NAME_PWD, values, "platform=? and account=?", new String[]{item.platform(), item.account()});
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
        values.put("note", item.note());
        values.put("favor", item.favor());
        values.put("upload", uploadInt);
        values.put("deleted", 0);
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
                    Long.parseLong(cursor.getString(5)),
                    cursor.getString(cursor.getColumnIndex("note")),                //note
                    cursor.getInt(cursor.getColumnIndex("favor"))
            );
            items.add(item);
        }
        return items;
    }

    public List<PwdItem> getPwdListBySearch(String string) {
        List<PwdItem> items = getUnDeletedPwdList();
        List<PwdItem> result = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            PwdItem item = items.get(i);
            if (item.account().contains(string)
            || item.platform().contains(string)) {
                result.add(item);
            }
        }
        return result;
    }

    public List<PwdItem> getUnDeletedPwdList() {
        List<PwdItem> items = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DB_NAME_PWD + " where deleted=0", new String[]{});
        while (cursor.moveToNext()) {
            PwdItem item = new PwdItem(
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    Long.parseLong(cursor.getString(5)),
                    cursor.getString(cursor.getColumnIndex("note")),                //note
                    cursor.getInt(cursor.getColumnIndex("favor"))
            );
            items.add(item);
        }
        return items;
    }

    public List<PwdItem> getFavorList() {
        List<PwdItem> items = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DB_NAME_PWD + " where favor=1 and deleted=0" , new String[]{});
        while (cursor.moveToNext()) {
            PwdItem item = new PwdItem(
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),                    //account
                    cursor.getString(4),                    //pwd
                    Long.parseLong(cursor.getString(5)),    //saveTime
                    cursor.getString(cursor.getColumnIndex("note")),                //note
                    cursor.getInt(cursor.getColumnIndex("favor"))
            );
            items.add(item);
        }
        return items;
    }

    private List<PwdItem> getPwdList(SQLiteDatabase db) {
        List<PwdItem> items = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + DB_NAME_PWD, new String[]{});
        while (cursor.moveToNext()) {
            PwdItem item = new PwdItem(
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    Long.parseLong(cursor.getString(5)),
                    cursor.getString(cursor.getColumnIndex("note")),                //note
                    cursor.getInt(cursor.getColumnIndex("favor"))
            );
            items.add(item);
        }
        return items;
    }

    public List<PwdItem> getPwdSizeByType(String type) {
        List<PwdItem> items = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DB_NAME_PWD + " where type=\"" + type + "\" and deleted=0" , new String[]{});
        while (cursor.moveToNext()) {
            PwdItem item = new PwdItem(
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),                    //account
                    cursor.getString(4),                    //pwd
                    Long.parseLong(cursor.getString(5)),    //saveTime
                    cursor.getString(cursor.getColumnIndex("note")),                //note
                    cursor.getInt(cursor.getColumnIndex("favor"))
            );
            items.add(item);
        }
        return items;
    }

    public void like(PwdItem item, boolean like) {
        int favor = like ? 1 : 0;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("type", item.type() + "");
        values.put("platform", item.platform() + "");
        values.put("account", item.account() + "");
        values.put("favor", favor);
        db.update(DB_NAME_PWD, values, "type=? and platform=? and account=?", new String[]{item.type() + "", item.platform() + "", item.account() + ""});
    }

    public void update(SQLiteDatabase db, PwdItem item) {
        ContentValues values = new ContentValues();
        values.put("type", item.type() + "");
        values.put("platform", item.platform() + "");
        values.put("account", item.account() + "");
        values.put("pwd", item.pwd() + "");
        values.put("note", item.note() + "");
        values.put("favor", item.favor());
        values.put("upload", 0);
        db.update(DB_NAME_PWD, values, "type=? and platform=? and account=?", new String[]{item.type() + "", item.platform() + "", item.account() + ""});
    }

    public void delete(SQLiteDatabase db, PwdItem item) {
        db.delete(DB_NAME_PWD, "type=? and platform=? and account=?", new String[]{item.type() + "", item.platform() + "", item.account() + ""});
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        int currentVersion = PrefUtil.getInteger(SharedPrefKeys.KEY_DB_VERSION, 1000);

        for (int i = currentVersion; i < DbConstant.DB_VERSION; i++) {
            dealDBFiled(db, i);
        }

        for (int i = currentVersion; i < DbConstant.DB_VERSION; i++) {
            dealDBData(db, i);
        }
    }

    private void dealDBFiled(SQLiteDatabase db, int version) {
        switch (version) {
            case 1001:
                String sql1001 = "alter table " + DB_NAME_PWD + " add column note string";
                db.execSQL(sql1001);
                break;
            case 1002:
                String sql1002 = "alter table " + DB_NAME_PWD + " add column favor Integer default 1";
                db.execSQL(sql1002);
                break;
        }
    }

    private void dealDBData(SQLiteDatabase db, int version) {
        List<PwdItem> items;
        switch (version) {
            case 1000:
                items = DbHelper.getInstance().getPwdList(db);
                for (PwdItem item : items) {
                    DbHelper.getInstance().update(db, new PwdItem(item.type(), item.platform(), item.account(), ClientEncodeUtil.encode(item.pwd()), item.saveTime(), "", 1));
                }
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
