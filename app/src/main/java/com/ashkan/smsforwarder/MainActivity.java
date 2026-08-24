package com.ashkan.smsforwarder;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity {
    private static final List<String> SUPPORTED_LANGUAGES = Arrays.asList("fa", "en", "ar", "tr");
    private static final int REQ_PERMISSIONS = 901;
    private static final int NAVY = Color.rgb(20, 42, 66);
    private static final int TEAL = Color.rgb(0, 137, 123);
    private static final int MUTED = Color.rgb(91, 111, 129);

    private EditText destinationInput;
    private Switch enabledSwitch;
    private TextView permissionStatus;
    private TextView lastEvent;
    private TextView stateBadge;

    @Override
    protected void attachBaseContext(android.content.Context newBase) {
        super.attachBaseContext(LocaleHelper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.rgb(229, 241, 244));
        getWindow().setNavigationBarColor(Color.rgb(238, 247, 248));
        ForwardNotification.ensureChannel(this);
        setContentView(buildUi());
        loadState();
        refreshStatus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (permissionStatus != null) refreshStatus();
    }

    private View buildUi() {
        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setBackgroundColor(Color.rgb(238, 247, 248));
        LinearLayout root = column();
        root.setPadding(dp(18), dp(22), dp(18), dp(30));

        root.addView(text(R.string.app_tagline, 13, TEAL, true));
        TextView title = text(R.string.app_title, 27, NAVY, true);
        title.setPadding(0, dp(5), 0, 0);
        root.addView(title);
        TextView subtitle = text(R.string.app_subtitle, 15, MUTED, false);
        subtitle.setLineSpacing(0, 1.25f);
        subtitle.setPadding(0, dp(8), 0, dp(18));
        root.addView(subtitle);

        LinearLayout statusCard = card();
        LinearLayout statusRow = row();
        stateBadge = text(R.string.status_off, 13, Color.WHITE, true);
        stateBadge.setGravity(Gravity.CENTER);
        stateBadge.setPadding(dp(14), dp(6), dp(14), dp(6));
        statusRow.addView(text(R.string.status_label, 16, NAVY, true), new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        statusRow.addView(stateBadge);
        statusCard.addView(statusRow);
        permissionStatus = text("", 13, MUTED, false);
        permissionStatus.setPadding(0, dp(12), 0, 0);
        statusCard.addView(permissionStatus);
        root.addView(statusCard, spacedCard());

        LinearLayout settingsCard = card();
        settingsCard.addView(text(R.string.destination_title, 16, NAVY, true));
        TextView helper = text(R.string.destination_helper, 13, MUTED, false);
        helper.setPadding(0, dp(5), 0, dp(10));
        settingsCard.addView(helper);
        destinationInput = new EditText(this);
        destinationInput.setHint(R.string.destination_hint);
        destinationInput.setTextSize(17);
        destinationInput.setSingleLine(true);
        destinationInput.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        destinationInput.setTextDirection(View.TEXT_DIRECTION_LTR);
        destinationInput.setInputType(InputType.TYPE_CLASS_PHONE);
        destinationInput.setPadding(dp(14), 0, dp(14), 0);
        destinationInput.setBackground(rounded(Color.rgb(247, 250, 251), Color.rgb(196, 215, 220), 12, 1));
        settingsCard.addView(destinationInput, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(52)));
        enabledSwitch = new Switch(this);
        enabledSwitch.setText(R.string.enabled_switch);
        enabledSwitch.setTextColor(NAVY);
        enabledSwitch.setTextSize(15);
        enabledSwitch.setGravity(Gravity.CENTER_VERTICAL);
        enabledSwitch.setPadding(0, dp(13), 0, dp(8));
        enabledSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> updateBadge(isChecked));
        settingsCard.addView(enabledSwitch);
        Button save = primaryButton(R.string.save_button);
        save.setOnClickListener(v -> saveSettings());
        settingsCard.addView(save);
        Button language = secondaryButton("Language / زبان / اللغة / Dil");
        language.setOnClickListener(v -> showLanguageDialog());
        LinearLayout.LayoutParams languageParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(50));
        languageParams.topMargin = dp(9);
        settingsCard.addView(language, languageParams);
        root.addView(settingsCard, spacedCard());

        LinearLayout actionsCard = card();
        actionsCard.addView(text(R.string.setup_title, 16, NAVY, true));
        TextView setupHint = text(R.string.setup_hint, 13, MUTED, false);
        setupHint.setPadding(0, dp(5), 0, dp(10));
        actionsCard.addView(setupHint);
        Button permissions = secondaryButton(R.string.permission_button);
        permissions.setOnClickListener(v -> requestMissingPermissions());
        actionsCard.addView(permissions);
        Button test = secondaryButton(R.string.test_button);
        test.setOnClickListener(v -> confirmTest());
        LinearLayout.LayoutParams testParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(50));
        testParams.topMargin = dp(9);
        actionsCard.addView(test, testParams);
        root.addView(actionsCard, spacedCard());

        LinearLayout guideCard = card();
        guideCard.setBackground(rounded(Color.rgb(223, 242, 238), Color.TRANSPARENT, 16, 0));
        guideCard.addView(text(R.string.guide_title, 17, NAVY, true));
        guideCard.addView(guideStep("1", R.string.guide_1));
        guideCard.addView(guideStep("2", R.string.guide_2));
        guideCard.addView(guideStep("3", R.string.guide_3));
        guideCard.addView(text(R.string.guide_device_title, 15, NAVY, true));
        guideCard.addView(guideStep("4", R.string.guide_device_1));
        guideCard.addView(guideStep("5", R.string.guide_device_2));
        guideCard.addView(guideStep("6", R.string.guide_device_3));
        root.addView(guideCard, spacedCard());

        LinearLayout activityCard = card();
        activityCard.addView(text(R.string.last_activity_title, 16, NAVY, true));
        lastEvent = text("", 13, MUTED, false);
        lastEvent.setPadding(0, dp(7), 0, 0);
        lastEvent.setLineSpacing(0, 1.2f);
        activityCard.addView(lastEvent);
        root.addView(activityCard, spacedCard());

        TextView note = text(R.string.footer_note, 12, MUTED, false);
        note.setGravity(Gravity.CENTER);
        note.setPadding(dp(8), dp(8), dp(8), 0);
        root.addView(note);
        scroll.addView(root);
        return scroll;
    }

    private void loadState() {
        destinationInput.setText(Prefs.getDestination(this));
        enabledSwitch.setChecked(Prefs.isEnabled(this));
    }

    private void saveSettings() {
        String number = destinationInput.getText().toString().trim();
        boolean enabled = enabledSwitch.isChecked();
        if (enabled && number.isEmpty()) {
            Toast.makeText(this, getString(R.string.toast_enter_destination), Toast.LENGTH_LONG).show();
            return;
        }
        Prefs.setDestination(this, number);
        Prefs.setEnabled(this, enabled);
        Toast.makeText(this, enabled ? getString(R.string.toast_enabled) : getString(R.string.toast_saved), Toast.LENGTH_SHORT).show();
        if (enabled && !hasCorePermissions()) requestMissingPermissions();
        refreshStatus();
    }

    private void showLanguageDialog() {
        String[] codes = {"fa", "en", "ar", "tr"};
        String[] labels = {"فارسی", "English", "العربية", "Türkçe"};
        int checked = 0;
        String current = Prefs.getLanguage(this);
        for (int i = 0; i < codes.length; i++) {
            if (codes[i].equals(current)) {
                checked = i;
                break;
            }
        }
        final int[] selected = {checked};
        new AlertDialog.Builder(this)
                .setTitle("App language")
                .setSingleChoiceItems(labels, checked, (dialog, which) -> selected[0] = which)
                .setPositiveButton("Apply", (dialog, which) -> {
                    Prefs.setLanguage(this, codes[selected[0]]);
                    recreate();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private boolean hasCorePermissions() {
        return checkSelfPermission(Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestMissingPermissions() {
        List<String> permissions = new ArrayList<>();
        if (checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) permissions.add(Manifest.permission.RECEIVE_SMS);
        if (checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) permissions.add(Manifest.permission.SEND_SMS);
        if (Build.VERSION.SDK_INT >= 33 && checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) permissions.add(Manifest.permission.POST_NOTIFICATIONS);
        if (permissions.isEmpty()) {
            Toast.makeText(this, getString(R.string.toast_permissions_ready), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!shouldShowAnyPermissionRationale() && hasAnyCorePermissionMissing()) {
            showPermissionSettingsDialog();
            return;
        }
        requestPermissions(permissions.toArray(new String[0]), REQ_PERMISSIONS);
    }

    private void confirmTest() {
        String destination = destinationInput.getText().toString().trim();
        if (destination.isEmpty()) {
            Toast.makeText(this, getString(R.string.toast_enter_destination), Toast.LENGTH_LONG).show();
            return;
        }
        if (checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, getString(R.string.toast_allow_send_first), Toast.LENGTH_LONG).show();
            requestMissingPermissions();
            return;
        }
        new AlertDialog.Builder(this)
                .setTitle(R.string.test_confirm_title)
                .setMessage(getString(R.string.test_confirm_message, destination))
                .setNegativeButton(R.string.test_confirm_negative, null)
                .setPositiveButton(R.string.test_confirm_positive, (dialog, which) -> {
                    try {
                        SmsSender.send(this, destination, getString(R.string.test_sms_body));
                        Toast.makeText(this, getString(R.string.toast_test_sent), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(this, getString(R.string.toast_test_failed), Toast.LENGTH_LONG).show();
                    }
                }).show();
    }

    private void refreshStatus() {
        boolean receive = checkSelfPermission(Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED;
        boolean send = checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED;
        permissionStatus.setText(receive && send ? getString(R.string.status_ready) : getString(R.string.status_needs_permissions));
        permissionStatus.setTextColor(receive && send ? TEAL : Color.rgb(184, 92, 38));
        lastEvent.setText(Prefs.getLastEvent(this));
        updateBadge(enabledSwitch.isChecked());
    }

    private void updateBadge(boolean enabled) {
        if (stateBadge == null) return;
        stateBadge.setText(enabled ? "فعال" : "خاموش");
        stateBadge.setBackground(rounded(enabled ? TEAL : Color.rgb(116, 133, 146), Color.TRANSPARENT, 20, 0));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_PERMISSIONS) {
            refreshStatus();
            if (!hasCorePermissions()) {
                showPermissionSettingsDialog();
            }
        }
    }

    private boolean hasAnyCorePermissionMissing() {
        return checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED;
    }

    private boolean shouldShowAnyPermissionRationale() {
        return shouldShowRequestPermissionRationale(Manifest.permission.RECEIVE_SMS)
                || shouldShowRequestPermissionRationale(Manifest.permission.SEND_SMS);
    }

    private void showPermissionSettingsDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.permission_dialog_title)
                .setMessage(R.string.permission_dialog_message)
                .setNegativeButton(R.string.permission_dialog_negative, null)
                .setPositiveButton(R.string.permission_dialog_positive, (dialog, which) -> openAppSettings())
                .show();
    }

    private void openAppSettings() {
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", getPackageName(), null));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private LinearLayout column() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        return layout;
    }

    private LinearLayout row() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER_VERTICAL);
        layout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        return layout;
    }

    private LinearLayout card() {
        LinearLayout card = column();
        card.setPadding(dp(16), dp(16), dp(16), dp(16));
        card.setBackground(rounded(Color.WHITE, Color.rgb(221, 232, 235), 16, 1));
        card.setElevation(dp(2));
        return card;
    }

    private LinearLayout.LayoutParams spacedCard() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.bottomMargin = dp(12);
        return params;
    }

    private TextView text(String value, int size, int color, boolean bold) {
        TextView view = new TextView(this);
        view.setText(value);
        view.setTextSize(size);
        view.setTextColor(color);
        view.setGravity(Gravity.RIGHT);
        view.setTextDirection(View.TEXT_DIRECTION_RTL);
        view.setTypeface(Typeface.create("sans", bold ? Typeface.BOLD : Typeface.NORMAL));
        return view;
    }

    private TextView text(int resId, int size, int color, boolean bold) {
        return text(getString(resId), size, color, bold);
    }

    private View guideStep(String number, int bodyResId) {
        LinearLayout line = row();
        line.setPadding(0, dp(10), 0, 0);
        TextView marker = text(number, 13, Color.WHITE, true);
        marker.setGravity(Gravity.CENTER);
        marker.setBackground(rounded(TEAL, Color.TRANSPARENT, 16, 0));
        line.addView(marker, new LinearLayout.LayoutParams(dp(28), dp(28)));
        TextView copy = text(bodyResId, 14, NAVY, false);
        LinearLayout.LayoutParams copyParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        copyParams.setMarginStart(dp(10));
        line.addView(copy, copyParams);
        return line;
    }

    private Button primaryButton(String label) {
        Button button = new Button(this);
        button.setText(label);
        button.setTextSize(15);
        button.setTextColor(Color.WHITE);
        button.setTypeface(Typeface.DEFAULT_BOLD);
        button.setAllCaps(false);
        button.setBackground(rounded(TEAL, Color.TRANSPARENT, 13, 0));
        button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(52)));
        return button;
    }

    private Button primaryButton(int resId) {
        return primaryButton(getString(resId));
    }

    private Button secondaryButton(String label) {
        Button button = new Button(this);
        button.setText(label);
        button.setTextSize(14);
        button.setTextColor(TEAL);
        button.setTypeface(Typeface.DEFAULT_BOLD);
        button.setAllCaps(false);
        button.setBackground(rounded(Color.WHITE, TEAL, 13, 1));
        button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(50)));
        return button;
    }

    private Button secondaryButton(int resId) {
        return secondaryButton(getString(resId));
    }

    private GradientDrawable rounded(int fill, int stroke, int radiusDp, int strokeDp) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(fill);
        drawable.setCornerRadius(dp(radiusDp));
        if (strokeDp > 0) drawable.setStroke(dp(strokeDp), stroke);
        return drawable;
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
