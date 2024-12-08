package pl.kurs.ws_test3r.models.specifications;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import pl.kurs.ws_test3r.models.person.Person;

import java.util.Locale;


@Component
public class StudentSearchCriteria implements SearchSpecification {

    private Specification<Person> filterByGraduatedUniversity(String university) {
        return (root, query, builder) -> builder.like(builder.lower(root.get("graduatedUniversity")), "%" + university.toLowerCase(Locale.ROOT) + "%");
    }

    private Specification<Person> filterByYearOfStudyFrom(Integer minYear) {
        return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get("yearOfStudy"), minYear);
    }

    private Specification<Person> filterByYearOfStudyTo(Integer maxYear) {
        return (root, query, builder) -> builder.lessThanOrEqualTo(root.get("yearOfStudy"), maxYear);
    }

    private Specification<Person> filterByFieldOfStudy(String field) {
        return (root, query, builder) -> builder.like(builder.lower(root.get("fieldOfStudy")), "%" + field.toLowerCase(Locale.ROOT) + "%");
    }

    private Specification<Person> filterByScholarshipFrom(Double minScholarship) {
        return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get("scholarship"), minScholarship);
    }

    private Specification<Person> filterByScholarshipTo(Double maxScholarship) {
        return (root, query, builder) -> builder.lessThanOrEqualTo(root.get("scholarship"), maxScholarship);
    }

    @Override
    public Specification<Person> buildCriteria(String field, String value) {
        return switch (field) {
            case "graduatedUniversity" -> filterByGraduatedUniversity(value);
            case "yearOfStudyFrom" -> filterByYearOfStudyFrom(Integer.parseInt(value));
            case "yearOfStudyTo" -> filterByYearOfStudyTo(Integer.parseInt(value));
            case "fieldOfStudy" -> filterByFieldOfStudy(value);
            case "scholarshipFrom" -> filterByScholarshipFrom(Double.parseDouble(value));
            case "scholarshipTo" -> filterByScholarshipTo(Double.parseDouble(value));
            default -> null;
        };
    }

    @Override
    public String getType() {
        return "student";
    }


}




