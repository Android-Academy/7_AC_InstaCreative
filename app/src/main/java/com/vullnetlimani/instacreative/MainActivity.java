package com.vullnetlimani.instacreative;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vullnetlimani.instacreative.Activities.PostActivity;
import com.vullnetlimani.instacreative.Fragments.HomeFragment;
import com.vullnetlimani.instacreative.Fragments.NotificationFragment;
import com.vullnetlimani.instacreative.Fragments.ProfileFragment;
import com.vullnetlimani.instacreative.Fragments.SearchFragment;
import com.vullnetlimani.instacreative.Helper.Constants;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Fragment selectorFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.nav_home:
                        selectorFragment = new HomeFragment();
                        break;
                    case R.id.nav_search:
                        selectorFragment = new SearchFragment();
                        break;
                    case R.id.nav_add:
                        selectorFragment = null;

                        startActivity(new Intent(MainActivity.this, PostActivity.class));

                        break;
                    case R.id.nav_heart:
                        selectorFragment = new NotificationFragment();
                        break;
                    case R.id.nav_profile:
                        selectorFragment = new ProfileFragment();
                        break;
                }

                if (selectorFragment != null) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, selectorFragment)
                            .addToBackStack(selectorFragment.getClass().getSimpleName())
                            .commit();
                }

                return true;
            }
        });

        Bundle inBundle = getIntent().getExtras();
        if (inBundle != null) {

            String profileId = inBundle.getString(Constants.PUBLISHER_ID);
            getSharedPreferences(Constants.PROFILE, MODE_PRIVATE).edit().putString(Constants.PROFILE_ID, profileId).apply();

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new ProfileFragment())

                    .commit();
            //    bottomNavigationView.setSelectedItemId(R.id.nav_profile);
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .addToBackStack(HomeFragment.class.getSimpleName())
                    .commit();
        }

    }
}