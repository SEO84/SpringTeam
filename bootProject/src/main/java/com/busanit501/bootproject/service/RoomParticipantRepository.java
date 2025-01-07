package com.busanit501.bootproject.service;

import com.busanit501.bootproject.domain.MatchingRoom;
import com.busanit501.bootproject.domain.RoomParticipant;
import com.busanit501.bootproject.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomParticipantRepository extends JpaRepository<RoomParticipant, Long> {

    /**
     * 특정 매칭방과 사용자에 대한 참가자 목록을 조회합니다.
     *
     * @param matchingRoom 매칭방 객체
     * @param user         사용자 객체
     * @return 참가자 목록
     */
    List<RoomParticipant> findAllByMatchingRoomAndUser(MatchingRoom matchingRoom, User user);

    /**
     * 특정 매칭방에 대한 참가자 수를 세어 반환합니다.
     *
     * @param matchingRoom 매칭방 객체
     * @return 참가자 수
     */
    long countByMatchingRoom(MatchingRoom matchingRoom);

    /**
     * 특정 매칭방 ID로 모든 참가자 목록을 조회합니다.
     *
     * @param roomId 매칭방 ID
     * @return 참가자 목록
     */
    List<RoomParticipant> findAllByMatchingRoom_RoomId(Long roomId);

    long countDistinctUserByMatchingRoomAndStatus(MatchingRoom room, RoomParticipant.ParticipantStatus status);

}
