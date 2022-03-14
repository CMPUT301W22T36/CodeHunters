package com.cmput301w22t36.codehunters;

import android.graphics.Bitmap;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

/**
 * Class: User
 * Initialize and track the user's attributes.
 */
// TODO: extends FSDataPoint
public class User {
    // the unique device IDs of the user's devices associated with their account
    private ArrayList<String> accountUUIDs;
    // the user's unique username
    private static String username;
    // the user's email for contact information
    private static String email;

    // TODO: update once QR class exists, and add the corresponding getters and setters
    /*
    // the QR code allowing a user to login to their account from another device
    private QR accountLoginQR;
    // the QR code to share their game profile
    private QR shareProfileQR;
    // the list of QR codes that this user has scanned
    private ArrayList<QR> UserQRList;
    */

    /**
     * Create a new user. An accountLoginQR and shareProfileQR will be created,
     *      and UserQRList will be initialized as an empty list.
     * @param //accountUUIDs an array of UUIDs indicating the devices linked to this user profile
     * @param //username the unique username that identifies the user
     * @param //email the user's email as optional contact information
     */
/*    public User(ArrayList<String> accountUUIDs, String username, String email) {
        setAccountUUIDs(accountUUIDs);
        setUsername(username);
        setEmail(email);

        // TODO: set the remaining variables and lists (accountLoginQR, shareProfileQR, UserQRList) as required
        // Generate and save the accountLoginQR
        // Generate and save the shareProfileQR
        // UserQRList will be initialized as an empty list.
    }*/

    // Getters and Setters
    public ArrayList<String> getAccountUUIDs() {
        return accountUUIDs;
    }

    public void setAccountUUIDs(ArrayList<String> accountUUIDs) {
        this.accountUUIDs = accountUUIDs;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        User.username = username;
    }

    public String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        User.email = email;
    }
}
