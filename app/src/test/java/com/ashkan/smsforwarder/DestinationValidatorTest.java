package com.ashkan.smsforwarder;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DestinationValidatorTest {
    @Test public void acceptsConfiguredFormat() {
        assertTrue(DestinationValidator.isValid("+12345678901"));
        assertTrue(DestinationValidator.isValid("1234567"));
        assertTrue(DestinationValidator.isValid("123456789012345"));
    }

    @Test public void rejectsMalformedOrAbsentDestination() {
        assertFalse(DestinationValidator.isValid(null));
        assertFalse(DestinationValidator.isValid(""));
        assertFalse(DestinationValidator.isValid("123456"));
        assertFalse(DestinationValidator.isValid("1234567890123456"));
        assertFalse(DestinationValidator.isValid("+123 456 789"));
        assertFalse(DestinationValidator.isValid("1234567;1234567"));
        assertFalse(DestinationValidator.isValid("\n1234567"));
    }
}
