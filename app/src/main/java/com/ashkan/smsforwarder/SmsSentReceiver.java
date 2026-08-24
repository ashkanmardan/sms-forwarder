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
            Context localized = LocaleHelper.wrap(context);
            Prefs.setLastEvent(context, localized.getString(R.string.event_failed, when));
            ForwardNotification.show(context, localized.getString(R.string.notification_failed_title), localized.getString(R.string.notification_failed_body));
        }
    }
}
