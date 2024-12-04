package pl.kurs.ws_test3r.exceptions.importexcpetions;

public class ImportStatusNotFoundException extends ImportException {
    public ImportStatusNotFoundException(String message) {
        super(message);
    }

    public ImportStatusNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}