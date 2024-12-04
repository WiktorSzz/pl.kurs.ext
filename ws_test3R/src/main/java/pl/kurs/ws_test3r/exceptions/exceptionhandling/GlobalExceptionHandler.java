package pl.kurs.ws_test3r.exceptions.exceptionhandling;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.kurs.ws_test3r.exceptions.*;
import pl.kurs.ws_test3r.exceptions.exceptionhandling.errordto.BasicErrorDTO;
import pl.kurs.ws_test3r.exceptions.exceptionhandling.errordto.ErrorDTO;
import pl.kurs.ws_test3r.exceptions.importexcpetions.*;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.stream.Collectors;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ImportProcessingException.class)
    public ResponseEntity<ExceptionResponseDTO> handleImportProcessingException(ImportProcessingException ex, HttpServletRequest request) {
        return buildErrorResponse(ex.getMessage(), "Unsupported entity type", HttpStatus.NOT_FOUND, request);
    }


    @ExceptionHandler(ImportStatusNotFoundException.class)
    public ResponseEntity<ExceptionResponseDTO> handleImportStatusNotFound(ImportStatusNotFoundException ex, HttpServletRequest request) {
        return buildErrorResponse(ex.getMessage(), "ImportStatusNotFound", HttpStatus.NOT_FOUND, request);
    }


    @ExceptionHandler(ImportException.class)
    public ResponseEntity<ExceptionResponseDTO> handleImportException(ImportException ex, HttpServletRequest request) {
        return buildErrorResponse(ex.getMessage(), "ImportError", HttpStatus.INTERNAL_SERVER_ERROR, request);
    }


    @ExceptionHandler(NoSearchResultsException.class)
    public ResponseEntity<ExceptionResponseDTO> handleNoSearchResults(NoSearchResultsException ex, HttpServletRequest request) {
        return buildErrorResponse(ex.getMessage(), "NoSearchResults", HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponseDTO> handleEntityNotFound(EntityNotFoundException ex, HttpServletRequest request) {
        return buildErrorResponse(ex.getMessage(), "EntityNotFound", HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(InvalidCommandTypeException.class)
    public ResponseEntity<ExceptionResponseDTO> handleInvalidCommandType(InvalidCommandTypeException ex, HttpServletRequest request) {
        return buildErrorResponse(ex.getMessage(), "InvalidCommandType", HttpStatus.BAD_REQUEST, request);
    }


    @ExceptionHandler(PositionDateConflictException.class)
    public ResponseEntity<ExceptionResponseDTO> handlePositionDateConflict(PositionDateConflictException ex, HttpServletRequest request) {
        log.warn("Job position date conflict for path: {}. Error: {}", request.getRequestURI(), ex.getMessage());
        return buildErrorResponse(ex.getMessage(), "PositionDateConflict", HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(IllegalEntityException.class)
    public ResponseEntity<ExceptionResponseDTO> handleIllegalEntity(IllegalEntityException ex, HttpServletRequest request) {
        Class<?> entityType = ex.getEntityType();
        BasicErrorDTO error;
        if (entityType != null) {
            error = new BasicErrorDTO(ex.getMessage(), "IllegalEntity", request.getRequestURI(), entityType);
        } else {
            error = new BasicErrorDTO(ex.getMessage(), "IllegalEntity", request.getRequestURI());
        }
        ExceptionResponseDTO response = new ExceptionResponseDTO(
                Collections.singletonList(error),
                "IllegalEntity",
                new Timestamp(System.currentTimeMillis())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponseDTO> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String errorMsg = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return buildErrorResponse(errorMsg, "ValidationError", HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionResponseDTO> handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {
        String errorMsg = ex.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining("; "));
        return buildErrorResponse(errorMsg, "ConstraintViolation", HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionResponseDTO> handleDataIntegrityViolation(DataIntegrityViolationException ex, HttpServletRequest request) {
        return buildErrorResponse("Data integrity violation occurred.", "DataIntegrityViolation", HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponseDTO> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
        return buildErrorResponse("Malformed request body.", "MalformedRequest", HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponseDTO> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        return buildErrorResponse("Access denied.", "AccessDenied", HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionResponseDTO> handleAuthenticationException(AuthenticationException ex, HttpServletRequest request) {
        return buildErrorResponse("Authentication error.", "AuthenticationError", HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponseDTO> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        return buildErrorResponse(ex.getMessage(), "IllegalArgument", HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<ErrorDTO> handleOptimisticLockingFailure(OptimisticLockingFailureException ex) {
        ErrorDTO error = new ErrorDTO("Optimistic locking failed: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }


    private ResponseEntity<ExceptionResponseDTO> buildErrorResponse(String message, String errorCode, HttpStatus status, HttpServletRequest request) {
        BasicErrorDTO error = new BasicErrorDTO(message, errorCode, request.getRequestURI());
        ExceptionResponseDTO response = new ExceptionResponseDTO(
                Collections.singletonList(error),
                errorCode,
                new Timestamp(System.currentTimeMillis())
        );
        return ResponseEntity.status(status).body(response);
    }
}
