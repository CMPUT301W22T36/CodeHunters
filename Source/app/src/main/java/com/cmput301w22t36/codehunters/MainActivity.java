package com.cmput301w22t36.codehunters;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
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
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301w22t36.codehunters.Data.DataMapper;
import com.cmput301w22t36.codehunters.Data.DataMappers.QRCodeMapper;
import com.cmput301w22t36.codehunters.Data.DataMappers.UserMapper;
import com.cmput301w22t36.codehunters.Data.DataTypes.QRCodeData;
import com.cmput301w22t36.codehunters.Data.DataTypes.User;
import com.cmput301w22t36.codehunters.Fragments.CodesFragment;
import com.cmput301w22t36.codehunters.Fragments.FirstWelcomeFragment;
import com.cmput301w22t36.codehunters.Fragments.MapFragment;
import com.cmput301w22t36.codehunters.Fragments.SearchUserFragment;
import com.cmput301w22t36.codehunters.Fragments.SocialFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Class: MainActivity
 *
 * Load the main foundational fragment with a bottom navigation bar and call the start of the app.
 */
public class MainActivity extends AppCompatActivity {
    public  static MainActivity mainActivity;
    TextView codesNav, mapNav, socialNav;
    FloatingActionButton scanQRCode;
    ActivityResultLauncher<Intent> activityResultLauncher;
    public CodesFragment codesFragment;
    public User searchUser;
    //TEST - MEHUL (populate list of qrcodes to test listview)
    public ArrayList<QRCode> codeArrayList = new ArrayList<QRCode>();
    QRCode current_code;

    public User loggedinUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivity= this;
        // Go to the First Welcome Fragment to identify this device and CodeHunters account
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.mainActivityFragmentView, FirstWelcomeFragment.class, null)
                    .commit();
        }

        codesNav = findViewById(R.id.navToCodes);
        mapNav = findViewById(R.id.navToMap);
        socialNav = findViewById(R.id.navToSocial);

        updateCodeLists();

        // Swap to the CodesFragment when the "Codes" textview is clicked
        codesNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                codesFragment = CodesFragment.newInstance(codeArrayList);
                ft.replace(R.id.mainActivityFragmentView, codesFragment);
                ft.commit();
//                getSupportFragmentManager().beginTransaction()
//                        .setReorderingAllowed(true)
//                        .replace(R.id.mainActivityFragmentView, CodesFragment.class, null)
//                        .commit();
            }
        });

        // Swap to the SocialFragment when the "Social" textview is clicked
        socialNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fts = getSupportFragmentManager().beginTransaction();
                SocialFragment fragmentDemoS = SocialFragment.newInstance(loggedinUser.getUsername());
                fts.replace(R.id.mainActivityFragmentView,fragmentDemoS);
                fts.commit();
                /**
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.mainActivityFragmentView, SocialFragment.class, null)
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
                        ArrayList<QRCode> newCodes= new ArrayList<>();
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
                            cur_code.setLat(current_code.getGeolocation().get(0));
                            cur_code.setLon(current_code.getGeolocation().get(1));
                            cur_code.setScore(current_code.getScore());
                            cur_code.setPhoto(current_code.getPhoto());
                            cur_code.setUserRef("/users/"+loggedinUser.getId());
                            cur_code.setId(current_code.getId());

                            //TEST QRCODE CONSTRUCTOR
                            QRCode code1 = new QRCode(cur_code);

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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        IntentResult intentResult = IntentIntegrator.parseActivityResult(
//                requestCode,resultCode,data
//        );
        IntentResult intentResult = IntentIntegrator.parseActivityResult(
                resultCode,data
        );
        if (intentResult!= null) {
            if (requestCode == 1) {
                String QRString = intentResult.getContents();
                if (intentResult.getContents() != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            MainActivity.this
                    );
                    builder.setTitle("Result");
                    current_code = new QRCode(QRString);
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
                                    //User has said no to photo-location so we create code with string code only
                                    codeArrayList.add(current_code);
                                    QRCodeData cur_code = new QRCodeData();
                                    cur_code.setHash(current_code.getHash());
                                    cur_code.setScore(current_code.getScore());
                                    cur_code.setId(current_code.getId());
                                    cur_code.setUserRef("/users/"+loggedinUser.getId());

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
                            builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    activityResultLauncher.launch(intent);
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
                                        codeArrayList.add(current_code);
                                        //test
                                        QRCodeData cur_code = new QRCodeData();
                                        cur_code.setHash(current_code.getHash());
                                        cur_code.setId(current_code.getId());
                                        cur_code.setLat(current_code.getGeolocation().get(0));
                                        cur_code.setLon(current_code.getGeolocation().get(1));
                                        cur_code.setScore(current_code.getScore());
                                        cur_code.setUserRef("/users/"+loggedinUser.getId());

                                        QRCodeMapper qrmapper = new QRCodeMapper();
                                        qrmapper.create(cur_code, qrmapper.new CompletionHandler<QRCodeData>() {
                                            @Override
                                            public void handleSuccess(QRCodeData data) {
                                                updateCodeLists();
                                            }
                                        });
                                    } else {
                                        ActivityCompat.requestPermissions(MainActivity.this, new String[]
                                                {Manifest.permission.ACCESS_FINE_LOCATION},44);
                                        //codeArrayList.add(current_code);
                                        QRCodeData cur_code = new QRCodeData();
                                        cur_code.setHash(current_code.getHash());
                                        cur_code.setId(current_code.getId());
                                        cur_code.setId(current_code.getId());
                                        cur_code.setLat(current_code.getGeolocation().get(0));
                                        cur_code.setLon(current_code.getGeolocation().get(1));
                                        cur_code.setScore(current_code.getScore());
                                        cur_code.setUserRef("/users/"+loggedinUser.getId());

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
                            builder1.show();
                            //dialogInterface.dismiss();
                        }
                    });
                    builder.show();
                } else {
                    Toast.makeText(getApplicationContext(), "You did not scan anything", Toast.LENGTH_SHORT)
                            .show();
                }
            }
            else if (requestCode == 2){

                if (intentResult==null)
                    return;
                String username = intentResult.getContents();
                UserMapper um = new UserMapper();
                um.queryUsername(username, um.new CompletionHandler<User>() {
                    @Override
                    public void handleSuccess(User data) {
                        // User with UUID found.
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                MainActivity.this);
                        // Set title
                        builder.setTitle("Login Successful");
                        // Set message
                        builder.setMessage("Welcome! "+ username);
                        // Set positive button
                        builder.setPositiveButton("Enter CodeHunters", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Dismiss dialog
                                dialogInterface.dismiss();
                                MainActivity.mainActivity.toMap();

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
                        builder.setMessage("not find user! "+ username);
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
                                    .replace(R.id.mainActivityFragmentView, SearchUserFragment.class, null)
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

    public void toMap(){
        MapFragment mapFragment = MapFragment.newInstance(codeArrayList);
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.mainActivityFragmentView, mapFragment)
                .commit();
    }

    public void updateCodeLists() {
        QRCodeMapper qrmapper = new QRCodeMapper();
        String UDID = Settings.Secure.getString(this.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        qrmapper.queryUsersCodes(UDID, qrmapper.new CompletionHandler<ArrayList<QRCodeData>>() {
            @Override
            public void handleSuccess(ArrayList<QRCodeData> data) {
                codeArrayList.clear();
                for (int i=0; i<data.size(); i++) {
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
                .replace(R.id.mainActivityFragmentView, mapFragment)
                .commit();
    }
}