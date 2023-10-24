package day.dayBackend.dto.request.member;

import day.dayBackend.util.validation.pattern.CustomPattern;
import day.dayBackend.util.validation.pattern.CustomPatternRegexp;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PasswordUpdateRequestDto {

    private String currentPassword;

    @Size(min = 8, max = 20, message = "{validation.Size}")
    @CustomPattern(regexp = CustomPatternRegexp.PASSWORD, message = "{validation.Pattern.password}")
    private String newPassword;
}
