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

    private Integer bestScore;
    private Integer ScanCount;
    private Integer score;
    // the user's email for contact information
    private String email;
    // the QR code allowing a user to login to their account from another device
    private QRCode accountLoginQR;
    // the QR code to share their game profile
    private QRCode shareProfileQR;
    // the list of QR codes that this user has scanned
    private ArrayList<QRCode> UserQRList;
    // the unique device IDs of the user's devices associated with their account
    private ArrayList<String> udid = new ArrayList<String>();
    // Whether user is owner
    private Boolean isOwner;
    // the user's unique username
    private String username;

    public Integer getBestScore() {
        return bestScore;
    }

    public void setBestScore(Integer bestScore) {
        this.bestScore = bestScore;
    }

    public Integer getScanCount() {
        return ScanCount;
    }

    public void setScanCount(Integer scanCount) {
        ScanCount = scanCount;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Boolean getOwner() {
        return isOwner;
    }

    public void setOwner(Boolean owner) {
        isOwner = owner;
    }

    /**
     * Get the UUIDs associated with this unique username
     */
    public ArrayList<String> getUdid() {
        return udid;
    }

    public String getUdid(int i) {
        return this.udid.get(i);
    }

    /**
     * Set the list of UUIDs associated with this unique username
     * @param udid: list of UUIDS
     */
    public void setUdid(ArrayList<String> udid) {
        this.udid = udid;
    }

    public void setUdid(int i, String udid) {
        this.udid.set(i, udid);
    }

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
