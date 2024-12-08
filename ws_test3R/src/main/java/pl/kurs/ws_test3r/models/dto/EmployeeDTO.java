package pl.kurs.ws_test3r.models.dto;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class EmployeeDTO extends PersonDTO {
    private LocalDate employmentStartDate;
    private String actualJobPosition;
    private double actualSalary;
    private int totalPositions;

    public EmployeeDTO(long id, String firstName, String lastName, String pesel, double height, double weight, String email, Long version, LocalDate employmentStartDate, String actualJobPosition, double actualSalary, int totalPositions) {
        super(id, firstName, lastName, pesel, height, weight, email, version);
        this.employmentStartDate = employmentStartDate;
        this.actualJobPosition = actualJobPosition;
        this.actualSalary = actualSalary;
        this.totalPositions = totalPositions;
    }
}
