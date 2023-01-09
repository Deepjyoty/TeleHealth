package com.gnrc.telehealth;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class PermissonActivity extends AppCompatActivity {

    TextView dontAllow;
    Button allow;
    private String[] PERMISSIONS;
    String is_signed_in="";
    SharedPreferences mPreferences;
    String sharedprofFile="permission";
    SharedPreferences.Editor preferencesEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permisson);
        mPreferences=getSharedPreferences(sharedprofFile,MODE_PRIVATE);
        preferencesEditor = mPreferences.edit();

        //Checking for the value stored in shared preference
        is_signed_in = mPreferences.getString("issignedin","false");
        if(is_signed_in.equals("true"))
        {
            Intent i = new Intent(this,LoginActivity.class);
            startActivity(i);
            finish();
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
            dontAllow = findViewById(R.id.tv_dontAllow);
            allow = findViewById(R.id.btn_allow);

            PERMISSIONS = new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,


            };

            dontAllow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finishAndRemoveTask();

                }
            });

            allow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startPermission();
                }
            });
        }else {
            Intent i = new Intent(this,LoginActivity.class);
            startActivity(i);
            finish();
        }

    }

    public void startPermission(){
        if (!hasPermissions(this,PERMISSIONS)) {
            ActivityCompat.requestPermissions(this,PERMISSIONS,1);
        }
    }
    private boolean hasPermissions(Context context, String... PERMISSIONS) {

        if (context != null && PERMISSIONS != null) {

            for (String permission: PERMISSIONS){

                if (ActivityCompat.checkSelfPermission(context,permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }else {
                ActivityCompat.requestPermissions(this,PERMISSIONS,1);
                Toast.makeText(this, "Location Permission is denied", Toast.LENGTH_SHORT).show();
            }

            if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {

            }else {
                Toast.makeText(this, "Camera Permission is denied", Toast.LENGTH_SHORT).show();

            }

            if (grantResults[2] == PackageManager.PERMISSION_GRANTED) {

                preferencesEditor.putString("issignedin","true");
                preferencesEditor.apply();

                Intent i = new Intent(this, LoginActivity.class);
                startActivity(i);
                finish();
            }else {
                Toast.makeText(this, "Read/Write Permission is denied", Toast.LENGTH_SHORT).show();

            }

        }
    }
    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();

    }
}