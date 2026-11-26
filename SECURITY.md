# Security Policy

## Supported versions

- `main`: supported development branch.
- No published GitHub Release is currently available.

Security fixes are developed against the latest state of `main`.

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
