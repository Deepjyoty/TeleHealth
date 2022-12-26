package com.gnrc.telehealth.Adapter;

import static com.zipow.videobox.confapp.ConfMgr.getApplicationContext;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.gnrc.telehealth.DatabaseSqlite.DBhandler;
import com.gnrc.telehealth.Model.AddFamilyModel;
import com.gnrc.telehealth.Model.MemberDetailsForDialogModel;
import com.gnrc.telehealth.R;
import com.gnrc.telehealth.SendDataToServer;
import com.gnrc.telehealth.ShowSurveyActivity;
import com.gnrc.telehealth.SurveyActivity;

import java.util.ArrayList;

public class ShowSurveyAdapter extends RecyclerView.Adapter<ShowSurveyAdapter.MyViewHolder>{
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<MemberDetailsForDialogModel> addFamilyModelArrayList;
    private MemberDetailsForDialogModel addFamilyModel;

    DBhandler dBhandler;
    String surveyID, familyID,videopath, memberList;

    public ShowSurveyAdapter(Context ctx, ArrayList<MemberDetailsForDialogModel> addFamilyModelArrayList){
        this.context = ctx;
        inflater = LayoutInflater.from(ctx);
        this.addFamilyModelArrayList = addFamilyModelArrayList;

    }

    @Override
    public ShowSurveyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.cardview_show_survey, parent, false);
        ShowSurveyAdapter.MyViewHolder holder = new ShowSurveyAdapter.MyViewHolder(view);

        return holder;
    }
    public void onBindViewHolder(ShowSurveyAdapter.MyViewHolder holder, int position) {
        addFamilyModel = addFamilyModelArrayList.get(position);
        //Picasso.get().load(familyHeadModelArrayList.get(position).getThumbnail()).into(holder.iv);
        holder.surveyId.setText("Head Name : " + addFamilyModelArrayList.get(position).getHeadName());
        holder.surveyAddress.setText("Address : " + addFamilyModelArrayList.get(position).getHeadAddress());
        holder.surveyDate.setText("Date/Time : " + addFamilyModelArrayList.get(position).getTimeStamp());
        holder.counter.setText("Survey\n"+"0"+ ++position);
        //holder.edit.setText(familyHeadModelArrayList.get(position).getEdittext());

        //familyHeadModelArrayList.get(position).getDescription();
        holder.syncSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager cm = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
                    // connected to the internet
                    if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                        // connected to wifi

                        //send survey data to server
                        surveyID = addFamilyModelArrayList.get(holder.getAbsoluteAdapterPosition()).getGroupSurveyID();
                        familyID = addFamilyModelArrayList.get(holder.getAbsoluteAdapterPosition()).getFamilyID();
                        videopath = addFamilyModelArrayList.get(holder.getAbsoluteAdapterPosition()).getVideoPath();
                        memberList = addFamilyModelArrayList.get(holder.getAbsoluteAdapterPosition()).getMemberList();
                        SendDataToServer abc = new SendDataToServer(context);
                        abc.saveDataToServer(surveyID,familyID,memberList);
                        abc.uploadPDF(surveyID, Uri.parse(videopath));

                    } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                        // connected to mobile data
                        //send survey data to server
                        surveyID = addFamilyModelArrayList.get(holder.getAbsoluteAdapterPosition()).getGroupSurveyID();
                        familyID = addFamilyModelArrayList.get(holder.getAbsoluteAdapterPosition()).getFamilyID();
                        videopath = addFamilyModelArrayList.get(holder.getAbsoluteAdapterPosition()).getVideoPath();
                        memberList = addFamilyModelArrayList.get(holder.getAbsoluteAdapterPosition()).getMemberList();
                        SendDataToServer abc = new SendDataToServer(context);
                        abc.saveDataToServer(surveyID,familyID,memberList);
                        abc.uploadPDF(surveyID, Uri.parse(videopath));
                    }
                } else {
                    Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }
    @Override
    public int getItemCount() {
        return addFamilyModelArrayList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView surveyId, surveyDate, counter, syncSurvey, surveyAddress;

        public MyViewHolder(View itemView) {
            super(itemView);

            surveyId = (TextView) itemView.findViewById(R.id.ssHeadName);
            surveyDate = (TextView) itemView.findViewById(R.id.ssSurveyDate);
            counter = (TextView) itemView.findViewById(R.id.tvCounterSurvey);
            syncSurvey = (TextView) itemView.findViewById(R.id.tvSyncSurvey);
            surveyAddress = itemView.findViewById(R.id.ssSurveyAddress);
            //iv = (ImageView) itemView.findViewById(R.id.iv);
        }

    }
}

