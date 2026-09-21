# Open-source audit — 2026-09-20

## Scope and structure

Reviewed the five reachable Git commits, tracked files, Android manifest, all seven Java classes, localized resources, vector icon, Gradle configuration, CI, README, and historical APK. The repository has one app module; `MainActivity` builds the UI and requests permissions; `Prefs` stores settings; `SmsReceiver` handles incoming broadcasts; `SmsSender` uses `SmsManager`; `SmsSentReceiver` handles send failures; `ForwardNotification` posts status; `LocaleHelper` applies user language. There is no app database or service.

`SmsReceiver` receives `SMS_RECEIVED`, checks enabled/destination/permission, obtains message parts from Android, groups by displayed sender, formats sender plus body, and calls `SmsSender`. The sender divides text and sends multipart SMS via the default SIM manager. The app skips a sender matching the destination. `Prefs` uses app-private `SharedPreferences` for destination, enabled state, language, permission-request flag, and last event; that event may contain a sender address. No full body is deliberately saved. `MainActivity` requests `RECEIVE_SMS`, `SEND_SMS`, and on Android 13+ `POST_NOTIFICATIONS`; it also has a user-confirmed test send.

Resources contain Persian, English, Arabic, Turkish, and German. Java source targets 17. The Gradle app uses namespace `com.ashkan.smsforwarder`, application ID `com.ashkan.smsforwarder.persian`, compile/target SDK 35, min SDK 23, and AGP 8.7.3. Gradle 8.14.3 wrapper was added (AGP 8.7 requires at least Gradle 8.9). JUnit is test-only; there are no app runtime third-party dependencies. The launcher icon is a repository vector and no externally attributed asset is tracked. Git history gives no independent proof of ownership for translations or assets, so imported material must be reviewed if discovered.

The previous GitHub Actions workflow used globally provisioned Gradle and only built debug. All five visible runs on the old workflow failed; the current manifest `package` attribute caused the reported AGP error. The replacement workflow runs wrapper clean, lint, test, and debug build for pushes to main, pull requests, and manual dispatch. Its first [draft PR run](https://github.com/ashkanmardan/sms-forwarder/actions/runs/35514550554) passed.

## Findings

| Severity | Finding and disposition |
| --- | --- |
| **CRITICAL** | At the initial audit no persistent production signing key or provenance was established. A new key was subsequently created after explicit maintainer authorization, but portable backup and signed-build verification remain open. The historical `releases/SMSForwarder-v2.1.0.apk` verifies as **debug-signed** and embeds `2.0.0`/code 2, not its filename's `2.1.0` or current source. Do not publish it as a release. |
| **HIGH** | No real-device QA of a signed APK. SMS delivery, permissions, multipart behavior, and dual-SIM handling remain unverified. Release gate remains closed. |
| **HIGH** | Loop prevention checks only sender equals destination; this usually stops a direct two-phone loop, but multi-hop cycles or sender-ID mismatches and duplicate broadcasts can create repeated sends/cost. No durable duplicate suppression or rate limit. Documented for device review. |
| **MEDIUM** | Receiver previously used `Map.computeIfAbsent`, unavailable on API 23 despite minSdk 23. Replaced with API-compatible map operations. Null SMS objects/bodies are skipped. |
| **MEDIUM** | Destination validation was only at UI save, leaving the test and sender paths inconsistent. Shared validation now guards save, test, receive, and send; unit tests cover valid and malformed formats. |
| **MEDIUM** | Destination and last sender status are stored without app-level encryption in private preferences. Backup is disabled; device compromise or visible notifications remain risks. |
| **MEDIUM** | Exported SMS receiver is protected by `BROADCAST_SMS`; sender-result receiver is private. Activity is exported for launcher use. Language restart formerly reused the incoming intent; it now creates an explicit activity intent. |
| **MEDIUM** | The original README exposed a personal phone number and email. Removed from current README. They remain in public Git history because history rewriting is prohibited; maintainer should assess privacy implications without force-push. |
| **LOW** | No runtime analytics, server client, Internet, contacts, or device-identifier permission found in reviewed source. This is source inspection, not a device traffic test. |
| **LOW** | Lint warnings remain to be evaluated separately; a passing lint task does not mean warning-free. |
| **OPTIONAL** | Add real, redacted screenshots, instrumentation tests, accessibility review, and dependency/security automation after device QA. |

The manifest declares only `RECEIVE_SMS`, `SEND_SMS`, and `POST_NOTIFICATIONS`, plus required telephony hardware. All are explained in README. `allowBackup=false` is set. No unused sensitive permission was proven. The historical APK described by this audit was removed from the current tree after v2.1.1 publication and is not a download source.

The separate desktop checkout contains uncommitted 2.2.0 work with remote SMS command handling. Its optional text-secret check and sender-number matching are not sufficient evidence for safe public remote control; it has no corresponding automated security tests. That work is excluded from this 2.1.1 release candidate until separately reviewed and hardened. No files in that checkout were changed during this audit.

## Secret and privacy scan

A value-redacted scan of all five original reachable text revisions and the working tree found a personal email and phone candidate only in old README revisions; the current README has neither. One distinct email identity also appears in Git commit metadata. Number-shaped values in localization resources occur in `destination_hint` examples; current test/doc number candidates are synthetic fixtures or dates. No credential assignments, private-key markers, signing files, or private-URL candidates were found in the scanned text. This heuristic scan is not proof that no secret exists, and binary APK content was not treated as source text. No history rewrite was performed.

## Version and release decision

Current edited source is `2.1.1`/code 4 because destination enforcement and API 23 compatibility alter executable behavior. There is no official GitHub Release as of this audit. The historical APK SHA-256 is `D9593939834E1F142ECCFAB6C9B66A09D4FFB8CD5699DACFD1DD02B6211F4B8B`; its certificate SHA-256 fingerprint is `af54c9601a3ee45c66a216462efc372a65d4257049aac16b7a2536cd02997553` and its subject identifies an Android debug certificate. It cannot establish source provenance or a production signing lineage. No release build is considered publishable until [release gates](RELEASING.md) are met.
