package com.gnrc.telehealth;

import static com.gnrc.telehealth.FamilyHeadActivity.removeSimpleProgressDialog;
import static com.gnrc.telehealth.FamilyHeadActivity.showSimpleProgressDialog;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gnrc.telehealth.Adapter.ResurveyAdapter;
import com.gnrc.telehealth.DatabaseSqlite.DBhandler;
import com.gnrc.telehealth.Model.ResurveyModel;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResurveyPreparation extends AppCompatActivity {

    Button search;
    TextInputLayout searchBox;
    String URLstring = "https://www.gnrctelehealth.com/telehealth_api/index_dev.php";
    ArrayList<ResurveyModel> resurveyModelArrayList;
    ArrayList<ResurveyModel> resurveyModelArrayList2;
    ArrayList<ResurveyModel> resurveyModelArrayList3;
    ArrayList<ResurveyModel> resurveyModelArrayList4;

    ResurveyModel resurveyModel;
    ResurveyModel resurveyModel2;
    ResurveyModel resurveyModel3;
    ResurveyModel resurveyModel4;

    DBhandler dBhandler;
    ResurveyAdapter resurveyAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resurvey_preparation);

        search = findViewById(R.id.btn_searchResurvey);
        searchBox = findViewById(R.id.etfamilyheadphone);
        recyclerView = findViewById(R.id.recyclerResurvey);


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getResurveyData();
            }
        });

    }

    public void getResurveyData(){
        showSimpleProgressDialog(this, "Loading...","Fetching Json",false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLstring,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("strrrrr", ">>" + response);
                        try {

                            removeSimpleProgressDialog();

                            JSONObject obj = new JSONObject(response);

                            resurveyModelArrayList = new ArrayList<>();
                            resurveyModelArrayList2 = new ArrayList<>();
                            resurveyModelArrayList3 = new ArrayList<>();
                            resurveyModelArrayList4 = new ArrayList<>();

                            for (int i = 0; i < obj.length(); i++) {
                                resurveyModel = new ResurveyModel();

                                JSONObject dataobj = obj.getJSONObject("data");
                                JSONObject headObj = dataobj.getJSONObject("head_data");

                                //Retrieving Family head Details
                                resurveyModel.setSSFM_ID(headObj.getString("SSFM_ID"));
                                resurveyModel.setSSFM_HEAD_NAME(headObj.getString("SSFM_HEAD_NAME"));
                                resurveyModel.setSSFM_CONTACT_NO(headObj.getString("SSFM_CONTACT_NO"));
                                resurveyModel.setSSFM_HOUSE_NO(headObj.getString("SSFM_HOUSE_NO"));
                                resurveyModel.setSSFM_ADDR(headObj.getString("SSFM_ADDR"));
                                resurveyModel.setSSFM_GAON_PNCHYT(headObj.getString("SSFM_GAON_PNCHYT"));
                                resurveyModel.setSSFM_BLOCK_CODE(headObj.getString("SSFM_BLOCK_CODE"));
                                resurveyModel.setSSFM_CITY_CODE(headObj.getString("SSFM_CITY_CODE"));
                                resurveyModel.setSSFM_DIST_CODE(headObj.getString("SSFM_DIST_CODE"));
                                resurveyModel.setSSFM_STATE_CODE(headObj.getString("SSFM_STATE_CODE"));
                                resurveyModel.setSSFM_PIN(headObj.getString("SSFM_PIN"));
                                resurveyModelArrayList.add(resurveyModel);

                                //Retrieving Member Details

                                JSONArray memberObj = dataobj.getJSONArray("member_data");
                                for (int j = 0; j < memberObj.length(); j++) {
                                    resurveyModel2 = new ResurveyModel();
                                    JSONObject memberObj2 = memberObj.getJSONObject(j);
                                    resurveyModel2.setSSR_REGN_DATE(memberObj2.getString("SSR_REGN_DATE"));
                                    resurveyModel2.setSSR_REGN_STATUS(memberObj2.getString("SSR_REGN_STATUS"));
                                    resurveyModel2.setSSR_PATIENT_NAME(memberObj2.getString("SSR_PATIENT_NAME"));
                                    resurveyModel2.setSSR_GENDER(memberObj2.getString("SSR_GENDER"));
                                    resurveyModel2.setSSR_DOB(memberObj2.getString("SSR_DOB"));
                                    resurveyModel2.setSSR_AGE_YR(memberObj2.getString("SSR_AGE_YR"));
                                    resurveyModel2.setSSR_AGE_MN(memberObj2.getString("SSR_AGE_MN"));
                                    resurveyModel2.setSSR_AGE_DY(memberObj2.getString("SSR_AGE_DY"));
                                    resurveyModel2.setSSR_CONTACT_NO(memberObj2.getString("SSR_CONTACT_NO"));
                                    resurveyModel2.setSSR_AREA_LOCALITY(memberObj2.getString("SSR_AREA_LOCALITY"));
                                    resurveyModel2.setSSR_REGN_NUM(memberObj2.getString("SSR_REGN_NUM"));
                                    resurveyModel2.setSSR_DISTRICT_CD(memberObj2.getString("SSR_DISTRICT_CD"));
                                    resurveyModel2.setSSR_BLOCK_NAME(memberObj2.getString("SSR_BLOCK_NAME"));
                                    resurveyModel2.setSSR_PANCHAYAT_NAME(memberObj2.getString("SSR_PANCHAYAT_NAME"));
                                    resurveyModel2.setSSR_VILLAGE_NAME(memberObj2.getString("SSR_VILLAGE_NAME"));
                                    resurveyModel2.setSSR_CRT_DT(memberObj2.getString("SSR_CRT_DT"));
                                    resurveyModel2.setSSR_CRT_USER_ID(memberObj2.getString("SSR_CRT_USER_ID"));
                                    resurveyModel2.setSSR_LST_UPD_DT(memberObj2.getString("SSR_LST_UPD_DT"));
                                    resurveyModel2.setFamily_id(memberObj2.getString("family_id"));
                                    resurveyModelArrayList2.add(resurveyModel2);
                                }




                                //Retrieving SymptomDetails

                                JSONArray symptomsObj = dataobj.getJSONArray("symptoms_header");
                                for (int k = 0; k < symptomsObj.length(); k++) {
                                    resurveyModel3 = new ResurveyModel();
                                    JSONObject symptomsObj2 = symptomsObj.getJSONObject(k);
                                    resurveyModel3.setGroup_surveyid(symptomsObj2.getString("group_surveyid"));
                                    resurveyModel3.setMemberId(symptomsObj2.getString("member_id"));
                                    resurveyModel3.setFamily_id(symptomsObj2.getString("family_id"));
                                    resurveyModel3.setMember_name(symptomsObj2.getString("member_name"));
                                    resurveyModel3.setSmoking(symptomsObj2.getString("smoking"));
                                    resurveyModel3.setAlcohol(symptomsObj2.getString("alcohol"));
                                    resurveyModel3.setMemberSurvey_id(symptomsObj2.getString("memberSurvey_id"));
                                    resurveyModel3.setLatitude(symptomsObj2.getString("latitude"));
                                    resurveyModel3.setLongtitude(symptomsObj2.getString("longtitude"));
                                    resurveyModel3.setSys(symptomsObj2.getString("sys"));
                                    resurveyModel3.setDia(symptomsObj2.getString("dia"));
                                    resurveyModel3.setType(symptomsObj2.getString("type"));
                                    resurveyModel3.setValue(symptomsObj2.getString("value"));
                                    resurveyModel3.setAtal_amrit(symptomsObj2.getString("atal_amrit"));
                                    resurveyModel3.setAyushman_bharat(symptomsObj2.getString("ayushman_bharat"));
                                    resurveyModel3.setTelemedicine_booked(symptomsObj2.getString("telemedicine_booked"));
                                    resurveyModel3.setOpd_booked(symptomsObj2.getString("opd_booked"));
                                    resurveyModel3.setAmbulance_booked(symptomsObj2.getString("ambulance_booked"));
                                    //resurveyModel3.setTimestamp(symptomsObj2.getString("timeStamp"));



                                    JSONArray symptomsListObj = symptomsObj2.getJSONArray("symptom");
                                    for (int l = 0; l < symptomsListObj.length(); l++) {
                                        resurveyModel4 = new ResurveyModel();
                                        JSONObject symptomsListObj2 = symptomsListObj.getJSONObject(l);
                                        resurveyModel4.setGroup_surveyid(symptomsListObj2.getString("group_surveyid"));
                                        resurveyModel4.setATR_CODE(symptomsListObj2.getString("ATR_CODE"));
                                        resurveyModel4.setSSR_REGN_NUM(symptomsListObj2.getString("member_id"));
                                        resurveyModel4.setFamily_id(symptomsListObj2.getString("family_id"));
                                        resurveyModel4.setMember_name(symptomsListObj2.getString("member_name"));
                                        resurveyModel4.setPRT_DESC(symptomsListObj2.getString("PRT_DESC"));
                                        resurveyModel4.setATR_DESC(symptomsListObj2.getString("ATR_DESC"));
                                        resurveyModel4.setCheckState(symptomsListObj2.getString("checkState"));
                                        resurveyModel4.setMemberSurvey_id(symptomsListObj2.getString("memberSurvey_id"));
                                        resurveyModelArrayList4.add(resurveyModel4);
                                    }
                                    resurveyModelArrayList3.add(resurveyModel3);
                                }

                                //Retrieving Symptoms list member wise;


                                Log.d("arrayoutput", "onResponse: "+resurveyModelArrayList);

                            }

                            resurveyAdapter = new ResurveyAdapter(ResurveyPreparation.this, resurveyModelArrayList,
                                    resurveyModelArrayList2,resurveyModelArrayList3,resurveyModelArrayList4);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false));
                            recyclerView.setAdapter(resurveyAdapter);
                            //setupRecyclerFrom_DB();

                            // }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })


        {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<String,String>();
                params.put("req_type","pull-survey");
                params.put("search_text",searchBox.getEditText().getText().toString());
                return params;
            }
        };
        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();

    }
}