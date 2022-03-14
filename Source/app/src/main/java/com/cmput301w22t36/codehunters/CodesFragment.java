package com.cmput301w22t36.codehunters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CodesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CodesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private ArrayList<QRCode> codeArrayList;
    private TextView num_codes;
    private TextView total_score;

    public CodesFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static CodesFragment newInstance(ArrayList<QRCode> codes) {
        CodesFragment fragment = new CodesFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, codes);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            codeArrayList = (ArrayList<QRCode>) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_codes, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Connect to textviews for number of codes scanned and total score of codes
        num_codes = view.findViewById(R.id.total_codes);
        total_score = view.findViewById(R.id.total_score);

        //Populate number of codes and total score
        num_codes.setText(String.valueOf(codeArrayList.size()));
        int sum = 0;
        for (int i=0;i<codeArrayList.size();i++) {
            sum += codeArrayList.get(i).getScore();
        }
        total_score.setText(String.valueOf(sum));

        //Set up listview
        ListView codeList;
        ArrayAdapter<QRCode> codeArrayAdapter;

        codeList = view.findViewById(R.id.code_list);

        //Populate qrcode listview and connect to customlist
        codeArrayAdapter = new CustomQRCodeList(this.getContext(), codeArrayList);
        codeList.setAdapter(codeArrayAdapter);
    }
}