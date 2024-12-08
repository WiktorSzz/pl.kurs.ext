package pl.kurs.ws_test3r.models.specifications;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.kurs.ws_test3r.models.person.Person;

import java.util.Set;


@Slf4j
@Service

public class PersonFilterService {

    private final Set<SearchSpecification> filterSpecifications;

    public PersonFilterService(Set<SearchSpecification> specifications) {
        this.filterSpecifications = specifications;
    }

    public Specification<Person> resolveFilter(String filterField, String filterValue) {
        Specification<Person> resolvedSpecification = null;
        for (SearchSpecification specification : filterSpecifications) {
            if ((resolvedSpecification = specification.buildCriteria(filterField, filterValue)) != null) {
                break;
            }
        }
        return resolvedSpecification;
    }
}

