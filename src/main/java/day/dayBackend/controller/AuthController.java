package day.dayBackend.controller;

import day.dayBackend.dto.request.DirectSignInRequestDto;
import day.dayBackend.dto.response.CommonResponseDto;
import day.dayBackend.dto.response.SignInResponseDto;
import day.dayBackend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * 이메일 로그인
     */
    @PostMapping("/signIn/direct")
    public CommonResponseDto<SignInResponseDto> directSignInV1(@Valid @RequestBody final DirectSignInRequestDto dto) {
        return CommonResponseDto.<SignInResponseDto>builder()
                .data(authService.signIn(dto))
                .build();
    }
}
