package day.dayBackend.service;

import day.dayBackend.domain.Member;
import day.dayBackend.domain.Upload;
import day.dayBackend.domain.authority.MemberAuthority;
import day.dayBackend.dto.request.member.MemberDeleteRequestDto;
import day.dayBackend.dto.request.member.EmailUpdateRequestDto;
import day.dayBackend.dto.request.member.MemberUpdateRequestDto;
import day.dayBackend.dto.request.member.PasswordUpdateRequestDto;
import day.dayBackend.dto.response.member.*;
import day.dayBackend.exception.NotFoundException;
import day.dayBackend.repository.MemberAuthorityRepository;
import day.dayBackend.repository.MemberRepository;
import day.dayBackend.repository.UploadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final UploadService uploadService;
    private final MemberRepository memberRepository;
    private final UploadRepository uploadRepository;
    private final MemberAuthorityRepository memberAuthorityRepository;

    /**
     * 메인페이지 회원정보 조회
     */
    public MemberResponseDto getMemberById(Long memberId) {
        return MemberResponseDto.fromEntity(
                memberRepository.findByIdWithHabit(memberId).orElseThrow(
                        () -> new NotFoundException("id에 해당하는 회원을 찾을 수 없습니다.")));
    }

    /**
     * 회원 정보 상세 조회
     */
    public MemberDetailResponseDto getMemberDetailById(Long id) {
        return MemberDetailResponseDto.fromEntity(
                memberRepository.findByIdAndDeletedAtNull(id).orElseThrow(
                        () -> new NotFoundException("id에 해당하는 회원을 찾을 수 없습니다")));
    }

    /**
     * 회원 이메일 조회
     */
    public EmailResponseDto getEmail(Long id) {
        return EmailResponseDto.fromEntity(
                memberRepository.findByIdAndDeletedAtNull(id).orElseThrow(
                        () -> new NotFoundException("id에 해당하는 회원을 찾을 수 없습니다.")));
    }

    /**
     * 회원 수정
     */
    @Transactional
    public MemberUpdateResponseDto updateMember(Long memberId, MemberUpdateRequestDto dto, MultipartFile profileImage, MultipartFile backgroundImage) {

        Member member = memberRepository.findByIdAndDeletedAtNull(memberId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 회원을 찾을 수 없습니다"));

        if (dto != null) {
            if (dto.getUsername().isPresent()) {
                member.updateUsername(dto.getUsername().get());
            }
            if (dto.getIntroduction().isPresent()) {
                member.updateIntroduction(dto.getIntroduction().get());
            }
        }

        if (profileImage != null && !profileImage.isEmpty()) {
            Long profileImageId = uploadService.uploadFile(memberId, profileImage);
            Upload profile = uploadRepository.findByIdAndDeletedAtNull(profileImageId)
                    .orElseThrow(NotFoundException::new);
            member.updateProfileImage(profile);
        }

        if (backgroundImage != null && !backgroundImage.isEmpty()) {
            Long backgroundImageId = uploadService.uploadFile(memberId, backgroundImage);
            Upload background = uploadRepository.findByIdAndDeletedAtNull(backgroundImageId)
                    .orElseThrow(NotFoundException::new);
            member.updateBackgroundImage(background);
        }

        return MemberUpdateResponseDto.fromEntity(member);
    }

    /**
     * 이메일 수정
     */
    @Transactional
    public EmailUpdateResponseDto updateEmail(Long memberId, EmailUpdateRequestDto dto) {
        String newEmail = dto.getNewEmail();
        validateEmailDuplication(newEmail);

        Member member = memberRepository.findByIdAndDeletedAtNull(memberId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 회원을 찾을 수 없습니다."));

        member.updateEmail(newEmail);
        return EmailUpdateResponseDto.fromEntity(member);
    }


    /**
     * 비밀번호 수정
     */
    @Transactional
    public void updatePassword(Long memberId, PasswordUpdateRequestDto dto) {
        Member member = memberRepository.findByIdAndDeletedAtNull(memberId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 회원을 찾을 수 없습니다."));
        if (!passwordEncoder.matches(dto.getCurrentPassword(), member.getPassword())) {
            throw new AccessDeniedException("비밀번호가 일치하지 않습니다.");
        }
        member.updatePassword(dto.getNewPassword());
    }

    /**
     * 회원 탈퇴
     */
    @Transactional
    public Long resign(Long memberId, MemberDeleteRequestDto dto) {
        Member member = memberRepository.findByIdAndDeletedAtNull(memberId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 회원을 찾을 수 없습니다."));

        if (!passwordEncoder.matches(dto.getCheckPassword(), member.getPassword())) {
            throw new AccessDeniedException("비밀번호가 일치하지 않습니다.");
        }

        member.delete();
        memberAuthorityRepository.findByMemberId(member.getId()).forEach(MemberAuthority::delete);

        return member.getId();
    }

    /**
     * 중복체크 유틸
     */
    private void validateEmailDuplication(String email) throws DuplicateKeyException {
        memberRepository.findByEmailAndDeletedAtNull(email)
                .ifPresent(m -> {
                    throw new DuplicateKeyException("이미 같은 이메일이 존재합니다.");
                });
    }

}
