package pl.kurs.ws_test3r.models.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = EmployeeDTO.class, name = "employee"),
        @JsonSubTypes.Type(value = StudentDTO.class, name = "student"),
        @JsonSubTypes.Type(value = PensionerDTO.class, name = "pensioner")
})

public abstract class PersonDTO {
    private long id;
    private String firstName;
    private String lastName;
    private String pesel;
    private double height;
    private double weight;
    private String email;
    private Long version;


}
