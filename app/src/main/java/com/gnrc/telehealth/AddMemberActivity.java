package com.gnrc.telehealth;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    RadioButton radioButton;
    private DBhandler dBhandler;
    private String URLstring = "https://www.gnrctelehealth.com/telehealth_api/index_dev.php";
    int selectedId;
    private TextInputLayout name, year,phone,occupation,month;
    AlertDialog dialog;

    AddFamily_Adapter addFamily_adapter;
    String format,format2;
    SimpleDateFormat simpleDateFormat,simpleDateFormat2;
    RecyclerView recyclerView;
    RadioGroup radioGroup;

    String familyId;
    SharedPreferences mPreferences1;
    SharedPreferences.Editor preferencesEditor;
    int finalCount;

    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmember);
        setTitle("Family Members");

        constraintLayout = findViewById(R.id.cl_noData);
        recyclerView = (RecyclerView) findViewById(R.id.am_recycler);
        addfamilymember = (Button) findViewById(R.id.add_family_member);
        familyId = getIntent().getStringExtra("id");
        mPreferences1=getSharedPreferences(familyId,MODE_PRIVATE);

        dBhandler = new DBhandler(getApplicationContext());


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
        TextView gender = customLayout.findViewById(R.id.tv_gender);
        radioGroup = (RadioGroup) customLayout.findViewById(R.id.amRG);
        // add a button
        RadioGroup radioGroup = customLayout.findViewById(R.id.rg_language2);
        RadioButton english = customLayout.findViewById(R.id.rb_english2);
        RadioButton assamese = customLayout.findViewById(R.id.rb_assamese2);
        RadioButton bengali = customLayout.findViewById(R.id.rb_bengali2);
        english.setChecked(true);
        SharedPreferences mPreferences2=getSharedPreferences("language",MODE_PRIVATE);
        preferencesEditor = mPreferences2.edit();
        String language = mPreferences2.getString("lang","NoValue");

        if (language.equals("Assamese")){
            assamese.setChecked(true);
            name.setHint("নাম");
            year.setHint("বছৰ");
            year.setHelperText("বয়স");
            month.setHelperText("বয়স");
            month.setHint("মাহ");
            phone.setHint("যোগাযোগ নং");
            occupation.setHint("বৃত্তি");
            gender.setText("লিংগ");

        }else if (language.equals("Bengali")){
            bengali.setChecked(true);
            name.setHint("নাম");
            year.setHint("বছর");
            year.setHelperText("বয়স");
            month.setHelperText("বয়স");
            month.setHint("মাস");
            phone.setHint("যযোগাযোগের নম্বর");
            occupation.setHint("পেশা");
            gender.setText("লিঙ্গ");
        }else{
            english.setChecked(true);
            name.setHint("Name");
            year.setHint("Year");
            year.setHelperText("Age");
            month.setHelperText("Age");
            month.setHint("Month");
            phone.setHint("Phone");
            occupation.setHint("Occupation");
            gender.setText("Gender");
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = customLayout.findViewById(checkedId);
                if (radioButton.getText().equals("Assamese")){
                    name.setHint("নাম");
                    year.setHint("বছৰ");
                    year.setHelperText("বয়স");
                    month.setHelperText("বয়স");
                    month.setHint("মাহ");
                    phone.setHint("যোগাযোগ নং");
                    occupation.setHint("বৃত্তি");
                    gender.setText("লিংগ");
                    preferencesEditor.putString("lang","Assamese");
                    preferencesEditor.apply();

                }else if (radioButton.getText().equals("Bengali")){
                    name.setHint("নাম");
                    year.setHint("বছর");
                    year.setHelperText("বয়স");
                    month.setHelperText("বয়স");
                    month.setHint("মাস");
                    phone.setHint("যযোগাযোগের নম্বর");
                    occupation.setHint("পেশা");
                    gender.setText("লিঙ্গ");
                    preferencesEditor.putString("lang","Bengali");
                    preferencesEditor.apply();

                }else {
                    name.setHint("Name");
                    year.setHint("Year");
                    year.setHelperText("Age");
                    month.setHelperText("Age");
                    month.setHint("Month");
                    phone.setHint("Phone");
                    occupation.setHint("Occupation");
                    gender.setText("Gender");
                    preferencesEditor.putString("lang","English");
                    preferencesEditor.apply();

                }
            }
        });
        builder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int which)
                    {

                    }
                });
        // create and show
        // the alert dialog
        dialog = builder.create();
        dialog.show();
        Button theButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        theButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedId = radioGroup.getCheckedRadioButtonId();
                if (name.getEditText().getText().length()<=2){
                    name.setError("Please enter Name with at least 3 letters");
                    name.clearFocus();
                    name.requestFocus();
                }

                else if (year.getEditText().getText().length()<1||
                        Integer.parseInt(year.getEditText().getText().toString()) > 120){
                    year.setError("Please enter valid age year");
                    year.clearFocus();
                    year.requestFocus();
                }
                else if (month.getEditText().getText().length()<1||
                        Integer.parseInt(month.getEditText().getText().toString()) < 1 ||
                        Integer.parseInt(month.getEditText().getText().toString()) > 11
                                ){
                    month.setError("Please enter valid age month");
                    month.clearFocus();
                    month.requestFocus();
                }
                else if (phone.getEditText().getText().length() != 10){
                    phone.setError("Phone number should be 10 digits");
                    phone.clearFocus();
                    phone.requestFocus();
                }
                else if (occupation.getEditText().getText().length() < 2){
                    occupation.setError("Please enter occupation with at least 3 letters");
                    occupation.clearFocus();
                    occupation.requestFocus();
                }

                else if (selectedId == -1 ){
                    Toast.makeText(AddMemberActivity.this,
                            "Please select Gender", Toast.LENGTH_SHORT).show();

                }else {
                    addDataToDatabase();
                    setupRecyclerAm_DB();
                    addFamily_adapter.notifyDataSetChanged();
                    constraintLayout.setVisibility(View.GONE);
                    dialog.dismiss();
                }
                Log.d("radioCheck", "onClick: "+selectedId);
               /* else {

                }*/
            }
        });
    }

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

        selectedId = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) radioGroup.findViewById(selectedId);
        dBhandler = new DBhandler(getApplicationContext());
        // find the radiobutton by returned id

        format = simpleDateFormat.format(new Date());
        format2 = simpleDateFormat2.format(new Date());
        dBhandler = new DBhandler(getApplicationContext());

        setupSharedPreference();

        dBhandler.addfamilymember(getIntent().getStringExtra("id") +"_00"+ finalCount,
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
        Cursor cursor = dBhandler.getFamilyMemberListWithoutMember(getIntent().getStringExtra("id"));

        //Family_Head_Model dmodel = new Family_Head_Model();
        if (cursor.getCount()>0){
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
        }else{
            constraintLayout.setVisibility(View.VISIBLE);
        }


    }

    public void setupSharedPreference(){
        Cursor cursor = dBhandler.getFamilyMemberListWithoutMember(getIntent().getStringExtra("id"));

        String id = null;

        if (cursor.moveToFirst()){
            do {
                id = cursor.getString(18);
            }while (cursor.moveToNext());
        }

        if (id!=null && id.equals(familyId)){
            int getCount = mPreferences1.getInt(familyId,0);
            finalCount = ++getCount;
            preferencesEditor = mPreferences1.edit();
            preferencesEditor.putInt(familyId,finalCount);
            preferencesEditor.apply();
        }else {
            int getCount = mPreferences1.getInt(familyId,0);
            finalCount = ++getCount;
            preferencesEditor = mPreferences1.edit();
            preferencesEditor.putInt(familyId,finalCount);
            preferencesEditor.apply();
        }
    }
    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();

    }
}