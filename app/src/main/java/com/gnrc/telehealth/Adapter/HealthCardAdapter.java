package com.gnrc.telehealth.Adapter;

import android.content.Context;
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


public class HealthCardAdapter extends RecyclerView.Adapter<HealthCardAdapter.MyViewHolder>{
    private LayoutInflater inflater;
    private HealthCard healthCard;
    public static ArrayList<MemberDetailsForDialogModel> addMemberDialogArrayList;
    private MemberDetailsForDialogModel memberDetailsForDialogModel;
    private Context context;

    public HealthCardAdapter(Context ctx, ArrayList<MemberDetailsForDialogModel> addMemberDialogArrayList, HealthCard healthCard){
        inflater = LayoutInflater.from(ctx);
        this.context = ctx;
        this.addMemberDialogArrayList = addMemberDialogArrayList;
        this.healthCard = healthCard;
    }

    @Override
    public HealthCardAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.health_card_information, parent, false);
        HealthCardAdapter.MyViewHolder holder = new HealthCardAdapter.MyViewHolder(view, healthCard);

        return holder;
    }
    public void onBindViewHolder(@NonNull HealthCardAdapter.MyViewHolder holder, int position) {
        memberDetailsForDialogModel = addMemberDialogArrayList.get(position);

        holder.member.setText(addMemberDialogArrayList.get(position).getMemberName());
        if(addMemberDialogArrayList.get(position).isAtal()){
            holder.atalYes.setChecked(true);
        } else {
            holder.atalNo.setChecked(true);
        }

        if(addMemberDialogArrayList.get(position).isAyush()){
            holder.ayushYes.setChecked(true);
        } else {
            holder.ayushNo.setChecked(true);
        }
    }
    @Override
    public int getItemCount() {
        return addMemberDialogArrayList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView member;
        RadioGroup radioAtal,radioAyushman;
        RadioButton atalYes,atalNo,ayushYes,ayushNo;
        private HealthCard healthCard;

        public MyViewHolder(View itemView, HealthCard healthCard) {
            super(itemView);
            member = (TextView) itemView.findViewById(R.id.tvHciMemberName);
            radioAtal = (RadioGroup) itemView.findViewById(R.id.rgAtal);
            radioAyushman = (RadioGroup) itemView.findViewById(R.id.rgAyushman);

            atalYes = (RadioButton) itemView.findViewById(R.id.rbAtalYes);
            atalNo = (RadioButton) itemView.findViewById(R.id.rbAtalNo);
            ayushYes = (RadioButton) itemView.findViewById(R.id.rbAyushYes);
            ayushNo = (RadioButton) itemView.findViewById(R.id.rbAyushNo);

            atalYes.setOnClickListener(this);
            atalNo.setOnClickListener(this);
            ayushYes.setOnClickListener(this);
            ayushNo.setOnClickListener(this);

            this.healthCard = healthCard;


        }


        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.rbAtalYes:
                    healthCard.onAtalClick(getAbsoluteAdapterPosition(), true);
                    break;
                case R.id.rbAtalNo:
                    healthCard.onAtalClick(getAbsoluteAdapterPosition(), false);
                    break;
                case R.id.rbAyushYes:
                    healthCard.onAyushmanClick(getAbsoluteAdapterPosition(), true);
                    break;
                case R.id.rbAyushNo:
                    healthCard.onAyushmanClick(getAbsoluteAdapterPosition(), false);
                    break;
            }
        }
    }
    public interface HealthCard {
        void onAtalClick(int position, boolean isTrue);

        void onAyushmanClick(int position,  boolean isTrue);
    }

}
