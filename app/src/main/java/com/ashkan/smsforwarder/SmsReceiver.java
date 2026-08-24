package com.ashkan.smsforwarder;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Telephony;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsMessage;

import java.text.DateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) return;
        if (!Prefs.isEnabled(context)) return;

        String destination = Prefs.getDestination(context);
        if (destination == null || destination.trim().isEmpty()) return;

        if (context.checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            Context localized = LocaleHelper.wrap(context);
            Prefs.setLastEvent(context, localized.getString(R.string.event_permission_failed));
            ForwardNotification.show(context, localized.getString(R.string.notification_failed_title), localized.getString(R.string.notification_permission_body));
            return;
        }

        SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        if (messages == null || messages.length == 0) return;

        Map<String, StringBuilder> grouped = new LinkedHashMap<>();
        for (SmsMessage sms : messages) {
            String sender = sms.getDisplayOriginatingAddress();
            Context localized = LocaleHelper.wrap(context);
            if (sender == null) sender = localized.getString(R.string.unknown_sender);
            grouped.computeIfAbsent(sender, k -> new StringBuilder()).append(sms.getDisplayMessageBody());
        }

        for (Map.Entry<String, StringBuilder> entry : grouped.entrySet()) {
            String sender = entry.getKey();

            // Safety: do not forward messages coming from the configured destination itself.
            // This also helps prevent accidental loops if a user points the destination at this phone's own number.
            try {
                if (PhoneNumberUtils.compare(sender, destination)) {
                    continue;
                }
            } catch (Exception ignored) {}

            String body = entry.getValue().toString();
            Context localized = LocaleHelper.wrap(context);
            String forwarded = localized.getString(R.string.forwarded_message, sender, body);

            try {
                SmsSender.send(context, destination, forwarded);
                String when = DateFormat.getDateTimeInstance().format(new Date());
                Prefs.setLastEvent(context, localized.getString(R.string.event_forwarded, sender, when));
                ForwardNotification.show(context, localized.getString(R.string.notification_forwarded_title), localized.getString(R.string.notification_forwarded_body, sender));
            } catch (Exception e) {
                String when = DateFormat.getDateTimeInstance().format(new Date());
                Prefs.setLastEvent(context, localized.getString(R.string.event_failed, when));
                ForwardNotification.show(context, localized.getString(R.string.notification_failed_title), localized.getString(R.string.notification_failed_body));
            }
        }
    }
}
