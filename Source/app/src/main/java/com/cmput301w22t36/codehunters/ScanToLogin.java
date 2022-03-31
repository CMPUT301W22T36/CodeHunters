package com.cmput301w22t36.codehunters;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.cmput301w22t36.codehunters.Data.DataMappers.UserMapper;
import com.cmput301w22t36.codehunters.Data.DataTypes.User;
import com.cmput301w22t36.codehunters.Fragments.MapFragment;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

/**
 * Turn to camera to scan the user generated login QR code
 */
public class ScanToLogin extends AppCompatActivity {
    // Initialize variables
    Button btscanlogin;
    String username;
    ArrayList<QRCode> codeArrayList = new ArrayList<QRCode>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_scan);

        // Assign variable
        btscanlogin = findViewById(R.id.bt_scanlogin);

        btscanlogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(
                        ScanToLogin.this
                );
                intentIntegrator.setBeepEnabled(false);
                intentIntegrator.setPrompt("Scan QR Code");
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setCaptureActivity(Capture.class);
                intentIntegrator.setRequestCode(2);
                intentIntegrator.initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Initialize intent result
        {
            super.onActivityResult(requestCode, resultCode, data);
            // Initialize intent result
            IntentResult intentResult = IntentIntegrator.parseActivityResult(
                    requestCode, resultCode, data
            );
            if (requestCode == 2&&intentResult!=null){
                String username = intentResult.getContents();

                Toast.makeText(getApplicationContext(), TextUtils.isEmpty(username)?"empty data":username, Toast.LENGTH_SHORT)
                        .show();

                // Check condition
                if (!TextUtils.isEmpty(username)) {
                    // When result content is not null
                    // Initialize alert dialog

                    UserMapper um = new UserMapper();
                    um.queryUsername(username, um.new CompletionHandler<User>() {
                        @Override
                        public void handleSuccess(User data) {
                            // User with UUID found.
                            AlertDialog.Builder builder = new AlertDialog.Builder(
                                    ScanToLogin.this);
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
                                    finish();

                                }
                            });
                            builder.show();
                        }

                        @Override
                        public void handleError(Exception e) {
                            // UUID not found in system.
                            AlertDialog.Builder builder = new AlertDialog.Builder(
                                    ScanToLogin.this);
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


                } else {
                    // When result content is null, a toast will shown
                    Toast.makeText(getApplicationContext(), "You did not scan anything", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        }
    }
}
