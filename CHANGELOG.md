# Changelog

## [2.1.1] - 2026-09-21

- Add open-source documentation, community templates, Gradle wrapper, and CI checks.
- Remove obsolete manifest `package` attribute while retaining the application ID.
- Validate destinations at every send entry point, ignore null SMS bodies, and avoid an API 24-only map method on API 23.
- Change app version to 2.1.1 (code 4) because these source changes affect the executable.
- Require four out-of-repository environment values for release packaging; no signing credential is committed.

The official signed binary is distributed through [GitHub Releases](https://github.com/ashkanmardan/sms-forwarder/releases/tag/v2.1.1). Historical commits are retained without rewriting history.
