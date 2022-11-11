package com.gnrc.telehealth;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gnrc.telehealth.DatabaseSqlite.DBhandler;

public class SurveyActivity extends AppCompatActivity {
    RecyclerView generalHabits, testFindings,healthCardInfo,covidFactsInfo;
    TextView genHab, testFin, heaCaIn, covFacIn;
    AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        generalHabits = findViewById(R.id.recyclerGeneralH);
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
    public void showGeneralHabitDialog(View view)
    {
        // Create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("General Habits");
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.general_habits,null);

        builder.setView(customLayout);

       /* RadioButton smokingBtn = customLayout.findViewById(R.id.radioButtonSmoking);
        *//*dob = customLayout.findViewById(R.id.fm_DOB);*//*
        RadioButton alcoholBtn = customLayout.findViewById(R.id.radioButtonAlcohol);*/

        LinearLayout smokingLayout = customLayout.findViewById(R.id.llSmoking);
        LinearLayout alcoholLayout = customLayout.findViewById(R.id.llAlcohol);



        CheckBox cb = new CheckBox(getApplicationContext());
        CheckBox cb1 = new CheckBox(getApplicationContext());

        smokingLayout.removeView(cb);
        alcoholLayout.removeView(cb1);
        DBhandler dBhandler = new DBhandler(getApplicationContext());
        Cursor cursor = dBhandler.getAddFamilyData();


        if (cursor.moveToFirst()) {
            do {
                if(cb.getParent()!=null)
                    ((ViewGroup)cb.getParent()).removeView(cb);{
                    cb.setText(cursor.getString(3));
                    smokingLayout.addView(cb);
                }
                if(cb1.getParent()!=null)
                    ((ViewGroup)cb1.getParent()).removeView(cb1);{
                    cb1.setText(cursor.getString(3));
                    alcoholLayout.addView(cb1);
                }

            } while (cursor.moveToNext());
        }
        // add a button
        builder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int which)
                    {

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

    }
}