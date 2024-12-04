package pl.kurs.ws_test3r.models.facades;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.kurs.ws_test3r.models.dto.StudentDTO;
import pl.kurs.ws_test3r.models.person.Student;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


@Slf4j
@Component("studentFacade")
public class StudentFacade implements EntityFacade<Student, StudentDTO> {

    @Override
    public Student buildEntityFromMap(Map<String, String> parameters) {

        parameters.forEach((key, value) -> {
            log.info("Parameter: {} - Type: {}", key, value != null ? value.getClass().getName() : "null");
        });
        List<String> requiredParams = Arrays.asList(
                "firstName", "lastName", "pesel", "height", "weight",
                "email", "graduatedUniversity", "yearOfStudy", "fieldOfStudy", "scholarship"
        );
        for (String param : requiredParams) {
            if (!parameters.containsKey(param)) {
                throw new IllegalArgumentException("Missing required parameters: " + param);
            }
        }
        Student student = new Student();
        student.setFirstName(parameters.get("firstName"));
        student.setLastName(parameters.get("lastName"));
        student.setPesel(parameters.get("pesel"));
        student.setHeight(Double.parseDouble(parameters.get("height")));
        student.setWeight(Double.parseDouble(parameters.get("weight")));
        student.setEmail(parameters.get("email"));
        student.setGraduatedUniversity(parameters.get("graduatedUniversity"));
        student.setYearOfStudy(Integer.parseInt(parameters.get("yearOfStudy")));
        student.setFieldOfStudy(parameters.get("fieldOfStudy"));
        student.setScholarship(Double.parseDouble(parameters.get("scholarship")));
        return student;
    }

    @Override
    public Student updateEntity(Student student, Map<String, Object> updates) {
        if (updates.containsKey("firstName")) {
            student.setFirstName((String) updates.get("firstName"));
        }
        if (updates.containsKey("lastName")) {
            student.setLastName((String) updates.get("lastName"));
        }
        if (updates.containsKey("pesel")) {
            student.setPesel((String) updates.get("pesel"));
        }
        if (updates.containsKey("height")) {
            student.setHeight(Double.parseDouble(updates.get("height").toString()));
        }
        if (updates.containsKey("weight")) {
            student.setWeight(Double.parseDouble(updates.get("weight").toString()));
        }
        if (updates.containsKey("email")) {
            student.setEmail((String) updates.get("email"));
        }
        if (updates.containsKey("graduatedUniversity")) {
            student.setGraduatedUniversity((String) updates.get("graduatedUniversity"));
        }
        if (updates.containsKey("yearOfStudy")) {
            student.setYearOfStudy((Integer) updates.get("yearOfStudy"));
        }
        if (updates.containsKey("fieldOfStudy")) {
            student.setFieldOfStudy((String) updates.get("fieldOfStudy"));
        }
        if (updates.containsKey("scholarship")) {
            student.setScholarship(Double.parseDouble(updates.get("scholarship").toString()));
        }
        return student;
    }

    @Override
    public StudentDTO toDto(Student student) {
        return new StudentDTO(
                student.getId(),
                student.getFirstName(),
                student.getLastName(),
                student.getPesel(),
                student.getHeight(),
                student.getWeight(),
                student.getEmail(),
                student.getVersion(),
                student.getGraduatedUniversity(),
                student.getYearOfStudy(),
                student.getFieldOfStudy(),
                student.getScholarship()
        );
    }

}
