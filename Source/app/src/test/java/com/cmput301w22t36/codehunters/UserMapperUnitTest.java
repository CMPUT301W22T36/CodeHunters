package com.cmput301w22t36.codehunters;

import com.cmput301w22t36.codehunters.Data.DataMappers.UserMapper;
import com.cmput301w22t36.codehunters.Data.DataTypes.User;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

public class UserMapperUnitTest {
    @Test
    public void get() {
        CountDownLatch latch = new CountDownLatch(2);

        User user = new User();
        user.setUsername("TestUser");
        user.setEmail("testEmail@test.com");
        user.appendUdid("TESTUDID001");

        UserMapper um = new UserMapper();
        um.create(user, um.new CompletionHandler() {
            @Override
            public void handleSuccess(User data) {
                assert(userEqualsUser(data, user));

                um.get(user.getId(), um.new CompletionHandler() {
                   @Override
                   public void handleSuccess(User data) {
                       assert(userEqualsUser(data, user));
                       latch.countDown();
                   }

                   @Override
                    public void handleError(Exception e) {
                       assert(false);
                       latch.countDown();
                   }
                });

            }
            @Override
            public void handleError (Exception e) {
                assert(false);
                latch.countDown();
            }
        });
    }

    private boolean userEqualsUser(User u1, User u2) {
        if (u1.getId() == u2.getId()
            && u1.getEmail() == u2.getEmail()
            && u1.getUsername() == u2.getUsername()) {
            return true;
        }
        else {
            return false;
        }
    }
}
