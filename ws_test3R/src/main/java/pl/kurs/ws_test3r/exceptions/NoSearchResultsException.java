package pl.kurs.ws_test3r.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoSearchResultsException extends RuntimeException {
    public NoSearchResultsException(String message) {
        super(message);
    }
}
