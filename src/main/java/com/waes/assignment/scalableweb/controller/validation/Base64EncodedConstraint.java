package com.waes.assignment.scalableweb.controller.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = Base64EncodedValidator.class)
@Target(PARAMETER)
@Retention(RUNTIME)
public @interface Base64EncodedConstraint {
    String message() default "Content is not BASE64 encoded";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}