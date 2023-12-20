package day.dayBackend.controller;

import day.dayBackend.config.SecurityUtil;
import day.dayBackend.dto.request.member.MemberDeleteRequestDto;
import day.dayBackend.dto.request.member.PasswordUpdateRequestDto;
import day.dayBackend.dto.request.member.EmailUpdateRequestDto;
import day.dayBackend.dto.request.member.MemberDirectCreateRequestDto;
import day.dayBackend.dto.request.member.MemberUpdateRequestDto;
import day.dayBackend.dto.response.*;
import day.dayBackend.dto.response.member.*;
import day.dayBackend.exception.NotAuthenticatedException;
import day.dayBackend.service.MemberService;
import day.dayBackend.service.SignInService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    @GetMapping("/email/check")
    public CommonResponseDto emailDuplicationCheckV1(@Valid @RequestParam(value = "email") @Email(message = "{validation.Pattern.email}") String email) {
        signInService.emailDuplicationCheck(email);
        return CommonResponseDto.builder().build();
    }

    /**
     * Username 중복체크
     */
    @GetMapping("/username/check")
    public CommonResponseDto usernameDuplicationCheckV1(@Valid @RequestParam(value = "username") @NotNull(message = "{validation.NotNull}") String username) {
        signInService.usernameDuplicationCheck(username);
        return CommonResponseDto.builder().build();
    }


    /**
     * 메인페이지 회원정보 보기
     */
    @GetMapping("")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public CommonResponseDto<MemberResponseDto> getMyInfoV1() {
        Long memberId = SecurityUtil.getCurrentUserPK().orElseThrow(() -> new NotAuthenticatedException("INVALID ID"));
        return CommonResponseDto.<MemberResponseDto>builder()
                .data(memberService.getMemberById(memberId))
                .build();
    }

    /**
     * 회원정보 상세 보기
     */
    @GetMapping("/detail")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public CommonResponseDto<MemberDetailResponseDto> getMyDetailInfoV1() {
        Long memberId = SecurityUtil.getCurrentUserPK().orElseThrow(() -> new NotAuthenticatedException("INVALID ID"));
        return CommonResponseDto.<MemberDetailResponseDto>builder()
                .data(memberService.getMemberDetailById(memberId))
                .build();
    }

    /**
     * 회원정보 수정
     */
    @PatchMapping("")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public CommonResponseDto<MemberUpdateResponseDto> updateMyInfoV1(@RequestPart(value = "memberInfo", required = false) final MemberUpdateRequestDto dto,
                                                                     @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
                                                                     @RequestPart(value = "backgroundImage", required = false) MultipartFile backgroundImage) throws IOException {
        Long memberId = SecurityUtil.getCurrentUserPK().orElseThrow(() -> new NotAuthenticatedException("INVALID ID"));
        return CommonResponseDto.<MemberUpdateResponseDto>builder()
                .data(memberService.updateMember(memberId, dto, profileImage, backgroundImage))
                .build();
    }

    /**
     * 이메일 수정
     */
    @PutMapping("/email")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public CommonResponseDto<EmailUpdateResponseDto> updateEmailV1(@Valid @RequestBody final EmailUpdateRequestDto dto) {
        Long memberId = SecurityUtil.getCurrentUserPK().orElseThrow(() -> new NotAuthenticatedException("INVALID ID"));
        return CommonResponseDto.<EmailUpdateResponseDto>builder()
                .data(memberService.updateEmail(memberId, dto))
                .build();
    }

    /**
     * 회원 PK조회
     */
    @GetMapping("/id")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public CommonResponseDto<Map<String, Long>> getMemberIdV1() {
        Long memberId = SecurityUtil.getCurrentUserPK().orElseThrow(() -> new NotAuthenticatedException("INVALID ID"));
        return CommonResponseDto.<Map<String, Long>>builder()
                .data(Map.of("memberId", memberId))
                .build();
    }

    /**
     * 이메일 조회
     */
    @GetMapping("/email")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public CommonResponseDto<EmailResponseDto> getEmailV1() {
        Long memberId = SecurityUtil.getCurrentUserPK().orElseThrow(() -> new NotAuthenticatedException("INVALID ID"));
        return CommonResponseDto.<EmailResponseDto>builder()
                .data(memberService.getEmail(memberId))
                .build();
    }

    /**
     * 비밀번호 수정
     */
    @PutMapping("/password")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public CommonResponseDto updatePasswordV1(@Valid @RequestBody final PasswordUpdateRequestDto dto) {
        Long memberId = SecurityUtil.getCurrentUserPK().orElseThrow(() -> new NotAuthenticatedException("INVALID ID"));
        memberService.updatePassword(memberId, dto);
        return CommonResponseDto.builder().build();
    }

    /**
     * 회원 탈퇴
     */
    @DeleteMapping("")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public CommonResponseDto resignV1(@RequestBody final MemberDeleteRequestDto dto) {
        Long memberId = SecurityUtil.getCurrentUserPK().orElseThrow(() -> new NotAuthenticatedException("INVALID ID"));
        memberService.resign(memberId, dto);
        return CommonResponseDto.builder().build();
    }

}
