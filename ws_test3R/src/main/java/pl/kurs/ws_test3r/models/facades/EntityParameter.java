package pl.kurs.ws_test3r.models.facades;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString

public class EntityParameter {

    private String fieldName;
    private String fieldValue;

    public EntityParameter(String fieldName, String fieldValue) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

}
