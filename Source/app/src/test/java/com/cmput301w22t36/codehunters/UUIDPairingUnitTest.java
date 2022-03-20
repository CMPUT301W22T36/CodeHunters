package com.cmput301w22t36.codehunters;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

/**
 * Class: UUIDPairingUnitTest
 * Test the UUIDPairing class.
 */
public class UUIDPairingUnitTest {
    // Mock object
    private UUIDPairing mockPair() {
        UUIDPairing pair = new UUIDPairing();
        return pair;
    }

    @Test
    void testAttributesNonQR() {
        UUIDPairing pair = mockPair();

        String UUID1 = "142adbe77b149562";
        pair.setUUID(UUID1);
        String username = "John Doe";
        pair.setUsername(username);

        assertEquals(UUID1, pair.getUUID());
        assertEquals(username, pair.getUsername());
    }
}
