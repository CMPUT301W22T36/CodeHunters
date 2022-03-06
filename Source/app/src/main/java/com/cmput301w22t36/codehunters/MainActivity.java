package com.cmput301w22t36.codehunters;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView codesNav, mapNav, socialNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    //.add(R.id.mainActivityFragmentView, MapFragment.class, null)
                    .add(R.id.mainActivityFragmentView, UserPersonalProfileFragment.class, null)
                    .commit();
        }

        codesNav = findViewById(R.id.navToCodes);
        mapNav = findViewById(R.id.navToMap);
        socialNav = findViewById(R.id.navToSocial);


        // Swap to the CodesFragment when the "Codes" textview is clicked
        codesNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.mainActivityFragmentView, CodesFragment.class, null)
                        .commit();
            }
        });

        // Swap to the SocialFragment when the "Social" textview is clicked
        socialNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.mainActivityFragmentView, SocialFragment.class, null)
                        .commit();
            }
        });

        // Swap to the MapFragment when the "Map" textview is clicked
        mapNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.mainActivityFragmentView, MapFragment.class, null)
                        .commit();
            }
        });


    }
}