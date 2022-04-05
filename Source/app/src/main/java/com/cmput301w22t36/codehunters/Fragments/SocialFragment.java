package com.cmput301w22t36.codehunters.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301w22t36.codehunters.Capture;
import com.cmput301w22t36.codehunters.Data.DataMappers.UserMapper;
import com.cmput301w22t36.codehunters.Data.DataTypes.User;
import com.cmput301w22t36.codehunters.MainActivity;
import com.cmput301w22t36.codehunters.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.Observable;
import java.util.Observer;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SocialFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

//Introduction: This fragment is used to contain and display all fragments related to users social communication in the App, also provide a search bottom for user to search other users.
//              It can replace fragment_container in MainActivity
//              It get username from MainActivity
public class SocialFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private Observer tabObserver;


    private String username;
    private TextView bestCodesNav, scoreBoardNav, allUsersNav;
    private FloatingActionButton searchUser;
    private AlertDialog dialogSearchUser;
    static Context context;

    public SocialFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param name
     * @return A new instance of fragment SocialFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SocialFragment newInstance(String name) {
        SocialFragment fragment = new SocialFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tabObserver = new TabObserver();
        if (getArguments() != null) {
            username = (String) getArguments().getString(ARG_PARAM1);

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
            BestCodesFragment bestCodesFragment = BestCodesFragment.newInstance(tabObserver);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    //.add(R.id.socialFragmentView, ScoreBoardFragment.class, null)
                    .add(R.id.socialFragmentView, bestCodesFragment, null)
                    .commit();
        }
        bestCodesNav = view.findViewById(R.id.navToBestCodes);
        scoreBoardNav = view.findViewById(R.id.navToScoreBoard);
        allUsersNav = view.findViewById(R.id.navToAllUsers);
        searchUser = view.findViewById(R.id.searchUser);
        buildDialogSearchUser();

        // Swap to the BestCodesFragment when the "Bestcodes" textview is clicked
        bestCodesNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BestCodesFragment bestCodesFragment = BestCodesFragment.newInstance(tabObserver);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.socialFragmentView, bestCodesFragment, null)
                        .commit();
            }
        });

        allUsersNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllUsersFragment allUsersFragment = AllUsersFragment.newInstance(tabObserver);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.socialFragmentView, allUsersFragment, null)
                        .commit();
            }
        });

        // Swap to the SocialFragment when the "Scoreboard" textview is clicked
        scoreBoardNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft2 = getActivity().getSupportFragmentManager().beginTransaction();
                ScoreBoardFragment fragmentDemo2 = ScoreBoardFragment.newInstance(username, tabObserver);
                ft2.replace(R.id.socialFragmentView,fragmentDemo2);
                ft2.commit();

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

    public void setDis(){
        dialogSearchUser.dismiss();
    }
    public void buildDialogSearchUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.dialog_searchuser, null);
        EditText username = view.findViewById(R.id.user_name_searchText);
        ImageView imageView = view.findViewById(R.id.image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(
                        getActivity()
                );
                intentIntegrator.setBeepEnabled(false);
                intentIntegrator.setPrompt("Scan QR Code");
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setCaptureActivity(Capture.class);
                intentIntegrator.setRequestCode(3);
                intentIntegrator.initiateScan();
                setDis();
            }
        });
        builder.setView(view)
                .setTitle("Search User")
                .setNegativeButton("Cancel",null)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if(!username.getText().toString().equals("")) {

                            UserMapper um = new UserMapper();
                            um.queryUsername(username.getText().toString(), um.new CompletionHandler<User>() {
                                @Override
                                public void handleSuccess(User data) {
                                    // Successfully stored user data.
                                    // goto the main game screen
                                    if (data!=null){

                                        MainActivity.mainActivity.searchUser = data;

                                        getActivity().getSupportFragmentManager().beginTransaction()
                                                .setReorderingAllowed(true)
                                                .replace(R.id.fragment_container, SearchUserFragment.class, null)
                                                .addToBackStack("tag").commit();
                                    }else {
                                        Toast.makeText(getActivity(), data.getUsername()+"", Toast.LENGTH_SHORT)
                                                .show();
                                    }

                                }
                                @Override
                                public void handleError(Exception e) {
                                    // Could not store user data.
                                    Toast.makeText(getActivity(), "User not exist", Toast.LENGTH_SHORT)
                                            .show();
                                }
                            });
                        }
                        else{Toast.makeText(getActivity(), "Please enter a user name.", Toast.LENGTH_SHORT)
                                .show();}
                    }
                });
        dialogSearchUser=builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).updateNavBar(2);
    }

    private class TabObserver implements Observer {
        public void update(Observable activeFragment, Object arg) {
            int index = (int) arg;
            if (index == 0) {
                bestCodesNav.setBackgroundColor(Color.parseColor("#9C27B0"));
                allUsersNav.setBackgroundColor(Color.parseColor("#e6ccff"));
                scoreBoardNav.setBackgroundColor(Color.parseColor("#e6ccff"));
            } else if (index == 1) {
                bestCodesNav.setBackgroundColor(Color.parseColor("#e6ccff"));
                allUsersNav.setBackgroundColor(Color.parseColor("#9C27B0"));
                scoreBoardNav.setBackgroundColor(Color.parseColor("#e6ccff"));
            } else if (index == 2) {
                bestCodesNav.setBackgroundColor(Color.parseColor("#e6ccff"));
                allUsersNav.setBackgroundColor(Color.parseColor("#e6ccff"));
                scoreBoardNav.setBackgroundColor(Color.parseColor("#9C27B0"));
            }
        }
    }

}