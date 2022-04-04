package com.cmput301w22t36.codehunters.Fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cmput301w22t36.codehunters.Data.DataMappers.QRCodeMapper;
import com.cmput301w22t36.codehunters.Data.DataTypes.QRCodeData;
import com.cmput301w22t36.codehunters.QRCode;
import com.cmput301w22t36.codehunters.Adapters.QRCodeAdapter;
import com.cmput301w22t36.codehunters.R;
import com.cmput301w22t36.codehunters.TabSetter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BestCodesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
//Introduction: This fragment is used to calculate and get codes with best scores.
//              It will get all codes from online database, sort them and display them from high score to low.
//              It can replace socialFragmentView in SocialFragment
//              just for  now: No actual functions but all display and navigation are completed.
public class BestCodesFragment extends Fragment {
    TextView title;
    ListView bestcodes;
    private ArrayAdapter<QRCode> codeArrayAdapter;
    private ArrayList<QRCode> codeArrayList = new ArrayList<>();
    private Observable tabChanger = new TabSetter();

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
     * @return A new instance of fragment SocialFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BestCodesFragment newInstance(Observer thingWithTabs) {
        BestCodesFragment fragment = new BestCodesFragment();
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
        return inflater.inflate(R.layout.fragment_bestcodes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title = view.findViewById(R.id.bestCodesT);
        bestcodes = view.findViewById(R.id.bestCodes);

        // Get all qrcodes from database, then sort.
        QRCodeMapper qm = new QRCodeMapper();
        qm.getAllCodes(qm.new CompletionHandler<ArrayList<QRCodeData>>() {
            @Override
            public void handleSuccess(ArrayList<QRCodeData> qrCodes) {
                if (this != null) {
                    rank(qrCodes);
                }
            }

            @Override
            public void handleError(Exception e) {
            }
        });
        bestcodes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * When a QRCode in the ListView is clicked, a dialog fragment will appear with the code's photo and location
             * @param adapterView
             * @param view
             * @param i
             * @param l
             */
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                QRCode qrCodeClicked = (QRCode) bestcodes.getItemAtPosition(i);
                Geolocation_PhotosFragment gpf = new Geolocation_PhotosFragment(qrCodeClicked);

                // Retrieve image for clicked qr post:
                QRCodeMapper qm = new QRCodeMapper();
                if (qrCodeClicked.getPhotourl() != null) {
                    qm.getImage(qrCodeClicked.getPhotourl(), qm.new CompletionHandler<Bitmap>() {
                        @Override
                        public void handleSuccess(Bitmap bMap) {
                            qrCodeClicked.setPhoto(bMap);
                            gpf.show(getActivity().getSupportFragmentManager(), "ADD_GEO");
                        }

                        @Override
                        public void handleError(Exception e) {
                            gpf.show(getActivity().getSupportFragmentManager(), "ADD_GEO");
                        }
                    });
                }
                else {
                    gpf.show(getActivity().getSupportFragmentManager(), "ADD_GEO");
                }
            }
        });

    }

    public void rank(ArrayList<QRCodeData> retrievedQrCodes){
        for (int i = 0; i<retrievedQrCodes.size();i++){
            QRCode qrcode = new QRCode(retrievedQrCodes.get(i));
            codeArrayList.add(qrcode);
        }

        Collections.sort(codeArrayList, Collections.reverseOrder());
        Context context = this.getContext();
        if (context != null) {
            codeArrayAdapter = new QRCodeAdapter(context, codeArrayList);
            bestcodes.setAdapter(codeArrayAdapter);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        tabChanger.notifyObservers(0);
    }




}
