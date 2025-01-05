package com.busanit501.bootproject.security;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class CustomUserDetailsService implements UserDetailsService {

    //시스템에 등록된, 암호화 해주는 도구 가져오기.
    private PasswordEncoder passwordEncoder;

    // 시큐리티에서, 로그인 작업 처리시, 동작하는 메서드 여기
    // 시큐리티에서, 타입을 UserDetails로 반환해야 확인 가능.
    // 결론, 로그인 했을 경우, 입력한 , username, password 값을
    // 여기 메서드로 가지고 온다. 참고, 가지고 오는 키는 고정,
    // 예시) username, password , 키가 고정, 주의사항,
    // 화면에 input 태그에서, name 이름 작성시 주의하기.

    public CustomUserDetailsService() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("로그인한 유저 확인 : "+ username);
        // 데이터베이스에 저장된 유저와 비교 작업 후, 처리 예정.
        // 테스트, 더미 데이터 작업,
        // 반환 타입 : UserDetails
        // User : 스프링 시큐리티에서 제공하는 클래스, 주의하기, 이름. 고정.

        UserDetails userDetails = User.builder()
                .username("111@test.com")
                // 서버에서는 평문으로 패스워드 넘어오면,
                // 기본 다 거부함. 기본 해쉬한 값으로 와야함.
//                .password("1234")
                .password(passwordEncoder.encode("111"))
                // 인증된 유저,
                // 관리자, ROLE_ADMIN
//                .authorities("ROLE_USER")
                .authorities("ROLE_USER", "ROLE_ADMIN")
                .build();

        return userDetails;
    }
}
