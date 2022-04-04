package com.cmput301w22t36.codehunters.Fragments;

import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.cmput301w22t36.codehunters.Data.DataMappers.UserMapper;
import com.cmput301w22t36.codehunters.Data.DataTypes.User;
import com.cmput301w22t36.codehunters.R;

/**
 * Class: EditNameFragment, a {@link Fragment} subclass.
 *
 * Display the fragment to change the user's name and manage any edits to the username.
 */
public class EditNameFragment extends Fragment {

    // Initialize views to manage them within a fragment
    private EditText editName;
    private TextView editEmail;
    private Button confirmChangeN;

    /**
     * Required empty public constructor
     */
    public EditNameFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EditEmailFragment.
     */
    public static EditNameFragment newInstance() {
        EditNameFragment fragment = new EditNameFragment();
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
        return inflater.inflate(R.layout.fragment_edit_name, container, false);
    }

    /**
     * Display the static email and edit the username.
     * @param view: the current view
     * @param savedInstanceState: This is the bundle that will be called through the superclass
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Obtain the views within the fragment
        editName = (EditText)view.findViewById(R.id.editName);
        editEmail = (TextView)view.findViewById(R.id.editEmail);
        confirmChangeN = (Button)view.findViewById(R.id.confirmChangeN);

        String uuid = Settings.Secure.getString(getActivity().getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        // The email is static and not currently being edited. Display this email.
        UserMapper um = new UserMapper();
        um.queryUDID(uuid, um.new CompletionHandler<User>() {
            @Override
            public void handleSuccess(User data) {
                // Do something with returned user data.
                // This will (probably) execute after the FixedCase1 function returns.

                // Display the username
                String email = data.getEmail();
                editEmail.setText(email);
            }

            @Override
            public void handleError(Exception e) {
                // Handle the case where user not found.
            }
        });

        // Set the confirm button to respond to user clicks and call their corresponding functions
        confirmChangeN.setOnClickListener(new View.OnClickListener() {
            /**
             * Confirm the new username
             * @param view: the current view
             */
            @Override
            public void onClick(View view) {
                // Obtain the new name
                String username = editName.getText().toString();

                // Ensure the username is unique
                UserMapper um = new UserMapper();
                um.usernameUnique(username, um.new CompletionHandler<User>() {
                    @Override
                    public void handleSuccess(User data) {
                        // Name is unique. Store the edited attributes
                        UserMapper um = new UserMapper();
                        um.queryUDID(uuid, um.new CompletionHandler<User>() {
                            @Override
                            public void handleSuccess(User data) {
                                // Do something with returned user data.
                                // This will (probably) execute after the FixedCase1 function returns.

                                // Store the new name
                                data.setUsername(username);
                                um.update(data, um.new CompletionHandler<User>(){
                                    @Override
                                    public void handleSuccess(User data) {
                                        // Do something with returned user data.
                                        // This will (probably) execute after the FixedCase1 function returns.

                                        // Return to the user profile
                                        getParentFragmentManager().beginTransaction()
                                                .replace(R.id.fragment_container, UserPersonalProfileFragment.class, null)
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

                    @Override
                    public void handleError(Exception e) {
                        // Name entered is not unique. Prompt the user to enter a unique name
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Unique Username Required");
                        builder.setMessage("This username is unavailable, please try another username.");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            /**
                             * Dismiss the notification
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
                });
            }
        });
    }
}