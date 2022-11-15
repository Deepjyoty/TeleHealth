package com.gnrc.telehealth.Adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class TestFindingsAdapter extends RecyclerView.Adapter<TestFindingsAdapter.MyViewHolder>{
    private LayoutInflater inflater;
    public static ArrayList<MemberDetailsForDialogModel> addMemberDialogArrayList;
    private MemberDetailsForDialogModel memberDetailsForDialogModel;


    public TestFindingsAdapter(Context ctx, ArrayList<MemberDetailsForDialogModel> addMemberDialogArrayList){

        inflater = LayoutInflater.from(ctx);
        this.addMemberDialogArrayList = addMemberDialogArrayList;

    }
    @Override
    public TestFindingsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.test_findings, parent, false);
        TestFindingsAdapter.MyViewHolder holder = new TestFindingsAdapter.MyViewHolder(view);

        return holder;
    }
    public void onBindViewHolder(TestFindingsAdapter.MyViewHolder holder, int position) {
        memberDetailsForDialogModel = addMemberDialogArrayList.get(position);
        //Picasso.get().load(familyHeadModelArrayList.get(position).getThumbnail()).into(holder.iv);
        holder.member.setText(addMemberDialogArrayList.get(position).getMemberName());
        holder.sys.getEditText().setText(addMemberDialogArrayList.get(position).getEditTextValueSys());
        holder.dia.getEditText().setText(addMemberDialogArrayList.get(position).getEditTextValueDia());
        holder.value.getEditText().setText((addMemberDialogArrayList.get(position).getEditTextValueValue()));
        //holder.edit.setText(familyHeadModelArrayList.get(position).getEdittext());

        //familyHeadModelArrayList.get(position).getDescription();


    }
    @Override
    public int getItemCount() {
        return addMemberDialogArrayList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView member;
        TextInputLayout sys,dia,value;

        public MyViewHolder(View itemView) {
            super(itemView);


            member = (TextView) itemView.findViewById(R.id.testFindingsMemberName);
            sys = (TextInputLayout) itemView.findViewById(R.id.sys);
            dia = (TextInputLayout) itemView.findViewById(R.id.dia);
            value = (TextInputLayout) itemView.findViewById(R.id.typeValue);

            sys.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    addMemberDialogArrayList.get(getAdapterPosition()).setEditTextValueSys(sys.getEditText().getText().toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            dia.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    addMemberDialogArrayList.get(getAdapterPosition()).setEditTextValueDia(dia.getEditText().getText().toString());

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            value.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    addMemberDialogArrayList.get(getAdapterPosition()).setEditTextValueValue(value.getEditText().getText().toString());

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            //iv = (ImageView) itemView.findViewById(R.id.iv);
        }

    }}
