package com.waes.assignment.scalableweb.controller.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = ValidJsonValidator.class)
@Target(PARAMETER)
@Retention(RUNTIME)
public @interface ValidJsonConstraint {
    String message() default "Content is not a valid JSON";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}