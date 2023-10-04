package day.dayBackend.controller;

import day.dayBackend.dto.request.MemberDirectCreateRequestDto;
import day.dayBackend.dto.response.CommonResponseDto;
import day.dayBackend.dto.response.MemberResponseDto;
import day.dayBackend.service.MemberService;
import day.dayBackend.service.SignInService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/member")
public class MemberController {

    private final MemberService memberService;
    private final SignInService signInService;

    /**
     * 직접 회원가입
     */
    @PostMapping("/direct")
    public CommonResponseDto<Map<String, Long>> joinDirectV1(@Valid @RequestBody MemberDirectCreateRequestDto dto) {
        return CommonResponseDto.<Map<String, Long>>builder()
                .data(Map.of("memberId", signInService.directJoin(dto)))
                .build();
    }

    /**
     * Email 중복체크
     */
    @GetMapping("/email")
    public CommonResponseDto emailDuplicationCheckV1(@Valid @RequestParam(value = "email") @Email(message = "{validation.Pattern.email}") String email) {
        System.out.println("email = " + email);
        signInService.emailDuplicationCheck(email);
        return CommonResponseDto.builder().build();
    }


    /**
     * 내 정보 보기
     */
    public CommonResponseDto<MemberResponseDto> getMyInfoV1() {
        return CommonResponseDto.<MemberResponseDto>builder()
                // :TODO 
                .build();
    }

}
