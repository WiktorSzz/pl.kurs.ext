package pl.kurs.ws_test3r.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IllegalEntityException extends IllegalArgumentException {

    private Class<?> entityType;

    public IllegalEntityException(String s) {
        super(s);
    }

    public IllegalEntityException(String s, Class<?> entityType) {
        super(s);
        this.entityType = entityType;
    }

    public Class<?> getEntityType() {
        return entityType;
    }
}
