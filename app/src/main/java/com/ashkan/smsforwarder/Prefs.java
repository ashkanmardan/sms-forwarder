package com.ashkan.smsforwarder;

import android.content.Context;
import android.content.SharedPreferences;

public final class Prefs {
    private static final String FILE = "sms_forwarder_prefs";
    private static final String KEY_DESTINATION = "destination";
    private static final String KEY_ENABLED = "enabled";
    private static final String KEY_LAST_EVENT = "last_event";
    private static final String KEY_LANGUAGE = "language";
    private static final String KEY_PERMISSIONS_REQUESTED = "permissions_requested";

    private Prefs() {}

    private static SharedPreferences prefs(Context context) {
        return context.getSharedPreferences(FILE, Context.MODE_PRIVATE);
    }

    public static String getDestination(Context context) {
        return prefs(context).getString(KEY_DESTINATION, "");
    }

    public static void setDestination(Context context, String value) {
        prefs(context).edit().putString(KEY_DESTINATION, value == null ? "" : value.trim()).apply();
    }

    public static boolean isEnabled(Context context) {
        return prefs(context).getBoolean(KEY_ENABLED, false);
    }

    public static void setEnabled(Context context, boolean enabled) {
        prefs(context).edit().putBoolean(KEY_ENABLED, enabled).apply();
    }

    public static String getLastEvent(Context context) {
        return prefs(context).getString(KEY_LAST_EVENT, context.getString(R.string.no_activity_yet));
    }

    public static void setLastEvent(Context context, String value) {
        prefs(context).edit().putString(KEY_LAST_EVENT, value).apply();
    }

    public static String getLanguage(Context context) {
        return prefs(context).getString(KEY_LANGUAGE, "");
    }

    public static void setLanguage(Context context, String value) {
        prefs(context).edit().putString(KEY_LANGUAGE, value == null ? "" : value.trim()).apply();
    }

    public static boolean werePermissionsRequested(Context context) {
        return prefs(context).getBoolean(KEY_PERMISSIONS_REQUESTED, false);
    }

    public static void markPermissionsRequested(Context context) {
        prefs(context).edit().putBoolean(KEY_PERMISSIONS_REQUESTED, true).apply();
    }
}
