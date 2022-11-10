package com.gnrc.telehealth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
                Intent i = new Intent(ChoiceActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}