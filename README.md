# SMS Forwarder

Simple Android app for forwarding incoming SMS messages to a chosen phone number.

## Overview

SMS Forwarder is a lightweight Android app built for personal use on a phone you control. It receives incoming SMS messages and forwards them through the same device using the SIM card already installed on the phone.

## Features

- Simple setup flow with clear on-screen guidance
- In-app permission help for SMS access
- Built-in test SMS action
- Multi-language support
- Offline-first behavior
- Local-only storage for settings and status

## Languages

- Persian
- English
- Arabic
- Turkish

The app language can be changed from inside the app.

## How It Works

1. Install the APK on an Android phone with SMS capability.
2. Open the app and enter the destination number in international format.
3. Allow SMS receive and send permissions.
4. Enable forwarding.
5. Run the built-in test SMS to confirm setup.

## APK

The current APK is included in this repository:

`releases/SMSForwarder-v2.1.0.apk`

## Device Notes

- Samsung: open App info, then Permissions, then allow SMS. If blocked, open the three-dot menu and allow Restricted settings.
- Xiaomi: open App info, then Other permissions, then allow SMS and Autostart if shown.
- If Android still blocks SMS, reopen the app after changing permissions.

## Important Notes

- Forwarded messages are sent from the device SIM card.
- Carrier SMS charges may apply.
- Message content is not uploaded to any remote server.
- This project is intended for personal sideload use, not Google Play publishing.

## Project Structure

- `app/src/main/java/com/ashkan/smsforwarder/`: app logic
- `app/src/main/res/`: UI and localized strings
- `releases/`: packaged APK files

## Contact

Ashkan Mardanpour  
Email: hv1j@live.com  
Phone: +98 918 859 3897
