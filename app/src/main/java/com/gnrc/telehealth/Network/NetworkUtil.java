package com.gnrc.telehealth.Network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkUtil {
    private static String TAG = "NetworkUtil";

    public static boolean getConnectivityStatus(Context context){
        boolean status = false;
        ConnectivityManager connectivityManager
                = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null){
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
                status = true;
                Log.d(TAG, "Wifi available");
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE){
                status = true;
                Log.d(TAG, "Mobile data available");
            }
        } else {
            status = false;
        }

        return status;
    }
}