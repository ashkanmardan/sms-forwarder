# Security policy

## Supported versions

Only the current `main` branch is maintained. No signed production release has been verified. The historical repository APK must not be treated as a supported release.

## Private reporting

Use [GitHub private vulnerability reporting](https://github.com/ashkanmardan/sms-forwarder/security/advisories/new). Do not include real SMS contents, phone numbers, private keys, or device identifiers. If private reporting is unavailable, open a minimal public issue requesting a private channel without disclosing exploit details.

## Security model and known limits

- The exported SMS receiver requires `android.permission.BROADCAST_SMS`; the sent-result receiver is not exported. The launcher activity is exported as required for launch. Android delivers SMS broadcasts and enforces the protected broadcast permission.
- The app checks `SEND_SMS` before forwarding. A valid destination is required at save, test, receive, and send boundaries. It does not verify ownership of the destination number.
- A message from a sender matching the destination is skipped. This normally stops a direct two-phone loop when sender IDs compare correctly, but does **not** reliably stop multi-hop cycles or number-format/caller-ID mismatches. Do not configure forwarding cycles.
- There is no durable duplicate detection or rate limit. Retransmitted broadcasts, device/carrier behavior, or repeated sends may cause duplicates and charges.
- Multipart parts are appended by sender in broadcast order; incomplete, malformed, or cross-message batches may be reconstructed incorrectly. Null message objects and null bodies are skipped. This path needs device-level tests.
- The app uses `SmsManager.getDefault()` and does not select a SIM explicitly; dual-SIM behavior is unverified.
- The destination and last status are stored in app-private, unencrypted preferences. The last status may contain a sender address. `allowBackup=false` is set, but a compromised/unlocked device remains a risk.
- No full SMS body is intentionally logged or shown in notifications. Android system, carrier, and recipient handling is outside app control. Sending via SMS is not end-to-end encrypted.

See [privacy](docs/PRIVACY.md), [audit](docs/OPEN_SOURCE_AUDIT.md), and [testing](docs/TESTING.md). Security-related changes require tests and review before release.
