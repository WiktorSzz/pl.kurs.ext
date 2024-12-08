package pl.kurs.ws_test3r.models.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.kurs.ws_test3r.models.Status;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ImportStatusDTO {
    private Long id;
    private String fileName;
    private Status status;
    private LocalDateTime createdDate;


}
