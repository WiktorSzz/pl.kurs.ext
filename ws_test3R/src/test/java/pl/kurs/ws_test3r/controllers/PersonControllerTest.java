package pl.kurs.ws_test3r.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pl.kurs.ws_test3r.models.commands.CreateEntityCommand;
import pl.kurs.ws_test3r.models.commands.UpdateEntityCommand;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnPersonWhenGetPersonByValidId() throws Exception {
        mockMvc.perform(get("/api/v1/persons/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("employee"))
                .andExpect(jsonPath("$.firstName").value("Magdalena"))
                .andExpect(jsonPath("$.lastName").value("Krawczyk"))
                .andExpect(jsonPath("$.pesel").value("80100212628"))
                .andExpect(jsonPath("$.height").value(160.04))
                .andExpect(jsonPath("$.weight").value(77.78))
                .andExpect(jsonPath("$.email").value("magdalena.krawczyk@testmail.com"))
                .andExpect(jsonPath("$.employmentStartDate").value("2019-02-21"))
                .andExpect(jsonPath("$.actualJobPosition").value("Software Engineer"))
                .andExpect(jsonPath("$.actualSalary").value(4662.28));
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldFailToReturnPersonWhenGetPersonByInvalidId() throws Exception {
        mockMvc.perform(get("/api/v1/persons/{id}", 999999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorsList[0].message").value("Person with ID 999999 does not exist."))
                .andExpect(jsonPath("$.errorCode").value("EntityNotFound"));
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldCreateEmployeeSuccessfully() throws Exception {
        CreateEntityCommand createPersonCommand = new CreateEntityCommand(
                "Employee",
                Map.of(
                        "firstName", "Krzysztof",
                        "lastName", "Lewandowski",
                        "pesel", "80062354328",
                        "height", Double.toString(175.0),
                        "weight", Double.toString(70.0),
                        "email", "krzysztof.lewandowski@example.com",
                        "employmentStartDate", "2018-06-15",
                        "actualJobPosition", "Developer",
                        "actualSalary", Double.toString(4000.0)
                )
        );

        String personJsonRequest = objectMapper.writeValueAsString(createPersonCommand);

        mockMvc.perform(post("/api/v1/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(personJsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Krzysztof"))
                .andExpect(jsonPath("$.lastName").value("Lewandowski"))
                .andExpect(jsonPath("$.pesel").value("80062354328"))
                .andExpect(jsonPath("$.height").value(175.0))
                .andExpect(jsonPath("$.weight").value(70.0))
                .andExpect(jsonPath("$.email").value("krzysztof.lewandowski@example.com"))
                .andExpect(jsonPath("$.employmentStartDate").value("2018-06-15"))
                .andExpect(jsonPath("$.actualJobPosition").value("Developer"))
                .andExpect(jsonPath("$.actualSalary").value(4000.0));
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldCreatePensionerSuccessfully() throws Exception {
        CreateEntityCommand createPersonCommand = new CreateEntityCommand(
                "Pensioner",
                Map.of(
                        "firstName", "Anna",
                        "lastName", "Kowalska",
                        "pesel", "55010112345",
                        "height", Double.toString(160.0),
                        "weight", Double.toString(58.0),
                        "email", "anna.kowalska@example.com",
                        "pension", Double.toString(2500.50),
                        "yearsWorked", Integer.toString(40)
                )
        );

        String personJsonRequest = objectMapper.writeValueAsString(createPersonCommand);

        mockMvc.perform(post("/api/v1/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(personJsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Anna"))
                .andExpect(jsonPath("$.lastName").value("Kowalska"))
                .andExpect(jsonPath("$.pesel").value("55010112345"))
                .andExpect(jsonPath("$.height").value(160.0))
                .andExpect(jsonPath("$.weight").value(58.0))
                .andExpect(jsonPath("$.email").value("anna.kowalska@example.com"))
                .andExpect(jsonPath("$.pension").value(2500.50))
                .andExpect(jsonPath("$.workedYears").value(40));
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldCreateStudentSuccessfully() throws Exception {
        CreateEntityCommand createPersonCommand = new CreateEntityCommand(
                "Student",
                Map.of(
                        "firstName", "Emilia",
                        "lastName", "Poduszkowska",
                        "pesel", "50062354324",
                        "height", Double.toString(163.0),
                        "weight", Double.toString(60.0),
                        "email", "emii.poducha@example.com",
                        "graduatedUniversity", "ASP",
                        "yearOfStudy", Integer.toString(4),
                        "fieldOfStudy", "Wzornictwo",
                        "scholarship", Double.toString(1250.0)
                )
        );


        String personJsonRequest = objectMapper.writeValueAsString(createPersonCommand);

        mockMvc.perform(post("/api/v1/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(personJsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Emilia"))
                .andExpect(jsonPath("$.lastName").value("Poduszkowska"))
                .andExpect(jsonPath("$.pesel").value("50062354324"))
                .andExpect(jsonPath("$.height").value(163.0))
                .andExpect(jsonPath("$.weight").value(60.0))
                .andExpect(jsonPath("$.email").value("emii.poducha@example.com"))
                .andExpect(jsonPath("$.graduatedUniversity").value("ASP"))
                .andExpect(jsonPath("$.yearOfStudy").value(4))
                .andExpect(jsonPath("$.fieldOfStudy").value("Wzornictwo"))
                .andExpect(jsonPath("$.scholarship").value(1250.0));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldFailToCreatePersonWithInvalidPesel() throws Exception {
        CreateEntityCommand invalidPeselCommand = new CreateEntityCommand(
                "Employee",
                Map.of(
                        "firstName", "Test",
                        "lastName", "Invalid",
                        "pesel", "1234567890",
                        "height", "180.5",
                        "weight", "75.0",
                        "email", "test@invalid.com",
                        "employmentStartDate", "2020-01-01",
                        "actualJobPosition", "Developer",
                        "actualSalary", "5000.0"
                )
        );

        String requestJson = objectMapper.writeValueAsString(invalidPeselCommand);

        mockMvc.perform(post("/api/v1/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorsList[0].message").value("pesel: Wrong PESEl!"));
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldFailToCreatePersonWithInvalidClassType() throws Exception {
        CreateEntityCommand invalidCommand = new CreateEntityCommand(
                "unknownType",
                Map.of(
                        "name", "Magdalena",
                        "surname", "Wiśniewski",
                        "pesel", "22062568893",
                        "height", Double.toString(179.41),
                        "weight", Double.toString(64.28),
                        "email", "magdalena.wiśniewski1@email.com",
                        "monthlyPension", Double.toString(2788.78),
                        "age", Integer.toString(59)
                )
        );

        String requestJson = objectMapper.writeValueAsString(invalidCommand);

        mockMvc.perform(post("/api/v1/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("IllegalArgument"))
                .andExpect(jsonPath("$.errorsList[0].message").value("No facade found for class type: unknownType"))
                .andExpect(jsonPath("$.errorsList[0].type").value("IllegalArgument"))
                .andExpect(jsonPath("$.errorsList[0].path").value("/api/v1/persons"));
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldSuccessfullyEditPensionerWithCorrectVersion() throws Exception {
        UpdateEntityCommand updatePersonCommand = new UpdateEntityCommand(
                "Pensioner",
                Map.of(
                        "email", "updated.email@example.com",
                        "height", 190.0,
                        "lastName", "UpdatedLastName"
                ),
                0L
        );

        String updateJsonRequest = objectMapper.writeValueAsString(updatePersonCommand);

        mockMvc.perform(put("/api/v1/persons/{id}", 7L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.type").value("pensioner"))
                .andExpect(jsonPath("$.email").value("updated.email@example.com"))
                .andExpect(jsonPath("$.height").value(190.0))
                .andExpect(jsonPath("$.lastName").value("UpdatedLastName"))
                .andExpect(jsonPath("$.version").value(0));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldSuccessfullyEditEmployeeWithCorrectVersion() throws Exception {
        UpdateEntityCommand updateEmployeeCommand = new UpdateEntityCommand(
                "Employee",
                Map.of(
                        "actualJobPosition", "Senior Developer",
                        "actualSalary", 7500.0
                ),
                0L
        );

        String updateJsonRequest = objectMapper.writeValueAsString(updateEmployeeCommand);

        mockMvc.perform(put("/api/v1/persons/{id}", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.type").value("employee"))
                .andExpect(jsonPath("$.actualJobPosition").value("Senior Developer"))
                .andExpect(jsonPath("$.actualSalary").value(7500.0))
                .andExpect(jsonPath("$.version").value(0));
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldSuccessfullyEditStudentWithCorrectVersion() throws Exception {
        UpdateEntityCommand updateStudentCommand = new UpdateEntityCommand(
                "Student",
                Map.of(
                        "email", "updated.student@example.com",
                        "fieldOfStudy", "Computer Science",
                        "scholarship", 2000.0
                ),
                0L
        );

        String updateJsonRequest = objectMapper.writeValueAsString(updateStudentCommand);

        mockMvc.perform(put("/api/v1/persons/{id}", 3L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.type").value("student"))
                .andExpect(jsonPath("$.email").value("updated.student@example.com"))
                .andExpect(jsonPath("$.fieldOfStudy").value("Computer Science"))
                .andExpect(jsonPath("$.scholarship").value(2000.0))
                .andExpect(jsonPath("$.version").value(0));
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldFailToEditPersonWithIncorrectVersion() throws Exception {
        UpdateEntityCommand updatePersonCommand = new UpdateEntityCommand(
                "Pensioner",
                Map.of(
                        "name", "Zuzia",
                        "surname", "Krawczyk",
                        "pesel", "96051399842",
                        "height", 155.9,
                        "weight", 55.52,
                        "email", "zuzia.krawczyk@example.com",
                        "monthlyPension", 2739.52,
                        "age", 20
                ),
                4L
        );

        String updateJsonRequest = objectMapper.writeValueAsString(updatePersonCommand);

        mockMvc.perform(put("/api/v1/persons/{id}", 8L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJsonRequest))
                .andExpect(status().isBadRequest());
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnPersonsByFirstNamePablo() throws Exception {
        mockMvc.perform(get("/api/v1/persons")
                        .param("firstName", "Pablo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].firstName").value("Pablo"))
                .andExpect(jsonPath("$.content[0].lastName").value("Chmielewski"))
                .andExpect(jsonPath("$.content[0].pesel").value("26012659138"))
                .andExpect(jsonPath("$.content[0].email").value("pawel.chmielewski1@testmail.com"))
                .andExpect(jsonPath("$.content[0].height").value(179.41))
                .andExpect(jsonPath("$.content[0].weight").value(75.66));
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldFilterPersonsByHeightRange() throws Exception {
        mockMvc.perform(get("/api/v1/persons")
                        .param("heightFrom", "160")
                        .param("heightTo", "180"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].firstName").value("Magdalena"))
                .andExpect(jsonPath("$.content[1].firstName").value("Pablo"))
                .andExpect(jsonPath("$.content[2].firstName").value("Joanna"));
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldFilterEmployeesByEmploymentStartDateRange() throws Exception {
        mockMvc.perform(get("/api/v1/persons")
                        .param("type", "Employee")
                        .param("employmentStartDateFrom", "2019-01-01")
                        .param("employmentStartDateTo", "2020-12-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].firstName").value("Magdalena"))
                .andExpect(jsonPath("$.content[0].employmentStartDate").value("2019-02-21"));
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldFindEmployeesByJobPosition() throws Exception {
        mockMvc.perform(get("/api/v1/persons")
                        .param("jobPosition", "Software Engineer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].firstName").value("Magdalena"))
                .andExpect(jsonPath("$.content[0].lastName").value("Krawczyk"))
                .andExpect(jsonPath("$.content[0].actualJobPosition").value("Software Engineer"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnStudentsByScholarshipRange() throws Exception {
        mockMvc.perform(get("/api/v1/persons")
                        .param("type", "Student")
                        .param("scholarshipFrom", "1000")
                        .param("scholarshipTo", "1200"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].scholarship").value(1007.26));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnPensionersByPensionRange() throws Exception {
        mockMvc.perform(get("/api/v1/persons")
                        .param("type", "Pensioner")
                        .param("pensionFrom", "2700")
                        .param("pensionTo", "2800"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(3))
                .andExpect(jsonPath("$.content[*].pension", Matchers.containsInAnyOrder(2739.52, 2782.44, 2788.78)));

    }


}
