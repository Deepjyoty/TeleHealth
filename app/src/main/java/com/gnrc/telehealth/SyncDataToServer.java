package com.gnrc.telehealth;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gnrc.telehealth.DatabaseSqlite.DBhandler;
import com.gnrc.telehealth.Model.Family_Head_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SyncDataToServer {
    private Context context;
    private static ProgressDialog mProgressDialog;
    private String URLstring = "https://www.gnrctelehealth.com/telehealth_api/";
    private DBhandler dBhandler;

    public SyncDataToServer(Context context) {
        this.context = context;
        storingJSON();
    }
    private void storingJSON() {

        showSimpleProgressDialog(context, "Loading...","Saving",false);

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
                            Toast.makeText(context, ""+ message, Toast.LENGTH_SHORT).show();
                            familyHeadModelArrayList = new ArrayList<>();
                            JSONArray dataArray  = obj.getJSONArray("products");

                            for (int i = 0; i < dataArray.length(); i++) {
                                Family_Head_Model playerModel = new Family_Head_Model();
                                JSONObject dataobj = dataArray.getJSONObject(i);

                                /*playerModel.setId(dataobj.getInt("id"));
                                playerModel.setTitle(dataobj.getString("title"));
                                playerModel.setBrand(dataobj.getString("brand"));
                                playerModel.setPrice(dataobj.getInt("price"));
                                playerModel.setThumbnail(dataobj.getString("thumbnail"));
                                playerModel.setDescription(dataobj.getString("description"));*/

                                familyHeadModelArrayList.add(playerModel);

                            }

                            setupRecyclerFrom_DB();
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
                dBhandler = new DBhandler(getApplicationContext());
                Cursor cursor = dBhandler.getFamilyDbData();
                //Family_Head_Model dmodel = new Family_Head_Model();
                Map<String,String> params = new HashMap<String,String>();
                params.put("req_type","create-family");
                params.put("family-id","0");
                params.put("family-head-name", cursor.getString(10));
                params.put("contact-no", cursor.getString(2));
                params.put("house-no", cursor.getString(3));
                params.put("address", cursor.getString(4));
                params.put("gaon-panchayat-code", cursor.getString(5));
                params.put("block-code", cursor.getString(6));
                params.put("city-code", cursor.getString(7));
                params.put("dist-code",cursor.getString(8));
                params.put("state-code",cursor.getString(9));
                params.put("pin-code", cursor.getString(10));
                params.put("user-id", cursor.getString(0));
                Log.d("deep", "getParams: "+params);
                return params;

            }
        };
        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
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

}
