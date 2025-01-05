package com.busanit501.bootproject.dto;

import lombok.*;

        import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long userId;
    private String email;
    private String password; // 비밀번호는 일반 텍스트로 저장
    private String name;
    private LocalDate birth;
    private String gender;
    private String address;
    private String profilePicture;
    private String phoneNumber;
    private float rating;
    private int ratingCount;

}

