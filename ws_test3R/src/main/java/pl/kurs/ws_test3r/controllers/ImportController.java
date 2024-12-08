package pl.kurs.ws_test3r.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.kurs.ws_test3r.models.ImportStatus;
import pl.kurs.ws_test3r.services.EntityImportService;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/import")
@RequiredArgsConstructor
public class ImportController {

    private final EntityImportService importService;

    @PostMapping
    public ResponseEntity<ImportStatus> importFile(@RequestPart("file") MultipartFile file) throws IOException {
        ImportStatus newImport = importService.registerImport(file.getOriginalFilename());
        importService.processEntityFile(file.getInputStream(), newImport.getId(), file.getOriginalFilename());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(newImport);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ImportStatus> findImport(@PathVariable Long id) {
        ImportStatus actualImport = importService.searchById(id);
        return ResponseEntity.ok(actualImport);
    }

}


