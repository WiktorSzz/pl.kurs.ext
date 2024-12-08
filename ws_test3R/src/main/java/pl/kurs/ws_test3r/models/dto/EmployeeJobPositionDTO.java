package pl.kurs.ws_test3r.models.dto;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class EmployeeJobPositionDTO {
    private Long id;
    private Long employeeId;
    private String positionName;
    private double salary;
    private LocalDate employmentStartDate;
    private LocalDate employmentEndDate;
}
