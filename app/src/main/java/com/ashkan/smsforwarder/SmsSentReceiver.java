package com.ashkan.smsforwarder;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.text.DateFormat;
import java.util.Date;

public class SmsSentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (getResultCode() != Activity.RESULT_OK) {
            String when = DateFormat.getDateTimeInstance().format(new Date());
            Prefs.setLastEvent(context, "ارسال پیامک در " + when + " ناموفق بود.");
            ForwardNotification.show(context, "خطا در انتقال پیامک", "پیامک منتقل‌شده ارسال نشد.");
        }
    }
}
