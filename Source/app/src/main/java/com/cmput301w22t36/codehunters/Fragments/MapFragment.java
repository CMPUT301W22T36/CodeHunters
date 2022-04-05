package com.cmput301w22t36.codehunters.Fragments;
/*
This Class allows the user to see the location of QRCodes that have a location on a map and view
the info for the QRCode.

Outstanding issues:
 - We don't yet have a fragment to view QRCode data, currently the pins just create a toast when tapped
 - For newly scanned pins to be seen, the fragment must be closed and reopened
 */

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmput301w22t36.codehunters.Data.DataMappers.QRCodeMapper;
import com.cmput301w22t36.codehunters.MainActivity;
import com.cmput301w22t36.codehunters.QRCode;
import com.cmput301w22t36.codehunters.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */



public class MapFragment extends Fragment {

    // Objects used for map, map control, and map overlays
    protected MapView map;
    protected IMapController mapController;
    protected MyLocationNewOverlay locationOverlay;
    protected ItemizedOverlayWithFocus<OverlayItem> qrPinsOverlay;

    // List of "OverlayItem"s (i.e. list of qrCodes in map form)
    protected ArrayList<OverlayItem> qrPinsList;
    private ArrayList<QRCode> mappedQrPinsList;
    private ArrayList<QRCode> codeArrayList;

    // Objects used for permission checking
    private Boolean locPermission, netPermission, netStatePermission;
    private ActivityResultLauncher<String[]> permissionLauncher;

    // Other objects
    private FloatingActionButton followButton;
    private Double defaultZoom = 18.0d;
    private static final String ARG_PARAM1 = "param1";

    public MapFragment() {
        // Required empty public constructor
    }


    /**
     * This method generates a new instance of the MapFragment with the required data to plot pins
     * @param codes an ArrayList of all the QRCodes
     * @return a new instance of the MapFragment ready to go
     */
    public static MapFragment newInstance(ArrayList<QRCode> codes) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, codes);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * onCreate method to prepare permissions and pins to be plotted
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        qrPinsList = new ArrayList<OverlayItem>();
        mappedQrPinsList = new ArrayList<QRCode>();

        if (getArguments() != null) {
            codeArrayList = (ArrayList<QRCode>) getArguments().getSerializable(ARG_PARAM1);
            for (QRCode code : codeArrayList) {
                if (code.getLat() != 0) {
                    GeoPoint codeGeoPoint = new GeoPoint(
                            code.getLat(),
                            code.getLon()
                    );
                    qrPinsList.add(new OverlayItem(
                            code.getHash().substring(0, 10),
                            String.valueOf(code.getScore()).concat(" Points"),
                            codeGeoPoint
                    ));
                    mappedQrPinsList.add(code);
                }
            }
        }


        // Check what permissions we have already
        ArrayList<String> requiredRequests = checkPermissions();

        // Create the object that makes the permission requests for us
        permissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(), isGranted -> {
                    // Update weather we have permission based on return of request
                    if (isGranted.containsKey(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        locPermission = isGranted.get(Manifest.permission.ACCESS_FINE_LOCATION);
                    }
                    if (isGranted.containsKey(Manifest.permission.INTERNET)) {
                        netPermission = isGranted.get(Manifest.permission.INTERNET);
                    }
                    if (isGranted.containsKey(Manifest.permission.ACCESS_NETWORK_STATE)) {
                        netStatePermission = isGranted.get(Manifest.permission.ACCESS_NETWORK_STATE);
                    }
                }
        );

        // request all of the permissions we don't have
        if (!requiredRequests.isEmpty()) {
            permissionLauncher.launch(requiredRequests.toArray(new String[0]));
        }

        // This is some magic that osmdroid needs, I think it's for tile caching
        Context ctx = getActivity().getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
    }

    /**
     * returns the inflated View
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    /**
     * Now that the view has been created, this method configures the map, its overlays, and buttons
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get the follow button and have the map track your location when pressed
        followButton = getView().findViewById(R.id.followMeButton);
        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationOverlay.enableFollowLocation();
                mapController.setZoom(defaultZoom);
            }
        });

        // Get map object and controller for map
        map = (MapView) getView().findViewById(R.id.map);
        mapController = map.getController();

        // Some other magic map stuff we need to do and enabling pinch to zoom
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);

        // Set default zoom, add overlay for current location and enable following mode by default
        // so that when the map loads in, it goes to the user's location.
        mapController.setZoom(defaultZoom);
        locationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getActivity()), map);
        locationOverlay.enableMyLocation();
        map.getOverlays().add(locationOverlay);
        locationOverlay.enableFollowLocation();


        // the overlay for the QR code pins with click listeners
        qrPinsOverlay = new ItemizedOverlayWithFocus<OverlayItem>(qrPinsList,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        // This return value was given in the example, I'm just leaving it
                        return true;
                    }
                    @Override
                    public boolean onItemLongPress(final int index, final OverlayItem item) {
                        // when long pressed, show the image of the code
                        QRCode qrCodeClicked = mappedQrPinsList.get(index);
                        QRCodeMapper qm = new QRCodeMapper();
                        String photoUrl = qrCodeClicked.getPhotoUrl();
                        if (photoUrl != null) {
                            qm.getImage(qrCodeClicked.getPhotoUrl(), qm.new CompletionHandler<Bitmap>() {
                                @Override
                                public void handleSuccess(Bitmap data) {
                                    qrCodeClicked.setPhoto(data);
                                    new Geolocation_PhotosFragment(qrCodeClicked).show(getActivity().getSupportFragmentManager(), "ADD_GEO");
                                }

                                @Override
                                public void handleError(Exception e) {
                                    new Geolocation_PhotosFragment(qrCodeClicked).show(getActivity().getSupportFragmentManager(), "ADD_GEO");
                                }
                            });
                        } else {
                            new Geolocation_PhotosFragment(qrCodeClicked).show(getActivity().getSupportFragmentManager(), "ADD_GEO");
                        }

                        // This return value was given in the example, I'm just leaving it
                        return false;
                    }
                }, getActivity());

        // enable pin tapping and add overlay to map
        qrPinsOverlay.setFocusItemsOnTap(true);
        map.getOverlays().add(qrPinsOverlay);
    }

    /**
     * Runs all the tasks to resume the fragment
     */
    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
        ((MainActivity) getActivity()).updateNavBar(1);
    }

    /**
     * runs all the tasks to pause the fragment
     */
    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }

    /**
     * checks for permissions that the app has from those it needs, then creates a list of the
     * permissions that are still required
     * @return an ArrayList of all the String names of the required permissions
     */
    private ArrayList<String> checkPermissions() {
        ArrayList<String> requiredPermissions = new ArrayList<>();


        // Check if we have the required permissions already
        locPermission = ContextCompat.checkSelfPermission(this.getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        netPermission = ContextCompat.checkSelfPermission(this.getContext(),
                Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED;
        netStatePermission = ContextCompat.checkSelfPermission(this.getContext(),
                Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED;


        // for each permission we don't have, add to list of permissions required
        if (!locPermission) {
            requiredPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (!netPermission) {
            requiredPermissions.add(Manifest.permission.INTERNET);
        }

        if (!netStatePermission) {
            requiredPermissions.add(Manifest.permission.ACCESS_NETWORK_STATE);
        }

        return requiredPermissions;
    }
}