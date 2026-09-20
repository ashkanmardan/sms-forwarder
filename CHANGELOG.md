# Changelog

## [Unreleased]

- Add open-source documentation, community templates, Gradle wrapper, and CI checks.
- Remove obsolete manifest `package` attribute while retaining the application ID.
- Validate destinations at every send entry point, ignore null SMS bodies, and avoid an API 24-only map method on API 23.
- Change app version to 2.1.1 (code 4) because these source changes affect the executable. No public release is claimed.

Git history records earlier changes but does not establish a signed, source-matched 2.1.0 release. The historical APK embeds version 2.0.0 despite its filename.
