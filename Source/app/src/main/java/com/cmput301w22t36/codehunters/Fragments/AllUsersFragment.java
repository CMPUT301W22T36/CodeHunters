package com.cmput301w22t36.codehunters.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cmput301w22t36.codehunters.Adapters.UsersAdapter;
import com.cmput301w22t36.codehunters.Data.DataMappers.UserMapper;
import com.cmput301w22t36.codehunters.Data.DataTypes.User;
import com.cmput301w22t36.codehunters.R;
import com.cmput301w22t36.codehunters.TabSetter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllUsersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
//Introduction: This fragment is used to list all users name in game.
//              It will help user check other users' name and search .
public class AllUsersFragment extends Fragment {
    private TextView title;
    private ListView allUsers;
    private ArrayAdapter<User> userArrayAdapter;
    private ArrayList<User> userArrayList = new ArrayList<>();
    private Observable tabChanger = new TabSetter();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public AllUsersFragment() {
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
    public static AllUsersFragment newInstance(Observer thingWithTabs) {
        AllUsersFragment fragment = new AllUsersFragment();
        fragment.tabChanger.addObserver(thingWithTabs);
        Bundle args = new Bundle();
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
        return inflater.inflate(R.layout.fragment_all_users, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title = view.findViewById(R.id.allUsersT);
        allUsers = view.findViewById(R.id.allUsers);

        // all users (sorted by total score)
        UserMapper ums = new UserMapper();
        ums.usersOrderedBy("score", ums.new CompletionHandler<ArrayList<User>>() {
            @Override
            public void handleSuccess(ArrayList<User> AU) {
                Collections.reverse(AU);
                display(AU);
            }
        });

    }
    public void display(ArrayList<User> A){
        userArrayAdapter = new UsersAdapter(this.getContext(), A);
        allUsers.setAdapter(userArrayAdapter);
        userArrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        tabChanger.notifyObservers(1);
    }
}