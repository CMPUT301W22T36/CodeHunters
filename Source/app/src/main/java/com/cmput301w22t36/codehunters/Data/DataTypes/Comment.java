package com.cmput301w22t36.codehunters.Data.DataTypes;

import com.cmput301w22t36.codehunters.Data.Data;

public class Comment extends Data {
    private String userRef;
    private String hashRef;
    private String comment;
    private long timestamp = System.currentTimeMillis();

    public String getUserRef() {
        return userRef;
    }

    public void setUserRef(String userRef) {
        this.userRef = userRef;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getHashRef() {
        return hashRef;
    }

    public void setHashRef(String hashRef) {
        this.hashRef = hashRef;
    }
}
