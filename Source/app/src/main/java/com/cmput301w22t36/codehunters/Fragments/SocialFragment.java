package com.cmput301w22t36.codehunters.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301w22t36.codehunters.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SocialFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

//Introduction: This fragment is used to contain and display all fragments related to users social communication in the App, also provide a search bottom for user to search other users.
//              It can replace fragment_container in MainActivity
//              just for  now: No actual functions but all display and navigation are completed.
public class SocialFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView bestCodesNav, scoreBoardNav;
    FloatingActionButton searchUser;
    AlertDialog dialogSearchUser;
    static Context context;

    public SocialFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SocialFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SocialFragment newInstance(String param1, String param2) {
        SocialFragment fragment = new SocialFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_social, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState == null) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.socialFragmentView, BestCodesFragment.class, null)
                    .commit();
        }
        bestCodesNav = view.findViewById(R.id.navToBestCodes);
        scoreBoardNav = view.findViewById(R.id.navToScoreBoard);
        searchUser = view.findViewById(R.id.searchUser);
        buildDialogSearchUser();

        // Swap to the BestCodesFragment when the "Bestcodes" textview is clicked
        bestCodesNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.socialFragmentView, BestCodesFragment.class, null)
                        .commit();
            }
        });

        // Swap to the SocialFragment when the "Scoreboard" textview is clicked
        scoreBoardNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.socialFragmentView, ScoreBoardFragment.class, null)
                        .commit();
            }
        });
        // Swap to the SearchUserFragment when the search bottom is clicked and enter a valid user name
        searchUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogSearchUser.show();
            }
        });

    }
    public void buildDialogSearchUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.dialog_searchuser, null);
        EditText username = view.findViewById(R.id.user_name_searchText);
        builder.setView(view)
                .setTitle("Search User")
                .setNegativeButton("Cancel",null)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if(!username.getText().toString().equals("")) {
                            if(true){
                                //todo
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .setReorderingAllowed(true)
                                        .replace(R.id.fragment_container, SearchUserFragment.class, null)
                                        .addToBackStack("tag").commit();
                                 }
                            else{Toast.makeText(getActivity(), "User not exist", Toast.LENGTH_SHORT)
                                    .show();}
                        }
                        else{Toast.makeText(getActivity(), "Please enter a user name.", Toast.LENGTH_SHORT)
                                .show();}
                    }
                });
        dialogSearchUser=builder.create();
    }

}