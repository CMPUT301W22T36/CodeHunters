package com.cmput301w22t36.codehunters;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.cmput301w22t36.codehunters.Fragments.SocialFragment;
import com.cmput301w22t36.codehunters.Fragments.UserPersonalProfileFragment;
import com.google.android.material.navigation.NavigationView;

public class MenuHeader extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Initialize the layout
    private DrawerLayout drawer;

    // Set up the hamburger button
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sidebar_layout); ///////TODO??

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Open and close the hamburger menu sidebar
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // The fragment to start with before any items clicked, also loaded when rotate the device
        /*
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new MapFragment()).commit();
        }*/
    }

    // When items are clicked in the sidebar, move to the specified fragment.
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.my_account:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new UserPersonalProfileFragment()).commit();
                break;
            case R.id.list_nearby_codes:
                // TODO: Placeholder
                break;
        }

        // Select the clicked item
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // To close the sidebar drawer if it is open
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
