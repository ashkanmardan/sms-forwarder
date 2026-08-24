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
            Prefs.setLastEvent(context, "پیامک دریافت شد، اما دسترسی ارسال پیامک داده نشده است.");
            ForwardNotification.show(context, "پیامک منتقل نشد", "دسترسی ارسال پیامک داده نشده است.");
            return;
        }

        SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        if (messages == null || messages.length == 0) return;

        Map<String, StringBuilder> grouped = new LinkedHashMap<>();
        for (SmsMessage sms : messages) {
            String sender = sms.getDisplayOriginatingAddress();
            if (sender == null) sender = "فرستنده ناشناس";
            grouped.computeIfAbsent(sender, k -> new StringBuilder()).append(sms.getDisplayMessageBody());
        }

        for (Map.Entry<String, StringBuilder> entry : grouped.entrySet()) {
            String sender = entry.getKey();

            // Safety: do not forward messages coming from the configured destination itself.
            // This also helps prevent accidental loops if a user points the destination at this phone's own number.
            try {
                if (!"فرستنده ناشناس".equals(sender) && PhoneNumberUtils.compare(sender, destination)) {
                    continue;
                }
            } catch (Exception ignored) {}

            String body = entry.getValue().toString();
            String forwarded = "[انتقال پیامک]\nاز طرف: " + sender + "\n\n" + body;

            try {
                SmsSender.send(context, destination, forwarded);
                String when = DateFormat.getDateTimeInstance().format(new Date());
                Prefs.setLastEvent(context, "پیامکِ " + sender + " در " + when + " منتقل شد.");
                ForwardNotification.show(context, "پیامک منتقل شد", "پیام دریافتی از " + sender + " ارسال شد.");
            } catch (Exception e) {
                String when = DateFormat.getDateTimeInstance().format(new Date());
                Prefs.setLastEvent(context, "انتقال پیامک در " + when + " ناموفق بود.");
                ForwardNotification.show(context, "خطا در انتقال پیامک", "پیامک دریافتی ارسال نشد.");
            }
        }
    }
}
