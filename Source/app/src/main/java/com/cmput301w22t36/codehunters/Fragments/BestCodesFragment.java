package com.cmput301w22t36.codehunters.Fragments;

import android.content.Context;
import android.graphics.Bitmap;
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
import androidx.fragment.app.FragmentActivity;

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
    private TextView title;
    private ListView bestCodes;
    private ArrayAdapter<QRCode> codeArrayAdapter;
    private ArrayList<QRCode> codeArrayList = new ArrayList<>() ;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private final Observable tabChanger = new TabSetter();

    public BestCodesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SocialFragment.
     */
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
        bestCodes = view.findViewById(R.id.bestCodes);

        // Get all qrCodes from database, then sort.
        QRCodeMapper qm = new QRCodeMapper();
        qm.getAllCodes(qm.new CompletionHandler<ArrayList<QRCodeData>>() {
            @Override
            public void handleSuccess(ArrayList<QRCodeData> qrCodes) {
                rankAndSet(qrCodes);
            }

            @Override
            public void handleError(Exception e) {
            }
        });

        bestCodes.setOnItemClickListener((adapterView, view1, i, l) -> {
            QRCode qrCodeClicked = (QRCode) bestCodes.getItemAtPosition(i);
            Geolocation_PhotosFragment gpf = new Geolocation_PhotosFragment(qrCodeClicked);

            // Retrieve image for clicked qr post:
            QRCodeMapper qm1 = new QRCodeMapper();
            if (qrCodeClicked.getPhotourl() != null) {
                qm1.getImage(qrCodeClicked.getPhotourl(), qm1.new CompletionHandler<Bitmap>() {
                    @Override
                    public void handleSuccess(Bitmap bMap) {
                        qrCodeClicked.setPhoto(bMap);
                        FragmentActivity fActivity = getActivity();
                        if (fActivity != null) {
                            gpf.show(fActivity.getSupportFragmentManager(), "ADD_GEO");
                        }
                    }

                    @Override
                    public void handleError(Exception e) {
                        FragmentActivity fActivity = getActivity();
                        if (fActivity != null) {
                            gpf.show(fActivity.getSupportFragmentManager(), "ADD_GEO");
                        }
                    }
                });
            }
            else {
                FragmentActivity fActivity = getActivity();
                if (fActivity != null) {
                    gpf.show(fActivity.getSupportFragmentManager(), "ADD_GEO");
                }
            }
        });
    }

    public void rankAndSet(ArrayList<QRCodeData> retrievedQrCodes){
        ArrayList<QRCode> codeArrayList = new ArrayList<>();

        for (int i = 0; i<retrievedQrCodes.size();i++){
            QRCode qrcode = new QRCode(retrievedQrCodes.get(i));
            codeArrayList.add(qrcode);
        }

        Collections.sort(codeArrayList, Collections.reverseOrder());

        // Rank gets called in a different thread, need to ensure context is not null.
        Context context = this.getContext();
        if (context != null) {
            ArrayAdapter<QRCode> codeArrayAdapter = new QRCodeAdapter(context, codeArrayList);
            bestCodes.setAdapter(codeArrayAdapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        tabChanger.notifyObservers(0);
    }
}
