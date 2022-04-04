package com.cmput301w22t36.codehunters;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.cmput301w22t36.codehunters.Data.DataMappers.QRCodeMapper;
import com.cmput301w22t36.codehunters.Capture;
import com.cmput301w22t36.codehunters.Data.DataMappers.UserMapper;
import com.cmput301w22t36.codehunters.Data.DataTypes.QRCodeData;
import com.cmput301w22t36.codehunters.Data.DataTypes.User;
import com.cmput301w22t36.codehunters.Fragments.MapFragment;
import com.cmput301w22t36.codehunters.Fragments.UserPersonalProfileFragment;
import com.cmput301w22t36.codehunters.MainActivity;
import com.cmput301w22t36.codehunters.QRCode;
import com.cmput301w22t36.codehunters.R;
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.ArrayList;

/**
 * Class: FirstWelcomeActivity
 *
 * Load the set up new account activity. Test if the device has an associated account, connect
 * the username to this device and game session, and proceed to the main game screen.
 */
public class FirstWelcomeActivity extends AppCompatActivity {
    // Initialize views to manage them within the activity
    private EditText editNameField;
    private EditText editEmailField;
    private Button confirmButton;
    private Button scanButton;

    // The user should not be able to exit this activity without having ensured the device has
    // connected to an account
    @Override
    public void onBackPressed() {
        // The back button should do nothing
    }

    /**                setResult(22, intent); // Use 22 to indicate that the ScanToLogin activity should be loaded

     * Test if this device already has an associated account, then either load that account or
     * prompt to setup a new account.
     * @param savedInstanceState: This is the bundle that will be called through the superclass
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_welcome_activity);

        // Obtain the device UUID
        /*
        Source attribution for the following line of code:
        Author: https://stackoverflow.com/users/923557/nino-van-hooff
        URL: https://stackoverflow.com/questions/31852653/making-getcontentresolver-to-work-in-class-extending-fragment-class
        StackOverflow: https://stackoverflow.com
        */
        String UDID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        // Query whether a UDID is associated with a user.
        UserMapper um = new UserMapper();
        um.queryUDID(UDID, um.new CompletionHandler<User>() {
            @Override
            public void handleSuccess(User data) {
                // User with UUID found.
                userFound(data);
            }

            @Override
            public void handleError(Exception e) {
                // UUID not found in system.
                userNotFound(UDID);
            }
        });
    }

    private void userFound(User user) {
        // If it is in the database, i.e. the user already has an account on this device, set the user and skip the login page
        MainActivity.mainActivity.loggedinUser = user;

        // Return to the main activity to the Map Fragment
        Intent intent = new Intent();
        intent.putExtra("ReturnToMapFrag", "tempMessage");
        setResult(12, intent); // Use 12 to indicate that the Map Fragment should be loaded
        finish();
    }

    private void userNotFound(String UDID) {
        // If the user does not yet have an account on this device, prompt them to set up their account

        // Obtain the views within the activity
        editNameField = (EditText) findViewById(R.id.usernameView);
        editEmailField = (EditText) findViewById(R.id.userEmailView);
        confirmButton = (Button)findViewById(R.id.confirm_button);
        scanButton = (Button)findViewById(R.id.scan_button);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Confirm the new account
             * @param view: the current view
             */
            @Override
            public void onClick(View view) {
                // Obtain the new email
                String username = editNameField.getText().toString();
                String email = editEmailField.getText().toString();
                storeAccount(username, email);
            }
        });

        // Set the buttons to respond to user clicks and call their corresponding functions
        scanButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Scan the account QR code
             * @param view: the current view
             */
            @Override
            public void onClick(View view) {
                // Return to the main activity and load the ScanToLogin activity
                Intent intent = new Intent();
                intent.putExtra("ReturnToScanToLogin", "tempMessage");
                setResult(22, intent); // Use 22 to indicate that the ScanToLogin activity should be loaded
                finish();
            }
        });
    }

    void storeAccount(String username, String email) {
        // Store the new account, and move to the main game activity
        UserMapper um = new UserMapper();
        um.usernameUnique(username, um.new CompletionHandler<User>() {
            @Override
            public void handleSuccess(User data) {
                // Name is unique. Attempt to store user.
                User user = new User();
                user.setUsername(username);
                user.setEmail(email);
                user.setScore(0);
                user.setScanCount(0);
                user.setBestScore(0);
                user.setOwner(false);
                String UDID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                user.appendUdid(UDID);

                UserMapper um = new UserMapper();
                um.create(user, um.new CompletionHandler<User>() {
                    @Override
                    public void handleSuccess(User data) {
                        // Successfully stored user data.
                        MainActivity.mainActivity.loggedinUser = data;

                        // Return to the main activity to the Map Fragment
                        Intent intent = new Intent();
                        intent.putExtra("ReturnToMapFrag", "tempMessage");
                        setResult(12, intent); // Use 12 to indicate that the Map Fragment should be loaded
                        finish();
                    }
                    @Override
                    public void handleError(Exception e) {
                        // Could not store user data.
                    }
                });
            }

            @Override
            public void handleError(Exception e) {
                // Name not unique, prompt the user to enter a unique name
                AlertDialog.Builder builder = new AlertDialog.Builder(FirstWelcomeActivity.this);
                builder.setTitle("Unique Username Required");
                builder.setMessage("This username is unavailable, please try another username.");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
                // remain within this activity as the user must choose another username
            }
        });
    }
}
