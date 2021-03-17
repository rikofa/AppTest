package com.aplikasi.apptest.ViewModel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.aplikasi.apptest.Database.Database;
import com.aplikasi.apptest.Model.Data;
import com.aplikasi.apptest.Repository.RepositryData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class ViewModelMain  extends ViewModel {

    Database helper;
    private RepositryData repositryData;
    LiveData<List<Data>> dataList;


    public Boolean insertData(String note, String tanggal, Context context){
        repositryData = new RepositryData(context);

        Boolean insert = repositryData.insert(note, tanggal);
        return insert;
    }

    public Boolean update(int done, int id, Context context){
        repositryData = new RepositryData(context);

        Boolean insert = repositryData.update(done, id);
        return insert;
    }

    public LiveData<List<Data>> getList(Context context){
        repositryData = new RepositryData(context);
        dataList = repositryData.getData();
        return dataList;
    }

    public int getUncheck(Context context){
        repositryData = new RepositryData(context);
        int count = repositryData.getUncheck();
        return count;
    }

    public int getCheck(Context context){
        repositryData = new RepositryData(context);
        int count = repositryData.getCheck();
        return count;
    }

    public Boolean delete(Context context){
        repositryData = new RepositryData(context);
        Boolean a = repositryData.deleteDone();
        return a;
    }

    public Boolean updateAll(Context context, int done){
        repositryData = new RepositryData(context);
        Boolean a = repositryData.updateAll(done);
        return a;
    }

}
