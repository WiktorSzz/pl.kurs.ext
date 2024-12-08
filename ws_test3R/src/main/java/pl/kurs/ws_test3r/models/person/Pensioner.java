package pl.kurs.ws_test3r.models.person;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@DiscriminatorValue("Pensioner")
@Entity
@Slf4j
public class Pensioner extends Person {
    private static final long serialVersionUID = 1L;
    private double pension;
    private int yearsWorked;

    public Pensioner(String firstName, String lastName, String pesel, double height, double weight, String email, double pension, int yearsWorked) {
        super(firstName, lastName, pesel, height, weight, email);
        this.pension = pension;
        this.yearsWorked = yearsWorked;
    }


    public Pensioner(Pensioner pensioner) {
        super(pensioner);
        this.pension = pensioner.getPension();
        this.yearsWorked = pensioner.getYearsWorked();
    }

}
