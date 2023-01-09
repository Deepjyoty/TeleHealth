package com.gnrc.telehealth;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ChoiceActivity extends AppCompatActivity {
    Button telehealth, survey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);
        telehealth = (Button) findViewById(R.id.btntelehealth);
        survey = (Button) findViewById(R.id.btnsurvey);
        telehealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChoiceActivity.this,WebViewActivity.class);
                startActivity(i);
                finish();
            }
        });
        survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChoiceActivity.this, PermissonActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
    public void onBackPressed() {
        AlertDialog diaBox = AskOption();
        diaBox.show();
    }

    private AlertDialog AskOption()
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                .setTitle("EXIT")
                .setMessage("Are you sure you want to exit?")


                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        finishAndRemoveTask();
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        return myQuittingDialogBox;
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