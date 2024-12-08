package pl.kurs.ws_test3r.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kurs.ws_test3r.exceptions.PositionDateConflictException;
import pl.kurs.ws_test3r.models.commands.CreateEmployeeJobPositionCommand;
import pl.kurs.ws_test3r.models.dto.EmployeeDTO;

import pl.kurs.ws_test3r.services.EmployeeJobPositionService;

@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeJobPositionService employeeJobPositionService;


    @PostMapping("/{employeeId}/assign-job-position")
    public ResponseEntity<EmployeeDTO> assignJobPosition(
            @PathVariable("employeeId") Long employeeId,
            @RequestBody @Valid CreateEmployeeJobPositionCommand command) throws PositionDateConflictException {
        EmployeeDTO updatedEmployee = employeeJobPositionService.createEmployeeJobPosition(employeeId, command);
        return ResponseEntity.ok(updatedEmployee);
    }

}