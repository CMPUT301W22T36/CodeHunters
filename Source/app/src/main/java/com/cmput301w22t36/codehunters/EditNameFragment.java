package com.cmput301w22t36.codehunters;

import android.content.DialogInterface;
import android.os.Bundle;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditNameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditNameFragment extends Fragment {

    // Initialize views to manage them within a fragment
    private EditText editName;
    private TextView editEmail;
    private Button confirmChangeN;

    public EditNameFragment() {
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
    public static EditNameFragment newInstance(String param1, String param2) {
        EditNameFragment fragment = new EditNameFragment();
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
        return inflater.inflate(R.layout.fragment_edit_name, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Obtain the views within the fragment
        editName = (EditText)view.findViewById(R.id.editName);
        editEmail = (TextView)view.findViewById(R.id.editEmail);
        confirmChangeN = (Button)view.findViewById(R.id.confirmChangeN);

        // TODO: implementation with database from later user stories, currently a placeholder.
        // TODO: get email from user.
        // The email is static and not currently being edited.
        editEmail.setText("example@example.com");

        // Set the confirm button to respond to user clicks and call their corresponding functions
        confirmChangeN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtain the new name
                String message = editName.getText().toString();

                // TODO: implementation with database from later user stories, currently a placeholder.
                // TODO: ensure the username is unique! Search the database.
                boolean uniqueName = true;

                if (uniqueName) {
                    // Store the edited attributes
                    // TODO: implementation with database from later user stories, currently a placeholder.
                    // TODO: make the change to the user profile, ensure stored to database
                    User.setUsername(message);
                    UUIDPairing.setUsername(message);

                    // Return to the user profile
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.mainActivityFragmentView, UserPersonalProfileFragment.class, null)
                            .commit();
                } else {
                    // prompt the user to enter a unique name
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
            }
        });
    }
}