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

/**
 * This class is an array adapter for showing lists of users. Currently it is used in the All Users
 * tab of the social tab
 */

public class UsersAdapter extends ArrayAdapter<User> {

    /**
     * Constructor for adapter, just calls super
     * @param context context of the array adapter
     * @param usersList list of users to be displayed
     */
    public UsersAdapter(Context context, ArrayList<User> usersList) {
        super(context, 0, usersList);
    }

    /**
     * Contructs the view for an idividual item in the list
     * @param position position in the list (both on the backend and displayed)
     * @param convertView this value can be null
     * @param parent this value cannot be null
     * @return the view that has been constructed for this element
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null) {
            view = LayoutInflater.from(super.getContext()).inflate(R.layout.arrayadapter_users, parent,false);
        }

        User thisUser = super.getItem(position);
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
