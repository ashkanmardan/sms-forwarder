# Device test report

## Latest submitted smoke-test evidence

The user reported a successful phone test and supplied four screenshots. Redacted copies are stored in [`screenshots/`](screenshots/):

- `success-1.jpg`: English help and phone tips
- `success-2.jpg`: language selector
- `success-3.jpg`: enabled forwarding and setup checklist
- `success-4.jpg`: enabled forwarding status and completed setup

Phone numbers and recent-message text were blurred before adding the images to the repository.

Status: **PARTIAL / USER-CONFIRMED**. The maintainer confirmed successful real-device testing of the app/release candidate. The supplied evidence shows setup complete, forwarding enabled, permissions ready, language selection, and the test-message/help flow. Exact artifact-to-device provenance and a per-case pass/fail log are not independently documented. Device model and Android version are unknown.

For future evidence, test the exact signed release APK with synthetic messages and record pass/fail for: clean install; first launch; language switch; permission grant, denial and revocation; forwarding disabled and enabled; incoming synthetic SMS and recipient receipt; multipart SMS; app and phone restart; invalid and changed destination; built-in test; and dual-SIM selection/behavior if applicable. Do not record IMEI, SIM numbers, private phone numbers, or real SMS bodies.
