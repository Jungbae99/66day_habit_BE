package day.dayBackend.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class ErrorResponseDto {

    private String status;

    private final String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);

    private String message;

    private Object debugMessage;

    @Builder
    ErrorResponseDto(String status, String message, Object debugMessage) {
        this.status = status;
        this.message = message;
        this.debugMessage = debugMessage;
        System.out.println("debug Message: " + debugMessage.toString());
    }
}
