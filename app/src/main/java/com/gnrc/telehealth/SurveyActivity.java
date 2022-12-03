package com.gnrc.telehealth;

import static com.gnrc.telehealth.FamilyHeadActivity.removeSimpleProgressDialog;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gnrc.telehealth.Adapter.GeneralHabitsAdapter;
import com.gnrc.telehealth.Adapter.HealthCardAdapter;
import com.gnrc.telehealth.Adapter.TestFindingsAdapter;
import com.gnrc.telehealth.DatabaseSqlite.DBhandler;
import com.gnrc.telehealth.Model.MemberDetailsForDialogModel;
import com.gnrc.telehealth.Model.SignsAndSymptomsModel;
import com.gnrc.telehealth.inmeetingfunction.customizedmeetingui.view.adapter.AttenderVideoAdapter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class SurveyActivity extends AppCompatActivity implements GeneralHabitsAdapter.GeneralHabits, HealthCardAdapter.HealthCard {

    RecyclerView  recyclerViewDialog, covidRecycler,generalRecycler, healthCardRecycler;
    TextView genHab, testFin, heaCaIn, covFacIn, signsSymptoms, otherinfo;
    String latitude, longitude,list1,familyid;
    CheckBox cb, cb1, cb2;
    AlertDialog dialog;
    int num = 1000;
    int finalCount;

    SharedPreferences mPreferences,mPreferences1,mPreferences2;
    SharedPreferences.Editor preferencesEditor;
    private static final int REQUEST_LOCATION = 1;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.CHINESE);
    SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd-MM-yyyy_hh:mm:ss", Locale.CHINESE);
    String format, format2;

    ArrayList<CheckBox> checkBoxesSymptoms;
    ArrayList<String> mapObject;
    ArrayList<String> memberSurveyIdArrayList;
    ArrayList<MemberDetailsForDialogModel> list;
    ArrayList<MemberDetailsForDialogModel> member;
    ArrayList<MemberDetailsForDialogModel> member1;
    ArrayList<MemberDetailsForDialogModel> list2;

    MemberDetailsForDialogModel editModel;
    MemberDetailsForDialogModel editModel2;
    SignsAndSymptomsModel signsAndSymptomsModel;

    Button editButton, saveSurvey;
    TreeMap<String, ArrayList<String>> multiMap;
    SignsAndSymptomsModel ssModel;
    AttenderVideoAdapter.ItemClickListener itemClickListener;

    private boolean myBoolean = false;
    public static ArrayList<Boolean> ssModelArraylist;
    private String URLstring = "https://www.gnrctelehealth.com/telehealth_api/";
    private TextView txtGeneralHabits,txtHealthCard;
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

        //showGeneralHabitsSmoking();
        showGeneralHabitsAlcohol(CallingTypeLoad);
        showTestFindingsBP();
        showTestFindingsBS();
        showHCIAtalAmrit(CallingTypeLoad);
        //showHCIAyushmanBharat();
        //showCovidFactsInfo();
        fetchingSymptoms();
        showSymptomsMembers();
        showOiAmbulance();
        showOiOpd();
        showOiTelemed();
        locationPermission();

        //setupTestFindingsDialogRecycler();
    }

    private void initView(){
        genHab = findViewById(R.id.tvGh);
        testFin = findViewById(R.id.tvTf);
        heaCaIn = findViewById(R.id.tvHci);
        //covFacIn = findViewById(R.id.tvCfi);
        signsSymptoms = findViewById(R.id.tvSaS);
        otherinfo = findViewById(R.id.tvOtherInfo);
        editButton = findViewById(R.id.editBu);
        saveSurvey = findViewById(R.id.btnSaveSurvey);

        txtGeneralHabits = findViewById(R.id.txt_general_habits);
        txtHealthCard = findViewById(R.id.tvHciInfo);
    }

    private void initListener(){
        saveSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor1 = dBhandler.getGeneralHabitsAlcohol(familyid);
                Cursor cursor2 = dBhandler.getTestFindings(familyid);
                Cursor cursor3 = dBhandler.getHCIAtalAmrit(familyid);
                Cursor cursor4 = dBhandler.getSymptomsMemberByFamilyId(familyid);
                Cursor cursor5 = dBhandler.getOtherInfoOpd(familyid);
                Cursor cursor6 = dBhandler.getOtherInfoAmbulance(familyid);
                Cursor cursor7 = dBhandler.getOtherInfoTelemed(familyid);

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
                    Toast.makeText(SurveyActivity.this, "Please Complete OPD Survey",
                            Toast.LENGTH_SHORT).show();
                }
                else if (cursor6.getCount() == 0){
                    Toast.makeText(SurveyActivity.this, "Please Complete Ambulance Survey",
                            Toast.LENGTH_SHORT).show();
                }
                else if (cursor7.getCount() == 0){
                    Toast.makeText(SurveyActivity.this, "Please Complete Tele Medicine Survey",
                            Toast.LENGTH_SHORT).show();
                }else {
                    if (isCameraPresentInPhone()){
                        getCameraPermission();
                        recordVideo();
                    }else {

                    }
                }



               /* Intent i = new Intent(SurveyActivity.this,FamilyHeadActivity.class);
                startActivity(i);
                Toast.makeText(SurveyActivity.this, "Data Saved Successfully", Toast.LENGTH_SHORT).show();*/
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
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

                        if (finalCount == 0){
                            for (int i = 0; i < member.size(); i++){
                                String isSmoker = member.get(i).isSmoker() ? "Yes" : "No";
                                String isAlcoholic = member.get(i).isAlcoholic() ? "Yes" : "No";

                                int getCount = mPreferences1.getInt(familySurveyId, 0);
                                finalCount = ++getCount;
                                preferencesEditor = mPreferences1.edit();
                                preferencesEditor.putInt(familySurveyId, finalCount);
                                preferencesEditor.apply();

                                memberSurveyIdArrayList.add("M_SRV_" + familySurveyId + "_00" + finalCount);

                                dBhandler.addGeneralHabitsAlcohol(member.get(i).getMember_id(),
                                        getIntent().getStringExtra("familyId"),member.get(i).getMemberName(),
                                        isSmoker,
                                        isAlcoholic,"M_SRV_" + familySurveyId + "_00"+finalCount,
                                        latitude,longitude,format);

                            }
                        }else {
                            for (int i = 0; i < member.size(); i++){
                                String isSmoker = member.get(i).isSmoker() ? "Yes" : "No";
                                String isAlcoholic = member.get(i).isAlcoholic() ? "Yes" : "No";
                                dBhandler.addGeneralHabitsAlcohol(member.get(i).getMember_id(),
                                        getIntent().getStringExtra("familyId"),
                                        member.get(i).getMemberName(), isSmoker,
                                        isAlcoholic, memberSurveyIdArrayList.get(i), latitude,longitude,format);

                            }
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
                editModel.setMemberName(cursor1.getString(2));
                editModel.setEditTextValueSys(cursor1.getString(3));
                editModel.setEditTextValueDia(cursor1.getString(4));
                editModel.setTypeSpinner(cursor1.getString(5));
                editModel.setEditTextValueValue(cursor1.getString(6));
                list2.add(editModel);
            } while (cursor1.moveToNext());
        }

        /*if (cursor1.getCount() > 0 ){
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
                editButton.setVisibility(View.VISIBLE);
            }
        }else {*/
            for (int i = 0; i < member.size(); i++){
                editModel = new MemberDetailsForDialogModel();
                editModel.setMemberName(member.get(i).getMemberName());
                list.add(editModel);
                TestFindingsAdapter testFindingsAdapter = new TestFindingsAdapter(this, list,getIntent().getStringExtra("familyId"));
                recyclerViewDialog.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false));
                recyclerViewDialog.setAdapter(testFindingsAdapter);
                editButton.setVisibility(View.GONE);
            }
        if (cursor1.getCount() > 0 ){
            editButton.setVisibility(View.VISIBLE);
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
                            dBhandler.addTestFindings(member.get(i).getMember_id(),
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
                            dBhandler.addHCIAtalAmrit(member1.get(i).getMember_id(),
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
        final View customLayout = getLayoutInflater().inflate(R.layout.other_info_dialog,null);

        builder.setView(customLayout);

        DBhandler dBhandler = new DBhandler(getApplicationContext());
        LinearLayout teleMed = customLayout.findViewById(R.id.llTeleMedicine);
        LinearLayout opd = customLayout.findViewById(R.id.llOpd);
        LinearLayout ambBook = customLayout.findViewById(R.id.llAmbulance);



        ArrayList<CheckBox> checkBoxesTeleMed = new ArrayList<>();
        ArrayList<CheckBox> checkBoxesOpd= new ArrayList<>();
        ArrayList<CheckBox> checkBoxesAmbulsnce= new ArrayList<>();


        for (int i = 0; i < member.size(); i++){
            cb = new CheckBox(SurveyActivity.this);
            cb1 = new CheckBox(SurveyActivity.this);
            cb2 = new CheckBox(SurveyActivity.this);
            cb.setText(member.get(i).getMemberName());
            cb.setTag(member.get(i).getMember_id());
            teleMed.addView(cb);
            checkBoxesTeleMed.add(cb);
            cb1.setText(member.get(i).getMemberName());
            cb1.setTag(member.get(i).getMember_id());
            opd.addView(cb1);
            checkBoxesOpd.add(cb1);
            cb2.setText(member.get(i).getMemberName());
            cb2.setTag(member.get(i).getMember_id());
            ambBook.addView(cb2);
            checkBoxesAmbulsnce.add(cb2);
            Cursor cursor1 = dBhandler.getOtherInfoTelemed(getIntent().getStringExtra("familyId"));
            if (cursor1.moveToFirst()){
                do {
                    if (cursor1.getString(0).equals(cb.getTag())){
                        cb.setChecked(true);

                    }
                }while (cursor1.moveToNext());
            }
            Cursor cursor2 = dBhandler.getOtherInfoOpd(getIntent().getStringExtra("familyId"));
            if (cursor2.moveToFirst()){
                do {
                    if (cursor2.getString(0).equals(cb1.getTag())){
                        cb1.setChecked(true);
                    }
                }while (cursor2.moveToNext());
            }
            Cursor cursor3 = dBhandler.getOtherInfoAmbulance(getIntent().getStringExtra("familyId"));
            if (cursor3.moveToFirst()){
                do {
                    if (cursor3.getString(0).equals(cb2.getTag())){
                        cb2.setChecked(true);

                    }
                }while (cursor3.moveToNext());
            }
        }

        // add a button
        builder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    DBhandler dBhandler;
                    @Override
                    public void onClick(DialogInterface dialog,int which)
                    {
                        dBhandler = new DBhandler(getApplicationContext());
                        SQLiteDatabase db = dBhandler.getWritableDatabase();
                        db.execSQL("delete from tbl_other_info_telemed where family_id  = '" + familyid + "'" );
                        db.execSQL("delete from tbl_other_info_opd where family_id  = '" + familyid + "'" );
                        db.execSQL("delete from tbl_other_info_ambulance where family_id  = '" + familyid + "'" );

                       /* db.delete("tbl_other_info_telemed", null,null);
                        db.delete("tbl_other_info_opd",null,null);
                        db.delete("tbl_other_info_ambulance",null,null);*/

                        for (int i = 0; i<checkBoxesTeleMed.size();i++){
                            if (checkBoxesTeleMed.get(i).isChecked()) {
                                dBhandler = new DBhandler(getApplicationContext());
                                dBhandler.addOtherInfoTelemed(
                                        String.valueOf(checkBoxesTeleMed.get(i).getTag()),
                                        getIntent().getStringExtra("familyId"),
                                        String.valueOf(checkBoxesTeleMed.get(i).getText()),
                                        memberSurveyIdArrayList.get(i),format);
                                checkBoxesTeleMed.get(i).setChecked(true);
                                showOiTelemed();
                            }
                        }
                        //
                        for (int i = 0; i<checkBoxesOpd.size();i++){
                            if (checkBoxesOpd.get(i).isChecked()) {
                                dBhandler = new DBhandler(getApplicationContext());
                                dBhandler.addOtherInfoOpd(
                                        String.valueOf(checkBoxesOpd.get(i).getTag()),
                                        getIntent().getStringExtra("familyId"),
                                        String.valueOf(checkBoxesOpd.get(i).getText()),
                                        memberSurveyIdArrayList.get(i),format);
                                checkBoxesOpd.get(i).setChecked(true);
                                showOiOpd();
                            }
                        }
                        for (int i = 0; i<checkBoxesAmbulsnce.size();i++){
                            if (checkBoxesAmbulsnce.get(i).isChecked()) {
                                dBhandler = new DBhandler(getApplicationContext());
                                dBhandler.addOtherInfoAmbulance(
                                        String.valueOf(checkBoxesAmbulsnce.get(i).getTag()),
                                        getIntent().getStringExtra("familyId"),
                                        String.valueOf(checkBoxesAmbulsnce.get(i).getText()),
                                        memberSurveyIdArrayList.get(i),format);
                                checkBoxesAmbulsnce.get(i).setChecked(true);
                                showOiAmbulance();
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

    public void setupTestFindingsDialogRecycler(){
        /*DBhandler dBhandler = new DBhandler(getApplicationContext());
        Cursor cursor = dBhandler.getTestFindings(getIntent().getStringExtra("familyId"));
        ArrayList<MemberDetailsForDialogModel> list2 = new ArrayList<>();
        //RecyclerView recyclerViewDialog = (RecyclerView) findViewById(R.id.rv_testFindings);
        if (cursor.moveToFirst()) {
            do {
                editModel = new MemberDetailsForDialogModel();
                editModel.setMemberName(cursor.getString(2));
                editModel.setEditTextValueSys(cursor.getString(3));
                editModel.setEditTextValueDia(cursor.getString(4));
                editModel.setTypeSpinner(cursor.getString(5));
                editModel.setEditTextValueValue(cursor.getString(6));
                list2.add(editModel);
            } while (cursor.moveToNext());
            TestFindingsAdapterSurveyActivity testFindingsAdapterSurveyActivity =
                    new TestFindingsAdapterSurveyActivity(this, list2,getIntent().getStringExtra("familyId"));
            recyclerViewDialog.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false));
            recyclerViewDialog.setAdapter(testFindingsAdapterSurveyActivity);
        }*/
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
                String sourceString = "<b>" + cursor.getString(2) + "</b>" + "<br/>" + "SYS:"+cursor.getString(3)+"<br/>"+
                "DIA:"+cursor.getString(4);
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
                String sourceString = "<b>" + cursor.getString(2) + "</b>" + "<br/>"+
                        "TYPE:"+cursor.getString(5)+"<br/>"+"VALUE:"+cursor.getString(6);
                tvTestFindings.setText(Html.fromHtml(sourceString));
                displayTestFindings.addView(tvTestFindings);
            } while (cursor.moveToNext());
        }
    }
/*
    public void showGeneralHabitsSmoking(){
        DBhandler dBhandler = new DBhandler(getApplicationContext());
        Cursor cursor = dBhandler.getGeneralHabitsSmoking(getIntent().getStringExtra("familyId"));
        TextView tvSmoking;
        LinearLayout displaySmoking = findViewById(R.id.ll_smokingDisplay);
        displaySmoking.removeAllViews();
        if (cursor.moveToFirst()) {
            do {
                tvSmoking = new TextView(SurveyActivity.this);
                tvSmoking.setPadding(10,10,10,10);
                tvSmoking.setTextColor(Color.parseColor("#000000"));
                tvSmoking.setGravity(Gravity.CENTER);
                tvSmoking.setText(cursor.getString(2));
                displaySmoking.addView(tvSmoking);
            } while (cursor.moveToNext());
        }

    }
*/

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

                        Cursor cursorGeneralHabits = dBhandler.getGeneralHabitsAlcoholByMember(cursorFamilyMember.getString(0));

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
                        sourceString += "<b>" + cursor.getString(2) + "</b><br/>" +
                                "<b>Smoking? :</b>" + cursor.getString(3) + "<br/>" +
                                "<b>Alcohol? :</b>" + cursor.getString(4) + "<br/>";
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

    public void showHCIAtalAmrit( String callingType){
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
                        sourceString += "<b>" + cursor.getString(2) + "</b><br/>" +
                                "<b>Atal Amrit Beneficiary? :</b>" + cursor.getString(3) + "<br/>" +
                                "<b>Ayushman Beneficiary? :</b>" + cursor.getString(4) + "<br/>";
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

/*
    public void showHCIAyushmanBharat(){
        DBhandler dBhandler = new DBhandler(getApplicationContext());
        Cursor cursor = dBhandler.getHCIAyushmanBharat(getIntent().getStringExtra("familyId"));
        TextView tvAyushman;
        LinearLayout displayAyushmanBharat = findViewById(R.id.ll_AyusBhaDisplay);
        displayAyushmanBharat.removeAllViews();
        if (cursor.moveToFirst()) {
            do {
                tvAyushman = new TextView(SurveyActivity.this);
                tvAyushman.setPadding(10,10,10,10);
                tvAyushman.setTextColor(Color.parseColor("#000000"));
                tvAyushman.setGravity(Gravity.CENTER);
                tvAyushman.setText(cursor.getString(2));
                displayAyushmanBharat.addView(tvAyushman);
            } while (cursor.moveToNext());
        }
    }
*/
    public void showOiTelemed(){
        DBhandler dBhandler = new DBhandler(getApplicationContext());
        Cursor cursor = dBhandler.getOtherInfoTelemed(getIntent().getStringExtra("familyId"));
        TextView tvTeleMedicine;
        LinearLayout displayTelemed = findViewById(R.id.ll_OtherInfoTele);
        displayTelemed.removeAllViews();
        if (cursor.moveToFirst()) {
            do {
                tvTeleMedicine = new TextView(SurveyActivity.this);
                tvTeleMedicine.setPadding(10,10,10,10);
                tvTeleMedicine.setTextColor(Color.parseColor("#000000"));
                tvTeleMedicine.setGravity(Gravity.CENTER);
                tvTeleMedicine.setText(cursor.getString(2));
                displayTelemed.addView(tvTeleMedicine);
            } while (cursor.moveToNext());
        }
    }

    public void showOiOpd(){
        DBhandler dBhandler = new DBhandler(getApplicationContext());
        Cursor cursor = dBhandler.getOtherInfoOpd(getIntent().getStringExtra("familyId"));
        TextView tvOpd;
        LinearLayout displayOpd = findViewById(R.id.ll_OtherInfoOpd);
        displayOpd.removeAllViews();
        if (cursor.moveToFirst()) {
            do {
                tvOpd = new TextView(SurveyActivity.this);
                tvOpd.setPadding(10,10,10,10);
                tvOpd.setTextColor(Color.parseColor("#000000"));
                tvOpd.setGravity(Gravity.CENTER);
                tvOpd.setText(cursor.getString(2));
                displayOpd.addView(tvOpd);
            } while (cursor.moveToNext());
        }
    }

    public void showOiAmbulance(){
        DBhandler dBhandler = new DBhandler(getApplicationContext());
        Cursor cursor = dBhandler.getOtherInfoAmbulance(getIntent().getStringExtra("familyId"));
        TextView tvAmbulance;
        LinearLayout displayAmbulance = findViewById(R.id.ll_OtherInfoAmbulance);
        displayAmbulance.removeAllViews();
        if (cursor.moveToFirst()) {
            do {
                tvAmbulance = new TextView(SurveyActivity.this);
                tvAmbulance.setPadding(10,10,10,10);
                tvAmbulance.setTextColor(Color.parseColor("#000000"));
                tvAmbulance.setGravity(Gravity.CENTER);
                tvAmbulance.setText(cursor.getString(2));
                displayAmbulance.addView(tvAmbulance);
            } while (cursor.moveToNext());
        }
    }

/*
    public void showCovidFactsInfo(){
        DBhandler dBhandler = new DBhandler(getApplicationContext());
        Cursor cursor = dBhandler.getCovidFacts(getIntent().getStringExtra("familyId"));
        TextView tvCovidFacts;
        LinearLayout displayAyushmanBharat = findViewById(R.id.ll_covidFacts);
        displayAyushmanBharat.removeAllViews();
        if (cursor.moveToFirst()) {
            do {
                tvCovidFacts = new TextView(SurveyActivity.this);
                tvCovidFacts.setPadding(10,10,10,10);
                tvCovidFacts.setTextColor(Color.parseColor("#000000"));
                tvCovidFacts.setGravity(Gravity.CENTER);
                String sourceString = "<b>" + cursor.getString(2) + "</b>" + "<br/>" + "<b>" +
                        "Covid : "+"</b>"+cursor.getString(3)+"<br/>"+"<b>" +
                        "Vaccine Status : "+"</b>"+cursor.getString(4)+"<br/>"+"<b>" +
                        "Reason : "+"</b>"+cursor.getString(5);
                tvCovidFacts.setText(Html.fromHtml(sourceString));
                displayAyushmanBharat.addView(tvCovidFacts);
            } while (cursor.moveToNext());
        }
    }
*/

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
                                dBhandler.addSymptomsMember(String.valueOf(checkBoxesSymptoms.get(i).getTag(R.id.Atr_Code)),
                                        String.valueOf(checkBoxesSymptoms.get(i).getTag(R.id.memberId)),
                                        getIntent().getStringExtra("familyId"),
                                        String.valueOf(checkBoxesSymptoms.get(i).getText()),
                                        String.valueOf(checkBoxesSymptoms.get(i).getTag(R.id.symptomHeader)),
                                        String.valueOf(checkBoxesSymptoms.get(i).getTag(R.id.symptomBody)),
                                        "true",memberSurveyIdArrayList.get(i),format);
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
                String sourceString = "<b>" + cursor.getString(3) + "</b>" ;
                tvMemberName.setText(Html.fromHtml(sourceString));
                displaySymptomsMember.addView(tvMemberName);
                do {
                    tvSymptomName = new TextView(SurveyActivity.this);
                    tvSymptomName.setPadding(10,10,10,10);
                    tvSymptomName.setTextColor(Color.parseColor("#000000"));
                    tvSymptomName.setGravity(Gravity.CENTER);
                    String sourceString2 = "<b>" + " Symptoms : "+"</b>"+cursor.getString(5);
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
                editModel.setMemberName(cursor1.getString(2));
                editModel.setEditTextValueSys(cursor1.getString(3));
                editModel.setEditTextValueDia(cursor1.getString(4));
                editModel.setTypeSpinner(cursor1.getString(5));
                editModel.setEditTextValueValue(cursor1.getString(6));
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
                            dBhandler.addTestFindings(member.get(i).getMember_id(),
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
        startActivityForResult(intent,VIDEO_RECORD_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VIDEO_RECORD_CODE){

            if (resultCode == RESULT_OK){
                videoPath = data.getData();
            }
            else if (resultCode == RESULT_CANCELED){

            }else{

            }
        }
    }

    public void setupSharedPreference(){
       /* */
        for (int i = 0; i<member.size(); i++) {

        }
        /*Cursor cursor = dBhandler.getGeneralHabitsAlcohol(familyid);
        String id = null;

        if (cursor.moveToFirst()){
            do {
                id = cursor.getString(1);
            }while (cursor.moveToNext());
        }

        if (id!=null && id.equals(familySurveyId)){
            int getCount = mPreferences1.getInt(familySurveyId,0);
            finalCount = ++getCount;
            preferencesEditor = mPreferences1.edit();
            preferencesEditor.putInt(familySurveyId,finalCount);
            preferencesEditor.apply();
        }else {
            int getCount = mPreferences1.getInt(familySurveyId,0);
            finalCount = ++getCount;
            preferencesEditor = mPreferences1.edit();
            preferencesEditor.putInt(familySurveyId,finalCount);
            preferencesEditor.apply();
        }*/
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
        db.execSQL("delete from tbl_general_habits_alcohol where family_id  = '" + familyid + "'" );
        db.execSQL("delete from tbl_test_findings where family_id  = '" + familyid + "'" );
        db.execSQL("delete from tbl_hci_atal_amrit where family_id  = '" + familyid + "'" );
        db.execSQL("delete from tbl_other_info_telemed where family_id  = '" + familyid + "'" );
        db.execSQL("delete from tbl_other_info_opd where family_id  = '" + familyid + "'" );
        db.execSQL("delete from tbl_other_info_ambulance where family_id  = '" + familyid + "'" );
        db.execSQL("delete from tbl_symptoms_member where family_id  = '" + familyid + "'" );
    }

}