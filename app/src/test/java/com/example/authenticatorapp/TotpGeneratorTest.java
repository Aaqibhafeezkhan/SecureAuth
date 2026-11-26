package com.example.authenticatorapp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TotpGeneratorTest {
    private static final String RFC_SECRET = "GEZDGNBVGY3TQOJQGEZDGNBVGY3TQOJQ";

    @Test
    public void generatesExistingRfc6238Example() throws Exception {
        assertEquals("287082", TotpGenerator.generate(RFC_SECRET, 59_000L));
    }

    @Test
    public void generatesRfc4226AppendixDVectors() throws Exception {
        String[] expected = {
                "755224", "287082", "359152", "969429", "338314",
                "254676", "287922", "162583", "399871", "520489"
        };
        for (int counter = 0; counter < expected.length; counter++) {
            assertEquals(expected[counter], TotpGenerator.generate(RFC_SECRET, counter * 30_000L));
        }
    }

    @Test
    public void generatesRfc6238Sha1Vectors() throws Exception {
        long[] timestamps = {
                1_111_111_109_000L,
                1_111_111_111_000L,
                1_234_567_890_000L,
                2_000_000_000_000L,
                20_000_000_000_000L
        };
        String[] expected = {"081804", "050471", "005924", "279037", "353130"};
        for (int index = 0; index < timestamps.length; index++) {
            assertEquals(expected[index], TotpGenerator.generate(RFC_SECRET, timestamps[index]));
        }
    }

    @Test
    public void codeRemainsStableWithinThirtySecondWindow() throws Exception {
        assertEquals(TotpGenerator.generate(RFC_SECRET, 0L),
                TotpGenerator.generate(RFC_SECRET, 29_999L));
        assertEquals(TotpGenerator.generate(RFC_SECRET, 30_000L),
                TotpGenerator.generate(RFC_SECRET, 59_999L));
    }

    @Test
    public void normalizesBase32CaseWhitespaceHyphensAndPadding() throws Exception {
        String canonical = TotpGenerator.generate("JBSWY3DPEHPK3PXP", 59_000L);
        assertEquals(canonical, TotpGenerator.generate("jbswy3dpehpk3pxp", 59_000L));
        assertEquals(canonical, TotpGenerator.generate("JBSW Y3DP EHPK 3PXP", 59_000L));
        assertEquals(canonical, TotpGenerator.generate("JBSW-Y3DP-EHPK-3PXP", 59_000L));
        assertEquals(canonical, TotpGenerator.generate("JBSWY3DPEHPK3PXP====", 59_000L));
    }

    @Test
    public void rejectsEmptyBlankNullAndInvalidBase32Characters() {
        assertFalse(TotpGenerator.isValidSecret(""));
        assertFalse(TotpGenerator.isValidSecret("   "));
        assertFalse(TotpGenerator.isValidSecret(null));
        assertFalse(TotpGenerator.isValidSecret("JBSWY3DPEHPK3PX0"));
        assertFalse(TotpGenerator.isValidSecret("JBSWY3DPEHPK3PX1"));
        assertFalse(TotpGenerator.isValidSecret("JBSWY3DPEHPK3PX8"));
        assertFalse(TotpGenerator.isValidSecret("JBSWY3DPEHPK3PX9"));
    }

    @Test
    public void enforcesMinimumDecodedSecretLength() {
        assertFalse(TotpGenerator.isValidSecret("GEZDGNBVGY3TQOJ"));
        assertTrue(TotpGenerator.isValidSecret("GEZDGNBVGY3TQOJQ"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void generateRejectsNullSecret() throws Exception {
        TotpGenerator.generate(null, 0L);
    }
}
