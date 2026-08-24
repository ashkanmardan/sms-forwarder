package com.ashkan.smsforwarder;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.os.Build;

import java.util.Locale;

public final class LocaleHelper {
    private LocaleHelper() {}

    public static ContextWrapper wrap(Context context) {
        String language = Prefs.getLanguage(context);
        if (language == null || language.isEmpty()) {
            return new ContextWrapper(context);
        }

        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Configuration configuration = new Configuration(context.getResources().getConfiguration());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocale(locale);
            configuration.setLayoutDirection(locale);
            return new ContextWrapper(context.createConfigurationContext(configuration));
        }

        configuration.locale = locale;
        context.getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());
        return new ContextWrapper(context);
    }
}
