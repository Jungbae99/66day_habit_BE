package day.dayBackend.dto.response;

import day.dayBackend.jwt.TokenDto;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SignInResponseDto {

    private String email;
    private String username;
//    private String profileImageUrl;
    private String accessToken;
    private String refreshToken;

    @Builder
    public SignInResponseDto(String email, String username, TokenDto token) {
        this.email = email;
        this.username = username;
        this.accessToken = token.getAccessToken();
        this.refreshToken = token.getRefreshToken();
    }
}
