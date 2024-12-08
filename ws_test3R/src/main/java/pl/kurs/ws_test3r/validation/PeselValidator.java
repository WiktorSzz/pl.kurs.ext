package pl.kurs.ws_test3r.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class PeselValidator implements ConstraintValidator<Pesel, String> {

    @Override
    public void initialize(Pesel constraintAnnotation) {

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null)
            return false;
        return s.matches("\\d{11}");
    }
}
