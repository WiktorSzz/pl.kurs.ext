package pl.kurs.ws_test3r.models.specifications;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import pl.kurs.ws_test3r.models.person.Person;

import java.util.Locale;


@Component
public class PersonSearchCriteria implements SearchSpecification {

    private Specification<Person> filterByType(String type) {
        return (root, query, builder) -> builder.like(builder.lower(root.get("type")), "%" + type.toLowerCase(Locale.ROOT) + "%");
    }

    private Specification<Person> filterByFirstName(String firstName) {
        return (root, query, builder) -> builder.like(builder.lower(root.get("firstName")), "%" + firstName.toLowerCase(Locale.ROOT) + "%");
    }

    private Specification<Person> filterByLastName(String lastName) {
        return (root, query, builder) -> builder.like(builder.lower(root.get("lastName")), "%" + lastName.toLowerCase(Locale.ROOT) + "%");
    }

    private Specification<Person> filterByPesel(String pesel) {
        return (root, query, builder) -> builder.equal(root.get("pesel"), pesel);
    }

    private Specification<Person> filterByHeightFrom(Double minHeight) {
        return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get("height"), minHeight);
    }

    private Specification<Person> filterByHeightTo(Double maxHeight) {
        return (root, query, builder) -> builder.lessThanOrEqualTo(root.get("height"), maxHeight);
    }

    private Specification<Person> filterByWeightFrom(Double minWeight) {
        return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get("weight"), minWeight);
    }

    private Specification<Person> filterByWeightTo(Double maxWeight) {
        return (root, query, builder) -> builder.lessThanOrEqualTo(root.get("weight"), maxWeight);
    }

    private Specification<Person> filterByEmail(String email) {
        return (root, query, builder) -> builder.like(builder.lower(root.get("email")), "%" + email.toLowerCase(Locale.ROOT) + "%");
    }

    @Override
    public Specification<Person> buildCriteria(String fieldName, String value) {
        return switch (fieldName) {
            case "type" -> filterByType(value);
            case "firstName" -> filterByFirstName(value);
            case "lastName" -> filterByLastName(value);
            case "pesel" -> filterByPesel(value);
            case "heightFrom" -> filterByHeightFrom(Double.parseDouble(value));
            case "heightTo" -> filterByHeightTo(Double.parseDouble(value));
            case "weightFrom" -> filterByWeightFrom(Double.parseDouble(value));
            case "weightTo" -> filterByWeightTo(Double.parseDouble(value));
            case "email" -> filterByEmail(value);
            default -> null;
        };
    }

    @Override
    public String getType() {
        return "person";
    }
}




