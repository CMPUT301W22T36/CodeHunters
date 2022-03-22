package com.cmput301w22t36.codehunters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.cmput301w22t36.codehunters.Data.DataTypes.User;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

/**
 * Class: UserUnitTest
 * Test the User class.
 */
public class UserUnitTest {
    // Mock object
    private User mockUser() {
        User user = new User();
        return user;
    }

    @Test
    void testAttributesNonQR() {
        User user = mockUser();

        String UUID1 = "142adbe77b149562";
        ArrayList<String> accountUUIDs = new ArrayList<String>();
        accountUUIDs.add(UUID1);
        user.setUdid(accountUUIDs);
        String username = "John Doe";
        user.setUsername(username);
        String email = "example@gmail.com";
        user.setEmail(email);

        assertEquals(accountUUIDs, user.getUdid());
        assertEquals(username, user.getUsername());
        assertEquals(email, user.getEmail());
    }
}
