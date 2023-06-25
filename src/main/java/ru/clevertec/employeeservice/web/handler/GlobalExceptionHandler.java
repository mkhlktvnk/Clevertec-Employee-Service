package ru.clevertec.employeeservice.web.handler;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.clevertec.employeeservice.service.exception.InvalidDataException;
import ru.clevertec.employeeservice.service.exception.ResourceNotFoundException;
import ru.clevertec.employeeservice.web.handler.response.ApiError;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException e,
                                                                    HttpServletRequest request) {
        ApiError apiError = ApiError.builder()
                .message(e.getMessage())
                .errorUrl(request.getRequestURI())
                .errorTime(Instant.now())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(apiError);
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<ApiError> handleInvalidDataException(InvalidDataException e, HttpServletRequest request) {
        ApiError apiError = ApiError.builder()
                .message(e.getMessage())
                .errorUrl(request.getRequestURI())
                .errorTime(Instant.now())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(apiError);
    }
}
