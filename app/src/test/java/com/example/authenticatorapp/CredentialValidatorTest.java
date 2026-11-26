package com.example.authenticatorapp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CredentialValidatorTest {
    @Test
    public void rejectsNull() {
        assertFalse(CredentialValidator.isValidAccountLabel(null));
    }

    @Test
    public void acceptsOptionalEmptyAndWhitespaceLabels() {
        assertTrue(CredentialValidator.isValidAccountLabel(""));
        assertTrue(CredentialValidator.isValidAccountLabel("   "));
    }

    @Test
    public void acceptsAsciiAndNonAsciiLetters() {
        assertTrue(CredentialValidator.isValidAccountLabel("Work account"));
        assertTrue(CredentialValidator.isValidAccountLabel("東京アカウント"));
        assertTrue(CredentialValidator.isValidAccountLabel("München"));
    }

    @Test
    public void rejectsIsoControlCharacters() {
        assertFalse(CredentialValidator.isValidAccountLabel("name\u0000"));
        assertFalse(CredentialValidator.isValidAccountLabel("name\n"));
        assertFalse(CredentialValidator.isValidAccountLabel("name\r"));
        assertFalse(CredentialValidator.isValidAccountLabel("name\t"));
    }

    @Test
    public void enforcesTrimmedLengthLimit() {
        assertTrue(CredentialValidator.isValidAccountLabel(repeat('a', 64)));
        assertTrue(CredentialValidator.isValidAccountLabel(repeat('a', 64) + "  "));
        assertFalse(CredentialValidator.isValidAccountLabel(repeat('a', 65)));
    }

    private String repeat(char value, int count) {
        StringBuilder result = new StringBuilder(count);
        for (int index = 0; index < count; index++) {
            result.append(value);
        }
        return result.toString();
    }
}
