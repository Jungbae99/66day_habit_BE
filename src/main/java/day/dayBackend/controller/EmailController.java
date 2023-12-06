package day.dayBackend.controller;

import day.dayBackend.dto.response.CommonResponseDto;
import day.dayBackend.service.SignInService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/email")
public class EmailController {

    private final SignInService signInService;

    /**
     * 메일 코드 검증
     */
    @PutMapping("/certification")
    public CommonResponseDto verifyCertificationEmailV1(
            @RequestParam(value = "certCode") String certCode,
            @RequestParam(value = "email") @Valid @Email(message = "{validation.Pattern.email}") String email
    ) {
        signInService.verifyCertificationEmail(certCode, email);
        return CommonResponseDto.builder().build(); // TODO: 리턴값 어떻게 할지 생각
    }

    /**
     * 인증 이메일 발송
     */
    @PostMapping("/certification")
    public CommonResponseDto sendCertificationEmailV1(
            @RequestParam(value = "email") @Valid @Email(message = "{validation.Email}") String email) {
        signInService.sendCertificationEmail(email);
        return CommonResponseDto.builder().build();
    }


}
