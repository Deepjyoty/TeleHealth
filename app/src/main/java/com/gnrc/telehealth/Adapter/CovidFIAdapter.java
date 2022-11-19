package com.gnrc.telehealth.Adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.gnrc.telehealth.Model.MemberDetailsForDialogModel;
import com.gnrc.telehealth.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class CovidFIAdapter extends RecyclerView.Adapter<CovidFIAdapter.MyViewHolder>{
    private LayoutInflater inflater;
    public static ArrayList<MemberDetailsForDialogModel> addMemberDialogArrayList;
    private MemberDetailsForDialogModel memberDetailsForDialogModel;
    ArrayAdapter<String> spinnerArrayAdapter;


    public CovidFIAdapter(Context ctx, ArrayList<MemberDetailsForDialogModel> addMemberDialogArrayList){

        inflater = LayoutInflater.from(ctx);
        this.addMemberDialogArrayList = addMemberDialogArrayList;

    }
    @Override
    public CovidFIAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.covid_facts, parent, false);
        CovidFIAdapter.MyViewHolder holder = new CovidFIAdapter.MyViewHolder(view);

        return holder;
    }
    public void onBindViewHolder(CovidFIAdapter.MyViewHolder holder, int position) {
        memberDetailsForDialogModel = addMemberDialogArrayList.get(position);
        //Picasso.get().load(familyHeadModelArrayList.get(position).getThumbnail()).into(holder.iv);
        holder.member.setText(addMemberDialogArrayList.get(position).getMemberName());


    }
    @Override
    public int getItemCount() {
        return addMemberDialogArrayList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView member;
        RadioGroup covid,dose,reason;
        RadioButton radioButton1,radioButton2,radioButton3;

        public MyViewHolder(View itemView) {
            super(itemView);


            member = (TextView) itemView.findViewById(R.id.tvCovidMememberName);
            covid = (RadioGroup) itemView.findViewById(R.id.covidMemberName);
            dose = (RadioGroup) itemView.findViewById(R.id.covidDoseRadioGroup);
            reason = (RadioGroup) itemView.findViewById(R.id.covidReasonRadioGroup);

            covid.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    radioButton1 = (RadioButton) covid.findViewById(checkedId);
                    addMemberDialogArrayList.get(getAdapterPosition()).
                            setCovidStatus(radioButton1.getText().toString());
                }
            });
            dose.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    radioButton2 = (RadioButton) dose.findViewById(checkedId);
                    addMemberDialogArrayList.get(getAdapterPosition()).
                            setDoseStatus(radioButton2.getText().toString());
                }
            });
            reason.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    radioButton3 = (RadioButton) reason.findViewById(checkedId);
                    addMemberDialogArrayList.get(getAdapterPosition()).
                            setNoVaccineReason(radioButton3.getText().toString());
                }
            });

            //iv = (ImageView) itemView.findViewById(R.id.iv);
        }

    }}
