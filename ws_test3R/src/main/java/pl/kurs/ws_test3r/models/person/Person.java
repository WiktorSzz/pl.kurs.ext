package pl.kurs.ws_test3r.models.person;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import pl.kurs.ws_test3r.validation.Pesel;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@DynamicInsert
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "UC_PERSON_EMAIL", columnNames = "email"),
        @UniqueConstraint(name = "UC_PERSON_PESEL", columnNames = "pesel")
})
@EqualsAndHashCode
@ToString
@Entity
public abstract class Person implements Serializable, Identificationable {
    private static final long serialVersionUID = 1L;
    @Column(insertable = false, updatable = false)
    private String type;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Pesel
    private String pesel;
    @Column(nullable = false)
    private double height;
    @Column(nullable = false)
    private double weight;
    @Email
    private String email;
    @Version
    private Long version = 0l;

    public Person(String firstName, String lastName, String pesel, double height, double weight, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.pesel = pesel;
        this.height = height;
        this.weight = weight;
        this.email = email;
    }



    public Person(Person person) {
        this.type = person.getType();
        this.id = person.getId();
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.pesel = person.getPesel();
        this.height = person.getHeight();
        this.weight = person.getWeight();
        this.email = person.getEmail();
        this.version = person.getVersion();
    }




}
