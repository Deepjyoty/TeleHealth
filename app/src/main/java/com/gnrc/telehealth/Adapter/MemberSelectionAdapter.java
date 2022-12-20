package com.gnrc.telehealth.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gnrc.telehealth.Model.AddFamilyModel;
import com.gnrc.telehealth.Model.Family_Head_Model;
import com.gnrc.telehealth.Model.MemberDetailsForDialogModel;
import com.gnrc.telehealth.R;
import com.gnrc.telehealth.SurveyActivity;

import java.util.ArrayList;

public class MemberSelectionAdapter extends RecyclerView.Adapter<MemberSelectionAdapter.MyViewHolder>{
    private LayoutInflater inflater;
    private ArrayList<AddFamilyModel> addFamilyModelArrayList;
    public static ArrayList<MemberDetailsForDialogModel> addMemberDialogArrayList;
    private AddFamilyModel addFamilyModel;
    private ArrayList<String> checkedID = new ArrayList<>();
    public userclicklistener userclicklistener;
    MemberDetailsForDialogModel memberDetailsForDialogModel;

    public interface userclicklistener{
        void memberId(ArrayList<String> checkedId);
        void removeMemberID(ArrayList<String> checkedId);
    }

    public MemberSelectionAdapter(Context ctx, ArrayList<AddFamilyModel> addFamilyModelArrayList,
                                  userclicklistener userclicklistener){

        inflater = LayoutInflater.from(ctx);
        this.addFamilyModelArrayList = addFamilyModelArrayList;
        this.userclicklistener = userclicklistener;

    }
    @Override
    public MemberSelectionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.cardview_member_selection, parent, false);
        MemberSelectionAdapter.MyViewHolder holder = new MemberSelectionAdapter.MyViewHolder(view);

        return holder;
    }
    public void onBindViewHolder(MemberSelectionAdapter.MyViewHolder holder, int position) {
        addFamilyModel = addFamilyModelArrayList.get(position);
        //Picasso.get().load(familyHeadModelArrayList.get(position).getThumbnail()).into(holder.iv);
        holder.name.setText(addFamilyModelArrayList.get(position).getPtName());
        holder.gender.setText(addFamilyModelArrayList.get(position).getGender());
        holder.age.setText(addFamilyModelArrayList.get(position).getYear());
        //holder.edit.setText(familyHeadModelArrayList.get(position).getEdittext());

        //familyHeadModelArrayList.get(position).getDescription();

        holder.selection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    checkedID.add(addFamilyModelArrayList.get(holder.getAdapterPosition()).getRegnum());
                    userclicklistener.memberId(checkedID);
                }else{
                    checkedID.remove(addFamilyModelArrayList.get(holder.getAdapterPosition()).getRegnum());
                    userclicklistener.removeMemberID(checkedID);
                }
            }
        });

        /*holder.selection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.selection.isChecked()){

                }else {

                }
                *//*Intent i = new Intent(inflater.getContext(), SurveyActivity.class);
                i.putExtra("familyId", addFamilyModelArrayList.get(holder.getAdapterPosition()).getF_id());
                i.putExtra("member_id", addFamilyModelArrayList.get(holder.getAdapterPosition()).getRegnum());
                inflater.getContext().startActivity(i);*//*
            }

        });*/
        //addFamilyModel.setMemberId(checkedID);
        Log.d("dikhao", "onBindViewHolder: "+addFamilyModel);

    }
    @Override
    public int getItemCount() {
        return addFamilyModelArrayList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name, gender ,age;
        ImageView iv;
        CheckBox selection;

        public MyViewHolder(View itemView) {
            super(itemView);


            name = (TextView) itemView.findViewById(R.id.amName);
            gender = (TextView) itemView.findViewById(R.id.amGender);
            age = (TextView) itemView.findViewById(R.id.amAge);
            selection = (CheckBox) itemView.findViewById(R.id.amView);

            //iv = (ImageView) itemView.findViewById(R.id.iv);
        }

    }
}

