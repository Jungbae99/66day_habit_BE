package day.dayBackend.controller;

import day.dayBackend.dto.request.member.MemberDeleteRequestDto;
import day.dayBackend.dto.request.member.PasswordUpdateRequestDto;
import day.dayBackend.dto.request.member.EmailUpdateRequestDto;
import day.dayBackend.dto.request.member.MemberDirectCreateRequestDto;
import day.dayBackend.dto.request.member.MemberUpdateRequestDto;
import day.dayBackend.dto.response.*;
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
     * 메인페이지 회원정보 보기
     */
    @GetMapping("")
    public CommonResponseDto<MemberResponseDto> getMyInfoV1(@RequestParam(value = "id") Long id) {
        //:TODO ContextHolder 에서 PK 꺼내는 것으로 변경
        return CommonResponseDto.<MemberResponseDto>builder()
                .data(memberService.getMemberById(id))
                .build();
    }

    /**
     * 회원정보 상세 보기
     */
    @GetMapping("/detail")
    public CommonResponseDto<MemberDetailResponseDto> getMyDetailInfoV1(@RequestParam(value = "id") Long id) {
        //:TODO ContextHolder 에서 PK 꺼내는 것으로 변경
        return CommonResponseDto.<MemberDetailResponseDto>builder()
                .data(memberService.getMemberDetailById(id))
                .build();
    }

    /**
     * 회원정보 수정
     */
    @PatchMapping("")
    public CommonResponseDto<MemberUpdateResponseDto> updateMyInfoV1(@Valid @RequestBody final MemberUpdateRequestDto dto, @RequestParam(value = "id") Long id) {
        return CommonResponseDto.<MemberUpdateResponseDto>builder()
                .data(memberService.updateMember(id, dto))
                .build();
    }

    /**
     * 이메일 수정
     */
    @PutMapping("/email")
    public CommonResponseDto<EmailUpdateResponseDto> updateEmailV1(@Valid @RequestBody final EmailUpdateRequestDto dto, @RequestParam(value = "id") Long id) {
        return CommonResponseDto.<EmailUpdateResponseDto>builder()
                .data(memberService.updateEmail(id, dto))
                .build();
    }

    /**
     * 비밀번호 수정
     */
    @PutMapping("/password")
    public CommonResponseDto updatePasswordV1(@Valid @RequestBody final PasswordUpdateRequestDto dto, @RequestParam(value = "id") Long id) {
        memberService.updatePassword(id, dto);
        return CommonResponseDto.builder().build();
    }

    /**
     * 회원 탈퇴
     */
    @DeleteMapping("")
    public CommonResponseDto resignV1(@RequestParam(value = "id") Long id, @RequestBody final MemberDeleteRequestDto dto) {
        memberService.resign(id, dto);
        return CommonResponseDto.builder().build();
    }

}
