package com.gnrc.telehealth.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import static android.R.layout.simple_spinner_item;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.gnrc.telehealth.Family_List_Activity;
import com.gnrc.telehealth.Model.DataModel;
import com.gnrc.telehealth.Model.RoomModel;
import com.gnrc.telehealth.Model.StateDataModel;
import com.gnrc.telehealth.R;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.MyViewHolder> implements Filterable {

    private LayoutInflater inflater;
    private ArrayList<DataModel> dataModelArrayList;
    private ArrayList<StateDataModel> statedataModelArrayList;
    private ArrayList<DataModel> filteredlist;
    private ArrayList<DataModel> count;
    private DataModel dataModel;
    private StateDataModel stateDataModel;
    public userclicklistener userclicklistener;
    //private DBhandler dbHandler;
    private int aCount = 0;
    private ArrayList<String> names = new ArrayList<String>();
    TextInputLayout familyhead, phone, house, address, city, pin;
    Spinner dist, state;


    public interface userclicklistener{
        void selecteduser(DataModel dataModel);
    }

    public RvAdapter(Context ctx, ArrayList<DataModel> dataModelArrayList, userclicklistener userclicklistener){

        inflater = LayoutInflater.from(ctx);
        this.dataModelArrayList = dataModelArrayList;
        this.statedataModelArrayList = statedataModelArrayList;
        filteredlist = new ArrayList<>(dataModelArrayList);
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
        dataModel = dataModelArrayList.get(position);
        //Picasso.get().load(dataModelArrayList.get(position).getThumbnail()).into(holder.iv);
        holder.name.setText(dataModelArrayList.get(position).getFamilyhead());
        holder.phone.setText(dataModelArrayList.get(position).getPhone());
        holder.city.setText(dataModelArrayList.get(position).getAddress());
        holder.view.setText(dataModelArrayList.get(position).getViewtext());
        holder.edit.setText(dataModelArrayList.get(position).getEdittext());

        //dataModelArrayList.get(position).getDescription();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            int count = 0;
            @Override
            public void onClick(View v) {
                int c = count++;
                /*dataModelArrayList.get(holder.getAdapterPosition()).setCurrentcount(dataModelArrayList.get(holder.getAdapterPosition()).getCurrentcount()+1);
                Toast.makeText(inflater.getContext(), ""+c, Toast.LENGTH_SHORT).show();*/
                userclicklistener.selecteduser(dataModel);
            }

        });
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
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

                familyhead.getEditText().setText(dataModelArrayList.get(holder.getAdapterPosition()).getFamilyhead());
                phone.getEditText().setText(dataModelArrayList.get(holder.getAdapterPosition()).getPhone());
                house.getEditText().setText(dataModelArrayList.get(holder.getAdapterPosition()).getHouse());
                address.getEditText().setText(dataModelArrayList.get(holder.getAdapterPosition()).getAddress());
                city.getEditText().setText(dataModelArrayList.get(holder.getAdapterPosition()).getCity());
                if ((dataModelArrayList.get(holder.getAdapterPosition()).getDist())!= null) {
                    names.add(dataModelArrayList.get(holder.getAdapterPosition()).getDist());
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(inflater.getContext(), simple_spinner_item, names);
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                    dist.setAdapter(spinnerArrayAdapter);
/*                    int spinnerPosition = spinnerArrayAdapter.getPosition(dataModelArrayList.get(holder.getAdapterPosition()).getDist());
                    dist.setSelection(spinnerPosition);*/
                }
                if ((dataModelArrayList.get(holder.getAdapterPosition()).getState())!= null) {
                    names.add(dataModelArrayList.get(holder.getAdapterPosition()).getState());
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(inflater.getContext(), simple_spinner_item, names);
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                    state.setAdapter(spinnerArrayAdapter);
                    int spinnerPosition = spinnerArrayAdapter.getPosition(dataModelArrayList.get(holder.getAdapterPosition()).getState());
                    state.setSelection(spinnerPosition);
                }
                pin.getEditText().setText(dataModelArrayList.get(holder.getAdapterPosition()).getPin());
                pin.getEditText().setText(dataModelArrayList.get(holder.getAdapterPosition()).getPin());

                //Toast.makeText(inflater.getContext(), ""+dataModelArrayList.get(holder.getAdapterPosition()).getFamilyhead(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public int getItemCount() {
        return dataModelArrayList.size();
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
            edit = (TextView) itemView.findViewById(R.id.tveditfamily);

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
            ArrayList<DataModel> filtered = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                filtered.addAll(filteredlist);
            }else {
                String filterpattern = constraint.toString().toLowerCase().trim();
                for (DataModel item:filteredlist ){
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
        dataModelArrayList.clear();
        dataModelArrayList.addAll((ArrayList) results.values);
        notifyDataSetChanged();
        }
    };

    private void thisWasClicked(int position) {
            aCount++;
          /*  dataModelArrayList.get(position).setCurrentcount(aCount);


        //aCount++;
        dataModelArrayList.get(position).setCurrentcount(aCount);
        Toast.makeText(inflater.getContext(), ""+dataModelArrayList.get(position).getCurrentcount(), Toast.LENGTH_SHORT).show();
*/
    }

}