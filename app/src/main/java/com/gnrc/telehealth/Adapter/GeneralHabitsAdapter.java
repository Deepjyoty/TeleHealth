package com.gnrc.telehealth.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gnrc.telehealth.DatabaseSqlite.DBhandler;
import com.gnrc.telehealth.Model.MemberDetailsForDialogModel;
import com.gnrc.telehealth.R;

import java.util.ArrayList;

public class GeneralHabitsAdapter extends RecyclerView.Adapter<GeneralHabitsAdapter.MyViewHolder>{
    private GeneralHabits habitListener;
    private LayoutInflater inflater;
    public static ArrayList<MemberDetailsForDialogModel> addMemberDialogArrayList;
    private MemberDetailsForDialogModel memberDetailsForDialogModel;
    private Context context;


    public GeneralHabitsAdapter(Context ctx, ArrayList<MemberDetailsForDialogModel> addMemberDialogArrayList,
                                GeneralHabits habitListener){
        inflater = LayoutInflater.from(ctx);
        this.context = ctx;
        this.addMemberDialogArrayList = addMemberDialogArrayList;
        this.habitListener = habitListener;

    }

    @Override
    public GeneralHabitsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.general_habits, parent, false);
        GeneralHabitsAdapter.MyViewHolder holder = new GeneralHabitsAdapter.MyViewHolder(view, habitListener);

        return holder;
    }

    public void onBindViewHolder(@NonNull GeneralHabitsAdapter.MyViewHolder holder, int position) {
        memberDetailsForDialogModel = addMemberDialogArrayList.get(position);
        holder.member.setText(addMemberDialogArrayList.get(position).getMemberName());



        if(addMemberDialogArrayList.get(position).isSmoker()){
            holder.smokYes.setChecked(true);
        } else {
            holder.smokNo.setChecked(true);
        }

        if(addMemberDialogArrayList.get(position).isAlcoholic()){
            holder.alcoYes.setChecked(true);
        } else {
            holder.alcoNo.setChecked(true);
        }
    }

    @Override
    public int getItemCount() {
        return addMemberDialogArrayList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView member,smoking, alcohol;
        RadioGroup radioSmoking,radioAlcohol;
        RadioButton smokYes,smokNo,alcoYes,alcoNo;
        SharedPreferences.Editor preferencesEditor;
        private GeneralHabits habitListener;

        public MyViewHolder(View itemView, GeneralHabits habitListener) {
            super(itemView);
            member =  itemView.findViewById(R.id.tvMemberName);

            smoking = itemView.findViewById(R.id.tv_smoking);
            alcohol = itemView.findViewById(R.id.tv_alcohol);

            radioSmoking =  itemView.findViewById(R.id.rgSmoking);
            radioAlcohol =  itemView.findViewById(R.id.rgAlcohol);

            smokYes =  itemView.findViewById(R.id.smokYes);
            smokNo =  itemView.findViewById(R.id.smokNo);
            alcoYes =  itemView.findViewById(R.id.alcoYes);
            alcoNo =  itemView.findViewById(R.id.alcoNo);

            smokYes.setOnClickListener(this);
            smokNo.setOnClickListener(this);
            alcoYes.setOnClickListener(this);
            alcoNo.setOnClickListener(this);

            this.habitListener = habitListener;
            SharedPreferences mPreferences2 = itemView.getContext().getSharedPreferences("language",MODE_PRIVATE);
            String language = mPreferences2.getString("lang","NoValue");

            if (language.equals("English")){
                smoking.setText("Smoking");
                alcohol.setText("Alcohol");

            }else if (language.equals("Assamese")){
                smoking.setText("ধূমপায়ী");
                alcohol.setText("মদ্যপায়ী");

            }else if (language.equals("Bengali")){
                smoking.setText("ধূমপায়ী");
                alcohol.setText("মদ্যপ");
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.smokYes:
                    habitListener.onSmokinClick(getAbsoluteAdapterPosition(), true);
                    break;
                case R.id.smokNo:
                    habitListener.onSmokinClick(getAbsoluteAdapterPosition(), false);
                    break;
                case R.id.alcoYes:
                    habitListener.onAlcoholicClick(getAbsoluteAdapterPosition(), true);
                    break;
                case R.id.alcoNo:
                    habitListener.onAlcoholicClick(getAbsoluteAdapterPosition(), false);
                    break;
            }
        }
    }

    public interface GeneralHabits {
        void onSmokinClick(int position, boolean isTrue);

        void onAlcoholicClick(int position,  boolean isTrue);
    }



}

