package com.cmput301w22t36.codehunters.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cmput301w22t36.codehunters.Data.DataMappers.QRCodeMapper;
import com.cmput301w22t36.codehunters.Data.DataMappers.UserMapper;
import com.cmput301w22t36.codehunters.Data.DataTypes.QRCodeData;
import com.cmput301w22t36.codehunters.Data.DataTypes.User;
import com.cmput301w22t36.codehunters.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScoreBoardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
//Introduction: This fragment is used to calculate and get bests in different ranking, including by highest score, by number,by total score.
//              It will get all data from online database, do the ranks and display the ranks.
//              It can replace socialFragmentView in SocialFragment
//              just for  now: No actual functions but all display and navigation are completed.
public class ScoreBoardFragment extends Fragment {
    TextView title;
    TextView byHighestScoreT;
    TextView byHighestScore;
    TextView byNumberT;
    TextView byNumber;
    TextView byTotalScoreT;
    TextView byTotalScore;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";


    // TODO: Rename and change types of parameters
    private String username;





    public ScoreBoardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param name
     * @return A new instance of fragment SocialFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScoreBoardFragment newInstance(String name) {
        ScoreBoardFragment fragment = new ScoreBoardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = (String) getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scoreboard, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        title = view.findViewById(R.id.scoreBoardT1);
        byHighestScoreT = view.findViewById(R.id.scoreBoardT2);
        byNumberT = view.findViewById(R.id.scoreBoardT3);
        byTotalScoreT = view.findViewById(R.id.scoreBoardT4);

        byHighestScore = view.findViewById(R.id.byHighestScore);
        byNumber = view.findViewById(R.id.byNumber);
        byTotalScore = view.findViewById(R.id.byTotalScore);


        // ordered by score
        UserMapper ums = new UserMapper();
        ums.usersOrderedBy("score", ums.new CompletionHandler<ArrayList<User>>() {
            @Override
            public void handleSuccess(ArrayList<User> UAS) {
                //test
                byTotalScore.setText(UAS.get(0).getUsername());
                /**for (int i = 0; i<UAS.size();i++){

                    if(UAS.get(i).getUsername() == username){
                        byTotalScore.setText(UAS.get(i+1).getUsername() +"-----Rank:"+(i+1)+"\nYou: "+username+"-----Rank:"+i+"\n" + UAS.get(i-1).getUsername() +"-----Rank:"+(i-1));
                    }
                }*/
            }
        });

        // ordered by scanCount
        UserMapper umc = new UserMapper();
        umc.usersOrderedBy("scanCount", umc.new CompletionHandler<ArrayList<User>>() {
            @Override
            public void handleSuccess(ArrayList<User> UAC) {
                for (int i = 0; i<UAC.size();i++){
                    if(UAC.get(i).getUsername() == username){
                        byNumber.setText(UAC.get(i+1).getUsername() +"-----Rank:"+(i+1)+"\nYou: "+username+"-----Rank:"+i+"\n" + UAC.get(i-1).getUsername() +"-----Rank:"+(i-1));
                    }
                    else{byNumber.setText(username);}
                }
            }
        });

        // ordered by bestScore
        UserMapper umb = new UserMapper();
        umb.usersOrderedBy("bestScore", umb.new CompletionHandler<ArrayList<User>>() {
            @Override
            public void handleSuccess(ArrayList<User> UAB) {
                byHighestScore.setText(UAB.get(0).getUsername());
                /**
                for (int i = 0; i<UAB.size();i++){
                    if(UAB.get(i).getUsername() == username){
                        byHighestScore.setText(UAB.get(i+1).getUsername() +"-----Rank:"+(i+1)+"\nYou: "+username+"-----Rank:"+i+"\n" + UAB.get(i-1).getUsername() +"-----Rank:"+(i-1));
                    }
                }*/
            }
        });



    }
}
