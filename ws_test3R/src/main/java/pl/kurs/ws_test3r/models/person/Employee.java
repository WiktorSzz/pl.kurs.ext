package pl.kurs.ws_test3r.models.person;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = "jobPositions")
@ToString(exclude = "jobPositions")
@DiscriminatorValue("Employee")
@Entity
@Slf4j
public class Employee extends Person {
    private static final long serialVersionUID = 1L;
    private LocalDate employmentStartDate;
    private String actualJobPosition;
    private double actualSalary;
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EmployeeJobPosition> jobPositions = new HashSet<>();

    public Employee(String firstName, String lastName, String pesel, double height, double weight, String email, LocalDate employmentStartDate, String actualJobPosition, double actualSalary) {
        super(firstName, lastName, pesel, height, weight, email);
        this.employmentStartDate = employmentStartDate;
        this.actualJobPosition = actualJobPosition;
        this.actualSalary = actualSalary;

    }


    public Employee(Employee employee) {
        super(employee);
        this.employmentStartDate = employee.getEmploymentStartDate();
        this.actualJobPosition = employee.getActualJobPosition();
        this.actualSalary = employee.getActualSalary();
        this.jobPositions = new HashSet<>(employee.getJobPositions());
    }

}
