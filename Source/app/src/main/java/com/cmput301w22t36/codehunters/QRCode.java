package com.cmput301w22t36.codehunters;

import android.graphics.Bitmap;

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
 *      **No outstanding issues**
 */

/**
 * This is a class that represents a QRCode and holds its associated attributes (ex. Score, Hash)
 */
public class QRCode extends QRCodeData implements Serializable, Comparable<QRCode> {
    private String hash;
    private boolean has_photo;
    private boolean has_location;
    private transient Bitmap photo;
    //(x,y) style coordinate of geolocation -- x = latitude, y = longitude
    private ArrayList<Double> geolocation;

    /**
     * QRCode Constructor: takes in code and self computes associated hash and score
     * @param code
     *      String code passed in from which score and hash wil be computed
     */
    public QRCode(String code) {
        //Set code equal to passed in code and compute hash
        this.setCode(code);
        hash = DigestUtils.sha256Hex(code);

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
     * Gets hash of QRCode object
     * @return
     *      Hash of QRCode
     */
    public String getHash() {
        return hash;
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
     * Gets Photo associated with QRCode object
     * @return
     *      Photo of QRCode in the form of a Bitmap
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
        has_photo = true;
        this.photo = photo;
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
