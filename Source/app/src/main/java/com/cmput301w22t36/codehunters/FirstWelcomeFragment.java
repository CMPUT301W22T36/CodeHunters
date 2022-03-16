package com.cmput301w22t36.codehunters;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.cmput301w22t36.codehunters.Data.DataTypes.User;

/**
 * Class: FirstWelcomeFragment, a {@link Fragment} subclass.
 *
 * Load the set up new account fragment. Test if the device has an associated account, connect
 * the username to this device and game session, and proceed to the main game screen.
 */
public class FirstWelcomeFragment extends Fragment {

    // Initialize views to manage them within a fragment
    private EditText editName;
    private EditText editEmail;
    private Button confirm;
    private Button scan;

    /**
     * Required empty public constructor
     */
    public FirstWelcomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FirstWelcomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FirstWelcomeFragment newInstance() {
        FirstWelcomeFragment fragment = new FirstWelcomeFragment();
        return fragment;
    }

    /**
     * This initializes the fragment
     * @param savedInstanceState: this is the bundle that will be called through the superclass
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * This inflates the fragment's layout
     * @param inflater: the LayoutInflator for the view
     * @param container: the ViewGroup of the view
     * @param savedInstanceState: this is the bundle that will be called through the superclass
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.first_welcome, container, false);
    }

    /**
     * Test if this device already has an associated account, then either load that account or
     * prompt to setup a new account.
     * @param view: the current view
     * @param savedInstanceState: This is the bundle that will be called through the superclass
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Obtain the device UUID
        /*
        Source attribution for the following line of code:
        Author: https://stackoverflow.com/users/923557/nino-van-hooff
        URL: https://stackoverflow.com/questions/31852653/making-getcontentresolver-to-work-in-class-extending-fragment-class
        StackOverflow: https://stackoverflow.com
        */
        String UUID_androidId = Settings.Secure.getString(getActivity().getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        // TODO: implementation with database from later user stories, currently a placeholder. Note, currently assumes this is a new device.
        // TODO: test if this device is already in the DB (via UUID)
        boolean knownDevice = false;

        // If it is in the database, i.e. the user already has an account on this device, set the user and skip the login page
        if (knownDevice) {
            // Set the user matching the UUID as the game player
            // TODO: implementation with database from later user stories, currently a placeholder.
            // TODO: obtain the username to match this UUID
            String username = "Test: John Doe";
            User.setUsername(username);

            // Move to the main game fragment
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.mainActivityFragmentView, MapFragment.class, null)
                    .commit();
        } else {
            // If the user does not yet have an account on this device, prompt them to set up their account

            // Obtain the views within the fragment
            editName = (EditText) view.findViewById(R.id.usernameView);
            editEmail = (EditText) view.findViewById(R.id.userEmailView);
            confirm = (Button)view.findViewById(R.id.confirm_button);
            scan = (Button)view.findViewById(R.id.scan_button);

            // Set the buttons to respond to user clicks and call their corresponding functions
            confirm.setOnClickListener(new View.OnClickListener() {
                /**
                 * Confirm the new account
                 * @param view: the current view
                 */
                @Override
                public void onClick(View view) {
                    // Store the new account, and move to the main game fragment

                    // Obtain the new email
                    String username = editName.getText().toString();
                    String email = editEmail.getText().toString();

                    // TODO: implementation with database from later user stories, currently a placeholder.
                    // TODO: ensure the username is unique! Search the database.
                    boolean uniqueName = false;

                    // TODO: remove this example!
                    if (true) {
                        // goto the profile fragment for demonstration purposes only!
                        getParentFragmentManager().beginTransaction()
                                .replace(R.id.mainActivityFragmentView, UserPersonalProfileFragment.class, null)
                                .commit();
                    } else if (uniqueName) {
                        // Store the attributes of the new account
                        // TODO: implementation with database from later user stories, currently a placeholder.
                        // TODO: make the change to the user profile, ensure stored to database
                        User.setUsername(username);
                        User.setEmail(email);
                        UUIDPairing.setUsername(username);
                        UUIDPairing.setUUID(UUID_androidId);

                        // goto the main game screen
                        getParentFragmentManager().beginTransaction()
                                .replace(R.id.mainActivityFragmentView, MapFragment.class, null)
                                .commit();
                    } else {
                        // prompt the user to enter a unique name
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Unique Username Required");
                        builder.setMessage("This username is unavailable, please try another username.");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            /**
                             * Confirm the new account
                             * @param dialogInterface: the current dialog interface
                             * @param i: button id of button clicked
                             */
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.show();

                        // remain within this fragment as the user must choose another username
                    }
                }
            });

            // Set the buttons to respond to user clicks and call their corresponding functions
            scan.setOnClickListener(new View.OnClickListener() {
                /**
                 * Scan the account QR code
                 * @param view: the current view
                 */
                @Override
                public void onClick(View view) {
                    // Move to the fragment to scan the QR code
                    // TODO: change to ScanToLogin fragment name, and once return goto MapFrag.
                    Intent myIntent = new Intent(getActivity().getApplicationContext(),ScanToLogin.class);
                    getActivity().startActivity(myIntent);
                }
            });
        }
    }
}