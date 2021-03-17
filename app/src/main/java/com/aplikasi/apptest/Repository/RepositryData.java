package com.aplikasi.apptest.Repository;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.aplikasi.apptest.Database.Database;
import com.aplikasi.apptest.Model.Data;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteException;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;

public class RepositryData {

    private SQLiteDatabase db;
    private Database dbHelper;

    public RepositryData(Context context) {
        dbHelper = Database.getInstance(context);
    }

    public void open() throws SQLiteException {
        db = dbHelper.getWritableDatabase(Database.DATABASE_PASS);
    }

    public void close() {
        dbHelper.close();
    }

    public boolean insert(String note, String tanggal) {
        db = dbHelper.getWritableDatabase(Database.DATABASE_PASS);
        ContentValues contentValues = new ContentValues();
        Log.d("SAD", "sa " + tanggal);
        Log.d("okas", note);
        contentValues.put("text", note);
        contentValues.put("done", 0);
        contentValues.put("date", tanggal);
        db.insert(Database.TABEL_DATA, null, contentValues);
        return true;
    }

    public boolean update(int done, int id) {
        db = dbHelper.getWritableDatabase(Database.DATABASE_PASS);
        ContentValues contentValues = new ContentValues();
        Log.d("okas", "id = "+id + " done = " + done);
        contentValues.put("done", done);
        db.update(Database.TABEL_DATA, contentValues, "id = " + id , null);
        return true;
    }

    public MutableLiveData<List<Data>> getData(){
        final MutableLiveData<List<Data>> dataList = new MutableLiveData<>();
        List<Data> dataList2 = new ArrayList<>();
        db = dbHelper.getReadableDatabase(Database.DATABASE_PASS);
        Cursor res = db.rawQuery("select * from " + Database.TABEL_DATA, null);
        Log.d("LIST", " ada berapa = " + res.getCount());

        if (res.moveToFirst() && res.getCount() > 0){

            try {
                do {
                    Data data = new Data();
                    data.setId(res.getInt(res.getColumnIndex("id")));
                    data.setNote(res.getString(res.getColumnIndex("text")));
                    data.setDate(res.getString(res.getColumnIndex("date")));
                    data.setDone(res.getInt(res.getColumnIndex("done")));
                    dataList2.add(data);
                }while(res.moveToNext());
            }catch (Exception e){
                e.printStackTrace();
            }finally{
                if (res != null && !res.isClosed()) {
                    res.close();
                }
            }
        }

        Log.d("LIST", " size berapa = " + dataList2.size());
        dataList.setValue(dataList2);

        return dataList;
    }

    public int getUncheck(){
        int count = 0;
        db = dbHelper.getReadableDatabase(Database.DATABASE_PASS);
        Cursor res = db.rawQuery("select * from " + Database.TABEL_DATA + " where done = 0", null);
        if (res.moveToFirst() || res.getCount() > count)
            count = res.getCount();

        res.close();

        return count;

    }

    public int getCheck(){
        int count = 0;
        db = dbHelper.getReadableDatabase(Database.DATABASE_PASS);
        Cursor res = db.rawQuery("select * from " + Database.TABEL_DATA + " where done = 1", null);
        if (res.moveToFirst() || res.getCount() > count)
            count = res.getCount();

        res.close();

        return count;

    }

    public Boolean deleteDone(){
        db = dbHelper.getReadableDatabase(Database.DATABASE_PASS);
        db.execSQL("delete from " + Database.TABEL_DATA + " " + "where done = 1" );
        return true;
    }

    public boolean updateAll(int done) {
        db = dbHelper.getWritableDatabase(Database.DATABASE_PASS);
        ContentValues contentValues = new ContentValues();
        contentValues.put("done", done);
        db.update(Database.TABEL_DATA, contentValues, null, null);
        return true;
    }

}
