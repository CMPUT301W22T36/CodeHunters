package com.cmput301w22t36.codehunters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * Custom Array Adapter for QRCode listview functionality
 */
public class QRCodeAdapter extends ArrayAdapter<QRCode> {
    private ArrayList<QRCode> codes;
    private Context context;

    /**
     * Constructor that builds custom array adapter
     * @param context
     * @param code_list array list of QRCode objects
     */
    public QRCodeAdapter(Context context, ArrayList<QRCode> code_list){
        super(context, 0, code_list);
        this.codes = code_list;
        this.context = context;
    }

    /**
     * Responsible for communicating with Java how a QRCode object within a list should be displayed in a ListView
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // return super.getView(position, convertView, parent);
        View view = convertView;

        if(view==null){
            view = LayoutInflater.from(context).inflate(R.layout.qrcode_content, parent,false);
        }

        QRCode qrcode = codes.get(position);

        //Connect to TextViews in xml file
        TextView code = view.findViewById(R.id.qr_code);
        TextView score = view.findViewById(R.id.qr_score);

        //Populate TextViews with roll value
        code.setText("Hash: " + String.valueOf(qrcode.getHash()).substring(0, 10));
        score.setText("Score: "+ String.valueOf(qrcode.getScore()));

        return view;
    }
}

