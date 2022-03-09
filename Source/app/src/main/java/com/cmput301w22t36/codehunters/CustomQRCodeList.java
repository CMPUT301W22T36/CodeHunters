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

public class CustomQRCodeList extends ArrayAdapter<QRCode> {
    private ArrayList<QRCode> codes;
    private Context context;

    public CustomQRCodeList(Context context, ArrayList<QRCode> rolls){
        super(context, 0, rolls);
        this.codes = rolls;
        this.context = context;
    }

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
        code.setText("Code: " + String.valueOf(qrcode.getCode()));
        score.setText("Score: "+ String.valueOf(qrcode.getScore()));

        return view;
    }
}

