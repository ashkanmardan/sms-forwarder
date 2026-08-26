package com.ashkan.smsforwarder;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.content.res.ColorStateList;
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
import java.util.List;

public class MainActivity extends Activity {
    private static final int REQ_PERMISSIONS = 901;
    private static final int NAVY = Color.rgb(30, 58, 95);
    private static final int TEAL = Color.rgb(0, 121, 107);
    private static final int MUTED = Color.rgb(71, 85, 105);
    private static final int BACKGROUND = Color.rgb(248, 250, 252);
    private static final int BORDER = Color.rgb(203, 213, 225);
    private static final int WARNING = Color.rgb(180, 83, 9);

    private EditText destinationInput;
    private Switch enabledSwitch;
    private TextView permissionStatus;
    private TextView lastEvent;
    private TextView stateBadge;
    private TextView setupProgress;
    private LinearLayout guideContent;

    @Override
    protected void attachBaseContext(android.content.Context newBase) {
        super.attachBaseContext(LocaleHelper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(BACKGROUND);
        getWindow().setNavigationBarColor(BACKGROUND);
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
        scroll.setBackgroundColor(BACKGROUND);
        LinearLayout root = column();
        root.setPadding(dp(18), dp(22), dp(18), dp(30));

        TextView brand = text("SF", 14, Color.WHITE, true);
        brand.setGravity(Gravity.CENTER);
        brand.setBackground(rounded(NAVY, Color.TRANSPARENT, 14, 0));
        root.addView(brand, new LinearLayout.LayoutParams(dp(44), dp(44)));
        root.addView(text(R.string.app_tagline, 13, TEAL, true));
        TextView title = text(R.string.app_title, 29, NAVY, true);
        title.setPadding(0, dp(5), 0, 0);
        root.addView(title);
        TextView subtitle = text(R.string.app_subtitle, 15, MUTED, false);
        subtitle.setLineSpacing(0, 1.25f);
        subtitle.setPadding(0, dp(8), 0, dp(18));
        root.addView(subtitle);

        LinearLayout statusCard = card();
        statusCard.setBackground(rounded(Color.rgb(233, 242, 248), Color.rgb(191, 214, 229), 20, 1));
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
        setupProgress = text("", 13, NAVY, true);
        setupProgress.setPadding(0, dp(10), 0, 0);
        statusCard.addView(setupProgress);
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
        language.setContentDescription(getString(R.string.language_button));
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
        Button guideToggle = secondaryButton(R.string.show_guide);
        guideCard.addView(guideToggle);
        guideContent = column();
        guideContent.setVisibility(View.GONE);
        guideContent.setPadding(0, dp(12), 0, 0);
        guideContent.addView(text(R.string.guide_title, 17, NAVY, true));
        guideContent.addView(guideStep("1", R.string.guide_1));
        guideContent.addView(guideStep("2", R.string.guide_2));
        guideContent.addView(guideStep("3", R.string.guide_3));
        TextView phoneTips = text(R.string.guide_device_title, 15, NAVY, true);
        phoneTips.setPadding(0, dp(18), 0, 0);
        guideContent.addView(phoneTips);
        guideContent.addView(guideStep("4", R.string.guide_device_1));
        guideContent.addView(guideStep("5", R.string.guide_device_2));
        guideContent.addView(guideStep("6", R.string.guide_device_3));
        guideCard.addView(guideContent);
        guideToggle.setOnClickListener(v -> {
            boolean opening = guideContent.getVisibility() != View.VISIBLE;
            guideContent.setVisibility(opening ? View.VISIBLE : View.GONE);
            guideToggle.setText(opening ? R.string.hide_guide : R.string.show_guide);
        });
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
        if (!number.isEmpty() && !number.matches("^\\+?[0-9]{7,15}$")) {
            destinationInput.setError(getString(R.string.invalid_destination));
            destinationInput.requestFocus();
            return;
        }
        Prefs.setDestination(this, number);
        Prefs.setEnabled(this, enabled);
        Toast.makeText(this, enabled ? getString(R.string.toast_enabled) : getString(R.string.toast_saved), Toast.LENGTH_SHORT).show();
        if (enabled && !hasCorePermissions()) requestMissingPermissions();
        refreshStatus();
    }

    private void showLanguageDialog() {
        String[] codes = {"fa", "en", "ar", "tr", "de"};
        String[] labels = {
                getString(R.string.language_name_fa),
                getString(R.string.language_name_en),
                getString(R.string.language_name_ar),
                getString(R.string.language_name_tr),
                getString(R.string.language_name_de)
        };
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
                .setTitle(R.string.language_dialog_title)
                .setSingleChoiceItems(labels, checked, (dialog, which) -> selected[0] = which)
                .setPositiveButton(R.string.language_apply, (dialog, which) -> {
                    applyLanguage(codes[selected[0]]);
                })
                .setNegativeButton(R.string.language_cancel, null)
                .show();
    }

    private void applyLanguage(String code) {
        Prefs.setLanguage(this, code);
        Intent restart = getIntent();
        finish();
        startActivity(restart);
        overridePendingTransition(0, 0);
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
        if (Prefs.werePermissionsRequested(this) && !shouldShowAnyPermissionRationale() && hasAnyCorePermissionMissing()) {
            showPermissionSettingsDialog();
            return;
        }
        Prefs.markPermissionsRequested(this);
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
        boolean numberReady = !destinationInput.getText().toString().trim().isEmpty();
        int completed = (numberReady ? 1 : 0) + (receive && send ? 1 : 0) + (enabledSwitch.isChecked() ? 1 : 0);
        setupProgress.setText(getString(R.string.setup_progress, completed));
        updateBadge(enabledSwitch.isChecked());
    }

    private void updateBadge(boolean enabled) {
        if (stateBadge == null) return;
        stateBadge.setText(enabled ? R.string.status_on : R.string.status_off);
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
        layout.setLayoutDirection(isRtl() ? View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
        return layout;
    }

    private LinearLayout row() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER_VERTICAL);
        layout.setLayoutDirection(isRtl() ? View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
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
        view.setGravity(isRtl() ? Gravity.RIGHT : Gravity.LEFT);
        view.setTextDirection(isRtl() ? View.TEXT_DIRECTION_RTL : View.TEXT_DIRECTION_LTR);
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
        button.setMinHeight(dp(52));
        button.setPadding(dp(16), dp(12), dp(16), dp(12));
        button.setBackground(ripple(TEAL, Color.rgb(0, 96, 84), 14, false));
        button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
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
        button.setMinHeight(dp(50));
        button.setPadding(dp(16), dp(11), dp(16), dp(11));
        button.setBackground(ripple(Color.WHITE, Color.rgb(226, 241, 239), 14, true));
        button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
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

    private RippleDrawable ripple(int fill, int pressed, int radiusDp, boolean outlined) {
        GradientDrawable content = rounded(fill, outlined ? TEAL : Color.TRANSPARENT, radiusDp, outlined ? 1 : 0);
        return new RippleDrawable(ColorStateList.valueOf(pressed), content, null);
    }

    private boolean isRtl() {
        return getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
