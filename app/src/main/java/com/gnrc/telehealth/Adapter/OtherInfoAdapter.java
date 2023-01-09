package com.gnrc.telehealth.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gnrc.telehealth.Model.MemberDetailsForDialogModel;
import com.gnrc.telehealth.R;

import java.util.ArrayList;

public class OtherInfoAdapter extends RecyclerView.Adapter<OtherInfoAdapter.MyViewHolder>{
    private LayoutInflater inflater;
    private OtherInfoAdapter.OtherInfo otherInfo;
    public static ArrayList<MemberDetailsForDialogModel> addMemberDialogArrayList;
    private MemberDetailsForDialogModel memberDetailsForDialogModel;
    private Context context;

    public OtherInfoAdapter(Context ctx, ArrayList<MemberDetailsForDialogModel> addMemberDialogArrayList,
                            OtherInfoAdapter.OtherInfo otherInfo){
        inflater = LayoutInflater.from(ctx);
        this.context = ctx;
        this.addMemberDialogArrayList = addMemberDialogArrayList;
        this.otherInfo = otherInfo;
    }

    @Override
    public OtherInfoAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.other_info_dialog, parent, false);
        OtherInfoAdapter.MyViewHolder holder = new OtherInfoAdapter.MyViewHolder(view, otherInfo);

        return holder;
    }
    public void onBindViewHolder(@NonNull OtherInfoAdapter.MyViewHolder holder, int position) {
        memberDetailsForDialogModel = addMemberDialogArrayList.get(position);

        holder.member.setText(addMemberDialogArrayList.get(position).getMemberName());
        if(addMemberDialogArrayList.get(position).isTeleMed()){
            holder.teleMedYes.setChecked(true);
        } else {
            holder.teleMedNo.setChecked(true);
        }

        if(addMemberDialogArrayList.get(position).isOpd()){
            holder.opdYes.setChecked(true);
        } else {
            holder.opdNo.setChecked(true);
        }
        if(addMemberDialogArrayList.get(position).isAmbulance()){
            holder.ambuYes.setChecked(true);
        } else {
            holder.ambuNo.setChecked(true);
        }
    }
    @Override
    public int getItemCount() {
        return addMemberDialogArrayList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView member, telemed, opd, ambul;
        RadioGroup radioTeleMed,radioOpd,radioAmbulance;
        RadioButton teleMedYes,teleMedNo,opdYes,opdNo,ambuYes,ambuNo;
        private OtherInfoAdapter.OtherInfo otherInfo;

        public MyViewHolder(View itemView, OtherInfoAdapter.OtherInfo otherInfo) {
            super(itemView);
            member = (TextView) itemView.findViewById(R.id.tvOiMemberName);

            telemed = itemView.findViewById(R.id.tv_teleMed);
            opd = itemView.findViewById(R.id.tv_opd);
            ambul = itemView.findViewById(R.id.tv_ambu);

            radioTeleMed = (RadioGroup) itemView.findViewById(R.id.rgTeleMed);
            radioOpd = (RadioGroup) itemView.findViewById(R.id.rgOpd);
            radioAmbulance = (RadioGroup) itemView.findViewById(R.id.rgAmbulance);

            teleMedYes = (RadioButton) itemView.findViewById(R.id.rbTeleMedYes);
            teleMedNo = (RadioButton) itemView.findViewById(R.id.rbTeleMedNo);
            opdYes = (RadioButton) itemView.findViewById(R.id.rbOpdYes);
            opdNo = (RadioButton) itemView.findViewById(R.id.rbOpdNo);
            ambuYes = (RadioButton) itemView.findViewById(R.id.rbAmbuYes);
            ambuNo = (RadioButton) itemView.findViewById(R.id.rbAmbuNo);

            teleMedYes.setOnClickListener(this);
            teleMedNo.setOnClickListener(this);
            opdYes.setOnClickListener(this);
            opdNo.setOnClickListener(this);
            ambuYes.setOnClickListener(this);
            ambuNo.setOnClickListener(this);

            this.otherInfo = otherInfo;

            SharedPreferences mPreferences2 = itemView.getContext().getSharedPreferences("language",MODE_PRIVATE);
            String language = mPreferences2.getString("lang","NoValue");

            if (language.equals("English")){
                telemed.setText("Telemedicine Booked?");
                opd.setText("OPD Booked?");
                ambul.setText("Ambulance Booked?");

            }else if (language.equals("Assamese")){
                telemed.setText("টেলিমেডিচিন বুকিং কৰা হৈছে?");
                opd.setText("OPD বুকিং কৰা হৈছে?");
                ambul.setText("এম্বুলেন্স বুকিং কৰা হৈছে?");

            }else if (language.equals("Bengali")){
                telemed.setText("টেলিমেডিসিন বুকড?");
                opd.setText("ওপিডি বুকড?");
                ambul.setText("অ্যাম্বুলেন্স বুকড?");
            }
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.rbTeleMedYes:
                    otherInfo.onTeleMedClick(getAbsoluteAdapterPosition(), true);
                    break;
                case R.id.rbTeleMedNo:
                    otherInfo.onTeleMedClick(getAbsoluteAdapterPosition(), false);
                    break;
                case R.id.rbOpdYes:
                    otherInfo.onOpdClick(getAbsoluteAdapterPosition(), true);
                    break;
                case R.id.rbOpdNo:
                    otherInfo.onOpdClick(getAbsoluteAdapterPosition(), false);
                    break;
                case R.id.rbAmbuYes:
                    otherInfo.onAmbulanceClick(getAbsoluteAdapterPosition(), true);
                    break;
                case R.id.rbAmbuNo:
                    otherInfo.onAmbulanceClick(getAbsoluteAdapterPosition(), false);
                    break;
            }
        }
    }
    public interface OtherInfo {
        void onTeleMedClick(int position, boolean isTrue);

        void onOpdClick(int position,  boolean isTrue);

        void onAmbulanceClick(int position,  boolean isTrue);
    }

}
