package com.gnrc.telehealth;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gnrc.telehealth.Adapter.ShowSurveyAdapter;
import com.gnrc.telehealth.DatabaseSqlite.DBhandler;
import com.gnrc.telehealth.Model.AddFamilyModel;
import com.gnrc.telehealth.Model.MemberDetailsForDialogModel;
import com.gnrc.telehealth.Network.MyReceiver;
import com.gnrc.telehealth.Network.NetworkListener;

import java.util.ArrayList;

public class ShowSurveyActivity extends AppCompatActivity implements NetworkListener {
    Button newSurvey;
    DBhandler dBhandler;
    ArrayList<MemberDetailsForDialogModel> surveydetailsarraylist;
    private ArrayList<AddFamilyModel> addFamilyModelArrayList;
    MemberDetailsForDialogModel surveymodel;
    ShowSurveyAdapter showSurveyAdapter;
    RecyclerView recyclerView;
    private BroadcastReceiver MyReceiver = null;
    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_survey);
        setTitle("Pending Surveys To Be Synced");

        constraintLayout = findViewById(R.id.cl_noDataToSync);
        MyReceiver = new MyReceiver(this);
        MyReceiver.getResultData();
        Log.d("receiver", "onCreate: "+MyReceiver.getResultData());

        recyclerView = findViewById(R.id.rvShowSurvey);

        populateRecycler();

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
            if (cursor.getCount()>0){
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

                        surveydetailsarraylist.add(surveymodel);
                    }while (cursor.moveToNext());
                    showSurveyAdapter = new ShowSurveyAdapter(this, surveydetailsarraylist);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                            LinearLayoutManager.VERTICAL,false));
                    recyclerView.setAdapter(showSurveyAdapter);
                    showSurveyAdapter.notifyDataSetChanged();
                }

            }else{
                constraintLayout.setVisibility(View.VISIBLE);
            }
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        registerNetworkReceiver();
        populateRecycler();
        showSurveyAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerNetworkReceiver();
        populateRecycler();
        showSurveyAdapter.notifyDataSetChanged();
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

    @Override
    public void onNetworkDisconnected() {
        //Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNetworkConnected(String type) {
        //Toast.makeText(this, "Internet", Toast.LENGTH_SHORT).show();
    }
}