package com.busanit501.bootproject.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pet")
public class Pet extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long petId; // 반려동물 ID

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 반려동물의 주인 (User)

    @Column(length = 100, nullable = false)
    private String name; // 반려동물 이름

    @Column(length = 50, nullable = false)
    private String type; // 반려동물 종류

    @Column(nullable = false)
    private LocalDate birth; // 생년월일

    @Enumerated(EnumType.STRING)
    private Gender gender; // 반려동물 성별

    @Column(columnDefinition = "TEXT")
    private String personality; // 성격

    @Column(nullable = false)
    private float weight; // 몸무게

    @Column(length = 255, nullable = true)
    private String profilePicture; // 프로필 사진 URL

    @Column(nullable = false)
    private boolean isVerified; // 인증 여부

}