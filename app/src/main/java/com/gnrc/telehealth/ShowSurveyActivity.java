package com.gnrc.telehealth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.gnrc.telehealth.Adapter.Family_Head_Adapter;
import com.gnrc.telehealth.Adapter.ShowSurveyAdapter;
import com.gnrc.telehealth.DatabaseSqlite.DBhandler;
import com.gnrc.telehealth.Model.AddFamilyModel;
import com.gnrc.telehealth.Model.MemberDetailsForDialogModel;
import com.gnrc.telehealth.Network.MyReceiver;

import java.util.ArrayList;

public class ShowSurveyActivity extends AppCompatActivity {
    Button newSurvey;
    DBhandler dBhandler;
    ArrayList<MemberDetailsForDialogModel> surveydetailsarraylist;
    private ArrayList<AddFamilyModel> addFamilyModelArrayList;
    MemberDetailsForDialogModel surveymodel;
    ShowSurveyAdapter showSurveyAdapter;
    RecyclerView recyclerView;
    private BroadcastReceiver MyReceiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_survey);
        MyReceiver = new MyReceiver();
        MyReceiver.getResultData();
        Log.d("receiver", "onCreate: "+MyReceiver.getResultData());

        recyclerView = findViewById(R.id.rvShowSurvey);
        /*newSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ShowSurveyActivity.this, SurveyActivity.class);
                i.putExtra("familyId",getIntent().getStringExtra("familyId"));
                i.putExtra("headPhoneNo",getIntent().getStringExtra("headPhoneNo"));
                startActivity(i);
            }
        });*/
        populateRecycler();
    }
    public void syncSurvey(){
        DBhandler dBhandler = new DBhandler(getApplicationContext());
        Cursor cursor = dBhandler.getFamilyDbData();
        if (cursor.moveToFirst()){
            do {

            }while(cursor.moveToNext());
        }
    }
    /**
     * register bradcast receiver for network
     */
    private void registerNetworkReceiver(){
        try {
            registerReceiver(MyReceiver,
                    new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * un register broadcast receiver
     */
    private void unregisterNetworkReceiver(){
        try {
            if (MyReceiver != null){
                LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(MyReceiver);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    public void populateRecycler(){
        dBhandler = new DBhandler(getApplicationContext());
        Cursor cursor = dBhandler.getVideoPath();
        Log.d("hi", "onCreate: "+cursor.getCount());

        if (cursor.getCount()>0){
            surveydetailsarraylist = new ArrayList<>();
            if (cursor.moveToFirst()){
                do {
                    surveymodel =new MemberDetailsForDialogModel();
                    surveymodel.setGroupSurveyID(cursor.getString(0));
                    surveymodel.setFamilyID(cursor.getString(1));
                    Cursor cursor1 = dBhandler.getFamilyMasterNameAddress(cursor.getString(1));
                    if (cursor1.getCount()>0){
                        if (cursor1.moveToFirst()){
                            surveymodel.setHeadName(cursor1.getString(0));
                            surveymodel.setHeadAddress(cursor1.getString(1));
                        }
                    }
                    surveymodel.setVideoPath(cursor.getString(2));
                    surveymodel.setTimeStamp(cursor.getString(3));
                    /*SQLiteDatabase db = dBhandler.getWritableDatabase();
                    db.execSQL("select SSFM_HEAD_NAME, SSFM_ADDR from  tbl_family_master where family_id  = '" + familyid + "'" );*/

                    surveydetailsarraylist.add(surveymodel);
                }while (cursor.moveToNext());
                showSurveyAdapter = new ShowSurveyAdapter(this, surveydetailsarraylist);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                        LinearLayoutManager.VERTICAL,false));
                recyclerView.setAdapter(showSurveyAdapter);
                showSurveyAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        registerNetworkReceiver();
        populateRecycler();

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerNetworkReceiver();
        populateRecycler();

    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterNetworkReceiver();
    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterNetworkReceiver();
    }
}