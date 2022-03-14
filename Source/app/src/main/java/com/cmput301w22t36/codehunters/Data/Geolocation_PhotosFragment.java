package com.cmput301w22t36.codehunters.Data;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.cmput301w22t36.codehunters.QRCode;
import com.cmput301w22t36.codehunters.R;

/**
 * Introductory Comments:
 *      This Java file is a fragment representing the geolocation and photo dialog of our application.
 *      The fragment opens when a QR Code in the Codes fragment is clicked and shows the geolocation
 *      and photo of the QR Code and QR Code Location respectively. If location or photo was not
 *      provided, this fragment will not show anything
 *
 *      **Outstanding issue: The XML for this file is currently plain and needs to be
 *      updated to look better**
 */

public class Geolocation_PhotosFragment extends DialogFragment {

    private QRCode qrCodeClicked;
    private TextView geolocation;
    private ImageView photo;

    public Geolocation_PhotosFragment(QRCode qrCodeClicked) {
        this.qrCodeClicked = qrCodeClicked;
    }
    

    @NonNull
    @Override
    /**
     * Opens dialog box and sets TextView and ImageView to geolocation and Bitmap image
     * @return
     * returns the dialog box showing geolocation and photo
     */
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.geolocation_photos,null);
        geolocation = (TextView) view.findViewById(R.id.geolocation);
        photo = (ImageView) view.findViewById(R.id.photo);

        if (qrCodeClicked.getGeolocation() != null) {
            geolocation.setText(qrCodeClicked.getGeolocation().toString());
        } else {
            geolocation.setText("You did not record a location");
        }
        if (qrCodeClicked.getPhoto() != null) {
            photo.setImageBitmap(qrCodeClicked.getPhoto());
        } else {
            photo.setImageBitmap(null);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setNegativeButton("OK", null)
               .create();

    }
}
