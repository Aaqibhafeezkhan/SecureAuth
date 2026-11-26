# Contributing

## Build

Use the checked-in Gradle wrapper and the JDK and Android SDK versions documented in [README.md](README.md).

```sh
./gradlew assembleDebug
```

## Tests

Run JVM unit tests:

```sh
./gradlew testDebugUnitTest
```

Instrumented tests require a connected Android device or emulator:

```sh
./gradlew connectedDebugAndroidTest
```

## Commits

Use Conventional Commits, for example `fix:`, `test:`, `docs:`, and `chore:`.

Sign off every commit with the Developer Certificate of Origin:

```sh
git commit -s
```

Every commit must include a matching `Signed-off-by` trailer. Do not commit signing material, real TOTP secrets, generated APKs, or local IDE state.
