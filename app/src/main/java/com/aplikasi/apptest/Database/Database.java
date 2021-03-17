package com.aplikasi.apptest.Database;

import android.content.Context;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;


public class Database extends SQLiteOpenHelper {

    private static Database sInstance;
    public static final String DATABASE_NAME = "data.db";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_LOCATION = "/data/data/com.aplikasi.apptest/databases/";
    public static final String DATABASE_PASS = "12345";

    public static final String TABEL_DATA = "tabel_data";
    private Context context;
    private SQLiteDatabase database;

    public static Database getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new Database(context);
        }
        return sInstance;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        SQLiteDatabase.loadLibs(context);
    }

    public void openDatabase() {
        String dbPath = context.getDatabasePath(DATABASE_NAME).getPath();
        if (database != null && database.isOpen()) {
            return;
        }
        database = SQLiteDatabase.openOrCreateDatabase(DATABASE_PASS,dbPath,null);
    }
    public void closeDatabase() {
        if (database != null) {
            database.close();
        }
    }
}
