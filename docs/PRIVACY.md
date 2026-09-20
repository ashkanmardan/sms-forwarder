# Privacy notice (source review, 2026-09-20)

SMS Forwarder reads the sender address and displayed body of incoming SMS delivered by Android while forwarding is enabled. It combines message parts in the received broadcast by sender, formats a new message containing the sender and body, and sends that message to the configured destination using `SmsManager`. A user-confirmed test action sends a localized synthetic message.

The destination, forwarding enabled state, language, permission-request flag, and a short last-event status are stored in app-private `SharedPreferences`. The status can contain a sender address and time. The app does not deliberately save complete SMS bodies in its own storage, and source review found no SMS-body logging. Android's SMS database, carrier records, recipient device, system backups, and notifications are outside this app's storage claim. App backup is disabled in the manifest.

The reviewed source declares no Internet permission and contains no server requests, analytics SDK, contact access, device-ID collection, or app export/backup function. The Gradle app has no runtime third-party dependencies. These are source-level observations, not a network traffic or device forensic test. **Forwarding still sends private SMS content through the cellular carrier and to the recipient.** Carrier charges and retention policies may apply.

Notifications and the app status show a sender address but not the SMS body. Notification visibility on the lock screen depends on device settings. The destination is not encrypted at rest by app code; Android app sandboxing and device security provide its protection. Clearing app data removes these preferences; removing the app normally removes its private data. Do not put real SMS content or numbers in issue reports.

Use this app only on a phone you control and with appropriate consent and authorization. For questions or corrections, open a public issue without private data; use a private GitHub security advisory for vulnerabilities.
