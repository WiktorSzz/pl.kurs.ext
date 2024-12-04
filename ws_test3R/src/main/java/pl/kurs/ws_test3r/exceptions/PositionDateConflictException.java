package pl.kurs.ws_test3r.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PositionDateConflictException extends Exception {
    public PositionDateConflictException(String message) {
        super(message);
    }

    public PositionDateConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}
