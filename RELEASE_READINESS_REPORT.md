# v2.1.1 release readiness report

- Release: `SMS Forwarder v2.1.1`
- Source commit/tag: `64effcc85bd0e262851f52fb5a033fcb13d6b2dd` / `v2.1.1`
- CI: main Android checks passed
- Lint: 0 errors, 12 warnings
- Tests: debug and release unit tests passed
- Release build: signed APK build passed
- Package/version: `com.ashkan.smsforwarder.persian`, `2.1.1`, versionCode `4`
- Certificate SHA-256: `af0c188fcf19864053bf9aaba4fac1701bf263d2df3512d21a5b7e5d959b9111`
- APK SHA-256: `6a267d4ad1aba2c21a2bade36b77e247244be06079c428fa7e283f378f2e75c9`
- Device QA: **PARTIAL / USER-CONFIRMED**. The maintainer confirmed successful real-device testing of the app/release candidate; exact artifact-to-device provenance and a per-case log are not independently documented. Screenshots are redacted in `docs/screenshots/`.
- SMS forwarding: user-confirmed on a real Android phone; exact APK provenance is not independently documented.
- Release URL: https://github.com/ashkanmardan/sms-forwarder/releases/tag/v2.1.1
- Assets: `SMSForwarder-v2.1.1.apk`, `SHA256SUMS.txt`; public download was rehashed and matched.
- Legacy APK cleanup: historical debug APK removed from the current tree; history was not rewritten.
- Secret scan: no tracked keystore, private key, password file, or environment secret found.
- Privacy/security: documented; no Internet permission or app server is used for forwarding.
- Known limitations: default-SIM behavior, dual-SIM coverage, limited loop protection, non-durable duplicate suppression, carrier fees, and lack of end-to-end SMS encryption.
