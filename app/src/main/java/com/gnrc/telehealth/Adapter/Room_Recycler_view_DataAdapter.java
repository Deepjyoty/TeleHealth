package com.gnrc.telehealth.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gnrc.telehealth.Model.DataModel;
import com.gnrc.telehealth.Model.RoomModel;
import com.gnrc.telehealth.R;

import java.util.ArrayList;
import java.util.List;

public class Room_Recycler_view_DataAdapter extends RecyclerView.Adapter<Room_Recycler_view_DataAdapter.ViewHolder>{
    Context context;
    List<DataModel> dataModelArrayList;

    public Room_Recycler_view_DataAdapter(Context context, ArrayList<DataModel> arrayList) {
        this.context = context;
        this.dataModelArrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_item_design,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        DataModel repo = dataModelArrayList.get(position);

        holder.name.setText(repo.getFamilyhead());
        holder.phone.setText(repo.getPhone());
        holder.city.setText(repo.getAddress());




    }

    @Override
    public int getItemCount() {
        return dataModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView phone, name, city,view,edit;


        public ViewHolder(View itemView) {
            super(itemView);

            phone = (TextView) itemView.findViewById(R.id.phone);
            name = (TextView) itemView.findViewById(R.id.name);
            city = (TextView) itemView.findViewById(R.id.city);
            view = (TextView) itemView.findViewById(R.id.tvview);
            edit = (TextView) itemView.findViewById(R.id.tveditfamily);

        }
    }
}
