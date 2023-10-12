package day.dayBackend.controller;

import day.dayBackend.dto.request.DirectSignInRequestDto;
import day.dayBackend.dto.response.CommonResponseDto;
import day.dayBackend.dto.response.SignInResponseDto;
import day.dayBackend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signIn/direct")
    public ResponseEntity<CommonResponseDto<SignInResponseDto>> directSignInV1(@Valid @RequestBody final DirectSignInRequestDto dto) {
        SignInResponseDto signInResponseDto = authService.signIn(dto);
        String refToken = signInResponseDto.getResponseCookie().toString();
        signInResponseDto.setResponseCookie(null);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refToken)
                .body(CommonResponseDto.<SignInResponseDto>builder()
                        .data(signInResponseDto)
                        .build());
    }
}
