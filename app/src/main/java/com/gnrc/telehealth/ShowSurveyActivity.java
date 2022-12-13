package com.gnrc.telehealth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.gnrc.telehealth.Adapter.Family_Head_Adapter;
import com.gnrc.telehealth.Adapter.ShowSurveyAdapter;
import com.gnrc.telehealth.DatabaseSqlite.DBhandler;
import com.gnrc.telehealth.Model.MemberDetailsForDialogModel;

import java.util.ArrayList;

public class ShowSurveyActivity extends AppCompatActivity {
    Button newSurvey;
    DBhandler dBhandler;
    ArrayList<MemberDetailsForDialogModel> surveydetailsarraylist;
    MemberDetailsForDialogModel surveymodel;
    ShowSurveyAdapter showSurveyAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_survey);

        newSurvey = findViewById(R.id.btnNewSurvey);
        recyclerView = findViewById(R.id.rvShowSurvey);
        newSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ShowSurveyActivity.this, SurveyActivity.class);
                i.putExtra("familyId",getIntent().getStringExtra("familyId"));
                i.putExtra("headPhoneNo",getIntent().getStringExtra("headPhoneNo"));
                startActivity(i);
            }
        });
        dBhandler = new DBhandler(getApplicationContext());
        Cursor cursor = dBhandler.getVideoPath();
        Log.d("hi", "onCreate: "+cursor.getCount());
        if (cursor.getCount()>0){
            surveydetailsarraylist = new ArrayList<>();
            if (cursor.moveToFirst()){
                do {
                    surveymodel =new MemberDetailsForDialogModel();
                    surveymodel.setGroupSurveyID(cursor.getString(0));
                    surveymodel.setTimeStamp(cursor.getString(2));
                    surveydetailsarraylist.add(surveymodel);
                }while (cursor.moveToNext());
                showSurveyAdapter = new ShowSurveyAdapter(this, surveydetailsarraylist);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false));
                recyclerView.setAdapter(showSurveyAdapter);
            }
        }


    }
    public void syncSurvey(){
        DBhandler dBhandler = new DBhandler(getApplicationContext());
        Cursor cursor = dBhandler.getFamilyDbData();
        if (cursor.moveToFirst()){
            do {

            }while(cursor.moveToNext());
        }
    }
}