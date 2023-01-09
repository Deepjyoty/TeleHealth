package com.gnrc.telehealth;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gnrc.telehealth.Adapter.Family_Head_Adapter;
import com.gnrc.telehealth.DatabaseSqlite.DBhandler;
import com.gnrc.telehealth.Model.Family_Head_Model;
import com.gnrc.telehealth.Model.StateDataModel;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class FamilyHeadActivity extends AppCompatActivity implements  Family_Head_Adapter.userclicklistener{
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    private DBhandler dBhandler;
    private String URLstring = "https://www.gnrctelehealth.com/telehealth_api/index_dev.php";
    private static ProgressDialog mProgressDialog;
    private ArrayList<String> states;
    private ArrayList<String> district ;
    private Family_Head_Adapter familyHeadAdapter;
    private RecyclerView recyclerView;
    private TextInputLayout familyhead, phone, house, address, city, pin;
    private Spinner dist, state;
    int spinnerPosition;

    String userid="";
    SharedPreferences mPreferences;
    String sharedprofFile="com.gnrc.telehealth";
    SharedPreferences.Editor preferencesEditor;
    Family_Head_Model playerModel;
    StateDataModel stateModel;
    ArrayList<Family_Head_Model> familyHeadModelArrayList;
    ArrayList<StateDataModel> stateDataModelArrayList;
    ArrayList<StateDataModel> districtDataModelArrayList;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyyhhmmss", Locale.CHINESE);
    String format;
    Button addfamilymember;
    ArrayAdapter<String> spinnerArrayAdapter;
    AlertDialog dialog;
    String value,value2;
    ConstraintLayout constraintLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_list);
        setTitle("Family Head");
        constraintLayout = findViewById(R.id.cl_noData);
        addfamilymember = findViewById(R.id.add_family_head);
        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        recyclerView = findViewById(R.id.recycler);
        mPreferences=getSharedPreferences(sharedprofFile,MODE_PRIVATE);
        preferencesEditor = mPreferences.edit();
        userid = mPreferences.getString("user_id","");

        //Handling navigation drawer item clicks
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.nav_settings){
                    Intent newIntent = new Intent(FamilyHeadActivity.this, ShowSurveyActivity.class);
                    startActivity(newIntent);
                }
                else if (id == R.id.nav_logout){
                    AlertDialog.Builder builder = new AlertDialog.Builder(FamilyHeadActivity.this);
                    builder.setTitle("LOGOUT");
                    builder.setMessage("Are you sure you want to logout?");


                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    preferencesEditor.putString("issignedin","false");
                                    preferencesEditor.apply();
                                    Toast.makeText(FamilyHeadActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(FamilyHeadActivity.this,LoginActivity.class);
                                    startActivity(i);
                                }
                            });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.create();
                    builder.show();

                    /*finish();
                    (FamilyHeadActivity.this).finishAffinity();
                    System.exit(0);*/
                }
                return true;
            }
        });

        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                //fethingJSON();

                viewAll();
                setupRecyclerFrom_DB();
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to mobile data
                //fethingJSON();
                viewAll();
                setupRecyclerFrom_DB();

            }
        } else {
            setupRecyclerFrom_DB();
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
        Intent i = new Intent(this, NewsFeedActivity.class);
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
                familyHeadAdapter.getFilter().filter(newText);
                //spinnerArrayAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    public void showAlertDialogButtonClicked()
    {

        // Create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Family Details");


        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.family_head_custom_alert_dialog,null);
        builder.setView(customLayout);
        familyhead = customLayout.findViewById(R.id.etfamilyhead);
        phone = customLayout.findViewById(R.id.etfamilyheadphone);
        house = customLayout.findViewById(R.id.etfamilyheadhouseno);
        address = customLayout.findViewById(R.id.etfamilyheadaddress);
        city = customLayout.findViewById(R.id.etfamilyheadcity);
        pin = customLayout.findViewById(R.id.etfamilyheadpin);
        SharedPreferences mPreferences=getSharedPreferences("language",MODE_PRIVATE);
        preferencesEditor = mPreferences.edit();
        //Checking for the value stored in shared preference
        RadioGroup radioGroup = customLayout.findViewById(R.id.rg_language1);
        RadioButton radioButton = customLayout.findViewById(R.id.rb_english1);
        radioButton.setChecked(true);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = customLayout.findViewById(checkedId);
                if (radioButton.getText().equals("Assamese")){
                    familyhead.setHint("নাম");
                    phone.setHint("যোগাযোগ নং");
                    house.setHint("ঘৰৰ নম্বৰ");
                    address.setHint("ঠিকনা");
                    city.setHint("চহৰ");
                    pin.setHint("পিন");
                    preferencesEditor.putString("lang","Assamese");
                    preferencesEditor.apply();

                }else if (radioButton.getText().equals("Bengali")){
                    familyhead.setHint("নাম");
                    phone.setHint("যযোগাযোগের নম্বর");
                    house.setHint("বাড়ির নম্বর");
                    address.setHint("ঠিকানা");
                    city.setHint("শহর");
                    pin.setHint("পিন");
                    preferencesEditor.putString("lang","Bengali");
                    preferencesEditor.apply();

                }else {
                    familyhead.setHint("Head Name");
                    phone.setHint("Phone");
                    house.setHint("House No");
                    address.setHint("Address");
                    city.setHint("City");
                    pin.setHint("Pin");
                    preferencesEditor.putString("lang","English");
                    preferencesEditor.apply();
                }
            }
        });
        // add a button
        builder.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,int which)
                            {
                            }
                        });
        // create and show
        // the alert dialog
        dialog = builder.create();
        dialog.show();
        //Custom click listener for validation
        Button theButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        theButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (familyhead.getEditText().getText().length()<=2){
                    familyhead.setError("Please Insert Name with at least 3 letters");
                    familyhead.clearFocus();
                    familyhead.requestFocus();
                }
                else if (phone.getEditText().getText().length() != 10){
                    phone.setError("Phone number should be 10 digits");
                    phone.clearFocus();
                    phone.requestFocus();
                }
                else if (house.getEditText().getText().length() < 1){
                    house.setError("Please enter valid house number");
                    house.clearFocus();
                    house.requestFocus();
                }
                else if (address.getEditText().getText().length() < 2){
                    address.setError("Address should be greater than 2 letters");
                    address.clearFocus();
                    address.requestFocus();
                }
                else if (city.getEditText().getText().length() < 2){
                    city.setError("City should have at least 3 letters");
                    city.clearFocus();
                    city.requestFocus();
                }
                else if (state.getSelectedItemPosition() == 0){
                    Toast.makeText(FamilyHeadActivity.this,
                            "Please select appropriate state", Toast.LENGTH_SHORT).show();

                }
                else if (dist.getSelectedItemPosition() == 0){
                    Toast.makeText(FamilyHeadActivity.this,
                            "Please select appropriate district", Toast.LENGTH_SHORT).show();

                }
                else if (pin.getEditText().getText().length()!=6){
                    pin.setError("Pin should be 6 digits");
                    pin.clearFocus();
                    pin.requestFocus();
                }else {

                    adddatatodatabase();
                    setupRecyclerFrom_DB();
                    familyHeadAdapter.notifyDataSetChanged();
                    constraintLayout.setVisibility(View.GONE);
                    dialog.dismiss();

                }
            }
        });
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                setupspinner();
                setupspinnerdist();
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to mobile data
                setupspinner();
                setupspinnerdist();
            }
        } else {
            setupspinner();
            setupspinnerdist();
            // not connected to the internet
        }
    }

    public void viewAll() {

        addfamilymember.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showAlertDialogButtonClicked();
                    }
                }
        );
    }

    private void adddatatodatabase(){
        format = simpleDateFormat.format(new Date());
        dBhandler = new DBhandler(getApplicationContext());
        dBhandler.addFamily_head_db("FH" + phone.getEditText().getText().toString()+format,
                familyhead.getEditText().getText().toString(),
                phone.getEditText().getText().toString(),
                house.getEditText().getText().toString(),
                address.getEditText().getText().toString(),
                "10002",
                "3",
                city.getEditText().getText().toString(),
                value,
                value2,
                pin.getEditText().getText().toString(),
                mPreferences.getString("user_id",""));
    }

    private void setupspinner(){
        state = (Spinner) dialog.findViewById(R.id.spfamilyheadstate);
        dBhandler = new DBhandler(getApplicationContext());
        Cursor cursor = dBhandler.getStateDbData();
        states = new ArrayList<String>();
        stateDataModelArrayList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                stateModel = new StateDataModel();
                stateModel.setId(cursor.getString(0));
                stateModel.setState(cursor.getString(1));
                stateDataModelArrayList.add(stateModel);

            } while (cursor.moveToNext());

            spinnerArrayAdapter = new ArrayAdapter<String>
                    (FamilyHeadActivity.this, android.R.layout.simple_spinner_item, states);

            for (int i = 0; i < stateDataModelArrayList.size(); i++) {
                states.add(stateDataModelArrayList.get(i).getState());
                spinnerPosition = spinnerArrayAdapter.getPosition(stateDataModelArrayList.get(0).getState());
            }

            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
            state.setAdapter(spinnerArrayAdapter);
            state.setSelection(spinnerPosition);
            state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    if (position == 0){
                        Toast.makeText(FamilyHeadActivity.this,
                                "Please select appropriate data", Toast.LENGTH_SHORT).show();
                    }else{
                        value2 = adapterView.getItemAtPosition(position).toString();
                    }

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
        Cursor cursor = dBhandler.getDistDbData();
        district = new ArrayList<String>();
        districtDataModelArrayList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                stateModel = new StateDataModel();
                stateModel.setId(cursor.getString(0));
                stateModel.setDistrict(cursor.getString(1));
                districtDataModelArrayList.add(stateModel);

            } while (cursor.moveToNext());
            spinnerArrayAdapter = new ArrayAdapter<String>(FamilyHeadActivity.this, android.R.layout.simple_spinner_item, district);
            for (int i = 0; i < districtDataModelArrayList.size(); i++) {
                district.add(districtDataModelArrayList.get(i).getDistrict());
                spinnerPosition = spinnerArrayAdapter.getPosition(districtDataModelArrayList.get(0).getDistrict());
            }
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
            dist.setAdapter(spinnerArrayAdapter);
            dist.setSelection(spinnerPosition);
            dist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    if (position == 0){
                        Toast.makeText(FamilyHeadActivity.this,
                                "Please select appropriate data", Toast.LENGTH_SHORT).show();
                    }else{
                        value = adapterView.getItemAtPosition(position).toString();
                    }


                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
    }

    private void setupRecyclerFrom_DB(){
        familyHeadModelArrayList = new ArrayList<>();
        dBhandler = new DBhandler(getApplicationContext());
        Cursor cursor = dBhandler.getFamilyDbData();
        Cursor cursor1 = dBhandler.getSurveyTypeFlag();
        //Family_Head_Model dmodel = new Family_Head_Model();
        format = simpleDateFormat.format(new Date());
        if (cursor.getCount()>0){
            if (cursor.moveToFirst()) {
                do {
                    playerModel = new Family_Head_Model();
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
                    familyHeadModelArrayList.add(playerModel);
                    Log.d("val", "setupRecycler: " + playerModel);
                    playerModel.setViewtext("Add Member");

                    if (cursor1.getCount()>0){
                        if (cursor1.moveToFirst()){
                            do {
                                if (cursor1.getString(1).equals(cursor.getString(0))){
                                    playerModel.setEdittext("Re-Survey");
                                }else {
                                    playerModel.setEdittext("Survey");
                                }
                            }while (cursor1.moveToNext());
                        }
                    }else{
                        playerModel.setEdittext("Survey");
                    }


                }while (cursor.moveToNext());
                familyHeadAdapter = new Family_Head_Adapter(this, familyHeadModelArrayList,this::selecteduser);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false));
                recyclerView.setAdapter(familyHeadAdapter);

            }

        }else{
            constraintLayout.setVisibility(View.VISIBLE);
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
    public void selecteduser(Family_Head_Model familyHeadModel) {
        //familyHeadModel.getid();
        //startActivity(new Intent(this,DataDetails.class).putExtra("data",familyHeadModel));
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