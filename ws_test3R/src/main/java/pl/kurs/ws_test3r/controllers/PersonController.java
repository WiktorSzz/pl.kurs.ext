package pl.kurs.ws_test3r.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kurs.ws_test3r.models.commands.CreateEntityCommand;
import pl.kurs.ws_test3r.models.commands.UpdateEntityCommand;
import pl.kurs.ws_test3r.models.dto.PersonDTO;
import pl.kurs.ws_test3r.services.PersonService;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @GetMapping
    public ResponseEntity<Page<PersonDTO>> getPersons(
            @RequestParam Map<String, String> searchParams,
            @PageableDefault Pageable pageRequest) {
        Page<PersonDTO> result = personService.search(searchParams, pageRequest);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonDTO> getPersonById(@PathVariable Long id) {
        PersonDTO personDTO = personService.getPersonDetails(id);
        return ResponseEntity.ok(personDTO);
    }

    @PostMapping
    public ResponseEntity<PersonDTO> addPerson(@RequestBody @Valid CreateEntityCommand command) {
        PersonDTO createdPerson = personService.createPerson(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPerson);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonDTO> updatePerson(@PathVariable Long id, @Valid @RequestBody UpdateEntityCommand updateCommand) {
        PersonDTO updatedPerson = personService.modifyPerson(id, updateCommand);
        return ResponseEntity.ok(updatedPerson);
    }

}
