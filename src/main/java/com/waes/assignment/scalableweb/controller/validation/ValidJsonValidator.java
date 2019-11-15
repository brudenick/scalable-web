package com.waes.assignment.scalableweb.controller.validation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.IOException;

public class ValidJsonValidator implements ConstraintValidator<ValidJsonConstraint, byte[]> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void initialize(final ValidJsonConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(final byte[] content, final ConstraintValidatorContext context) {
        try {
            if (objectMapper.readTree(Base64.decodeBase64(content)).isEmpty()){
                return false;
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
