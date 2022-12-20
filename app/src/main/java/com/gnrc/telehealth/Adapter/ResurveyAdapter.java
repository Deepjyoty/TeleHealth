package com.gnrc.telehealth.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gnrc.telehealth.DatabaseSqlite.DBhandler;
import com.gnrc.telehealth.FamilyHeadActivity;
import com.gnrc.telehealth.Model.AddFamilyModel;
import com.gnrc.telehealth.Model.Family_Head_Model;
import com.gnrc.telehealth.Model.MemberDetailsForDialogModel;
import com.gnrc.telehealth.Model.ResurveyModel;
import com.gnrc.telehealth.R;
import com.gnrc.telehealth.SendDataToServer;
import com.gnrc.telehealth.SurveyActivity;

import java.util.ArrayList;

public class ResurveyAdapter extends RecyclerView.Adapter<ResurveyAdapter.MyViewHolder>{
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<ResurveyModel> resurveyModelArrayList;
    private ArrayList<ResurveyModel> resurveyModelArrayList2;
    private ArrayList<ResurveyModel> resurveyModelArrayList3;
    private ArrayList<ResurveyModel> resurveyModelArrayList4;
    private ResurveyModel resurveyModel;

    DBhandler dBhandler;
    String surveyID,familyID;
    ArrayList<String> memberId = new ArrayList<>();

    public ResurveyAdapter(Context ctx, ArrayList<ResurveyModel> resurveyModelArrayList,
                           ArrayList<ResurveyModel> resurveyModelArrayList2,
                           ArrayList<ResurveyModel> resurveyModelArrayList3,
                           ArrayList<ResurveyModel> resurveyModelArrayList4){
        this.context = ctx;
        inflater = LayoutInflater.from(ctx);
        this.resurveyModelArrayList = resurveyModelArrayList;
        this.resurveyModelArrayList2 = resurveyModelArrayList2;
        this.resurveyModelArrayList3 = resurveyModelArrayList3;
        this.resurveyModelArrayList4 = resurveyModelArrayList4;


    }

