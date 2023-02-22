package com.example.replyblog.config;

import com.example.replyblog.jwt.JwtAuthFilter;
import com.example.replyblog.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity // 스프링 Security 지원을 가능하게 함
@EnableGlobalMethodSecurity(securedEnabled = true) // @Secured 어노테이션 활성화
public class WebSecurityConfig {
    private final JwtUtil jwtUtil;
    //
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // h2-console 사용 및 resources 접근 허용 설정
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toH2Console())
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF 설정
        http.csrf().disable();
        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        //http.authorizeRequests().anyRequest().authenticated();
        http.authorizeRequests().antMatchers("/api/user/**").permitAll()
                .antMatchers("/api/blog").permitAll()
                .antMatchers("/api/reply").permitAll()
                .anyRequest().authenticated()
                // JWT 인증/인가를 사용하기 위한 설정
                .and().addFilterBefore(new JwtAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class); //    private final JwtUtil jwtUtil; 추가하기!

        // 로그인 사용
        http.formLogin().permitAll();// 로그인 페이지가 있을 경우 넣기!.loginPage(".api/user/login-page").permitAll();
        // 로그인 실패
        http.exceptionHandling().accessDeniedPage("/api/user/forbidden");
        return http.build();
    }

}
/*
- **CSRF(사이트 간 요청 위조, Cross-site request forgery)**
    - 공격자가 인증된 브라우저에 저장된 쿠키의 세션 정보를 활용하여 웹 서버에 사용자가 의도하지 않은 요청을 전달하는 것
    - CSRF 설정이 되어있는 경우 html 에서 CSRF 토큰 값을 넘겨주어야 요청을 수신 가능
    - 쿠키 기반의 취약점을 이용한 공격 이기 때문에 REST 방식의 API에서는 disable 가능
    - POST 요청마다 처리해 주는 대신 **CSRF protection 을 disable**
 */