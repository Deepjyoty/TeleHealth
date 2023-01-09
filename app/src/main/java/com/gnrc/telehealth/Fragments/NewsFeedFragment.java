package com.gnrc.telehealth.Fragments;

import static com.zipow.videobox.confapp.ConfMgr.getApplicationContext;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
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
import com.gnrc.telehealth.Adapter.News_feed_Adapter;
import com.gnrc.telehealth.DatabaseSqlite.DBhandler;
import com.gnrc.telehealth.Model.Model_newsfeed;
import com.gnrc.telehealth.Network.MyReceiver;
import com.gnrc.telehealth.Network.NetworkListener;
import com.gnrc.telehealth.R;
import com.gnrc.telehealth.ShowSurveyActivity;
import com.gnrc.telehealth.SurveyActivity;
import com.gnrc.telehealth.VideoPlayerActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class NewsFeedFragment extends Fragment implements NetworkListener {
    private String URLstring = "https://www.gnrctelehealth.com/telehealth_api/index_dev.php";
    private static ProgressDialog mProgressDialog;
    ArrayList<Model_newsfeed> dataModelArrayList;
    private News_feed_Adapter newsfeedAdapter;
    private RecyclerView recyclerView;
    ConstraintLayout constraintLayout;
    private BroadcastReceiver MyReceiver = null;
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
        MyReceiver = new MyReceiver(this);
        // Add the following lines to create RecyclerView
        recyclerView = view.findViewById(R.id.newsrecycler);
        constraintLayout = view.findViewById(R.id.noInternet);
        ConnectivityManager cm = (ConnectivityManager) getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                fetchingJSON();

            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to mobile data
                fetchingJSON();
            }
        } else {

            /*Toast.makeText(getContext(), "This page cannot be loaded due to no " +
                    "Internet connectivity", Toast.LENGTH_SHORT).show();*/

            }


        return view;
    }
    private void fetchingJSON() {

        showSimpleProgressDialog(getContext(), "Loading...","Fetching Data",false);

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
                        Toast.makeText(getContext(), "This page cannot be loaded due to no " +
                                "Internet connectivity", Toast.LENGTH_SHORT).show();
                        removeSimpleProgressDialog();
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
        newsfeedAdapter = new News_feed_Adapter(getContext(),dataModelArrayList,this::selecteduser);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(newsfeedAdapter);


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
                newsfeedAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }
    public void selecteduser(Model_newsfeed dataModel) {

        //dataModel.getid();

        if (dataModel.getContent_type().equals("video")){
            startActivity(new Intent(getActivity(), VideoPlayerActivity.class).putExtra("data",dataModel));
        }

    }
    /**
     * register bradcast receiver for network
     */
    private void registerNetworkReceiver(){
        try {
            getContext().registerReceiver(MyReceiver,
                    new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * un register broadcast receiver
     */
    private void unregisterNetworkReceiver(){
        try {
            if (MyReceiver != null){
                LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(MyReceiver);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onNetworkDisconnected() {
        recyclerView.setVisibility(View.GONE);
        constraintLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onNetworkConnected(String type) {
        recyclerView.setVisibility(View.VISIBLE);
        fetchingJSON();
        constraintLayout.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        registerNetworkReceiver();
    }
    @Override
    public void onResume() {
        super.onResume();
        registerNetworkReceiver();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterNetworkReceiver();
    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterNetworkReceiver();
    }
}