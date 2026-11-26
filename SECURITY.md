# Security Policy

## Supported versions

- The latest published release.
- The `main` branch.

No GitHub Release is currently published. Until one is available, `main` is the only published development version.

## Reporting a vulnerability

Use GitHub's **Report a vulnerability** action under the repository's **Security** tab to submit a private vulnerability report. Do not disclose suspected vulnerabilities in public issues before maintainers have had an opportunity to investigate.

## In scope

- Local TOTP secret storage and encryption.
- Android Keystore key creation, use, invalidation, and deletion.
- TOTP generation and Base32 credential validation.
- Sensitive local data exposure through application behavior.

## Out of scope

- Rooted, compromised, or maliciously modified devices and operating systems.
- Backend authentication, server-side sessions, APIs, or remote authorization, because SecureAuth has no backend.
- Recovery of secrets after device loss, application removal, or Keystore key invalidation.
