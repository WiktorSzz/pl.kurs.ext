package pl.kurs.ws_test3r.models.person;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@DiscriminatorValue("Pensioner")
@Entity
public class Pensioner extends Person {
    private static final long serialVersionUID = 1L;
    private double pension;
    private int yearsWorked;

    public Pensioner(String firstName, String lastName, String pesel, double height, double weight, String email, double pension, int yearsWorked) {
        super(firstName, lastName, pesel, height, weight, email);
        this.pension = pension;
        this.yearsWorked = yearsWorked;
    }


}
