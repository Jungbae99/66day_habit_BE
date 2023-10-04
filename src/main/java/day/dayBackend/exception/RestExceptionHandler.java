package day.dayBackend.exception;

import day.dayBackend.dto.response.ErrorResponseDto;
import jakarta.validation.ConstraintViolationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorResponseDto.builder()
                        .status(HttpStatus.BAD_REQUEST.toString())
                        .message("INVALID PARAMETER")
                        .debugMessage(ex.getFieldErrors().stream().collect(
                                Collectors.toMap(
                                        key -> key.getField(),
                                        val -> val.getDefaultMessage() != null ? val.getDefaultMessage() : "",
                                        (val1, val2) -> val1 + ";" + val2
                                )
                        ))
                        .build());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<ErrorResponseDto> handleConstraintViolationException(ConstraintViolationException ex) {
        String errorMessage = ex.getMessage().replace("emailDuplicationCheckV1.arg0: ", ""); //TODO 문자열 제거..?
        System.out.println(errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorResponseDto.builder()
                        .status(HttpStatus.BAD_REQUEST.toString())
                        .message("Constraint violation")
                        .debugMessage(errorMessage)
                        .build()
        );
    }

    @ExceptionHandler(DuplicateKeyException.class)
    protected ResponseEntity<ErrorResponseDto> handleDuplicateKeyException(DuplicateKeyException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                ErrorResponseDto.builder()
                        .status(HttpStatus.CONFLICT.toString())
                        .message("Duplicate resource")
                        .debugMessage(ex.getMessage())
                        .build()
        );
    }

}
