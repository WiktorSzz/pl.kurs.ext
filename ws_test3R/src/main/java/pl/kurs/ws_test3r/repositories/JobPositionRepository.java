package pl.kurs.ws_test3r.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurs.ws_test3r.models.person.EmployeeJobPosition;


public interface JobPositionRepository extends JpaRepository<EmployeeJobPosition, Long> {

}
