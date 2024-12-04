package pl.kurs.ws_test3r.models.commands;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Data
public class CreateEmployeeJobPositionCommand {

    @NotBlank(message = "Position name must not be blank")
    private String positionName;

    @NotNull(message = "Salary must not be null")
    @Positive(message = "Salary must be positive")
    private Double salary;
    @NotNull
    private LocalDate employmentStartDate;
    @NotNull
    private LocalDate employmentEndDate;
}