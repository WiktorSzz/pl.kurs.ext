package pl.kurs.ws_test3r.models.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StatusDTO {
    private String status;

    public StatusDTO(String status) {
        this.status = status;
    }
}
