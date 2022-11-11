package com.gnrc.telehealth.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gnrc.telehealth.Model.Family_Head_Model;
import com.gnrc.telehealth.Model.StateDataModel;
import com.gnrc.telehealth.R;
import com.gnrc.telehealth.AddMemberActivity;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class Family_Head_Adapter extends RecyclerView.Adapter<Family_Head_Adapter.MyViewHolder> implements Filterable {

    private LayoutInflater inflater;
    private ArrayList<Family_Head_Model> familyHeadModelArrayList;
    private ArrayList<StateDataModel> statedataModelArrayList;
    private ArrayList<Family_Head_Model> filteredlist;
    private ArrayList<Family_Head_Model> count;
    private Family_Head_Model familyHeadModel;
    private StateDataModel stateDataModel;
    public userclicklistener userclicklistener;
    //private DBhandler dbHandler;
    private int aCount = 0;
    private ArrayList<String> names = new ArrayList<String>();
    TextInputLayout familyhead, phone, house, address, city, pin;
    Spinner dist, state;


    public interface userclicklistener{
        void selecteduser(Family_Head_Model familyHeadModel);
    }

    public Family_Head_Adapter(Context ctx, ArrayList<Family_Head_Model> familyHeadModelArrayList, userclicklistener userclicklistener){

        inflater = LayoutInflater.from(ctx);
        this.familyHeadModelArrayList = familyHeadModelArrayList;
        this.statedataModelArrayList = statedataModelArrayList;
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
        //holder.edit.setText(familyHeadModelArrayList.get(position).getEdittext());

        //familyHeadModelArrayList.get(position).getDescription();

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
                inflater.getContext().startActivity(i);
/*                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Family Details");
                final View customLayout = inflater.inflate(R.layout.family_head_custom_alert_dialog,null);
                builder.setView(customLayout);
                AlertDialog dialog = builder.create();
                dialog.show();
                familyhead = customLayout.findViewById(R.id.etfamilyhead);
                phone = customLayout.findViewById(R.id.etfamilyheadphone);
                house = customLayout.findViewById(R.id.etfamilyheadhouseno);
                address = customLayout.findViewById(R.id.etfamilyheadaddress);
                city = customLayout.findViewById(R.id.etfamilyheadcity);
                dist = customLayout.findViewById(R.id.spfamilyheaddistrict);
                state = customLayout.findViewById(R.id.spfamilyheadstate);
                pin = customLayout.findViewById(R.id.etfamilyheadpin);

                familyhead.getEditText().setText(familyHeadModelArrayList.get(holder.getAdapterPosition()).getFamilyhead());
                phone.getEditText().setText(familyHeadModelArrayList.get(holder.getAdapterPosition()).getPhone());
                house.getEditText().setText(familyHeadModelArrayList.get(holder.getAdapterPosition()).getHouse());
                address.getEditText().setText(familyHeadModelArrayList.get(holder.getAdapterPosition()).getAddress());
                city.getEditText().setText(familyHeadModelArrayList.get(holder.getAdapterPosition()).getCity());
                if ((familyHeadModelArrayList.get(holder.getAdapterPosition()).getDist())!= null) {
                    names.add(familyHeadModelArrayList.get(holder.getAdapterPosition()).getDist());
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(inflater.getContext(), simple_spinner_item, names);
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                    dist.setAdapter(spinnerArrayAdapter);
*//*                    int spinnerPosition = spinnerArrayAdapter.getPosition(familyHeadModelArrayList.get(holder.getAdapterPosition()).getDist());
                    dist.setSelection(spinnerPosition);*//*
                }
                if ((familyHeadModelArrayList.get(holder.getAdapterPosition()).getState())!= null) {
                    names.add(familyHeadModelArrayList.get(holder.getAdapterPosition()).getState());
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(inflater.getContext(), simple_spinner_item, names);
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                    state.setAdapter(spinnerArrayAdapter);
                    int spinnerPosition = spinnerArrayAdapter.getPosition(familyHeadModelArrayList.get(holder.getAdapterPosition()).getState());
                    state.setSelection(spinnerPosition);
                }
                pin.getEditText().setText(familyHeadModelArrayList.get(holder.getAdapterPosition()).getPin());
                pin.getEditText().setText(familyHeadModelArrayList.get(holder.getAdapterPosition()).getPin());*/

                //Toast.makeText(inflater.getContext(), ""+familyHeadModelArrayList.get(holder.getAdapterPosition()).getFamilyhead(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public int getItemCount() {
        return familyHeadModelArrayList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView phone, name, city,view,edit;
        ImageView iv;

        public MyViewHolder(View itemView) {
            super(itemView);

            phone = (TextView) itemView.findViewById(R.id.phone);
            name = (TextView) itemView.findViewById(R.id.name);
            city = (TextView) itemView.findViewById(R.id.city);
            view = (TextView) itemView.findViewById(R.id.tvview);
            //edit = (TextView) itemView.findViewById(R.id.tveditfamily);

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