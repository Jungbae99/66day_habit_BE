package day.dayBackend.service;


import day.dayBackend.domain.Member;
import day.dayBackend.dto.request.MemberDirectCreateRequestDto;
import day.dayBackend.repository.MemberRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Transactional
public class SignInService {

    private final MemberRepository memberRepository;

    public Long directJoin(MemberDirectCreateRequestDto dto) {
        validateEmailDuplication(dto.getEmail());
        //:TODO password encoding
        Member member = Member.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .introduction(dto.getIntroduction())
                .username(dto.getUsername())
                .build();

        memberRepository.save(member);
        return member.getId();
    }




    /**
     * 중복체크 유틸
     */
    private void validateEmailDuplication(String email) throws DuplicateKeyException {
        memberRepository.findByEmailAndDeletedAtNull(email)
                .ifPresent(member -> {
                    throw new DuplicateKeyException("이미 같은 이메일이 존재합니다.");
                });
    }

    public void emailDuplicationCheck(String email) {
        validateEmailDuplication(email);
    }
}
