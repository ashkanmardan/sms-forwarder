# SMS Forwarder

[![Android checks](https://github.com/ashkanmardan/sms-forwarder/actions/workflows/build-apk.yml/badge.svg)](https://github.com/ashkanmardan/sms-forwarder/actions/workflows/build-apk.yml)

## Overview

SMS Forwarder is a small Java Android app that forwards incoming SMS from a phone you control to one configured number through that phone's SIM and carrier. It is intended for authorized personal use. Forwarding is off by default.

## Features

- Configure one destination and enable or disable forwarding.
- Confirm and send a built-in synthetic test SMS.
- See permission guidance and a short status message.
- Use Persian, English, Arabic, Turkish, or German in the app.

## Screenshots

No verified, redacted screenshots have been supplied yet. See [device testing](docs/DEVICE_TEST_REPORT.md).

## How it works

Android delivers `SMS_RECEIVED` to `SmsReceiver`. If forwarding is enabled, a valid destination is saved, and `SEND_SMS` is granted, the app formats the received sender and message body and sends a multipart SMS with `SmsManager`. The app skips messages whose sender compares equal to the destination. That check is only partial loop protection; see [security notes](SECURITY.md).

## Requirements

- Android 6.0 or newer (API 23), a phone with SMS capability, and a working SIM/service.
- SMS receive and send permission. Carrier fees may apply.
- For building: JDK 17, Android SDK 35, and the checked-in Gradle wrapper.

## Installation and download

The app is intended for sideloading. A verified signed GitHub Release is **not available yet**. When one is published, use [GitHub Releases](https://github.com/ashkanmardan/sms-forwarder/releases/latest). The APK in `releases/` is a historical debug-signed file whose embedded version disagrees with its filename; do not treat it as an official release.

To build a debug APK from this source, run `./gradlew assembleDebug` (`gradlew.bat assembleDebug` on Windows). The output is `app/build/outputs/apk/debug/app-debug.apk`. Debug builds are for testing only.

## Permissions

| Permission | Purpose |
| --- | --- |
| `RECEIVE_SMS` | Receive incoming SMS broadcasts for forwarding. |
| `SEND_SMS` | Send forwarded and user-confirmed test SMS. |
| `POST_NOTIFICATIONS` | Show forwarding/failure status on Android 13+. Forwarding itself does not require this permission. |

The manifest also declares required telephony hardware. Permission requests occur in the app UI; a denied permission can stop forwarding. Android and carrier restrictions may apply.

## Privacy and security

The destination and enabled state are saved in private `SharedPreferences`; the app does not save full SMS bodies in its own preferences. The source declares no Internet permission and contains no analytics or server client. Sending forwarded SMS **does transmit its content through the cellular carrier and to the destination**. The app is not an end-to-end encrypted messenger. Read the [privacy notice](docs/PRIVACY.md) and [security policy](SECURITY.md) before use.

## Supported languages

Persian, English, Arabic, Turkish, and German resource sets are present. The initial language follows Android's resource selection until the user chooses one in the app.

## Build from source and testing

`./gradlew clean lint test assembleDebug` runs the local checks. See [testing](docs/TESTING.md) for coverage and device gaps. The same checks run in [CI](.github/workflows/build-apk.yml); the badge reflects GitHub's live result, which must be checked separately from a local build.

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md), [roadmap](ROADMAP.md), and [code of conduct](CODE_OF_CONDUCT.md). Never include real SMS bodies, phone numbers, signing keys, or private identifiers in issues or pull requests.

## Responsible use and limitations

Use only on a device you control, with appropriate authorization and consent. Do not use it for covert monitoring. There is no verified production signing key or device QA yet. The app uses the default `SmsManager`, has limited loop detection and no durable duplicate suppression, and has not been verified on dual-SIM phones. See [audit](docs/OPEN_SOURCE_AUDIT.md).

## License and maintainer

Project-owned source and documentation are offered under the [MIT License](LICENSE). Android SDK and Gradle components retain their own licenses. Maintained by [Ashkan Mardanpour](https://github.com/ashkanmardan).

### فارسی

این برنامه پیامک‌های دریافتی را با سیم‌کارت گوشی به شماره‌ای که کاربر تنظیم کرده می‌فرستد. فقط روی دستگاه تحت کنترل خود و با رضایت و مجوز لازم استفاده کنید. پیامک از طریق اپراتور و به شمارهٔ مقصد منتقل می‌شود. فایل موجود در پوشهٔ `releases` انتشار رسمی و تأییدشده نیست.
