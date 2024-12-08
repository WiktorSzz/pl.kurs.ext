package pl.kurs.ws_test3r.models.facades;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.kurs.ws_test3r.models.commands.UpdateEntityCommand;
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
    public Employee updateEntity(Employee employee, UpdateEntityCommand command) {
        Map<String, Object> properties = command.getAttributes();
        Employee copiedEmployee = new Employee(employee);
        copiedEmployee.setVersion(command.getVersion());
        if (command.getVersion() == null) {
            throw new IllegalArgumentException("Version cannot be null");
        }


        properties.forEach((key, value) -> {
            switch (key) {
                case "firstName" -> copiedEmployee.setFirstName((String) value);
                case "lastName" -> copiedEmployee.setLastName((String) value);
                case "pesel" -> copiedEmployee.setPesel((String) value);
                case "height" -> copiedEmployee.setHeight(Double.parseDouble(value.toString()));
                case "weight" -> copiedEmployee.setWeight(Double.parseDouble(value.toString()));
                case "email" -> copiedEmployee.setEmail((String) value);
                case "employmentStartDate" -> {
                    if (value != null) {
                        copiedEmployee.setEmploymentStartDate(LocalDate.parse(value.toString()));
                    }
                }
                case "actualJobPosition" -> copiedEmployee.setActualJobPosition((String) value);
                case "actualSalary" -> copiedEmployee.setActualSalary(Double.parseDouble(value.toString()));
                default -> throw new IllegalArgumentException("Unsupported property: " + key);
            }
        });

        return copiedEmployee;
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
