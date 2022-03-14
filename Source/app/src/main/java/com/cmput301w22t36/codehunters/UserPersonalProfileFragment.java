package com.cmput301w22t36.codehunters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserPersonalProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserPersonalProfileFragment extends Fragment {

    // Initialize views to manage them within a fragment
    private TextView usernameView;
    private TextView userEmailView;

    private Button edit_name_button;
    private Button edit_email_button;
    private Button get_account_QR_button;
    private Button share_profile_button;

    public UserPersonalProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserPersonalProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserPersonalProfileFragment newInstance(String param1, String param2) {
        UserPersonalProfileFragment fragment = new UserPersonalProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.user_personal_profile, container, false);
    }

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
            @Override
            public void onClick(View view) {
                // Move to the fragment to edit the username
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.mainActivityFragmentView, EditNameFragment.class, null)
                        .commit();
            }
        });
        edit_email_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Move to the fragment to edit the email for the users contact information
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.mainActivityFragmentView, EditEmailFragment.class, null)
                        .commit();
            }
        });
        get_account_QR_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Placeholder
            }
        });
        share_profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Placeholder
            }
        });
    }
}