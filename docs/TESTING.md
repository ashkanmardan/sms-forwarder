# Testing

Run `./gradlew clean lint test assembleDebug` with JDK 17 and Android SDK 35. Windows uses `gradlew.bat`. `DestinationValidatorTest` checks accepted boundary lengths and rejects null, empty, malformed, whitespace, and multi-recipient input. UI save, built-in test, receiver, and sender use that same validator.

The unit suite does not exercise Android broadcast delivery, multipart reconstruction, SMS carrier delivery, permissions, persistence, language rendering, notifications, or loop behavior. Those need Android instrumentation and real-device tests with **synthetic SMS only**. A passing Gradle `test` task is not proof of forwarding on a phone.

Before any production release, follow [device test report](DEVICE_TEST_REPORT.md) on the exact signed APK, including forwarding off/on, denied/revoked permissions, multipart messages, app/device restart, invalid destination, changed destination, test action, and dual-SIM where supported. Record failures without private data.
