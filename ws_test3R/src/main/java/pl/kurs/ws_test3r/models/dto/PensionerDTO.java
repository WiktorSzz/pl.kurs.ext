package pl.kurs.ws_test3r.models.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class PensionerDTO extends PersonDTO {
    private double pension;
    private Integer workedYears;

    public PensionerDTO(long id, String firstName, String lastName, String pesel, double height, double weight, String email, Long version, double pension, Integer workedYears) {
        super(id, firstName, lastName, pesel, height, weight, email, version);
        this.pension = pension;
        this.workedYears = workedYears;
    }
}
