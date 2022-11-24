package com.gnrc.telehealth;

import static com.gnrc.telehealth.Family_List_Activity.removeSimpleProgressDialog;
import static com.gnrc.telehealth.Family_List_Activity.showSimpleProgressDialog;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
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
import com.gnrc.telehealth.Adapter.CovidFIAdapter;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class SurveyActivity extends AppCompatActivity {
    RecyclerView generalHabits, testFindings,healthCardInfo,covidFactsInfo,recyclerViewDialog,covidRecycler,signsSymptomsRecycler;
    TextView genHab, testFin, heaCaIn, covFacIn, signsSymptoms;
    AlertDialog dialog;
    SharedPreferences mPreferences;
    SharedPreferences.Editor preferencesEditor;
    CheckBox cb,cb1;
    ArrayList<MemberDetailsForDialogModel> member;
    ArrayList<MemberDetailsForDialogModel> list2;
    MemberDetailsForDialogModel memberDetailsForDialogModel;
    MemberDetailsForDialogModel editModel;
    MemberDetailsForDialogModel editModel2;
    SignsAndSymptomsModel signsAndSymptomsModel;
    ArrayList<String> mapObject;
    Button editButton;
    String list1;
    TreeMap<String,ArrayList<String>> multiMap;
    String memberName;
    int num = 1000;
    ArrayList<CheckBox> checkBoxesSymptoms;
    SignsAndSymptomsModel ssModel;
    AttenderVideoAdapter.ItemClickListener itemClickListener;
    private boolean myBoolean = false;
    public static ArrayList<Boolean> ssModelArraylist;
    private String URLstring = "https://www.gnrctelehealth.com/telehealth_api/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        mPreferences=getSharedPreferences("smokingAlcohol",MODE_PRIVATE);


        genHab = findViewById(R.id.tvGh);
        testFin = findViewById(R.id.tvTf);
        heaCaIn = findViewById(R.id.tvHci);
        covFacIn = findViewById(R.id.tvCfi);
        signsSymptoms = findViewById(R.id.tvSaS);
        editButton = findViewById(R.id.editBu);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditTestFindings();
            }
        });

        genHab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        covFacIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCovidFIDialog(v);
            }
        });
        signsSymptoms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSymptoms(v);
            }
        });

        DBhandler dBhandler = new DBhandler(getApplicationContext());
        Cursor cursor = dBhandler.getFamilyMemberList(getIntent().getStringExtra("familyId"));

        member = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                memberDetailsForDialogModel = new MemberDetailsForDialogModel();
                memberDetailsForDialogModel.setMemberName(cursor.getString(3));
                memberDetailsForDialogModel.setMember_id(cursor.getString(0));
                member.add(memberDetailsForDialogModel);
            } while (cursor.moveToNext());
        }


        showGeneralHabitsSmoking();
        showGeneralHabitsAlcohol();
        showTestFindingsBP();
        showTestFindingsBS();
        showHCIAtalAmrit();
        showHCIAyushmanBharat();
        showCovidFactsInfo();
        fetchingSymptoms();
        showSymptomsMembers();
        //setupTestFindingsDialogRecycler();
    }
    public void showGeneralHabitDialog(View view)    {
        // Create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("General Habits");
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.general_habits,null);

        builder.setView(customLayout);


        LinearLayout smokingLayout = customLayout.findViewById(R.id.llSmoking);
        LinearLayout alcoholLayout = customLayout.findViewById(R.id.llAlcohol);



        ArrayList<CheckBox> checkBoxesSmoking = new ArrayList<>();
        ArrayList<CheckBox> checkBoxesAlcohol= new ArrayList<>();


        for (int i = 0; i < member.size(); i++){
            cb = new CheckBox(SurveyActivity.this);
            cb1 = new CheckBox(SurveyActivity.this);
            cb.setText(member.get(i).getMemberName());
            cb.setTag(member.get(i).getMember_id());
            smokingLayout.addView(cb);
            checkBoxesSmoking.add(cb);
            cb1.setText(member.get(i).getMemberName());
            cb1.setTag(member.get(i).getMember_id());
            alcoholLayout.addView(cb1);
            checkBoxesAlcohol.add(cb1);
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
                        db.delete("tbl_general_habits_alcohol", null,null);
                        db.delete("tbl_general_habits_smoking",null,null);
                        for (int i = 0; i<checkBoxesAlcohol.size();i++){
                            if (checkBoxesAlcohol.get(i).isChecked()) {
                                dBhandler = new DBhandler(getApplicationContext());
                                dBhandler.addGeneralHabitsAlcohol(
                                        String.valueOf(checkBoxesAlcohol.get(i).getTag()),
                                        getIntent().getStringExtra("familyId"),
                                        String.valueOf(checkBoxesAlcohol.get(i).getText()));
                                showGeneralHabitsAlcohol();
                            }
                        }
                        //
                        for (int i = 0; i<checkBoxesSmoking.size();i++){
                            if (checkBoxesSmoking.get(i).isChecked()) {
                                dBhandler = new DBhandler(getApplicationContext());
                                dBhandler.addGeneralHabitsSmoking(
                                        String.valueOf(checkBoxesSmoking.get(i).getTag()),
                                        getIntent().getStringExtra("familyId"),
                                        String.valueOf(checkBoxesSmoking.get(i).getText()));
                                showGeneralHabitsSmoking();
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
        if (cursor1.getCount() > 0 ){
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
        }else {
            for (int i = 0; i < member.size(); i++){
                editModel = new MemberDetailsForDialogModel();
                editModel.setMemberName(member.get(i).getMemberName());
                list.add(editModel);
                TestFindingsAdapter testFindingsAdapter = new TestFindingsAdapter(this, list,getIntent().getStringExtra("familyId"));
                recyclerViewDialog.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false));
                recyclerViewDialog.setAdapter(testFindingsAdapter);
                editButton.setVisibility(View.GONE);
            }
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
                        db.delete("tbl_test_findings", null,null);
                        for (int i = 0; i < member.size(); i++){
                            dBhandler.addTestFindings(member.get(i).getMember_id(),
                                    getIntent().getStringExtra("familyId"),member.get(i).getMemberName(),
                                    TestFindingsAdapter.addMemberDialogArrayList.get(i).getEditTextValueSys(),
                                    TestFindingsAdapter.addMemberDialogArrayList.get(i).getEditTextValueDia(),
                                    TestFindingsAdapter.addMemberDialogArrayList.get(i).getTypeSpinner(),
                                    TestFindingsAdapter.addMemberDialogArrayList.get(i).getEditTextValueValue());
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
        final View customLayout = getLayoutInflater().inflate(R.layout.health_card_information,null);

        builder.setView(customLayout);

        DBhandler dBhandler = new DBhandler(getApplicationContext());
        LinearLayout atalAmritLayout = customLayout.findViewById(R.id.ll_AtalAmritAbhyan);
        LinearLayout ayushmanBharatLayout = customLayout.findViewById(R.id.ll_AyushmanBharat);



        ArrayList<CheckBox> checkBoxesAtalAmrit = new ArrayList<>();
        ArrayList<CheckBox> checkBoxesAyushmanBharat= new ArrayList<>();


        for (int i = 0; i < member.size(); i++){
            cb = new CheckBox(SurveyActivity.this);
            cb1 = new CheckBox(SurveyActivity.this);
            cb.setText(member.get(i).getMemberName());
            cb.setTag(member.get(i).getMember_id());
            atalAmritLayout.addView(cb);
            checkBoxesAtalAmrit.add(cb);
            cb1.setText(member.get(i).getMemberName());
            cb1.setTag(member.get(i).getMember_id());
            ayushmanBharatLayout.addView(cb1);
            checkBoxesAyushmanBharat.add(cb1);
            Cursor cursor1 = dBhandler.getHCIAtalAmrit(getIntent().getStringExtra("familyId"));
            if (cursor1.moveToFirst()){
                do {
                    if (cursor1.getString(0).equals(cb.getTag())){
                        cb.setChecked(true);

                    }
                }while (cursor1.moveToNext());
            }
            Cursor cursor2 = dBhandler.getHCIAyushmanBharat(getIntent().getStringExtra("familyId"));
            if (cursor2.moveToFirst()){
                do {
                    if (cursor2.getString(0).equals(cb1.getTag())){
                        cb1.setChecked(true);

                    }
                }while (cursor2.moveToNext());
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
                        db.delete("tbl_hci_atal_amrit", null,null);
                        db.delete("tbl_hci_ayushman_bharat",null,null);
                        for (int i = 0; i<checkBoxesAyushmanBharat.size();i++){
                            if (checkBoxesAyushmanBharat.get(i).isChecked()) {
                                dBhandler = new DBhandler(getApplicationContext());
                                dBhandler.addHCIAyushmanBharat(
                                        String.valueOf(checkBoxesAyushmanBharat.get(i).getTag()),
                                        getIntent().getStringExtra("familyId"),
                                        String.valueOf(checkBoxesAyushmanBharat.get(i).getText()));
                                checkBoxesAyushmanBharat.get(i).setChecked(true);
                                showHCIAyushmanBharat();
                            }
                        }
                        //
                        for (int i = 0; i<checkBoxesAtalAmrit.size();i++){
                            if (checkBoxesAtalAmrit.get(i).isChecked()) {
                                dBhandler = new DBhandler(getApplicationContext());
                                dBhandler.addHCIAtalAmrit(
                                        String.valueOf(checkBoxesAtalAmrit.get(i).getTag()),
                                        getIntent().getStringExtra("familyId"),
                                        String.valueOf(checkBoxesAtalAmrit.get(i).getText()));
                                checkBoxesAtalAmrit.get(i).setChecked(true);
                                showHCIAtalAmrit();
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
                        db.delete("tbl_covid_facts", null,null);
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
    public void showGeneralHabitsAlcohol(){
        DBhandler dBhandler = new DBhandler(getApplicationContext());
        Cursor cursor = dBhandler.getGeneralHabitsAlcohol(getIntent().getStringExtra("familyId"));
        TextView tvAlcohol;
        LinearLayout displayAlcohol = findViewById(R.id.ll_alcoholDisplay);
        displayAlcohol.removeAllViews();
        if (cursor.moveToFirst()) {
            do {
                tvAlcohol = new TextView(SurveyActivity.this);
                tvAlcohol.setPadding(10,10,10,10);
                tvAlcohol.setTextColor(Color.parseColor("#000000"));
                tvAlcohol.setGravity(Gravity.CENTER);
                tvAlcohol.setText(cursor.getString(2));
                displayAlcohol.addView(tvAlcohol);
            } while (cursor.moveToNext());
        }
    }
    public void showHCIAtalAmrit(){
        DBhandler dBhandler = new DBhandler(getApplicationContext());
        Cursor cursor = dBhandler.getHCIAtalAmrit(getIntent().getStringExtra("familyId"));
        TextView tvAtal;
        LinearLayout displayAtalAmrit = findViewById(R.id.ll_AtlAmrDisplay);
        displayAtalAmrit.removeAllViews();
        if (cursor.moveToFirst()) {
            do {
                tvAtal = new TextView(SurveyActivity.this);
                tvAtal.setPadding(10,10,10,10);
                tvAtal.setTextColor(Color.parseColor("#000000"));
                tvAtal.setGravity(Gravity.CENTER);
                tvAtal.setText(cursor.getString(2));
                displayAtalAmrit.addView(tvAtal);
            } while (cursor.moveToNext());
        }
    }
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
                        db.delete("tbl_symptoms_member", null,null);
                        for (int i = 0; i<checkBoxesSymptoms.size();i++){
                            if (checkBoxesSymptoms.get(i).isChecked()) {
                                dBhandler = new DBhandler(getApplicationContext());
                                dBhandler.addSymptomsMember(String.valueOf(checkBoxesSymptoms.get(i).getTag(R.id.Atr_Code)),
                                        String.valueOf(checkBoxesSymptoms.get(i).getTag(R.id.memberId)),
                                        getIntent().getStringExtra("familyId"),
                                        String.valueOf(checkBoxesSymptoms.get(i).getText()),
                                        String.valueOf(checkBoxesSymptoms.get(i).getTag(R.id.symptomHeader)),
                                        String.valueOf(checkBoxesSymptoms.get(i).getTag(R.id.symptomBody)),
                                        "true");
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
                        db.delete("tbl_test_findings", null,null);
                        for (int i = 0; i < member.size(); i++){
                            dBhandler.addTestFindings(member.get(i).getMember_id(),
                                    getIntent().getStringExtra("familyId"),member.get(i).getMemberName(),
                                    TestFindingsAdapter.addMemberDialogArrayList.get(i).getEditTextValueSys(),
                                    TestFindingsAdapter.addMemberDialogArrayList.get(i).getEditTextValueDia(),
                                    TestFindingsAdapter.addMemberDialogArrayList.get(i).getTypeSpinner(),
                                    TestFindingsAdapter.addMemberDialogArrayList.get(i).getEditTextValueValue());
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


}