    @Override
    public ResurveyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.cardview_resurvey, parent, false);
        ResurveyAdapter.MyViewHolder holder = new ResurveyAdapter.MyViewHolder(view);

        return holder;
    }
    public void onBindViewHolder(ResurveyAdapter.MyViewHolder holder, int position) {
        resurveyModel = resurveyModelArrayList.get(position);
        //Picasso.get().load(familyHeadModelArrayList.get(position).getThumbnail()).into(holder.iv);
        holder.surveyId.setText("Head Name : " + resurveyModelArrayList.get(position).getSSFM_HEAD_NAME());
        holder.surveyAddress.setText("Address : " + resurveyModelArrayList.get(position).getSSFM_ADDR());
        holder.surveyDate.setText("Date/Time : " + resurveyModelArrayList.get(position).getSSR_CRT_DT());
        holder.counter.setText("Re-Survey\n"+"0"+ ++position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DBhandler dBhandler = new DBhandler(context);

                for (int i = 0; i<resurveyModelArrayList.size(); i++){
                    dBhandler.addFamily_head_db(
                            resurveyModelArrayList.get(i).getSSFM_ID(),
                            resurveyModelArrayList.get(i).getSSFM_HEAD_NAME(),
                            resurveyModelArrayList.get(i).getSSFM_CONTACT_NO(),
                            resurveyModelArrayList.get(i).getSSFM_HOUSE_NO(),
                            resurveyModelArrayList.get(i).getSSFM_ADDR(),
                            resurveyModelArrayList.get(i).getSSFM_GAON_PNCHYT(),
                            resurveyModelArrayList.get(i).getSSFM_BLOCK_CODE(),
                            resurveyModelArrayList.get(i).getSSFM_CITY_CODE(),
                            resurveyModelArrayList.get(i).getSSFM_DIST_CODE(),
                            resurveyModelArrayList.get(i).getSSFM_STATE_CODE(),
                            resurveyModelArrayList.get(i).getSSFM_PIN());
                /*for (int i = 0; i<resurveyModelArrayList.size(); i++){

                }*/


                    //Insert Family Head data to Sqlite

                    for (int j = 0; j<resurveyModelArrayList2.size(); j++){
                        dBhandler.addfamilymember(
                                resurveyModelArrayList2.get(j).getSSR_REGN_NUM(),
                                resurveyModelArrayList2.get(j).getSSR_REGN_DATE(),
                                resurveyModelArrayList2.get(j).getSSR_REGN_STATUS(),
                                resurveyModelArrayList2.get(j).getSSR_PATIENT_NAME(),
                                resurveyModelArrayList2.get(j).getSSR_GENDER(),
                                resurveyModelArrayList2.get(j).getSSR_DOB(),
                                resurveyModelArrayList2.get(j).getSSR_AGE_YR(),
                                resurveyModelArrayList2.get(j).getSSR_AGE_MN(),
                                resurveyModelArrayList2.get(j).getSSR_AGE_DY(),
                                resurveyModelArrayList2.get(j).getSSR_CONTACT_NO(),
                                resurveyModelArrayList2.get(j).getSSR_AREA_LOCALITY(),
                                resurveyModelArrayList2.get(j).getSSR_DISTRICT_CD(),
                                resurveyModelArrayList2.get(j).getSSR_BLOCK_NAME(),
                                resurveyModelArrayList2.get(j).getSSR_PANCHAYAT_NAME(),
                                resurveyModelArrayList2.get(j).getSSR_VILLAGE_NAME(),
                                resurveyModelArrayList2.get(j).getSSR_CRT_DT(),
                                resurveyModelArrayList2.get(j).getSSR_CRT_USER_ID(),
                                resurveyModelArrayList2.get(j).getSSR_LST_UPD_DT(),
                                resurveyModelArrayList2.get(j).getFamily_id());
                    }

                    for (int k = 0; k<resurveyModelArrayList3.size();k++){
                        dBhandler.addGeneralHabitsAlcohol(
                                resurveyModelArrayList3.get(k).getGroup_surveyid(),
                                resurveyModelArrayList3.get(k).getMemberId(),
                                resurveyModelArrayList3.get(k).getFamily_id(),
                                resurveyModelArrayList3.get(k).getMember_name(),
                                resurveyModelArrayList3.get(k).getSmoking(),
                                resurveyModelArrayList3.get(k).getAlcohol(),
                                resurveyModelArrayList3.get(k).getMemberSurvey_id(),
                                resurveyModelArrayList3.get(k).getLatitude(),
                                resurveyModelArrayList3.get(k).getLongtitude(),
                                resurveyModelArrayList3.get(k).getSSR_CRT_DT());
                        memberId.add(resurveyModelArrayList3.get(k).getMemberId());
                        surveyID = resurveyModelArrayList3.get(k).getGroup_surveyid();
                        familyID = resurveyModelArrayList3.get(k).getFamily_id();

                        //Insert Test Findings to Sqlite
                        dBhandler.addTestFindings(
                                resurveyModelArrayList3.get(k).getGroup_surveyid(),
                                resurveyModelArrayList3.get(k).getSSR_REGN_NUM(),
                                resurveyModelArrayList3.get(k).getFamily_id(),
                                resurveyModelArrayList3.get(k).getMember_name(),
                                resurveyModelArrayList3.get(k).getSys(),
                                resurveyModelArrayList3.get(k).getDia(),
                                resurveyModelArrayList3.get(k).getType(),
                                resurveyModelArrayList3.get(k).getValue(),
                                resurveyModelArrayList3.get(k).getMemberSurvey_id(),
                                resurveyModelArrayList3.get(k).getSSR_CRT_DT());

                        //Insert Health card Information to Sqlite
                        dBhandler.addHCI(resurveyModelArrayList3.get(k).getGroup_surveyid(),
                                resurveyModelArrayList3.get(k).getSSR_REGN_NUM(),
                                resurveyModelArrayList3.get(k).getFamily_id(),
                                resurveyModelArrayList3.get(k).getMember_name(),
                                resurveyModelArrayList3.get(k).getAtal_amrit(),
                                resurveyModelArrayList3.get(k).getAyushman_bharat(),
                                resurveyModelArrayList3.get(k).getMemberSurvey_id(),
                                resurveyModelArrayList3.get(k).getSSR_CRT_DT());

                        dBhandler.addOtherInfo(resurveyModelArrayList3.get(k).getGroup_surveyid(),
                                resurveyModelArrayList3.get(k).getSSR_REGN_NUM(),
                                resurveyModelArrayList3.get(k).getFamily_id(),
                                resurveyModelArrayList3.get(k).getMember_name(),
                                resurveyModelArrayList3.get(k).getTelemedicine_booked(),
                                resurveyModelArrayList3.get(k).getOpd_booked(),
                                resurveyModelArrayList3.get(k).getAmbulance_booked(),
                                resurveyModelArrayList3.get(k).getMemberSurvey_id(),
                                resurveyModelArrayList3.get(k).getSSR_CRT_DT());


                    }
                    for (int l = 0;l<resurveyModelArrayList4.size();l++){
                        dBhandler.addSymptomsMember(
                                resurveyModelArrayList4.get(l).getGroup_surveyid(),
                                resurveyModelArrayList4.get(l).getATR_CODE(),
                                resurveyModelArrayList4.get(l).getSSR_REGN_NUM(),
                                resurveyModelArrayList4.get(l).getFamily_id(),
                                resurveyModelArrayList4.get(l).getMember_name(),
                                resurveyModelArrayList4.get(l).getPRT_DESC(),
                                resurveyModelArrayList4.get(l).getATR_DESC(),
                                resurveyModelArrayList4.get(l).getCheckState(),
                                resurveyModelArrayList4.get(l).getMemberSurvey_id(),
                                resurveyModelArrayList4.get(l).getSSR_CRT_DT());
                    }

                }

                dBhandler.addSurveyTypeFlag(surveyID,familyID,"R");

                Intent i = new Intent(inflater.getContext(), FamilyHeadActivity.class);
                i.putExtra("resurvey","resurvey");
                i.putExtra("SurveyId",surveyID);
                i.putExtra("FamilyId",familyID);
                i.putStringArrayListExtra("MemberId",memberId);
                Log.d("letsee", "showSymptomsMembersResurvey2: "+memberId);
                inflater.getContext().startActivity(i);
            }
        });

    }
    @Override
    public int getItemCount() {
        return resurveyModelArrayList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView surveyId, surveyDate, counter, syncSurvey, surveyAddress;

        public MyViewHolder(View itemView) {
            super(itemView);

            surveyId = (TextView) itemView.findViewById(R.id.ssHeadName);
            surveyDate = (TextView) itemView.findViewById(R.id.ssSurveyDate);
            counter = (TextView) itemView.findViewById(R.id.tvCounterSurvey);
            surveyAddress = itemView.findViewById(R.id.ssSurveyAddress);
            //iv = (ImageView) itemView.findViewById(R.id.iv);
        }

    }
}

