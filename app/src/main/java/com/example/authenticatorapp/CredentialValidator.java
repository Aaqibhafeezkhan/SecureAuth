package com.example.authenticatorapp;

public final class CredentialValidator {
    private CredentialValidator() {
    }

    public static boolean isValidAccountLabel(String value) {
        if (value == null) {
            return false;
        }
        for (int index = 0; index < value.length(); index++) {
            if (Character.isISOControl(value.charAt(index))) {
                return false;
            }
        }
        return value.trim().length() <= 64;
    }
}
