package com.cmput301w22t36.codehunters;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class QRCodeClassTest {
    @Test
    public void testQRCode_Score_Hash() {
        QRCode code = new QRCode("BFG5DGW54");
        assertEquals("8227ad036b504e39fe29393ce170908be2b1ea636554488fa86de5d9d6cd2c32", code.getHash());
        assertEquals(19, code.getScore());
    }
}
