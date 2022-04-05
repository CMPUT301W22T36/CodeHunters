package com.cmput301w22t36.codehunters;

import android.graphics.Bitmap;

import com.cmput301w22t36.codehunters.Data.DataTypes.Comment;
import com.cmput301w22t36.codehunters.Data.DataTypes.QRCodeData;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.lang.Math;
import java.util.Hashtable;

/**
 * Introductory Comments:
 *      This Java file is a custom QRCode class. The purpose of this class is to abstract all of the
 *      attributes and associated methods of a QRCode. Each QRCode has a Score, Hash, and Code. Each
 *      QRCode can have an associated Photo (Bitmap) and Geolocation (Array of doubles -- (x,y)) but
 *      these attributes are not needed to initialize a QRCode object.
 *
 *      **No Outstanding Issues**
 */

/**
 * This is a class that represents a QRCode and holds its associated attributes (ex. Score, Hash)
 */
public class QRCode extends QRCodeData implements Serializable, Comparable<QRCode> {
    private boolean has_photo;
    private boolean has_location;
    //(x,y) style coordinate of geolocation -- x = latitude, y = longitude
    private ArrayList<Double> geolocation;
    //Comments
    private ArrayList<Comment> comments;

    public QRCode() {
        has_photo = false;
        has_location = false;
    }

    /**
     * QRCode Constructor: takes in code and self computes associated hash and score
     * @param code
     *      String code passed in from which score and hash wil be computed
     */
    public QRCode(String code) {
        //Set code equal to passed in code and compute hash
        String hash = DigestUtils.sha256Hex(code);
        this.setHash(hash);

        //Compute score of code
        String []letters = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v"};
        Hashtable<String, Integer> letters_values = new Hashtable<String, Integer>();
        for (int i=0; i<letters.length; i++) {
            letters_values.put(letters[i], i+10);
        }
        int length = 1;
        for (int i=1; i<hash.length(); i++) {
            if (hash.charAt(i)==hash.charAt(i-1)) {
                length++;
            } else if (length > 1){
                String value = String.valueOf(hash.charAt(i-1));
                try{
                    int number = Integer.parseInt(value);
                    if (number==0) { number = 20; };
                    this.setScore(this.getScore() + (int) Math.pow(number, length-1));
                } catch (NumberFormatException ex) {
                    this.setScore(this.getScore() + (int) Math.pow(letters_values.get(value), length-1));
                }
                length = 1;
            }
        }

        has_photo = false;
        has_location = false;
    }

    /**
     * QRCode (alternate )Constructor: takes in data and converts to normal QRCode class instance
     * @param data
     *      QRCode data type object retrieved from the firestore database
     */
    public QRCode(QRCodeData data) {
        this.setHash(data.getHash());
        this.setScore(data.getScore());
        this.setUserRef(data.getUserRef());
        this.setPhotoUrl(data.getPhotoUrl());
        this.setId(data.getId());
        //Check to see if data has a location, if so, add that to our QrCode
        if (data.getLat() != 0.0) {
            ArrayList<Double> location = new ArrayList<Double>();
            location.add(data.getLat());
            location.add(data.getLon());
            this.setLat(data.getLat());
            this.setLon(data.getLon());
            this.setGeolocation(location);
        }
        //Check to see if data has a photo, if so, add that to our QRCode
        if (data.getPhoto() != null) {
            this.setPhoto(data.getPhoto());
            has_photo = true;
        }
    }

    /**
     * Gets Geolocation of QRCode object
     * @return
     *      Geolocation of QRCode in the form of (x,y) coordinates
     */
    public ArrayList<Double> getGeolocation() {
        return geolocation;
    }

    /**
     * Used to check whether QRCode object has an associated photo
     * @return
     *      has_photo boolean
     */
    public boolean hasPhoto() {
        return has_photo;
    }

    /**
     * Used to check whether QRCode object has an associated geolocation
     * @return
     *      has_location boolean
     */

    public boolean hasLocation() {
        return has_location;
    }

    /**
     * Gets Comments associated with QRCode object
     * @return
     *      Comments of QRCode in the form of a list
     */
    public ArrayList<Comment> getComments() {
        return comments;
    }


    /**
     * Allows us to set the geolocation of a QRCode object
     * @param geolocation
     *      Geolocation in the form of an (x,y) coordinate
     */
    public void setGeolocation(ArrayList<Double> geolocation) {
        has_location = true;
        this.geolocation = geolocation;
    }

    /**
     * Set the photo of the QRCode to the one passed into the function
     * @param photo
     *      Photo we want to set as the QRCode's photo
     */
    @Override
    public void setPhoto(Bitmap photo) {
        super.setPhoto(photo);
        has_photo = false;
    }

    /**
     * Allows us to add a comment to a QRCode object
     * @param comment
     *      Comment we want to add of type comment (user & message)
     */
    public void addComment(Comment comment) {
        comments.add(comment);
    }

    /**
     * Overridden comparator of QRCode objects based on the scores of the QRCodes
     * @param qrCode
     * @return
     *      1 if the current QRCode's score is higher, -1 for the opposite, and 0 if the score are equal
     */
    @Override
    public int compareTo(QRCode qrCode) {
        if (this.getScore() > qrCode.getScore()) {
            return 1;
        } else if (this.getScore() < qrCode.getScore()) {
            return -1;
        } else {
            return 0;
        }
    }
}
