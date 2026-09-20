package com.ashkan.smsforwarder;

/** Validates the app's deliberately narrow destination format. */
public final class DestinationValidator {
    private DestinationValidator() {}

    public static boolean isValid(String destination) {
        return destination != null && destination.matches("^\\+?[0-9]{7,15}$");
    }
}
