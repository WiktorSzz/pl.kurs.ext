package pl.kurs.ws_test3r.models.specifications;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import pl.kurs.ws_test3r.models.person.Person;

@Component
public class PensionerSearchCriteria implements SearchSpecification {

    private Specification<Person> filterByPensionFrom(Double minPension) {
        return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get("pension"), minPension);
    }

    private Specification<Person> filterByPensionTo(Double maxPension) {
        return (root, query, builder) -> builder.lessThanOrEqualTo(root.get("pension"), maxPension);
    }

    private Specification<Person> filterByYearsWorkedFrom(Integer minYears) {
        return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get("yearsWorked"), minYears);
    }

    private Specification<Person> filterByYearsWorkedTo(Integer maxYears) {
        return (root, query, builder) -> builder.lessThanOrEqualTo(root.get("yearsWorked"), maxYears);
    }


    @Override
    public Specification<Person> buildCriteria(String fieldName, String value) {
        return switch (fieldName) {
            case "pensionFrom" -> filterByPensionFrom(Double.parseDouble(value));
            case "pensionTo" -> filterByPensionTo(Double.parseDouble(value));
            case "yearsWorkedFrom" -> filterByYearsWorkedFrom(Integer.parseInt(value));
            case "yearsWorkedTo" -> filterByYearsWorkedTo(Integer.parseInt(value));

            default -> null;
        };
    }

    @Override
    public String getType() {
        return "pensioner";
    }
}

