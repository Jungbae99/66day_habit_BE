package day.dayBackend.service;


import day.dayBackend.domain.Certified;
import day.dayBackend.domain.EmailCertification;
import day.dayBackend.domain.Member;
import day.dayBackend.domain.Upload;
import day.dayBackend.domain.authority.MemberAuthority;
import day.dayBackend.dto.request.member.MemberDirectCreateRequestDto;
import day.dayBackend.exception.NotFoundException;
import day.dayBackend.repository.*;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
@Transactional
public class SignInService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberAuthorityRepository memberAuthorityRepository;
    private final AuthorityRepository authorityRepository;
    private final UploadRepository uploadRepository;
    private final EmailService emailService;
    private final EmailCertificationRepository emailCertificationRepository;

    /**
     * 회원가입
     */
    public Long directJoin(MemberDirectCreateRequestDto dto) {
        validateEmailDuplication(dto.getEmail());
        usernameDuplicationCheck(dto.getUsername());

        String encoded = passwordEncoder.encode(dto.getPassword());
        Upload profileImage = uploadRepository.findByIdAndDeletedAtNull(1L).orElseThrow(NotFoundException::new);
        Upload backgroundImage = uploadRepository.findByIdAndDeletedAtNull(2L).orElseThrow(NotFoundException::new);

        Member member = Member.builder()
                .email(dto.getEmail())
                .password(encoded)
                .introduction(dto.getIntroduction())
                .profileImage(profileImage)
                .backgroundImage(backgroundImage)
                .username(dto.getUsername())
                .build();

        MemberAuthority memberAuthority = MemberAuthority.createMemberAuthority(
                member, authorityRepository.findByAuthorityName("ROLE_USER").orElseThrow(NotFoundException::new));

        emailCertificationRepository.findByEmailAndCertifiedAndDeletedAtNull(dto.getEmail(), Certified.CERTIFIED)
                .orElseThrow(() -> new IllegalArgumentException("인증되지 않은 이메일입니다."));

        memberAuthorityRepository.save(memberAuthority);
        memberRepository.save(member);
        return member.getId();
    }

    /**
     * 메일 발송
     */
    public void sendCertificationEmail(String email) {
        // 메일 발송
        try {
            EmailCertification emailCertification = EmailCertification.createEmailCertification(email);
            emailCertificationRepository.save(emailCertification);
            emailService.sendEmail(email, emailCertification.getCertCode());

        } catch (MessagingException e) {
            // TODO: 에러 처리 로직 추가
            throw new RuntimeException("메일 전송에 실패했습니다", e);
        }
    }

    /**
     * 메일 인증 CertCode 비교
     */
    public void verifyCertificationEmail(String certCode, String email) {
        EmailCertification latestCertification =
                emailCertificationRepository.findByCertCodeAndEmail(certCode, email)
                        .orElseThrow(() -> new NotFoundException("인증 정보가 일치하지 않습니다."));

        if (latestCertification.isExpired()) {
            throw new IllegalArgumentException("인증코드가 만료되었습니다.");
        }

        latestCertification.updateCertified();
        latestCertification.delete();
    }

    /**
     * 중복체크 유틸
     */
    private void validateUsernameDuplication(String username) throws DuplicateKeyException {
        memberRepository.findByUsernameAndDeletedAtNull(username)
                .ifPresent(member -> {
                    throw new DuplicateKeyException("이미 같은 이름이 존재합니다.");
                });
    }

    private void validateEmailDuplication(String email) throws DuplicateKeyException {
        memberRepository.findByEmailAndDeletedAtNull(email)
                .ifPresent(member -> {
                    throw new DuplicateKeyException("이미 같은 이메일이 존재합니다.");
                });
    }

    public void usernameDuplicationCheck(String username) {
        validateUsernameDuplication(username);
    }

    public void emailDuplicationCheck(String email) {
        validateEmailDuplication(email);
    }
}
