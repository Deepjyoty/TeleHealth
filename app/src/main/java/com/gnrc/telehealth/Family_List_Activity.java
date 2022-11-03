package com.gnrc.telehealth;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gnrc.telehealth.Adapter.RvAdapter;
import com.gnrc.telehealth.DatabaseSqlite.DBhandler;
import com.gnrc.telehealth.Model.DataModel;
import com.gnrc.telehealth.Model.StateDataModel;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Family_List_Activity extends AppCompatActivity implements  RvAdapter.userclicklistener{
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    Button addfamily;
    private DBhandler dBhandler;
    private String URLstring = "https://www.gnrctelehealth.com/telehealth_api/";
    private static ProgressDialog mProgressDialog;
    ArrayList<DataModel> dataModelArrayList;
    ArrayList<StateDataModel> stateDataModelArrayList;
    ArrayList<StateDataModel> districtDataModelArrayList;




    private RvAdapter rvAdapter;
    private RecyclerView recyclerView;
    TextInputLayout familyhead, phone, house, address, city, pin;
    Spinner dist, state;
    String userid="";
    SharedPreferences mPreferences;
    String sharedprofFile="com.gnrc.telehealth";
    SharedPreferences.Editor preferencesEditor;
    DataModel playerModel;
    StateDataModel stateModel;
    private ArrayList<String> states;
    private ArrayList<String> district ;
    ArrayAdapter<String> spinnerArrayAdapter;
    int spinnerPosition;
    AlertDialog dialog;
    String value,value2;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_list);

        addfamily = findViewById(R.id.addfamily);
        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        recyclerView = findViewById(R.id.recycler);
        mPreferences=getSharedPreferences(sharedprofFile,MODE_PRIVATE);
        preferencesEditor = mPreferences.edit();
        userid = mPreferences.getString("user_id","");


        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                fethingJSON();
                viewAll();
                //setupRecycler();
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to mobile data
                fethingJSON();
                viewAll();

            }
        } else {
            setupRecycler();
            viewAll();
            // not connected to the internet
        }

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Family_List_Activity.this,ChoiceActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu,menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                rvAdapter.getFilter().filter(newText);
                spinnerArrayAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    public void showAlertDialogButtonClicked(View view)
    {

        // Create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Family Details");


        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.family_head_custom_alert_dialog,null);
        builder.setView(customLayout);

        // add a button
        builder.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,int which)
                            {
                                // send data from the
                                // AlertDialog to the Activity
                                familyhead = customLayout.findViewById(R.id.etfamilyhead);
                                phone = customLayout.findViewById(R.id.etfamilyheadphone);
                                house = customLayout.findViewById(R.id.etfamilyheadhouseno);
                                address = customLayout.findViewById(R.id.etfamilyheadaddress);
                                city = customLayout.findViewById(R.id.etfamilyheadcity);
                                //dist = (Spinner) customLayout.findViewById(R.id.spfamilyheaddistrict);
                                //state = (Spinner) customLayout.findViewById(R.id.spfamilyheadstate);
                                pin = customLayout.findViewById(R.id.etfamilyheadpin);

                                storingJSON();


                                Intent i = new Intent(Family_List_Activity.this,Family_List_Activity.class);
                                startActivity(i);
                                finish();
                            }
                        });
        // create and show
        // the alert dialog
        dialog = builder.create();
        dialog.show();
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                fetchingspinnerdistrict();
                fetchingspinnerstate();
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to mobile data
                fetchingspinnerdistrict();
                fetchingspinnerstate();
            }
        } else {
            setupspinner();
            setupspinnerdist();
            // not connected to the internet
        }
    }

    // Do something with the data
    // coming from the AlertDialog
    private void sendDialogDataToActivity(String data)
    {
        Toast.makeText(this,data,Toast.LENGTH_SHORT).show();
    }
    private void fetchingspinnerdistrict() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Family Details");

        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.family_head_custom_alert_dialog,null);
        builder.setView(customLayout);
        dist = (Spinner) dialog.findViewById(R.id.spfamilyheaddistrict);
        state = (Spinner) dialog.findViewById(R.id.spfamilyheadstate);


        showSimpleProgressDialog(this, "Loading...","Fetching Json",false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLstring,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("strrrrr", ">>" + response);
                        try {
                            removeSimpleProgressDialog();

                            JSONArray obj = new JSONArray(response);
                            districtDataModelArrayList = new ArrayList<>();

                            for (int i = 0; i < obj.length(); i++) {
                                stateModel = new StateDataModel();
                                JSONObject dataobj = obj.getJSONObject(i);

                                dBhandler = new DBhandler(getApplicationContext());
                                dBhandler.addspinnerdist(dataobj.getString("id"),
                                        dataobj.getString("value"));

                            };
                            setupspinnerdist();
                            removeSimpleProgressDialog();
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
                params.put("req_type","get-district-list");
                return params;
            }
        };
        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
    private void fetchingspinnerstate() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Family Details");

        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.family_head_custom_alert_dialog,null);
        builder.setView(customLayout);
        dist = (Spinner) dialog.findViewById(R.id.spfamilyheaddistrict);
        state = (Spinner) dialog.findViewById(R.id.spfamilyheadstate);

        showSimpleProgressDialog(this, "Loading...","Fetching Json",false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLstring,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("strrrrr", ">>" + response);
                        try {
                            removeSimpleProgressDialog();

                            JSONArray obj = new JSONArray(response);
                            stateDataModelArrayList = new ArrayList<>();
                            states = new ArrayList<String>();
                            for (int i = 0; i < obj.length(); i++) {
                                stateModel = new StateDataModel();
                                JSONObject dataobj = obj.getJSONObject(i);

                                /*stateModel.setId(dataobj.getString("id"));
                                stateModel.setState(dataobj.getString("value"));*/
                                dBhandler = new DBhandler(getApplicationContext());
                                dBhandler.addspinner(dataobj.getString("id"),
                                        dataobj.getString("value"));

                            };
                            setupspinner();
                            removeSimpleProgressDialog();
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
                params.put("req_type","get-state-list");
                return params;

            }
        };
        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
    public void fethingJSON() {

        showSimpleProgressDialog(this, "Loading...","Fetching Json",false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLstring,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("strrrrr", ">>" + response);
                        try {

                            removeSimpleProgressDialog();

                            JSONArray obj = new JSONArray(response);
                            dataModelArrayList = new ArrayList<>();

                            for (int i = 0; i < obj.length(); i++) {
                                playerModel = new DataModel();
                                JSONObject dataobj = obj.getJSONObject(i);

                                /*playerModel.setId(dataobj.getString("SSFM_ID"));
                                playerModel.setFamilyhead(dataobj.getString("SSFM_HEAD_NAME"));
                                playerModel.setPhone(dataobj.getString("SSFM_CONTACT_NO"));
                                playerModel.setHouse(dataobj.getString("SSFM_HOUSE_NO"));
                                playerModel.setAddress(dataobj.getString("SSFM_ADDR"));
                                playerModel.setGaon_panchayat(dataobj.getString("SSFM_GAON_PNCHYT"));
                                playerModel.setBlock_code(dataobj.getString("SSFM_BLOCK_CODE"));
                                playerModel.setCity(dataobj.getString("SSFM_CITY_CODE"));
                                playerModel.setDist(dataobj.getString("SSFM_DIST_CODE"));
                                playerModel.setState(dataobj.getString("SSFM_STATE_CODE"));
                                playerModel.setPin(dataobj.getString("SSFM_PIN"));
                                playerModel.setViewtext("View");
                                playerModel.setEdittext("EditFamily");

                                dataModelArrayList.add(playerModel);*/
                                dBhandler = new DBhandler(getApplicationContext());
                                dBhandler.addnewprod(dataobj.getString("SSFM_ID"),
                                        dataobj.getString("SSFM_HEAD_NAME"),
                                        dataobj.getString("SSFM_CONTACT_NO"),
                                        dataobj.getString("SSFM_HOUSE_NO"),
                                        dataobj.getString("SSFM_ADDR"),
                                        dataobj.getString("SSFM_GAON_PNCHYT"),
                                        dataobj.getString("SSFM_BLOCK_CODE"),
                                        dataobj.getString("SSFM_CITY_CODE"),
                                        dataobj.getString("SSFM_DIST_CODE"),
                                        dataobj.getString("SSFM_STATE_CODE"),
                                        dataobj.getString("SSFM_PIN"));
                                playerModel.setViewtext("View");
                                playerModel.setEdittext("EditFamily");
                            }

                            setupRecycler();

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
                params.put("req_type","get-family");
                params.put("family-id","0");
                return params;
            }
        };
        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void storingJSON() {

        showSimpleProgressDialog(this, "Loading...","Saving",false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLstring,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("strrrrr", ">>" + response);
                        try {
                            removeSimpleProgressDialog();

                            JSONObject obj = new JSONObject(response);
                            Log.d("deep", ">>" + response);

                            String message = obj.getString("message");
                            Toast.makeText(Family_List_Activity.this, ""+ message, Toast.LENGTH_SHORT).show();
                            dataModelArrayList = new ArrayList<>();
                            JSONArray dataArray  = obj.getJSONArray("products");

                            for (int i = 0; i < dataArray.length(); i++) {
                                DataModel playerModel = new DataModel();
                                JSONObject dataobj = dataArray.getJSONObject(i);

                                /*playerModel.setId(dataobj.getInt("id"));
                                playerModel.setTitle(dataobj.getString("title"));
                                playerModel.setBrand(dataobj.getString("brand"));
                                playerModel.setPrice(dataobj.getInt("price"));
                                playerModel.setThumbnail(dataobj.getString("thumbnail"));
                                playerModel.setDescription(dataobj.getString("description"));*/

                                dataModelArrayList.add(playerModel);

                            }

                            setupRecycler();
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
                        //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })

        {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<String,String>();
                params.put("req_type","create-family");
                params.put("family-id","0");
                params.put("family-head-name", familyhead.getEditText().getText().toString());
                params.put("contact-no", phone.getEditText().getText().toString());
                params.put("house-no", house.getEditText().getText().toString());
                params.put("address", address.getEditText().getText().toString());
                params.put("gaon-panchayat-code", "10002");
                params.put("block-code", "3");
                params.put("city-code", city.getEditText().getText().toString());
                params.put("dist-code",value);
                params.put("state-code",value2);
                params.put("pin-code", pin.getEditText().getText().toString());
                params.put("user-id", userid);
                Log.d("deep", "getParams: "+params);
                return params;

            }
        };
        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void setupspinner(){
        state = (Spinner) dialog.findViewById(R.id.spfamilyheadstate);
        dBhandler = new DBhandler(getApplicationContext());
        Cursor cursor = dBhandler.getspinnerdata();
        states = new ArrayList<String>();
        stateDataModelArrayList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                stateModel = new StateDataModel();
                stateModel.setId(cursor.getString(0));
                stateModel.setState(cursor.getString(1));
                stateDataModelArrayList.add(stateModel);

            } while (cursor.moveToNext());
            spinnerArrayAdapter = new ArrayAdapter<String>(Family_List_Activity.this, android.R.layout.simple_spinner_item, states);
            for (int i = 0; i < stateDataModelArrayList.size(); i++) {
                states.add(stateDataModelArrayList.get(i).getState());
                spinnerPosition = spinnerArrayAdapter.getPosition(stateDataModelArrayList.get(i).getState());
            }
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
            state.setAdapter(spinnerArrayAdapter);
            state.setSelection(spinnerPosition);
            state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    value2 = adapterView.getItemAtPosition(position).toString();

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
    }
    private void setupspinnerdist(){
        dist = (Spinner) dialog.findViewById(R.id.spfamilyheaddistrict);
        dBhandler = new DBhandler(getApplicationContext());
        Cursor cursor = dBhandler.getspinnerdatadist();
        district = new ArrayList<String>();
        districtDataModelArrayList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                stateModel = new StateDataModel();
                stateModel.setId(cursor.getString(0));
                stateModel.setDistrict(cursor.getString(1));
                districtDataModelArrayList.add(stateModel);

            } while (cursor.moveToNext());
            spinnerArrayAdapter = new ArrayAdapter<String>(Family_List_Activity.this, android.R.layout.simple_spinner_item, district);
            for (int i = 0; i < districtDataModelArrayList.size(); i++) {
                district.add(districtDataModelArrayList.get(i).getDistrict());
                spinnerPosition = spinnerArrayAdapter.getPosition(districtDataModelArrayList.get(i).getDistrict());
            }
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
            dist.setAdapter(spinnerArrayAdapter);
            dist.setSelection(spinnerPosition);
            dist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    value = adapterView.getItemAtPosition(position).toString();

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
    }
    private void setupRecycler(){
        dataModelArrayList = new ArrayList<>();
        dBhandler = new DBhandler(getApplicationContext());
        Cursor cursor = dBhandler.getAllData();
        //DataModel dmodel = new DataModel();

        if (cursor.moveToFirst()) {
            do {
                playerModel = new DataModel();
                playerModel.setId(cursor.getString(0));
                playerModel.setFamilyhead(cursor.getString(1));
                playerModel.setPhone(cursor.getString(2));
                playerModel.setHouse(cursor.getString(3));
                playerModel.setAddress(cursor.getString(4));
                playerModel.setGaon_panchayat(cursor.getString(5));
                playerModel.setBlock_code(cursor.getString(6));
                playerModel.setCity(cursor.getString(7));
                playerModel.setDist(cursor.getString(8));
                playerModel.setState(cursor.getString(9));
                playerModel.setPin(cursor.getString(10));
                dataModelArrayList.add(playerModel);
                playerModel.setViewtext("View");
                playerModel.setEdittext("EditFamily");
            }while (cursor.moveToNext());
            rvAdapter = new RvAdapter(this,dataModelArrayList,this::selecteduser);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false));
            recyclerView.setAdapter(rvAdapter);

        }

    }

    public static void removeSimpleProgressDialog() {
        try {
            if (mProgressDialog != null) {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                }
            }
        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();

        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void showSimpleProgressDialog(Context context, String title,
                                                String msg, boolean isCancelable) {
        try {
            if (mProgressDialog == null) {
                mProgressDialog = ProgressDialog.show(context, title, msg);
                mProgressDialog.setCancelable(isCancelable);
            }

            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }

        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();
        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void selecteduser(DataModel dataModel) {
        //dataModel.getid();
        //startActivity(new Intent(this,DataDetails.class).putExtra("data",dataModel));
    }
    public void viewAll() {

        addfamily.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showAlertDialogButtonClicked(v);
/*                        dBhandler = new DBhandler(getApplicationContext());
                        Cursor res = dBhandler.getAllData();
                        if(res.getCount() == 0) {
                            // show message
                            showMessage("Error","Nothing found");
                            return;
                        }

                        StringBuffer buffer = new StringBuffer();
                        while (res.moveToNext()) {
                            buffer.append("Id :"+ res.getString(0)+"\n");
                            buffer.append("Family Head :"+ res.getString(1)+"\n");
                            buffer.append("Phone :"+ res.getString(2)+"\n");
                            buffer.append("House :"+ res.getString(3)+"\n");
                            buffer.append("Address :"+ res.getString(4)+"\n");
                            buffer.append("GaonPanchayat :"+ res.getString(5)+"\n");
                            buffer.append("BlockCode :"+ res.getString(6)+"\n");
                            buffer.append("City :"+ res.getString(7)+"\n");
                            buffer.append("District :"+ res.getString(8)+"\n");
                            buffer.append("State :"+ res.getString(9)+"\n");
                            buffer.append("Pin :"+ res.getString(10)+"\n");

                        }

                        // Show all data
                        showMessage("Data",buffer.toString());*/
                    }
                }
        );
    }

    public void showMessage(String title,String Message){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

}