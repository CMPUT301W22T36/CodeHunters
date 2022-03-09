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

public class ScanToLogin extends AppCompatActivity {
    //Initialize variables
    Button btscanlogin;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_scan);

        btscanlogin = findViewById(R.id.bt_scanlogin);

        btscanlogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(
                        ScanToLogin.this);
                intentIntegrator.setPrompt("Scan QR Code");
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setCaptureActivity(Capture.class);
                intentIntegrator.initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(
                requestCode, resultCode, data
        );
        username = intentResult.getContents();
        if (username != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    ScanToLogin.this);
            builder.setTitle("Login Successful");
            builder.setMessage("Welcome! "+ username);
            builder.setPositiveButton("Enter CodeHunters", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.show();
        } else {
            Toast.makeText(getApplicationContext(), "You did not scan anything", Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
