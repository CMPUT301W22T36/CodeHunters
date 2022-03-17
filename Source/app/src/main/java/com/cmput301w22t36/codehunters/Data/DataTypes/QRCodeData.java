package com.cmput301w22t36.codehunters.Data.DataTypes;

import com.cmput301w22t36.codehunters.Data.Data;

import java.util.List;

public class QRCodeData extends Data {
    private String userRef;
    private int score;
    private String code;
    private int lat;
    private int lon;
    private String photourl;
    // Access methods need to be defined. Might need a way to record who made what comment as well.
    //private List<String> comments;

    public String getUserRef() {
        return userRef;
    }

    public void setUserRef(String userRef) {
        this.userRef = userRef;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getLat() {
        return lat;
    }

    public void setLat(int lat) {
        this.lat = lat;
    }

    public int getLon() {
        return lon;
    }

    public void setLon(int lon) {
        this.lon = lon;
    }

    public String getPhotourl() {
        return photourl;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }
}
