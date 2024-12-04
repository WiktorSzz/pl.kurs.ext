package pl.kurs.ws_test3r.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kurs.ws_test3r.exceptions.PositionDateConflictException;
import pl.kurs.ws_test3r.models.commands.CreateEmployeeJobPositionCommand;
import pl.kurs.ws_test3r.models.dto.EmployeeDTO;
import pl.kurs.ws_test3r.models.facades.EmployeeFacade;
import pl.kurs.ws_test3r.models.person.Employee;
import pl.kurs.ws_test3r.models.person.EmployeeJobPosition;
import pl.kurs.ws_test3r.repositories.EmployeeRepository;
import pl.kurs.ws_test3r.repositories.JobPositionRepository;

import java.time.LocalDate;


@Service
@RequiredArgsConstructor
public class EmployeeJobPositionService {

    private final EmployeeRepository employeeRepository;
    private final JobPositionRepository jobPositionRepository;
    private final EmployeeFacade employeeFacade;
    @PersistenceContext
    private EntityManager entityManager;


    @Transactional
    public EmployeeDTO createEmployeeJobPosition(Long employeeId, CreateEmployeeJobPositionCommand command) throws PositionDateConflictException {
        Employee employee = fetchEmployeeWithLock(employeeId);
        EmployeeJobPosition newPosition = prepareNewJobPosition(employee, command);
        validateJobPositionDates(employee, newPosition);
        persistAndUpdateEmployeeData(newPosition);
        return employeeFacade.toDto(employee);
    }


    private Employee fetchEmployeeWithLock(Long employeeId) {
        return employeeRepository.findEmployeeWithId(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found with ID: " + employeeId));
    }

    private EmployeeJobPosition prepareNewJobPosition(Employee employee, CreateEmployeeJobPositionCommand command) {
        EmployeeJobPosition newPosition = new EmployeeJobPosition();
        newPosition.setPositionName(command.getPositionName());
        newPosition.setSalary(command.getSalary());
        newPosition.setEmploymentStartDate(command.getEmploymentStartDate());
        newPosition.setEmploymentEndDate(command.getEmploymentEndDate());
        newPosition.setEmployee(employee);
        return newPosition;
    }


    private void validateJobPositionDates(Employee employee, EmployeeJobPosition newPosition) throws PositionDateConflictException {
        if (newPosition.getEmploymentStartDate() != null && newPosition.getEmploymentEndDate() != null &&
                newPosition.getEmploymentStartDate().isAfter(newPosition.getEmploymentEndDate())) {
            throw new PositionDateConflictException(
                    "Start date must be earlier than or equal to end date: " +
                            newPosition.getEmploymentStartDate() + " - " + newPosition.getEmploymentEndDate()
            );
        }

        LocalDate currentStart = employee.getEmploymentStartDate();
        LocalDate currentEnd = null;

        if (isDateOverlap(currentStart, currentEnd, newPosition.getEmploymentStartDate(), newPosition.getEmploymentEndDate())) {
            throw new PositionDateConflictException(
                    "New position overlaps with current position: " +
                            newPosition.getEmploymentStartDate() + " - " + newPosition.getEmploymentEndDate()
            );
        }

        boolean hasConflicts = employee.getJobPositions().stream()
                .anyMatch(existingPosition -> isDateOverlap(
                        existingPosition.getEmploymentStartDate(),
                        existingPosition.getEmploymentEndDate(),
                        newPosition.getEmploymentStartDate(),
                        newPosition.getEmploymentEndDate()
                ));

        if (hasConflicts) {
            throw new PositionDateConflictException(
                    "Conflict in job position dates: " +
                            newPosition.getEmploymentStartDate() + " - " + newPosition.getEmploymentEndDate()
            );
        }
    }


    private void persistAndUpdateEmployeeData(EmployeeJobPosition newPosition) {
        Employee employee = newPosition.getEmployee();

        if (isActiveJobPosition(newPosition)) {
            employee.setActualJobPosition(newPosition.getPositionName());
            employee.setActualSalary(newPosition.getSalary());
        }
        employee.getJobPositions().add(newPosition);
        jobPositionRepository.save(newPosition);
        entityManager.merge(employee);
        entityManager.flush();
    }


    private boolean isActiveJobPosition(EmployeeJobPosition position) {
        LocalDate today = LocalDate.now();
        return (position.getEmploymentStartDate() == null || !today.isBefore(position.getEmploymentStartDate())) &&
                (position.getEmploymentEndDate() == null || !today.isAfter(position.getEmploymentEndDate()));
    }


    private boolean isDateOverlap(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
        return (start1 == null || end2 == null || !start1.isAfter(end2)) &&
                (start2 == null || end1 == null || !start2.isAfter(end1));
    }
}

