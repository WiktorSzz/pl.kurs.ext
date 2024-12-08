package pl.kurs.ws_test3r.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.ws_test3r.config.FacadeRegistry;
import pl.kurs.ws_test3r.exceptions.EntityNotFoundException;
import pl.kurs.ws_test3r.exceptions.InvalidCommandTypeException;
import pl.kurs.ws_test3r.exceptions.NoSearchResultsException;
import pl.kurs.ws_test3r.models.commands.CreateEntityCommand;
import pl.kurs.ws_test3r.models.commands.UpdateEntityCommand;
import pl.kurs.ws_test3r.models.dto.PersonDTO;
import pl.kurs.ws_test3r.models.facades.EntityFacade;
import pl.kurs.ws_test3r.models.facades.EntityParameter;
import pl.kurs.ws_test3r.models.person.Person;
import pl.kurs.ws_test3r.models.specifications.PersonFilterService;
import pl.kurs.ws_test3r.repositories.PersonRepository;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonService {

    private final FacadeRegistry facadeRegistry;
    private final PersonRepository personRepository;
    private final PersonFilterService searchService;


    @Transactional(readOnly = true)
    public Page<PersonDTO> search(Map<String, String> filters, Pageable pageable) {
        Specification<Person> combinedSpecification = filters.entrySet().stream()
                .map(entry -> searchService.resolveFilter(entry.getKey(), entry.getValue()))
                .filter(spec -> spec != null)
                .reduce(Specification.where(null), Specification::and);

        Page<Person> resultPage = personRepository.findAll(combinedSpecification, pageable);

        if (resultPage.isEmpty()) {
            throw new NoSearchResultsException("No results found for the provided search parameters.");
        }
        return resultPage.map(this::convertToDTO);
    }


    @Transactional
    public PersonDTO createPerson(CreateEntityCommand command) {
        validateCommandType(command.getType());
        String facadeIdentifier = resolveFacadeKey(command.getType());

        EntityFacade<Person, PersonDTO> facadeInstance = facadeRegistry.getFacade(facadeIdentifier, Person.class, PersonDTO.class);

        if (facadeInstance == null) {
            throw new EntityNotFoundException("No facade registered for class type: " + command.getType());
        }

        log.info("Creating person of type: {} with parameters: {}", command.getType(), command.getParameters());
        List<EntityParameter> parametersList = command.getParameters().entrySet().stream()
                .map(entry -> new EntityParameter(entry.getKey(), entry.getValue()))
                .toList();

        Person personToSave = facadeInstance.createEntity(parametersList);
        personToSave = personRepository.saveAndFlush(personToSave);
        return facadeInstance.toDto(personToSave);
    }

    @Transactional
    public PersonDTO modifyPerson(Long id, UpdateEntityCommand command) {
        log.info("Command version before updateEntity: {}", command.getVersion());
        Person existingEntity = personRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Person with ID " + id + " does not exist."));
        if (!existingEntity.getClass().getSimpleName().equalsIgnoreCase(command.getClassType())) {
            throw new InvalidCommandTypeException("Mismatched class type: expected " +
                    existingEntity.getClass().getSimpleName() + ", but got " + command.getClassType());
        }
        String facadeIdentifier = resolveFacadeKey(command.getClassType());
        EntityFacade<Person, PersonDTO> facadeInstance = (EntityFacade<Person, PersonDTO>) facadeRegistry.getFacade(facadeIdentifier, Person.class, PersonDTO.class);
        if (facadeInstance == null) {
            throw new EntityNotFoundException("No facade registered for class type: " + command.getClassType());
        }
        Person updatedEntity = facadeInstance.updateEntity(existingEntity, command);
        personRepository.saveAndFlush(updatedEntity);
        return facadeInstance.toDto(updatedEntity);
    }


    @Transactional(readOnly = true)
    public PersonDTO getPersonDetails(Long id) {
        Person foundPerson = personRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Person with ID " + id + " does not exist."));

        return convertToDTO(foundPerson);
    }

    private void validateCommandType(String type) {
        if (type == null || type.isEmpty()) {
            throw new InvalidCommandTypeException("Class type cannot be null or empty.");
        }
    }

    private PersonDTO convertToDTO(Person personEntity) {
        String facadeIdentifier = resolveFacadeKey(personEntity.getClass().getSimpleName());
        EntityFacade<Person, PersonDTO> facadeInstance = facadeRegistry.getFacade(facadeIdentifier, Person.class, PersonDTO.class);
        return facadeInstance.toDto(personEntity);
    }


    private String resolveFacadeKey(String classType) {
        if (facadeRegistry.getFacadeKeys().contains(classType.toLowerCase() + "Facade")) {
            return classType.toLowerCase() + "Facade";
        }
        throw new IllegalArgumentException("No facade found for class type: " + classType);
    }
}

