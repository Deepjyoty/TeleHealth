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
import com.gnrc.telehealth.DataDetails;
import com.gnrc.telehealth.Model.DataModel2;
import com.gnrc.telehealth.Model.Model_newsfeed;
import com.gnrc.telehealth.R;
import com.gnrc.telehealth.VideoPlayerActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class NewsFeedFragment extends Fragment {
    private String URLstring = "https://www.gnrctelehealth.com/telehealth_api/";
    private static ProgressDialog mProgressDialog;
    ArrayList<Model_newsfeed> dataModelArrayList;
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

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLstring,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("deep", "" + response);

                        try {

                            removeSimpleProgressDialog();

                            JSONArray obj = new JSONArray(response);
                            dataModelArrayList = new ArrayList<>();
                            //JSONArray dataArray  = obj.getJSONArray("products");

                            for (int i = 0; i < obj.length(); i++) {

                                JSONObject dataobj = obj.getJSONObject(i);
                                String title = dataobj.getString("title");
                                String author = dataobj.getString("author");
                                String date = dataobj.getString("date");
                                String content_desc = dataobj.getString("content_desc");
                                String video_url = dataobj.getString("video_url");
                                String thumbnail_url = dataobj.getString("thumbnail_url");
                                String content_type = dataobj.getString("content_type");
                                Model_newsfeed playerModel = new Model_newsfeed(title,author,date,content_desc,video_url,thumbnail_url,content_type);
                                playerModel.setTitle(title);
                                playerModel.setAuthor(author);
                                playerModel.setDate(date);
                                playerModel.setContent_desc(content_desc);
                                playerModel.setVideo_url(video_url);
                                playerModel.setThumbnail_url(thumbnail_url);
                                playerModel.setContent_type(content_type);

                                dataModelArrayList.add(playerModel);

                            }
                            Log.d("deep1", "" + dataModelArrayList);
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
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("req_type","get-news-feed");
                Log.d("deep", "getParams: " + params);
                return params;
            }
        };

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
    public void selecteduser(Model_newsfeed dataModel) {

        //dataModel.getid();
        startActivity(new Intent(getActivity(), VideoPlayerActivity.class).putExtra("data",dataModel));


    }
}