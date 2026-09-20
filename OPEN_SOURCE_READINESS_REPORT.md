# Open-source readiness report — 2026-09-20

## Repository state

Work is on `chore/open-source-readiness` from `a819975`, published as [draft PR #1](https://github.com/ashkanmardan/sms-forwarder/pull/1). Five original reachable commits and all tracked source files were audited. No history rewrite or force-push was performed. Relevant GitHub repository topics were set.

## License

MIT license added for project-owned material, with a [scope notice](NOTICE.md). No runtime third-party library or attributed imported asset was found. Git history alone cannot independently prove ownership of every translation or icon; contributors must identify imported material.

## CI

The old public `Build APK` workflow has five visible failed runs. The replacement uses the checked-in Gradle 8.14.3 wrapper and runs clean, lint, tests, and debug build on main pushes, pull requests, and manual dispatch. A local equivalent passed. [Draft PR #1's workflow](https://github.com/ashkanmardan/sms-forwarder/actions/runs/35514550554) completed successfully on the implementation commit.

## Tests

`testDebugUnitTest` and `testReleaseUnitTest` each ran two `DestinationValidatorTest` cases, with zero failures or errors. This covers destination syntax at meaningful bounds and malformed values. Broadcast delivery and carrier behavior remain untested.

## Build

Local `clean lint test assembleDebug` succeeded on JDK 17 and Android SDK 35. Lint has **0 errors and 12 warnings**. Warnings include target API age, locale handling, RTL/accessibility details, data extraction rules, and minor SDK/resource observations. None was disabled to force a pass; they remain follow-up work.

## APK

The new **debug-only** APK embeds `com.ashkan.smsforwarder.persian`, version `2.1.1`/code 4, min SDK 23, target SDK 35. Its local SHA-256 is `0EB69B2C7209016143C4C63540F00AF45564D59B30828630426BDDF6ADC64704`; this hash is for a local test artifact, not a release.

The tracked `releases/SMSForwarder-v2.1.0.apk` is historical: embedded version `2.0.0`/code 2, Android debug certificate, SHA-256 `D9593939834E1F142ECCFAB6C9B66A09D4FFB8CD5699DACFD1DD02B6211F4B8B`. It is not source-matched to current code and must not be promoted.

## Signing

No persistent production key or signing configuration was found in the repository or related local project copy. No key was created or replaced. A signed release build was **not run** because the stated signing stop condition applies. See [release procedure](docs/RELEASING.md).

## Device QA

Not run: `adb devices` listed no attached device. The exact signed release APK does not exist. See [device checklist](docs/DEVICE_TEST_REPORT.md).

## Privacy

Incoming SMS sender/body are read and transmitted by carrier SMS to the chosen destination when enabled. Preferences hold destination and state; a status can hold the sender. No full SMS body is deliberately saved or logged by app code. Reviewed source has no Internet permission, analytics, contacts access, server client, or device-ID collection. These are source observations, not device traffic verification. See [privacy notice](docs/PRIVACY.md).

## Security

The protected exported SMS receiver, private sent-result receiver, permissions, backup setting, destination validation, null handling, multipart path, and limited loop check were reviewed. Known risks: reciprocal forwarding loops, duplicates, incomplete multipart assembly, default-SIM behavior, and unencrypted private preferences. The language restart now uses an explicit intent. See [security policy](SECURITY.md) and [audit](docs/OPEN_SOURCE_AUDIT.md).

## Permissions

`RECEIVE_SMS` receives incoming messages; `SEND_SMS` forwards and sends a user-confirmed test; `POST_NOTIFICATIONS` shows status on Android 13+. Telephony hardware is required. No unused sensitive permission was proven.

## Screenshots

No actual app screenshots were supplied or captured, so none were added.

## GitHub Release

GitHub's public releases API returned no releases at audit time. **No public release or checksum asset was created** because signing provenance and device QA are missing. The historical APK stays in the tree as flagged evidence until a verified release exists.

## Remaining risks

Production key provenance, device tests, remote CI result, and source-to-APK verification remain release gates. Previous README contact details remain in public Git history under the no-rewrite constraint. A heuristic scan found no credential assignments, private-key markers, or signing files in reachable text; this is not a guarantee.

## OSS program readiness

[Program assessment](docs/OPEN_SOURCE_PROGRAM_READINESS.md): JetBrains project support should wait for maturity; individual non-commercial use may be possible. BrowserStack and PVS-Studio are relevant but conditional. Snyk may be useful. GitHub's public-repository security settings are ready for maintainer review; no acceptance is claimed.

## Recommended next actions

1. Review draft PR #1 and require the final head commit's GitHub workflow to pass before merging.
2. Maintainer: identify the existing intended production key, its owner, secure backup, and certificate fingerprint; if none exists, explicitly authorize creation of a persistent key and agree how it will be stored. Do not send key bytes or passwords in an issue.
3. Build and verify the source-matched signed 2.1.1 APK, then perform the synthetic-data device checklist and resolve release-blocking findings.
4. Only then create a public GitHub Release with signed APK and SHA256SUMS, and consider removing the legacy APK in a normal later commit.
