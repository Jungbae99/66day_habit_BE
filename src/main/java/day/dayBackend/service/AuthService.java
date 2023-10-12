package day.dayBackend.service;

import day.dayBackend.domain.Certified;
import day.dayBackend.domain.Member;
import day.dayBackend.dto.request.DirectSignInRequestDto;
import day.dayBackend.dto.request.SignInRequestDto;
import day.dayBackend.dto.response.SignInResponseDto;
import day.dayBackend.exception.NotFoundException;
import day.dayBackend.jwt.TokenDto;
import day.dayBackend.jwt.TokenProvider;
import day.dayBackend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final CustomPrincipalDetailService customPrincipalDetailService;
    private final MemberRepository memberRepository;

    /**
     * 로그인
     */
    @Transactional
    public SignInResponseDto signIn(DirectSignInRequestDto dto) {

        Authentication authentication = customAuthentication(dto);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Token set 발급
        TokenDto tokenSet = createTokenSetByAuthentication(authentication);

        // Member 영속화
        Member member = memberRepository.findByEmailAndDeletedAtNull(authentication.getName())
                .orElseThrow(NotFoundException::new);

        if (member.getCertified() == Certified.NOT_CERTIFIED) {
            throw new AccessDeniedException("UNCERTIFIED");
        }
        member.updateRefreshToken(tokenSet.getRefreshToken());

        return SignInResponseDto.builder()
                .email(member.getEmail())
                .username(member.getUsername())
                .token(tokenSet)
                .build();
    }

    /**
     * 토큰 리프레쉬
     */
    @Transactional
    public TokenDto tokenRefresh(String refreshToken) {
        if (!tokenProvider.validateToken(refreshToken, false)) {
            log.warn("Invalid refresh token expected");
            throw new AccessDeniedException("INVALID_TOKEN");
        }

        Authentication authentication = tokenProvider.getAuthentication(refreshToken, false);

        Member member = memberRepository.findByEmailAndDeletedAtNull(authentication.getName())
                .orElseThrow(NotFoundException::new);

        if (!refreshToken.equals(member.getRefreshToken())) {
            log.warn("Not matched refresh token");
            throw new AccessDeniedException("INVALID_TOKEN");
        }

        TokenDto tokenSet = createTokenSetByAuthentication(authentication);
        member.updateRefreshToken(tokenSet.getRefreshToken());
        return tokenSet;
    }


    /**
     * 로그인 요청 Dto 를 Authentication 객체로 변환
     */
    private <T extends SignInRequestDto> Authentication customAuthentication(T dto) {
        Authentication authentication = null;

        if (dto instanceof DirectSignInRequestDto directSignInRequestDto) {
            authentication = authenticationManagerBuilder.getObject().authenticate(
                    new UsernamePasswordAuthenticationToken(directSignInRequestDto.getEmail(), directSignInRequestDto.getPassword())
            );
        }
        return authentication;
    }

    /**
     * Authentication 객체를 받아 Token Set 으로 변환
     */
    private TokenDto createTokenSetByAuthentication(Authentication authentication) {
        return TokenDto.builder()
                .accessToken(tokenProvider.getAccessToken(authentication))
                .refreshToken(tokenProvider.getRefreshToken(authentication))
                .build();
    }
}
