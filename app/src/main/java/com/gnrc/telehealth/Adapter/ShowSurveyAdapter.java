package com.gnrc.telehealth.Adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gnrc.telehealth.DatabaseSqlite.DBhandler;
import com.gnrc.telehealth.Model.AddFamilyModel;
import com.gnrc.telehealth.Model.MemberDetailsForDialogModel;
import com.gnrc.telehealth.R;
import com.gnrc.telehealth.SendDataToServer;
import com.gnrc.telehealth.SurveyActivity;

import java.util.ArrayList;

public class ShowSurveyAdapter extends RecyclerView.Adapter<ShowSurveyAdapter.MyViewHolder>{
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<MemberDetailsForDialogModel> addFamilyModelArrayList;
    private MemberDetailsForDialogModel addFamilyModel;

    DBhandler dBhandler;
    String surveyID, familyID,videopath;

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
                Log.d("rabbit", "onClick: This was executed first");
                surveyID = addFamilyModelArrayList.get(holder.getAbsoluteAdapterPosition()).getGroupSurveyID();
                familyID = addFamilyModelArrayList.get(holder.getAbsoluteAdapterPosition()).getFamilyID();
                videopath = addFamilyModelArrayList.get(holder.getAbsoluteAdapterPosition()).getVideoPath();
                SendDataToServer abc = new SendDataToServer(context);
                abc.saveDataToServer(surveyID,familyID);
                abc.uploadPDF(surveyID, Uri.parse(videopath));
                Log.d("rabbit", "onClick: This was executed Second");
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

