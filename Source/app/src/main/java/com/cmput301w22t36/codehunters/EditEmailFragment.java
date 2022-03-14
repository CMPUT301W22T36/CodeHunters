package com.cmput301w22t36.codehunters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditEmailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditEmailFragment extends Fragment {

    // Initialize views to manage them within a fragment
    private TextView editName;
    private EditText editEmail;
    private Button confirmChangeE;

    public EditEmailFragment() {
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
    public static EditEmailFragment newInstance(String param1, String param2) {
        EditEmailFragment fragment = new EditEmailFragment();
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
        return inflater.inflate(R.layout.fragment_edit_email, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Obtain the views within the fragment
        editName = (TextView) view.findViewById(R.id.editName);
        editEmail = (EditText)view.findViewById(R.id.editEmail);
        confirmChangeE = (Button)view.findViewById(R.id.confirmChangeE);

        // TODO: implementation with database from later user stories, currently a placeholder.
        // TODO: get username from the user.
        // The username is static and not currently being edited.
        editName.setText("John Doe");

        // Set the buttons to respond to user clicks and call their corresponding functions
        confirmChangeE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtain the new email
                String message = editEmail.getText().toString();

                // Store the edited attributes
                // TODO: implementation with database from later user stories, currently a placeholder.
                // TODO: make the change to the user profile, ensure stored to database
                User.setEmail(message);

                // Return to the user profile
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.mainActivityFragmentView, UserPersonalProfileFragment.class, null)
                        .commit();
            }
        });
    }
}