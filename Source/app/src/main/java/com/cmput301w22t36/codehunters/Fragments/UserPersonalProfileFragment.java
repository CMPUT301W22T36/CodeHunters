package com.cmput301w22t36.codehunters.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cmput301w22t36.codehunters.R;

/**
 * Class: UserPersonalProfileFragment, a {@link Fragment} subclass.
 *
 * Load the set up user's personal profile fragment and display their information. Obtain any
 * username or email edits.
 *
 * Outstanding issues: currently not properly accessed in the tangible prototype.
 */
public class UserPersonalProfileFragment extends Fragment {

    // Initialize views to manage them within a fragment
    private TextView usernameView;
    private TextView userEmailView;

    private Button edit_name_button;
    private Button edit_email_button;
    private Button get_account_QR_button;
    private Button share_profile_button;

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

        // TODO: get the user information from the user.
        usernameView.setText("John Doe");
        userEmailView.setText("example@example.com");

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
                        .replace(R.id.mainActivityFragmentView, EditNameFragment.class, null)
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
                        .replace(R.id.mainActivityFragmentView, EditEmailFragment.class, null)
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
            }
        });
    }
}