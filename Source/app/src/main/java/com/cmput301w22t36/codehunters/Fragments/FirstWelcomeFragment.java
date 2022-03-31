package com.cmput301w22t36.codehunters.Fragments;

import android.content.DialogInterface;
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


import com.cmput301w22t36.codehunters.Capture;
import com.cmput301w22t36.codehunters.Data.DataMappers.QRCodeMapper;
import com.cmput301w22t36.codehunters.Data.DataMappers.UserMapper;
import com.cmput301w22t36.codehunters.Data.DataTypes.QRCodeData;
import com.cmput301w22t36.codehunters.Data.DataTypes.User;
import com.cmput301w22t36.codehunters.MainActivity;
import com.cmput301w22t36.codehunters.QRCode;
import com.cmput301w22t36.codehunters.R;
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.ArrayList;

/**
 * Class: FirstWelcomeFragment, a {@link Fragment} subclass.
 *
 * Load the set up new account fragment. Test if the device has an associated account, connect
 * the username to this device and game session, and proceed to the main game screen.
 */
public class FirstWelcomeFragment extends Fragment {
    // Initialize views to manage them within a fragment
    private EditText editNameField;
    private EditText editEmailField;
    private Button confirmButton;
    private Button scanButton;

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
        String UDID = Settings.Secure.getString(getActivity().getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        // Query whether a UDID is associated with a user.
        UserMapper um = new UserMapper();
        um.queryUDID(UDID, um.new CompletionHandler<User>() {
            @Override
            public void handleSuccess(User data) {
                // User with UUID found.
                userFound(data, view);
            }

            @Override
            public void handleError(Exception e) {
                // UUID not found in system.
                userNotFound(UDID, view);
            }
        });
    }

    private void userFound(User user, View view) {
        // If it is in the database, i.e. the user already has an account on this device, set the user and skip the login page
        ((MainActivity) getActivity()).loggedinUser = user;

        QRCodeMapper codeMapper = new QRCodeMapper();
        codeMapper.getAllCodes(codeMapper.new CompletionHandler<ArrayList<QRCodeData>>() {
            @Override
            public void handleSuccess(ArrayList<QRCodeData> codes) {
                ArrayList<QRCode> newCodes= new ArrayList<>();
                for (QRCodeData code : codes) {
                    newCodes.add(new QRCode(code));
                }
                ((MainActivity) getActivity()).openMap(newCodes);
            }
        });
    }

    private void userNotFound(String UDID, View view) {
        // If the user does not yet have an account on this device, prompt them to set up their account

        // Obtain the views within the fragment
        editNameField = (EditText) view.findViewById(R.id.usernameView);
        editEmailField = (EditText) view.findViewById(R.id.userEmailView);
        confirmButton = (Button)view.findViewById(R.id.confirm_button);
        scanButton = (Button)view.findViewById(R.id.scan_button);

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
                // Move to the fragment to scan the QR code
                // TODO: change to ScanToLogin fragment name, and once return goto MapFrag.
                IntentIntegrator intentIntegrator = new IntentIntegrator(
                        getActivity()
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


    void storeAccount(String username, String email) {
        // Store the new account, and move to the main game fragment
        UserMapper um = new UserMapper();
        um.usernameUnique(username, um.new CompletionHandler<User>() {
            @Override
            public void handleSuccess(User data) {
                // Name is unique. Attempt to store user.
                User user = new User();
                user.setUsername(username);
                user.setEmail(email);
                String UDID = Settings.Secure.getString(getActivity().getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                user.appendUdid(UDID);

                UserMapper um = new UserMapper();
                um.create(user, um.new CompletionHandler<User>() {
                    @Override
                    public void handleSuccess(User data) {
                        // Successfully stored user data.
                        // goto the main game screen
                        ((MainActivity) getActivity()).loggedinUser = user;
                        getParentFragmentManager().beginTransaction()
                                .replace(R.id.mainActivityFragmentView, UserPersonalProfileFragment.class, null)
                                .commit();
                    }
                    @Override
                    public void handleError(Exception e) {
                        // Could not store user data.

                    }
                });

            }

            @Override
            public void handleError(Exception e) {
                // Name not unique.
                // prompt the user to enter a unique name
                // TODO: Needs to be tested.
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Unique Username Required");
                builder.setMessage("This username is unavailable, please try another username.");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
                // remain within this fragment as the user must choose another username
            }
        });

    }
}