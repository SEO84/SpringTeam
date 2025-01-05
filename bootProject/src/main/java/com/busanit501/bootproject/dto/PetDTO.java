package com.busanit501.bootproject.dto;

import com.busanit501.bootproject.domain.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PetDTO {

    private Long petId; // 반려동물 ID
    private Long userId; // 사용자 ID (반려동물의 주인)
    private String name; // 반려동물 이름
    private String type; // 반려동물 종류 (예: 개, 고양이)
    private int age; // 반려동물 나이
    private Gender gender; // 반려동물 성별
    private String personality; // 반려동물 성격
    private Float weight; // 반려동물 몸무게
    private String profilePicture; // 반려동물 프로필 사진 URL
    private LocalDateTime createdAt; // 등록 시간
    private LocalDateTime updatedAt; // 수정 시간
}
