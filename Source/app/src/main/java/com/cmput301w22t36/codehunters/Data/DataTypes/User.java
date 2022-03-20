package com.cmput301w22t36.codehunters.Data.DataTypes;

import com.cmput301w22t36.codehunters.Data.Data;
import com.cmput301w22t36.codehunters.Data.DataMappers.QRCodeMapper;
import com.cmput301w22t36.codehunters.QRCode;

import java.util.ArrayList;

/**
 * Class: User
 *
 * Initialize and track a user's attributes.
 */
public class User extends Data {
    // the unique device IDs of the user's devices associated with their account
    private ArrayList<String> accountUUIDs;
    // the user's unique username
    private String username;
    // the user's email for contact information
    private String email;
    // the QR code allowing a user to login to their account from another device
    private QRCode accountLoginQR;
    // the QR code to share their game profile
    private QRCode shareProfileQR;
    // the list of QR codes that this user has scanned
    private ArrayList<QRCode> UserQRList;
    // Whether user is owner
    private Boolean isOwner;

    // Getters and Setters
    /**
     * Get the UUIDs associated with this unique username
     */
    public ArrayList<String> getAccountUUIDs() {
        return accountUUIDs;
    }

    /**
     * Set the list of UUIDs associated with this unique username
     * @param accountUUIDs: list of UUIDS
     */
    public void setAccountUUIDs(ArrayList<String> accountUUIDs) {
        this.accountUUIDs = accountUUIDs;
    }

    /**
     * Get the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the unique username
     * @param username: unique name to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the email
     * @param email: the contact information to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get the QR code allowing a user to login to their account from another device
     */
    public QRCode getAccountLoginQR() {
        return accountLoginQR;
    }

    /**
     * Set the QR code allowing a user to login to their account from another device
     * @param accountLoginQR: the QRCode to set
     */
    public void setAccountLoginQR(QRCode accountLoginQR) {
        this.accountLoginQR = accountLoginQR;
    }

    /**
     * Get the QR code to share their game profile
     */
    public QRCode getShareProfileQR() {
        return shareProfileQR;
    }

    /**
     * Set the QR code to share their game profile
     * @param shareProfileQR: the QRCode to set
     */
    public void setShareProfileQR(QRCode shareProfileQR) {
        this.shareProfileQR = shareProfileQR;
    }

    /**
     * Get the list of QR codes that this user has scanned
     */
    public ArrayList<QRCode> getUserQRList() {
        return UserQRList;
    }

    /**
     * Set the list of QR codes that this user has scanned
     * @param userQRList: the list of QR codes to set
     */
    public void setUserQRList(ArrayList<QRCode> userQRList) {
        UserQRList = userQRList;
    }
}
