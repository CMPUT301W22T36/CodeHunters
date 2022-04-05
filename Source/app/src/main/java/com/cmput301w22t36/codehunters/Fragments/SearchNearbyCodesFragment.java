package com.cmput301w22t36.codehunters.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.cmput301w22t36.codehunters.Data.DataMappers.QRCodeMapper;
import com.cmput301w22t36.codehunters.Data.DataTypes.QRCodeData;
import com.cmput301w22t36.codehunters.QRCode;
import com.cmput301w22t36.codehunters.Adapters.QRCodeAdapter;
import com.cmput301w22t36.codehunters.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Introductory Comments:
 *
 * Class: SearchNearbyCodesFragment, a {@link Fragment} subclass.
 * Take in the users prompts to search for QR codes by geolocation.
 * Note: All handleSuccess() and handleError() methods are processed calls to the Firestore database.
 */
public class SearchNearbyCodesFragment extends Fragment {

    // Initialize views to manage them within a fragment
    private ListView codeList;
    private EditText latInput;
    private EditText lonInput;
    private Button search;
    private ArrayList<QRCode> codeArrayList = new ArrayList<>();
    private ArrayAdapter<QRCode> codeArrayAdapter;
    private ArrayList<QRCode> sortedDistanceQRList = new ArrayList<>();

    /**
     * Required empty public constructor
     */
    public SearchNearbyCodesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SearchNearbyCodesFragment.
     */
    public static SearchNearbyCodesFragment newInstance() {
        SearchNearbyCodesFragment fragment = new SearchNearbyCodesFragment();
        return fragment;
    }

    /**
     * This initializes the fragment
     * @param savedInstanceState: this is the bundle that will be called through the superclass
     */
    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

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
        return inflater.inflate(R.layout.fragment_search_nearby_codes, container, false);
    }

    /**
     * Obtain a latitude and longitude as user input and compute the list of nearby QR codes.
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Obtain the views within the fragment
        codeList = view.findViewById(R.id.code_list);
        latInput = (EditText)view.findViewById(R.id.lat);
        lonInput = (EditText)view.findViewById(R.id.lon);
        search = (Button)view.findViewById(R.id.searchCode);

        // Get the current location as default
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            Location loc = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            double longi = loc.getLongitude();
            double lat = loc.getLatitude();
            displayList(lat,longi, codeArrayList);
        }

        // Set the search button to respond to user clicks
        search.setOnClickListener(new View.OnClickListener() {
            /**
             * Search for codes near the lat and lon specified by the user
             * @param view: the current view
             */
            @Override
            public void onClick(View view) {
                if (latInput.getText() != null && lonInput.getText() != null) {
                    // Obtain the lat and lon values
                    String latValue = latInput.getText().toString();
                    String lonValue = lonInput.getText().toString();

                    // Convert the values for searching
                    double latInteger = Double.parseDouble(latValue);
                    double lonInteger = Double.parseDouble(lonValue);

                    displayList(latInteger, lonInteger, codeArrayList);
                } else {
                    Toast.makeText(getContext(), "Error: one or more of coordinate fields are empty", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

    /**
     * The qrDistance function finds the Manhattan Distance of your current location and
     * the location of QR Codes and returns a sortedDistanceQRList if the distance converted
     * to KM is less than 5 KM
     * @param qrdistance: the list of all QR codes from which to obtain the closest
     * @param lat: the given latatidue
     * @param lon: the given longitude
     * @return sortedDistanceQRList
     */
    public ArrayList<QRCode> qrDistance(ArrayList<QRCode> qrdistance, double lat, double lon) {
        sortedDistanceQRList.clear();
        for (int i = 0; i < qrdistance.size(); i++) {
            QRCode qrcode = qrdistance.get(i);
            if (qrcode.hasLocation()) {
                //Manhattan distance of current location and location of QR Codes from database
                // is computed
                double databaseCodeLat = qrcode.getLat();
                double databaseCodeLongi = qrcode.getLon();
                double latDistance = Math.abs(lat - databaseCodeLat);
                double longiDistance = Math.abs(lon - databaseCodeLongi);
                // Manhattan Distance is converted to KM
                double a = Math.pow(Math.sin(Math.toRadians(latDistance / 2)), 2) + Math.cos(Math.toRadians(databaseCodeLat)) * Math.cos(Math.toRadians(lat)) * Math.pow(Math.sin(Math.toRadians(longiDistance / 2)), 2);
                double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
                double km = 6371 * c;
                if (km < 5) {
                    sortedDistanceQRList.add(qrcode);
                }
            }
        }
        return sortedDistanceQRList;
    }

    /**
     * Displays the list of sorted QRCodes by location in the listview
     * @param latInteger
     * @param lonInteger
     * @param codeArrayList
     */
    // Obtain the nearby QR codes and display them with the ListView
    private void displayList(double latInteger, double lonInteger, ArrayList<QRCode> codeArrayList) {

        //Get all codes from the database and apply the qrDistance function on them, then display them
        QRCodeMapper qrm = new QRCodeMapper();
        qrm.getAllCodesLocations(qrm.new CompletionHandler<ArrayList<QRCodeData>>() {
            @Override
            public void handleSuccess(ArrayList<QRCodeData> QRA) {
                listQRs(QRA);
                ArrayList<QRCode> sortedQRDistanceList;
                sortedQRDistanceList = qrDistance(codeArrayList, latInteger, lonInteger);
                codeArrayAdapter = new QRCodeAdapter(getContext(), sortedQRDistanceList);
                codeList.setAdapter(codeArrayAdapter);
            }

            @Override
            public void handleError(Exception e) {
                // Handle the case where user not found.
            }
        });
        codeList.setOnItemClickListener((adapterView, view1, i, l) -> {
            QRCode qrCodeClicked = (QRCode) codeList.getItemAtPosition(i);
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
    }

    /**
     * Gets all QRCodeData objects and adds to codeArrayList
     * @param A: the list of all QR codes from which to obtain the closest
     */
    public void listQRs(ArrayList<QRCodeData> A){
        for (int i = 0; i<A.size();i++){
            QRCode qrcode = new QRCode(A.get(i));
            codeArrayList.add(qrcode);
        }
    }

    /**
     * Notify the adapter that the data has been updated
     */
    public void notifyCodesAdapter() {
        codeArrayAdapter.notifyDataSetChanged();
    }
}