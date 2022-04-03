package com.cmput301w22t36.codehunters.Fragments;

import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cmput301w22t36.codehunters.Data.DataMappers.QRCodeMapper;
import com.cmput301w22t36.codehunters.Data.DataMappers.UserMapper;
import com.cmput301w22t36.codehunters.Data.DataTypes.QRCodeData;
import com.cmput301w22t36.codehunters.Data.DataTypes.User;
import com.cmput301w22t36.codehunters.MainActivity;
import com.cmput301w22t36.codehunters.QRCode;
import com.cmput301w22t36.codehunters.QRCodeAdapter;
import com.cmput301w22t36.codehunters.R;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BestCodesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
//Introduction: This fragment is used to calculate and get codes with best scores.
//              It will get all codes from online database, sort them and display them from high score to low.
//              It can replace socialFragmentView in SocialFragment
//              just for  now: No actual functions but all display and navigation are completed.
public class AllUsersFragment extends Fragment {
    TextView title;
    ListView allusers;
    private ArrayAdapter<User> userArrayAdapter;
    private ArrayList<User> userArrayList = new ArrayList<>() ;

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
    public static AllUsersFragment newInstance(String param1, String param2) {
        AllUsersFragment fragment = new AllUsersFragment();
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
        return inflater.inflate(R.layout.fragment_all_users, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title = view.findViewById(R.id.allUsersT);
        allusers = view.findViewById(R.id.allUsers);

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
        //userArrayAdapter = new UserAdapter(this.getContext(), A);
        //allusers.setAdapter(userArrayAdapter);
    }
}