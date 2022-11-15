package com.gnrc.telehealth.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gnrc.telehealth.AddMemberActivity;
import com.gnrc.telehealth.Model.AddFamilyModel;

import com.gnrc.telehealth.Model.Family_Head_Model;
import com.gnrc.telehealth.R;
import com.gnrc.telehealth.SurveyActivity;

import java.util.ArrayList;


public class AddFamily_Adapter extends RecyclerView.Adapter<AddFamily_Adapter.MyViewHolder>{
    private LayoutInflater inflater;
    private ArrayList<AddFamilyModel> addFamilyModelArrayList;
    private AddFamilyModel addFamilyModel;
    public AddFamily_Adapter(Context ctx, ArrayList<AddFamilyModel> addFamilyModelArrayList){

        inflater = LayoutInflater.from(ctx);
        this.addFamilyModelArrayList = addFamilyModelArrayList;

    }
    @Override
    public AddFamily_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.card_view_fm, parent, false);
        AddFamily_Adapter.MyViewHolder holder = new AddFamily_Adapter.MyViewHolder(view);

        return holder;
    }
    public void onBindViewHolder(AddFamily_Adapter.MyViewHolder holder, int position) {
        addFamilyModel = addFamilyModelArrayList.get(position);
        //Picasso.get().load(familyHeadModelArrayList.get(position).getThumbnail()).into(holder.iv);
        holder.name.setText(addFamilyModelArrayList.get(position).getPtName());
        holder.gender.setText(addFamilyModelArrayList.get(position).getGender());
        holder.age.setText(addFamilyModelArrayList.get(position).getYear());
        //holder.edit.setText(familyHeadModelArrayList.get(position).getEdittext());

        //familyHeadModelArrayList.get(position).getDescription();
        holder.survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(inflater.getContext(), SurveyActivity.class);
                i.putExtra("familyId", addFamilyModelArrayList.get(holder.getAdapterPosition()).getF_id());
                i.putExtra("member_id", addFamilyModelArrayList.get(holder.getAdapterPosition()).getRegnum());
                inflater.getContext().startActivity(i);
            }
        });

    }
    @Override
    public int getItemCount() {
        return addFamilyModelArrayList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name, gender ,age,survey;
        ImageView iv;

        public MyViewHolder(View itemView) {
            super(itemView);


            name = (TextView) itemView.findViewById(R.id.amName);
            gender = (TextView) itemView.findViewById(R.id.amGender);
            age = (TextView) itemView.findViewById(R.id.amAge);
            survey = (TextView) itemView.findViewById(R.id.amView);

            //iv = (ImageView) itemView.findViewById(R.id.iv);
        }

    }
}

