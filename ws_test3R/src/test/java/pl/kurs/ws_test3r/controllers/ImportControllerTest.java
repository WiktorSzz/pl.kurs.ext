package pl.kurs.ws_test3r.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import pl.kurs.ws_test3r.exceptions.importexcpetions.ImportProcessingException;
import pl.kurs.ws_test3r.exceptions.importexcpetions.ImportStatusNotFoundException;
import pl.kurs.ws_test3r.models.ImportStatus;
import pl.kurs.ws_test3r.models.Status;
import pl.kurs.ws_test3r.services.EntityImportService;

import java.io.InputStream;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class ImportControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EntityImportService importService;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void shouldRollbackTransactionWhenInvalidDataIsEncountered() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "invalid.csv",
                MediaType.TEXT_PLAIN_VALUE,
                ("TYP,imie,nazwisko,pesel,wzrost,waga,adres email\n" +
                        "Invalid,Content,12345678901,180,75,invalid@example.com\n").getBytes()
        );

        ImportStatus mockImportStatus = new ImportStatus();
        mockImportStatus.setId(1L);
        Mockito.when(importService.registerImport(Mockito.anyString())).thenReturn(mockImportStatus);

        Mockito.doThrow(new ImportProcessingException("Unsupported entity type"))
                .when(importService).processEntityFile(Mockito.any(), Mockito.anyLong(), Mockito.anyString());

        mockMvc.perform(multipart("/api/v1/import")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertTrue(
                        result.getResolvedException() instanceof ImportProcessingException))
                .andExpect(result -> Assertions.assertEquals("Unsupported entity type",
                        result.getResolvedException().getMessage()));
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testImportPersonAndCheckIfExistsInDatabaseByAdmin() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.csv",
                "text/plain",
                ("Employee,Wacław,Kolebski,010142345,180,80,waclawoo.kolebski@example.com,2012-03-01,Manager,5400.0\n").getBytes()
        );

        ImportStatus mockImportStatus = new ImportStatus("test.csv");
        mockImportStatus.setId(1L);
        Mockito.when(importService.registerImport(Mockito.anyString())).thenReturn(mockImportStatus);

        Mockito.doNothing()
                .when(importService)
                .processEntityFile(Mockito.any(InputStream.class), Mockito.eq(1L), Mockito.anyString());

        mockMvc.perform(multipart("/api/v1/import")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isAccepted());

        Mockito.verify(importService, Mockito.times(1)).registerImport(Mockito.anyString());
        Mockito.verify(importService, Mockito.times(1))
                .processEntityFile(Mockito.any(InputStream.class), Mockito.eq(1L), Mockito.anyString());
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void shouldReturnImportStatusWhenValidIdIsProvided() throws Exception {
        ImportStatus status = new ImportStatus();
        status.setId(1L);
        status.setStatus(Status.RUNNING);
        status.setCreatedDate(LocalDateTime.now());
        status.setStartDate(LocalDateTime.now());
        status.setProcessedRows(1000L);
        status.setFailedRows(10L);

        Mockito.when(importService.searchById(1L)).thenReturn(status);

        mockMvc.perform(get("/api/v1/import/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("RUNNING"))
                .andExpect(jsonPath("$.processedRows").value(1000L))
                .andExpect(jsonPath("$.failedRows").value(10L));
    }


    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void shouldReturnForbiddenWhenUserWithInsufficientRoleTriesToStartImport() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.csv",
                "text/csv",
                "TYP,imie,nazwisko,pesel,wzrost,waga,adres email\n".getBytes()
        );

        mockMvc.perform(multipart("/api/v1/import")
                        .file(file))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void shouldReturnImportStatusDetailsForValidId() throws Exception {
        ImportStatus status = new ImportStatus();
        status.setId(2L);
        status.setStatus(Status.COMPLETED);
        status.setCreatedDate(LocalDateTime.now().minusMinutes(10));
        status.setStartDate(LocalDateTime.now().minusMinutes(5));
        status.setEndDate(LocalDateTime.now());
        status.setProcessedRows(2000L);
        status.setFailedRows(50L);

        Mockito.when(importService.searchById(2L)).thenReturn(status);

        mockMvc.perform(get("/api/v1/import/2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.status").value("COMPLETED"))
                .andExpect(jsonPath("$.processedRows").value(2000L))
                .andExpect(jsonPath("$.failedRows").value(50L));
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void shouldReturnNotFoundWhenImportStatusDoesNotExist() throws Exception {
        Mockito.when(importService.searchById(99L))
                .thenThrow(new ImportStatusNotFoundException("Import status not found for ID: 99"));

        mockMvc.perform(get("/api/v1/import/99")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("ImportStatusNotFound"))
                .andExpect(jsonPath("$.errorsList[0].message").value("Import status not found for ID: 99"));
    }

    @Test
    @WithMockUser(username = "viewer", roles = {"VIEWER"})
    public void shouldReturnForbiddenWhenUserWithoutAdminRoleTriesToStartImport() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.csv",
                MediaType.TEXT_PLAIN_VALUE,
                ("TYP,imie,nazwisko,pesel,wzrost,waga,adres email\n" +
                        "Employee,John,Doe,12345678901,180,75,john.doe@example.com\n").getBytes()
        );

        mockMvc.perform(multipart("/api/v1/import")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void shouldImportEmployeeAndVerify() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "employees.csv",
                "text/plain",
                "Employee,Aleksandra,Nowak,91010167890,165,60,aleksandra.nowak@example.com,2010-06-15,Team Lead,7000.0\n".getBytes()
        );
        Long id = 1L;
        String fileName = "employees.csv";
        ImportStatus mockImportStatus = new ImportStatus();
        mockImportStatus.setId(id);
        mockImportStatus.setFileName(fileName);

        Mockito.when(importService.registerImport(Mockito.anyString())).thenReturn(mockImportStatus);
        Mockito.doNothing().when(importService).processEntityFile(Mockito.any(InputStream.class), Mockito.eq(id), Mockito.eq(fileName));

        mockMvc.perform(multipart("/api/v1/import")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isAccepted());

        Mockito.verify(importService, Mockito.times(1)).registerImport(Mockito.anyString());
        Mockito.verify(importService, Mockito.times(1)).processEntityFile(Mockito.any(InputStream.class), Mockito.eq(id), Mockito.eq(fileName));
    }


}

