package com.cmput301w22t36.codehunters;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.cmput301w22t36.codehunters.Data.DataMappers.QRCodeMapper;
import com.cmput301w22t36.codehunters.Data.DataTypes.QRCodeData;
import com.cmput301w22t36.codehunters.Data.DataTypes.User;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CodeMapperUnitTest {
    //@Test
    public void testMatchingCodes() throws InterruptedException {

        QRCodeMapper codeMapper = new QRCodeMapper();

        User testUser = new User();
        testUser.setUsername("TestUser");
        testUser.setEmail("testEmail@test.com");
        testUser.appendUdid("TESTUDID001");

        ArrayList<QRCode> testCodes = new ArrayList<>();

        testCodes.add(new QRCode("test1"));
        testCodes.add(new QRCode("test2"));
        testCodes.add(new QRCode("test3"));

        CountDownLatch latch = new CountDownLatch(testCodes.size());

        for (QRCode code : testCodes) {
            code.setUserRef("/users/" + "TESTUDID001");
            codeMapper.set(code, codeMapper.new CompletionHandler<QRCodeData>() {
                @Override
                public void handleSuccess(QRCodeData code) {
                    latch.countDown();
                }
            });
        }

        latch.await(2000, TimeUnit.MILLISECONDS);

        ArrayList<QRCode> matchableCodes = new ArrayList<>();

        matchableCodes.add(new QRCode("test1"));
        matchableCodes.add(new QRCode("test2"));
        matchableCodes.add(new QRCode("test4"));

        CountDownLatch secondLatch = new CountDownLatch(1);

        codeMapper.getMatchingCodes(testUser, matchableCodes,
                codeMapper.new CompletionHandler<ArrayList<QRCodeData>>() {
                    @Override
                    public void handleSuccess(ArrayList<QRCodeData> matchedCodes) {
                        assertEquals(3, matchableCodes.size());
                        assertTrue(matchedCodes.contains(new QRCode("test1")));
                        assertTrue(matchedCodes.contains(new QRCode("test2")));
                        assertFalse(matchedCodes.contains(new QRCode("test3")));
                        assertFalse(matchedCodes.contains(new QRCode("test4")));
                        secondLatch.countDown();
                    }
                });

        secondLatch.await(2000, TimeUnit.MILLISECONDS);
    }
}
