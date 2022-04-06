package com.cmput301w22t36.codehunters.Fragments;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.cmput301w22t36.codehunters.Data.DataMappers.QRCodeMapper;
import com.cmput301w22t36.codehunters.Data.DataMappers.UserMapper;
import com.cmput301w22t36.codehunters.Data.DataTypes.QRCodeData;
import com.cmput301w22t36.codehunters.MainActivity;
import com.cmput301w22t36.codehunters.QRCode;
import com.cmput301w22t36.codehunters.Adapters.QRCodeAdapter;
import com.cmput301w22t36.codehunters.R;

import java.util.ArrayList;

/**
 * class: SearchUserFragment, a {@link Fragment} subclass.
 *
 * Introduction: Display the information of searched user, show all codes that searched user scanned as listview,
 * show all codes that both searched user and current user had scanned as listview.
*/

public class SearchUserFragment extends Fragment {
    TextView searchedUserT;
    TextView searchedUser;
    TextView codesInCommonT;
    private ListView commonCodesListView;
    TextView allCodesT;
    ListView allCodesListView;
    ArrayList<QRCode> tempData = new ArrayList<QRCode>();

    public SearchUserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SocialFragment.
     */
    public static SearchUserFragment newInstance() {
        SearchUserFragment fragment = new SearchUserFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * This initializes the fragment
     * @param savedInstanceState: this is the bundle that will be called through the superclass
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * This inflates the fragment's layout
     * @param inflater: the LayoutInflator for the view
     * @param container: the ViewGroup of the view
     * @param savedInstanceState: this is the bundle that will be called through the superclass
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_searcheduser, container, false);

        return inflate;
    }

    /**
     * Obtain all information and QRCode list views
     * @param view
     * @param savedInstanceState
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        codesInCommonT = view.findViewById(R.id.t2);
        commonCodesListView = view.findViewById(R.id.codesInCommon);
        allCodesT = view.findViewById(R.id.t3);
        allCodesListView = view.findViewById(R.id.allCodes);
        searchedUserT= view.findViewById(R.id.t1);
        searchedUser = view.findViewById(R.id.stats);

       /* QRCodeMapper qrCodeMapper = new QRCodeMapper();

        qrCodeMapper.getMatchingCodes(MainActivity.mainActivity.searchUser,new ArrayList<>(),qrCodeMapper.new CompletionHandler<ArrayList<QRCodeData>>(){
            @Override
            public void handleSuccess(ArrayList<QRCodeData> data) {
                QRCodeAdapter2 qrCodeAdapter = new QRCodeAdapter2(getActivity(),data);
                viewById.setAdapter(qrCodeAdapter);
            }
            @Override
            public void handleError(Exception e) {

            }
        });*/

        UserMapper um = new UserMapper();
        QRCodeMapper qr = new QRCodeMapper();


        qr.getMatchingCodes(MainActivity.mainActivity.searchUser,
                MainActivity.mainActivity.codeArrayList,
                qr.new CompletionHandler<ArrayList<QRCodeData>>(){
            @Override
            public void handleSuccess(ArrayList<QRCodeData> matchedCodesData) {
                ArrayList<QRCode> matchedCodes = new ArrayList<>();
                for (QRCodeData codeData : matchedCodesData) {
                    matchedCodes.add(new QRCode(codeData));
                }

                QRCodeAdapter matchedCodeAdapter = new QRCodeAdapter(getActivity(),matchedCodes);
                commonCodesListView.setAdapter(matchedCodeAdapter);
            }

            @Override
            public void handleError(Exception e) {
            }
        });

        qr.queryQRCodes(MainActivity.mainActivity.searchUser,
                qr.new CompletionHandler<ArrayList<QRCodeData>>(){
                    @Override
                    public void handleSuccess(ArrayList<QRCodeData> searchedUserCodeData) {
                        ArrayList<QRCode> searchedUserCodes = new ArrayList<>();
                        for (QRCodeData codeData : searchedUserCodeData) {
                            searchedUserCodes.add(new QRCode(codeData));
                        }

                        QRCodeAdapter allCodesAdapter = new QRCodeAdapter(getActivity(),searchedUserCodes);
                        allCodesListView.setAdapter(allCodesAdapter);
                    }
                });

        // User information display
        searchedUser.setText("Username: "+MainActivity.mainActivity.searchUser.getUsername()
                +"\r\n"
                +"Email: "+MainActivity.mainActivity.searchUser.getEmail()
                +"\r\n"
                +"Is Owner: "+MainActivity.mainActivity.searchUser.getOwner()
                +"\r\n"
                +"BestScore: "+String.valueOf(MainActivity.mainActivity.searchUser.getBestScore())
                +"\r\n"
                +"TotalScore: "+String.valueOf(MainActivity.mainActivity.searchUser.getScore())
                +"\r\n"
                +"ScanCount: "+String.valueOf(MainActivity.mainActivity.searchUser.getScanCount())
        );
        allCodesListView.setOnItemClickListener((adapterView, view1, i, l) -> {
            QRCode qrCodeClicked = (QRCode) allCodesListView.getItemAtPosition(i);
            Geolocation_PhotosFragment gpf = new Geolocation_PhotosFragment(qrCodeClicked);

            // Retrieve image for clicked qr post:
            QRCodeMapper qm1 = new QRCodeMapper();
            if (qrCodeClicked.getPhotoUrl() != null) {
                qm1.getImage(qrCodeClicked.getPhotoUrl(), qm1.new CompletionHandler<Bitmap>() {
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
//        allCodesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            /**
//             * When a QRCode in the ListView is clicked, a dialog fragment will appear with the code's photo and location
//             * @param adapterView
//             * @param view
//             * @param i
//             * @param l
//             */
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                QRCode qrCodeClicked = (QRCode) allCodesListView.getItemAtPosition(i);
//                new Geolocation_PhotosFragment(qrCodeClicked).show(getActivity().getSupportFragmentManager(), "ADD_GEO");
//
//            }
//        });

        commonCodesListView.setOnItemClickListener((adapterView, view1, i, l) -> {
            QRCode qrCodeClicked = (QRCode) commonCodesListView.getItemAtPosition(i);
            Geolocation_PhotosFragment gpf = new Geolocation_PhotosFragment(qrCodeClicked);

            // Retrieve image for clicked qr post:
            QRCodeMapper qm1 = new QRCodeMapper();
            if (qrCodeClicked.getPhotoUrl() != null) {
                qm1.getImage(qrCodeClicked.getPhotoUrl(), qm1.new CompletionHandler<Bitmap>() {
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
//        commonCodesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            /**
//             * When a QRCode in the ListView is clicked, a dialog fragment will appear with the code's photo and location
//             * @param adapterView
//             * @param view
//             * @param i
//             * @param l
//             */
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                QRCode qrCodeClicked = (QRCode) commonCodesListView.getItemAtPosition(i);
//                new Geolocation_PhotosFragment(qrCodeClicked).show(getActivity().getSupportFragmentManager(), "ADD_GEO");
//
//            }
//        });
/*
        ArrayAdapter arrayAdapter1 = new ArrayAdapter(this.getContext(), android.R.layout.simple_expandable_list_item_1,CodesInCommon);
        codesInCommon.setAdapter(arrayAdapter1);
        ArrayAdapter arrayAdapter2 = new ArrayAdapter(this.getContext(), android.R.layout.simple_expandable_list_item_1,AllCodes);
        allCodes.setAdapter(arrayAdapter2);*/




    }
}
