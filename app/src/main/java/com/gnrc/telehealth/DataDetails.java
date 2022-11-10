package com.gnrc.telehealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.gnrc.telehealth.Model.Model_newsfeed;
import com.squareup.picasso.Picasso;

public class DataDetails extends AppCompatActivity {
    ImageView imageView;
    TextView textView;
    Model_newsfeed dataModel;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_details);
        ActionBar actionBar = getSupportActionBar();


        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        imageView = findViewById(R.id.detailimage);
        textView = findViewById(R.id.textdetails);
        intent = getIntent();



        if (intent!=null){
            dataModel = (Model_newsfeed) intent.getSerializableExtra("data");

            //Toast.makeText(this, "You are "+dataModel.getTitle(), Toast.LENGTH_SHORT).show();
            String description = dataModel.getContent_desc();
            textView.setText(description);
            Picasso.get().load(dataModel.getThumbnail_url()).into(imageView);
        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}