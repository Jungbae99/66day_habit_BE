package day.dayBackend.service;


import day.dayBackend.domain.Member;
import day.dayBackend.domain.Upload;
import day.dayBackend.domain.authority.MemberAuthority;
import day.dayBackend.dto.request.member.MemberDirectCreateRequestDto;
import day.dayBackend.exception.NotFoundException;
import day.dayBackend.repository.AuthorityRepository;
import day.dayBackend.repository.MemberAuthorityRepository;
import day.dayBackend.repository.MemberRepository;
import day.dayBackend.repository.UploadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SignInService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberAuthorityRepository memberAuthorityRepository;
    private final AuthorityRepository authorityRepository;
    private final UploadRepository uploadRepository;

    /**
     * 회원가입
     */
    public Long directJoin(MemberDirectCreateRequestDto dto) {
        validateEmailDuplication(dto.getEmail());
        usernameDuplicationCheck(dto.getUsername());

        String encoded = passwordEncoder.encode(dto.getPassword());
        Upload profileImage = uploadRepository.findByIdAndDeletedAtNull(1L).orElseThrow(NotFoundException::new);
        Upload backgroundImage = uploadRepository.findByIdAndDeletedAtNull(1L).orElseThrow(NotFoundException::new);

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

        memberAuthorityRepository.save(memberAuthority);

        memberRepository.save(member);
        return member.getId();
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
