package day.dayBackend.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseCookie;

@Getter
public class SignInResponseDto {

    private String email;
    private String username;
    private String accessToken;
    private ResponseCookie responseCookie;

    public void setResponseCookie(ResponseCookie responseCookie) {
        this.responseCookie = responseCookie;
    }

    @Builder
    public SignInResponseDto(String email, String username, String accessToken, ResponseCookie responseCookie) {
        this.email = email;
        this.username = username;
        this.accessToken = accessToken;
        this.responseCookie = responseCookie;
    }
}
