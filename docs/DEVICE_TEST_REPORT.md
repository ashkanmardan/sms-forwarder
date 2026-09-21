# Device test report

## Latest submitted smoke-test evidence

The user reported a successful phone test and supplied four screenshots. Redacted copies are stored in [`screenshots/`](screenshots/):

- `success-1.jpg`: English help and phone tips
- `success-2.jpg`: language selector
- `success-3.jpg`: enabled forwarding and setup checklist
- `success-4.jpg`: enabled forwarding status and completed setup

Phone numbers and recent-message text were blurred before adding the images to the repository.

Status: **NOT RUN**. No connected test phone and no verified signed release APK were available during the 2026-09-20 repository audit. Device model, Android version, and app version under test: not recorded. No installation or forwarding success is claimed.

When a device is available, test the exact signed release APK with synthetic messages and record pass/fail for: clean install; first launch; language switch; permission grant, denial and revocation; forwarding disabled and enabled; incoming synthetic SMS and recipient receipt; multipart SMS; app and phone restart; invalid and changed destination; built-in test; and dual-SIM selection/behavior if applicable. Do not record IMEI, SIM numbers, private phone numbers, or real SMS bodies.
