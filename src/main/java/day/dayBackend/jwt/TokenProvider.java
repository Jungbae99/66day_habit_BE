package day.dayBackend.jwt;

import day.dayBackend.domain.authority.CustomUser;
import day.dayBackend.service.CustomPrincipalDetailService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class TokenProvider implements InitializingBean {

    private static final String AUTH_CLAIM_NAME = "auth";

    private final long accessTokenValidityTime;
    private final long refreshTokenValidityTime;

    private final String accessSecret;
    private final String refreshSecret;
    private Key accessKey;
    private Key refreshKey;

    private final CustomPrincipalDetailService customPrincipalDetailService;


    public TokenProvider(
            @Value("${jwt.access-token-validity-time}") long accessTokenValidityTime,
            @Value("${jwt.refresh-token-validity-time}") long refreshTokenValidityTime,
            @Value("${jwt.access-secret}") String accessSecret,
            @Value("${jwt.refresh-secret}") String refreshSecret,
            @Autowired CustomPrincipalDetailService customPrincipalDetailService
    ) {
        this.accessTokenValidityTime = accessTokenValidityTime;
        this.refreshTokenValidityTime = refreshTokenValidityTime;
        this.accessSecret = accessSecret;
        this.refreshSecret = refreshSecret;
        this.customPrincipalDetailService = customPrincipalDetailService;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        this.accessKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessSecret));
        this.refreshKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(refreshSecret));
    }

    /**
     * authentication 객체로 엑세스 토큰 생성
     */
    public String getAccessToken(Authentication authentication) {
        return createToken(authentication, accessTokenValidityTime, accessKey, true);
    }

    /**
     * authentication 객체로 리프레시 토큰 생성
     */
    public String getRefreshToken(Authentication authentication) {
        return createToken(authentication, refreshTokenValidityTime, refreshKey, false);
    }

    /**
     * 엑세스 | 리프레시 토큰으로부터 authentication 객체 생성
     */
    public Authentication getAuthentication(String token, boolean isAccessToken) {
        Key key = isAccessToken ? accessKey : refreshKey;
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        UserDetails principal;

        Collection<? extends GrantedAuthority> authorities;
        // 토큰에 클레임이 존재할 경우, DB 접근 없이 클레임에서 정보를 찾음
        if (claims.containsKey(AUTH_CLAIM_NAME) && claims.containsKey("memberId")) {
            if (claims.get(AUTH_CLAIM_NAME).toString().isBlank()) {
                throw new AccessDeniedException("UNCERTIFIED");
            }

            authorities = Arrays.stream(claims.get(AUTH_CLAIM_NAME).toString().split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            principal = new CustomUser(claims.getSubject(), "", authorities, Long.valueOf(claims.get("memberId").toString()));
            // 토큰에 클래임이 존재하지 않으면 DB 접근 후 정보를 찾음
        } else {
            principal = customPrincipalDetailService.loadUserByUsername(claims.getSubject());
            authorities = principal.getAuthorities();
        }
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    /**
     * 토큰 검증
     */
    public boolean validateToken(String token, boolean isAccessToken) {
        try {
            if (isAccessToken) {
                Jwts.parserBuilder().setSigningKey(accessKey).build().parseClaimsJws(token);
            } else {
                Jwts.parserBuilder().setSigningKey(refreshKey).build().parseClaimsJws(token);
            }
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            // 잘못된 jwt 형식
        } catch (ExpiredJwtException e) {
            // 만료된 jwt
        } catch (UnsupportedJwtException e) {
            // 지원하지 않는 jwt
        } catch (IllegalArgumentException e) {
            // 잘못된 토큰
        }
        return false;
    }

    /**
     * socket 연결을 위한 jwt 검증
     */
    public void validateTokenForStomp(final String token) {
        try {
            Jwts.parserBuilder().setSigningKey(accessKey).build().parseClaimsJws(token);
        } catch (SecurityException | MalformedJwtException e) {
            throw new MalformedJwtException("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            throw new MalformedJwtException("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            throw new MalformedJwtException("지원 되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            throw new MalformedJwtException("JWT 토큰이 잘못 되었습니다.");
        }
    }


    /**
     * 쿠키 생성
     */
    private ResponseCookie generateCookie(String name, String value, String path, boolean secure, String sameSite, long refreshTokenValidityTime) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .path(path)
                .maxAge(refreshTokenValidityTime)
                .httpOnly(true)
                .secure(secure)
                .sameSite(sameSite)
                .build();
        return cookie;
    }

    /**
     * refreshToken 담을 쿠키 생성
     */
    public ResponseCookie getRefreshTokenCookie(Authentication authentication) {
        String refreshToken = getRefreshToken(authentication);
        return generateCookie("jwtCookie", refreshToken, "/", true, "Lax", refreshTokenValidityTime);
    }

    public ResponseCookie setRefreshTokenCookie(String refreshToken) {
        return generateCookie("jwtCookie", refreshToken, "/", true, "Lax", 0);
    }


    /**
     * 토큰 생성
     */
    private String createToken(Authentication authentication, long tokenValidityTime, Key key, boolean claimIncluded) {
        String authorities = authentication.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity = new Date(now + tokenValidityTime);

        JwtBuilder builder = Jwts.builder()
                .setSubject(authentication.getName());

        if (claimIncluded) {
            Long userPK = null;
            if (authentication instanceof UsernamePasswordAuthenticationToken) {
                userPK = ((CustomUser) authentication.getPrincipal()).getUserPK();
            }
            builder.claim(AUTH_CLAIM_NAME, authorities)
                    .claim("memberId", userPK);
        }
        return builder.signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    /**
     * 토큰 유효시간 구하기
     */
    public Long getExpiration(String token, boolean isAccessToken) {
        return Jwts.parserBuilder()
                .setSigningKey(isAccessToken ? accessKey : refreshKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration().getTime();
    }

    /**
     * 토큰 멤버 아이디 구하기
     */
    public Long getMemberId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(accessKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        if (!claims.containsKey("memberId")) {
            throw new AccessDeniedException("INVALID_TOKEN");
        }
        return claims.get("memberId", Long.class);
    }

    public String extractJwt(final StompHeaderAccessor accessor) {
        return accessor.getFirstNativeHeader("Authorization");
    }
}
