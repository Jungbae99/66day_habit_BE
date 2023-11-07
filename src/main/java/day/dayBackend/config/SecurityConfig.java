package day.dayBackend.config;

import day.dayBackend.jwt.JwtAccessDeniedHandler;
import day.dayBackend.jwt.JwtAuthenticationEntryPoint;
import day.dayBackend.jwt.JwtSecurityConfig;
import day.dayBackend.jwt.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final TokenProvider tokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    public SecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtAccessDeniedHandler jwtAccessDeniedHandler, TokenProvider tokenProvider, RedisTemplate<String, String> redisTemplate) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
        this.tokenProvider = tokenProvider;
        this.redisTemplate = redisTemplate;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .headers()
                .frameOptions()
                .sameOrigin()

                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // h2 console TODO: test 환경에서만
                .and()
                .authorizeHttpRequests()

                .requestMatchers(HttpMethod.GET, "/favicon.ico").permitAll()
                .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console")).permitAll()
                .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()

                // 로그인 및 토큰 refresh
                .requestMatchers(HttpMethod.POST, "/v1/auth/signIn/direct").permitAll()
                .requestMatchers(HttpMethod.POST, "/v1/auth/refresh").permitAll()

                // 회원가입
                .requestMatchers(HttpMethod.POST, "/v1/member/direct").permitAll()

                // 이메일 중복확인
                .requestMatchers(HttpMethod.GET, "/v1/member/email/**").permitAll()

                // 습관 조회
                .requestMatchers(HttpMethod.GET, "/v1/habit/**").permitAll()
                
                // 랜덤 조회
                .requestMatchers(HttpMethod.GET, "/v1/recommend/random/**").permitAll()
                .anyRequest().authenticated()

                .and()
                .apply(new JwtSecurityConfig(tokenProvider, redisTemplate));

        return http.build();
    }
}
