package com.cmput301w22t36.codehunters.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cmput301w22t36.codehunters.Adapters.UsersAdapter;
import com.cmput301w22t36.codehunters.Data.DataMappers.UserMapper;
import com.cmput301w22t36.codehunters.Data.DataTypes.User;
import com.cmput301w22t36.codehunters.MainActivity;
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
//              Users are clickable to show the SearchUser fragment for details
public class AllUsersFragment extends Fragment {
    private ListView allUsers;
    private ArrayAdapter<User> userArrayAdapter;
    private final Observable tabChanger = new TabSetter();

    public AllUsersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SocialFragment.
     */
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
    /**
     * display all users get from database
     * @param A
     */
    public void display(ArrayList<User> A){
        // Gets called from different thread, needs to do null checks.
        Context context = this.getContext();
        if (context != null) {
            userArrayAdapter = new UsersAdapter(context, A);
            allUsers.setAdapter(userArrayAdapter);
            userArrayAdapter.notifyDataSetChanged();

            allUsers.setOnItemClickListener((adapterView, view, i, l) -> {
                MainActivity.mainActivity.searchUser = userArrayAdapter.getItem(i);

                SearchUserFragment searchUserFragment = SearchUserFragment.newInstance();
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragment_container, searchUserFragment, null)
                            .addToBackStack("tag").commit();
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        tabChanger.notifyObservers(1);
    }
}