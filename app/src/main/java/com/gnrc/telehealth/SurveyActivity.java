package com.gnrc.telehealth;

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
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gnrc.telehealth.Adapter.Family_Head_Adapter;
import com.gnrc.telehealth.Adapter.TestFindingsAdapter;
import com.gnrc.telehealth.DatabaseSqlite.DBhandler;
import com.gnrc.telehealth.Model.MemberDetailsForDialogModel;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class SurveyActivity extends AppCompatActivity {
    RecyclerView generalHabits, testFindings,healthCardInfo,covidFactsInfo,recyclerViewDialog;
    TextView genHab, testFin, heaCaIn, covFacIn, testFind;
    AlertDialog dialog;
    SharedPreferences mPreferences;
    SharedPreferences.Editor preferencesEditor;
    CheckBox cb,cb1;
    ArrayList<MemberDetailsForDialogModel> member;
    MemberDetailsForDialogModel memberDetailsForDialogModel;
    TextInputLayout sys,dia,value;
    MemberDetailsForDialogModel editModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        mPreferences=getSharedPreferences("smokingAlcohol",MODE_PRIVATE);


        testFindings = findViewById(R.id.recyclerTestF);
        healthCardInfo = findViewById(R.id.recyclerHealthCI);
        covidFactsInfo = findViewById(R.id.recyclerCovidFI);

        genHab = findViewById(R.id.tvGh);
        testFin = findViewById(R.id.tvTf);
        heaCaIn = findViewById(R.id.tvHci);
        covFacIn = findViewById(R.id.tvCfi);

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
                                dBhandler.addGeneralHabitsAlcohol(String.valueOf(checkBoxesAlcohol.get(i).getTag()),getIntent().getStringExtra("familyId"),String.valueOf(checkBoxesAlcohol.get(i).getText()));
                                showGeneralHabitsAlcohol();
                            }
                        }
                        //
                        for (int i = 0; i<checkBoxesSmoking.size();i++){
                            if (checkBoxesSmoking.get(i).isChecked()) {
                                dBhandler = new DBhandler(getApplicationContext());
                                dBhandler.addGeneralHabitsSmoking(String.valueOf(checkBoxesSmoking.get(i).getTag()),getIntent().getStringExtra("familyId"),String.valueOf(checkBoxesSmoking.get(i).getText()));
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
                            dBhandler.addTestFindings(member.get(i).getMember_id(),getIntent().getStringExtra("familyId"),member.get(i).getMemberName(),
                                    TestFindingsAdapter.addMemberDialogArrayList.get(i).getEditTextValueSys(),
                                    TestFindingsAdapter.addMemberDialogArrayList.get(i).getEditTextValueDia(),TestFindingsAdapter.addMemberDialogArrayList.get(i).getEditTextValueValue());
                            setupTestFindingsDialogRecycler();
                        }
                    }

                });
        dialog = builder.create();
        dialog.show();
        dialog.getWindow().clearFlags( WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
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
                editModel.setEditTextValueValue(cursor.getString(5));
                list.add(editModel);
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
                tvAlcohol.setText(cursor.getString(2));
                displayAlcohol.addView(tvAlcohol);
            } while (cursor.moveToNext());
        }
    }
}