package com.cmput301w22t36.codehunters;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
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
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */



public class MapFragment extends Fragment {

    protected MapView map;
    protected IMapController mapController;
    protected MyLocationNewOverlay locationOverlay;


    private Boolean locPermission, netPermission, netStatePermission;
    private ActivityResultLauncher<String[]> permissionLauncher;
    private FloatingActionButton followButton;

    public MapFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<String> requiredRequests = checkPermissions();

        permissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(), isGranted -> {
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

        if (!requiredRequests.isEmpty()) {
            permissionLauncher.launch(requiredRequests.toArray(new String[0]));
        }

        Context ctx = getActivity().getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        followButton = getView().findViewById(R.id.followMeButton);
        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationOverlay.enableFollowLocation();
            }
        });

        map = (MapView) getView().findViewById(R.id.map);
        mapController = map.getController();

        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);

        mapController.setZoom(18.0);
        locationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getActivity()), map);
        locationOverlay.enableMyLocation();
        map.getOverlays().add(locationOverlay);
        locationOverlay.enableFollowLocation();
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }

    private ArrayList<String> checkPermissions() {
        ArrayList<String> requiredPermissions = new ArrayList<>();


        // Check if we have the required permissions
        locPermission = ContextCompat.checkSelfPermission(this.getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        netPermission = ContextCompat.checkSelfPermission(this.getContext(),
                Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED;
        netStatePermission = ContextCompat.checkSelfPermission(this.getContext(),
                Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED;

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