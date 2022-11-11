package com.gnrc.telehealth;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.gnrc.telehealth.Adapter.AddFamily_Adapter;
import com.gnrc.telehealth.DatabaseSqlite.DBhandler;
import com.gnrc.telehealth.Model.AddFamilyModel;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddMemberActivity extends AppCompatActivity {
    Button addfamilymember;
    private DBhandler dBhandler;
    private String URLstring = "https://www.gnrctelehealth.com/telehealth_api/";
    private static ProgressDialog mProgressDialog;
    private TextInputLayout name,dob, year,phone,occupation,month;
    AlertDialog dialog;
    Button addFamilymember;
    AddFamily_Adapter addFamily_adapter;
    String format,format2;
    SimpleDateFormat simpleDateFormat,simpleDateFormat2;
    RecyclerView recyclerView;
    RadioGroup radioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmember);

        recyclerView = (RecyclerView) findViewById(R.id.am_recycler);
        addfamilymember = (Button) findViewById(R.id.add_family_member);

        addfamilymember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialogButtonClicked(v);
            }
        });
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi

                setupRecyclerAm_DB();
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to mobile data

                setupRecyclerAm_DB();
            }
        } else {

            setupRecyclerAm_DB();
            // not connected to the internet
        }


    }
    public void showAlertDialogButtonClicked(View view)
    {
        // Create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Member");
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.addmember_custom_alert_dialog,null);

        builder.setView(customLayout);
        name = customLayout.findViewById(R.id.fm_name);
        /*dob = customLayout.findViewById(R.id.fm_DOB);*/
        year = customLayout.findViewById(R.id.fm_Year);
        month = customLayout.findViewById(R.id.fm_month);
        phone = customLayout.findViewById(R.id.fm_phone);
        occupation = customLayout.findViewById(R.id.fm_occupation);
        radioGroup = (RadioGroup) customLayout.findViewById(R.id.amRG);
        // add a button
        builder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int which)
                    {
                        addDataToDatabase();
                        setupRecyclerAm_DB();
                        addFamily_adapter.notifyDataSetChanged();
                        // send data from the
                        // AlertDialog to the Activity

                        //dist = (Spinner) customLayout.findViewById(R.id.spfamilyheaddistrict);
                        //state = (Spinner) customLayout.findViewById(R.id.spfamilyheadstate);
                        //pin = customLayout.findViewById(R.id.etfamilyheadpin);

                        /*adddatatodatabase();
                        setupRecyclerFrom_DB();
                        familyHeadAdapter.notifyDataSetChanged();*/
                                /*Intent i = new Intent(Family_List_Activity.this,Family_List_Activity.class);
                                startActivity(i);
                                finish();*/
                    }
                });
        // create and show
        // the alert dialog
        dialog = builder.create();
        dialog.show();
        /*dob.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datepicker();
            }
        });*/

    }
    /*public void datepicker(){
        final Calendar c = Calendar.getInstance();

        // on below line we are getting
        // our day, month and year.
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // on below line we are creating a variable for date picker dialog.
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                // on below line we are passing context.
                AddMemberActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // on below line we are setting date to our text view.
                        dob.getEditText().setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        int age1 = c.get(Calendar.YEAR)- year;
                        AddMemberActivity.this.year.getEditText().getText();

                    }
                },
                // on below line we are passing year,
                // month and day for selected date in our date picker.
                year, month, day);
        // at last we are calling show to
        // display our date picker dialog.
        datePickerDialog.show();
    }*/
    private void addDataToDatabase(){
        Calendar c = Calendar.getInstance();
        int dobYear = Math.abs(Integer.parseInt(year.getEditText().getText().toString()));
        int dobMonth = Math.abs(Integer.parseInt(month.getEditText().getText().toString()));


        LocalDate now = LocalDate.now();
        LocalDate dob = now.minusYears(dobYear)
                .minusMonths(dobMonth);


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        simpleDateFormat = new SimpleDateFormat("ddMMyyyyhhmmss", Locale.CHINESE);
        simpleDateFormat2 = new SimpleDateFormat("dd-MM-yyyy hh:mm", Locale.CHINESE);
        SharedPreferences mPreferences=getSharedPreferences("com.gnrc.telehealth",MODE_PRIVATE);

        //Checking for the value stored in shared preference
        String userid = mPreferences.getString("user_id","NoValue");
        int selectedId = radioGroup.getCheckedRadioButtonId();

        dBhandler = new DBhandler(getApplicationContext());
        // find the radiobutton by returned id
        RadioButton radioButton = (RadioButton) radioGroup.findViewById(selectedId);
        format = simpleDateFormat.format(new Date());
        format2 = simpleDateFormat2.format(new Date());
        dBhandler = new DBhandler(getApplicationContext());
        dBhandler.addfamilymember("AM" + phone.getEditText().getText().toString()+format,
                format2,
                "A",
                name.getEditText().getText().toString(),
                String.valueOf(radioButton.getText()),
                dob.format(formatter),
                year.getEditText().getText().toString(),
                month.getEditText().getText().toString(),
                "-1",
                phone.getEditText().getText().toString(),
                getIntent().getStringExtra("address"),
                getIntent().getStringExtra("dist"),
                getIntent().getStringExtra("block"),
                getIntent().getStringExtra("gaon"),
                "test",
                format2,
                userid,
                format2,
                getIntent().getStringExtra("id"));


    }
    private void setupRecyclerAm_DB(){
        ArrayList<AddFamilyModel> addFamilyArrayList = new ArrayList<>();
        AddFamilyModel familyModel;
        dBhandler = new DBhandler(getApplicationContext());
        Cursor cursor = dBhandler.getAddFamilyData();
        //Family_Head_Model dmodel = new Family_Head_Model();

        if (cursor.moveToFirst()) {
            do {
                familyModel = new AddFamilyModel();
                familyModel.setRegnum(cursor.getString(0));
                familyModel.setRegDt(cursor.getString(1));
                familyModel.setRegStatus(cursor.getString(2));
                familyModel.setPtName(cursor.getString(3));
                familyModel.setGender(cursor.getString(4));
                familyModel.setDob(cursor.getString(5));
                familyModel.setYear(cursor.getString(6));
                familyModel.setMonth(cursor.getString(7));
                familyModel.setDay(cursor.getString(8));
                familyModel.setContact(cursor.getString(9));
                familyModel.setAreaLocality(cursor.getString(10));
                familyModel.setDistCode(cursor.getString(11));
                familyModel.setBlockName(cursor.getString(12));
                familyModel.setPancName(cursor.getString(13));
                familyModel.setVillName(cursor.getString(14));
                familyModel.setCreateDate(cursor.getString(15));
                familyModel.setLoginId(cursor.getString(16));
                familyModel.setUpdDate(cursor.getString(17));
                familyModel.setF_id(cursor.getString(18));

                addFamilyArrayList.add(familyModel);
            }while (cursor.moveToNext());
            addFamily_adapter = new AddFamily_Adapter(this, addFamilyArrayList);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false));
            recyclerView.setAdapter(addFamily_adapter);

        }

    }
}