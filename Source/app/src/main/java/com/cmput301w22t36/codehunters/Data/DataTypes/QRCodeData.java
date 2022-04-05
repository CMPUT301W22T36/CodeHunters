package com.cmput301w22t36.codehunters.Data.DataTypes;

import android.graphics.Bitmap;

import com.cmput301w22t36.codehunters.Data.Data;

import java.io.Serializable;

/**
 * Introductory Comments:
 *      This Java file is a custom QRCodeData class. The purpose of this class is to abstract all of the
 *      essential attributes and associated methods of the QRCode class into a class that interacts with the database.
 *
 *      **No Outstanding Issues**
 */

/**
 * This is a class that holds the neccessary data for a QRCode to be stored and retrieved from the database
 */
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

    /**
     * Get the reference to the document id of the user who has scanned this code
     * @return
     *      reference to the document id of the user who has scanned this code
     */
    public String getUserRef() {
        return userRef;
    }

    /**
     * Set userref of QRCodeData to the one passed into the function
     * @param userRef
     *      reference to the document id of the user who has scanned this code
     */
    public void setUserRef(String userRef) {
        this.userRef = userRef;
    }

    /**
     * Retrieve the score associated with the QRCode object and its hash
     * @return
     *      Score of code
     */
    public int getScore() {
        return score;
    }

    /**
     * Set score of the code to the one passed into the function
     * @param score
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Retrieve and return the hash of the object
     * @return
     *      QRCode hash that is retrieved
     */
    public String getHash() {
        return hash;
    }

    /**
     * Set the hash of the QRCodeData object to the one passed in to the function
     * @param hash
     */
    public void setHash(String hash) {
        this.hash = hash;
    }

    /**
     * Retrieve the QRCodeData object's longitude
     * @return
     *      Longitude of object
     */
    public double getLat() {
        return lat;
    }

    /**
     * Set the latitude of the QRCodeData object to the passed in value
     * @param lat
     */
    public void setLat(Double lat) {
        this.lat = lat;
    }

    /**
     * Retrieve the QRCodeData object's longitude
     * @return
     *      Longitude of object
     */
    public double getLon() {
        return lon;
    }

    /**
     * Set the longitude of the QRCodeData object to the passed in value
     * @param lon
     */
    public void setLon(Double lon) {
        this.lon = lon;
    }

    /**
     * Get the photourl of the object (used in database storing of photos)
     * @return
     *      photourl of the QRCodeData object
     */
    public String getPhotourl() {
        return photourl;
    }

    /**
     * Set the photourl of the object to the one passed into the function (used in database storing of photos)
     * @param photourl
     *      Passed in photourl
     */
    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }

    /**
     * Gets the photo associated with the QRCodeData object
     * @return
     *      Photo associated with object
     */
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
