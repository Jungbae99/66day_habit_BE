package day.dayBackend.dto.request.member;

import day.dayBackend.util.validation.pattern.CustomPattern;
import day.dayBackend.util.validation.pattern.CustomPatternRegexp;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class EmailUpdateRequestDto {

    @CustomPattern(regexp = CustomPatternRegexp.EMAIL, message = "{validation.Pattern.email}")
    private String newEmail;

}
