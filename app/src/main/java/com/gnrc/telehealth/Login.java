package com.gnrc.telehealth;

import static com.gnrc.telehealth.Family_List_Activity.removeSimpleProgressDialog;
import static com.gnrc.telehealth.Family_List_Activity.showSimpleProgressDialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gnrc.telehealth.DatabaseSqlite.DBhandler;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnFailureListener;
import com.google.android.play.core.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    private AppUpdateManager appUpdateManager;
    private static final int IMMEDIATE_APP_UPDATE_REQ_CODE = 124;
    ProgressDialog pdDialog;
    private DBhandler dBhandler;
    String URL_LOGIN = "https://www.gnrctelehealth.com/telehealth_api/";
    private String URLstring = "https://www.gnrctelehealth.com/telehealth_api/";
    String luser,lpass;
    TextInputLayout username,password;
    Button loginButton;
    String is_signed_in="";
    SharedPreferences mPreferences;
    String sharedprofFile="com.gnrc.telehealth";
    SharedPreferences.Editor preferencesEditor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        //Calling Shared Preferences
        mPreferences=getSharedPreferences(sharedprofFile,MODE_PRIVATE);
        preferencesEditor = mPreferences.edit();
        //Checking for the value stored in shared preference
        is_signed_in = mPreferences.getString("issignedin","false");
        if(is_signed_in.equals("true"))
        {
            fetchingspinnerstate();
            getDistrictListApi();
            Intent i = new Intent(Login.this,NewsFeedActivity.class);
            startActivity(i);

        }
        pdDialog= new ProgressDialog(Login.this);
        pdDialog.setTitle("Login please wait...");
        pdDialog.setCancelable(false);
        mPreferences=getSharedPreferences(sharedprofFile,MODE_PRIVATE);
        preferencesEditor = mPreferences.edit();
        loginButton = (Button) findViewById(R.id.loginbtn);
        username = (TextInputLayout) findViewById(R.id.usernameinp);
        password = (TextInputLayout) findViewById(R.id.passwordinp);
        //Defining the update manager
        appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());
        //Calling the app update method
        check_PlayStore_Update();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
                    // connected to the internet
                    if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                        // connected to wifi
                        luser=username.getEditText().getText().toString().trim();
                        lpass=password.getEditText().getText().toString().trim();
                        if(luser.isEmpty()||lpass.isEmpty())
                        {
                            Toast.makeText(Login.this,"please enter valid data",Toast.LENGTH_SHORT).show();
                        }else {
                            Loginrequest();
                        }
                        Toast.makeText(Login.this, "logged in", Toast.LENGTH_SHORT).show();

                    } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                        // connected to mobile data
                        luser=username.getEditText().getText().toString().trim();
                        lpass=password.getEditText().getText().toString().trim();
                        if(luser.isEmpty()||lpass.isEmpty())
                        {
                            Toast.makeText(Login.this,"please enter valid data",Toast.LENGTH_SHORT).show();
                        }else {
                            Loginrequest();
                        }
                        Toast.makeText(Login.this, "logged in", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Check your Internet connection", Toast.LENGTH_SHORT).show();
                    // not connected to the internet
                }

            }
        });
        //Defining connectivity manager and checking internet status


    }
    //method to check play-store for new version of app
    private void check_PlayStore_Update() {

        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                startUpdateFlow(appUpdateInfo);
            } else if  (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS){
                startUpdateFlow(appUpdateInfo);
            }

        });
        appUpdateInfoTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.d("deep", "onFailure: "+ e.getMessage());
            }
        });
    }

    //method to set the update type for app updates. Here it is set as IMMEDIATE
    private void startUpdateFlow(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, this, Login.IMMEDIATE_APP_UPDATE_REQ_CODE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    //Method to get the status of the update
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMMEDIATE_APP_UPDATE_REQ_CODE) {
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Update canceled by user! Result Code: " + resultCode, Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Update success! Result Code: " + resultCode, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Update Failed! Result Code: " + resultCode, Toast.LENGTH_LONG).show();
                check_PlayStore_Update();
            }
        }
    }

    //Method to login using Volley api while sending the username and password data
    private void Loginrequest()
    {
        fetchingspinnerstate();
        getDistrictListApi();
        Log.d("deep", "getParams: "+username);
        pdDialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("anyText",response);
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            String user_id = jsonObject.getString("user_id");
                            if(status.equals("1")){
                                Toast.makeText(getApplicationContext(),"Logged In  Success",Toast.LENGTH_LONG).show();
                                pdDialog.dismiss();
                                preferencesEditor.putString("issignedin","true");
                                preferencesEditor.putString("status",status);
                                preferencesEditor.putString("message",message);
                                preferencesEditor.putString("user_id",user_id);
                                preferencesEditor.apply();
                                Intent i = new Intent(Login.this,NewsFeedActivity.class);
                                startActivity(i);
                                finish();
                            }
                            if(status.equals("0")){
                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                                pdDialog.dismiss();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"Registration Error !1"+e,Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pdDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Login Error !2"+error,Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("req_type","auth-user");
                params.put("username", luser);
                params.put("password", lpass);
                Log.d("deep", "getParams: "+params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, ChoiceActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void getDistrictListApi() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Family Details");

        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.family_head_custom_alert_dialog,null);
        builder.setView(customLayout);

        showSimpleProgressDialog(this, "Loading...","Fetching Json",false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLstring,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("strrrrr", ">>" + response);
                        try {
                            removeSimpleProgressDialog();

                            JSONArray obj = new JSONArray(response);


                            for (int i = 0; i < obj.length(); i++) {

                                JSONObject dataobj = obj.getJSONObject(i);

                                dBhandler = new DBhandler(getApplicationContext());
                                dBhandler.setDistDb(dataobj.getString("id"),
                                        dataobj.getString("value"));

                            };

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


        showSimpleProgressDialog(this, "Loading...","Fetching Json",false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLstring,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("strrrrr", ">>" + response);
                        try {
                            removeSimpleProgressDialog();

                            JSONArray obj = new JSONArray(response);
                            for (int i = 0; i < obj.length(); i++) {
                                JSONObject dataobj = obj.getJSONObject(i);

                                /*stateModel.setId(dataobj.getString("id"));
                                stateModel.setState(dataobj.getString("value"));*/
                                dBhandler = new DBhandler(getApplicationContext());
                                dBhandler.setStateDb(dataobj.getString("id"),
                                        dataobj.getString("value"));

                            };

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
}