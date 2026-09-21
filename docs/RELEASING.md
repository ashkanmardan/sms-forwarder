# Release procedure

1. Complete CI, lint, unit tests, debug build, and real-device QA using synthetic data. Resolve critical security findings.
2. Confirm the production signing key's owner, provenance, secure backup, alias, and certificate SHA-256 fingerprint. Verify it is the intended persistent key. **Do not create a new key without maintainer authorization.** Never commit a key or password.
3. Configure signing through the four environment variables `SMSF_RELEASE_STORE_FILE`, `SMSF_RELEASE_STORE_PASSWORD`, `SMSF_RELEASE_KEY_ALIAS`, and `SMSF_RELEASE_KEY_PASSWORD`. Never put their values in command logs, repository files, issues, or CI artifacts. A release packaging request fails when any value is absent. Keep key material outside the repository.
4. Run `./gradlew clean lint test assembleRelease`. Verify the **signed** APK with `apksigner verify --verbose --print-certs` and `aapt dump badging`; record package ID, version name/code, min/target SDK, certificate fingerprint, and APK SHA-256.
5. Match the APK to the reviewed source commit, then create a GitHub Release with the signed `SMSForwarder-vX.Y.Z.apk` and `SHA256SUMS.txt`. Use release notes based on the changelog; never upload a debug APK as an official release.
6. Only after the verified release exists, consider removing the historical APK from the current tree in a normal commit. Do not rewrite history or force-push.

The v2.1.1 release is signed with the persistent production key, verified, tagged, and published. Keep the keystore and recovery password backed up outside Git.
