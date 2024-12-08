package pl.kurs.ws_test3r.models.specifications;

import org.springframework.data.jpa.domain.Specification;
import pl.kurs.ws_test3r.models.person.Person;


public interface SearchSpecification {

    Specification<Person> buildCriteria(String field, String value);

    String getType();
}