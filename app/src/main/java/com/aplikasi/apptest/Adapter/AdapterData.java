package com.aplikasi.apptest.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.aplikasi.apptest.MainActivity;
import com.aplikasi.apptest.Model.Data;
import com.aplikasi.apptest.R;
import com.aplikasi.apptest.ViewModel.ViewModelMain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterData extends RecyclerView.Adapter<AdapterData.MyViewHolder> {
    private Context context;
    private List<Data> dataList = new ArrayList<>();
    int id, done;
    View view;

    public AdapterData(Context context, List<Data> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public AdapterData.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_data, parent, false);
        return new AdapterData.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterData.MyViewHolder holder, int position) {
        holder.tvText.setText(dataList.get(position).getNote());
        String formatAkhir = "dd/MM/yyyy";
        String formatAwal = "yyyy-MM-dd hh:mm:ss";
        SimpleDateFormat sdf1 = new SimpleDateFormat(formatAwal, Locale.FRENCH);
        SimpleDateFormat sdf2 = new SimpleDateFormat(formatAkhir, Locale.FRENCH);
        Date tanggal = null;
        Log.d("asd", " asidasdn = " + dataList.get(position).getDate());
        try {
            tanggal = sdf1.parse(dataList.get(position).getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String date = sdf2.format(tanggal);
        holder.tvDate.setText(date);
        if (dataList.get(position).getDone() == 1)
            holder.cbCheck.setChecked(true);
        holder.cbCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                done = dataList.get(position).getDone();
                id = dataList.get(position).getId();
                Log.d("as", "apa done = " + done);
                select(view, id, done);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvText, tvDate;
        CheckBox cbCheck;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvText = itemView.findViewById(R.id.tvText);
            tvDate = itemView.findViewById(R.id.tvDate);
            cbCheck = itemView.findViewById(R.id.cbCheck);
        }
    }

    public void select(View v, int id, int done){
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        ViewModelMain mainViewModel = new ViewModelProvider(activity).get(ViewModelMain.class);
        if(done == 0)
            done = 1;
        else
            done = 0;

        mainViewModel.update(done, id, activity);
        mainViewModel.getList(activity).observe(activity, list->{
            dataList.clear();
            dataList = list;
            notifyDataSetChanged();
            ((MainActivity)activity).getCount();
        });
    }
}
