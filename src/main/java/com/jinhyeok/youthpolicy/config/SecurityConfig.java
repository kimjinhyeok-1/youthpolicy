package com.jinhyeok.youthpolicy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // 스프링에게 "이건 설정 파일이야!" 라고 알려줍니다.
@EnableWebSecurity // 스프링 시큐리티 기능을 활성화합니다.
public class SecurityConfig {

    // ✅ 1. BCrypt 암호화 기계를 스프링 컨테이너에 등록 (Bean)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ✅ 2. Security 기본 설정 덮어쓰기
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 방어 기능 끄기 (프론트 fetch API 테스트를 위해 일단 끕니다)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // ⭐️ "지금은 모든 API 요청 그냥 다 통과시켜줘!" 라는 뜻입니다.
                );
        return http.build();
    }
}