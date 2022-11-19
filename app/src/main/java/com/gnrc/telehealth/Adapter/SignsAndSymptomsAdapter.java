package com.gnrc.telehealth.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gnrc.telehealth.Model.MemberDetailsForDialogModel;
import com.gnrc.telehealth.Model.SignsAndSymptomsModel;
import com.gnrc.telehealth.R;

import java.util.ArrayList;

public class SignsAndSymptomsAdapter extends RecyclerView.Adapter<SignsAndSymptomsAdapter.MyViewHolder>{
    private LayoutInflater inflater;
    public static ArrayList<SignsAndSymptomsModel> addSymptomsDialogArrraayList;
    private SignsAndSymptomsModel symptomsDetailsForDialogModel;
    ArrayAdapter<String> spinnerArrayAdapter;


    public SignsAndSymptomsAdapter(Context ctx, ArrayList<SignsAndSymptomsModel> addSymptomsDialogArrraayList){

        inflater = LayoutInflater.from(ctx);
        this.addSymptomsDialogArrraayList = addSymptomsDialogArrraayList;

    }
    @Override
    public SignsAndSymptomsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.cardview_signs_symptoms, parent, false);
        SignsAndSymptomsAdapter.MyViewHolder holder = new SignsAndSymptomsAdapter.MyViewHolder(view);

        return holder;
    }
    public void onBindViewHolder(SignsAndSymptomsAdapter.MyViewHolder holder, int position) {
        symptomsDetailsForDialogModel = addSymptomsDialogArrraayList.get(position);
        //Picasso.get().load(familyHeadModelArrayList.get(position).getThumbnail()).into(holder.iv);
        holder.header.setText(addSymptomsDialogArrraayList.get(position).getPRT_DESC());
        holder.body.setText(addSymptomsDialogArrraayList.get(position).getATR_DESC());
        holder.memberName.setText(addSymptomsDialogArrraayList.get(position).getMember_name());


    }
    @Override
    public int getItemCount() {
        return addSymptomsDialogArrraayList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView header,body;
        CheckBox memberName;

        public MyViewHolder(View itemView) {
            super(itemView);


            header = (TextView) itemView.findViewById(R.id.tvHeaderSS);
            body = (TextView) itemView.findViewById(R.id.tvBodySS);
            memberName = (CheckBox) itemView.findViewById(R.id.cbMemberName);


            //iv = (ImageView) itemView.findViewById(R.id.iv);
        }

    }}

