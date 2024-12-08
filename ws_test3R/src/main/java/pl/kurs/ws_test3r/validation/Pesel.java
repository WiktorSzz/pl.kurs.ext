package pl.kurs.ws_test3r.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(
        validatedBy = {PeselValidator.class}
)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Pesel {

    String message() default "Wrong PESEl!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
