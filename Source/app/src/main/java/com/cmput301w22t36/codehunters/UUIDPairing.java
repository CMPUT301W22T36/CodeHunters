package com.cmput301w22t36.codehunters;

/**
 * Class: UUIDPairing
 * Initialize and track the pairs of unique usernames with the UUID of a device.
 */
// TODO: extends FSDataPoint
public class UUIDPairing {
    // the unique device ID
    private static String UUID;
    // the user's unique username paired with that UUID
    private static String username;

    // Getters and Setters
    public String getUUID() {
        return UUID;
    }

    public static void setUUID(String UUID) {
        UUIDPairing.UUID = UUID;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        UUIDPairing.username = username;
    }
}
