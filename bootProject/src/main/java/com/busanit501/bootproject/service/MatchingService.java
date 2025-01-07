package com.busanit501.bootproject.service;

import com.busanit501.bootproject.domain.MatchingRoom;
import com.busanit501.bootproject.domain.Pet;
import com.busanit501.bootproject.domain.RoomParticipant;
import com.busanit501.bootproject.domain.User;
import com.busanit501.bootproject.dto.MatchingRoomDTO;
import com.busanit501.bootproject.exception.ResourceNotFoundException;
import com.busanit501.bootproject.repository.MatchingRoomRepository;
import com.busanit501.bootproject.repository.PetRepository;
import com.busanit501.bootproject.repository.RoomParticipantRepository;
import com.busanit501.bootproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MatchingService {

    private final MatchingRoomRepository roomRepository;
    private final RoomParticipantRepository participantRepository;
    private final PetRepository petRepository;
    private final UserRepository userRepository;

    @Autowired
    public MatchingService(MatchingRoomRepository roomRepository,
                           RoomParticipantRepository participantRepository,
                           PetRepository petRepository,
                           UserRepository userRepository) {
        this.roomRepository = roomRepository;
        this.participantRepository = participantRepository;
        this.petRepository = petRepository;
        this.userRepository = userRepository;
    }

    public List<MatchingRoom> getAllRooms() {
        return roomRepository.findAll();
    }

    public MatchingRoom getRoomById(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("매칭방을 찾을 수 없습니다. ID: " + roomId));
    }

    @Transactional
    public void createRoom(MatchingRoomDTO dto, User hostUser) {
        // 새 매칭방 생성
        MatchingRoom room = new MatchingRoom();
        room.setHost(hostUser);
        room.setTitle(dto.getTitle());
        room.setDescription(dto.getDescription());
        room.setPlace(dto.getPlace());
        room.setMeetingDate(dto.getMeetingDate());
        room.setMeetingTime(dto.getMeetingTime());
        room.setMaxParticipants(dto.getMaxParticipants());
        room.setUser(hostUser); // hostUser를 사용자 필드에도 넣고 있음

        MatchingRoom savedRoom = roomRepository.save(room);

        // 호스트 펫들 등록
        List<Pet> pets = petRepository.findAllById(dto.getPetIds());
        for (Pet pet : pets) {
            RoomParticipant participant = new RoomParticipant();
            participant.setMatchingRoom(savedRoom);
            participant.setUser(hostUser);
            participant.setPet(pet);
            participant.setStatus(RoomParticipant.ParticipantStatus.Accepted);
            participantRepository.save(participant);
        }
    }

    @Transactional
    public void updateRoom(Long roomId, MatchingRoomDTO dto, User hostUser) {
        MatchingRoom room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("매칭방을 찾을 수 없습니다."));

        if (!room.getHost().getUserId().equals(hostUser.getUserId())) {
            throw new RuntimeException("방장만 수정할 수 있습니다.");
        }

        // 기본 정보 갱신
        room.setTitle(dto.getTitle());
        room.setDescription(dto.getDescription());
        room.setPlace(dto.getPlace());
        room.setMeetingDate(dto.getMeetingDate());
        room.setMeetingTime(dto.getMeetingTime());
        room.setMaxParticipants(dto.getMaxParticipants());

        // 호스트의 펫 참가 정보 다시 세팅(기존 호스트 펫 정보는 모두 삭제 후 새로 등록)
        List<RoomParticipant> existingParticipants = participantRepository.findAllByMatchingRoomAndUser(room, hostUser);
        participantRepository.deleteAll(existingParticipants);

        List<Pet> pets = petRepository.findAllById(dto.getPetIds());
        for (Pet pet : pets) {
            RoomParticipant participant = new RoomParticipant();
            participant.setMatchingRoom(room);
            participant.setUser(hostUser);
            participant.setPet(pet);
            participant.setStatus(RoomParticipant.ParticipantStatus.Accepted);
            participantRepository.save(participant);
        }
        // 수정된 room은 트랜잭션 종료 시점에 자동으로 DB 반영
    }

    // 참가 신청
    @Transactional
    public void applyRoom(Long roomId, Long userId, List<Long> petIds) {
        MatchingRoom room = getRoomById(roomId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));

        // 이미 신청했는지 확인
        List<RoomParticipant> existingParticipants = participantRepository.findAllByMatchingRoomAndUser(room, user);
        if (!existingParticipants.isEmpty()) {
            throw new RuntimeException("이미 참가 신청을 했습니다.");
        }

        // 펫 유효성 체크
        List<Pet> pets = petRepository.findAllById(petIds);
        if (pets.size() != petIds.size()) {
            throw new ResourceNotFoundException("일부 펫을 찾을 수 없습니다.");
        }

        // 최대인원 확인
        long acceptedParticipants =
                participantRepository.countDistinctUserByMatchingRoomAndStatus(room, RoomParticipant.ParticipantStatus.Accepted);
        if (acceptedParticipants + 1 > room.getMaxParticipants()) {
            throw new RuntimeException("참가 인원이 초과되었습니다.");
        }

        // Pending 상태로 참가 신청
        for (Pet pet : pets) {
            RoomParticipant participant = new RoomParticipant();
            participant.setMatchingRoom(room);
            participant.setUser(user);
            participant.setPet(pet);
            participant.setStatus(RoomParticipant.ParticipantStatus.Pending);
            participantRepository.save(participant);
        }
    }

    // 참가자 승인
    @Transactional
    public void acceptParticipant(Long roomId, Long userId) {
        MatchingRoom room = getRoomById(roomId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));

        List<RoomParticipant> participants = participantRepository.findAllByMatchingRoomAndUser(room, user);
        if (participants.isEmpty()) {
            throw new ResourceNotFoundException("참가 신청을 찾을 수 없습니다.");
        }

        long acceptedParticipants =
                participantRepository.countDistinctUserByMatchingRoomAndStatus(room, RoomParticipant.ParticipantStatus.Accepted);
        if (acceptedParticipants + 1 > room.getMaxParticipants()) {
            throw new RuntimeException("최대 참가 인원을 초과하여 승인할 수 없습니다.");
        }

        for (RoomParticipant participant : participants) {
            if (participant.getStatus() == RoomParticipant.ParticipantStatus.Accepted) {
                throw new RuntimeException("이미 승인된 참가 신청입니다.");
            }
            participant.setStatus(RoomParticipant.ParticipantStatus.Accepted);
            participantRepository.save(participant);
        }
    }

    // 참가자 거절
    @Transactional
    public void rejectParticipant(Long roomId, Long userId) {
        MatchingRoom room = getRoomById(roomId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));

        List<RoomParticipant> participants = participantRepository.findAllByMatchingRoomAndUser(room, user);
        if (participants.isEmpty()) {
            throw new ResourceNotFoundException("참가 신청을 찾을 수 없습니다.");
        }

        for (RoomParticipant participant : participants) {
            if (participant.getStatus() == RoomParticipant.ParticipantStatus.Rejected) {
                throw new RuntimeException("이미 거절된 참가 신청입니다.");
            }
            participant.setStatus(RoomParticipant.ParticipantStatus.Rejected);
            participantRepository.save(participant);
        }
    }

    public List<RoomParticipant> getParticipantsByRoomId(Long roomId) {
        MatchingRoom room = getRoomById(roomId);
        return participantRepository.findAllByMatchingRoom_RoomId(roomId);
    }

    public List<RoomParticipant> filterParticipants(List<RoomParticipant> participants,
                                                    RoomParticipant.ParticipantStatus status) {
        return participants.stream()
                .filter(p -> p.getStatus() == status)
                .collect(Collectors.toList());
    }

    /**
     * MatchingRoom -> MatchingRoomDTO 변환
     */
    public MatchingRoomDTO convertToDto(MatchingRoom room) {
        MatchingRoomDTO dto = new MatchingRoomDTO();
        // roomId는 Controller에서 추가 세팅하거나 여기서도 가능
        // dto.setRoomId(room.getRoomId());
        dto.setTitle(room.getTitle());
        dto.setDescription(room.getDescription());
        dto.setPlace(room.getPlace());
        dto.setMeetingDate(room.getMeetingDate());
        dto.setMeetingTime(room.getMeetingTime());
        dto.setMaxParticipants(room.getMaxParticipants());
        dto.setPetIds(room.getParticipants().stream()
                // 호스트가 등록한 펫만 추려내기
                .filter(p -> p.getUser().getUserId().equals(room.getHost().getUserId()))
                .map(p -> p.getPet().getPetId())
                .collect(Collectors.toList()));
        return dto;
    }
}
