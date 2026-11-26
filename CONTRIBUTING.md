# Contributing

## Build

Use the checked-in Gradle wrapper with JDK 8 and Android SDK platform 30 with Build Tools 30.0.3.

\`\`\`sh
./gradlew assembleDebug
\`\`\`

## Test

\`\`\`sh
./gradlew testDebugUnitTest
./gradlew connectedDebugAndroidTest
\`\`\`

Instrumented tests require a connected Android device or emulator.

## Commits

Use Conventional Commits such as fix:, test:, docs:, or chore:. Sign off every commit with the Developer Certificate of Origin:

\`\`\`sh
git commit -s
\`\`\`

Every commit must contain a matching Signed-off-by trailer. Do not commit signing material, real TOTP secrets, generated APKs, or IDE state.
