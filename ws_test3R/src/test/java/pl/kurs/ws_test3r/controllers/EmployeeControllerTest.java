package pl.kurs.ws_test3r.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pl.kurs.ws_test3r.models.commands.CreateEmployeeJobPositionCommand;
import pl.kurs.ws_test3r.repositories.EmployeeRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    EmployeeRepository employeeRepository;


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldFailWhenAssigningJobPositionWithConflictingDates() throws Exception {
        Long employeeId = 1L;

        CreateEmployeeJobPositionCommand command = new CreateEmployeeJobPositionCommand(
                "Conflicting Position",
                8000.0,
                LocalDate.of(2018, 01, 01),
                LocalDate.of(2020, 01, 01)
        );

        mockMvc.perform(post("/api/employee/" + employeeId + "/assign-job-position")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldFailWhenAssigningJobPositionToNonExistentEmployee() throws Exception {
        Long nonExistentEmployeeId = 999L;

        CreateEmployeeJobPositionCommand command = new CreateEmployeeJobPositionCommand(
                "Non-Existent Position",
                5000.0,
                LocalDate.of(2023, 01, 01),
                LocalDate.of(2024, 01, 01)
        );

        mockMvc.perform(post("/api/employee/" + nonExistentEmployeeId + "/assign-job-position")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldFailWhenAssigningJobPositionWithInvalidData() throws Exception {
        Long employeeId = 1L;

        CreateEmployeeJobPositionCommand command = new CreateEmployeeJobPositionCommand(
                "",
                -5000.0,
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2024, 1, 1)
        );

        mockMvc.perform(post("/api/employee/" + employeeId + "/assign-job-position")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void shouldFailWhenNoAuthorization() throws Exception {
        Long employeeId = 1L;

        CreateEmployeeJobPositionCommand command = new CreateEmployeeJobPositionCommand(
                "Junior Developer",
                5000.0,
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2025, 1, 1)
        );

        mockMvc.perform(post("/api/employee/" + employeeId + "/assign-job-position")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldPreventRaceConditionWhenAssigningJobPosition() throws Exception {
        Long employeeId = 1L;

        CreateEmployeeJobPositionCommand command = new CreateEmployeeJobPositionCommand(
                "Junior Developer",
                5000.0,
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2025, 1, 1)
        );

        String commandJson = objectMapper.writeValueAsString(command);

        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        List<Callable<Void>> tasks = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            tasks.add(() -> {
                mockMvc.perform(post("/api/employee/" + employeeId + "/assign-job-position")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(commandJson))
                        .andExpect(status().isOk());
                return null;
            });
        }
        executorService.invokeAll(tasks);
        executorService.shutdown();
    }

}

