package pl.kurs.ws_test3r.models.person;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@DiscriminatorValue("Student")
@Entity
@Slf4j
public class Student extends Person {

    private static final long serialVersionUID = 1L;
    @NotBlank(message = "Graduated University can't be empty")
    private String graduatedUniversity;
    @Min(value = 1, message = "Year of study must be at least 1")
    private int yearOfStudy;
    private String fieldOfStudy;
    @PositiveOrZero(message = "Scholarship must be zero or a positive amount")
    private double scholarship;

    public Student(String firstName, String lastName, String pesel, double height, double weight, String email, String graduatedUniversity, int yearOfStudy, String fieldOfStudy, double scholarship) {
        super(firstName, lastName, pesel, height, weight, email);
        this.graduatedUniversity = graduatedUniversity;
        this.yearOfStudy = yearOfStudy;
        this.fieldOfStudy = fieldOfStudy;
        this.scholarship = scholarship;
    }




    public Student(Student student) {
        super(student);
        this.graduatedUniversity = student.getGraduatedUniversity();
        this.yearOfStudy = student.getYearOfStudy();
        this.fieldOfStudy = student.getFieldOfStudy();
        this.scholarship = student.getScholarship();
    }

}


