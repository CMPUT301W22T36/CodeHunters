package com.cmput301w22t36.codehunters.Data;
/**
 * Introductory Comments:
 *      This Java file is a custom class representing the most abstract form of data.
 *      Anything that extends this class will be forced to have an id and getters and
 *      setters to this id. This id represents a document id for any of the data-types that extend this
 *      so that they can be stored on the firestore. *Comment class, User class, and QRCode class will extend this.
 *
 *      **No Outstanding Issues**
 */

/**
 * Abstract data class
 */
public abstract class Data {
    private String id;

    /**
     * Retrieves document ID of the data
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the document ID of the data to the one passed into the function
     * @param id
     *      document ID we're setting for the data object
     */
    public void setId(String id) {
        this.id = id;
    }
}
