package com.cmput301w22t36.codehunters.Data.DataTypes;

import android.graphics.Bitmap;

import com.cmput301w22t36.codehunters.Data.Data;

import java.io.Serializable;

public class QRCodeData extends Data implements Serializable {
    private String userRef;
    private int score;
    private String hash;
    private double lat;
    private double lon;
    private String photourl;
    private transient Bitmap photo;

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

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getPhotourl() {
        return photourl;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    /**
     * Allows us to set the photo of a QRCode object
     * @param photo
     *      Photo in the form of a bitmap
     */
    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }
}
