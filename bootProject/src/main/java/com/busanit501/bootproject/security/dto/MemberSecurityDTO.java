package com.busanit501.bootproject.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Map;

// 여기 DTO, 리턴 타입을 UserDetails 반환 해야함.
// Security, 로그인 처리 후, 반환 타입을 약속함.
// Security 에서 제공하는 User 상속 받으면, 가장 간단하게 구현 가능함.

@Getter
@Setter
@ToString
// MemberSecurityDTO , 클래스는
// 다형성으로, 반환 타입, UserDetails ,되고,
// 반환 타입 , OAuth2User 됨.
public class MemberSecurityDTO extends User{

    private String mid;
    private String mpw;
    private String email;
    private boolean del;
    private boolean social;

    // 소셜 로그인 정보를 담을 요소
    private Map<String, Object> props;

    public MemberSecurityDTO(String username, String password, String email,
                             boolean del, boolean social,
                             Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.mid = username;
        this.mpw = password;
        this.email = email;
        this.del = del;
        this.social = social;

    }

}

