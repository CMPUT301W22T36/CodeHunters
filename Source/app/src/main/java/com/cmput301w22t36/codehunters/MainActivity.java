package com.cmput301w22t36.codehunters;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301w22t36.codehunters.Data.DataMappers.QRCodeMapper;
import com.cmput301w22t36.codehunters.Data.DataMappers.UserMapper;
import com.cmput301w22t36.codehunters.Data.DataTypes.QRCodeData;
import com.cmput301w22t36.codehunters.Data.DataTypes.User;
import com.cmput301w22t36.codehunters.Fragments.CodesFragment;
import com.cmput301w22t36.codehunters.Fragments.MapFragment;
import com.cmput301w22t36.codehunters.Fragments.SearchNearbyCodesFragment;
import com.cmput301w22t36.codehunters.Fragments.SearchUserFragment;
import com.cmput301w22t36.codehunters.Fragments.SocialFragment;
import com.cmput301w22t36.codehunters.Fragments.UserPersonalProfileFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

/**
 * Class: MainActivity
 *
 * Load the main foundational fragment with a bottom navigation bar and call the start of the app.
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public  static MainActivity mainActivity;

    public static final String TAG = "MainActivity"; // For activityResultLaunch debugging
    private DrawerLayout drawer; // For the screen header

    TextView codesNav, mapNav, socialNav;
    FloatingActionButton scanQRCode;
    ActivityResultLauncher<Intent> activityResultLauncher;
    public CodesFragment codesFragment;
    public User searchUser;
    //TEST - MEHUL (populate list of qrcodes to test listview)
    public ArrayList<QRCode> codeArrayList = new ArrayList<QRCode>();
    QRCode current_code;

    public User loggedinUser;

    // Launch the FirstWelcomeActivity requiring returned results to indicate if
    // the user selected ScanToLogin
    ActivityResultLauncher<Intent> activityResultLaunch = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                /**
                 * This accepts and analyzes the result codes from the FirstWelcomeActivity
                 * @param result: the result that FirstWelcomeActivity returns after returning
                 */
                @Override
                public void onActivityResult(ActivityResult result) {
                    // Indicate return to MainActivity
                    Log.d(TAG, "OnActivityResult: ");

                    // Test the result code for the next screen to load
                    if (result.getResultCode() == 12) {
                        // Load the Map Fragment
                        QRCodeMapper codeMapper = new QRCodeMapper();
                        codeMapper.getAllCodes(codeMapper.new CompletionHandler<ArrayList<QRCodeData>>() {
                            @Override
                            public void handleSuccess(ArrayList<QRCodeData> codes) {
                                ArrayList<QRCode> newCodes= new ArrayList<>();
                                for (QRCodeData code : codes) {
                                    newCodes.add(new QRCode(code));
                                }
                                openMap(newCodes);
                            }
                        });
                        /*getSupportFragmentManager().beginTransaction()
                                .setReorderingAllowed(true)
                                .add(R.id.fragment_container, MapFragment.class, null)
                                .commit();*/
                    } else if (result.getResultCode() == 22) {
                        // Load the ScanToLogin activity
                        // TODO: change to ScanToLogin fragment name, and once return goto MapFrag.
                        /*Intent myIntent = new Intent(MainActivity.this, ScanToLogin.class);
                        startActivity(myIntent);*/

                        IntentIntegrator intentIntegrator = new IntentIntegrator(
                                com.cmput301w22t36.codehunters.MainActivity.this//getActivity()
                        );
                        intentIntegrator.setBeepEnabled(false);
                        intentIntegrator.setPrompt("Scan QR Code");
                        intentIntegrator.setOrientationLocked(true);
                        intentIntegrator.setCaptureActivity(Capture.class);
                        intentIntegrator.setRequestCode(2);
                        intentIntegrator.initiateScan();

                        getSupportFragmentManager().beginTransaction()
                                .setReorderingAllowed(true)
                                .add(R.id.fragment_container, MapFragment.class, null)
                                .commit();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivity = this;
        // Go to the First Welcome Fragment to identify this device and CodeHunters account
        Intent intent = new Intent(MainActivity.this, FirstWelcomeActivity.class);
        activityResultLaunch.launch(intent);

        // Set the hamburger menu
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



        // The remaining setup for the main screens
        codesNav = findViewById(R.id.navToCodes);
        mapNav = findViewById(R.id.navToMap);
        socialNav = findViewById(R.id.navToSocial);

        updateCodeLists();

        // Swap to the CodesFragment when the "Codes" textview is clicked
        codesNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCodeLists();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                codesFragment = CodesFragment.newInstance(codeArrayList);
                ft.replace(R.id.fragment_container, codesFragment);
                ft.commit();
//                getSupportFragmentManager().beginTransaction()
//                        .setReorderingAllowed(true)
//                        .replace(R.id.fragment_container, CodesFragment.class, null)
//                        .commit();
            }
        });

        // Swap to the SocialFragment when the "Social" textview is clicked
        socialNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fts = getSupportFragmentManager().beginTransaction();
                SocialFragment fragmentDemoS = SocialFragment.newInstance(loggedinUser.getUsername());
                fts.replace(R.id.fragment_container,fragmentDemoS);
                fts.commit();
                /*
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragment_container, SocialFragment.class, null)
                        .commit();*/
            }
        });

        // Swap to the MapFragment when the "Map" textview is clicked
        mapNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QRCodeMapper codeMapper = new QRCodeMapper();
                codeMapper.getAllCodes(codeMapper.new CompletionHandler<ArrayList<QRCodeData>>() {
                    @Override
                    public void handleSuccess(ArrayList<QRCodeData> codes) {
                        ArrayList<QRCode> newCodes = new ArrayList<>();
                        for (QRCodeData code : codes) {
                            newCodes.add(new QRCode(code));
                        }
                        openMap(newCodes);
                    }
                });
            }
        });

        scanQRCode = findViewById(R.id.scanQRCode);

        scanQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(
                        MainActivity.this
                );
                intentIntegrator.setBeepEnabled(false);
                intentIntegrator.setPrompt("Scan QR Code");
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setCaptureActivity(Capture.class);
                intentIntegrator.setRequestCode(1);
                intentIntegrator.initiateScan();
            }
        });

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Bundle bundle = result.getData().getExtras();
                            Bitmap qrLocationImage = (Bitmap) bundle.get("data");
                            current_code.setPhoto(qrLocationImage);
                            QRCodeData cur_code = new QRCodeData();
                            cur_code.setHash(current_code.getHash());
                            if (current_code.hasLocation()) {
                                cur_code.setLat(current_code.getGeolocation().get(0));
                                cur_code.setLon(current_code.getGeolocation().get(1));
                            }
                            cur_code.setScore(current_code.getScore());
                            cur_code.setPhoto(current_code.getPhoto());
                            cur_code.setUserRef("/users/"+loggedinUser.getId());
                            for (QRCode code : codeArrayList) {
                                if (cur_code.getHash().equals(code.getHash())) {
                                    cur_code.setId(code.getId());
                                    break;
                                }
                            }


                            // Update the User's score and bestScore:
                            QRCodeMapper qrmapper = new QRCodeMapper();
                            qrmapper.update(cur_code, qrmapper.new CompletionHandler<QRCodeData>() {
                                @Override
                                public void handleSuccess(QRCodeData data) {
                                    // Update User Score:
                                    UserMapper um = new UserMapper();
                                    um.get(loggedinUser.getId(), um.new CompletionHandler<User>() {
                                        @Override
                                        public void handleSuccess(User user) {
                                            user.setScore(user.getScore() + cur_code.getScore());
                                            if (cur_code.getScore() > loggedinUser.getBestScore()) {
                                                loggedinUser.setBestScore(cur_code.getScore());
                                                um.update(user, um.new CompletionHandler<User>(){
                                                    @Override
                                                    public void handleSuccess(User user) {
                                                        updateCodeLists();
                                                    }
                                                    @Override
                                                    public void handleError(Exception e) {
                                                        updateCodeLists();
                                                    }
                                                });
                                            }
                                        }
                                        @Override
                                        public void handleError(Exception e) {
                                            updateCodeLists();
                                        }
                                    });
                                }
                            });



                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        IntentResult intentResult = IntentIntegrator.parseActivityResult(
//                requestCode,resultCode,data
//        );
        IntentResult intentResult = IntentIntegrator.parseActivityResult(
                resultCode, data
        );
        if (intentResult != null) {
            if (requestCode == 1) {
                String QRString = intentResult.getContents();
                if (intentResult.getContents() != null) {
                    current_code = new QRCode(QRString);
                    boolean duplicate = false;
                    for (QRCode code : codeArrayList) {
                        if (current_code.getHash().equals(code.getHash())) {
                            duplicate = true;
                            break;
                        }
                    }
                    if (duplicate == false) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                MainActivity.this
                        );
                        builder.setTitle("Result");
                        builder.setMessage(intentResult.getContents());
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(
                                        MainActivity.this
                                );
                                builder1.setTitle("Would You Like To Take A Photo of The QR Code Location");
                                builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        AlertDialog.Builder builder2 = new AlertDialog.Builder(
                                                MainActivity.this
                                        );
                                        builder2.setTitle("Would You Like To Store The Geolocation");
                                        builder2.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                //User has said no to both photo and location
                                                QRCodeData cur_code = new QRCodeData();
                                                cur_code.setHash(current_code.getHash());
                                                cur_code.setScore(current_code.getScore());
                                                cur_code.setUserRef("/users/" + loggedinUser.getId());
                                                QRCodeMapper qrmapper = new QRCodeMapper();
                                                qrmapper.create(cur_code, qrmapper.new CompletionHandler<QRCodeData>() {
                                                    @Override
                                                    public void handleSuccess(QRCodeData data) {
                                                        updateCodeLists();
                                                    }
                                                });
                                                dialogInterface.dismiss();
                                            }

                                        });
                                        builder2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                //User has said no to photo and yes to location
                                                if (ActivityCompat.checkSelfPermission(MainActivity.this,
                                                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                                    LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                                                    Location loc = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                                    double longi = loc.getLongitude();
                                                    double lat = loc.getLatitude();
                                                    String longitude = String.valueOf(longi);
                                                    String latitude = String.valueOf(lat);
                                                    ArrayList<Double> geolocation = new ArrayList<Double>();
                                                    geolocation.add(lat);
                                                    geolocation.add(longi);
                                                    current_code.setGeolocation(geolocation);
                                                    QRCodeData cur_code = new QRCodeData();
                                                    cur_code.setHash(current_code.getHash());
                                                    cur_code.setLat(current_code.getGeolocation().get(0));
                                                    cur_code.setLon(current_code.getGeolocation().get(1));
                                                    cur_code.setScore(current_code.getScore());
                                                    cur_code.setUserRef("/users/" + loggedinUser.getId());

                                                    QRCodeMapper qrmapper = new QRCodeMapper();
                                                    qrmapper.create(cur_code, qrmapper.new CompletionHandler<QRCodeData>() {
                                                        @Override
                                                        public void handleSuccess(QRCodeData data) {
                                                            updateCodeLists();
                                                        }
                                                    });
                                                } else {
                                                    ActivityCompat.requestPermissions(MainActivity.this, new String[]
                                                            {Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                                                    QRCodeData cur_code = new QRCodeData();
                                                    cur_code.setHash(current_code.getHash());
                                                    cur_code.setScore(current_code.getScore());
                                                    cur_code.setUserRef("/users/" + loggedinUser.getId());

                                                    QRCodeMapper qrmapper = new QRCodeMapper();
                                                    qrmapper.create(cur_code, qrmapper.new CompletionHandler<QRCodeData>() {
                                                        @Override
                                                        public void handleSuccess(QRCodeData data) {
                                                            updateCodeLists();
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                        builder2.show();
                                    }
                                });
                                builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        AlertDialog.Builder builder3 = new AlertDialog.Builder(
                                                MainActivity.this
                                        );
                                        builder3.setTitle("Would You Like To Store The Geolocation");
                                        builder3.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                //User said yes to photo and no to location
                                                QRCodeData cur_code = new QRCodeData();
                                                cur_code.setHash(current_code.getHash());
                                                cur_code.setScore(current_code.getScore());
                                                cur_code.setUserRef("/users/" + loggedinUser.getId());

                                                QRCodeMapper qrmapper = new QRCodeMapper();
                                                qrmapper.create(cur_code, qrmapper.new CompletionHandler<QRCodeData>() {
                                                    @Override
                                                    public void handleSuccess(QRCodeData data) {
                                                        updateCodeLists();
                                                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                                        activityResultLauncher.launch(intent);
                                                    }
                                                });
                                                dialogInterface.dismiss();
                                            }
                                        });
                                        builder3.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                //User has said yes to photo and location
                                                if (ActivityCompat.checkSelfPermission(MainActivity.this,
                                                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                                    LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                                                    Location loc = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                                    double longi = loc.getLongitude();
                                                    double lat = loc.getLatitude();
                                                    String longitude = String.valueOf(longi);
                                                    String latitude = String.valueOf(lat);
                                                    ArrayList<Double> geolocation = new ArrayList<Double>();
                                                    geolocation.add(lat);
                                                    geolocation.add(longi);
                                                    current_code.setGeolocation(geolocation);
                                                    QRCodeData cur_code = new QRCodeData();
                                                    cur_code.setHash(current_code.getHash());
                                                    cur_code.setLat(current_code.getGeolocation().get(0));
                                                    cur_code.setLon(current_code.getGeolocation().get(1));
                                                    cur_code.setScore(current_code.getScore());
                                                    cur_code.setUserRef("/users/" + loggedinUser.getId());

                                                    QRCodeMapper qrmapper = new QRCodeMapper();
                                                    qrmapper.create(cur_code, qrmapper.new CompletionHandler<QRCodeData>() {
                                                        @Override
                                                        public void handleSuccess(QRCodeData data) {
                                                            updateCodeLists();
                                                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                                            activityResultLauncher.launch(intent);
                                                        }
                                                    });
                                                } else {
                                                    ActivityCompat.requestPermissions(MainActivity.this, new String[]
                                                            {Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                                                    QRCodeData cur_code = new QRCodeData();
                                                    cur_code.setHash(current_code.getHash());
                                                    cur_code.setScore(current_code.getScore());
                                                    cur_code.setUserRef("/users/" + loggedinUser.getId());

                                                    QRCodeMapper qrmapper = new QRCodeMapper();
                                                    qrmapper.create(cur_code, qrmapper.new CompletionHandler<QRCodeData>() {
                                                        @Override
                                                        public void handleSuccess(QRCodeData data) {
                                                            updateCodeLists();
                                                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                                            activityResultLauncher.launch(intent);
                                                        }
                                                    });
                                                }

                                            }
                                        });
                                        builder3.show();

                                        //dialogInterface.dismiss();
                                    }
                                });
                                builder1.show();
                            }
                        });
                        builder.show();
                    } else  {
                        Toast.makeText(getApplicationContext(), "Error: You have already scanned this code", Toast.LENGTH_SHORT)
                                .show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "You did not scan anything", Toast.LENGTH_SHORT)
                            .show();
                }
            } else if (requestCode == 2){

                if (intentResult==null)
                    return;
                String userID = intentResult.getContents();
                UserMapper um = new UserMapper();
                um.get(userID, um.new CompletionHandler<User>() {
                    @Override
                    public void handleSuccess(User data) {
                        // User with UUID found.
                        ArrayList<String> UDIDs = data.getUdid();
                        UDIDs.add(Settings.Secure.getString(getApplicationContext().
                                        getContentResolver(),
                                Settings.Secure.ANDROID_ID));
                        data.setUdid(UDIDs);
                        um.update(data, um.new CompletionHandler<User>() {
                            @Override
                            public void handleSuccess(User data) {
                                // User udid list successfully updated and is logged in
                                loggedinUser = data;
                                AlertDialog.Builder builder = new AlertDialog.Builder(
                                        MainActivity.this);
                                // Set title
                                builder.setTitle("Login Successful");
                                // Set message
                                builder.setMessage("Welcome! " + loggedinUser.getUsername());
                                // Set positive button
                                builder.setPositiveButton("Enter CodeHunters", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //Dismiss dialog
                                        dialogInterface.dismiss();
                                        QRCodeMapper codeMapper = new QRCodeMapper();
                                        codeMapper.getAllCodes(codeMapper.new CompletionHandler<ArrayList<QRCodeData>>() {
                                            @Override
                                            public void handleSuccess(ArrayList<QRCodeData> codes) {
                                                ArrayList<QRCode> newCodes= new ArrayList<>();
                                                for (QRCodeData code : codes) {
                                                    newCodes.add(new QRCode(code));
                                                }
                                                openMap(newCodes);
                                            }
                                        });
                                    }
                                });
                                builder.show();
                            }

                            @Override
                            public void handleError(Exception e) {
                                // UUID not found in system.
                                AlertDialog.Builder builder = new AlertDialog.Builder(
                                        MainActivity.this);
                                // Set title
                                builder.setTitle("Login Err");
                                // Set message
                                builder.setMessage("failed to update user associated with that code!\n" +
                                        "Please try again later or verify login QR code\n\n" +
                                        "Continuing to use the app not logged in is not a supported experience.");
                                // Set positive button
                                builder.setPositiveButton("Enter CodeHunters", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //Dismiss dialog
                                        dialogInterface.dismiss();
                                    }
                                });
                                builder.show();
                            }
                        });
                    }

                    @Override
                    public void handleError(Exception e) {
                        // UUID not found in system.
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                MainActivity.this);
                        // Set title
                        builder.setTitle("Login Err");
                        // Set message
                        builder.setMessage("Could not find user with that login code. " +
                                "Please verify your login code.\n\n" +
                                "Continuing to use the app not logged in is not a supported experience.");
                        // Set positive button
                        builder.setPositiveButton("Enter CodeHunters", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Dismiss dialog
                                dialogInterface.dismiss();
                            }
                        });
                        builder.show();
                    }
                });

            } else if (requestCode == 3){
                if (intentResult==null)
                    return;

                String username = intentResult.getContents();
                UserMapper um = new UserMapper();
                um.queryUsername(username, um.new CompletionHandler<User>() {
                    @Override
                    public void handleSuccess(User data) {
                        // User with UUID found.
                        if (data!=null&&!TextUtils.isEmpty(data.getUsername())){
                            searchUser = data;
                            getSupportFragmentManager().beginTransaction()
                                    .setReorderingAllowed(true)
                                    .replace(R.id.fragment_container, SearchUserFragment.class, null)
                                    .addToBackStack("tag").commit();
                        }

                    }

                    @Override
                    public void handleError(Exception e) {
                        // UUID not found in system.
                        Toast.makeText(MainActivity.this, "User not exist", Toast.LENGTH_SHORT)
                                .show();
                    }
                });
            }
        }
    }


    public void updateCodeLists() {
        QRCodeMapper qrmapper = new QRCodeMapper();
        String UDID = Settings.Secure.getString(this.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        qrmapper.queryUsersCodes(UDID, qrmapper.new CompletionHandler<ArrayList<QRCodeData>>() {
            @Override
            public void handleSuccess(ArrayList<QRCodeData> data) {
                codeArrayList.clear();
                for (int i = 0; i < data.size(); i++) {
                    QRCode cur_code = new QRCode(data.get(i));
                    codeArrayList.add(cur_code);
                    if (codesFragment != null) {
                        codesFragment.notifyCodesAdapter();
                    }
                }
            }
        });
    }

    public void openMap(ArrayList<QRCode> codes) {
        MapFragment mapFragment = MapFragment.newInstance(codes);
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment_container, mapFragment)
                .commit();
    }

    public void qrDistance(ArrayList<QRCode> qrdistance) {

        if (ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location loc = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            double longi = loc.getLongitude();
            double lat = loc.getLatitude();
            for (int i = 0; i < qrdistance.size(); i++) {
                QRCode qrcode = new QRCode(qrdistance.get(i));
                double databaseCodeLat = qrcode.getLat();
                double databaseCodeLongi = qrcode.getLon();
                double latDistance = Math.abs(lat - databaseCodeLat);
                double longiDistance = Math.abs(longi - databaseCodeLongi);
                double qrManhattanDistance = latDistance + longiDistance;
            }

        }
    }

    // When items are clicked in the sidebar, move to the specified fragment.
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.my_account:
                // Navigate to the user's profile page
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new UserPersonalProfileFragment()).commit();
                break;
            case R.id.list_nearby_codes:
                // Navigate to the search nearby QR codes by geolocation fragment
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SearchNearbyCodesFragment()).commit();
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