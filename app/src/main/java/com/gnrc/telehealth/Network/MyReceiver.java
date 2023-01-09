package com.gnrc.telehealth.Network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.gnrc.telehealth.SurveyActivity;

public class MyReceiver extends BroadcastReceiver {
    private static String TAG = "NetworkReceiver";
    private NetworkListener mListener;

    public MyReceiver() {
    }

    public MyReceiver(NetworkListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null){
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
                mListener.onNetworkConnected("WIFI");
                Log.d(TAG, "Wifi available");
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE){
                mListener.onNetworkConnected("MOBILE_DATA");
                Log.d(TAG, "Mobile data available");
            }
        } else {
            mListener.onNetworkDisconnected();
        }
    }
}
