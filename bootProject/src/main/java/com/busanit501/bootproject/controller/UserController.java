package com.busanit501.bootproject.controller;

import com.busanit501.bootproject.domain.Gallery;
import com.busanit501.bootproject.dto.UserDTO;
import com.busanit501.bootproject.repository.GalleryRepository;
import com.busanit501.bootproject.service.GalleryService;
import com.busanit501.bootproject.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@Controller
@RequestMapping("/profile")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private HttpSession session;
    @Autowired
    private GalleryService galleryService;
    @Autowired
    private GalleryRepository galleryRepository;


    @GetMapping("/gallery")
    public void profile(@AuthenticationPrincipal UserDetails user, Long userId, Model model) {
        List<Gallery> galleries = galleryRepository.findAll();
        // UserDTO userDTO = userService.getUserById(userId);
        //UserDTO userDTO = userService.getUserById(userId);

        model.addAttribute("user", user);
        model.addAttribute("galleries", galleries);
        // model.addAttribute("userDTO", userDTO);
        //log.info("userDTO : " + userDTO);
    }

    // 사용자 생성 (Create)
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.createUser(userDTO);
        return ResponseEntity.ok(createdUser);
    }

    // 사용자 조회 (Read)
    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long userId) {
        UserDTO user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    // 모든 사용자 조회 (Read)
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // 사용자 업데이트 (Update)
    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long userId, @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(userId, userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    // 사용자 삭제 (Delete)
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

}
