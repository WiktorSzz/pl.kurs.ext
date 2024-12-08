package pl.kurs.ws_test3r.exceptions.exceptionhandling.errordto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BasicErrorDTO extends ErrorDTO {
    private String type;
    private String path;
    private Class<?> entityType;

    public BasicErrorDTO(String message, String type, String path) {
        super(message);
        this.type = type;
        this.path = path;
    }

    public BasicErrorDTO(String message, String type, String path, Class<?> entityType) {
        super(message);
        this.type = type;
        this.path = path;
        this.entityType = entityType;
    }
}
