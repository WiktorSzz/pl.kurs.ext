package pl.kurs.ws_test3r.models;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonInclude;

@NoArgsConstructor
@Entity
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImportStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime createdDate;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long processedRows;
    private String fileName;
    private Long failedRows;
    private Long duration;

    public ImportStatus(String fileName) {
        this.fileName = fileName;
        this.status = Status.CREATED;
        this.createdDate = LocalDateTime.now();
    }
}
