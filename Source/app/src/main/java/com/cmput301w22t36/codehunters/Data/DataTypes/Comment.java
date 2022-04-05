package com.cmput301w22t36.codehunters.Data.DataTypes;

import com.cmput301w22t36.codehunters.Data.Data;

/**
 * Introductory Comments:
 *      This Java file is a custom Comment class. The purpose of this class is to abstract all of the
 *      attributes and associated methods of a Comment made on a QRCode.
 *
 *      **No Outstanding Issues**
 */

/**
 * Represents a comment made on a QRCode "post"
 */
public class Comment extends Data {
    private String userRef;
    private String hashRef;
    private String comment;
    private long timestamp = System.currentTimeMillis();

    /**
     * Retrieve a reference to the user who made the comment
     * @return
     *      a reference to the user who made the comment
     */
    public String getUserRef() {
        return userRef;
    }

    /**
     * Set the userRef to the user who made the comment
     * @param userRef
     *      a reference to the user who made the comment
     */
    public void setUserRef(String userRef) {
        this.userRef = userRef;
    }

    /**
     * Get the string message of the comment
     * @return
     *      message contents of the comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * Set the message contents of the comment to be whatever is passed into the function
     * @param comment
     *      message contents of the comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Retrieve a reference to the hash that the comment was made on
     * @return
     *      a reference to the hash that the comment was made on
     */
    public String getHashRef() {
        return hashRef;
    }

    /**
     * Set the hashRef to the hashRef passed into the function
     * @param hashRef
     *      a reference to the hash that the comment was made on
     */
    public void setHashRef(String hashRef) {
        this.hashRef = hashRef;
    }

    /**
     * Get the timestamp at which this comment was posted
     * @return
     *      Timestamp of comment
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Set the timestamp of the comment to time passed into the function
     * @param timestamp
     *      Timestamp of comment
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
