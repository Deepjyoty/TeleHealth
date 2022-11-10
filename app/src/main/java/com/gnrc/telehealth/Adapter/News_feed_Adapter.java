package com.gnrc.telehealth.Adapter;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.gnrc.telehealth.Model.Model_newsfeed;
import com.gnrc.telehealth.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class News_feed_Adapter extends RecyclerView.Adapter<News_feed_Adapter.MyViewHolder> implements Filterable {

    private LayoutInflater inflater;
    private ArrayList<Model_newsfeed> dataModelArrayList;
    private ArrayList<Model_newsfeed> filteredlist;
    private ArrayList<Model_newsfeed> count;
    private Model_newsfeed dataModel;
    public userclicklistener userclicklistener;

    private int aCount = 0;


    public interface userclicklistener{
        void selecteduser(Model_newsfeed dataModel);
    }
    public News_feed_Adapter(Context ctx, ArrayList<Model_newsfeed> dataModelArrayList, userclicklistener userclicklistener){

        inflater = LayoutInflater.from(ctx);
        this.dataModelArrayList = dataModelArrayList;
        filteredlist = new ArrayList<>(dataModelArrayList);
        this.userclicklistener = userclicklistener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.cardview_news_design, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Model_newsfeed dataModel = dataModelArrayList.get(position);
        Picasso.get().load(dataModelArrayList.get(position).getThumbnail_url()).into(holder.iv);
        holder.name.setText(dataModelArrayList.get(position).getTitle());
        holder.brand.setText(dataModelArrayList.get(position).getAuthor());
        holder.price.setText(String.valueOf(dataModelArrayList.get(position).getDate()));
        dataModelArrayList.get(position).getContent_desc();



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            int count = 0;
            @Override
            public void onClick(View v) {
                //int c = count++;
/*                dataModelArrayList.get(holder.getAdapterPosition()).setCurrentcount(dataModelArrayList.get(holder.getAdapterPosition()).getCurrentcount()+1);
                //Toast.makeText(inflater.getContext(), ""+c, Toast.LENGTH_SHORT).show();*/
                /*if (dataModelArrayList.get(holder.getAdapterPosition()).getContent_type() == "video"){
                    v.getContext().startActivity(new Intent(v.getContext(), DataDetails.class).putExtra("data",dataModel));
                    userclicklistener.selecteduser(dataModel);
                }else{
                    userclicklistener.selecteduser(dataModel);
                }*/
                userclicklistener.selecteduser(dataModel);

            }

        });


    }

    @Override
    public int getItemCount() {
        return dataModelArrayList.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView brand, name, price;
        ImageView iv;

        public MyViewHolder(View itemView) {
            super(itemView);

            brand = (TextView) itemView.findViewById(R.id.newsauthor);
            name = (TextView) itemView.findViewById(R.id.newstitle);
            price = (TextView) itemView.findViewById(R.id.newsdate);
            iv = (ImageView) itemView.findViewById(R.id.newsimage);
        }

    }
    @Override
    public Filter getFilter() {
        return datafilter;
    }
    private Filter datafilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Model_newsfeed> filtered = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                filtered.addAll(filteredlist);
            }else {
                String filterpattern = constraint.toString().toLowerCase().trim();
                for (Model_newsfeed item:filteredlist ){
                    if (item.getTitle().toLowerCase().contains(filterpattern)){
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
           // dataModelArrayList.get(position).setCurrentcount(aCount);
        //aCount++;
        //dataModelArrayList.get(position).setCurrentcount(aCount);
        //Toast.makeText(inflater.getContext(), ""+dataModelArrayList.get(position).getCurrentcount(), Toast.LENGTH_SHORT).show();


    }

}