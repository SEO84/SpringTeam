package com.busanit501.bootproject.service;

import com.busanit501.bootproject.domain.MatchingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchingRoomRepository extends JpaRepository<MatchingRoom, Long> {
    // 추가적인 쿼리 메서드가 필요하면 여기에 정의
}
