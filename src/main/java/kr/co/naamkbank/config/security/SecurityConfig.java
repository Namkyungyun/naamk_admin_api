package kr.co.naamkbank.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/**").permitAll()
//                        .requestMatchers("/h2-console/**").permitAll() // H2 Console 접근 허용
//                        .requestMatchers("/user/**").permitAll() // H2 Console 접근 허용
                        .anyRequest().authenticated() // 다른 요청은 인증 필요
                )
                .csrf(AbstractHttpConfigurer::disable) // H2 Console 사용 시 CSRF 비활성화
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)); // H2 Console의 iframe 사용 허용

        return http.build();
    }
}
