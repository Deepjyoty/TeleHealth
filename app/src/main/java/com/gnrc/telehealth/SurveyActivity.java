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
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gnrc.telehealth.Adapter.CovidFIAdapter;
import com.gnrc.telehealth.Adapter.Family_Head_Adapter;
import com.gnrc.telehealth.Adapter.SignsAndSymptomsAdapter;
import com.gnrc.telehealth.Adapter.TestFindingsAdapter;
import com.gnrc.telehealth.DatabaseSqlite.DBhandler;
import com.gnrc.telehealth.Model.Family_Head_Model;
import com.gnrc.telehealth.Model.MemberDetailsForDialogModel;
import com.gnrc.telehealth.Model.SignsAndSymptomsModel;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class SurveyActivity extends AppCompatActivity {
    RecyclerView generalHabits, testFindings,healthCardInfo,covidFactsInfo,recyclerViewDialog,covidRecycler,signsSymptomsRecycler;
    TextView genHab, testFin, heaCaIn, covFacIn, signsSymptoms;
    AlertDialog dialog;
    SharedPreferences mPreferences;
    SharedPreferences.Editor preferencesEditor;
    CheckBox cb,cb1;
    ArrayList<MemberDetailsForDialogModel> member;
    MemberDetailsForDialogModel memberDetailsForDialogModel;
    MemberDetailsForDialogModel editModel;
    SignsAndSymptomsModel signsAndSymptomsModel;
    ArrayList<String> mapObject;
    String list1;
    TreeMap<String,ArrayList<String>> multiMap;
    String memberName;
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

        for (int i = 0; i < member.size(); i++){
            editModel = new MemberDetailsForDialogModel();
            //editModel.setMember_id(member.get(i).getMember_id());
            editModel.setMemberName(member.get(i).getMemberName());
            list.add(editModel);

        TestFindingsAdapter testFindingsAdapter = new TestFindingsAdapter(this, list);
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
        DBhandler dBhandler = new DBhandler(getApplicationContext());
        Cursor cursor = dBhandler.getTestFindings(getIntent().getStringExtra("familyId"));
        ArrayList<MemberDetailsForDialogModel> list = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                editModel = new MemberDetailsForDialogModel();
                editModel.setMember_id(cursor.getString(0));
                editModel.setMemberName(cursor.getColumnName(2));
                editModel.setEditTextValueSys(cursor.getString(3));
                editModel.setEditTextValueDia(cursor.getString(4));
                editModel.setTypeSpinner(cursor.getString(5));
                editModel.setEditTextValueValue(cursor.getString(6));
                list.add(editModel);
            } while (cursor.moveToNext());

        }
    }
    public void showTestFindingsBP(){
        DBhandler dBhandler = new DBhandler(getApplicationContext());
        Cursor cursor = dBhandler.getTestFindings(getIntent().getStringExtra("familyId"));
        TextView tvTestFindings;
        LinearLayout displayTestFindings = findViewById(R.id.ll_BPDisplay);
        displayTestFindings.removeAllViews();
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

        showSimpleProgressDialog(this, "Loading...","Saving",false);

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
                                        jsondata.getString("ATR_SLNO"));
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

                           /* Iterator<String> keys = jsondata.keys();
                            while (keys.hasNext())
                            {
                                JSONObject dataobj = jsonObject.getJSONObject(keys.next());
                                JSONObject header = dataobj.getJSONObject("header");
                                header.getString("PRT_CODE");
                                header.getString("PRT_DESC");
                                header.getString("PRT_DESC_ALT");
                                JSONArray detail = dataobj.getJSONArray("detail");

                                for (int i = 0; i < detail.length(); i++) {
                                    JSONObject value = detail.getJSONObject(i);
                                    value.getString("ATR_CODE");
                                    value.getString("ATR_DESC");
                                    value.getString("PRT_SLNO");
                                }

                            }*/


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

        //Family_Head_Model dmodel = new Family_Head_Model();

        ArrayList<CheckBox> checkBoxesSmoking = new ArrayList<>();
        //ArrayList<SignsAndSymptomsModel> list = new ArrayList<>();



        for (Map.Entry<String, ArrayList<String>> entry : multiMap.entrySet()) {
            list1 = entry.getKey();
            Log.d("gandu", "showSymptoms: "+list1);
            Cursor cursor = dBhandler.getSymptomsMaster(list1);
            if (cursor.moveToFirst()) {
                tvSignsSymptomsHeader = new TextView(SurveyActivity.this);
                tvSignsSymptomsHeader.setPadding(10,10,10,10);
                tvSignsSymptomsHeader.setTextColor(Color.parseColor("#000000"));
                tvSignsSymptomsHeader.setGravity(Gravity.CENTER);
                tvSignsSymptomsHeader.setText(cursor.getString(2));
                symptoms.addView(tvSignsSymptomsHeader);
                do {


                    tvSignsSymptomsBody = new TextView(SurveyActivity.this);
                    tvSignsSymptomsBody.setPadding(10,10,10,10);
                    tvSignsSymptomsBody.setTextColor(Color.parseColor("#000000"));
                    tvSignsSymptomsBody.setGravity(Gravity.CENTER);
                    tvSignsSymptomsBody.setText(cursor.getString(6));
                    symptoms.addView(tvSignsSymptomsBody);
                    for (int i = 0; i < member.size(); i++){

                        //editModel.setMember_id(member.get(i).getMember_id());
                        cb = new CheckBox(SurveyActivity.this);
                        cb1 = new CheckBox(SurveyActivity.this);
                        cb.setText(member.get(i).getMemberName());
                        symptoms.addView(cb);
                        checkBoxesSmoking.add(cb);
                    }

                } while (cursor.moveToNext());

            }
          /*  for(String symptomsName : entry.getValue())
            {

            }*/
        }
        builder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    DBhandler dBhandler;
                    @Override
                    public void onClick(DialogInterface dialog,int which)
                    {

                    }

                });
        // create and show
        // the alert dialog
        /**/
        dialog = builder.create();
        dialog.show();
    }
    public void getSymptomsForView(){


    }

}