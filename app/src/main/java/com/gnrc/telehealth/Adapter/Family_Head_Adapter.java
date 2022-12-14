package com.gnrc.telehealth.Adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.gnrc.telehealth.DatabaseSqlite.DBhandler;
import com.gnrc.telehealth.MemberSelectionActivity;
import com.gnrc.telehealth.Model.AddFamilyModel;
import com.gnrc.telehealth.Model.Family_Head_Model;
import com.gnrc.telehealth.Model.StateDataModel;
import com.gnrc.telehealth.R;
import com.gnrc.telehealth.AddMemberActivity;
import com.gnrc.telehealth.ShowSurveyActivity;
import com.gnrc.telehealth.SurveyActivity;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class Family_Head_Adapter extends RecyclerView.Adapter<Family_Head_Adapter.MyViewHolder> implements Filterable {

    private LayoutInflater inflater;
    private ArrayList<Family_Head_Model> familyHeadModelArrayList;
    private ArrayList<Family_Head_Model> filteredlist;
    private ArrayList<Family_Head_Model> count;
    private Family_Head_Model familyHeadModel;

    public userclicklistener userclicklistener;
    private int aCount = 0;
    private ArrayList<String> names = new ArrayList<String>();


    public interface userclicklistener{
        void selecteduser(Family_Head_Model familyHeadModel);
    }

    public Family_Head_Adapter(Context ctx, ArrayList<Family_Head_Model> familyHeadModelArrayList,userclicklistener userclicklistener){

        inflater = LayoutInflater.from(ctx);
        this.familyHeadModelArrayList = familyHeadModelArrayList;

        filteredlist = new ArrayList<>(familyHeadModelArrayList);
        this.userclicklistener = userclicklistener;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.cardview_item_design, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        familyHeadModel = familyHeadModelArrayList.get(position);
        //Picasso.get().load(familyHeadModelArrayList.get(position).getThumbnail()).into(holder.iv);
        holder.name.setText(familyHeadModelArrayList.get(position).getFamilyhead());
        holder.phone.setText(familyHeadModelArrayList.get(position).getPhone());
        holder.city.setText(familyHeadModelArrayList.get(position).getAddress());
        holder.view.setText(familyHeadModelArrayList.get(position).getViewtext());
        holder.survey.setText(familyHeadModelArrayList.get(position).getEdittext());
        //holder.counter.setText(String.valueOf(++position));


        holder.survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = holder.dBhandler.getFamilyMemberListWithoutMember
                        (familyHeadModelArrayList.get(holder.getAbsoluteAdapterPosition()).getId());
                if (cursor.getCount()>0){
                    Intent i = new Intent(inflater.getContext(), MemberSelectionActivity.class);
                    i.putExtra("familyId",familyHeadModelArrayList.get(holder.getAdapterPosition()).getId());
                    i.putExtra("headPhoneNo",familyHeadModelArrayList.get(holder.getAbsoluteAdapterPosition())
                            .getPhone());
                    inflater.getContext().startActivity(i);
                }else{
                    Toast.makeText(inflater.getContext(), "Please add member(s) first", Toast.LENGTH_SHORT).show();
                }


            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            int count = 0;
            @Override
            public void onClick(View v) {
                int c = count++;
                /*familyHeadModelArrayList.get(holder.getAdapterPosition()).setCurrentcount(familyHeadModelArrayList.get(holder.getAdapterPosition()).getCurrentcount()+1);
                Toast.makeText(inflater.getContext(), ""+c, Toast.LENGTH_SHORT).show();*/
                userclicklistener.selecteduser(familyHeadModel);
            }

        });
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(inflater.getContext(), AddMemberActivity.class);
                i.putExtra("address",familyHeadModelArrayList.get(holder.getAdapterPosition()).getAddress());
                i.putExtra("gaon",familyHeadModelArrayList.get(holder.getAdapterPosition()).getGaon_panchayat());
                i.putExtra("block",familyHeadModelArrayList.get(holder.getAdapterPosition()).getBlock_code());
                i.putExtra("dist",familyHeadModelArrayList.get(holder.getAdapterPosition()).getDist());
                i.putExtra("id",familyHeadModelArrayList.get(holder.getAdapterPosition()).getId());
                Log.d("deep", "onClick: "+ familyHeadModelArrayList.get(holder.getAdapterPosition()).getId());
                inflater.getContext().startActivity(i);

            }

        });
    }

    @Override
    public int getItemCount() {
        return familyHeadModelArrayList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView phone, name, city,counter;
        TextView view,survey;
        int cnt = 0;
        DBhandler dBhandler = new DBhandler(itemView.getContext());
        public MyViewHolder(View itemView) {
            super(itemView);

            phone = (TextView) itemView.findViewById(R.id.phone);
            name = (TextView) itemView.findViewById(R.id.name);
            city = (TextView) itemView.findViewById(R.id.city);
            view =  itemView.findViewById(R.id.btn_addMember);
            survey =  itemView.findViewById(R.id.btn_survey);
            //counter = (TextView) itemView.findViewById(R.id.tvCounter);

            //iv = (ImageView) itemView.findViewById(R.id.iv);
        }

    }
    @Override
    public Filter getFilter() {
        return datafilter;
    }
    private Filter datafilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Family_Head_Model> filtered = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                filtered.addAll(filteredlist);
            }else {
                String filterpattern = constraint.toString().toLowerCase().trim();
                for (Family_Head_Model item:filteredlist ){
                    if (item.getFamilyhead().toLowerCase().contains(filterpattern)){
                        filtered.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filtered;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
        familyHeadModelArrayList.clear();
        familyHeadModelArrayList.addAll((ArrayList) results.values);
        notifyDataSetChanged();
        }
    };

    private void thisWasClicked(int position) {
            aCount++;
          /*  familyHeadModelArrayList.get(position).setCurrentcount(aCount);


        //aCount++;
        familyHeadModelArrayList.get(position).setCurrentcount(aCount);
        Toast.makeText(inflater.getContext(), ""+familyHeadModelArrayList.get(position).getCurrentcount(), Toast.LENGTH_SHORT).show();
*/
    }

}