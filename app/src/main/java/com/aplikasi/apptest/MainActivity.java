package com.aplikasi.apptest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aplikasi.apptest.Adapter.AdapterData;
import com.aplikasi.apptest.Database.Database;
import com.aplikasi.apptest.Model.Data;
import com.aplikasi.apptest.ViewModel.ViewModelMain;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    ViewModelMain viewModelMain;
    EditText edtNote, edtDate;
    Button btnCreate, btnClear;
    Database dbHelper;
    AdapterData adapterData;
    RecyclerView recyclerView;
    Toolbar toolbar;
    TextView tvCount;
    String dateString = null;
    Boolean selectAll;
    CheckBox checkbox;
    Date dateNow;
    DateFormat dateFormatNow;
    String pickerDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModelMain = new ViewModelProvider(this).get(ViewModelMain.class);

        initUI();

        initDatabase();

        setTitle("Technical Test");

        showList();


    }

    private void initUI(){
        edtNote = findViewById(R.id.edtNote);
        edtDate = findViewById(R.id.edtDate);
        tvCount = findViewById(R.id.tvCount);
        selectAll = false;

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        checkbox = findViewById(R.id.checkbox);
        checkbox.setOnClickListener(this);


        btnClear = findViewById(R.id.btnClear);
        btnCreate = findViewById(R.id.btnCreate);

        recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        btnCreate.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        edtDate.setOnClickListener(this);

        dateNow = Calendar.getInstance().getTime();
        dateFormatNow = android.text.format.DateFormat.getDateFormat(MainActivity.this);

        if (!selectAll)
            updateAll(0);
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.btnCreate:
                if (edtNote.getText().toString().isEmpty()){
                    edtNote.setError("Masukan Note !");
                    return;
                }else if (dateString == null){
                    edtDate.setError("Pilih Tanggal !");
                }else {
                    if (checkDate(pickerDate)) {
                        String note = edtNote.getText().toString();
                        Boolean succes = viewModelMain.insertData(note, dateString, this).booleanValue();
                        if (succes)
                            showList();
                    }else{
                        Toast.makeText(this, "Masukan Tanggal Tidak Kurang Dari Tanggal 16 July 2011 Dan Tidak Lebih Dari Tanggal Sekarang !", Toast.LENGTH_LONG).show();
                    }

                    clear();
                }
                break;
            case R.id.btnClear:
                deleteDone();
                checkbox.setChecked(false);
                break;

            case R.id.edtDate:
                DatePickerDialog dp = new DatePickerDialog(this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dp.show();
                break;

            case R.id.checkbox:
                if (selectAll){
                    updateAll(0);
                    selectAll = false;
                }else{
                    updateAll(1);
                    selectAll = true;
                }
        }
    }

    private void initDatabase() {
        dbHelper = new Database(this);

        File database = getApplicationContext().getDatabasePath(Database.DATABASE_NAME);
        if (!database.exists()) {
            dbHelper.getReadableDatabase(Database.DATABASE_PASS);
            if (copyDatabase(this)) {
               Toast.makeText(this, "Database initialized", Toast.LENGTH_SHORT).show();
                return;
            }else{
                Toast.makeText(this,"Database Error", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    private boolean copyDatabase(Context context) {
        try {
            InputStream inputStream = context.getAssets().open(Database.DATABASE_NAME);
            String outFileName = Database.DATABASE_LOCATION + Database.DATABASE_NAME;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[] buff = new byte[1024];
            int length ;
            while ((length = inputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            Log.w("TAG", "Database initialized" );
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public void showList(){
        viewModelMain.getList(MainActivity.this).observe(this, list->{
            Log.d("as", "berapa = " + list.size());
            adapterData = new AdapterData(this, list);
            recyclerView.setAdapter(adapterData);
            getCount();
        });
    }

    public void getCount(){
        tvCount.setText(viewModelMain.getUncheck(MainActivity.this) + " Item Left");
        getCheck();
    }

    public void deleteDone(){
        if (viewModelMain.delete(this))
            showList();
    }

    public void updateAll(int done){
        if (viewModelMain.updateAll(this, done))
            showList();
    }

    public void getCheck(){
        btnClear.setText("Clear " + viewModelMain.getCheck(MainActivity.this) + " Complate Item");
    }

    final Calendar myCalendar = Calendar.getInstance();
    final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "yyyy-MM-dd";
            String myFormat2 = "MM/dd/yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRENCH);
            SimpleDateFormat sdf2 = new SimpleDateFormat(myFormat2, Locale.FRENCH);
            edtDate.setText(sdf2.format(myCalendar.getTime()));
            dateString = sdf.format(myCalendar.getTime()) + " 00:00:00";
            pickerDate = sdf.format(myCalendar.getTime());
        }
    };
    public boolean checkDate(String datePick) {
        try {
            String inputPattern = "yyyy/MM/dd";
            String outputPattern = "yyyy-MM-dd";
            SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
            SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
            Date before;
            Log.d("adw", "ewf " + dateFormatNow.format(dateNow));
            before = inputFormat.parse(dateFormatNow.format(dateNow));

            String endDate = outputFormat.format(before);

            String myFormatString = "yyyy-MM-dd";

            SimpleDateFormat df = new SimpleDateFormat(myFormatString, Locale.FRANCE);
            Date start = df.parse("2011-07-16");
            Date end = df.parse(endDate);
            Date pick = df.parse(datePick);

            Log.d("DS", "pik = " + pick.toString() + " end = " + end.toString() + " start = " + start);

            if(pick.after(start)){
                if (pick.before(new Date()))
                    return true;
                else
                    return false;
            }else
                return false;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void clear(){
        edtDate.setText("");
        edtNote.setText("");
        dateString = null;
    }

}