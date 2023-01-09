package com.gnrc.telehealth;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.gnrc.telehealth.Fragments.NewsFeedFragment;
import com.gnrc.telehealth.Fragments.SurveyFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NewsFeedActivity extends AppCompatActivity {
    BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);

        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnItemSelectedListener(selectedListener);


        // When we open the application first
        // time the fragment should be shown to the user
        // in this case it is home fragment
        NewsFeedFragment fragment = new NewsFeedFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, fragment, "");
        fragmentTransaction.commit();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {

                case R.id.newsfeed:
                    //actionBar.setTitle("Home");
                    NewsFeedFragment fragment = new NewsFeedFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content, fragment, "");
                    fragmentTransaction.commit();
                    return true;

                case R.id.survey:
                    //actionBar.setTitle("Profile");
                    SurveyFragment fragment1 = new SurveyFragment();
                    FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction1.replace(R.id.content, fragment1);
                    fragmentTransaction1.commit();
                    return true;

            }
            return false;
        }
    };
    public void onBackPressed() {
        Intent i = new Intent(this,ChoiceActivity.class);
        startActivity(i);
        finish();
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