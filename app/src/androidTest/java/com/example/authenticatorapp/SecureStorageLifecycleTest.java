package com.example.authenticatorapp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.util.Arrays;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SecureStorageLifecycleTest {
    private static final String PREFS = "secureauth_accounts";
    private static final String SECRET_KEY = "secret";
    private static final String ACCOUNT_KEY = "account";
    private static final String IV_KEY = "iv";
    private static final String KEY_ALIAS = "secureauth_totp_key";
    private static final String TEST_SECRET = "JBSWY3DPEHPK3PXP";
    private final Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

    @After
    public void cleanUp() throws Exception {
        SecureStorage.clear(context);
    }

    @Test
    public void saveThenLoadReturnsSameSecretAndLabel() throws Exception {
        SecureStorage.save(context, "lifecycle-test", TEST_SECRET);

        assertEquals(TEST_SECRET, SecureStorage.loadSecret(context));
        assertEquals("lifecycle-test", SecureStorage.loadAccount(context));
    }

    @Test
    public void consecutiveSavesUseDifferentIvAndCiphertext() throws Exception {
        SecureStorage.save(context, "lifecycle-test", TEST_SECRET);
        SharedPreferences preferences = preferences();
        byte[] firstIv = Base64.decode(preferences.getString(IV_KEY, null), Base64.NO_WRAP);
        byte[] firstCiphertext = Base64.decode(preferences.getString(SECRET_KEY, null), Base64.NO_WRAP);

        SecureStorage.save(context, "lifecycle-test", TEST_SECRET);
        byte[] secondIv = Base64.decode(preferences.getString(IV_KEY, null), Base64.NO_WRAP);
        byte[] secondCiphertext = Base64.decode(preferences.getString(SECRET_KEY, null), Base64.NO_WRAP);

        assertEquals(12, firstIv.length);
        assertEquals(12, secondIv.length);
        assertFalse(Arrays.equals(firstIv, secondIv));
        assertFalse(Arrays.equals(firstCiphertext, secondCiphertext));
    }

    @Test
    public void tamperedCiphertextFailsAuthentication() throws Exception {
        SecureStorage.save(context, "lifecycle-test", TEST_SECRET);
        SharedPreferences preferences = preferences();
        byte[] ciphertext = Base64.decode(preferences.getString(SECRET_KEY, null), Base64.NO_WRAP);
        ciphertext[0] ^= 1;
        preferences.edit().putString(SECRET_KEY, Base64.encodeToString(ciphertext, Base64.NO_WRAP)).commit();

        boolean rejected = false;
        try {
            SecureStorage.loadSecret(context);
        } catch (GeneralSecurityException expected) {
            rejected = true;
        }
        assertTrue(rejected);
    }

    @Test
    public void clearRemovesKeystoreAlias() throws Exception {
        SecureStorage.save(context, "lifecycle-test", TEST_SECRET);
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);
        assertTrue(keyStore.containsAlias(KEY_ALIAS));

        SecureStorage.clear(context);
        keyStore.load(null);

        assertFalse(keyStore.containsAlias(KEY_ALIAS));
    }

    private SharedPreferences preferences() {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }
}
