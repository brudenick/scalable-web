package com.waes.assignment.scalableweb.controller.validation;

import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class Base64EncodedValidatorTest {

    private final static Base64EncodedValidator validator = new Base64EncodedValidator();

    @Test
    public void shouldPassValidation() {
        // Setup
        final byte[] base64Encoded = Base64.getEncoder().encode("{base64}".getBytes());

        // Test
        boolean result = validator.isValid(base64Encoded, null);

        // Result verification
        assertTrue(result);
    }

    @Test
    public void shouldFailValidation() {
        // Setup
        final byte[] notBase64Encoded = "{base64}".getBytes();

        // Test
        boolean result = validator.isValid(notBase64Encoded, null);

        // Result verification
        assertFalse(result);
    }
}