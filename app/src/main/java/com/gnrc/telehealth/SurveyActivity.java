package com.gnrc.telehealth;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gnrc.telehealth.Adapter.GeneralHabitsAdapter;
import com.gnrc.telehealth.Adapter.HealthCardAdapter;
import com.gnrc.telehealth.Adapter.OtherInfoAdapter;
import com.gnrc.telehealth.Adapter.TestFindingsAdapter;
import com.gnrc.telehealth.DatabaseSqlite.DBhandler;
import com.gnrc.telehealth.Model.MemberDetailsForDialogModel;
import com.gnrc.telehealth.Model.SignsAndSymptomsModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class SurveyActivity extends AppCompatActivity
        implements GeneralHabitsAdapter.GeneralHabits, HealthCardAdapter.HealthCard, OtherInfoAdapter.OtherInfo {

    RecyclerView  recyclerViewDialog, covidRecycler,generalRecycler, healthCardRecycler;
    Button genHab, testFin, heaCaIn, covFacIn, signsSymptoms, otherinfo,
    editGenHab, editTestFind, editHealthCard, editSignSymptoms, editOtherInfo;
    String latitude, longitude,list1,familyid;
    CheckBox cb;
    AlertDialog dialog;
    int num = 1000;
    int finalCount;
    private static ProgressDialog mProgressDialog;
    private RequestQueue rQueue;

    SharedPreferences mPreferences,mPreferences1;
    SharedPreferences.Editor preferencesEditor;
    private static final int REQUEST_LOCATION = 1;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.CHINESE);
    SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("ddMMyyyyhhmmss", Locale.CHINESE);
    String format, format2;

    ArrayList<CheckBox> checkBoxesSymptoms;
    ArrayList<String> mapObject;
    public ArrayList<String> memberSurveyIdArrayList;

    ArrayList<MemberDetailsForDialogModel> member;
    ArrayList<MemberDetailsForDialogModel> member1;
    ArrayList<MemberDetailsForDialogModel> member2;
    ArrayList<MemberDetailsForDialogModel> list2;

    MemberDetailsForDialogModel editModel;
    MemberDetailsForDialogModel editModel2;


    Button  saveSurvey;
    TreeMap<String, ArrayList<String>> multiMap;
    SignsAndSymptomsModel ssModel;

    public static ArrayList<Boolean> ssModelArraylist;
    private String URLstring = "https://www.gnrctelehealth.com/telehealth_api/index_dev.php";
    private TextView txtGeneralHabits,txtHealthCard,txtOtherInfo;
    private String CallingTypeLoad = "LOAD", CallingTypeUpdate = "UPDATE";
    private static int CAMERA_PERMISSION_CODE = 100;
    private static int VIDEO_RECORD_CODE = 101;
    private Uri videoPath;
    private String familySurveyId, memberSurveyId;

    DBhandler dBhandler;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        mPreferences = getSharedPreferences("smokingAlcohol", MODE_PRIVATE);

        member = new ArrayList<>();
        memberSurveyIdArrayList = new ArrayList<String>();
        initView();

        initListener();

        format2 = simpleDateFormat2.format(new Date());

        familyid = getIntent().getStringExtra("familyId");

        dBhandler = new DBhandler(getApplicationContext());

        mPreferences1=getSharedPreferences(familySurveyId,MODE_PRIVATE);

        familySurveyId = "F_SRV_" + familyid + format2;

        format = simpleDateFormat.format(new Date());

        showGeneralHabitsAlcohol(CallingTypeLoad);
        showTestFindingsBP();
        showTestFindingsBS();
        showHCIAtalAmrit(CallingTypeLoad);
        showOtherInfo(CallingTypeLoad);
        fetchingSymptoms();
        showSymptomsMembers();

        locationPermission();
        getCameraPermission();
        OnGPS();

        if (finalCount == 0){
            for (int i = 0; i < member.size(); i++) {
                int getCount = mPreferences1.getInt(familySurveyId, 0);
                finalCount = ++getCount;
                preferencesEditor = mPreferences1.edit();
                preferencesEditor.putInt(familySurveyId, finalCount);
                preferencesEditor.apply();

                memberSurveyIdArrayList.add("M_SRV_" + familySurveyId + "_00" + finalCount);
            }
        }
    }

    private void initView(){
        //Buttons for taking survey
        genHab = findViewById(R.id.btnGeneralHabits);
        testFin = findViewById(R.id.btnTestFindings);
        heaCaIn = findViewById(R.id.btnHci);
        //covFacIn = findViewById(R.id.tvCfi);
        signsSymptoms = findViewById(R.id.btnSignsSymptoms);
        otherinfo = findViewById(R.id.btnOtherInfo);

        //Buttons to edit survey
        editGenHab = findViewById(R.id.editBtnGeneralHabits);
        editTestFind = findViewById(R.id.editBtnTestFindings);
        editHealthCard = findViewById(R.id.editBtnHealth);
        editSignSymptoms = findViewById(R.id.editBtnSigns);
        editOtherInfo = findViewById(R.id.editBtnOi);

        saveSurvey = findViewById(R.id.btnSaveSurvey);

        txtGeneralHabits = findViewById(R.id.txt_general_habits);
        txtHealthCard = findViewById(R.id.tvHciInfo);
        txtOtherInfo = findViewById(R.id.tvOInfo);
    }

    private void initListener(){
        saveSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor1 = dBhandler.getGeneralHabitsAlcohol(familyid);
                Cursor cursor2 = dBhandler.getTestFindings(familyid);
                Cursor cursor3 = dBhandler.getHCIAtalAmrit(familyid);
                Cursor cursor4 = dBhandler.getSymptomsMemberByFamilyId(familyid);
                Cursor cursor5 = dBhandler.getOtherInfo(familyid);

                if (cursor1.getCount() == 0){
                    Toast.makeText(SurveyActivity.this, "Please Complete General Habits Survey",
                            Toast.LENGTH_SHORT).show();
                }
                else if (cursor2.getCount() == 0){
                    Toast.makeText(SurveyActivity.this, "Please Complete Test Findings Survey",
                            Toast.LENGTH_SHORT).show();
                }
                else if (cursor3.getCount() == 0){
                    Toast.makeText(SurveyActivity.this, "Please Complete Health Card Information Survey",
                            Toast.LENGTH_SHORT).show();
                }
                else if (cursor4.getCount() == 0){
                    Toast.makeText(SurveyActivity.this, "Please Complete Signs and Symptoms Survey",
                            Toast.LENGTH_SHORT).show();
                }

                else if (cursor5.getCount() == 0){
                    Toast.makeText(SurveyActivity.this, "Please Complete Other Info Survey",
                            Toast.LENGTH_SHORT).show();
                }else {
                    if (isCameraPresentInPhone()){
                        saveDataToServer();
                        recordVideo();
                    }
                }



               /* Intent i = new Intent(SurveyActivity.this,FamilyHeadActivity.class);
                startActivity(i);
                Toast.makeText(SurveyActivity.this, "Data Saved Successfully", Toast.LENGTH_SHORT).show();*/
            }
        });

        editTestFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditTestFindings();
            }
        });

        genHab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gpsTracker();
                showGeneralHabitDialog(v);
            }
        });

        testFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTestFindingDialog(v);
            }
        });

        heaCaIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHealthCardInfoDialog(v);
            }
        });

        /*covFacIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCovidFIDialog(v);
            }
        });*/

        signsSymptoms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSymptoms(v);
            }
        });

        otherinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOtherInfoDialog(v);
            }
        });
    }

    public void showGeneralHabitDialog(View view)    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("General Habits");

        View customLayout = getLayoutInflater().inflate(R.layout.general_habits_recycler,null);
        builder.setView(customLayout);

        generalRecycler = customLayout.findViewById(R.id.rvGeneralHabits);

        GeneralHabitsAdapter testFindingsAdapter = new GeneralHabitsAdapter(this, member, this);
        generalRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false));
        generalRecycler.setAdapter(testFindingsAdapter);

        builder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    DBhandler dBhandler;
                    @Override
                    public void onClick(DialogInterface dialog,int which) {
                        dBhandler = new DBhandler(getApplicationContext());
                        SQLiteDatabase db = dBhandler.getWritableDatabase();
                        db.execSQL("delete from tbl_general_habits_alcohol where family_id  = '" + familyid + "'" );
                        for (int i = 0; i < member.size(); i++){
                            String isSmoker = member.get(i).isSmoker() ? "Yes" : "No";
                            String isAlcoholic = member.get(i).isAlcoholic() ? "Yes" : "No";
                            dBhandler.addGeneralHabitsAlcohol(familySurveyId,member.get(i).getMember_id(),
                                    getIntent().getStringExtra("familyId"),
                                    member.get(i).getMemberName(), isSmoker,
                                    isAlcoholic, memberSurveyIdArrayList.get(i), latitude,longitude,format);

                        }

                        showGeneralHabitsAlcohol(CallingTypeUpdate);

                    }

                });
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void showTestFindingDialog(View view) {
        // Create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Test Findings");
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.testfindingsrecycler, null);
        builder.setView(customLayout);
        recyclerViewDialog = (RecyclerView) customLayout.findViewById(R.id.testFindingsDialogRecycler);
        ArrayList<MemberDetailsForDialogModel> list = new ArrayList<>();
        DBhandler dBhandler = new DBhandler(getApplicationContext());
        Cursor cursor1 = dBhandler.getTestFindings(getIntent().getStringExtra("familyId"));
        list2 = new ArrayList<>();
        //RecyclerView recyclerViewDialog = (RecyclerView) findViewById(R.id.rv_testFindings);
        if (cursor1.moveToFirst()) {
            do {
                MemberDetailsForDialogModel editModel = new MemberDetailsForDialogModel();
                editModel.setMemberName(cursor1.getString(3));
                editModel.setEditTextValueSys(cursor1.getString(4));
                editModel.setEditTextValueDia(cursor1.getString(5));
                editModel.setTypeSpinner(cursor1.getString(6));
                editModel.setEditTextValueValue(cursor1.getString(7));
                list2.add(editModel);
            } while (cursor1.moveToNext());
        }
            for (int i = 0; i < member.size(); i++){
                editModel = new MemberDetailsForDialogModel();
                editModel.setMemberName(member.get(i).getMemberName());
                list.add(editModel);
                TestFindingsAdapter testFindingsAdapter = new TestFindingsAdapter(this, list,getIntent().getStringExtra("familyId"));
                recyclerViewDialog.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false));
                recyclerViewDialog.setAdapter(testFindingsAdapter);

            }
        if (cursor1.getCount() > 0 ){

        }


        builder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    DBhandler dBhandler;
                    @Override
                    public void onClick(DialogInterface dialog,int which)
                    {
                        dBhandler = new DBhandler(getApplicationContext());
                        SQLiteDatabase db = dBhandler.getWritableDatabase();
                        db.execSQL("delete from tbl_test_findings where family_id  = '" + familyid + "'" );
                       // db.delete("tbl_test_findings", null,null);
                        for (int i = 0; i < member.size(); i++){
                            dBhandler.addTestFindings(familySurveyId,
                                    member.get(i).getMember_id(),
                                    getIntent().getStringExtra("familyId"),member.get(i).getMemberName(),
                                    TestFindingsAdapter.addMemberDialogArrayList.get(i).getEditTextValueSys(),
                                    TestFindingsAdapter.addMemberDialogArrayList.get(i).getEditTextValueDia(),
                                    TestFindingsAdapter.addMemberDialogArrayList.get(i).getTypeSpinner(),
                                    TestFindingsAdapter.addMemberDialogArrayList.get(i).getEditTextValueValue(),
                                    memberSurveyIdArrayList.get(i),format);
                            //setupTestFindingsDialogRecycler();
                            showTestFindingsBP();
                            showTestFindingsBS();
                        }
                    }

                });
        dialog = builder.create();
        dialog.show();
        dialog.getWindow().clearFlags( WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                |WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    public void showHealthCardInfoDialog(View view)    {
        // Create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Health Card Information");
        // set the custom layout
        View customLayout = getLayoutInflater().inflate(R.layout.general_habits_recycler,null);

        builder.setView(customLayout);

        healthCardRecycler = customLayout.findViewById(R.id.rvGeneralHabits);

        HealthCardAdapter testFindingsAdapter = new HealthCardAdapter(this, member1,this);
        healthCardRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false));
        healthCardRecycler.setAdapter(testFindingsAdapter);

        builder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    DBhandler dBhandler;
                    @Override
                    public void onClick(DialogInterface dialog,int which)
                    {
                        dBhandler = new DBhandler(getApplicationContext());
                        SQLiteDatabase db = dBhandler.getWritableDatabase();
                        db.execSQL("delete from tbl_hci_atal_amrit where family_id  = '" + familyid + "'" );

                        for (int i = 0; i < member1.size(); i++){
                            String isAtal = member1.get(i).isAtal() ? "Yes" : "No";
                            String isAyush = member1.get(i).isAyush() ? "Yes" : "No";

                            dBhandler = new DBhandler(getApplicationContext());
                            dBhandler.addHCI(familySurveyId,member1.get(i).getMember_id(),
                                    getIntent().getStringExtra("familyId"), member1.get(i).getMemberName(),
                                    isAtal,isAyush,memberSurveyIdArrayList.get(i),format);
                        }
                        showHCIAtalAmrit(CallingTypeUpdate);
                    }
                });
        dialog = builder.create();
        dialog.show();
    }
