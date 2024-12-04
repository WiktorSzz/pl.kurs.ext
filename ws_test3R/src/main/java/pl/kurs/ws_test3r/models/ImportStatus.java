package pl.kurs.ws_test3r.models;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
        this.status = status;
        this.fileName = fileName;
        this.createdDate = createdDate;
    }

}
