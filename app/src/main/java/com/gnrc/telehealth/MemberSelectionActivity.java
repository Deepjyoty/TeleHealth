package com.gnrc.telehealth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gnrc.telehealth.Adapter.MemberSelectionAdapter;
import com.gnrc.telehealth.DatabaseSqlite.DBhandler;
import com.gnrc.telehealth.Model.AddFamilyModel;
import com.gnrc.telehealth.Model.Family_Head_Model;
import com.gnrc.telehealth.Model.MemberDetailsForDialogModel;

import java.util.ArrayList;

public class MemberSelectionActivity extends AppCompatActivity implements MemberSelectionAdapter.userclicklistener {
    String familyId,resurveyFlag;
    DBhandler dBhandler;
    MemberDetailsForDialogModel memberDetailsForDialogModel;
    MemberSelectionAdapter memberSelectionAdapter;
    RecyclerView recyclerView;
    AddFamilyModel familyModel;
    Button memberSelection;
    ArrayList<String> getCheckedId = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_selection);

        dBhandler = new DBhandler(getApplicationContext());
        //addFamilyModel = new AddFamilyModel();
        familyId = getIntent().getStringExtra("familyId");
        memberSelection = findViewById(R.id.btn_memberSelection);
        recyclerView = findViewById(R.id.rv_memberSelection);
        setupRecyclerAm_DB();
        /*for (int j = 0; j<getCheckedId.size(); j++){
            addFamilyModel.getF_id();
            addFamilyModel.getRegnum();
            addFamilyModelArrayList.add(addFamilyModel);
        }*/
        memberSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getCheckedId.isEmpty()){
                    Toast.makeText(MemberSelectionActivity.this, "Please select at least one member",
                            Toast.LENGTH_SHORT).show();
                }else{
                    Intent i = new Intent(MemberSelectionActivity.this, SurveyActivity.class);
                    i.putStringArrayListExtra("memberList",getCheckedId);
                    i.putExtra("familyId", familyModel.getF_id());
                    i.putExtra("member_id", familyModel.getRegnum());
                    startActivity(i);
                }
            }
        });
    }

    private void setupRecyclerAm_DB(){
        ArrayList<AddFamilyModel> addFamilyArrayList = new ArrayList<>();

        Cursor cursor = dBhandler.getFamilyMemberListWithoutMember(familyId);

        //Family_Head_Model dmodel = new Family_Head_Model();

        if (cursor.moveToFirst()) {
            do {
                familyModel = new AddFamilyModel();
                familyModel.setRegnum(cursor.getString(0));
                familyModel.setRegDt(cursor.getString(1));
                familyModel.setRegStatus(cursor.getString(2));
                familyModel.setPtName(cursor.getString(3));
                familyModel.setGender(cursor.getString(4));
                familyModel.setDob(cursor.getString(5));
                familyModel.setYear(cursor.getString(6));
                familyModel.setMonth(cursor.getString(7));
                familyModel.setDay(cursor.getString(8));
                familyModel.setContact(cursor.getString(9));
                familyModel.setAreaLocality(cursor.getString(10));
                familyModel.setDistCode(cursor.getString(11));
                familyModel.setBlockName(cursor.getString(12));
                familyModel.setPancName(cursor.getString(13));
                familyModel.setVillName(cursor.getString(14));
                familyModel.setCreateDate(cursor.getString(15));
                familyModel.setLoginId(cursor.getString(16));
                familyModel.setUpdDate(cursor.getString(17));
                familyModel.setF_id(cursor.getString(18));

                addFamilyArrayList.add(familyModel);

            }while (cursor.moveToNext());
            memberSelectionAdapter = new MemberSelectionAdapter(this, addFamilyArrayList,this);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false));
            recyclerView.setAdapter(memberSelectionAdapter);

        }

    }
    @Override
    public void memberId(ArrayList<String> checkedId) {
        getCheckedId.clear();
        for (int i = 0; i<checkedId.size(); i++){
            getCheckedId.add(checkedId.get(i));
        }
        //familyHeadModel.getid();
        //startActivity(new Intent(this,DataDetails.class).putExtra("data",familyHeadModel));
    }

    @Override
    public void removeMemberID(ArrayList<String> checkedId) {
        getCheckedId.clear();
        for (int i = 0; i<checkedId.size(); i++){
            getCheckedId.add(checkedId.get(i));
        }
    }

}