/*
    public void showCovidFIDialog(View view)    {
        // Create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Covid Facts and Information");
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.covidfactsrecycler, null);
        builder.setView(customLayout);

        covidRecycler = (RecyclerView) customLayout.findViewById(R.id.covidFIRecycler);
        ArrayList<MemberDetailsForDialogModel> list = new ArrayList<>();

        for (int i = 0; i < member.size(); i++){
            editModel = new MemberDetailsForDialogModel();
            //editModel.setMember_id(member.get(i).getMember_id());
            editModel.setMemberName(member.get(i).getMemberName());
            editModel.setCheckedStatus(member.get(i).getCheckedStatus());
            list.add(editModel);

            CovidFIAdapter testFindingsAdapter = new CovidFIAdapter(this, list);
            covidRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false));
            covidRecycler.setAdapter(testFindingsAdapter);
        }
        builder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    DBhandler dBhandler;
                    @Override
                    public void onClick(DialogInterface dialog,int which)
                    {
                        dBhandler = new DBhandler(getApplicationContext());
                        SQLiteDatabase db = dBhandler.getWritableDatabase();
                        db.execSQL("delete from tbl_covid_facts where family_id  = '" + familyid + "'" );
                        */
/*db.delete("tbl_covid_facts", null,null);*//*

                        for (int i = 0; i < member.size(); i++){
                            dBhandler.addCovidFacts(member.get(i).getMember_id(),
                                    getIntent().getStringExtra("familyId"),member.get(i).getMemberName(),
                                    CovidFIAdapter.addMemberDialogArrayList.get(i).getCovidStatus(),
                                    CovidFIAdapter.addMemberDialogArrayList.get(i).getDoseStatus(),
                                    CovidFIAdapter.addMemberDialogArrayList.get(i).getNoVaccineReason());
                            showCovidFactsInfo();
                        }
                    }

                });
        dialog = builder.create();
        dialog.show();


    }
*/

    public void showOtherInfoDialog(View view)    {
        // Create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Other Info");
        // set the custom layout
        View customLayout = getLayoutInflater().inflate(R.layout.general_habits_recycler,null);

        builder.setView(customLayout);

        healthCardRecycler = customLayout.findViewById(R.id.rvGeneralHabits);

        OtherInfoAdapter testFindingsAdapter = new OtherInfoAdapter(this, member2,this);
        healthCardRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL,false));
        healthCardRecycler.setAdapter(testFindingsAdapter);

        builder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    DBhandler dBhandler;
                    @Override
                    public void onClick(DialogInterface dialog,int which)
                    {
                        dBhandler = new DBhandler(getApplicationContext());
                        SQLiteDatabase db = dBhandler.getWritableDatabase();
                        db.execSQL("delete from tbl_other_info where family_id  = '" + familyid + "'" );

                        for (int i = 0; i < member2.size(); i++){
                            String isTelemed = member2.get(i).isTeleMed() ? "Yes" : "No";
                            String isOpd = member2.get(i).isOpd() ? "Yes" : "No";
                            String isAmbulance = member2.get(i).isAmbulance() ? "Yes" : "No";

                            dBhandler = new DBhandler(getApplicationContext());
                            dBhandler.addOtherInfo(familySurveyId, member2.get(i).getMember_id(),
                                    getIntent().getStringExtra("familyId"), member2.get(i).getMemberName(),
                                    isTelemed,isOpd,isAmbulance,memberSurveyIdArrayList.get(i),format);
                        }
                        showOtherInfo(CallingTypeUpdate);
                    }
                });
        dialog = builder.create();
        dialog.show();
    }

    public void showTestFindingsBP(){
        DBhandler dBhandler = new DBhandler(getApplicationContext());
        Cursor cursor = dBhandler.getTestFindings(getIntent().getStringExtra("familyId"));
        TextView tvTestFindings;
        LinearLayout displayTestFindings = findViewById(R.id.ll_BPDisplay);
        displayTestFindings.removeAllViews();
       /*
        ArrayList<MemberDetailsForDialogModel> list = new ArrayList<>();
        for (int i = 0; i < member.size(); i++) {
            editModel = new MemberDetailsForDialogModel();
            editModel.setMember_id(member.get(i).getMember_id());
            list.add(editModel);

            TestFindingsAdapter testFindingsAdapter = new TestFindingsAdapter(this, list, getIntent().getStringExtra("familyId"));
            recyclerViewDialog.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
            recyclerViewDialog.setAdapter(testFindingsAdapter);
        }*/
        if (cursor.moveToFirst()) {
            do {
                tvTestFindings = new TextView(SurveyActivity.this);
                tvTestFindings.setPadding(10,10,10,10);
                tvTestFindings.setTextColor(Color.parseColor("#000000"));
                tvTestFindings.setGravity(Gravity.CENTER);
                String sourceString = "<b>" + cursor.getString(3) + "</b>" + "<br/>" +
                        "SYS:"+cursor.getString(4)+"<br/>"+
                "DIA:"+cursor.getString(5);
                tvTestFindings.setText(Html.fromHtml(sourceString));
                displayTestFindings.addView(tvTestFindings);
            } while (cursor.moveToNext());
        }
    }

    public void showTestFindingsBS(){
        DBhandler dBhandler = new DBhandler(getApplicationContext());
        Cursor cursor = dBhandler.getTestFindings(getIntent().getStringExtra("familyId"));
        TextView tvTestFindings;
        LinearLayout displayTestFindings = findViewById(R.id.ll_BSDisplay);
        displayTestFindings.removeAllViews();
        if (cursor.moveToFirst()) {
            do {
                tvTestFindings = new TextView(SurveyActivity.this);
                tvTestFindings.setPadding(10,10,10,10);
                tvTestFindings.setTextColor(Color.parseColor("#000000"));
                tvTestFindings.setGravity(Gravity.CENTER);
                String sourceString = "<b>" + cursor.getString(3) + "</b>" + "<br/>"+
                        "TYPE:"+cursor.getString(6)+"<br/>"+"VALUE:"+cursor.getString(7);
                tvTestFindings.setText(Html.fromHtml(sourceString));
                displayTestFindings.addView(tvTestFindings);
            } while (cursor.moveToNext());
        }
    }

    public void showGeneralHabitsAlcohol( String callingType){
        String sourceString = "";

        switch (callingType){
            case "LOAD":
                member = new ArrayList<>();
                DBhandler dBhandler = new DBhandler(getApplicationContext());
                Cursor cursorFamilyMember = dBhandler.getFamilyMemberList(getIntent().getStringExtra("familyId"));

                /**
                 * member data
                 */

                if (cursorFamilyMember.moveToFirst()) {
                    do {
                        MemberDetailsForDialogModel memberDetailsForDialogModel = new MemberDetailsForDialogModel();

                        memberDetailsForDialogModel.setMemberName(cursorFamilyMember.getString(3)); //member name
                        memberDetailsForDialogModel.setMember_id(cursorFamilyMember.getString(0)); //member id

                        Cursor cursorGeneralHabits =
                                dBhandler.getGeneralHabitsAlcoholByMember(cursorFamilyMember.getString(0));

                        if(cursorGeneralHabits.moveToFirst()){
                            if(cursorGeneralHabits.getString(0).equals("Yes")) { //smoking
                                memberDetailsForDialogModel.setSmoker(true);
                            } else {
                                memberDetailsForDialogModel.setSmoker(false);
                            }

                            if(cursorGeneralHabits.getString(1).equals("Yes")) { //alcoholic
                                memberDetailsForDialogModel.setAlcoholic(true);
                            } else {
                                memberDetailsForDialogModel.setAlcoholic(false);
                            }
                        }

                        member.add(memberDetailsForDialogModel);
                    } while (cursorFamilyMember.moveToNext());
                }



                Cursor cursor = dBhandler.getGeneralHabitsAlcohol(getIntent().getStringExtra("familyId"));
                if (cursor.moveToFirst()) {
                    do {
                        sourceString += "<b>" + cursor.getString(3) + "</b><br/>" +
                                "<b>Smoking? :</b>" + cursor.getString(4) + "<br/>" +
                                "<b>Alcohol? :</b>" + cursor.getString(5) + "<br/>";
                    } while (cursor.moveToNext());
                }
                break;
            case "UPDATE":
                for(int i = 0; i < member.size(); i++){
                    String isSmoker = member.get(i).isSmoker() ? "Yes" : "No";
                    String isAlcoholic = member.get(i).isAlcoholic() ? "Yes" : "No";
                    sourceString += "<b>" + member.get(i).getMemberName() + "</b><br/>" +
                            "<b>Smoking? :</b>" + isSmoker + "<br/>" +
                            "<b>Alcohol? :</b>" + isAlcoholic + "<br/>";
                }
                break;
        }
        txtGeneralHabits.setText(Html.fromHtml(sourceString));
    }

    public void showOtherInfo( String callingType){
        String sourceString = "";

        switch (callingType){
            case "LOAD":
                member2 = new ArrayList<>();
                DBhandler dBhandler = new DBhandler(getApplicationContext());
                Cursor cursorFamilyMember = dBhandler.getFamilyMemberList(getIntent().getStringExtra("familyId"));

                /**
                 * member data
                 */

                if (cursorFamilyMember.moveToFirst()) {
                    do {
                        MemberDetailsForDialogModel memberDetailsForDialogModel = new MemberDetailsForDialogModel();

                        memberDetailsForDialogModel.setMemberName(cursorFamilyMember.getString(3)); //member name
                        memberDetailsForDialogModel.setMember_id(cursorFamilyMember.getString(0)); //member id

                        Cursor cursorHealthCard = dBhandler.getOtherInfoByMember(cursorFamilyMember.getString(0));

                        if(cursorHealthCard.moveToFirst()){
                            if(cursorHealthCard.getString(0).equals("Yes")) { //Telemed
                                memberDetailsForDialogModel.setTeleMed(true);
                            } else {
                                memberDetailsForDialogModel.setTeleMed(false);
                            }

                            if(cursorHealthCard.getString(1).equals("Yes")) { //opd
                                memberDetailsForDialogModel.setOpd(true);
                            } else {
                                memberDetailsForDialogModel.setOpd(false);
                            }
                            if(cursorHealthCard.getString(2).equals("Yes")) { //ambulance
                                memberDetailsForDialogModel.setAmbulance(true);
                            } else {
                                memberDetailsForDialogModel.setAmbulance(false);
                            }
                        }

                        member2.add(memberDetailsForDialogModel);
                    } while (cursorFamilyMember.moveToNext());
                }



                Cursor cursor = dBhandler.getOtherInfo(getIntent().getStringExtra("familyId"));
                if (cursor.moveToFirst()) {
                    do {
                        sourceString += "<b>" + cursor.getString(3) + "</b><br/>" +
                                "<b>Tele Medicine Booked? :</b>" + cursor.getString(5) + "<br/>" +
                                "<b>OPD booked? :</b>" + cursor.getString(6) + "<br/>" +
                                "<b>Ambulance Booked? :</b>" + cursor.getString(7) + "<br/>";
                    } while (cursor.moveToNext());
                }
                break;
            case "UPDATE":
                for(int i = 0; i < member2.size(); i++){
                    String isTeleMed = member2.get(i).isTeleMed() ? "Yes" : "No";
                    String isOpd = member2.get(i).isOpd() ? "Yes" : "No";
                    String isAmbulance = member2.get(i).isAmbulance() ? "Yes" : "No";
                    sourceString += "<b>" + member2.get(i).getMemberName() + "</b><br/>" +
                            "<b>Tele Medicine Booked? :</b>" + isTeleMed + "<br/>" +
                            "<b>OPD booked? :</b>" + isOpd + "<br/>" +
                            "<b>Ambulance Booked? :</b>" + isAmbulance + "<br/>";
                }
                break;
        }
        txtOtherInfo.setText(Html.fromHtml(sourceString));
    }

    public void showHCIAtalAmrit(String callingType){
        String sourceString = "";

        switch (callingType){
            case "LOAD":
                member1 = new ArrayList<>();
                DBhandler dBhandler = new DBhandler(getApplicationContext());
                Cursor cursorFamilyMember = dBhandler.getFamilyMemberList(getIntent().getStringExtra("familyId"));

                /**
                 * member data
                 */

                if (cursorFamilyMember.moveToFirst()) {
                    do {
                        MemberDetailsForDialogModel memberDetailsForDialogModel = new MemberDetailsForDialogModel();

                        memberDetailsForDialogModel.setMemberName(cursorFamilyMember.getString(3)); //member name
                        memberDetailsForDialogModel.setMember_id(cursorFamilyMember.getString(0)); //member id

                        Cursor cursorHealthCard = dBhandler.getHCIAtalAmritByMember(cursorFamilyMember.getString(0));

                        if(cursorHealthCard.moveToFirst()){
                            if(cursorHealthCard.getString(0).equals("Yes")) { //atalAmrit
                                memberDetailsForDialogModel.setAtal(true);
                            } else {
                                memberDetailsForDialogModel.setAtal(false);
                            }

                            if(cursorHealthCard.getString(1).equals("Yes")) { //ayushmanBharat
                                memberDetailsForDialogModel.setAyush(true);
                            } else {
                                memberDetailsForDialogModel.setAyush(false);
                            }
                        }

                        member1.add(memberDetailsForDialogModel);
                    } while (cursorFamilyMember.moveToNext());
                }



                Cursor cursor = dBhandler.getHCIAtalAmrit(getIntent().getStringExtra("familyId"));
                if (cursor.moveToFirst()) {
                    do {
                        sourceString += "<b>" + cursor.getString(3) + "</b><br/>" +
                                "<b>Atal Amrit Beneficiary? :</b>" + cursor.getString(4) + "<br/>" +
                                "<b>Ayushman Beneficiary? :</b>" + cursor.getString(5) + "<br/>";
                    } while (cursor.moveToNext());
                }
                break;
            case "UPDATE":
                for(int i = 0; i < member1.size(); i++){
                    String isAtal = member1.get(i).isAtal() ? "Yes" : "No";
                    String isAyush = member1.get(i).isAyush() ? "Yes" : "No";
                    sourceString += "<b>" + member1.get(i).getMemberName() + "</b><br/>" +
                            "<b>Atal Amrit Beneficiary? :</b>" + isAtal + "<br/>" +
                            "<b>Ayushman Beneficiary? :</b>" + isAyush + "<br/>";
                }
                break;
        }
        txtHealthCard.setText(Html.fromHtml(sourceString));
    }

    private void fetchingSymptoms() {

        //showSimpleProgressDialog(this, "Loading...","Saving",false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLstring,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("strrrrr", ">>" + response);
                        multiMap = new TreeMap<>();
                        try {
                            removeSimpleProgressDialog();

                            JSONObject obj = new JSONObject(response);


                            Log.d("deep", ">>" + response);

                            String message = obj.getString("status");
                            Toast.makeText(SurveyActivity.this, ""+ message, Toast.LENGTH_SHORT).show();
                            JSONArray jsonObject = obj.getJSONArray("data");

                            for (int i = 0; i < jsonObject.length(); i++) {

                                mapObject = new ArrayList<>();
                                JSONObject jsondata = jsonObject.getJSONObject(i);
                                DBhandler dBhandler = new DBhandler(getApplicationContext());
                                dBhandler.addSymptomsMaster(jsondata.getString("ATR_CODE"),
                                        jsondata.getString("PRT_CODE"),
                                        jsondata.getString("PRT_DESC"),
                                        jsondata.getString("PRT_DESC_ALT"),
                                        jsondata.getString("PRT_DESC_BENG"),
                                        jsondata.getString("PRT_SLNO"),
                                        jsondata.getString("ATR_DESC"),
                                        jsondata.getString("ATR_DESC_ALT"),
                                        jsondata.getString("ATR_DESC_BENG"),
                                        jsondata.getString("ATR_SLNO"),
                                        jsondata.getString("IMAGE_URL"));

                                if (!jsondata.getString("IMAGE_URL").equals("")){
                                    Picasso.get().load(jsondata.getString("IMAGE_URL"))
                                            .into(new Target() {
                                                      @Override
                                                      public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                                          try {
                                                              String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
                                                              File myDir = new File(root + "/symptom_image");

                                                              if (!myDir.exists()) {
                                                                  myDir.mkdirs();
                                                              }

                                                              String name = ++num + ".jpg";
                                                              myDir = new File(myDir, name);
                                                              FileOutputStream out = new FileOutputStream(myDir);
                                                              bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

                                                              out.flush();
                                                              out.close();
                                                          } catch(Exception e){
                                                              // some action
                                                          }
                                                      }
                                                      @Override
                                                      public void onBitmapFailed(Exception e,Drawable errorDrawable) {
                                                      }

                                                      @Override
                                                      public void onPrepareLoad(Drawable placeHolderDrawable) {
                                                      }
                                                  }
                                            );
                                }


                                mapObject.add(jsondata.getString("ATR_CODE"));
                                mapObject.add(jsondata.getString("PRT_DESC"));
                                mapObject.add(jsondata.getString("PRT_DESC_ALT"));
                                mapObject.add(jsondata.getString("PRT_DESC_BENG"));
                                mapObject.add(jsondata.getString("PRT_SLNO"));
                                mapObject.add(jsondata.getString("ATR_DESC"));
                                mapObject.add(jsondata.getString("ATR_DESC_ALT"));
                                mapObject.add(jsondata.getString("ATR_DESC_BENG"));
                                mapObject.add(jsondata.getString("ATR_SLNO"));

                                multiMap.put(jsondata.getString("PRT_CODE"),mapObject);


                                //

                            }

                            // }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })

        {
            @Override
            protected Map<String, String> getParams() {
                DBhandler dBhandler = new DBhandler(getApplicationContext());
                Cursor cursor = dBhandler.getFamilyDbData();
                //Family_Head_Model dmodel = new Family_Head_Model();
                Map<String,String> params = new HashMap<String,String>();
                params.put("req_type","get-symptoms-list");
                Log.d("deep", "getParams: "+params);
                return params;

            }
        };
        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    public void showSymptoms(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Signs And Symptoms");
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.signs_and_symptoms,null);

        builder.setView(customLayout);
        LinearLayout symptoms;
        symptoms = customLayout.findViewById(R.id.signsAndSymptomsDialog);
        DBhandler dBhandler = new DBhandler(getApplicationContext());
        TextView tvSignsSymptomsHeader,tvSignsSymptomsBody;
        ImageView ivSymptoms;

        //Family_Head_Model dmodel = new Family_Head_Model();
        ssModelArraylist = new ArrayList<>();

        checkBoxesSymptoms = new ArrayList<>();
        ArrayList<CheckBox> checkBoxesSymptoms2 = new ArrayList<>();
        HashMap<String,ArrayList<CheckBox>> symptomsMap = new HashMap<>();
        int chkId = 1001;

        for (Map.Entry<String, ArrayList<String>> entry : multiMap.entrySet()) {
            list1 = entry.getKey();

            Cursor cursor = dBhandler.getSymptomsMaster(list1);
            if (cursor.moveToFirst()) {
                tvSignsSymptomsHeader = new TextView(SurveyActivity.this);
                tvSignsSymptomsHeader.setPadding(10,10,10,10);
                tvSignsSymptomsHeader.setTextColor(Color.parseColor("#FFFFFF"));
                tvSignsSymptomsHeader.setGravity(Gravity.CENTER);
                tvSignsSymptomsHeader.setTextSize(20);
                tvSignsSymptomsHeader.setPadding(0,40,0,40);
                tvSignsSymptomsHeader.setBackgroundColor(Color.parseColor("#046874"));
                tvSignsSymptomsHeader.setText(cursor.getString(2));
                symptoms.addView(tvSignsSymptomsHeader);

                ivSymptoms = new ImageView(SurveyActivity.this);
                Picasso.get().load(cursor.getString(10)).into(ivSymptoms);
                symptoms.addView(ivSymptoms);
                do {
                    tvSignsSymptomsBody = new TextView(SurveyActivity.this);
                    tvSignsSymptomsBody.setPadding(20,0,0,0);
                    tvSignsSymptomsBody.setTextColor(Color.parseColor("#000000"));
                    tvSignsSymptomsBody.setBackgroundColor(Color.parseColor("#FFFDD0"));
                    tvSignsSymptomsBody.setGravity(Gravity.CENTER);
                    tvSignsSymptomsBody.setText(cursor.getString(6));
                    symptoms.addView(tvSignsSymptomsBody);
                    for (int i = 0; i < member.size(); i++){

                        cb = new CheckBox(SurveyActivity.this);
                        cb.setId(++chkId);
                        cb.setTag(R.id.Atr_Code,cursor.getString(0));
                        cb.setTag(R.id.memberId,member.get(i).getMember_id());
                        cb.setTag(R.id.symptomBody,cursor.getString(6));
                        cb.setTag(R.id.symptomHeader,cursor.getString(2));
                        cb.setTag(R.id.surveymemberID,memberSurveyIdArrayList.get(i));
                        cb.setText(member.get(i).getMemberName());
                        symptoms.addView(cb);
                        checkBoxesSymptoms.add(cb);
                        Cursor cursor1 = dBhandler.getSymptomsMember(member.get(i).getMember_id());
                        if (cursor1.moveToFirst()){
                            do {
                                if (cursor1.getString(0).equals(cb.getTag(R.id.Atr_Code))){
                                    cb.setChecked(true);

                                }
                            }while (cursor1.moveToNext());
                        }
                    }
                } while (cursor.moveToNext());
            }


        }

        builder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    DBhandler dBhandler;
                    @Override
                    public void onClick(DialogInterface dialog,int which)
                    {   ssModel = new SignsAndSymptomsModel();

                        dBhandler = new DBhandler(getApplicationContext());
                        SQLiteDatabase db = dBhandler.getWritableDatabase();
                        db.execSQL("delete from tbl_symptoms_member where family_id  = '" + familyid + "'" );
                        /*db.delete("tbl_symptoms_member", null,null);*/
                        for (int i = 0; i<checkBoxesSymptoms.size();i++){
                            if (checkBoxesSymptoms.get(i).isChecked()) {
                                dBhandler = new DBhandler(getApplicationContext());
                                dBhandler.addSymptomsMember(familySurveyId,
                                        String.valueOf(checkBoxesSymptoms.get(i).getTag(R.id.Atr_Code)),
                                        String.valueOf(checkBoxesSymptoms.get(i).getTag(R.id.memberId)),
                                        getIntent().getStringExtra("familyId"),
                                        String.valueOf(checkBoxesSymptoms.get(i).getText()),
                                        String.valueOf(checkBoxesSymptoms.get(i).getTag(R.id.symptomHeader)),
                                        String.valueOf(checkBoxesSymptoms.get(i).getTag(R.id.symptomBody)),
                                        "true",
                                        String.valueOf(checkBoxesSymptoms.get(i).getTag(R.id.surveymemberID)),format);
                                checkBoxesSymptoms.get(i).setChecked(true);
                                //ssModelArraylist.add(true);
                                //Log.d("testingcheck", "onClick: "+ssModelArraylist.set(i,checkBoxesSymptoms.get(i).isChecked()));
                                showSymptomsMembers();

                            }
                        }
                    }

                });
        // create and show
        // the alert dialog
        /**/
        dialog = builder.create();
        dialog.show();

    }

    public void showSymptomsMembers(){
        DBhandler dBhandler = new DBhandler(getApplicationContext());

        TextView tvMemberName,tvSymptomName;
        LinearLayout displaySymptomsMember = findViewById(R.id.ll_signsAndSymptoms);
        displaySymptomsMember.removeAllViews();
        for (int i = 0; i < member.size(); i++){
            Cursor cursor = dBhandler.getSymptomsMember(member.get(i).getMember_id());
            if (cursor.moveToFirst()) {
                tvMemberName = new TextView(SurveyActivity.this);
                tvMemberName.setPadding(10,10,10,10);
                tvMemberName.setTextColor(Color.parseColor("#000000"));
                tvMemberName.setGravity(Gravity.CENTER);
                String sourceString = "<b>" + cursor.getString(4) + "</b>" ;
                tvMemberName.setText(Html.fromHtml(sourceString));
                displaySymptomsMember.addView(tvMemberName);
                do {
                    tvSymptomName = new TextView(SurveyActivity.this);
                    tvSymptomName.setPadding(10,10,10,10);
                    tvSymptomName.setTextColor(Color.parseColor("#000000"));
                    tvSymptomName.setGravity(Gravity.CENTER);
                    String sourceString2 = "<b>" + " Symptoms : "+"</b>"+cursor.getString(6);
                    tvSymptomName.setText(Html.fromHtml(sourceString2));
                    displaySymptomsMember.addView(tvSymptomName);
                } while (cursor.moveToNext());
            }
        }
    }

    public void showEditTestFindings(){
        // Create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Test Findings");
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.testfindingsrecycler, null);
        builder.setView(customLayout);
        recyclerViewDialog = (RecyclerView) customLayout.findViewById(R.id.testFindingsDialogRecycler);
        ArrayList<MemberDetailsForDialogModel> list = new ArrayList<>();
        DBhandler dBhandler = new DBhandler(getApplicationContext());

        Cursor cursor1 = dBhandler.getTestFindings(getIntent().getStringExtra("familyId"));
        list2 = new ArrayList<>();
        //RecyclerView recyclerViewDialog = (RecyclerView) findViewById(R.id.rv_testFindings);
        if (cursor1.moveToFirst()) {
            do {
                MemberDetailsForDialogModel editModel = new MemberDetailsForDialogModel();
                editModel.setMemberName(cursor1.getString(3));
                editModel.setEditTextValueSys(cursor1.getString(4));
                editModel.setEditTextValueDia(cursor1.getString(5));
                editModel.setTypeSpinner(cursor1.getString(6));
                editModel.setEditTextValueValue(cursor1.getString(7));
                list2.add(editModel);
            } while (cursor1.moveToNext());
        }
        for (int i = 0; i < list2.size(); i++){
            editModel2 = new MemberDetailsForDialogModel();
            editModel2.setMemberName(list2.get(i).getMemberName());
            editModel2.setEditTextValueSys(list2.get(i).getEditTextValueSys());
            editModel2.setEditTextValueDia(list2.get(i).getEditTextValueDia());
            editModel2.setEditTextValueValue(list2.get(i).getEditTextValueValue());
            editModel2.setTypeSpinner(list2.get(i).getTypeSpinner());

            list.add(editModel2);

            TestFindingsAdapter testFindingsAdapter = new TestFindingsAdapter(this, list,getIntent().getStringExtra("familyId"));
            recyclerViewDialog.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false));
            recyclerViewDialog.setAdapter(testFindingsAdapter);


        }
        builder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    DBhandler dBhandler;
                    @Override
                    public void onClick(DialogInterface dialog,int which)
                    {
                        dBhandler = new DBhandler(getApplicationContext());
                        SQLiteDatabase db = dBhandler.getWritableDatabase();
                        db.execSQL("delete from tbl_test_findings where family_id  = '" + familyid + "'" );
                        for (int i = 0; i < member.size(); i++){
                            dBhandler.addTestFindings(familySurveyId,
                                    member.get(i).getMember_id(),
                                    getIntent().getStringExtra("familyId"),member.get(i).getMemberName(),
                                    TestFindingsAdapter.addMemberDialogArrayList.get(i).getEditTextValueSys(),
                                    TestFindingsAdapter.addMemberDialogArrayList.get(i).getEditTextValueDia(),
                                    TestFindingsAdapter.addMemberDialogArrayList.get(i).getTypeSpinner(),
                                    TestFindingsAdapter.addMemberDialogArrayList.get(i).getEditTextValueValue(),
                                    memberSurveyIdArrayList.get(i),format);
                            //setupTestFindingsDialogRecycler();
                            showTestFindingsBP();
                            showTestFindingsBS();
                        }
                    }

                });
        dialog = builder.create();
        dialog.show();
        dialog.getWindow().clearFlags( WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                |WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public void onSmokinClick(int position, boolean isTrue) {
        member.get(position).setSmoker(isTrue);
    }

    @Override
    public void onAlcoholicClick(int position, boolean isTrue) {
        member.get(position).setAlcoholic(isTrue);
    }

    @Override
    public void onAtalClick(int position, boolean isTrue) {
        member1.get(position).setAtal(isTrue);
    }

    @Override
    public void onAyushmanClick(int position, boolean isTrue) {
        member1.get(position).setAyush(isTrue);
    }

    private boolean isCameraPresentInPhone(){
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)){
            return  true;
        }else {
            return false;
        }
    }

    private void getCameraPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
    }

    private void recordVideo(){
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
        intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 10983040L);//5*1048*1048=5MB
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30);
        startActivityForResult(intent,VIDEO_RECORD_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VIDEO_RECORD_CODE){

            if (resultCode == RESULT_OK){
                ConnectivityManager cm = (ConnectivityManager) getApplicationContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
                    // connected to the internet
                    if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                        // connected to wifi
                        videoPath = data.getData();
                        uploadPDF(familySurveyId+".mp4",videoPath);
                        Intent i = new Intent(SurveyActivity.this,ShowSurveyActivity.class);
                        startActivity(i);
                    } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                        // connected to mobile data
                        videoPath = data.getData();
                        uploadPDF(familySurveyId+".mp4",videoPath);
                        Intent i = new Intent(SurveyActivity.this,ShowSurveyActivity.class);
                        startActivity(i);
                    }
                } else {
                    DBhandler dBhandler = new DBhandler(getApplicationContext());
                    for (int i = 0; i < member.size(); i++){
                        dBhandler.addVideoPath(familySurveyId, videoPath,format);
                    }

                    Toast.makeText(SurveyActivity.this,
                            "Data saved locally due to no Internet Connection", Toast.LENGTH_LONG).show();
                    clearTables();
                    Intent i = new Intent(SurveyActivity.this,ShowSurveyActivity.class);
                    startActivity(i);
                    // not connected to the internet
                }



            }
            else if (resultCode == RESULT_CANCELED){

            }else{

            }
        }
    }

    public void locationPermission(){
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void gpsTracker(){
         //Get permissions
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define a listener that responds to location updates

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        } else {
            getLocation();
        }

    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes",
                new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                SurveyActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                SurveyActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        } else {
            GPSTracker abc = new GPSTracker(this);
            latitude = String.valueOf(abc.getLatitude());
            longitude = String.valueOf(abc.getLongitude());

        }
    }

    public void onBackPressed() {
        AlertDialog diaBox = AskOption();
        diaBox.show();
    }

    private AlertDialog AskOption()
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                .setTitle("ARE YOU SURE YOU WANT TO EXIT THE SURVEY?")
                .setMessage("All your unsaved data will be deleted")


                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        clearTables();
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        return myQuittingDialogBox;

    }
    public void clearTables(){
        SQLiteDatabase db = dBhandler.getWritableDatabase();
        db.execSQL("delete from tbl_general_habits_alcohol where group_surveyid  = '" + familySurveyId + "'" );
        db.execSQL("delete from tbl_test_findings where group_surveyid  = '" + familySurveyId + "'" );
        db.execSQL("delete from tbl_hci_atal_amrit where group_surveyid  = '" + familySurveyId + "'" );
        db.execSQL("delete from tbl_other_info where group_surveyid  = '" + familySurveyId + "'" );
        db.execSQL("delete from tbl_symptoms_member where group_surveyid  = '" + familySurveyId + "'" );
    }

    private void saveDataToServer() {
        int count = 0;

        showSimpleProgressDialog(this, "Loading...","Saving",false);
        //JSONArray abc = new JSONArray();
        JSONObject js = new JSONObject();
        ArrayList<String> memberID = new ArrayList<>();
        dBhandler = new DBhandler(getApplicationContext());


        try {
            JSONObject jsonobject = new JSONObject();
            JSONObject jsonobjectHeadDetails = new JSONObject();

            JSONObject jsonobjectMemberData;
            JSONArray memberArray = new JSONArray();


            JSONObject jsonobjectSymptomsData;

            JSONArray symptomArray1;

            JSONObject jsonobjectSymptoms;

            JSONObject storingSymptoms = new JSONObject();

            JSONArray symptomsArray = new JSONArray();

            Cursor cursor = dBhandler.getFamilyDbData();
            if (cursor.moveToFirst()){
                do {
                    if (cursor.getString(0).equals(familyid)) {
                        jsonobjectHeadDetails.put(cursor.getColumnName(1), cursor.getString(1));
                        jsonobjectHeadDetails.put(cursor.getColumnName(2), cursor.getString(2));
                        jsonobjectHeadDetails.put(cursor.getColumnName(3), cursor.getString(3));
                        jsonobjectHeadDetails.put(cursor.getColumnName(4), cursor.getString(4));
                        jsonobjectHeadDetails.put(cursor.getColumnName(5), cursor.getString(5));
                        jsonobjectHeadDetails.put(cursor.getColumnName(6), cursor.getString(6));
                        jsonobjectHeadDetails.put(cursor.getColumnName(7), cursor.getString(7));
                        jsonobjectHeadDetails.put(cursor.getColumnName(8), cursor.getString(8));
                        jsonobjectHeadDetails.put(cursor.getColumnName(9), cursor.getString(9));
                        jsonobjectHeadDetails.put(cursor.getColumnName(10), cursor.getString(10));
                        jsonobjectHeadDetails.put(cursor.getColumnName(0), cursor.getString(0));

                    }
                }while (cursor.moveToNext());
            }

            Cursor cursor2 = dBhandler.getFamilyMemberList(familyid);

            if (cursor2.moveToFirst()){
                do {
                    jsonobjectMemberData = new JSONObject();

                    jsonobjectMemberData.put(cursor2.getColumnName(1), cursor2.getString(1));
                    jsonobjectMemberData.put(cursor2.getColumnName(2), cursor2.getString(2));
                    jsonobjectMemberData.put(cursor2.getColumnName(3), cursor2.getString(3));
                    jsonobjectMemberData.put(cursor2.getColumnName(4), cursor2.getString(4));
                    jsonobjectMemberData.put(cursor2.getColumnName(5), cursor2.getString(5));
                    jsonobjectMemberData.put(cursor2.getColumnName(6), cursor2.getString(6));
                    jsonobjectMemberData.put(cursor2.getColumnName(7), cursor2.getString(7));
                    jsonobjectMemberData.put(cursor2.getColumnName(8), cursor2.getString(8));
                    jsonobjectMemberData.put(cursor2.getColumnName(9), cursor2.getString(9));
                    jsonobjectMemberData.put(cursor2.getColumnName(10), cursor2.getString(10));
                    jsonobjectMemberData.put(cursor2.getColumnName(0), cursor2.getString(0));
                    jsonobjectMemberData.put(cursor2.getColumnName(11), cursor2.getString(11));
                    jsonobjectMemberData.put(cursor2.getColumnName(12), cursor2.getString(12));
                    jsonobjectMemberData.put(cursor2.getColumnName(13), cursor2.getString(13));
                    jsonobjectMemberData.put(cursor2.getColumnName(14), cursor2.getString(14));
                    jsonobjectMemberData.put(cursor2.getColumnName(14), cursor2.getString(14));
                    jsonobjectMemberData.put(cursor2.getColumnName(15), cursor2.getString(15));
                    jsonobjectMemberData.put(cursor2.getColumnName(16), cursor2.getString(16));
                    jsonobjectMemberData.put(cursor2.getColumnName(17), cursor2.getString(17));
                    jsonobjectMemberData.put(cursor2.getColumnName(18), cursor2.getString(18));

                    memberArray.put(jsonobjectMemberData);
                    memberID.add(cursor2.getString(0));
                } while (cursor2.moveToNext());
            }

            Cursor cursor3 = dBhandler.getGeneralHabitsAlcohol(familyid);
            Cursor cursor4 = dBhandler.getTestFindings(familyid);
            Cursor cursor5 = dBhandler.getHCIAtalAmrit(familyid);
            Cursor cursor9 = dBhandler.getOtherInfo(familyid);
            for (int i = 0; i<memberID.size();i++){
                //General Habits
                Cursor cursor6 = dBhandler.getSymptomsMember(memberID.get(i));
                jsonobjectSymptomsData = new JSONObject();
                if (cursor3.moveToFirst()){
                    do {
                        if (cursor3.getString(0).equals(memberID.get(i))){
                            jsonobjectSymptomsData.put(cursor3.getColumnName(0), cursor3.getString(0));
                            jsonobjectSymptomsData.put(cursor3.getColumnName(1), cursor3.getString(1));
                            jsonobjectSymptomsData.put(cursor3.getColumnName(2), cursor3.getString(2));
                            jsonobjectSymptomsData.put(cursor3.getColumnName(3), cursor3.getString(3));
                            jsonobjectSymptomsData.put(cursor3.getColumnName(4), cursor3.getString(4));
                            jsonobjectSymptomsData.put(cursor3.getColumnName(5), cursor3.getString(5));
                            jsonobjectSymptomsData.put(cursor3.getColumnName(6), cursor3.getString(6));
                            jsonobjectSymptomsData.put(cursor3.getColumnName(7), cursor3.getString(7));
                            jsonobjectSymptomsData.put(cursor3.getColumnName(8), cursor3.getString(8));
                        }

                    }while (cursor3.moveToNext());
                }

                if (cursor4.moveToFirst()){
                    do {
                        if (cursor4.getString(0).equals(memberID.get(i))){
                            jsonobjectSymptomsData.put(cursor4.getColumnName(0), cursor4.getString(0));
                            jsonobjectSymptomsData.put(cursor4.getColumnName(1), cursor4.getString(1));
                            jsonobjectSymptomsData.put(cursor4.getColumnName(2), cursor4.getString(2));
                            jsonobjectSymptomsData.put(cursor4.getColumnName(3), cursor4.getString(3));
                            jsonobjectSymptomsData.put(cursor4.getColumnName(4), cursor4.getString(4));
                            jsonobjectSymptomsData.put(cursor4.getColumnName(5), cursor4.getString(5));
                            jsonobjectSymptomsData.put(cursor4.getColumnName(6), cursor4.getString(6));
                            jsonobjectSymptomsData.put(cursor4.getColumnName(7), cursor4.getString(7));
                            jsonobjectSymptomsData.put(cursor4.getColumnName(8), cursor4.getString(8));
                        }

                    }while (cursor4.moveToNext());
                }
                //---- health card
                if (cursor5.moveToFirst()){
                    do {
                        if (cursor5.getString(0).equals(memberID.get(i))){
                            jsonobjectSymptomsData.put(cursor5.getColumnName(0), cursor5.getString(0));
                            jsonobjectSymptomsData.put(cursor5.getColumnName(1), cursor5.getString(1));
                            jsonobjectSymptomsData.put(cursor5.getColumnName(2), cursor5.getString(2));
                            jsonobjectSymptomsData.put(cursor5.getColumnName(3), cursor5.getString(3));
                            jsonobjectSymptomsData.put(cursor5.getColumnName(4), cursor5.getString(4));
                            jsonobjectSymptomsData.put(cursor5.getColumnName(5), cursor5.getString(5));
                            jsonobjectSymptomsData.put(cursor5.getColumnName(6), cursor5.getString(6));

                        }

                    }while (cursor5.moveToNext());
                }







                if (cursor9.moveToFirst()){
                    do {
                        if (cursor9.getString(0).equals(memberID.get(i))){
                            jsonobjectSymptomsData.put(cursor9.getColumnName(0), cursor9.getString(0));
                            jsonobjectSymptomsData.put(cursor9.getColumnName(1), cursor9.getString(1));
                            jsonobjectSymptomsData.put(cursor9.getColumnName(2), cursor9.getString(2));
                            jsonobjectSymptomsData.put(cursor9.getColumnName(3), cursor9.getString(3));
                            jsonobjectSymptomsData.put(cursor9.getColumnName(4), cursor9.getString(4));

                        }

                    }while (cursor9.moveToNext());
                }
                symptomsArray.put(jsonobjectSymptomsData);

                // symptoms---
                if (cursor6.moveToFirst()){
                    symptomArray1 = new JSONArray();

                    do {
                        jsonobjectSymptoms = new JSONObject();
                        if (cursor6.getString(1).equals(memberID.get(i))){
                            jsonobjectSymptoms.put(cursor6.getColumnName(0), cursor6.getString(0));
                            jsonobjectSymptoms.put(cursor6.getColumnName(1), cursor6.getString(1));
                            jsonobjectSymptoms.put(cursor6.getColumnName(2), cursor6.getString(2));
                            jsonobjectSymptoms.put(cursor6.getColumnName(3), cursor6.getString(3));
                            jsonobjectSymptoms.put(cursor6.getColumnName(4), cursor6.getString(4));
                            jsonobjectSymptoms.put(cursor6.getColumnName(5), cursor6.getString(5));
                            jsonobjectSymptoms.put(cursor6.getColumnName(6), cursor6.getString(6));
                            jsonobjectSymptoms.put(cursor6.getColumnName(7), cursor6.getString(7));
                            jsonobjectSymptoms.put(cursor6.getColumnName(8), cursor6.getString(8));

                        }
                        symptomArray1.put(jsonobjectSymptoms);

                    }while (cursor6.moveToNext());
                    jsonobjectSymptomsData.put("Symptom",symptomArray1);
                    /*symptomsArray.put(symptomArray1);*/
/*
                    symptomsArray.put(100, jsonobjectSymptoms);
*/
                }

                //jsonobjectSymptoms.put(memberID.get(i),jsonobjectSymptomsData);
            }

            jsonobject.put("Head Data", jsonobjectHeadDetails);
            jsonobject.put("Member Data", memberArray);
            jsonobject.put("Symptoms Header", symptomsArray);
         //   jsonobject.put("Symptoms Details", storingSymptoms);
            js.put("data", jsonobject);

        }catch (JSONException e) {
            e.printStackTrace();
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLstring,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("rajnikant1", ">>" + response);
                        try {
                            removeSimpleProgressDialog();

                            JSONObject obj = new JSONObject(response);
                            Log.d("rajnikant2", ">>get result" + obj);

                            //String message = obj.getString("message");
                            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();

                            // }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("rajnikant3", ">>exception" + e.getMessage());
                        }

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        //Log.d("rajnikant", ">>get result" + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String,String> params = new HashMap<String,String>();
                params.put("req_type","save-survey");
                params.put("serverdata", js.toString());

                return params;

            }
        };

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public static void removeSimpleProgressDialog() {
        try {
            if (mProgressDialog != null) {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                }
            }
        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();

        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void showSimpleProgressDialog(Context context, String title,
                                                String msg, boolean isCancelable) {
        try {
            if (mProgressDialog == null) {
                mProgressDialog = ProgressDialog.show(context, title, msg);
                mProgressDialog.setCancelable(isCancelable);
            }

            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }

        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();
        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTeleMedClick(int position, boolean isTrue) {
        member2.get(position).setTeleMed(isTrue);
    }

    @Override
    public void onOpdClick(int position, boolean isTrue) {
        member2.get(position).setOpd(isTrue);
    }

    @Override
    public void onAmbulanceClick(int position, boolean isTrue) {
        member2.get(position).setAmbulance(isTrue);
    }

    private void uploadPDF(final String pdfname, Uri pdffile){
        String Url = "http://172.16.12.98:9000/api/index.php";
        InputStream iStream = null;
        try {
            iStream = getContentResolver().openInputStream(pdffile);
            final byte[] inputData = getBytes(iStream);

            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, Url,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            Log.d("ressssssoo",new String(response.data));
                            rQueue.getCache().clear();
                            try {
                                JSONObject jsonObject = new JSONObject(new String(response.data));
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"),
                                        Toast.LENGTH_LONG).show();
                                Log.d("fileupload", "onErrorResponse: "+jsonObject.getString("message"));
                                //jsonObject.toString().replace("\\\\","");

                            } catch (JSONException e) {

                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("fileupload", "onErrorResponse: "+error.getMessage());
                        }
                    }) {

                /*
                 * If you want to add more parameters with the image
                 * you can do it here
                 * here we have only one parameter with the image
                 * which is tags
                 * */
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    // params.put("tags", "ccccc");  add string parameters
                    return params;
                }

                /*
                 *pass files using below method
                 * */
                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    params.put("video_consent", new DataPart(pdfname ,inputData));
                    return params;
                }
            };

            volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            rQueue = Volley.newRequestQueue(SurveyActivity.this);
            rQueue.add(volleyMultipartRequest);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

}