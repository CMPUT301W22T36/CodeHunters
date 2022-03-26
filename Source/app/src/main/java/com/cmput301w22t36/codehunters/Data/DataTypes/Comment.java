package com.cmput301w22t36.codehunters.Data.DataTypes;

import com.cmput301w22t36.codehunters.Data.Data;

public class Comment extends Data {
    private String userRef;
    private String qrCodeRef;
    private String comment;

    public String getUserRef() {
        return userRef;
    }

    public void setUserRef(String userRef) {
        this.userRef = userRef;
    }

    public String getQrCodeRef() {
        return qrCodeRef;
    }

    public void setQrCodeRef(String qrCodeRef) {
        this.qrCodeRef = qrCodeRef;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
