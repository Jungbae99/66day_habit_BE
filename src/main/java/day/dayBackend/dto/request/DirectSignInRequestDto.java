package day.dayBackend.dto.request;

import day.dayBackend.util.validation.pattern.CustomPattern;
import day.dayBackend.util.validation.pattern.CustomPatternRegexp;
import lombok.Getter;

@Getter
public class DirectSignInRequestDto implements SignInRequestDto {

    @CustomPattern(regexp = CustomPatternRegexp.EMAIL, message = "{validation.Pattern.email}")
    private String email;

    @CustomPattern(regexp = CustomPatternRegexp.PASSWORD, message = "{validation.Pattern.password}")
    private String password;
}
