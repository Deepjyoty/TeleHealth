package com.gnrc.telehealth.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gnrc.telehealth.Model.AddFamilyModel;
import com.gnrc.telehealth.Model.MemberDetailsForDialogModel;
import com.gnrc.telehealth.R;
import com.gnrc.telehealth.SurveyActivity;

import java.util.ArrayList;

public class ShowSurveyAdapter extends RecyclerView.Adapter<ShowSurveyAdapter.MyViewHolder>{
    private LayoutInflater inflater;
    private ArrayList<MemberDetailsForDialogModel> addFamilyModelArrayList;
    private MemberDetailsForDialogModel addFamilyModel;

    public ShowSurveyAdapter(Context ctx, ArrayList<MemberDetailsForDialogModel> addFamilyModelArrayList){

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
        holder.surveyId.setText(addFamilyModelArrayList.get(position).getGroupSurveyID());
        holder.surveyDate.setText(addFamilyModelArrayList.get(position).getTimeStamp());
        holder.counter.setText("Survey\n"+"0"+String.valueOf(++position));
        //holder.edit.setText(familyHeadModelArrayList.get(position).getEdittext());

        //familyHeadModelArrayList.get(position).getDescription();
        holder.syncSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(inflater.getContext(), SurveyActivity.class);
                i.putExtra("familyId", addFamilyModelArrayList.get(holder.getAdapterPosition()).getGroupSurveyID());
                inflater.getContext().startActivity(i);
            }
        });

    }
    @Override
    public int getItemCount() {
        return addFamilyModelArrayList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView surveyId, surveyDate, counter, syncSurvey;

        public MyViewHolder(View itemView) {
            super(itemView);

            surveyId = (TextView) itemView.findViewById(R.id.ssSurveyID);
            surveyDate = (TextView) itemView.findViewById(R.id.ssSurveyDate);
            counter = (TextView) itemView.findViewById(R.id.tvCounterSurvey);
            syncSurvey = (TextView) itemView.findViewById(R.id.tvSyncSurvey);
            //iv = (ImageView) itemView.findViewById(R.id.iv);
        }

    }
}

