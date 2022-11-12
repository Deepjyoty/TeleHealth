package com.gnrc.telehealth;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gnrc.telehealth.DatabaseSqlite.DBhandler;

import java.util.ArrayList;

public class SurveyActivity extends AppCompatActivity {
    RecyclerView generalHabits, testFindings,healthCardInfo,covidFactsInfo;
    TextView genHab, testFin, heaCaIn, covFacIn;
    AlertDialog dialog;
    SharedPreferences mPreferences;
    SharedPreferences.Editor preferencesEditor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        mPreferences=getSharedPreferences("smokingAlcohol",MODE_PRIVATE);
        preferencesEditor = mPreferences.edit();

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
        LinearLayout displaySmoking = findViewById(R.id.ll_smokingDisplay);
        LinearLayout displayAlcohol = findViewById(R.id.ll_alcoholDisplay);


        DBhandler dBhandler = new DBhandler(getApplicationContext());
        Cursor cursor = dBhandler.getFamilyMemberList(getIntent().getStringExtra("familyId"));
        ArrayList<String> member = new ArrayList<>();
        ArrayList<CheckBox> checkBoxesSmoking = new ArrayList<>();
        ArrayList<CheckBox> checkBoxesAlcohol= new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                String memberData = cursor.getString(3);
                member.add(memberData);
            } while (cursor.moveToNext());
        }
        CheckBox cb,cb1;
        for (int i = 0; i < member.size(); i++){
            cb = new CheckBox(SurveyActivity.this);
            cb1 = new CheckBox(SurveyActivity.this);
            cb.setText( member.get(i));
            smokingLayout.addView(cb);
            checkBoxesSmoking.add(cb);
            cb1.setText(member.get(i));
            alcoholLayout.addView(cb1);
            checkBoxesAlcohol.add(cb1);
        }



        // add a button
        builder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    TextView tvSmoking,tvAlcohol;
                    @Override
                    public void onClick(DialogInterface dialog,int which)
                    {
                        for (int i = 0; i<checkBoxesAlcohol.size();i++){
                            if (checkBoxesAlcohol.get(i).isChecked()) {
                                tvAlcohol = new TextView(SurveyActivity.this);
                                tvAlcohol.setPadding(10,10,10,10);
                                tvAlcohol.setTextColor(Color.parseColor("#000000"));
                                preferencesEditor.putString("alcohol", String.valueOf(checkBoxesAlcohol.get(i).getText()));
                                tvAlcohol.setText(mPreferences.getString("alcohol","false"));
                                displayAlcohol.addView(tvAlcohol);

                            }
                        }
                        for (int i = 0; i<checkBoxesSmoking.size();i++){
                            if (checkBoxesSmoking.get(i).isChecked()) {
                                tvSmoking = new TextView(SurveyActivity.this);
                                tvSmoking.setPadding(10,10,10,10);
                                tvSmoking.setTextColor(Color.parseColor("#000000"));
                                preferencesEditor.putString("smoking", String.valueOf(checkBoxesSmoking.get(i).getText()));
                                tvSmoking.setText(mPreferences.getString("smoking","false"));

                                displaySmoking.addView(tvSmoking);
                            }
                        }
                    }
                });
        // create and show
        // the alert dialog
        dialog = builder.create();
        dialog.show();

    }
}