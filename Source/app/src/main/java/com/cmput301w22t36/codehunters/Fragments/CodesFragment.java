package com.cmput301w22t36.codehunters.Fragments;

import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.ColorInt;
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

import com.cmput301w22t36.codehunters.Data.DataMappers.QRCodeMapper;
import com.cmput301w22t36.codehunters.Data.DataTypes.QRCodeData;
import com.cmput301w22t36.codehunters.MainActivity;
import com.cmput301w22t36.codehunters.QRCode;
import com.cmput301w22t36.codehunters.Adapters.QRCodeAdapter;
import com.cmput301w22t36.codehunters.R;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Introductory Comments:
 *      This Java file is a fragment representing the "codes" tab of our application. The fragment
 *      houses a custom ListView of a user's QRCode objects displaying the relevant information from
 *      each code (Code (String) & Score (int)) as well as the user's total codes scanned and total score.
 *      The fragment also allows for users to sort their codes by highest or lowest score (done by
 *      long-pressing "sort by" TextView and selecting sorting method from pop-up menu).
 *
 *      **Outstanding issue: Searchbar is present in the layout but does not currently have any functionality**
 */

/**
 * Fragment responsible for displaying user's scanned QRCodes and relevant information.
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
    private TextView sort_method_txt;
    //Set up listview
    private ListView codeList;
    private ArrayAdapter<QRCode> codeArrayAdapter;

    private StatsUpdater statsUpdater;

    /**
     * Required constructor
     */
    public CodesFragment() {
        // Required empty public constructor
    }

    /**
     * On creation of a newinstance of this fragment, a list of QRCodes are passed in
     * @param codes
     * @return
     */
    // TODO: Rename and change types and number of parameters
    public static CodesFragment newInstance(ArrayList<QRCode> codes) {
        CodesFragment fragment = new CodesFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, codes);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * On creation of the fragment, the bundle housing the list of QRCodes is received so they can be displayed later
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            codeArrayList = (ArrayList<QRCode>) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    /**
     * Connect to xml file associated to this fragment and initialize layout
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_codes, container, false);

    }

    /**
     * Once the fragment is created, we connect to the associated views in the layout file and populate them.
     *      (ex. populate the ListView of QRCodes with the arraylist of QRCodes)
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Connect to views from xml file
        num_codes = view.findViewById(R.id.total_codes);
        total_score = view.findViewById(R.id.total_score);
        sort_method = view.findViewById(R.id.sort_by);
        sort_method_txt= view.findViewById(R.id.sort_method);
        codeList = view.findViewById(R.id.code_list);
        statsUpdater = new StatsUpdater();

        //Populate number of codes and total score
        num_codes.setText(String.valueOf(codeArrayList.size()));
        int sum = 0;
        for (int i=0;i<codeArrayList.size();i++) {
            sum += codeArrayList.get(i).getScore();
        }
        total_score.setText(String.valueOf(sum));

        //Populate qrcode listview and connect to customlist
        codeArrayAdapter = new QRCodeAdapter(this.getContext(), codeArrayList);
        codeList.setAdapter(codeArrayAdapter);
        codeArrayAdapter.registerDataSetObserver(statsUpdater);



        codeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * When a QRCode in the ListView is clicked, a dialog fragment will appear with the code's photo and location
             * @param adapterView
             * @param view
             * @param i
             * @param l
             */
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                QRCode qrCodeClicked = (QRCode) codeList.getItemAtPosition(i);
                new Geolocation_PhotosFragment(qrCodeClicked).show(getActivity().getSupportFragmentManager(), "ADD_GEO");

            }
        });

        //SORT BY FEATURE //Context menu setup -- registering "sort-by" TextView
        registerForContextMenu(sort_method);
        registerForContextMenu(codeList);
    }

    /**
     * On creation of the context menu, we add options, in this case the two sorting options are by score (Highest to lowest & Lowest to highest)
     * @param menu
     * @param v
     * @param menuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //Add sort options -- Highest/Lowest Scoring
        if (v == codeList) {
            menu.add(0, v.getId(), 0, "Delete");
        } else {
            menu.add(0, v.getId(), 0, "Highest Score");
            menu.add(0, v.getId(), 0, "Lowest Score");
        }
    }

    /**
     * Add functionality to sorting methods in menu, sort by descending (based on score) for highest score sorting
     * and ascending (based on score) for lowest score sorting
     * @param item
     * @return
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "Highest Score") {
            //Sort codes by score - Descending order
            Collections.sort(codeArrayList, Collections.reverseOrder());
            codeArrayAdapter.notifyDataSetChanged();
            //Toast pop-up to confirm with user their selection
            sort_method_txt.setTextColor(Color.parseColor("#333333"));
            sort_method_txt.setText("Highest Score");
            Toast.makeText(this.getContext(), "Sort Method Selected: " +item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        else if (item.getTitle() == "Lowest Score") {
            //Sort codes by score - Ascending order
            Collections.sort(codeArrayList);
            codeArrayAdapter.notifyDataSetChanged();
            //Toast pop-up to confirm with user their selection
            sort_method_txt.setTextColor(Color.parseColor("#333333"));
            sort_method_txt.setText("Lowest Score");
            Toast.makeText(this.getContext(), "Sort Method Selected: " +item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        else if (item.getTitle() == "Delete") {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            //Gets session that was long-pressed
            QRCode selected_code = (QRCode) codeList.getItemAtPosition(info.position);
            QRCodeData cur_code = new QRCodeData();
            cur_code.setHash(selected_code.getHash());
            cur_code.setScore(selected_code.getScore());
            cur_code.setId(selected_code.getId());
            if(selected_code.hasLocation()) {
                cur_code.setLat(selected_code.getGeolocation().get(0));
                cur_code.setLon(selected_code.getGeolocation().get(1));
            }
            if(selected_code.getPhoto() != null) {
                cur_code.setPhoto(selected_code.getPhoto());
            }
            codeArrayList.remove(selected_code);
            codeArrayAdapter.notifyDataSetChanged();
            QRCodeMapper qrmapper = new QRCodeMapper();
            qrmapper.delete(cur_code, qrmapper.new CompletionHandler<QRCodeData>() {
                @Override
                public void handleSuccess(QRCodeData data) {
                    ((MainActivity)getActivity()).updateUsersScore();
                }
            });
            num_codes.setText(String.valueOf(codeArrayList.size()));
            Integer totalscore = Integer.valueOf((String) total_score.getText());
            totalscore = totalscore - selected_code.getScore();
            total_score.setText(String.valueOf(totalscore));
            codeArrayAdapter.notifyDataSetChanged();
        }
        return true;
    }

    public void notifyCodesAdapter() {
        // Gets called from a different thread.
        if (codeArrayAdapter != null) {
            codeArrayAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).updateNavBar(0);
    }

    private class StatsUpdater extends DataSetObserver {
        @Override
        public void onChanged() {
            super.onChanged();
            int totalScore = 0;
            for (QRCode code : codeArrayList) {
                totalScore += code.getScore();
            }
            total_score.setText(String.valueOf(totalScore));
            num_codes.setText(String.valueOf(codeArrayList.size()));
        }
    }
}