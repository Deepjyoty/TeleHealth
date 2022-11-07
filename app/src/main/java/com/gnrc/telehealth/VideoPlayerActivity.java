package com.gnrc.telehealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


import com.gnrc.telehealth.Fragments.NewsFeedFragment;
import com.gnrc.telehealth.Model.Model_newsfeed;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class VideoPlayerActivity extends AppCompatActivity {
    Intent intent;
    TextView textView;
    Model_newsfeed dataModel;
    VideoView videoView;

    // Your Video URL
    String videoUrl;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        videoView = findViewById(R.id.videoView2);
        ActionBar actionBar = getSupportActionBar();
        View decorView = getWindow().getDecorView();
        int uiOptions = /*View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                |*/ View.SYSTEM_UI_FLAG_FULLSCREEN ;
        decorView.setSystemUiVisibility(uiOptions);
        actionBar.hide();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        intent = getIntent();
        MediaController mediaController = new MediaController(this);
        if (intent!=null){
            dataModel = (Model_newsfeed) intent.getSerializableExtra("data");
            if (Objects.equals(dataModel.getContent_type(), "video")){
                Toast.makeText(this, "Its a video", Toast.LENGTH_SHORT).show();
            }
/*            videoUrl = dataModel.getVideo_url();
            Uri uri = Uri.parse(videoUrl);
            videoView.setVideoURI(uri);
            String dat = dataModel.getContent_desc();
            Log.d("deep2", "onCreate: " + dat);*/


            mediaController.setAnchorView(videoView);

            mediaController.setMediaPlayer(videoView);
            videoView.setMediaController(mediaController);
            videoView.start();
            Log.d("deep2", "onCreate: " + videoUrl);
            //Toast.makeText(this, "You are "+dataModel.getTitle(), Toast.LENGTH_SHORT).show();
            /*String description = dataModel.getContent_desc();
            textView.setText(description);*/
            //Picasso.get().load(dataModel.getThumbnail_url()).into(imageView);
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