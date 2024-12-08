package pl.kurs.ws_test3r.models.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class StudentDTO extends PersonDTO {
    private String graduatedUniversity;
    private Integer yearOfStudy;
    private String fieldOfStudy;
    private double scholarship;

    public StudentDTO(Long id, String name, String surname, String pesel, Double height, Double weight, String email, Long version, String universityName, Integer yearOfStudy, String studyField, Double scholarship) {
        super(id, name, surname, pesel, height, weight, email, version);
        this.graduatedUniversity = universityName;
        this.yearOfStudy = yearOfStudy;
        this.fieldOfStudy = studyField;
        this.scholarship = scholarship;
    }


}
