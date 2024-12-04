package pl.kurs.ws_test3r.models.person;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = "employee")
@ToString
@Entity

public class EmployeeJobPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String positionName;

    private LocalDate employmentStartDate;

    private LocalDate employmentEndDate;

    private double salary;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

}
