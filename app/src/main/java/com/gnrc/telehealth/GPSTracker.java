package com.gnrc.telehealth;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.Nullable;

public class GPSTracker extends Service implements LocationListener {

    private Context context;

    private String TAG = getClass().getName();

    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;
    private boolean canGetLocation = false;

    private Location location;
    private double latitude, longitude;

    private static long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static long MIN_TIME_BETWEEN_UPDATES = 1000 * 60 * 1;

    private LocationManager locationManager;

    public GPSTracker(Context context) {
        this.context = context;
        getLocation();
    }

    @SuppressLint("MissingPermission")
    public Location getLocation() {
        try {
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

            //false if not enabled for both
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                //no data available
            } else {
                this.canGetLocation = true;

//                 if (ActivityCompat.checkSelfPermission(this,
////                            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
////                            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
////                            != PackageManager.PERMISSION_GRANTED) {
////                        //if permission not provided yet
////                 }

                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BETWEEN_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    Log.d(TAG,"Network Enabled");
                    if(locationManager != null){
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if(location != null){
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }

                if(isGPSEnabled) {
                    if(location == null){
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BETWEEN_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    }

                    Log.d(TAG, "GPS Enabled");
                    if(locationManager != null){
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                        if(location != null){
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return location;
    }

    public void stopGPS (){
        if(locationManager != null){
            locationManager.removeUpdates(GPSTracker.this);
        }
    }

    public double getLatitude() {
        if(location != null){
            latitude = location.getLatitude();
        }
        return latitude;
    }

    public double getLongitude () {
        if(location != null){
            longitude = location.getLongitude();
        }
        return longitude;
    }

    //check if gps/wifi enabled
    public boolean canGetLocation(){
        return this.canGetLocation;
    }

    //show the settings button
    public void showSettingsAlert(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("GPS Settings");
        alertDialog.setMessage("Please turn on GPS and restart the Application. Continue ?");

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
