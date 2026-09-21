# Open-source readiness report — current state

## Repository State

`main` is the active branch. PR #1 was merged without history rewriting or force-push. The current cleanup commit is `950203f`.

## OSS Foundation

MIT licensing and scope are documented in [LICENSE](LICENSE) and [NOTICE.md](NOTICE.md). The repository includes README, SECURITY, PRIVACY, CONTRIBUTING, CODE_OF_CONDUCT, ROADMAP, issue templates, pull request template, Dependabot configuration, and Android CI.

## Release

`SMS Forwarder v2.1.1` is public at [GitHub Releases](https://github.com/ashkanmardan/sms-forwarder/releases/tag/v2.1.1). Official binaries are distributed through Releases, not the source tree.

## Signing

A persistent production signing identity exists outside Git. The v2.1.1 APK was built and verified with that identity; no signing material or password is tracked.

## CI

The latest main Android checks passed: clean, lint, unit tests, and assembleDebug. See the [workflow run](https://github.com/ashkanmardan/sms-forwarder/actions/runs/35594087772).

## APK Verification

- Package: `com.ashkan.smsforwarder.persian`
- Version: `2.1.1` / versionCode `4`
- minSdk: `23`; targetSdk: `35`
- Certificate SHA-256: `af0c188fcf19864053bf9aaba4fac1701bf263d2df3512d21a5b7e5d959b9111`
- APK SHA-256: `6a267d4ad1aba2c21a2bade36b77e247244be06079c428fa7e283f378f2e75c9`
- Public download was rehashed and matched `SHA256SUMS.txt`.

## Device QA

**PARTIAL / USER-CONFIRMED.** The maintainer confirmed successful real-device testing of the app/release candidate, including forwarding and setup flow, and supplied four redacted screenshots. Exact artifact-to-device provenance and a per-case test log are not independently documented. See [device test report](docs/DEVICE_TEST_REPORT.md).

## Privacy

The app uses carrier SMS to send forwarded content to the configured destination. Source review found no Internet permission, analytics, app server, contacts access, or device-ID collection. Read [docs/PRIVACY.md](docs/PRIVACY.md).

## Security

Permissions, exported receiver behavior, destination validation, null handling, multipart handling, and limited loop protection are documented. Known limitations include reciprocal loops, duplicates, default-SIM behavior, and unencrypted private preferences. See [SECURITY.md](SECURITY.md).

## Screenshots

Four redacted real-device screenshots are stored in [docs/screenshots](docs/screenshots). They contain no phone numbers, SMS bodies, OTPs, or personal identifiers.

## Remaining Risks

Dual-SIM and carrier-specific behavior need broader coverage. Loop protection and duplicate suppression remain limited. Carrier fees may apply, and SMS is not end-to-end encrypted. Keep the production key and recovery password backed up outside Git.

## OSS Program Readiness

The project is **READY TO APPLY** for general open-source distribution and relevant repository security features. Specialized programs remain **POSSIBLE / APPLY WITH REALISTIC EXPECTATIONS** or **WAIT FOR MORE MATURITY** as described in [the program assessment](docs/OPEN_SOURCE_PROGRAM_READINESS.md). No acceptance, users, downloads, or external adoption are claimed.

## Recommended Next Actions

1. Maintain backups of the production signing key and recovery password outside Git.
2. Add more synthetic-data device coverage for OEM and dual-SIM behavior.
3. Keep CI, dependency updates, and release checksums maintained for future releases.
