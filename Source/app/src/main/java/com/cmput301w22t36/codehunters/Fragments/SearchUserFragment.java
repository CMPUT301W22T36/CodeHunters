package com.cmput301w22t36.codehunters.Fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.Settings;
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

import com.cmput301w22t36.codehunters.Data.DataMapper;
import com.cmput301w22t36.codehunters.Data.DataMappers.QRCodeMapper;
import com.cmput301w22t36.codehunters.Data.DataMappers.UserMapper;
import com.cmput301w22t36.codehunters.Data.DataTypes.QRCodeData;
import com.cmput301w22t36.codehunters.Data.DataTypes.User;
import com.cmput301w22t36.codehunters.MainActivity;
import com.cmput301w22t36.codehunters.QRCodeAdapter;
import com.cmput301w22t36.codehunters.QRCodeAdapter2;
import com.cmput301w22t36.codehunters.R;
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
    ArrayList<QRCodeData> tempData = new ArrayList<>();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView viewById;


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
        View inflate = inflater.inflate(R.layout.fragment_searcheduser, container, false);
        viewById = inflate.findViewById(R.id.codesInCommon);

        return inflate;
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
        String UDID = Settings.Secure.getString(getActivity().getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        UserMapper um = new UserMapper();
        QRCodeMapper qr = new QRCodeMapper();
        um.queryUDID(UDID, um.new CompletionHandler<User>() {
            @Override
            public void handleSuccess(User data) {
                // Name is unique. Attempt to store user.
                User user = data;

                qr.queryQRCodes(user,qr.new CompletionHandler<ArrayList<QRCodeData>>(){
                    @Override
                    public void handleSuccess(ArrayList<QRCodeData> data) {
                        super.handleSuccess(data);

                        qr.queryQRCodes(MainActivity.mainActivity.searchUser,qr.new CompletionHandler<ArrayList<QRCodeData>>(){
                            @Override
                            public void handleSuccess(ArrayList<QRCodeData> data1) {
                                super.handleSuccess(data1);
                                tempData.clear();
                                for (int i = 0; i < data.size(); i++) {
                                    for (int j = 0; j < data1.size(); j++) {
                                        if (data.get(i).getHash().equals(data1.get(j).getHash())){
                                            tempData.add(data1.get(j));
                                        }
                                    }
                                }

                                QRCodeAdapter2 qrCodeAdapter = new QRCodeAdapter2(getActivity(),tempData);
                                viewById.setAdapter(qrCodeAdapter);

                                QRCodeAdapter2 qrCodeAdapter2 = new QRCodeAdapter2(getActivity(),data1);
                                allCodes.setAdapter(qrCodeAdapter2);
                            }

                            @Override
                            public void handleError(Exception e) {
                                super.handleError(e);
                            }
                        });

                    }

                    @Override
                    public void handleError(Exception e) {
                        super.handleError(e);
                    }
                });

            }


            @Override
            public void handleError(Exception e) {

            }
        });

      /*  CodesInCommon.add("code1");
        CodesInCommon.add("code2");
        CodesInCommon.add("code3");
        CodesInCommon.add("code4");
        CodesInCommon.add("code5");
        CodesInCommon.add("code6");
        AllCodes.add("code1");
        AllCodes.add("code2");
        AllCodes.add("code3");*/

        searchedUserT= view.findViewById(R.id.t1);
        searchedUser = view.findViewById(R.id.stats);

        searchedUser.setText("username: "+MainActivity.mainActivity.searchUser.getUsername()
                +"\r\n"
                +"BestScore: "+MainActivity.mainActivity.searchUser.getBestScore()
                +"\r\n"
                +"Email: "+MainActivity.mainActivity.searchUser.getEmail()
                +"\r\n"
                +"Is Owner: "+MainActivity.mainActivity.searchUser.getOwner()
                +"\r\n"
                +"Total Score: "+MainActivity.mainActivity.searchUser.getScore()
                +"\r\n"
                +"Scan Count: "+MainActivity.mainActivity.searchUser.getScanCount()

        );




        codesInCommonT = view.findViewById(R.id.t2);
        codesInCommon = view.findViewById(R.id.codesInCommon);
        allCodesT = view.findViewById(R.id.t3);
        allCodes = view.findViewById(R.id.allCodes);


/*
        ArrayAdapter arrayAdapter1 = new ArrayAdapter(this.getContext(), android.R.layout.simple_expandable_list_item_1,CodesInCommon);
        codesInCommon.setAdapter(arrayAdapter1);
        ArrayAdapter arrayAdapter2 = new ArrayAdapter(this.getContext(), android.R.layout.simple_expandable_list_item_1,AllCodes);
        allCodes.setAdapter(arrayAdapter2);*/

        //todo
        //get bestcodes, need get all data from database and do some sorts.



    }
}
