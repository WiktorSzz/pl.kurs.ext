package pl.kurs.ws_test3r.models.facades;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.kurs.ws_test3r.models.dto.EmployeeDTO;
import pl.kurs.ws_test3r.models.person.Employee;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Component("employeeFacade")
public class EmployeeFacade implements EntityFacade<Employee, EmployeeDTO> {

    @Override
    public Employee buildEntityFromMap(Map<String, String> parameters) {
        parameters.forEach((key, value) -> {
            log.info("Parameter: {} - Type: {}", key, value != null ? value.getClass().getName() : "null");
        });
        List<String> requiredParams = Arrays.asList(
                "firstName", "lastName", "pesel", "height", "weight",
                "email", "employmentStartDate", "actualJobPosition", "actualSalary"
        );
        for (String param : requiredParams) {
            if (!parameters.containsKey(param)) {
                throw new IllegalArgumentException("Missing required parameters: " + param);
            }
        }

        Employee employee = new Employee();
        employee.setFirstName(parameters.get("firstName"));
        employee.setLastName(parameters.get("lastName"));
        employee.setPesel(parameters.get("pesel"));
        employee.setHeight(Double.parseDouble(parameters.get("height")));
        employee.setWeight(Double.parseDouble(parameters.get("weight")));
        employee.setEmail(parameters.get("email"));
        employee.setEmploymentStartDate(LocalDate.parse(parameters.get("employmentStartDate")));
        employee.setActualJobPosition(parameters.get("actualJobPosition"));
        employee.setActualSalary(Double.parseDouble(parameters.get("actualSalary")));
        return employee;
    }

    @Override
    public Employee updateEntity(Employee employee, Map<String, Object> updates) {
        if (updates.containsKey("firstName")) {
            employee.setFirstName((String) updates.get("firstName"));
        }
        if (updates.containsKey("lastName")) {
            employee.setLastName((String) updates.get("lastName"));
        }
        if (updates.containsKey("pesel")) {
            employee.setPesel((String) updates.get("pesel"));
        }
        if (updates.containsKey("height")) {
            employee.setHeight(Double.parseDouble(updates.get("height").toString()));
        }
        if (updates.containsKey("weight")) {
            employee.setWeight(Double.parseDouble(updates.get("weight").toString()));
        }
        if (updates.containsKey("email")) {
            employee.setEmail((String) updates.get("email"));
        }
        if (updates.containsKey("employmentStartDate")) {
            String dateString = (String) updates.get("employmentStartDate");
            if (dateString != null) {
                employee.setEmploymentStartDate(LocalDate.parse(dateString));
            }
        }
        if (updates.containsKey("actualJobPosition")) {
            employee.setActualJobPosition((String) updates.get("actualJobPosition"));
        }
        if (updates.containsKey("actualSalary")) {
            employee.setActualSalary(Double.parseDouble(updates.get("actualSalary").toString()));
        }
        return employee;
    }

    @Override
    public EmployeeDTO toDto(Employee employee) {
        log.info("Employee positions: {}", employee.getJobPositions());
        return new EmployeeDTO(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getPesel(),
                employee.getHeight(),
                employee.getWeight(),
                employee.getEmail(),
                employee.getVersion(),
                employee.getEmploymentStartDate(),
                employee.getActualJobPosition(),
                employee.getActualSalary(),
                employee.getJobPositions().size()
        );
    }
}
