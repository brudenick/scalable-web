package com.waes.assignment.scalableweb.controller.validation;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class Base64EncodedValidator implements ConstraintValidator<Base64EncodedConstraint, byte[]> {

    @Override
    public void initialize(final Base64EncodedConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(final byte[] content, final ConstraintValidatorContext context) {
        return Base64.isBase64(content);
    }
}
