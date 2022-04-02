package com.cmput301w22t36.codehunters;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * Turn to camera to scan the user generated login QR code
 */
public class ScanToLogin extends AppCompatActivity {
    // Initialize variables
    Button btscanlogin;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_scan);

        // Assign variable
        btscanlogin = findViewById(R.id.bt_scanlogin);

        btscanlogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initialize intent intergra
                IntentIntegrator intentIntegrator = new IntentIntegrator(
                        ScanToLogin.this);
                // Set prompt text
                intentIntegrator.setPrompt("Scan QR Code");
                // Locked orientation
                intentIntegrator.setOrientationLocked(true);
                // Set capture activity
                intentIntegrator.setCaptureActivity(Capture.class);
                // Initiate scan
                intentIntegrator.initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Initialize intent result
        IntentResult intentResult = IntentIntegrator.parseActivityResult(
                requestCode, resultCode, data
        );
        // Get content and store it in variable username
        username = intentResult.getContents();
        // Check condition
        if (username != null) {
            // When result content is not null
            // Initialize alert dialog
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

                    // TODO: return to the MapFragment.
//                    getSupportFragmentManager().beginTransaction()
//                                .setReorderingAllowed(true)
//                                .add(R.id.fragment_container, MapFragment.class, null)
//                                .commit();

                }
            });
            builder.show();
        } else {
            // When result content is null, a toast will shown
            Toast.makeText(getApplicationContext(), "You did not scan anything", Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
