# Release procedure

1. Complete CI, lint, unit tests, debug build, and real-device QA using synthetic data. Resolve critical security findings.
2. Confirm the production signing key's owner, provenance, secure backup, alias, and certificate SHA-256 fingerprint. Verify it is the intended persistent key. **Do not create a new key without maintainer authorization.** Never commit a key or password.
3. Configure signing locally outside the repository or through protected CI secrets. The repository intentionally contains no release signing configuration or credentials.
4. Run `./gradlew clean lint test assembleRelease`. Verify the **signed** APK with `apksigner verify --verbose --print-certs` and `aapt dump badging`; record package ID, version name/code, min/target SDK, certificate fingerprint, and APK SHA-256.
5. Match the APK to the reviewed source commit, then create a GitHub Release with the signed `SMSForwarder-vX.Y.Z.apk` and `SHA256SUMS.txt`. Use release notes based on the changelog; never upload a debug APK as an official release.
6. Only after the verified release exists, consider removing the historical APK from the current tree in a normal commit. Do not rewrite history or force-push.

Current stop: no persistent production signing key/provenance is established, and the historical file is debug-signed and embeds version 2.0.0 despite its 2.1.0 filename. **Do not publish a public release yet.**
