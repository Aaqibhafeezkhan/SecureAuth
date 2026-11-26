# SecureAuth

SecureAuth is an offline Android authenticator that generates six-digit TOTP codes from a manually entered Base32 secret.

## What it is

SecureAuth stores one TOTP account locally and generates codes on the device. The secret is encrypted with AES-GCM using an Android Keystore key before it is saved in app-private SharedPreferences. The app has no backend, network access, synchronization, or password-based user account system.

## Features

- HMAC-SHA1 TOTP with six digits and 30-second time steps.
- Manual Base32 secret entry and optional account label.
- AES-GCM encrypted secret storage with an Android Keystore AES key.
- Account restore after app restart without repopulating the secret input.
- Explicit local account deletion that clears preferences and deletes the Keystore key.
- Backup disabled and secure-window protection enabled in non-debuggable builds.
- JVM tests for RFC 4226 and RFC 6238 vectors, Base32 normalization, and account-label validation.
- Instrumented tests for encrypted storage lifecycle and GCM tamper detection.

## Security design

| Asset or threat | Control | Where implemented | How verified |
| --- | --- | --- | --- |
| TOTP secret at rest | AES-GCM with a 256-bit Android Keystore key and a fresh provider-generated IV | SecureStorage.java, SecureStorage | SecureStorageLifecycleTest; manual review |
| Ciphertext modification | GCM authentication tag checked during decryption | SecureStorage.java, SecureStorage.loadSecret | SecureStorageLifecycleTest.tamperedCiphertextFailsAuthentication |
| Secret exposed through backup | Android backup disabled | AndroidManifest.xml | Manual review |
| Codes captured in screenshots or recents | FLAG_SECURE in non-debuggable builds | MainActivity.java, MainActivity | Manual review |
| Unnecessary background refresh | Refresh loop runs only while the activity is started | MainActivity.java, MainActivity.onStart/onStop | Manual review |
| Invalid TOTP input | Base32 validation, minimum decoded secret length, and null handling | TotpGenerator.java, TotpGenerator | TotpGeneratorTest, TotpSecretValidationTest |
| Unsafe account labels | Optional label, 64-character trimmed limit, ISO control-character rejection | CredentialValidator.java, CredentialValidator | CredentialValidatorTest |
| Credential deletion | Synchronous preference clearing and Keystore key deletion | SecureStorage.java, SecureStorage.clear | SecureStorageLifecycleTest |

## How it works

~~~mermaid
flowchart TD
    A[Enter optional account label and Base32 secret] --> B[Validate label and secret]
    B --> C[Encrypt secret with Android Keystore AES-GCM]
    C --> D[Store ciphertext, IV, and label in app-private SharedPreferences]
    D --> E[Restore and decrypt on app start]
    E --> F[Generate and refresh six-digit TOTP code locally]
    F --> G[Delete account]
    G --> H[Clear preferences and delete Keystore key]
~~~

## Build and test

The wrapper uses Gradle 6.5 and the top-level build uses Android Gradle Plugin 4.1.1. The app targets Android SDK platform 30, specifies Build Tools 30.0.3, and compiles Java 8 bytecode. Use JDK 8 with the Android SDK platform and Build Tools installed and licensed.

Run JVM unit tests:

~~~sh
./gradlew testDebugUnitTest
~~~

Build a debug APK:

~~~sh
./gradlew assembleDebug
~~~

Run instrumented tests on a connected Android device or emulator:

~~~sh
./gradlew connectedDebugAndroidTest
~~~

JVM unit tests run on the host. Instrumented tests require an Android device or emulator and exercise Android Keystore and SharedPreferences.

## Limitations

- One active account is supported.
- Secrets must be entered manually as Base32; there is no otpauth:// import or QR scanner.
- The implementation supports SHA-1, six digits, and a 30-second period only.
- There is no biometric or device-credential gate.
- There is no backup, export, or recovery mechanism.
- The TOTP secret exists in process memory while the app is active.
- Code validity depends on the device clock.
- Debuggable builds allow screenshots; non-debuggable builds set FLAG_SECURE.
- The Android application ID is now io.github.aaqibhafeezkhan.secureauth. Version 1.2.0 installs as a separate app; accounts saved under com.example.authenticatorapp are not migrated.

## Roadmap

- Add a biometric or device-credential gate before revealing codes.
- Support multiple accounts with independently managed encrypted secrets.
- Add otpauth:// import and QR scanning.
- Support SHA-256 and SHA-512, eight-digit codes, and configurable periods.
- Add encrypted export and import with storage format versioning and authenticated metadata.

## Release and signing

Builds and releases are deliberately local; this repository does not use GitHub Actions. Create a release keystore outside the repository and configure signing locally. Verify release artifacts before distribution. Never commit keystores, passwords, signing properties, or real TOTP secrets.

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md) for build, test, commit, and DCO requirements. Sign off every commit.

## License

[MIT](LICENSE).
