package com.cmput301w22t36.codehunters;

import android.graphics.Bitmap;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.lang.Math;
import java.util.Hashtable;

public class QRCode implements Serializable, Comparable<QRCode> {
    private String hash;
    private int score;
    private String code;
    private boolean has_photo;
    private boolean has_location;
    private Bitmap photo;
    //(x,y) style coordinate of geolocation -- x = latitude, y = longitude
    private ArrayList<Double> geolocation;

    public QRCode(String code) {
        //Set code equal to passed in code and compute hash
        this.code = code;
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
                    score += (int) Math.pow(number, length-1);
                } catch (NumberFormatException ex) {
                    score += (int) Math.pow(letters_values.get(value), length-1);
                }
                length = 1;
            }
        }

        has_photo = false;
        has_location = false;
    }

    public String getHash() {
        return hash;
    }

    public int getScore() {
        return score;
    }

    public String getCode() {
        return code;
    }

    public ArrayList<Double> getGeolocation() {
        return geolocation;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        has_photo = true;
        this.photo = photo;
    }

    public void setGeolocation(ArrayList<Double> geolocation) {
        has_location = true;
        this.geolocation = geolocation;
    }

    @Override
    public int compareTo(QRCode qrCode) {
        if (this.score > qrCode.getScore()) {
            return 1;
        } else if (this.score < qrCode.getScore()) {
            return -1;
        } else {
            return 0;
        }
    }
}
