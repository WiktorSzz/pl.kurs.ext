package pl.kurs.ws_test3r.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidCommandTypeException extends RuntimeException {
    public InvalidCommandTypeException(String message) {
        super(message);
    }

    public InvalidCommandTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
