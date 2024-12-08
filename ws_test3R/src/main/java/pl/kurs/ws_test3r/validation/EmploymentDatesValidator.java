package pl.kurs.ws_test3r.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.kurs.ws_test3r.models.person.EmployeeJobPosition;

public class EmploymentDatesValidator implements ConstraintValidator<ValidEmploymentDates, EmployeeJobPosition> {

    @Override
    public boolean isValid(EmployeeJobPosition jobPosition, ConstraintValidatorContext context) {
        if (jobPosition.getEmploymentStartDate() == null || jobPosition.getEmploymentEndDate() == null) {
            return true;
        }
        return jobPosition.getEmploymentEndDate().isAfter(jobPosition.getEmploymentStartDate());
    }
}
