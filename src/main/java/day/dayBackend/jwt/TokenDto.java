package day.dayBackend.jwt;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseCookie;

@Getter
public class TokenDto {

    private String accessToken;
    private ResponseCookie refreshTokenCookie;

    @Builder
    public TokenDto(String accessToken, ResponseCookie refreshTokenCookie) {
        this.accessToken = accessToken;
        this.refreshTokenCookie = refreshTokenCookie;
    }
}
