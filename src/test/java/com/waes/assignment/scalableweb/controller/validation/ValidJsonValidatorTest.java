package com.waes.assignment.scalableweb.controller.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidJsonValidatorTest {

    private final static ValidJsonValidator validator = new ValidJsonValidator();

    @Test
    public void shouldPassValidation() {
        // Setup
        final byte[] json = "{\"valid\":\"json\"}".getBytes();

        // Test
        boolean result = validator.isValid(json, null);

        // Result verification
        assertTrue(result);
    }

    @Test
    public void shouldFailValidation() {
        // Setup
        final byte[] invalidJson = "{\"invalid\":json}".getBytes();

        // Test
        boolean result = validator.isValid(invalidJson, null);

        // Result verification
        assertFalse(result);
    }
}