package pl.kurs.ws_test3r.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.ws_test3r.exceptions.importexcpetions.ImportProcessingException;
import pl.kurs.ws_test3r.models.ImportStatus;
import pl.kurs.ws_test3r.models.Status;
import pl.kurs.ws_test3r.models.dto.ImportStatusDTO;
import pl.kurs.ws_test3r.sqlquery.PersonSqlQuery;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;


@Service
@RequiredArgsConstructor
@Slf4j
public class EntityImportService {

    private final JdbcTemplate jdbcTemplate;
    private final ImportStatusManager importStatusManager;
    private final Map<String, PersonSqlQuery> entityQueryRegistry;

    @Transactional
    @Async("importTaskExecutor")
    public void processEntityFile(InputStream inputStream, Long id, String fileName) {
        importStatusManager.markAsRunning(id);

        AtomicInteger counter = new AtomicInteger(0);
        AtomicLong operationStart = new AtomicLong(System.currentTimeMillis());

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            reader.lines()
                    .map(this::parseLine)
                    .forEach(record -> {
                        persistEntity(record);
                        counter.incrementAndGet();
                        trackProgress(counter, operationStart, id);
                    });

            importStatusManager.markAsCompleted(id, counter.get(), 0);
        } catch (Exception e) {
            log.error("An error occurred while processing the file", e);
            importStatusManager.markAsFailed(id);
            throw new ImportProcessingException("Failed to process the file.");
        }

        ImportStatusDTO importStatusDTO = new ImportStatusDTO(id, fileName, Status.CREATED, LocalDateTime.now());
        log.info("DTO created: {}", importStatusDTO);
    }

    private String[] parseLine(String line) {
        return line.split(",");
    }

    private void trackProgress(AtomicInteger counter, AtomicLong startTime, Long importId) {
        int processedRecords = counter.get();
        if (processedRecords % 50000 == 0) {
            long duration = System.currentTimeMillis() - startTime.get();
            log.info("Processed {} records in {} ms", processedRecords, duration);
            startTime.set(System.currentTimeMillis());
            importStatusManager.updateProgress(importId, processedRecords, 0);
        }
    }

    private void persistEntity(String[] entityData) {
        String queryKey = entityData[0].toLowerCase() + "Query";
        PersonSqlQuery query = entityQueryRegistry.get(queryKey);

        if (query == null) {
            throw new ImportProcessingException("Unsupported entity type: " + entityData[0]);
        }

        try {
            Object[] parameters = Arrays.copyOfRange(entityData, 1, entityData.length);
            jdbcTemplate.update(query.sqlQueryToInsert(), parameters);
        } catch (Exception ex) {
            log.error("Error saving entity: {}", Arrays.toString(entityData), ex);
            throw new ImportProcessingException("Error occurred during entity persistence.");
        }
    }

    @Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
    public ImportStatus searchById(Long id) {
        return importStatusManager.getImportStatus(id);
    }

    public ImportStatus registerImport(String fileName) {
        return importStatusManager.saveStatus(new ImportStatus(fileName));
    }
}