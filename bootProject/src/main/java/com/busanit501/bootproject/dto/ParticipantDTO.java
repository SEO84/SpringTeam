package com.busanit501.bootproject.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 매칭방 참가자 정보를 나타내는 DTO 클래스
 */
@Data
public class ParticipantDTO {

    /**
     * 참가자 이름 (필수 입력 항목)
     */
    @NotBlank(message = "참가자 이름은 필수 입력 항목입니다.")
    private String userName;

    /**
     * 참가자 프로필 사진 경로 (옵션 필드)
     */
    private String profilePicturePath;
}
