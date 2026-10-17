package com.example.authenticatorapp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import android.content.Context;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SecureStorageLifecycleTest {
    private final Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

    @After
    public void cleanUp() throws Exception {
        SecureStorage.clear(context);
    }

    @Test
    public void deletingAccountRemovesStoredCredentialAndLabel() throws Exception {
        SecureStorage.save(context, "lifecycle-test", "JBSWY3DPEHPK3PXP");

        SecureStorage.clear(context);

        assertNull(SecureStorage.loadSecret(context));
        assertEquals("", SecureStorage.loadAccount(context));
    }
}
