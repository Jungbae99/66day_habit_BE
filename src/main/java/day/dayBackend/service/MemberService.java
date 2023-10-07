package day.dayBackend.service;

import day.dayBackend.domain.Member;
import day.dayBackend.dto.request.member.MemberDeleteRequestDto;
import day.dayBackend.dto.request.member.EmailUpdateRequestDto;
import day.dayBackend.dto.request.member.MemberUpdateRequestDto;
import day.dayBackend.dto.request.member.PasswordUpdateRequestDto;
import day.dayBackend.dto.response.EmailUpdateResponseDto;
import day.dayBackend.dto.response.MemberDetailResponseDto;
import day.dayBackend.dto.response.MemberResponseDto;
import day.dayBackend.dto.response.MemberUpdateResponseDto;
import day.dayBackend.exception.NotFoundException;
import day.dayBackend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final UploadService uploadService;

    /**
     * 메인페이지 회원정보 조회
     */
    public MemberResponseDto getMemberById(Long id) {
        return MemberResponseDto.fromEntity(
                memberRepository.findByIdAndDeletedAtNull(id).orElseThrow(
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
     * 회원 수정
     */
    @Transactional
    public MemberUpdateResponseDto updateMember(Long id, MemberUpdateRequestDto dto) {
        System.out.println("dto.getUsername() = " + dto.getUsername());
        System.out.println("dto.getIntroduction() = " + dto.getIntroduction());
//        System.out.println("dto.getProfileImage() = " + dto.getProfileImage());

        Member member = memberRepository.findByIdAndDeletedAtNull(id)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 회원을 찾을 수 없습니다"));
//
        if (dto.getUsername().isPresent()) {
            member.updateUsername(dto.getUsername().get());
        }
        if (dto.getIntroduction().isPresent()) {
            member.updateIntroduction(dto.getIntroduction().get());
        }
//        if (dto.getProfileImage().isPresent()) {
//            MultipartFile profileImage = dto.getProfileImage().get();
//            uploadService.uploadFile(id, profileImage);
//        }

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
        //:TODO Encoder 사용
        if (member.getPassword() != null && !member.getPassword().equals(dto.getCurrentPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
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
        // TODO: 권한삭제
        if (member.getPassword() != null && !member.getPassword().equals(dto.getCheckPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        member.delete();
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
