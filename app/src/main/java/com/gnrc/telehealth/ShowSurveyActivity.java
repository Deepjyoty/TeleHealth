package com.gnrc.telehealth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.gnrc.telehealth.DatabaseSqlite.DBhandler;

public class ShowSurveyActivity extends AppCompatActivity {
    Button newSurvey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_survey);

        newSurvey = findViewById(R.id.btnNewSurvey);
        newSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ShowSurveyActivity.this, SurveyActivity.class);
                i.putExtra("familyId",getIntent().getStringExtra("familyId"));
                i.putExtra("headPhoneNo",getIntent().getStringExtra("headPhoneNo"));
                startActivity(i);
            }
        });
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