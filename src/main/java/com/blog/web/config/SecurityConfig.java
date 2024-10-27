package com.blog.web.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        try {
            http.csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(
                            //1. 접근 가능 페이지 설정 //
                            auth -> auth.requestMatchers(
                                            "/css/**",
                                            "/*.ico",
                                            "/",
                                            "/users/new",
                                            "/users/login"
                                    )
                                    .permitAll()
                                    //2. 그 외 페이지는 인증 필요 //
                                    .anyRequest()
                                    .authenticated()
                    )
//                    .formLogin(
//                            //3. 로그인 페이지 경로 지정 //
//                            form -> form.loginPage("/users/login")
//                                    .defaultSuccessUrl("/", true)
//                                    //4. 로그인 페이지는 누구나 접근 가능 //
//                                    .permitAll()
//                    )
                    .logout(
                            //5. 로그아웃 페이지 누구나 접근 가능
                            logout -> logout
                                    .logoutSuccessUrl("/")
                                    .permitAll()
                    );
            return http.build();

        } catch (Exception e) {
            log.info("securityFilterChain 예외 발생={}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /* CSRF 비활성화 */
    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        return CookieCsrfTokenRepository.withHttpOnlyFalse();
    }

}
