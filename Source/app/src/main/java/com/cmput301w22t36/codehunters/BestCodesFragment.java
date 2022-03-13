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

import java.util.ArrayList;

public class BestCodesFragment extends Fragment {
    TextView title;
    ListView bestcodes;
    ArrayList<String> BestCodes = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public BestCodesFragment() {
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
    public static BestCodesFragment newInstance(String param1, String param2) {
        BestCodesFragment fragment = new BestCodesFragment();
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
        return inflater.inflate(R.layout.fragment_bestcodes, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //just for test, you may find if you get back from search user, the add will double.But in really implements,
        // i wont add anything
        BestCodes.add("code1");
        BestCodes.add("code2");
        BestCodes.add("code3");

        title = view.findViewById(R.id.bestCodesT);
        bestcodes = view.findViewById(R.id.bestCodes);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this.getContext(), android.R.layout.simple_expandable_list_item_1,BestCodes);
        bestcodes.setAdapter(arrayAdapter);

        //todo
        //get bestcodes, need get all data from database and do some sorts.



    }
}
