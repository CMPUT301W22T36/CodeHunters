package com.cmput301w22t36.codehunters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
//Introduction: This fragment is used to show all status of searched users get from search.
//              It will get all data from online database, display the user stats, codes in common to user(player), and all his(searched user) codes
//              It can replace socialFragmentView in SocialFragment
//              just for  now: No actual functions but all display and navigation are completed.
import java.util.ArrayList;

public class SearchUserFragment extends Fragment {
    TextView searchedUserT;
    TextView searchedUser;
    TextView codesInCommonT;
    ListView codesInCommon;
    TextView allCodesT;
    ListView allCodes;
    ArrayList<String> CodesInCommon = new ArrayList<>();
    ArrayList<String> AllCodes = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public SearchUserFragment() {
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
    public static SearchUserFragment newInstance(String param1, String param2) {
        SearchUserFragment fragment = new SearchUserFragment();
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
        return inflater.inflate(R.layout.fragment_searcheduser, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CodesInCommon.add("code1");
        CodesInCommon.add("code2");
        CodesInCommon.add("code3");
        CodesInCommon.add("code4");
        CodesInCommon.add("code5");
        CodesInCommon.add("code6");
        AllCodes.add("code1");
        AllCodes.add("code2");
        AllCodes.add("code3");

        searchedUserT= view.findViewById(R.id.t1);
        searchedUser = view.findViewById(R.id.stats);
        codesInCommonT = view.findViewById(R.id.t2);
        codesInCommon = view.findViewById(R.id.codesInCommon);
        allCodesT = view.findViewById(R.id.t3);
        allCodes = view.findViewById(R.id.allCodes);



        ArrayAdapter arrayAdapter1 = new ArrayAdapter(this.getContext(), android.R.layout.simple_expandable_list_item_1,CodesInCommon);
        codesInCommon.setAdapter(arrayAdapter1);
        ArrayAdapter arrayAdapter2 = new ArrayAdapter(this.getContext(), android.R.layout.simple_expandable_list_item_1,AllCodes);
        allCodes.setAdapter(arrayAdapter2);

        //todo
        //get bestcodes, need get all data from database and do some sorts.



    }
}
