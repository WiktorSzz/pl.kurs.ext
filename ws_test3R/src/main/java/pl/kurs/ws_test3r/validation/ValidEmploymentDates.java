package pl.kurs.ws_test3r.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmploymentDatesValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEmploymentDates {
    String message() default "Employment end date must be after the start date.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
