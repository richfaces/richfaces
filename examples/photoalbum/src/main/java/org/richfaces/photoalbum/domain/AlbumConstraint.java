package org.richfaces.photoalbum.domain;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = AlbumValidator.class)
@Documented
public @interface AlbumConstraint {

    String message() default "{org.richfaces.photoalbum.domain}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}