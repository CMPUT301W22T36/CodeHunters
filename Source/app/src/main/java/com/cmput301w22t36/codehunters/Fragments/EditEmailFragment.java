package com.cmput301w22t36.codehunters.Fragments;

import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cmput301w22t36.codehunters.Data.DataMappers.UserMapper;
import com.cmput301w22t36.codehunters.Data.DataTypes.User;
import com.cmput301w22t36.codehunters.R;

/**
 * Class: EditEmailFragment, a {@link Fragment} subclass.
 *
 * Display the fragment to change the user's email and manage any edits to the email.
 */
public class EditEmailFragment extends Fragment {

    // Initialize views to manage them within a fragment
    private TextView editName;
    private EditText editEmail;
    private Button confirmChangeE;

    /**
     * Required empty public constructor
     */
    public EditEmailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EditEmailFragment.
     */
    public static EditEmailFragment newInstance() {
        EditEmailFragment fragment = new EditEmailFragment();
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
        return inflater.inflate(R.layout.fragment_edit_email, container, false);
    }

    /**
     * Display the static username and edit the email.
     * @param view: the current view
     * @param savedInstanceState: This is the bundle that will be called through the superclass
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Obtain the views within the fragment
        editName = (TextView) view.findViewById(R.id.editName);
        editEmail = (EditText)view.findViewById(R.id.editEmail);
        confirmChangeE = (Button)view.findViewById(R.id.confirmChangeE);

        String uuid = Settings.Secure.getString(getActivity().getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        // The username is static and not currently being edited. Display this username.
        UserMapper um = new UserMapper();
        /*um.queryUDID(uuid, um.new CompletionHandler<User>() {
            @Override
            public void handleSuccess(User data) {
                // Do something with returned user data.
                // This will (probably) execute after the FixedCase1 function returns.

                // Display the username
                String username = data.getUsername();
                editName.setText(username);
            }

            @Override
            public void handleError(Exception e) {
                // Handle the case where user not found.
            }
        });*/

        // Set the buttons to respond to user clicks and call their corresponding functions
        confirmChangeE.setOnClickListener(new View.OnClickListener() {
            /**
             * Confirm the new email
             * @param view: the current view
             */
            @Override
            public void onClick(View view) {
                // Obtain the new email
                String newEmail = editEmail.getText().toString();

                // Store the edited attributes
                UserMapper um = new UserMapper();
                um.queryUDID(uuid, um.new CompletionHandler<User>() {
                    @Override
                    public void handleSuccess(User data) {
                        // Do something with returned user data.
                        // This will (probably) execute after the FixedCase1 function returns.

                        // Store the new email
                        data.setEmail(newEmail);
                        um.update(data, um.new CompletionHandler<User>(){
                            @Override
                            public void handleSuccess(User data) {
                                // Do something with returned user data.
                                // This will (probably) execute after the FixedCase1 function returns.

                                // Return to the user profile
                                getParentFragmentManager().beginTransaction()
                                        .replace(R.id.mainActivityFragmentView, UserPersonalProfileFragment.class, null)
                                        .commit();
                            }

                            @Override
                            public void handleError(Exception e) {
                                // Handle the case where user not found.
                            }
                        });
                    }

                    @Override
                    public void handleError(Exception e) {
                        // Handle the case where user not found.
                    }
                });
            }
        });
    }
}