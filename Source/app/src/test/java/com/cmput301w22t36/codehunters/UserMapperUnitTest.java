package com.cmput301w22t36.codehunters;

import com.cmput301w22t36.codehunters.Data.DataMappers.UserMapper;
import com.cmput301w22t36.codehunters.Data.DataTypes.User;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class UserMapperUnitTest {
    CountDownLatch latch = new CountDownLatch(1);
    UserMapper um = new UserMapper();
    User user = new User();
    User user2;

    //@Test
    public void get() throws InterruptedException {

        user.setUsername("TestUser");
        user.setEmail("testEmail@test.com");
        user.appendUdid("TESTUDID001");

        um.create(user, um.new CompletionHandler<User>() {
            @Override
            public void handleSuccess(User data) {
                //assert(userEqualsUser(data, user));

                um.get(user.getId(), um.new CompletionHandler<User>() {
                   @Override
                   public void handleSuccess(User data) {
                       user2 = data;
                       latch.countDown();
                   }

                   @Override
                    public void handleError(Exception e) {
                       //assertTrue(false);
                       latch.countDown();
                   }
                });

            }
            @Override
            public void handleError (Exception e) {
                //assertTrue(false);
                latch.countDown();
            }
        });

        latch.await(2000, TimeUnit.MILLISECONDS);

        Assertions.assertTrue(userEqualsUser(user2, user));

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
