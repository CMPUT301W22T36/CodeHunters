package com.cmput301w22t36.codehunters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.cmput301w22t36.codehunters.Data.DataTypes.QRCodeData;
import com.cmput301w22t36.codehunters.Fragments.SearchNearbyCodesFragment;
import com.cmput301w22t36.codehunters.QRCode;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

public class qrDistanceTest {
    @Test
    public void qrDistanceTest() {
        SearchNearbyCodesFragment fragment = SearchNearbyCodesFragment.newInstance();
        //Adding code 1 with geolocation
        ArrayList<QRCode> codes = new ArrayList<QRCode>();
        QRCode code1 = new QRCode("BFG5DGW54");
        double lat = 37.42;
        double lon = -122.08;
        ArrayList<Double> location = new ArrayList<Double>();
        location.add(lat);
        location.add(lon);
        code1.setGeolocation(location);
        code1.setLat(lat);
        code1.setLon(lon);
        codes.add(code1);
        //Adding code 2 with geolocation
        QRCode code2 = new QRCode("BFG5DGW55");
        double lat1 = 37.421;
        double lon1 = -122.081;
        ArrayList<Double> location1 = new ArrayList<Double>();
        location1.add(lat1);
        location1.add(lon1);
        code2.setGeolocation(location1);
        code2.setLat(lat1);
        code2.setLon(lon1);
        codes.add(code2);
        //Adding code 3 with geolocation
        QRCode code3 = new QRCode("BFG5DGW56");
        double lat2 = 53.42;
        double lon2 = -124.08;
        ArrayList<Double> location2 = new ArrayList<Double>();
        location2.add(lat2);
        location2.add(lon2);
        code3.setGeolocation(location2);
        code3.setLat(lat2);
        code3.setLon(lon2);
        codes.add(code3);
        ArrayList<QRCode> qrTestList1 = new ArrayList<>();
        qrTestList1 = fragment.qrDistance(codes, lat, lon);
//Only two codes should be in the result because they are in 5km radius
        assertEquals(qrTestList1.size(), 2);




    }
}
