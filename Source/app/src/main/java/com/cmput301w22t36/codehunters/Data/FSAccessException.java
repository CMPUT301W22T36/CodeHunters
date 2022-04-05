package com.cmput301w22t36.codehunters.Data;

/** Used when an error occurs in data retrieval by DataMappers.
 * @author Connor
 */
public class FSAccessException extends Exception {
    public FSAccessException(String message) {
        super(message);
    }
}

