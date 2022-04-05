package com.cmput301w22t36.codehunters.Data.DataTypes;

import com.cmput301w22t36.codehunters.Data.Data;
import com.cmput301w22t36.codehunters.QRCode;

import java.util.ArrayList;

/**
 * Class: User
 *
 * Initialize and track a user's attributes.
 */
public class User extends Data {

    // the user's game statistics
    private Integer bestScore = 0;
    private Integer ScanCount = 0;
    private Integer score = 0;
    // the user's email for contact information
    private String email = "";
    // the QR code allowing a user to login to their account from another device
    private QRCode accountLoginQR;
    // the QR code to share their game profile
    private QRCode shareProfileQR;
    // the list of QR codes that this user has scanned
    private ArrayList<QRCode> UserQRList;
    // the unique device IDs of the user's devices associated with their account
    private ArrayList<String> udid = new ArrayList<String>();
    // Whether user is owner
    private Boolean isOwner = false;
    // the user's unique username
    private String username;

    /**
     * Get the user's best scoring QR code's score
     */
    public Integer getBestScore() {
        return bestScore;
    }

    /**
     * Set the user's best scoring QR code's score
     * @param bestScore: the corresponding score
     */
    public void setBestScore(Integer bestScore) {
        this.bestScore = bestScore;
    }

    /**
     * Get the number of codes user has scanned
     */
    public Integer getScanCount() {
        return ScanCount;
    }

    /**
     * Set the number of codes user has scanned
     * @param scanCount: the number of codes scanned
     */
    public void setScanCount(Integer scanCount) {
        ScanCount = scanCount;
    }

    /**
     * Get the user's total score
     */
    public Integer getScore() {
        return score;
    }

    /**
     * Set the user's total score
     * @param score: the total score
     */
    public void setScore(Integer score) {
        this.score = score;
    }

    /**
     * Get the flag indicating if the user is an Owner
     */
    public Boolean getOwner() {
        return isOwner;
    }

    /**
     * Set the flag indicating if the user is an Owner
     * @param owner: the flag indicating user is an owner or player
     */
    public void setOwner(Boolean owner) {
        isOwner = owner;
    }

    /**
     * Get the UUIDs associated with this unique username
     */
    public ArrayList<String> getUdid() {
        return udid;
    }

    /**
     * Get the single uuid associated with this unique username
     * @param i: the identifier for the udid being accessed
     */
    public String getUdid(int i) {
        return this.udid.get(i);
    }

    /**
     * Set the list of UUIDs associated with this user profile
     * @param udid: list of UUIDS
     */
    public void setUdid(ArrayList<String> udid) {
        this.udid = udid;
    }

    /**
     * Set the single UUIDs associated with this user profile
     * @param i: the identifier for the udid being added
     * @param udid: the new udid
     */
    public void setUdid(int i, String udid) {
        this.udid.set(i, udid);
    }

    /**
     * Append the uuid to this user profile
     * @param udid: the new udid
     */
    public void appendUdid(String udid) {
        this.udid.add(udid);
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
