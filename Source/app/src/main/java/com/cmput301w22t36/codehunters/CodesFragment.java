package com.cmput301w22t36.codehunters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301w22t36.codehunters.Data.Geolocation_PhotosFragment;

import java.util.ArrayList;
import java.util.Collections;

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
    private TextView sort_method;
    //Set up listview
    private ListView codeList;
    private ArrayAdapter<QRCode> codeArrayAdapter;

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
        //Connect to views from xml file
        num_codes = view.findViewById(R.id.total_codes);
        total_score = view.findViewById(R.id.total_score);
        sort_method = view.findViewById(R.id.sort_by);
        codeList = view.findViewById(R.id.code_list);

        //Populate number of codes and total score
        num_codes.setText(String.valueOf(codeArrayList.size()));
        int sum = 0;
        for (int i=0;i<codeArrayList.size();i++) {
            sum += codeArrayList.get(i).getScore();
        }
        total_score.setText(String.valueOf(sum));


        //Populate qrcode listview and connect to customlist
        codeArrayAdapter = new CustomQRCodeList(this.getContext(), codeArrayList);
        codeList.setAdapter(codeArrayAdapter);

        codeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                QRCode qrCodeClicked = (QRCode) codeList.getItemAtPosition(i);
                new Geolocation_PhotosFragment(qrCodeClicked).show(getActivity().getSupportFragmentManager(), "ADD_GEO");

            }
        });

        //SORT BY FEATURE //Context menu setup -- registering "sort-by" TextView
        registerForContextMenu(sort_method);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //Add sort options -- Highest/Lowest Scoring
        menu.add(0, v.getId(), 0, "Highest Score");
        menu.add(0, v.getId(), 0, "Lowest Score");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "Highest Score") {
            //Sort codes by score - Descending order
            Collections.sort(codeArrayList, Collections.reverseOrder());
            codeArrayAdapter.notifyDataSetChanged();
        }
        else if (item.getTitle()== "Lowest Score") {
            //Sort codes by score - Ascending order
            Collections.sort(codeArrayList);
            codeArrayAdapter.notifyDataSetChanged();
        }
        //Toast pop-up to confirm with user their selection
        Toast.makeText(this.getContext(), "Sort Method Selected: " +item.getTitle(), Toast.LENGTH_SHORT).show();
        return true;
    }
}