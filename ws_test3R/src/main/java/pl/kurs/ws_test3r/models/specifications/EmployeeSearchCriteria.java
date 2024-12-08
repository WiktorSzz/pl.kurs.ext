package pl.kurs.ws_test3r.models.specifications;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import pl.kurs.ws_test3r.models.person.Person;

import java.time.LocalDate;
import java.util.Locale;

@Component
public class EmployeeSearchCriteria implements SearchSpecification {

    private Specification<Person> filterByEmploymentStartDateFrom(LocalDate startDateFrom) {
        return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get("employmentStartDate"), startDateFrom);
    }

    private Specification<Person> filterByEmploymentStartDateTo(LocalDate startDateTo) {
        return (root, query, builder) -> builder.lessThanOrEqualTo(root.get("employmentStartDate"), startDateTo);
    }

    private Specification<Person> filterByActualJobPosition(String jobPosition) {
        return (root, query, builder) -> builder.like(builder.lower(root.get("actualJobPosition")), "%" + jobPosition.toLowerCase(Locale.ROOT) + "%");
    }

    private Specification<Person> filterByActualSalaryFrom(Double minSalary) {
        return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get("actualSalary"), minSalary);
    }

    private Specification<Person> filterByActualSalaryTo(Double maxSalary) {
        return (root, query, builder) -> builder.lessThanOrEqualTo(root.get("actualSalary"), maxSalary);
    }


    @Override
    public Specification<Person> buildCriteria(String fieldName, String value) {
        return switch (fieldName) {
            case "employmentStartDateFrom" -> filterByEmploymentStartDateFrom(LocalDate.parse(value));
            case "employmentStartDateTo" -> filterByEmploymentStartDateTo(LocalDate.parse(value));
            case "actualJobPosition" -> filterByActualJobPosition(value);
            case "actualSalaryFrom" -> filterByActualSalaryFrom(Double.parseDouble(value));
            case "actualSalaryTo" -> filterByActualSalaryTo(Double.parseDouble(value));
            default -> null;
        };
    }

    @Override
    public String getType() {
        return "Employee";
    }
}
