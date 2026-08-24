package com.ashkan.smsforwarder;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.SmsManager;

import java.util.ArrayList;

public final class SmsSender {
    private SmsSender() {}

    public static void send(Context context, String destination, String text) {
        if (destination == null || destination.trim().isEmpty()) {
            throw new IllegalArgumentException("Destination number is empty");
        }

        SmsManager manager = SmsManager.getDefault();
        ArrayList<String> parts = manager.divideMessage(text);

        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flags |= PendingIntent.FLAG_IMMUTABLE;
        }

        Intent sentIntent = new Intent(context, SmsSentReceiver.class)
                .setAction("com.ashkan.smsforwarder.SMS_SENT");
        PendingIntent sentPendingIntent = PendingIntent.getBroadcast(
                context, 1001, sentIntent, flags);

        ArrayList<PendingIntent> sentIntents = new ArrayList<>();
        for (int i = 0; i < parts.size(); i++) {
            sentIntents.add(sentPendingIntent);
        }

        manager.sendMultipartTextMessage(
                destination.trim(),
                null,
                parts,
                sentIntents,
                null
        );
    }
}
