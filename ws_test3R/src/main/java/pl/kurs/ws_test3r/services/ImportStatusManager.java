package pl.kurs.ws_test3r.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.ws_test3r.exceptions.importexcpetions.ImportStatusNotFoundException;
import pl.kurs.ws_test3r.models.ImportStatus;
import pl.kurs.ws_test3r.models.Status;
import pl.kurs.ws_test3r.repositories.ImportStatusRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImportStatusManager {

    private final ImportStatusRepository repository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateProgress(Long importStatusId, int processed, int failed) {
        ImportStatus status = getImportStatus(importStatusId);
        status.setProcessedRows((long) processed);
        status.setFailedRows((long) failed);
        repository.saveAndFlush(status);
        log.info("Updated progress for ImportStatus ID: {} - Processed: {}, Failed: {}", importStatusId, processed, failed);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ImportStatus saveStatus(ImportStatus status) {
        status.setCreatedDate(LocalDateTime.now());
        ImportStatus savedStatus = repository.saveAndFlush(status);
        log.info("Saved ImportStatus with ID: {}", savedStatus.getId());
        return savedStatus;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ImportStatus markAsRunning(Long importId) {
        ImportStatus status = getImportStatus(importId);
        status.setStatus(Status.RUNNING);
        status.setStartDate(LocalDateTime.now());
        return repository.saveAndFlush(status);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ImportStatus markAsCompleted(Long importId, int totalProcessed, int failed) {
        ImportStatus status = getImportStatus(importId);
        status.setStatus(Status.COMPLETED);
        status.setProcessedRows((long) totalProcessed);
        status.setFailedRows((long) failed);
        status.setEndDate(LocalDateTime.now());
        status.setDuration(getDuration(status));
        return repository.saveAndFlush(status);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ImportStatus markAsFailed(Long importId) {
        ImportStatus status = getImportStatus(importId);
        status.setStatus(Status.FAILED);
        status.setEndDate(LocalDateTime.now());
        status.setDuration(getDuration(status));
        return repository.saveAndFlush(status);
    }

    public ImportStatus getImportStatus(Long importId) {
        return repository.findById(importId)
                .orElseThrow(() -> new ImportStatusNotFoundException("ImportStatus not found for ID: " + importId));
    }

    private Long getDuration(ImportStatus status) {
        if (status.getStartDate() == null || status.getEndDate() == null) {
            return null;
        }
        return java.time.Duration.between(status.getStartDate(), status.getEndDate()).toMillis();
    }
}
