package com.busanit501.bootproject.controller;

import com.busanit501.bootproject.domain.MatchingRoom;
import com.busanit501.bootproject.domain.Pet;
import com.busanit501.bootproject.domain.RoomParticipant;
import com.busanit501.bootproject.domain.User;
import com.busanit501.bootproject.dto.MatchingRoomDTO;
import com.busanit501.bootproject.exception.ResourceNotFoundException;
import com.busanit501.bootproject.repository.UserRepository;
import com.busanit501.bootproject.service.MatchingService;
import com.busanit501.bootproject.service.PetService;
import com.busanit501.bootproject.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/matching")
public class MatchingController {

    private final MatchingService matchingService;
    private final PetService petService;
    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public MatchingController(MatchingService matchingService,
                              PetService petService,
                              UserRepository userRepository,
                              UserService userService) {
        this.matchingService = matchingService;
        this.petService = petService;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    /**
     * 매칭방 목록 페이지
     */
    @GetMapping("/list")
    public String list(Model model, HttpSession session) {
        User loginUser = getManagedLoginUser(session);
        if (loginUser == null) {
            return "redirect:/user/login";
        }
        List<MatchingRoom> rooms = matchingService.getAllRooms();
        model.addAttribute("rooms", rooms);
        return "matching/list";
    }

    /**
     * 매칭방 생성 폼 페이지
     */
    @GetMapping("/create")
    public String createForm(HttpSession session, Model model) {
        User loginUser = getManagedLoginUser(session);
        if (loginUser == null) {
            return "redirect:/user/login";
        }

        List<Pet> userPets = petService.findAllByUserId(loginUser.getUserId());
        model.addAttribute("userPets", userPets);

        MatchingRoomDTO dto = new MatchingRoomDTO();
        // 폼에 기본으로 들어갈 예시 값들
        dto.setMeetingDate(LocalDate.now());
        dto.setMeetingTime(LocalTime.now().withSecond(0).withNano(0));
        dto.setMaxParticipants(4L); // 기본값 4명
        model.addAttribute("matchingRoomDTO", dto);

        return "matching/create";
    }

    /**
     * 매칭방 생성 처리
     */
    @PostMapping("/create")
    public String createSubmit(@Valid @ModelAttribute("matchingRoomDTO") MatchingRoomDTO dto,
                               BindingResult bindingResult,
                               HttpSession session,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        User loginUser = getManagedLoginUser(session);
        if (loginUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/user/login";
        }

        if (bindingResult.hasErrors()) {
            // 유효성 에러 시 다시 폼으로
            List<Pet> userPets = petService.findAllByUserId(loginUser.getUserId());
            model.addAttribute("userPets", userPets);
            return "matching/create";
        }

        try {
            matchingService.createRoom(dto, loginUser);
            redirectAttributes.addFlashAttribute("successMessage", "매칭방이 성공적으로 생성되었습니다.");
            return "redirect:/matching/list";
        } catch (RuntimeException e) {
            // 오류 메시지
            List<Pet> userPets = petService.findAllByUserId(loginUser.getUserId());
            model.addAttribute("userPets", userPets);
            model.addAttribute("errorMessage", e.getMessage());
            return "matching/create";
        }
    }

    /**
     * 매칭방 상세 페이지
     */
    @GetMapping("/detail/{id}")
    public String getMatchingRoomDetail(@PathVariable("id") Long roomId,
                                        Model model,
                                        HttpSession session,
                                        RedirectAttributes redirectAttributes) {
        User loginUser = getManagedLoginUser(session);
        if (loginUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/user/login";
        }

        try {
            MatchingRoom room = matchingService.getRoomById(roomId);
            populateModelForDetail(model, room, loginUser);
            return "matching/detail";
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/matching/list";
        }
    }

    /**
     * 매칭방 수정 폼 페이지
     */

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Long roomId,
                           Model model,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {
        // 1) 로그인 여부 체크
        User loginUser = getManagedLoginUser(session);
        if (loginUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/user/login";
        }

        try {
            // 2) 해당 매칭방 찾기
            MatchingRoom room = matchingService.getRoomById(roomId);
            // 3) 방장만 수정할 수 있게 체크
            if (!room.getHost().getUserId().equals(loginUser.getUserId())) {
                redirectAttributes.addFlashAttribute("errorMessage", "방장만 수정할 수 있습니다.");
                return "redirect:/matching/detail/" + roomId;
            }
            // 4) MatchingRoom -> DTO 변환
            MatchingRoomDTO dto = matchingService.convertToDto(room);
            dto.setRoomId(roomId); // DTO에도 roomId 저장

            // 5) 사용자의 펫 목록 조회
            List<Pet> userPets = petService.findAllByUserId(loginUser.getUserId());

            // 6) View에 필요한 데이터 셋업
            model.addAttribute("matchingRoomDTO", dto);
            model.addAttribute("userPets", userPets);
            return "matching/edit"; // 수정 화면
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/matching/list";
        }
    }


    /**
     * 매칭방 수정 처리
     */
    @PostMapping("/edit/{id}")
    public String editSubmit(@PathVariable("id") Long roomId,
                             @Valid @ModelAttribute("matchingRoomDTO") MatchingRoomDTO dto,
                             BindingResult bindingResult,
                             HttpSession session,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        User loginUser = getManagedLoginUser(session);
        if (loginUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/user/login";
        }

        if (bindingResult.hasErrors()) {
            List<Pet> userPets = petService.findAllByUserId(loginUser.getUserId());
            model.addAttribute("userPets", userPets);
            return "matching/edit";
        }

        try {
            matchingService.updateRoom(roomId, dto, loginUser);
            redirectAttributes.addFlashAttribute("successMessage", "매칭방이 성공적으로 수정되었습니다.");
            return "redirect:/matching/detail/" + roomId;
        } catch (RuntimeException e) {
            List<Pet> userPets = petService.findAllByUserId(loginUser.getUserId());
            model.addAttribute("userPets", userPets);
            model.addAttribute("errorMessage", e.getMessage());
            return "matching/edit";
        }
    }

    /**
     * 참가 신청 처리
     */
    @PostMapping("/apply/{roomId}")
    public String applyRoom(@PathVariable("roomId") Long roomId,
                            @RequestParam(value = "additionalPetIds", required = false) List<Long> additionalPetIds,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        User loginUser = getManagedLoginUser(session);
        if (loginUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/user/login";
        }

        try {
            matchingService.applyRoom(roomId, loginUser.getUserId(), additionalPetIds);
            redirectAttributes.addFlashAttribute("successMessage", "참가 신청이 성공적으로 완료되었습니다.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/matching/detail/" + roomId;
    }

    /**
     * 참가자 승인
     */
    @PostMapping("/accept/{roomId}/{userId}")
    public String acceptParticipant(@PathVariable("roomId") Long roomId,
                                    @PathVariable("userId") Long userId,
                                    RedirectAttributes redirectAttributes) {
        try {
            matchingService.acceptParticipant(roomId, userId);
            redirectAttributes.addFlashAttribute("successMessage", "참가자가 승인되었습니다.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/matching/detail/" + roomId;
    }

    /**
     * 참가자 거절
     */
    @PostMapping("/reject/{roomId}/{userId}")
    public String rejectParticipant(@PathVariable("roomId") Long roomId,
                                    @PathVariable("userId") Long userId,
                                    RedirectAttributes redirectAttributes) {
        try {
            matchingService.rejectParticipant(roomId, userId);
            redirectAttributes.addFlashAttribute("successMessage", "참가자가 거절되었습니다.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/matching/detail/" + roomId;
    }

    /**
     * 세션에서 로그인된 사용자를 매니지드 엔티티로 재로드
     */
    private User getManagedLoginUser(HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return null;
        }
        return userRepository.findById(loginUser.getUserId()).orElse(null);
    }

    /**
     * 매칭방 상세 페이지에 필요한 데이터 세팅
     */
    private void populateModelForDetail(Model model, MatchingRoom room, User loginUser) {
        model.addAttribute("room", room);
        model.addAttribute("loginUser", loginUser);

        List<RoomParticipant> participants = matchingService.getParticipantsByRoomId(room.getRoomId());

        // 참가 상태별 분류
        List<RoomParticipant> pendingParticipants =
                matchingService.filterParticipants(participants, RoomParticipant.ParticipantStatus.Pending);
        List<RoomParticipant> acceptedParticipants =
                matchingService.filterParticipants(participants, RoomParticipant.ParticipantStatus.Accepted);

        model.addAttribute("pendingParticipants", pendingParticipants);
        model.addAttribute("acceptedParticipants", acceptedParticipants);

        boolean isHost = room.getHost().getUserId().equals(loginUser.getUserId());
        model.addAttribute("isHost", isHost);

        if (!isHost) {
            // 호스트가 아닌 경우, 사용자의 반려동물 목록 추가
            List<Pet> userPets = petService.findAllByUserId(loginUser.getUserId());
            model.addAttribute("userPets", userPets);
        }
    }
}
