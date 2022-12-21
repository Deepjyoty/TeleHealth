package com.gnrc.telehealth;

import static com.gnrc.telehealth.SurveyActivity.removeSimpleProgressDialog;
import static com.gnrc.telehealth.SurveyActivity.showSimpleProgressDialog;
import static com.zipow.videobox.confapp.ConfMgr.getApplicationContext;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gnrc.telehealth.Adapter.ShowSurveyAdapter;
import com.gnrc.telehealth.DatabaseSqlite.DBhandler;
import com.gnrc.telehealth.Model.MemberDetailsForDialogModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SendDataToServer {
    private static final int PERMISSION_REQUEST_CODE = 100;
    private Context context;
    public RequestQueue rQueue;
    public   String URLstring = "https://www.gnrctelehealth.com/telehealth_api/index_dev.php";
    String groupSurveyId;

    DBhandler dBhandler2 = new DBhandler(getApplicationContext());

    public SendDataToServer(Context context) {
        this.context = context;
    }

    public void saveDataToServer(String surveyId, String familyId, String memberList) {
        int count = 0;
        Log.d("rabbit", "onClick: This was executed Third");
        showSimpleProgressDialog(context, "Loading...","Saving",false);
        //JSONArray abc = new JSONArray();
        JSONObject js = new JSONObject();
        ArrayList<String> memberID = new ArrayList<>();


        Log.d("rabbit", "onClick: This was executed Fourth");
        try {
            JSONObject jsonobject = new JSONObject();
            JSONObject jsonobjectHeadDetails = new JSONObject();

            JSONObject jsonobjectMemberData;
            JSONArray memberArray = new JSONArray();

            JSONObject jsonobjectSymptomsData;

            JSONArray symptomArray1;

            JSONObject jsonobjectSymptoms;

            JSONObject storingSymptoms = new JSONObject();

            JSONArray symptomsArray = new JSONArray();

            Log.d("rabbit", "onClick: This was executed Fifth");

            DBhandler dBhandler = new DBhandler(context);

            Cursor cursor = dBhandler.getFamilyDbData();

            if (cursor.moveToFirst()){
                do {
                    if (cursor.getString(0).equals(familyId)) {
                        jsonobjectHeadDetails.put(cursor.getColumnName(1), cursor.getString(1));
                        jsonobjectHeadDetails.put(cursor.getColumnName(2), cursor.getString(2));
                        jsonobjectHeadDetails.put(cursor.getColumnName(3), cursor.getString(3));
                        jsonobjectHeadDetails.put(cursor.getColumnName(4), cursor.getString(4));
                        jsonobjectHeadDetails.put(cursor.getColumnName(5), cursor.getString(5));
                        jsonobjectHeadDetails.put(cursor.getColumnName(6), cursor.getString(6));
                        jsonobjectHeadDetails.put(cursor.getColumnName(7), cursor.getString(7));
                        jsonobjectHeadDetails.put(cursor.getColumnName(8), cursor.getString(8));
                        jsonobjectHeadDetails.put(cursor.getColumnName(9), cursor.getString(9));
                        jsonobjectHeadDetails.put(cursor.getColumnName(10), cursor.getString(10));
                        jsonobjectHeadDetails.put(cursor.getColumnName(0), cursor.getString(0));

                    }
                } while (cursor.moveToNext());
            }
            Log.d("rabbit", "onClick: This was executed Sixth");
            Cursor cursor2 = dBhandler.getFamilyMemberListWithoutMember(familyId);

            if (cursor2.moveToFirst()){
                do {
                    jsonobjectMemberData = new JSONObject();

                    jsonobjectMemberData.put(cursor2.getColumnName(1), cursor2.getString(1));
                    jsonobjectMemberData.put(cursor2.getColumnName(2), cursor2.getString(2));
                    jsonobjectMemberData.put(cursor2.getColumnName(3), cursor2.getString(3));
                    jsonobjectMemberData.put(cursor2.getColumnName(4), cursor2.getString(4));
                    jsonobjectMemberData.put(cursor2.getColumnName(5), cursor2.getString(5));
                    jsonobjectMemberData.put(cursor2.getColumnName(6), cursor2.getString(6));
                    jsonobjectMemberData.put(cursor2.getColumnName(7), cursor2.getString(7));
                    jsonobjectMemberData.put(cursor2.getColumnName(8), cursor2.getString(8));
                    jsonobjectMemberData.put(cursor2.getColumnName(9), cursor2.getString(9));
                    jsonobjectMemberData.put(cursor2.getColumnName(10), cursor2.getString(10));
                    jsonobjectMemberData.put(cursor2.getColumnName(0), cursor2.getString(0));
                    jsonobjectMemberData.put(cursor2.getColumnName(11), cursor2.getString(11));
                    jsonobjectMemberData.put(cursor2.getColumnName(12), cursor2.getString(12));
                    jsonobjectMemberData.put(cursor2.getColumnName(13), cursor2.getString(13));
                    jsonobjectMemberData.put(cursor2.getColumnName(14), cursor2.getString(14));
                    jsonobjectMemberData.put(cursor2.getColumnName(14), cursor2.getString(14));
                    jsonobjectMemberData.put(cursor2.getColumnName(15), cursor2.getString(15));
                    jsonobjectMemberData.put(cursor2.getColumnName(16), cursor2.getString(16));
                    jsonobjectMemberData.put(cursor2.getColumnName(17), cursor2.getString(17));
                    jsonobjectMemberData.put(cursor2.getColumnName(18), cursor2.getString(18));

                    memberArray.put(jsonobjectMemberData);
                    memberID.add(cursor2.getString(0));
                } while (cursor2.moveToNext());
            }

            Cursor cursor3 = dBhandler.getGeneralHabitsAlcohol(surveyId,memberList);
            Cursor cursor4 = dBhandler.getTestFindings(surveyId,memberList);
            Cursor cursor5 = dBhandler.getHCIAtalAmrit(surveyId,memberList);
            Cursor cursor9 = dBhandler.getOtherInfo(surveyId,memberList);
            for (int i = 0; i<memberID.size();i++){

                //General Habits
                Cursor cursor6 = dBhandler.getSymptomsMember(memberID.get(i),memberList);
                jsonobjectSymptomsData = new JSONObject();
                if (cursor3.moveToFirst()){
                    do {
                        if (cursor3.getString(1).equals(memberID.get(i))){
                            jsonobjectSymptomsData.put(cursor3.getColumnName(0), cursor3.getString(0));
                            jsonobjectSymptomsData.put(cursor3.getColumnName(1), cursor3.getString(1));
                            jsonobjectSymptomsData.put(cursor3.getColumnName(2), cursor3.getString(2));
                            jsonobjectSymptomsData.put(cursor3.getColumnName(3), cursor3.getString(3));
                            jsonobjectSymptomsData.put(cursor3.getColumnName(4), cursor3.getString(4));
                            jsonobjectSymptomsData.put(cursor3.getColumnName(5), cursor3.getString(5));
                            jsonobjectSymptomsData.put(cursor3.getColumnName(6), cursor3.getString(6));
                            jsonobjectSymptomsData.put(cursor3.getColumnName(7), cursor3.getString(7));
                            jsonobjectSymptomsData.put(cursor3.getColumnName(8), cursor3.getString(8));
                        }

                    }while (cursor3.moveToNext());
                }

                if (cursor4.moveToFirst()){
                    do {
                        if (cursor4.getString(1).equals(memberID.get(i))){
                            jsonobjectSymptomsData.put(cursor4.getColumnName(0), cursor4.getString(0));
                            jsonobjectSymptomsData.put(cursor4.getColumnName(1), cursor4.getString(1));
                            jsonobjectSymptomsData.put(cursor4.getColumnName(2), cursor4.getString(2));
                            jsonobjectSymptomsData.put(cursor4.getColumnName(3), cursor4.getString(3));
                            jsonobjectSymptomsData.put(cursor4.getColumnName(4), cursor4.getString(4));
                            jsonobjectSymptomsData.put(cursor4.getColumnName(5), cursor4.getString(5));
                            jsonobjectSymptomsData.put(cursor4.getColumnName(6), cursor4.getString(6));
                            jsonobjectSymptomsData.put(cursor4.getColumnName(7), cursor4.getString(7));
                            jsonobjectSymptomsData.put(cursor4.getColumnName(8), cursor4.getString(8));
                        }

                    }while (cursor4.moveToNext());
                }
                //---- health card
                if (cursor5.moveToFirst()){
                    do {
                        if (cursor5.getString(1).equals(memberID.get(i))){
                            jsonobjectSymptomsData.put(cursor5.getColumnName(0), cursor5.getString(0));
                            jsonobjectSymptomsData.put(cursor5.getColumnName(1), cursor5.getString(1));
                            jsonobjectSymptomsData.put(cursor5.getColumnName(2), cursor5.getString(2));
                            jsonobjectSymptomsData.put(cursor5.getColumnName(3), cursor5.getString(3));
                            jsonobjectSymptomsData.put(cursor5.getColumnName(4), cursor5.getString(4));
                            jsonobjectSymptomsData.put(cursor5.getColumnName(5), cursor5.getString(5));
                            jsonobjectSymptomsData.put(cursor5.getColumnName(6), cursor5.getString(6));

                        }

                    }while (cursor5.moveToNext());
                }


                if (cursor9.moveToFirst()){
                    do {
                        if (cursor9.getString(1).equals(memberID.get(i))){
                            jsonobjectSymptomsData.put(cursor9.getColumnName(0), cursor9.getString(0));
                            jsonobjectSymptomsData.put(cursor9.getColumnName(1), cursor9.getString(1));
                            jsonobjectSymptomsData.put(cursor9.getColumnName(2), cursor9.getString(2));
                            jsonobjectSymptomsData.put(cursor9.getColumnName(3), cursor9.getString(3));
                            jsonobjectSymptomsData.put(cursor9.getColumnName(4), cursor9.getString(4));

                        }

                    }while (cursor9.moveToNext());
                }
                symptomsArray.put(jsonobjectSymptomsData);

                // symptoms---
                if (cursor6.moveToFirst()){
                    symptomArray1 = new JSONArray();

                    do {
                        jsonobjectSymptoms = new JSONObject();
                        if (cursor6.getString(2).equals(memberID.get(i))){
                            jsonobjectSymptoms.put(cursor6.getColumnName(0), cursor6.getString(0));
                            jsonobjectSymptoms.put(cursor6.getColumnName(1), cursor6.getString(1));
                            jsonobjectSymptoms.put(cursor6.getColumnName(2), cursor6.getString(2));
                            jsonobjectSymptoms.put(cursor6.getColumnName(3), cursor6.getString(3));
                            jsonobjectSymptoms.put(cursor6.getColumnName(4), cursor6.getString(4));
                            jsonobjectSymptoms.put(cursor6.getColumnName(5), cursor6.getString(5));
                            jsonobjectSymptoms.put(cursor6.getColumnName(6), cursor6.getString(6));
                            jsonobjectSymptoms.put(cursor6.getColumnName(7), cursor6.getString(7));
                            jsonobjectSymptoms.put(cursor6.getColumnName(8), cursor6.getString(8));

                        }
                        symptomArray1.put(jsonobjectSymptoms);

                    }while (cursor6.moveToNext());
                    jsonobjectSymptomsData.put("Symptom",symptomArray1);
                    /*symptomsArray.put(symptomArray1);*/
/*
                    symptomsArray.put(100, jsonobjectSymptoms);
*/
                }

                //jsonobjectSymptoms.put(memberID.get(i),jsonobjectSymptomsData);
            }

            jsonobject.put("Head Data", jsonobjectHeadDetails);
            jsonobject.put("Member Data", memberArray);
            jsonobject.put("Symptoms Header", symptomsArray);
            //   jsonobject.put("Symptoms Details", storingSymptoms);
            js.put("data", jsonobject);

        }catch (JSONException e) {
            e.printStackTrace();
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLstring,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("rajnikant1", ">>" + response);
                        try {
                            removeSimpleProgressDialog();

                            JSONObject obj = new JSONObject(response);
                            Log.d("rajnikant2", ">>get result" + obj);

                            //String message = obj.getString("message");
                            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();

                            // }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("rajnikant3", ">>exception" + e.getMessage());
                        }

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                        //Log.d("rajnikant", ">>get result" + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String,String> params = new HashMap<String,String>();
                params.put("req_type","save-survey");
                params.put("serverdata", js.toString());

                return params;

            }
        };

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public void uploadPDF(final String pdfname, Uri pdffile){
        String Url = "http://172.16.12.98:9000/api/index.php";
        InputStream iStream = null;
        try {
            if (checkPermission()){
                iStream = context.getContentResolver().openInputStream(pdffile);
                final byte[] inputData = getBytes(iStream);

                VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, Url,
                        new Response.Listener<NetworkResponse>() {
                            @Override
                            public void onResponse(NetworkResponse response) {
                                Log.d("ressssssoo",new String(response.data));
                                rQueue.getCache().clear();
                                try {
                                    JSONObject jsonObject = new JSONObject(new String(response.data));
                                    Toast.makeText(context, jsonObject.getString("message"),
                                            Toast.LENGTH_LONG).show();
                                    Log.d("fileupload", "onErrorResponse: "+jsonObject.getString("message"));
                                    //jsonObject.toString().replace("\\\\","");

                                } catch (JSONException e) {

                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.d("fileupload", "onErrorResponse: "+error.getMessage());
                            }
                        }) {

                    /*
                     * If you want to add more parameters with the image
                     * you can do it here
                     * here we have only one parameter with the image
                     * which is tags
                     * */
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        // params.put("tags", "ccccc");  add string parameters
                        return params;
                    }

                    /*
                     *pass files using below method
                     * */
                    @Override
                    protected Map<String, DataPart> getByteData() {
                        Map<String, DataPart> params = new HashMap<>();
                        params.put("video_consent", new DataPart(pdfname ,inputData));
                        return params;
                    }
                };

                volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                        0,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                rQueue = Volley.newRequestQueue(context);
                rQueue.add(volleyMultipartRequest);
            }else {
                requestPermission();
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(context, "Write External Storage permission allows us to save files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("value", "Permission Granted, Now you can use local drive .");
            } else {
                Log.e("value", "Permission Denied, You cannot use local drive .");
            }
            break;
        }
    }
}
