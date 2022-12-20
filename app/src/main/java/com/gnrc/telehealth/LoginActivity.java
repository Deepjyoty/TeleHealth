package com.gnrc.telehealth;

import static com.gnrc.telehealth.FamilyHeadActivity.removeSimpleProgressDialog;
import static com.gnrc.telehealth.FamilyHeadActivity.showSimpleProgressDialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
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
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class LoginActivity extends AppCompatActivity {
    private AppUpdateManager appUpdateManager;
    private static final int IMMEDIATE_APP_UPDATE_REQ_CODE = 124;
    ProgressDialog pdDialog;
    private DBhandler dBhandler;
    String URL_LOGIN = "https://www.gnrctelehealth.com/telehealth_api/index_dev.php";
    private String URLstring = "https://www.gnrctelehealth.com/telehealth_api/index_dev.php";
    String luser,lpass;
    TextInputLayout username,password;
    Button loginButton;
    String is_signed_in="";
    SharedPreferences mPreferences;
    String sharedprofFile="com.gnrc.telehealth";
    SharedPreferences.Editor preferencesEditor;

    int num = 1000;
    TreeMap<String, ArrayList<String>> multiMap;
    ArrayList<String> mapObject;

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
            fetchingSymptoms();
            fetchingspinnerstate();
            getDistrictListApi();

            Intent i = new Intent(LoginActivity.this,NewsFeedActivity.class);
            startActivity(i);

        }
        pdDialog= new ProgressDialog(LoginActivity.this);
        pdDialog.setTitle("LoginActivity please wait...");
        pdDialog.setCancelable(false);
/*        mPreferences=getSharedPreferences(sharedprofFile,MODE_PRIVATE);
        preferencesEditor = mPreferences.edit();*/
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
                            Toast.makeText(LoginActivity.this,"please enter valid data",Toast.LENGTH_SHORT).show();
                        }else {
                            Loginrequest();
                        }
                        Toast.makeText(LoginActivity.this, "logged in", Toast.LENGTH_SHORT).show();

                    } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                        // connected to mobile data
                        luser=username.getEditText().getText().toString().trim();
                        lpass=password.getEditText().getText().toString().trim();
                        if(luser.isEmpty()||lpass.isEmpty())
                        {
                            Toast.makeText(LoginActivity.this,"please enter valid data",Toast.LENGTH_SHORT).show();
                        }else {
                            Loginrequest();
                        }
                        Toast.makeText(LoginActivity.this, "logged in", Toast.LENGTH_SHORT).show();
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
            appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, this, LoginActivity.IMMEDIATE_APP_UPDATE_REQ_CODE);
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
        fetchingSymptoms();
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
                                insertToSP(multiMap);
                                Intent i = new Intent(LoginActivity.this,NewsFeedActivity.class);
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
                Toast.makeText(getApplicationContext(),"LoginActivity Error !2"+error,Toast.LENGTH_LONG).show();
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

    private void fetchingSymptoms() {

        //showSimpleProgressDialog(this, "Loading...","Saving",false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLstring,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("strrrrr", ">>" + response);
                        multiMap = new TreeMap<>();
                        try {
                            removeSimpleProgressDialog();

                            JSONObject obj = new JSONObject(response);


                            Log.d("deep", ">>" + response);

                            String message = obj.getString("status");
                            Toast.makeText(LoginActivity.this, ""+ message, Toast.LENGTH_SHORT).show();
                            JSONArray jsonObject = obj.getJSONArray("data");

                            for (int i = 0; i < jsonObject.length(); i++) {

                                mapObject = new ArrayList<>();
                                JSONObject jsondata = jsonObject.getJSONObject(i);
                                DBhandler dBhandler = new DBhandler(getApplicationContext());
                                dBhandler.addSymptomsMaster(jsondata.getString("ATR_CODE"),
                                        jsondata.getString("PRT_CODE"),
                                        jsondata.getString("PRT_DESC"),
                                        jsondata.getString("PRT_DESC_ALT"),
                                        jsondata.getString("PRT_DESC_BENG"),
                                        jsondata.getString("PRT_SLNO"),
                                        jsondata.getString("ATR_DESC"),
                                        jsondata.getString("ATR_DESC_ALT"),
                                        jsondata.getString("ATR_DESC_BENG"),
                                        jsondata.getString("ATR_SLNO"),
                                        jsondata.getString("IMAGE_URL"));

                                if (!jsondata.getString("IMAGE_URL").equals("")){
                                    Picasso.get().load(jsondata.getString("IMAGE_URL"))
                                            .into(new Target() {
                                                      @Override
                                                      public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                                          try {
                                                              String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
                                                              File myDir = new File(root + "/symptom_image");

                                                              if (!myDir.exists()) {
                                                                  myDir.mkdirs();
                                                              }

                                                              String name = ++num + ".jpg";
                                                              myDir = new File(myDir, name);
                                                              FileOutputStream out = new FileOutputStream(myDir);
                                                              bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

                                                              out.flush();
                                                              out.close();
                                                          } catch(Exception e){
                                                              // some action
                                                          }
                                                      }
                                                      @Override
                                                      public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                                                      }

                                                      @Override
                                                      public void onPrepareLoad(Drawable placeHolderDrawable) {
                                                      }
                                                  }
                                            );
                                }


                                mapObject.add(jsondata.getString("ATR_CODE"));
                                mapObject.add(jsondata.getString("PRT_DESC"));
                                mapObject.add(jsondata.getString("PRT_DESC_ALT"));
                                mapObject.add(jsondata.getString("PRT_DESC_BENG"));
                                mapObject.add(jsondata.getString("PRT_SLNO"));
                                mapObject.add(jsondata.getString("ATR_DESC"));
                                mapObject.add(jsondata.getString("ATR_DESC_ALT"));
                                mapObject.add(jsondata.getString("ATR_DESC_BENG"));
                                mapObject.add(jsondata.getString("ATR_SLNO"));

                                multiMap.put(jsondata.getString("PRT_CODE"),mapObject);
                            }
                            insertToSP(multiMap);

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
                DBhandler dBhandler = new DBhandler(getApplicationContext());
                Cursor cursor = dBhandler.getFamilyDbData();
                //Family_Head_Model dmodel = new Family_Head_Model();
                Map<String,String> params = new HashMap<String,String>();
                params.put("req_type","get-symptoms-list");
                Log.d("deep", "getParams: "+params);
                return params;

            }
        };
        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    private void insertToSP(TreeMap<String, ArrayList<String>> jsonMap) {
        String jsonString = new Gson().toJson(jsonMap);
        SharedPreferences sharedPreferences = getSharedPreferences("multimap", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("map", jsonString);
        editor.apply();
    }
}