package com.example.authenticatorapp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CredentialValidationTest {
    @Test public void acceptsStandardTotpSecret() {
        assertTrue(TotpGenerator.isValidSecret("JBSWY3DPEHPK3PXP"));
    }

    @Test public void rejectsMalformedTotpSecret() {
        assertFalse(TotpGenerator.isValidSecret("not-base32"));
    }

    @Test public void rejectsUndersizedTotpSecret() {
        assertFalse(TotpGenerator.isValidSecret("JBSWY3"));
    }
}
