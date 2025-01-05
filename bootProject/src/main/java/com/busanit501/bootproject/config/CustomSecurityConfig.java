package com.busanit501.bootproject.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Log4j2
@Configuration
@RequiredArgsConstructor
// 시큐리티 설정 on 추가
@EnableWebSecurity
// 권한별 설정 추가
// 이전 문법 ://@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableMethodSecurity()
public class CustomSecurityConfig {

    //순서1,
    // 인증, 인가 관련 구체적인 설정은 여기 메서드에서 작성
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("===========config=================");

        // 순서3, 로그인 방식을 폼 로그인으로 설정.
        // 옛날 문법,
        // http.formLogin();
        http.formLogin(
                formLogin ->
                        formLogin.loginPage("/member/login")
        );

        // 순서4
        //로그인 후, 성공시 리다이렉트 될 페이지 지정, 간단한 버전.
        http.formLogin(formLogin ->
                formLogin.defaultSuccessUrl("/home",true)
        );

        // 순서5
        // 기본은 csrf 설정이 on, 작업시에는 끄고 작업하기.
        // 만약, 사용한다면,
        // 웹 화면에서 -> 서버로,  csrf 토큰 생성해서 전송.
        // 레스트로 작업시에도 , csrf 토큰 생성해서 전송.
        http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());


        // 순서 6, 가장 중요함.
        // 시큐리티의 전체 허용 여부 관련 목록
        // 주의사항, 위에서 부터 차례대로 설정 적용이 됨.
        // 첫번째 줄에 너무 큰 범위로 막는 설정을 하고, 다음 줄에서 허용을해도
        // 허용이 안됩니다.
//        http.authorizeHttpRequests(
//                authorizeRequests -> {
//                    authorizeRequests.requestMatchers
//                            ("/css/**", "/js/**","/*").permitAll();
//                    authorizeRequests.requestMatchers
//                            ("/board/list","/board/register").authenticated();
//                    authorizeRequests.requestMatchers
//                            ("/admin/**","/board/update").hasRole("ADMIN");
//                    //위의 3가지 조건을 제외한 나머지 모든 접근은 인증이 되어야 접근이 가능함.
//                    authorizeRequests.anyRequest().authenticated();
//                }
//
//        );


        // 순서 8, 로그아웃 설정.
        // 로그 아웃 설정.
        // 작업 진행 순서,
        // 웹브라우저 -> http://localhost:8080/member/logout
        // 시큐리티가 동작을하고, 로그아웃 처리를 자동으로 하고,
        // 로그 아웃 성공시, 성공 후 이동할 페이지로 이동 시킴.
        // ?logout , 파라미터,
        // /member/login?logout
        // 멤버 컨트롤러,
        //
        http.logout(
                logout -> logout.logoutUrl("/member/logout")
                        .logoutSuccessUrl("/member/login")

        );


        return http.build();
    }


    // 순서2,
    // css, js, 등 정적 자원은 시큐리티 필터에서 제외하기
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        log.info("시큐리티 동작 확인 ====webSecurityCustomizer======================");
        return (web) ->
                web.ignoring()
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    //순서7, 패스워드 암호화를 해주는 도구, 스프링 설정.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



}
