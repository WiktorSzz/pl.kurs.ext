package pl.kurs.ws_test3r.models.facades;

import pl.kurs.ws_test3r.models.commands.UpdateEntityCommand;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface EntityFacade<ENTITY, DTO> {

    default ENTITY createEntity(List<EntityParameter> parameters) {
        return buildEntityFromMap(parameters.stream().collect(Collectors.toMap(EntityParameter::getFieldName, EntityParameter::getFieldValue)));
    }

    ENTITY buildEntityFromMap(Map<String, String> parameters);

    ENTITY updateEntity(ENTITY entity, UpdateEntityCommand command);

    DTO toDto(ENTITY entity);


}
