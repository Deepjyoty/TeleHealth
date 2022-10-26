package com.gnrc.telehealth.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gnrc.telehealth.Adapter.RvAdapter2;
import com.gnrc.telehealth.Model.DataModel2;
import com.gnrc.telehealth.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class NewsFeedFragment extends Fragment {
    private String URLstring = "https://dummyjson.com/products";
    private static ProgressDialog mProgressDialog;
    ArrayList<DataModel2> dataModelArrayList;
    private RvAdapter2 rvAdapter2;
    private RecyclerView recyclerView;
    View view;



    public NewsFeedFragment() {
        // Required empty public constructor
    }

    public static NewsFeedFragment newInstance(String param1, String param2) {
        NewsFeedFragment fragment = new NewsFeedFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_news_feed, container, false);
        // Add the following lines to create RecyclerView
        recyclerView = view.findViewById(R.id.newsrecycler);

        fetchingJSON();
        return view;
    }
    private void fetchingJSON() {

        showSimpleProgressDialog(getContext(), "Loading...","Fetching Json",false);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLstring,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("deep", "" + response);

                        try {

                            removeSimpleProgressDialog();

                            JSONObject obj = new JSONObject(response);
                            dataModelArrayList = new ArrayList<>();
                            JSONArray dataArray  = obj.getJSONArray("products");

                            for (int i = 0; i < dataArray.length(); i++) {
                                DataModel2 playerModel = new DataModel2();
                                JSONObject dataobj = dataArray.getJSONObject(i);
                                playerModel.setId(dataobj.getInt("id"));
                                playerModel.setTitle(dataobj.getString("title"));
                                playerModel.setBrand(dataobj.getString("brand"));
                                playerModel.setPrice(dataobj.getInt("price"));
                                playerModel.setThumbnail(dataobj.getString("thumbnail"));
                                playerModel.setDescription(dataobj.getString("description"));

                                dataModelArrayList.add(playerModel);

                            }
                            Log.d("deep", "" + dataModelArrayList);
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
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        requestQueue.add(stringRequest);


    }
    private void setupRecycler(){
        recyclerView.setHasFixedSize(true);
        rvAdapter2 = new RvAdapter2(getContext(),dataModelArrayList,this::selecteduser);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(rvAdapter2);
        /*rvAdapter2 = new RvAdapter2(getContext(),dataModelArrayList,this::selecteduser);
        recyclerView.setAdapter(rvAdapter2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));*/

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

    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //MenuInflater inflater = getMenuInflater();
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
                rvAdapter2.getFilter().filter(newText);
                return false;
            }
        });
    }
    public void selecteduser(DataModel2 dataModel) {
        //dataModel.getid();
       // startActivity(new Intent(this,DataDetails.class).putExtra("data",dataModel));
    }
}