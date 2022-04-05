package com.cmput301w22t36.codehunters.Fragments;

import android.graphics.Bitmap;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.cmput301w22t36.codehunters.Data.DataMappers.UserMapper;
import com.cmput301w22t36.codehunters.Data.DataTypes.User;
import com.cmput301w22t36.codehunters.MainActivity;
import com.cmput301w22t36.codehunters.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

/**
 * Class: UserPersonalProfileFragment, a {@link Fragment} subclass.
 *
 * Load the set up user's personal profile fragment and display their information. Obtain any
 * username or email edits.
 */
public class UserPersonalProfileFragment extends Fragment {

    // Initialize views to manage them within a fragment
    private TextView usernameView;
    private TextView userEmailView;

    private Button edit_name_button;
    private Button edit_email_button;
    private Button get_account_QR_button;
    private Button share_profile_button;
    private String username;

    /**
     * Required empty public constructor
     */
    public UserPersonalProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EditEmailFragment.
     */
    public static UserPersonalProfileFragment newInstance() {
        UserPersonalProfileFragment fragment = new UserPersonalProfileFragment();
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
        return inflater.inflate(R.layout.user_personal_profile, container, false);
    }

    /**
     * Display the user profile, and respond to user requests to edit information or to obtain
     * the QR codes linked to their account.
     * @param view: the current view
     * @param savedInstanceState: This is the bundle that will be called through the superclass
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Obtain the views within the fragment
        usernameView = (TextView) view.findViewById(R.id.usernameView);
        userEmailView = (TextView) view.findViewById(R.id.userEmailView);

        edit_name_button = (Button)view.findViewById(R.id.edit_name_button);
        edit_email_button = (Button)view.findViewById(R.id.edit_email_button);
        get_account_QR_button = (Button)view.findViewById(R.id.get_account_QR_button);
        share_profile_button = (Button)view.findViewById(R.id.share_profile_button);

        // Display the user information for this device's account
        String uuid = Settings.Secure.getString(getActivity().getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        // Display the username and email.
        UserMapper um = new UserMapper();
        um.queryUDID(uuid, um.new CompletionHandler<User>() {
            @Override
            public void handleSuccess(User data) {
                // Do something with returned user data.
                // This will (probably) execute after the FixedCase1 function returns.

                // Display the username
                username = data.getUsername();
                String email = data.getEmail();
                usernameView.setText(username);
                userEmailView.setText(email);
            }

            @Override
            public void handleError(Exception e) {
                // Handle the case where user not found.
            }
        });

        // Set the buttons to respond to user clicks and call their corresponding functions
        edit_name_button.setOnClickListener(new View.OnClickListener() {
            /**
             * Prompt to edit the username
             * @param view: the current view
             */
            @Override
            public void onClick(View view) {
                // Move to the fragment to edit the username
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, EditNameFragment.class, null)
                        .commit();
            }
        });

        edit_email_button.setOnClickListener(new View.OnClickListener() {
            /**
             * Prompt to edit the profile email
             * @param view: the current view
             */
            @Override
            public void onClick(View view) {
                // Move to the fragment to edit the email for the users contact information
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, EditEmailFragment.class, null)
                        .commit();
            }
        });

        get_account_QR_button.setOnClickListener(new View.OnClickListener() {
            /**
             * Prompt to obtain the QR code allowing a user to login to their account from another device
             * @param view: the current view
             */
            @Override
            public void onClick(View view) {
                // TODO: Placeholder
                MultiFormatWriter writer = new MultiFormatWriter();
                try {
                    User loggedInUser = ((MainActivity) getActivity()).loggedinUser;
                    //Initialize bit matrix
                    BitMatrix matrix = writer.encode(loggedInUser.getId(), BarcodeFormat.QR_CODE,550,550);
                    //Initialize barcode encoder
                    BarcodeEncoder encoder = new BarcodeEncoder();
                    //Initialize bitmap
                    Bitmap bitmap = encoder.createBitmap(matrix);
                    //Set bitmap on image view
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            getActivity());
                    // Set title
                    ImageView imageView = new ImageView(getActivity());
                    imageView.setImageBitmap(bitmap);

                    builder.setView(imageView);
                    // Set positive button
                    builder.show();
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });

        share_profile_button.setOnClickListener(new View.OnClickListener() {
            /**
             * Prompt to obtain the QR code to share their game profile
             * @param view: the current view
             */
            @Override
            public void onClick(View view) {
                // TODO: Placeholder
                MultiFormatWriter writer = new MultiFormatWriter();
                try {
                    //Initialize bit matrix
                    BitMatrix matrix = writer.encode(username, BarcodeFormat.QR_CODE,550,550);
                    //Initialize barcode encoder
                    BarcodeEncoder encoder = new BarcodeEncoder();
                    //Initialize bitmap
                    Bitmap bitmap = encoder.createBitmap(matrix);
                    //Set bitmap on image view
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            getActivity());
                    // Set title
                    ImageView imageView = new ImageView(getActivity());
                    imageView.setImageBitmap(bitmap);

                    builder.setView(imageView);
                    // Set positive button
                    builder.show();
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}