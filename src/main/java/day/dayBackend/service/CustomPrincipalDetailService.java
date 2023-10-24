package day.dayBackend.service;

import day.dayBackend.domain.Member;
import day.dayBackend.domain.authority.CustomUser;
import day.dayBackend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomPrincipalDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    /**
     * username(email) 을 이용해 Authentication 객체 조회
     * AuthenticationManagerBuilder 의 authenticate() 실행 시 호출된다.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return memberRepository.findByEmailAndDeletedAtNull(email)
                .map(this::createUser)
                .orElseThrow(() -> new UsernameNotFoundException("이메일이 \"" + email + "\"인 회원을 찾을 수 없습니다."));
    }

    private CustomUser createUser(Member member) {
        List<GrantedAuthority> grantedAuthorities = member.getMemberAuthorities()
                .stream().map(authority -> new SimpleGrantedAuthority(authority.toAuthority().getAuthorityName()))
                .collect(Collectors.toList());
        return new CustomUser(
                member.getEmail(),
                member.getPassword() != null ? member.getPassword() : "",
                grantedAuthorities,
                member.getId()
        );
    }
}
