package com.cmput301w22t36.codehunters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.cmput301w22t36.codehunters.Data.DataTypes.QRCodeData;
import com.cmput301w22t36.codehunters.QRCode;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

/**
 * QRCode class unit-tester to test QRCode self-hash/self-score features as well as comparator override
 */
public class QRCodeUnitTest {
    @Test
    /**
     * Test QRCode self-hash and self-scoring features
     */
    public void testQRCode_Score_Hash() {
        QRCode code = new QRCode("BFG5DGW54");
        assertEquals("8227ad036b504e39fe29393ce170908be2b1ea636554488fa86de5d9d6cd2c32", code.getHash());
        assertEquals(19, code.getScore());
    }

    @Test
    /**
     * Test QRCode overridden sort comparator based on code score
     */
    public void testQRCode_Sorting() {
        ArrayList<QRCode> codes = new ArrayList<QRCode>();
        QRCode code1 = new QRCode("BFG5DGW54");
        assertEquals("8227ad036b504e39fe29393ce170908be2b1ea636554488fa86de5d9d6cd2c32", code1.getHash());
        assertEquals(19, code1.getScore());
        codes.add(code1);
        QRCode code2 = new QRCode("BFG5DGW55");
        assertEquals("16e6622bc1407e2f887dc65e91cca60853783ec6dabb06dd0031d493e3997e64", code2.getHash());
        assertEquals(81, code2.getScore());
        codes.add(code2);
        QRCode code3 = new QRCode("AFG5DGW55");
        assertEquals("0a6ed1b5db59976899cd322a1ff63a23d63841e8edeb59250e5830e326af321d", code3.getHash());
        assertEquals(35, code3.getScore());
        codes.add(code3);
        //TEST SORT
        Collections.sort(codes, Collections.reverseOrder()); //Sort by descending order (highest-scoring codes first)
        assertEquals("16e6622bc1407e2f887dc65e91cca60853783ec6dabb06dd0031d493e3997e64", codes.get(0).getHash());
        assertEquals("0a6ed1b5db59976899cd322a1ff63a23d63841e8edeb59250e5830e326af321d", codes.get(1).getHash());
        assertEquals("8227ad036b504e39fe29393ce170908be2b1ea636554488fa86de5d9d6cd2c32", codes.get(2).getHash());
    }
}
