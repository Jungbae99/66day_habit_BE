package day.dayBackend.controller;

import day.dayBackend.dto.request.DirectSignInRequestDto;
import day.dayBackend.dto.response.CommonResponseDto;
import day.dayBackend.dto.response.SignInResponseDto;
import day.dayBackend.jwt.TokenDto;
import day.dayBackend.jwt.TokenProvider;
import day.dayBackend.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "https://localhost:3000", allowedHeaders = "*", allowCredentials = "true")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * 이메일 로그인
     */
    @PostMapping("/signIn/direct")
    public ResponseEntity<CommonResponseDto<SignInResponseDto>> directSignInV1(@Valid @RequestBody final DirectSignInRequestDto dto) {
        SignInResponseDto signInResponseDto = authService.signIn(dto);
        String refCookie = signInResponseDto.getResponseCookie().toString();
        signInResponseDto.setResponseCookie(null);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refCookie)
                .body(CommonResponseDto.<SignInResponseDto>builder()
                        .data(signInResponseDto)
                        .build());
    }

    /**
     * 토큰 리프레시
     */
    @PostMapping("/refresh")
    public ResponseEntity<CommonResponseDto<TokenDto>> refreshTokenV1(@CookieValue Cookie jwtCookie) {
        TokenDto tokenDto = authService.tokenRefresh(jwtCookie.getValue());
        String refCookie = tokenDto.getRefreshTokenCookie().toString();
        tokenDto.setRefreshTokenCookie(null);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refCookie)
                .body(CommonResponseDto.<TokenDto>builder()
                        .data(tokenDto)
                        .build());
    }
}
