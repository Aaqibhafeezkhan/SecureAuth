# Changelog

All notable changes to SecureAuth are documented here.

## [1.2.0] - 2026-11-26

### Added

- A pure-Java account-label validator and JVM tests for null, optional labels, Unicode, control characters, and length boundaries.
- RFC 4226 and RFC 6238 TOTP vectors, time-step boundary tests, and Base32 normalization and rejection cases.
- Instrumented regression coverage for encrypted save and restore, randomized IVs and ciphertext, GCM tamper detection, and Keystore key deletion.
- MIT license and repository security and contribution policies.

### Changed

- The Android application ID is now `io.github.aaqibhafeezkhan.secureauth`, so this version installs as a separate app. Existing saved accounts are not migrated.
- Account labels are optional, limited to 64 characters after trimming, and reject all ISO control characters.
- The TOTP refresh loop runs only while the activity is started.
- AES-GCM encryption uses a provider-generated IV with randomized encryption required and a 256-bit Android Keystore key.
- JCenter repositories were replaced with Maven Central.

### Removed

- Unused Login and Register activities and their layouts.
- Template unit and instrumented tests.
- Tracked IDE project files and the unused drawable asset.

### Security

- Disabled Android application backup.
- Enabled secure-window protection in non-debuggable builds.
- Added null-safe TOTP secret validation and generation behavior.
