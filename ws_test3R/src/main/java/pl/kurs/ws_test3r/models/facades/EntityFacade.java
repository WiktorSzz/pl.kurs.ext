package pl.kurs.ws_test3r.models.facades;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface EntityFacade<ENTITY, DTO> {

    default ENTITY createEntity(List<EntityParameter> parameters) {
        return buildEntityFromMap(parameters.stream().collect(Collectors.toMap(EntityParameter::getFieldName, EntityParameter::getFieldValue)));
    }

    ENTITY buildEntityFromMap(Map<String, String> parameters);

    ENTITY updateEntity(ENTITY entity, Map<String, Object> updates);

    DTO toDto(ENTITY entity);


}
