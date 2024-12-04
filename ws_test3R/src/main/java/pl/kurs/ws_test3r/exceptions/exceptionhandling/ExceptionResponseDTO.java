package pl.kurs.ws_test3r.exceptions.exceptionhandling;

import pl.kurs.ws_test3r.exceptions.exceptionhandling.errordto.ErrorDTO;

import java.sql.Timestamp;
import java.util.List;

public class ExceptionResponseDTO {
    private List<? extends ErrorDTO> errorsList;
    private String errorCode;
    private Timestamp date;

    public ExceptionResponseDTO(List<? extends ErrorDTO> errorsList, String errorCode, Timestamp date) {
        this.errorsList = errorsList;
        this.errorCode = errorCode;
        this.date = date;
    }

    public List<? extends ErrorDTO> getErrorsList() {
        return errorsList;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public Timestamp getDate() {
        return date;
    }
}
