package com.cmput301w22t36.codehunters;

/**
 * Class: UUIDPairing
 * Initialize and track the pairs of unique usernames with the UUID of a device.
 */
// TODO: DELETE CLASS
public class UUIDPairing {
    // the unique device ID
    private static String UUID;
    // the user's unique username paired with that UUID
    private static String username;

    // Getters and Setters
    /**
     * Get the UUID associated with this unique username
     */
    public String getUUID() {
        return UUID;
    }

    /**
     * Set the UUID associated with this unique username
     * @param UUID: the UUID to set
     */
    public static void setUUID(String UUID) {
        UUIDPairing.UUID = UUID;
    }

    /**
     * Get the username associated with this UUID
     */
    public static String getUsername() {
        return username;
    }

    /**
     * Set the username associated with this UUID
     * @param username: the username to set
     */
    public static void setUsername(String username) {
        UUIDPairing.username = username;
    }
}
