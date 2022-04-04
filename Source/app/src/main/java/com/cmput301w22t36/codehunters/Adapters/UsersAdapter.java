package com.cmput301w22t36.codehunters.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cmput301w22t36.codehunters.Data.DataTypes.User;
import com.cmput301w22t36.codehunters.R;

import java.util.ArrayList;

public class UsersAdapter extends ArrayAdapter<User> {
    private ArrayList<User> users;
    private Context context;

    public UsersAdapter(Context context, ArrayList<User> usersList) {
        super(context, 0, usersList);
        this.context = context;
        this.users = usersList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.arrayadapter_users, parent,false);
        }

        User thisUser = users.get(position);
        TextView username = view.findViewById(R.id.usernameTextView);
        TextView totalScore = view.findViewById(R.id.scoreTextView);
        TextView scanCount = view.findViewById(R.id.countTextView);
        TextView bestCode = view.findViewById(R.id.bestCodeTextView);


        username.setText(thisUser.getUsername());
        totalScore.setText("Total Score: " + thisUser.getScore().toString());
        scanCount.setText("Scan Count: " + thisUser.getScanCount().toString());
        bestCode.setText("Best Code: " + thisUser.getBestScore().toString());

        return view;
    }

}
