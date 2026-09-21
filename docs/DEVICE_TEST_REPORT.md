# Device test report

## Latest submitted smoke-test evidence

The user reported a successful phone test and supplied four screenshots. Redacted copies are stored in [`screenshots/`](screenshots/):

- `success-1.jpg`: English help and phone tips
- `success-2.jpg`: language selector
- `success-3.jpg`: enabled forwarding and setup checklist
- `success-4.jpg`: enabled forwarding status and completed setup

Phone numbers and recent-message text were blurred before adding the images to the repository.

Status: **USER-CONFIRMED SUCCESSFUL PHONE TEST**. The maintainer reports that the phone test completed successfully. The supplied evidence shows setup complete, forwarding enabled, permissions ready, language selection, and the test-message/help flow. Device model, Android version, exact APK fingerprint, and a per-case pass/fail log were not recorded in the screenshots.

When a device is available, test the exact signed release APK with synthetic messages and record pass/fail for: clean install; first launch; language switch; permission grant, denial and revocation; forwarding disabled and enabled; incoming synthetic SMS and recipient receipt; multipart SMS; app and phone restart; invalid and changed destination; built-in test; and dual-SIM selection/behavior if applicable. Do not record IMEI, SIM numbers, private phone numbers, or real SMS bodies.
