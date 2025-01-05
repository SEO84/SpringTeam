package com.busanit501.bootproject.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User extends BaseEntity{
    @Id
    @Column(length = 50, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId; // 사용자 Number

    @Column(length = 100, nullable = false)
    private String email; // 사용자 이메일

    @Column(length = 50, nullable = false)
    private String password; // 암호화된 비밀번호

    @Column(length = 50, nullable = false)
    private String name; // 사용자 이름

    @Column(length = 10, nullable = false)
    private LocalDate birth; // 사용자 나이 (null 가능)

    @Column(nullable = false)
    private String gender; // 성별

    @Column(length = 200, nullable = false)
    private String address; // 주소

    @Column(nullable = true) // NULL 허용
    private String profilePicture; // 프로필 사진 URL

    @Column(length = 100, nullable = false)
    private String phoneNumber; // 전화번호

    @Column(nullable = false, columnDefinition = "FLOAT DEFAULT 0")
    private float rating; // 유저의 평균 평점

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private int ratingCount; // 리뷰 수
}
