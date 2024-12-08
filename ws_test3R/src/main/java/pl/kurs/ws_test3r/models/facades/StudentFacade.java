package pl.kurs.ws_test3r.models.facades;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.kurs.ws_test3r.models.commands.UpdateEntityCommand;
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
    public Student updateEntity(Student student, UpdateEntityCommand command) {
        Map<String, Object> properties = command.getAttributes();
        Student copiedStudent = new Student(student);
        copiedStudent.setVersion(command.getVersion());
        if (command.getVersion() == null) {
            throw new IllegalArgumentException("Version cannot be null");
        }
        properties.forEach((key, value) -> {
            switch (key) {
                case "firstName" -> copiedStudent.setFirstName((String) value);
                case "lastName" -> copiedStudent.setLastName((String) value);
                case "pesel" -> copiedStudent.setPesel((String) value);
                case "height" -> copiedStudent.setHeight(Double.parseDouble(value.toString()));
                case "weight" -> copiedStudent.setWeight(Double.parseDouble(value.toString()));
                case "email" -> copiedStudent.setEmail((String) value);
                case "graduatedUniversity" -> copiedStudent.setGraduatedUniversity((String) value);
                case "yearOfStudy" -> copiedStudent.setYearOfStudy(Integer.parseInt(value.toString()));
                case "fieldOfStudy" -> copiedStudent.setFieldOfStudy((String) value);
                case "scholarship" -> copiedStudent.setScholarship(Double.parseDouble(value.toString()));
                default -> throw new IllegalArgumentException("Unsupported property: " + key);
            }
        });

        return copiedStudent;
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
