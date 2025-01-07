package com.busanit501.bootproject.domain;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * 매칭방 참가자 엔티티의 복합 키를 정의하는 클래스
 */
@Data
@Embeddable
public class RoomParticipantId implements Serializable {

    /**
     * 매칭방 ID
     */
    private Long roomId;

    /**
     * 사용자 ID
     */
    private Long userId;

    /**
     * 반려동물 ID
     */
    private Long petId;

    /**
     * 기본 생성자
     */
    public RoomParticipantId() {}

    /**
     * 모든 필드를 초기화하는 생성자
     * @param roomId 매칭방 ID
     * @param userId 사용자 ID
     * @param petId 반려동물 ID
     */
    public RoomParticipantId(Long roomId, Long userId, Long petId) {
        this.roomId = roomId;
        this.userId = userId;
        this.petId = petId;
    }

    /**
     * equals 메서드 오버라이드 (ID 비교)
     * @param o 비교할 객체
     * @return 두 객체가 동일한 ID를 가지는지 여부
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomParticipantId that = (RoomParticipantId) o;
        return Objects.equals(roomId, that.roomId) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(petId, that.petId);
    }

    /**
     * hashCode 메서드 오버라이드 (ID 기반 해시 코드 생성)
     * @return 해시 코드
     */
    @Override
    public int hashCode() {
        return Objects.hash(roomId, userId, petId);
    }
}
